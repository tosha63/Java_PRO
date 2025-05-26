package t1.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import t1.dto.LimitRequestDto;
import t1.entity.Limit;

@Mapper(componentModel = "spring")
public interface LimitMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Limit map(@MappingTarget Limit entity, LimitRequestDto updateLimitRequestDto);
}
