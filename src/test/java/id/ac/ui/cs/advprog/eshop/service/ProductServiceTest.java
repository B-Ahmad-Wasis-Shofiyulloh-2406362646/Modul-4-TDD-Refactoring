package id.ac.ui.cs.advprog.eshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    void testCreate() {
        Product product = new Product();
        productService.create(product);
        verify(productRepository, times(1)).create(product);
    }

    @Test
    void testFindAll() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product());
        
        when(productRepository.findAll()).thenReturn(productList.iterator());

        List<Product> allProducts = productService.findAll();

        assertNotNull(allProducts);
        assertEquals(1, allProducts.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        String id = "some-id";
        productService.findById(id);
        verify(productRepository, times(1)).findById(id);
    }

    @Test
    void testUpdate() {
        Product product = new Product();
        productService.update(product);
        verify(productRepository, times(1)).update(product);
    }

    @Test
    void testDelete() {
        String id = "some-id";
        productService.delete(id);
        verify(productRepository, times(1)).delete(id);
    }
}