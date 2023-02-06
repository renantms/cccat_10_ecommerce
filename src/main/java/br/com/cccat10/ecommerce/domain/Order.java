package br.com.cccat10.ecommerce.domain;

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

    public void addProduct(Product product, Long quantity) {
        OrderProduct orderProduct = new OrderProduct(this, product, quantity);
        productList.add(orderProduct);
    }

    public BigDecimal getOrderValue() {
        BigDecimal orderValue = BigDecimal.ZERO.setScale(2, RoundingMode.CEILING);
        for (OrderProduct product : productList) {
            orderValue = orderValue.add(product.getTotalValue());
        }
        if (coupon != null) {
            orderValue = orderValue.subtract(orderValue.multiply(coupon.getDiscountPercentage()))
                    .setScale(2, RoundingMode.CEILING);
        }
        return orderValue;
    }

}
