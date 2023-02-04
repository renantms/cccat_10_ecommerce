package br.com.cccat10.ecommerce.usecase.impl;

import br.com.cccat10.ecommerce.domain.Order;
import br.com.cccat10.ecommerce.repository.OrderRepository;
import br.com.cccat10.ecommerce.usecase.CreateOrderUseCase;
import br.com.cccat10.ecommerce.validator.CpfValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCaseImpl implements CreateOrderUseCase {

    private final CpfValidator cpfValidator;

    private final OrderRepository orderRepository;

    @Override
    public void execute(final Order order) {
        cpfValidator.validate(order.getBuyerCpf());

        order.getProductList().forEach(product -> product.setOrder(order));
        orderRepository.save(order);
    }

}
