package com.cake.ordering.service.mapper;

import com.cake.ordering.domain.Flavor;
import com.cake.ordering.service.dto.FlavorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Flavor} and its DTO {@link FlavorDTO}.
 */
@Mapper(componentModel = "spring")
public interface FlavorMapper extends EntityMapper<FlavorDTO, Flavor> {}
