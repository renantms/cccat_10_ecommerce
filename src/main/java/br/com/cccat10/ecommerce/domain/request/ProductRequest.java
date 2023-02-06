package br.com.cccat10.ecommerce.domain.request;

import br.com.cccat10.ecommerce.domain.dto.ProductDTO;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductRequest {

    private Long id;

    private Long quantity;

    public ProductDTO toDTO() {
        ProductDTO productDTO = new ProductDTO();

        productDTO.setId(id);
        productDTO.setQuantity(quantity);
        return productDTO;
    }

}
