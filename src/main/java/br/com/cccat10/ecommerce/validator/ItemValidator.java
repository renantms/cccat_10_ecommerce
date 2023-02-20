package br.com.cccat10.ecommerce.validator;

import br.com.cccat10.ecommerce.domain.dto.ProductDTO;

import java.util.List;

public interface ItemValidator {

    void validate(List<ProductDTO> productList);

}
