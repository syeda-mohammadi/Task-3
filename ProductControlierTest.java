package Test.Tablle;

import Test.Tablle.controller.ProductController;
import Test.Tablle.entity.Product;
import Test.Tablle.exception.GlobalExceptionHandler;
import Test.Tablle.exception.ProductNotFoundException;
import Test.Tablle.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static java.lang.reflect.Array.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import(GlobalExceptionHandler.class)
public class ProductControlierTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Test
    void getById_shouldReturn200() throws Exception {

        when(service.getById(1L))
                .thenReturn(
                        new Product(
                                1L,
                                "Keyboard",
                                1200,
                                5
                        )
                );

        mockMvc.perform(
                        (org.springframework.test.web.servlet.RequestBuilder) get("/api/products/1")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.name")
                                .value("Keyboard")
                )
                .andExpect(
                        jsonPath("$.price")
                                .value(1200)
                );
    }

    @Test
    void getById_shouldReturn404WhenMissing()
            throws Exception {

        when(service.getById(99L))
                .thenThrow(
                        new ProductNotFoundException(99L)
                );

        mockMvc.perform(
                        (org.springframework.test.web.servlet.RequestBuilder) get("/api/products/99")
                )
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.status")
                                .value(404)
                )
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Product not found with id: 99"
                                )
                );
    }

    private Object get(String path) {
    }
}

