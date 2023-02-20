package br.com.cccat10.ecommerce.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderTest {
    private static final BigDecimal EXPECTED_VALUE_WITHOUT_DISCOUNT = new BigDecimal("228.75");

    private static final BigDecimal EXPECTED_VALUE_WITH_MINIMUM_FREIGHT = new BigDecimal("128.75");

    private static final BigDecimal EXPECTED_VALUE_WITH_DISCOUNT = new BigDecimal("148.69");

    @Test
    void shouldCalculateOrderValue() {
        List<OrderProduct> productList = new ArrayList<>();
        productList.add(createOrderProduct());

        Order order = Order.builder()
                .buyerCpf("11144477735")
                .productList(productList)
                .build();

        Assertions.assertEquals(EXPECTED_VALUE_WITHOUT_DISCOUNT, order.getOrderValue());
    }

    @Test
    void shouldCalculateOrderValueWithDiscount() {
        List<OrderProduct> productList = new ArrayList<>();
        productList.add(createOrderProduct());

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
        productList.add(createOrderProduct());

        Order order = Order.builder()
                .buyerCpf("11144477735")
                .productList(productList)
                .coupon(createExpiredCoupon())
                .build();

        Assertions.assertEquals(EXPECTED_VALUE_WITHOUT_DISCOUNT, order.getOrderValue());
    }

    @Test
    void shouldCalculateOrderValueWithMinimumFreight() {
        List<OrderProduct> productList = new ArrayList<>();
        productList.add(createOrderProductWithMinimumFreight());

        Order order = Order.builder()
                .buyerCpf("11144477735")
                .productList(productList)
                .build();

        Assertions.assertEquals(EXPECTED_VALUE_WITH_MINIMUM_FREIGHT, order.getOrderValue());
    }

    Product createProduct() {
        return Product.builder()
                .price(new BigDecimal("15.75"))
                .description("AAAAAAAAAAAA")
                .height(100L)
                .width(30L)
                .length(10L)
                .weight(new BigDecimal("3"))
                .build();

    }

    Product createProductWithMinimumFreight() {
        Product product = createProduct();
        product.setHeight(15L);
        product.setWidth(15L);
        product.setLength(10L);
        product.setWeight(new BigDecimal("0.5"));

        return product;
    }

    OrderProduct createOrderProduct() {
        OrderProduct orderProduct = new OrderProduct();

        orderProduct.setProduct(createProduct());
        orderProduct.setQuantity(5L);
        return orderProduct;
    }

    OrderProduct createOrderProductWithMinimumFreight() {
        OrderProduct orderProduct = new OrderProduct();

        orderProduct.setProduct(createProductWithMinimumFreight());
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
