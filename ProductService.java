package Test.Tablle.service;

import Test.Tablle.entity.Product;
import Test.Tablle.exception.ProductNotFoundException;
import Test.Tablle.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product create(Product product) {

        validateProduct(product);

        return repository.save(product);
    }

    public List<Product> getAll() {

        return repository.findAll();
    }

    public Product getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product update(Long id, Product updatedProduct) {

        validateProduct(updatedProduct);

        Product existing = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        existing.setName(updatedProduct.getName());
        existing.setPrice(updatedProduct.getPrice());
        existing.setQuantity(updatedProduct.getQuantity());

        return repository.save(existing);
    }

    public void delete(Long id) {

        Product existing = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        repository.delete(existing);
    }

    private void validateProduct(Product product) {

        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException(
                    "Product name is required"
            );
        }

        if (product.getPrice() < 0) {
            throw new IllegalArgumentException(
                    "Price cannot be negative"
            );
        }

        if (product.getQuantity() < 0) {
            throw new IllegalArgumentException(
                    "Quantity cannot be negative"
            );
        }
    }
}
