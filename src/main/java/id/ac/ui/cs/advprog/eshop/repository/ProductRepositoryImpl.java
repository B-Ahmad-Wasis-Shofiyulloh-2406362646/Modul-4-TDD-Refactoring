package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final List<Product> productData = new ArrayList<>();

    @Override
    public Product create(Product product) {
        productData.add(product);
        return product;
    }

    @Override
    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    @Override
    public Product findById(String id) {
        return productData.stream()
                .filter(product -> product.getProductId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Product update(Product updatedProduct) {
        return productData.stream()
                .filter(product -> product.getProductId().equals(updatedProduct.getProductId()))
                .findFirst()
                .map(existingProduct -> {
                    existingProduct.updateFrom(updatedProduct);
                    return existingProduct;
                })
                .orElse(null);
    }

    @Override
    public void delete(String id) {
        productData.removeIf(p -> p.getProductId().equals(id));
    }
}