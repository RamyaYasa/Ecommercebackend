package com.example.cart.service;

import com.example.cart.dto.CartItemRequestDTO;
import com.example.cart.dto.CartItemResponseDTO;
import com.example.cart.dto.CartResponseDTO;
import com.example.cart.model.Cart;
import com.example.cart.model.CartItem;
import com.example.cart.model.Coupon;
import com.example.cart.model.CouponType;
import com.example.cart.repository.CartItemRepository;
import com.example.cart.repository.CartRepository;
import com.example.cart.repository.CouponRepository;
import com.example.cart.model.Wallet;
import com.example.cart.repository.WalletRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository itemRepository;
    private final CouponRepository couponRepository;
    private final WalletRepository walletRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository itemRepository,
                           CouponRepository couponRepository,
                           WalletRepository walletRepository) {
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
        this.couponRepository = couponRepository;
        this.walletRepository = walletRepository;
    }

    private double computeDeliveryCharge(double subtotal) {
        return subtotal < 500.0 ? 30.0 : 0.0;
    }

    private Cart getOrCreateCartForUser(String userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = Cart.builder()
                    .userId(userId)
                    .subtotal(0.0)
                    .deliveryCharge(0.0)
                    .discountAmount(0.0)
                    .totalPrice(0.0)
                    .build();
            return cartRepository.save(newCart);
        });
    }

    private void recalcCart(Cart cart) {
        double subtotal = cart.getItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();

        cart.setSubtotal(subtotal);

        double delivery = computeDeliveryCharge(subtotal);
        cart.setDeliveryCharge(delivery);

        double total = subtotal - cart.getDiscountAmount() + delivery;
        cart.setTotalPrice(total);
    }

    private CartResponseDTO toDto(Cart cart) {
        CartResponseDTO dto = new CartResponseDTO();
        dto.setCartId(cart.getId());
        dto.setUserId(cart.getUserId());
        dto.setItems(cart.getItems().stream()
                .map(this::toItemDto)
                .collect(Collectors.toList()));
        dto.setSubtotal(cart.getSubtotal());
        dto.setDeliveryCharge(cart.getDeliveryCharge());
        dto.setDiscountAmount(cart.getDiscountAmount());
        dto.setAppliedCoupon(cart.getAppliedCoupon());
        dto.setTotalPrice(cart.getTotalPrice());
        return dto;
    }

    private CartItemResponseDTO toItemDto(CartItem item) {
        CartItemResponseDTO dto = new CartItemResponseDTO();
        dto.setId(item.getId());
        dto.setPid(item.getPid());
        dto.setPname(item.getPname());
        dto.setDescription(item.getDescription());
        dto.setPrice(item.getPrice());
        dto.setActualPrice(item.getActualPrice());
        dto.setDiscount(item.getDiscount());
        dto.setStockQuantity(item.getStockQuantity());
        dto.setQuantity(item.getQuantity());
        dto.setTotalPrice(item.getTotalPrice());
        return dto;
    }

    // ----------------- CART OPERATIONS ----------------------

    @Override
    @Transactional
    public CartResponseDTO addToCart(String userId, CartItemRequestDTO request) {

        Cart cart = getOrCreateCartForUser(userId);

        Optional<CartItem> existing = cart.getItems().stream()
                .filter(i -> i.getPid().equals(request.getPid()))
                .findFirst();

        if (existing.isPresent()) {
            CartItem item = existing.get();
            int newQty = item.getQuantity() + request.getQuantity();
            if (newQty > item.getStockQuantity()) {
                throw new IllegalArgumentException("Requested quantity exceeds available stock");
            }
            item.setQuantity(newQty);
            item.recalcTotal();
            itemRepository.save(item);

        } else {
            if (request.getQuantity() > request.getStockQuantity()) {
                throw new IllegalArgumentException("Requested quantity exceeds available stock");
            }

            CartItem newItem = CartItem.builder()
                    .pid(request.getPid())
                    .pname(request.getPname())
                    .description(request.getDescription())
                    .price(request.getPrice())
                    .stockQuantity(request.getStockQuantity())
                    .actualPrice(request.getActualPrice() != null ? request.getActualPrice() : request.getPrice())
                    .discount(request.getDiscount() != null ? request.getDiscount() : 0)
                    .quantity(request.getQuantity())
                    .build();

            newItem.recalcTotal();
            cart.addItem(newItem);
            itemRepository.save(newItem);
        }

        recalcCart(cart);
        cartRepository.save(cart);

        return toDto(cart);
    }

    @Override
    @Transactional
    public CartResponseDTO updateQuantity(String userId, Long itemId, int newQuantity) {

        if (newQuantity <= 0) {
            throw new IllegalArgumentException("Quantity must be >= 1");
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user: " + userId));

        CartItem target = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found in cart: " + itemId));

        if (newQuantity > target.getStockQuantity()) {
            throw new IllegalArgumentException("Requested quantity exceeds available stock");
        }

        target.setQuantity(newQuantity);
        target.recalcTotal();
        itemRepository.save(target);

        recalcCart(cart);
        cartRepository.save(cart);

        return toDto(cart);
    }

    @Override
    @Transactional
    public CartResponseDTO deleteItem(String userId, Long itemId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user: " + userId));

        CartItem target = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found in cart: " + itemId));

        cart.removeItem(target);
        itemRepository.delete(target);

        recalcCart(cart);
        cartRepository.save(cart);

        return toDto(cart);
    }

    @Override
    @Transactional
    public CartResponseDTO clearCart(String userId) {

        Cart cart = getOrCreateCartForUser(userId);

        cart.getItems().forEach(itemRepository::delete);
        cart.clearItems();

        cart.setDiscountAmount(0);
        cart.setAppliedCoupon(null);

        recalcCart(cart);
        cartRepository.save(cart);

        return toDto(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponseDTO getCart(String userId) {

        Cart cart = getOrCreateCartForUser(userId);
        recalcCart(cart);
        cartRepository.save(cart);

        return toDto(cart);
    }

    // ----------------- COUPON SYSTEM ----------------------

    @Override
    @Transactional
    public CartResponseDTO applyCoupon(String userId, String couponCode) {

        Cart cart = getOrCreateCartForUser(userId);

        Coupon coupon = couponRepository.findById(couponCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid coupon code"));

        if (!coupon.isActive()) {
            throw new IllegalArgumentException("Coupon is not active");
        }

        double subtotal = cart.getSubtotal();
        double discount = 0;

        switch (coupon.getType()) {

            case PERCENTAGE:
                discount = subtotal * (coupon.getValue() / 100);
                if (discount > coupon.getMaxDiscount()) {
                    discount = coupon.getMaxDiscount();
                }
                break;

            case FLAT:
                discount = coupon.getValue();
                break;

            case FREESHIP:
                cart.setDeliveryCharge(0);
                discount = 0;
                break;
        }

        cart.setAppliedCoupon(couponCode);
        cart.setDiscountAmount(discount);

        double finalTotal = (subtotal - discount) + cart.getDeliveryCharge();
        cart.setTotalPrice(finalTotal);

        cartRepository.save(cart);
        return toDto(cart);
    }

    @Override
    @Transactional
    public CartResponseDTO removeCoupon(String userId) {

        Cart cart = getOrCreateCartForUser(userId);

        cart.setAppliedCoupon(null);
        cart.setDiscountAmount(0);

        recalcCart(cart);
        cartRepository.save(cart);

        return toDto(cart);
    }

    @Transactional
    public String checkout(String userId) {

        Cart cart = getOrCreateCartForUser(userId);

        double amountToPay = cart.getTotalPrice();

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));

        if (wallet.getBalance() < amountToPay) {
            throw new IllegalArgumentException("Insufficient wallet balance!");
        }

        wallet.setBalance(wallet.getBalance() - amountToPay);
        walletRepository.save(wallet);

        // after payment, clear cart
        cart.clearItems();
        cart.setSubtotal(0);
        cart.setDiscountAmount(0);
        cart.setAppliedCoupon(null);
        cart.setTotalPrice(0);
        cartRepository.save(cart);

        return "Payment successful! Order placed.";
    }

}
