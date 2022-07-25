package br.com.vexsistemas.catalog.services;

import br.com.vexsistemas.catalog.dto.CategoryDTO;
import br.com.vexsistemas.catalog.dto.ProductDTO;
import br.com.vexsistemas.catalog.dto.UriDTO;
import br.com.vexsistemas.catalog.entities.Category;
import br.com.vexsistemas.catalog.entities.Product;
import br.com.vexsistemas.catalog.repositories.CategoryRepository;
import br.com.vexsistemas.catalog.repositories.ProductRepository;
import br.com.vexsistemas.catalog.services.exceptions.DataBaseException;
import br.com.vexsistemas.catalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.net.URL;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private S3Service s3Service;


    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(Pageable pageable) {
        Page<Product> productsList = repository.findAll(pageable);
        return productsList.map(x -> new ProductDTO(x, x.getCategories()));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> obj = repository.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Produto não existe no banco de dados."));
        return new ProductDTO(entity, entity.getCategories());
    }
    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id da categoria não encontrado no banco de dados");
        }
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id da categoria não encontrado no banco de dados");
        }
        catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity Violation");
        }
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setImgUrl(dto.getImgUrl());
        entity.setPrice(dto.getPrice());

        entity.getCategories().clear();
        for (CategoryDTO catDto : dto.getCategories()) {
            Category category = categoryRepository.getReferenceById(catDto.getId());
            entity.getCategories().add(category);
        }
    }

    public UriDTO uploadFile(MultipartFile file) {
        URL url = s3Service.uploadFile(file);
        return new UriDTO(url.toString());
    }
}
