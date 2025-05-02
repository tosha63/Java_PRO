package t1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import t1.dto.UserDto;
import t1.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User mapToEntity(UserDto userDto);
    UserDto mapToDto(User user);
    User map(@MappingTarget User user, UserDto userDto);
}
