package com.thiagosalvess.booking.infrastructure.persistence.mappers;

import com.thiagosalvess.booking.domain.entities.User;
import com.thiagosalvess.booking.infrastructure.persistence.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toDomain(UserEntity entity);

    UserEntity toEntity(User domain);
}
