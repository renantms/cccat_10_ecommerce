package br.com.cccat10.ecommerce.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderTest {

    private static final BigDecimal EXPECTED_VALUE_WITHOUT_DISCOUNT = new BigDecimal("47.25");

    private static final BigDecimal EXPECTED_VALUE_WITH_DISCOUNT = new BigDecimal("30.72");

    @Test
    void shouldCalculateOrderValue() {
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            productList.add(createProduct());
        }
        Order order = Order.builder()
                .buyerCpf("11144477735")
                .productList(productList)
                .build();

        Assertions.assertEquals(EXPECTED_VALUE_WITHOUT_DISCOUNT, order.getOrderValue());
    }

    @Test
    void shouldCalculateOrderValueWithDiscount() {
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            productList.add(createProduct());
        }
        Order order = Order.builder()
                .buyerCpf("11144477735")
                .productList(productList)
                .coupon(createCoupon())
                .build();

        Assertions.assertEquals(EXPECTED_VALUE_WITH_DISCOUNT, order.getOrderValue());
    }

    Product createProduct() {
        return Product.builder()
                .quantity(5)
                .price(new BigDecimal("15.75"))
                .description("AAAAAAAAAAAA")
                .build();

    }

    private Coupon createCoupon() {
        return Coupon.builder()
                .couponName("VALE35")
                .discountPercentage(new BigDecimal("0.35"))
                .build();
    }


}
