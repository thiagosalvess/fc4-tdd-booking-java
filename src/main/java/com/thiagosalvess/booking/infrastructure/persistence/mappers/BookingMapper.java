package com.thiagosalvess.booking.infrastructure.persistence.mappers;

import com.thiagosalvess.booking.domain.entities.Booking;
import com.thiagosalvess.booking.infrastructure.persistence.entities.BookingEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = {PropertyMapper.class, UserMapper.class})
public interface BookingMapper {
    @Mapping(target = "dateRange",
            expression = "java(new com.thiagosalvess.booking.domain.valueobject.DateRange(entity.getStartDate(), entity.getEndDate()))")
    Booking toDomain(BookingEntity entity);
    @Mappings({
            @Mapping(target = "startDate", expression = "java(domain.getDateRange().getStartDate())"),
            @Mapping(target = "endDate",   expression = "java(domain.getDateRange().getEndDate())")
    })
    BookingEntity toEntity(Booking domain);
}
