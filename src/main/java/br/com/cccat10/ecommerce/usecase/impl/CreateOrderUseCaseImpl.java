package br.com.cccat10.ecommerce.usecase.impl;

import br.com.cccat10.ecommerce.domain.Coupon;
import br.com.cccat10.ecommerce.domain.Order;
import br.com.cccat10.ecommerce.domain.Product;
import br.com.cccat10.ecommerce.domain.dto.OrderValueDTO;
import br.com.cccat10.ecommerce.domain.dto.ProductDTO;
import br.com.cccat10.ecommerce.generator.OrderIdGenerator;
import br.com.cccat10.ecommerce.repository.CouponRepository;
import br.com.cccat10.ecommerce.repository.OrderRepository;
import br.com.cccat10.ecommerce.repository.ProductRepository;
import br.com.cccat10.ecommerce.usecase.CreateOrderUseCase;
import br.com.cccat10.ecommerce.validator.CpfValidator;
import br.com.cccat10.ecommerce.validator.ItemValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCaseImpl implements CreateOrderUseCase {

    private final CpfValidator cpfValidator;

    private final ItemValidator itemValidator;

    private final OrderIdGenerator orderIdGenerator;

    private final OrderRepository orderRepository;

    private final CouponRepository couponRepository;

    private final ProductRepository productRepository;

    @Override
    public OrderValueDTO execute(final Order order, final String couponName, final List<ProductDTO> products) {
        cpfValidator.validate(order.getBuyerCpf());
        itemValidator.validate(products);

        if (couponName != null) {
            Coupon coupon = couponRepository.findByCouponName(couponName);
            order.setCoupon(coupon);
        }
        products.forEach(
                product -> {
                    Product savedProduct = productRepository.findById(product.getId()).orElseThrow();
                    order.addProduct(savedProduct, product.getQuantity());
                }
        );
        OrderValueDTO orderValue = order.calculateOrderValue();
        order.setTotalValue(orderValue.getTotalValue());
        order.setFreightValue(orderValue.getFreightValue());
        order.setUniqueId(orderIdGenerator.generateUniqueId());

        orderRepository.save(order);

        return orderValue;
    }

}
