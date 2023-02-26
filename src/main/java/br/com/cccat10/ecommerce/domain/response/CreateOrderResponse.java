package br.com.cccat10.ecommerce.domain.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateOrderResponse {

    private BigDecimal totalValue;

    private BigDecimal freightValue;

}
