package br.com.cccat10.ecommerce.usecase.impl;

import br.com.cccat10.ecommerce.domain.Order;
import br.com.cccat10.ecommerce.repository.OrderRepository;
import br.com.cccat10.ecommerce.usecase.RetrieveOrderUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RetrieveOrderUseCaseImpl implements RetrieveOrderUseCase {

    private final OrderRepository orderRepository;

    @Override
    public Order execute(Long id) {
        return orderRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

}
