package br.com.cccat10.ecommerce.usecase;

import br.com.cccat10.ecommerce.domain.Order;

import java.math.BigDecimal;

public interface CreateOrderUseCase {

    BigDecimal execute(Order order, String couponName);

}
