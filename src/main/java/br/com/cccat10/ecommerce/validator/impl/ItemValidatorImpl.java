package br.com.cccat10.ecommerce.validator.impl;

import br.com.cccat10.ecommerce.domain.dto.ProductDTO;
import br.com.cccat10.ecommerce.exception.DuplicatedItemException;
import br.com.cccat10.ecommerce.exception.InvalidQuantityException;
import br.com.cccat10.ecommerce.validator.ItemValidator;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ItemValidatorImpl implements ItemValidator {

    @Override
    public void validate(List<ProductDTO> productList) {
        boolean isInvalidQuantity = productList.stream().anyMatch(this::isInvalidQuantity);

        if (isInvalidQuantity) {
            throw new InvalidQuantityException();
        }
        Set<ProductDTO> productDTOSet = new HashSet<>(productList);
        if (productDTOSet.size() != productList.size()) {
            throw new DuplicatedItemException();
        }

    }

    private boolean isInvalidQuantity(ProductDTO productDTO) {
        return productDTO.getQuantity() <= 0L;
    }
}
