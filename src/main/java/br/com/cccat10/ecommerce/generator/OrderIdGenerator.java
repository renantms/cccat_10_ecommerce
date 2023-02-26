package br.com.cccat10.ecommerce.generator;

import br.com.cccat10.ecommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OrderIdGenerator {

    private final OrderRepository orderRepository;

    private static final int MAX_NUMBER_OF_ZEROS = 8;

    public String generateUniqueId() {
        String orderNewId = String.valueOf(orderRepository.count() + 1);
        int year = LocalDate.now().getYear();
        String uniqueId = StringUtils.leftPad(orderNewId, MAX_NUMBER_OF_ZEROS, "0");

        return year + uniqueId;
    }

}
