package br.com.cccat10.ecommerce.usecase;

import br.com.cccat10.ecommerce.domain.Order;

public interface RetrieveOrderUseCase {

    Order execute(Long id);

}
