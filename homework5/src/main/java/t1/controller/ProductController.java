package t1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import t1.dto.ProductDto;
import t1.dto.UpdateProductDto;
import t1.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/v1/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("{id}")
    public ProductDto getProduct(@PathVariable("id") Long productId) {
        return productService.getProduct(productId);
    }

    @GetMapping("/all/{userId}")
    public List<ProductDto> getAllProductByUserId(@PathVariable("userId") Long userId) {
        return productService.getAllProductsByUserId(userId);
    }

    @PostMapping
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @PutMapping("{id}")
    public ProductDto updateProduct(@PathVariable("id") Long productId, @RequestBody UpdateProductDto updateProductDto) {
        return productService.updateProduct(productId, updateProductDto);
    }

    @DeleteMapping("{id}")
    public String deleteProduct(@PathVariable("id") Long productId) {
        productService.deleteProduct(productId);
        return "Product deleted successfully";
    }
}
