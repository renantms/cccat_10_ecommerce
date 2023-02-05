package br.com.cccat10.ecommerce.usecase.impl;

import br.com.cccat10.ecommerce.domain.Coupon;
import br.com.cccat10.ecommerce.domain.Order;
import br.com.cccat10.ecommerce.domain.Product;
import br.com.cccat10.ecommerce.repository.CouponRepository;
import br.com.cccat10.ecommerce.repository.OrderRepository;
import br.com.cccat10.ecommerce.usecase.CreateOrderUseCase;
import br.com.cccat10.ecommerce.validator.CpfValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CreateOrderUseCaseImplTest {

    CpfValidator cpfValidator = Mockito.mock(CpfValidator.class);

    OrderRepository orderRepository = Mockito.mock(OrderRepository.class);

    CouponRepository couponRepository = Mockito.mock(CouponRepository.class);

    CreateOrderUseCase createOrderUseCase = new CreateOrderUseCaseImpl(cpfValidator, orderRepository, couponRepository);


    @Test
    void shouldCreateOrderAndReturnTotalValue() {
        Order order = createOrder();

        BigDecimal totalValue = createOrderUseCase.execute(order, null);

        Assertions.assertAll(
                () -> Assertions.assertEquals(new BigDecimal("72.63"), totalValue),
                () -> Mockito.verify(cpfValidator, Mockito.times(1)).validate(order.getBuyerCpf()),
                () -> Mockito.verify(couponRepository, Mockito.times(0)).findByName(Mockito.any()),
                () -> Mockito.verify(orderRepository, Mockito.times(1)).save(Mockito.any())
        );
    }

    @Test
    void shouldCreateOrderWithCouponAndReturnTotalValue() {
        Order order = createOrder();
        Coupon coupon = createCoupon();
        Mockito.when(couponRepository.findByName(coupon.getCouponName())).thenReturn(coupon);

        BigDecimal totalValue = createOrderUseCase.execute(order, coupon.getCouponName());

        Assertions.assertAll(
                () -> Assertions.assertEquals(new BigDecimal("58.11"), totalValue),
                () -> Mockito.verify(cpfValidator, Mockito.times(1)).validate(order.getBuyerCpf()),
                () -> Mockito.verify(couponRepository, Mockito.times(1)).findByName(coupon.getCouponName()),
                () -> Mockito.verify(orderRepository, Mockito.times(1)).save(Mockito.any())
        );
    }

    private Coupon createCoupon() {
        return Coupon.builder()
                .couponName("VALE20")
                .discountPercentage(new BigDecimal("0.20"))
                .build();
    }

    Order createOrder() {
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            productList.add(createProduct());
        }
        return Order.builder()
                .buyerCpf("11144477735")
                .productList(productList)
                .build();
    }

    Product createProduct() {
        return Product.builder()
                .quantity(5)
                .price(new BigDecimal("24.21"))
                .description("AAAAAAAAAAAA")
                .build();

    }

}
