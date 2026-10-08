package Test.Tablle;

import Test.Tablle.entity.Product;
import Test.Tablle.exception.ProductNotFoundException;
import Test.Tablle.repository.ProductRepository;
import Test.Tablle.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    @Test
    void getById_shouldReturnProduct() {

        Product product =
                new Product(1L, "Keyboard", 1200, 5);

        when(repository.findById(1L))
                .thenReturn(Optional.of(product));

        Product result = service.getById(1L);

        assertEquals("Keyboard", result.getName());
        assertEquals(1200, result.getPrice());
    }

    @Test
    void getById_shouldThrowExceptionWhenMissing() {

        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> service.getById(99L)
        );
    }

    @Test
    void update_shouldModifyExistingProduct() {

        Product existing =
                new Product(1L, "Old Name", 100, 2);

        Product updated =
                new Product("New Name", 200, 5);

        when(repository.findById(1L))
                .thenReturn(Optional.of(existing));

        when(repository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Product result =
                service.update(1L, updated);

        assertEquals("New Name", result.getName());
        assertEquals(200, result.getPrice());
        assertEquals(5, result.getQuantity());

        verify(repository).save(existing);
    }

    @Test
    void delete_shouldThrowExceptionWhenMissing() {

        when(repository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> service.delete(10L)
        );

        verify(repository, never())
                .delete(any(Product.class));
    }

    @Test
    void create_shouldRejectNegativePrice() {

        Product product =
                new Product("Mouse", -10, 2);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.create(product)
        );

        verify(repository, never())
                .save(any(Product.class));
    }
}
