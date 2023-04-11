package com.cake.ordering.service.mapper;

import com.cake.ordering.domain.Icing;
import com.cake.ordering.service.dto.IcingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Icing} and its DTO {@link IcingDTO}.
 */
@Mapper(componentModel = "spring")
public interface IcingMapper extends EntityMapper<IcingDTO, Icing> {}
