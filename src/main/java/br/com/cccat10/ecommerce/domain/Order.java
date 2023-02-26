package br.com.cccat10.ecommerce.domain;

import br.com.cccat10.ecommerce.domain.dto.OrderValueDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"order\"")
public class Order {

    @Id
    @Column(name = "id_order")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(name = "buyer_cpf")
    private String buyerCpf;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> productList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_coupon")
    private Coupon coupon;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @CreationTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "total_value", precision = 8, scale = 2)
    private BigDecimal totalValue;

    @Column(name = "freight_value", precision = 8, scale = 2)
    private BigDecimal freightValue;

    @Column(name = "unique_id")
    private String uniqueId;

    public void addProduct(Product product, Long quantity) {
        OrderProduct orderProduct = new OrderProduct(this, product, quantity);
        productList.add(orderProduct);
    }

    public OrderValueDTO calculateOrderValue() {
        BigDecimal orderValue = BigDecimal.ZERO.setScale(2, RoundingMode.CEILING);
        BigDecimal freightValue = BigDecimal.ZERO.setScale(2, RoundingMode.CEILING);
        for (OrderProduct product : productList) {
            orderValue = orderValue.add(product.getTotalValue());
            freightValue = freightValue.add(product.getFreightValue());
        }
        if (coupon != null && LocalDateTime.now().isBefore(coupon.getExpireDate())) {
            orderValue = orderValue.subtract(orderValue.multiply(coupon.getDiscountPercentage()))
                    .setScale(2, RoundingMode.CEILING);
        }
        OrderValueDTO orderValueDTO = new OrderValueDTO();
        orderValueDTO.setTotalValue(orderValue);
        orderValueDTO.setFreightValue(freightValue);
        return orderValueDTO;
    }

}
