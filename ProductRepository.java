package Test.Tablle.repository;

import Test.Tablle.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository  extends JpaRepository<Product, Long> {

}
