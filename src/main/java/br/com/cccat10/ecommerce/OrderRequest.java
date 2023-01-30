package br.com.cccat10.ecommerce;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderRequest {

    private String buyerCpf;

    private List<ProductRequest> productList;

    private BigDecimal discountPercentage;

}
