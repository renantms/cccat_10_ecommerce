package br.com.cccat10.ecommerce.usecase.impl;

import br.com.cccat10.ecommerce.domain.Coupon;
import br.com.cccat10.ecommerce.domain.Order;
import br.com.cccat10.ecommerce.repository.CouponRepository;
import br.com.cccat10.ecommerce.repository.OrderRepository;
import br.com.cccat10.ecommerce.usecase.CreateOrderUseCase;
import br.com.cccat10.ecommerce.validator.CpfValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCaseImpl implements CreateOrderUseCase {

    private final CpfValidator cpfValidator;

    private final OrderRepository orderRepository;

    private final CouponRepository couponRepository;

    @Override
    public BigDecimal execute(final Order order, String couponName) {
        cpfValidator.validate(order.getBuyerCpf());
        if (couponName != null) {
            Coupon coupon = couponRepository.findByName(couponName);
            order.setCoupon(coupon);
        }
        order.getProductList().forEach(product -> product.setOrder(order));
        orderRepository.save(order);

        return order.getOrderValue();
    }

}
