package br.com.cccat10.ecommerce.usecase.impl;

import br.com.cccat10.ecommerce.domain.Coupon;
import br.com.cccat10.ecommerce.domain.Order;
import br.com.cccat10.ecommerce.domain.Product;
import br.com.cccat10.ecommerce.domain.dto.ProductDTO;
import br.com.cccat10.ecommerce.repository.CouponRepository;
import br.com.cccat10.ecommerce.repository.OrderRepository;
import br.com.cccat10.ecommerce.repository.ProductRepository;
import br.com.cccat10.ecommerce.usecase.CreateOrderUseCase;
import br.com.cccat10.ecommerce.validator.CpfValidator;
import br.com.cccat10.ecommerce.validator.ItemValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CreateOrderUseCaseImplTest {

    CpfValidator cpfValidator = Mockito.mock(CpfValidator.class);

    ItemValidator itemValidator = Mockito.mock(ItemValidator.class);

    OrderIdGenerator orderIdGenerator = Mockito.mock(OrderIdGenerator.class);

    OrderRepository orderRepository = Mockito.mock(OrderRepository.class);

    CouponRepository couponRepository = Mockito.mock(CouponRepository.class);

    ProductRepository productRepository = Mockito.mock(ProductRepository.class);

    CreateOrderUseCase createOrderUseCase = new CreateOrderUseCaseImpl(cpfValidator, itemValidator, orderIdGenerator,
            orderRepository, couponRepository, productRepository);


    @Test
    void shouldCreateOrderAndReturnTotalValue() {
        Order order = createOrder();
        ProductDTO productDTO = createProductDTO();
        Product product = createProduct();

        Mockito.when(productRepository.findById(productDTO.getId())).thenReturn(Optional.of(product));

        BigDecimal totalValue = createOrderUseCase.execute(order, null, List.of(productDTO));

        Assertions.assertAll(
                () -> Assertions.assertEquals(new BigDecimal("271.05"), totalValue),
                () -> Mockito.verify(cpfValidator, Mockito.times(1)).validate(order.getBuyerCpf()),
                () -> Mockito.verify(couponRepository, Mockito.times(0)).findByCouponName(Mockito.any()),
                () -> Mockito.verify(orderRepository, Mockito.times(1)).save(Mockito.any())
        );
    }

    @Test
    void shouldCreateOrderWithCouponAndReturnTotalValue() {
        Order order = createOrder();
        Coupon coupon = createCoupon();
        ProductDTO productDTO = createProductDTO();
        Product product = createProduct();

        Mockito.when(productRepository.findById(productDTO.getId())).thenReturn(Optional.of(product));
        Mockito.when(couponRepository.findByCouponName(coupon.getCouponName())).thenReturn(coupon);

        BigDecimal totalValue = createOrderUseCase.execute(order, coupon.getCouponName(), List.of(productDTO));

        Assertions.assertAll(
                () -> Assertions.assertEquals(new BigDecimal("216.84"), totalValue),
                () -> Mockito.verify(cpfValidator, Mockito.times(1)).validate(order.getBuyerCpf()),
                () -> Mockito.verify(couponRepository, Mockito.times(1)).findByCouponName(coupon.getCouponName()),
                () -> Mockito.verify(orderRepository, Mockito.times(1)).save(Mockito.any())
        );
    }

    private Coupon createCoupon() {
        return Coupon.builder()
                .couponName("VALE20")
                .discountPercentage(new BigDecimal("0.20"))
                .expireDate(LocalDateTime.now().plusDays(1L))
                .build();
    }

    Order createOrder() {
        return Order.builder()
                .buyerCpf("11144477735")
                .productList(new ArrayList<>())
                .build();
    }

    Product createProduct() {
        return Product.builder()
                .price(new BigDecimal("24.21"))
                .description("AAAAAAAAAAAA")
                .height(100L)
                .width(30L)
                .length(10L)
                .weight(new BigDecimal("3"))
                .build();

    }

    ProductDTO createProductDTO() {
        ProductDTO productRequest = new ProductDTO();
        productRequest.setId(1L);
        productRequest.setQuantity(5L);
        return productRequest;
    }

}
