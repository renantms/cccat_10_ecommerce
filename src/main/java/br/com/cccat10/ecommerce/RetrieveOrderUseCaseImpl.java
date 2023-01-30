package br.com.cccat10.ecommerce;

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
