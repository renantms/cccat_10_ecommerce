package br.com.cccat10.ecommerce.repository;

import br.com.cccat10.ecommerce.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
