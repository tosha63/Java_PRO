package t1.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import t1.dto.ProductDto;
import t1.dto.UpdateProductDto;
import t1.mapper.ProductMapper;
import t1.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductDto createProduct(ProductDto productDto) {
        final var product = productMapper.mapToEntity(productDto);
        final var saveProduct = productRepository.save(product);
        return productMapper.mapToDto(saveProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public ProductDto getProduct(Long id) {
        return productRepository.findById(id)
                                .map(productMapper::mapToDto)
                                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll()
                                .stream()
                                .map(productMapper::mapToDto)
                                .toList();
    }

    public List<ProductDto> getAllProductsByUserId(Long userId) {
        return productRepository.findAllByUserId(userId)
                                .stream()
                                .map(productMapper::mapToDto)
                                .toList();
    }

    public ProductDto getProductByIdAndUserId(Long productId, Long userId) {
        return productRepository.findByIdAndUserId(productId, userId)
                                .map(productMapper::mapToDto)
                                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public ProductDto updateProduct(Long id, UpdateProductDto updateProductDto) {
        final var entity = productRepository.findById(id)
                                            .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        final var updateProduct = productMapper.map(entity, updateProductDto);
        productRepository.save(updateProduct);
        return productMapper.mapToDto(updateProduct);
    }
}
