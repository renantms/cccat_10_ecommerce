package br.com.cccat10.ecommerce.domain.response;

import br.com.cccat10.ecommerce.domain.response.ProductResponse;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderResponse {

    private String buyerCpf;

    private List<ProductResponse> productList;

    private LocalDateTime orderDate;

    private String couponName;

    private BigDecimal orderValue;

}
