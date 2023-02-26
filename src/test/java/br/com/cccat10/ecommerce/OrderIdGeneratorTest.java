package br.com.cccat10.ecommerce;

import br.com.cccat10.ecommerce.generator.OrderIdGenerator;
import br.com.cccat10.ecommerce.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.time.LocalDate;

public class OrderIdGeneratorTest {

    private static final int YEAR = LocalDate.now().getYear();

    private static final String ID_MASK = "00000000";

    OrderRepository orderRepository = Mockito.mock(OrderRepository .class);

    OrderIdGenerator orderIdGenerator = new OrderIdGenerator(orderRepository);

    @ParameterizedTest
    @ValueSource(longs = {
            1,
            10,
            100,
            1000,
            10000,
            100000,
            1000000,
            10000000
    })
    void shouldGenerateId(long value) {
        Mockito.when(orderRepository.count()).thenReturn(value);

        String expectedId = YEAR + ID_MASK.substring(0, ID_MASK.length() - String.valueOf(value).length()) + (value + 1);

        Assertions.assertEquals(expectedId, orderIdGenerator.generateUniqueId());
    }

}
