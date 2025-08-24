package com.thiagosalvess.booking.infrastructure.persistence.mappers;

import com.thiagosalvess.booking.domain.entities.User;
import com.thiagosalvess.booking.infrastructure.persistence.entities.UserEntity;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-24T00:34:03+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toDomain(UserEntity entity) {
        if ( entity == null ) {
            return null;
        }

        UUID id = null;
        String name = null;

        id = entity.getId();
        name = entity.getName();

        User user = new User( id, name );

        return user;
    }

    @Override
    public UserEntity toEntity(User domain) {
        if ( domain == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setId( domain.getId() );
        userEntity.setName( domain.getName() );

        return userEntity;
    }
}
