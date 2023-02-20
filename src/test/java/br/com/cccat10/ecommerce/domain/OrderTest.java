package br.com.cccat10.ecommerce.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderTest {

    private static final BigDecimal EXPECTED_VALUE_WITHOUT_DISCOUNT = new BigDecimal("236.25");

    private static final BigDecimal EXPECTED_VALUE_WITH_DISCOUNT = new BigDecimal("153.57");

    @Test
    void shouldCalculateOrderValue() {
        List<OrderProduct> productList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            productList.add(createOrderProduct());
        }
        Order order = Order.builder()
                .buyerCpf("11144477735")
                .productList(productList)
                .build();

        Assertions.assertEquals(EXPECTED_VALUE_WITHOUT_DISCOUNT, order.getOrderValue());
    }

    @Test
    void shouldCalculateOrderValueWithDiscount() {
        List<OrderProduct> productList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            productList.add(createOrderProduct());
        }
        Order order = Order.builder()
                .buyerCpf("11144477735")
                .productList(productList)
                .coupon(createCoupon())
                .build();

        Assertions.assertEquals(EXPECTED_VALUE_WITH_DISCOUNT, order.getOrderValue());
    }

    @Test
    void shouldCalculateOrderValueWithDiscountExpired() {
        List<OrderProduct> productList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            productList.add(createOrderProduct());
        }
        Order order = Order.builder()
                .buyerCpf("11144477735")
                .productList(productList)
                .coupon(createExpiredCoupon())
                .build();

        Assertions.assertEquals(EXPECTED_VALUE_WITHOUT_DISCOUNT, order.getOrderValue());
    }

    Product createProduct() {
        return Product.builder()
                .price(new BigDecimal("15.75"))
                .description("AAAAAAAAAAAA")
                .build();

    }

    OrderProduct createOrderProduct() {
        OrderProduct orderProduct = new OrderProduct();

        orderProduct.setProduct(createProduct());
        orderProduct.setQuantity(5L);
        return orderProduct;
    }

    private Coupon createCoupon() {
        return Coupon.builder()
                .couponName("VALE35")
                .discountPercentage(new BigDecimal("0.35"))
                .expireDate(LocalDateTime.now().plusDays(1L))
                .build();
    }

    private Coupon createExpiredCoupon() {
        Coupon expiredCoupon = createCoupon();
        expiredCoupon.setExpireDate(LocalDateTime.now().minusDays(1L));
        return expiredCoupon;
    }


}
