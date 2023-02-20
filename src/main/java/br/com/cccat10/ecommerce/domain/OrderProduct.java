package br.com.cccat10.ecommerce.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Entity
@NoArgsConstructor
@Table(name = "order_product")
public class OrderProduct {

    @EmbeddedId
    private OrderProductId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    private Product product;

    @Column(name = "quantity")
    private Long quantity;

    public OrderProduct(Order order, Product product, Long quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.id = new OrderProductId(order.getOrderId(), product.getId());
    }

    public BigDecimal getTotalValue() {
        return product.getPrice().multiply(new BigDecimal(quantity)).add(getFreightValue());
    }

    public BigDecimal getFreightValue() {
        return product.getFreightValue().multiply(new BigDecimal(quantity)).setScale(2, RoundingMode.CEILING);
    }

}
