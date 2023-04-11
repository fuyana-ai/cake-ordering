package com.cake.ordering.service.mapper;

import com.cake.ordering.domain.Color;
import com.cake.ordering.service.dto.ColorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Color} and its DTO {@link ColorDTO}.
 */
@Mapper(componentModel = "spring")
public interface ColorMapper extends EntityMapper<ColorDTO, Color> {}
