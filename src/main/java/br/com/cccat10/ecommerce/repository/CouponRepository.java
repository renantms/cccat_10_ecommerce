package br.com.cccat10.ecommerce.repository;

import br.com.cccat10.ecommerce.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Long, Coupon> {

    Coupon findByName(String couponName);

}
