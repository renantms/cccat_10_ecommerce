package br.com.cccat10.ecommerce.usecase;

import br.com.cccat10.ecommerce.domain.Order;
import br.com.cccat10.ecommerce.domain.dto.ProductDTO;

import java.math.BigDecimal;
import java.util.List;

public interface CreateOrderUseCase {

    BigDecimal execute(Order order, String couponName, List<ProductDTO> products);

}
