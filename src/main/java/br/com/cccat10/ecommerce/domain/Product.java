package br.com.cccat10.ecommerce.domain;

import br.com.cccat10.ecommerce.domain.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @Column(name = "id_product")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "height")
    private Long height;

    @Column(name = "width")
    private Long width;

    @Column(name = "length")
    private Long length;

    @Column(name = "weight", precision = 6, scale = 2)
    private BigDecimal weight;

    private static final BigDecimal MIN_FREIGHT_VALUE = BigDecimal.TEN;
    private static final BigDecimal DISTANCE = new BigDecimal("1000");

    public BigDecimal getFreightValue() {
        final var weightScaled = weight.setScale(6, RoundingMode.CEILING);

        final var volume = BigDecimal.valueOf(calculateVolume());
        final var density = weightScaled.divide(volume, RoundingMode.CEILING).setScale(6, RoundingMode.CEILING);
        final var freightPrice = density.divide(new BigDecimal(100), RoundingMode.CEILING).multiply(volume).multiply(DISTANCE);

        return MIN_FREIGHT_VALUE.max(freightPrice);
    }

    private Double calculateVolume() {
        return (height / 100D) * (width / 100D) * (length / 100D);
    }
}
