package br.com.vexsistemas.catalog.repositories;

import br.com.vexsistemas.catalog.entities.Product;
import br.com.vexsistemas.catalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repository;

    private long existingId;
    private long nomExistingId;
    private long countTotalProduct;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nomExistingId = 10000L;
        countTotalProduct = 25;
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
        Product product = Factory.createProduct();
        product.setId(null);
        product = repository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProduct + 1, product.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        repository.deleteById(existingId);

        Optional<Product> result = repository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void deleteShouldTrhowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            repository.deleteById(nomExistingId);
        });
    }

    @Test
    public void findByIdShouldProductNotEmptyWhenIdExists() {
        Optional<Product> result = repository.findById(existingId);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void findByIdShouldProductNotEmptyWhenIdNomExists() {
        Optional<Product> result = repository.findById(nomExistingId);
        Assertions.assertFalse(result.isPresent());
    }
}
