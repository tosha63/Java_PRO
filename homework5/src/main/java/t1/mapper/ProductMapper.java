package t1.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import t1.dto.ProductDto;
import t1.dto.UpdateProductDto;
import t1.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "user.id", source = "userId")
    Product mapToEntity(ProductDto productDto);
    @Mapping(target = "userId", source = "user.id")
    ProductDto mapToDto(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product map(@MappingTarget Product entity, UpdateProductDto updateProductDto);
}
