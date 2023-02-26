package br.com.cccat10.ecommerce.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderValueDTO {

    private BigDecimal totalValue;

    private BigDecimal freightValue;

}
