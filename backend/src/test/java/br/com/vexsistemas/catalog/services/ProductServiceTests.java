package br.com.vexsistemas.catalog.services;

import br.com.vexsistemas.catalog.dto.ProductDTO;
import br.com.vexsistemas.catalog.entities.Product;
import br.com.vexsistemas.catalog.repositories.ProductRepository;
import br.com.vexsistemas.catalog.services.exceptions.DataBaseException;
import br.com.vexsistemas.catalog.services.exceptions.ResourceNotFoundException;
import br.com.vexsistemas.catalog.tests.Factory;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;
    @Mock
    private ProductRepository repository;

    private long existingId;
    private long nomExistingId;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nomExistingId = 2L;
        dependentId = 3L;
        product = Factory.createProduct();
        page = new PageImpl<>(List.of(product));

        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(nomExistingId)).thenReturn(Optional.empty());
        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nomExistingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {service.delete(existingId);});
        Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {service.delete(nomExistingId);});
        Mockito.verify(repository, Mockito.times(1)).deleteById(nomExistingId);
    }

    @Test
    public void deleteShouldThrowDataBaseExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(DataBaseException.class, () -> {service.delete(dependentId);});
        Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
    }

    @Test
    public void findAllPagedShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> result = service.findAllPaged(pageable);
        Assertions.assertNotNull(result);
        Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
    }
}
