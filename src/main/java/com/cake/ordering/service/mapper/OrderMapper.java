package com.cake.ordering.service.mapper;

import com.cake.ordering.domain.Cake;
import com.cake.ordering.domain.Order;
import com.cake.ordering.service.dto.CakeDTO;
import com.cake.ordering.service.dto.OrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {
    @Mapping(target = "cake", source = "cake", qualifiedByName = "cakeId")
    OrderDTO toDto(Order s);

    @Named("cakeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CakeDTO toDtoCakeId(Cake cake);
}
