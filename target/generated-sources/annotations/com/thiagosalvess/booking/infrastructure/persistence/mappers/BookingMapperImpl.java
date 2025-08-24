package com.thiagosalvess.booking.infrastructure.persistence.mappers;

import com.thiagosalvess.booking.domain.entities.Booking;
import com.thiagosalvess.booking.infrastructure.persistence.entities.BookingEntity;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-24T00:34:03+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Eclipse Adoptium)"
)
@Component
public class BookingMapperImpl implements BookingMapper {

    @Autowired
    private PropertyMapper propertyMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Booking toDomain(BookingEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Booking booking = new Booking();

        booking.setId( entity.getId() );
        booking.setProperty( propertyMapper.toDomain( entity.getProperty() ) );
        booking.setGuest( userMapper.toDomain( entity.getGuest() ) );
        booking.setGuestCount( entity.getGuestCount() );
        booking.setStatus( entity.getStatus() );
        booking.setTotalPrice( entity.getTotalPrice() );

        booking.setDateRange( new com.thiagosalvess.booking.domain.valueobject.DateRange(entity.getStartDate(), entity.getEndDate()) );

        return booking;
    }

    @Override
    public BookingEntity toEntity(Booking domain) {
        if ( domain == null ) {
            return null;
        }

        BookingEntity bookingEntity = new BookingEntity();

        bookingEntity.setId( domain.getId() );
        bookingEntity.setProperty( propertyMapper.toEntity( domain.getProperty() ) );
        bookingEntity.setGuest( userMapper.toEntity( domain.getGuest() ) );
        bookingEntity.setGuestCount( domain.getGuestCount() );
        bookingEntity.setStatus( domain.getStatus() );
        bookingEntity.setTotalPrice( domain.getTotalPrice() );

        bookingEntity.setStartDate( domain.getDateRange().getStartDate() );
        bookingEntity.setEndDate( domain.getDateRange().getEndDate() );

        return bookingEntity;
    }
}
