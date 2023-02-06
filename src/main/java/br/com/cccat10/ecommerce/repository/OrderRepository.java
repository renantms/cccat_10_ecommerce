package br.com.cccat10.ecommerce.repository;

import br.com.cccat10.ecommerce.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
