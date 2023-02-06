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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCaseImpl implements CreateOrderUseCase {

    private final CpfValidator cpfValidator;

    private final OrderRepository orderRepository;

    private final CouponRepository couponRepository;

    private final ProductRepository productRepository;

    @Override
    public BigDecimal execute(final Order order, final String couponName, final List<ProductDTO> products) {
        cpfValidator.validate(order.getBuyerCpf());
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
        orderRepository.save(order);

        return order.getOrderValue();
    }

}
