package com.bearsoft.pharmadepot.services.models;

import com.bearsoft.pharmadepot.exceptions.category.CategoryNotFoundException;
import com.bearsoft.pharmadepot.exceptions.product.ProductAlreadyExistsException;
import com.bearsoft.pharmadepot.models.domain.dtos.ProductDTO;
import com.bearsoft.pharmadepot.models.domain.entities.Category;
import com.bearsoft.pharmadepot.models.domain.entities.Product;
import com.bearsoft.pharmadepot.models.domain.enums.MedType;
import com.bearsoft.pharmadepot.repositories.CategoryRepository;
import com.bearsoft.pharmadepot.repositories.ProductRepository;
import com.bearsoft.pharmadepot.validators.ObjectsValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectsValidator<ProductDTO> productDTOObjectsValidator;

    @Transactional
    public ProductDTO addProduct(ProductDTO productDTO) {
        productDTOObjectsValidator.validate(productDTO);

        String productName = productDTO.getName();
        if (productRepository.existsByName(productName)) {
            throw new ProductAlreadyExistsException(String.format("Product %s already exists.", productName));
        }

        MedType medType = MedType.valueOf(productDTO.getCategory());
        Category category = categoryRepository.findByMedType(medType)
                .orElseThrow(
                        () -> new CategoryNotFoundException(String.format("Category should be one of these: ANALGESIC, ANTIBIOTIC, VITAMIN)", medType)));

        Product product = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .category(category)
                .build();

        productRepository.save(product);
        return productDTO;
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products
                .stream()
                .map(this::convertToProductDTO)
                .toList();
    }

    private ProductDTO convertToProductDTO(Product product) {
        return ProductDTO.builder()
                .name(product.getName())
                .price(product.getPrice())
                .category(product.getCategory().getMedType().name())
                .build();
    }
}
