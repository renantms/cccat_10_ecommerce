package br.com.cccat10.ecommerce.usecase;

import br.com.cccat10.ecommerce.domain.Order;
import br.com.cccat10.ecommerce.domain.dto.OrderValueDTO;
import br.com.cccat10.ecommerce.domain.dto.ProductDTO;

import java.util.List;

public interface CreateOrderUseCase {

    OrderValueDTO execute(Order order, String couponName, List<ProductDTO> products);

}
