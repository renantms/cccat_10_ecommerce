package br.com.cccat10.ecommerce.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupon")
public class Coupon {

    @Id
    @Column(name = "id_coupon")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    @Column(name = "coupon_name")
    private String couponName;

    @Column(name = "discount_percentage", precision = 5, scale = 4)
    private BigDecimal discountPercentage;

    @Column(name = "expire_date")
    private LocalDateTime expireDate;

}
