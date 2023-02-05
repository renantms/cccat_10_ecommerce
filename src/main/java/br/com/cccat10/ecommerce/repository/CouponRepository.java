package br.com.cccat10.ecommerce.repository;

import br.com.cccat10.ecommerce.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Coupon findByCouponName(String couponName);

}
