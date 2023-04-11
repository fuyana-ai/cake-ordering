package com.cake.ordering.service.mapper;

import com.cake.ordering.domain.Cake;
import com.cake.ordering.domain.Color;
import com.cake.ordering.domain.Flavor;
import com.cake.ordering.domain.Icing;
import com.cake.ordering.service.dto.CakeDTO;
import com.cake.ordering.service.dto.ColorDTO;
import com.cake.ordering.service.dto.FlavorDTO;
import com.cake.ordering.service.dto.IcingDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cake} and its DTO {@link CakeDTO}.
 */
@Mapper(componentModel = "spring")
public interface CakeMapper extends EntityMapper<CakeDTO, Cake> {
    @Mapping(target = "flavors", source = "flavors", qualifiedByName = "flavorNameSet")
    @Mapping(target = "colors", source = "colors", qualifiedByName = "colorNameSet")
    @Mapping(target = "icing", source = "icing", qualifiedByName = "icingId")
    CakeDTO toDto(Cake s);

    @Mapping(target = "removeFlavor", ignore = true)
    @Mapping(target = "removeColor", ignore = true)
    Cake toEntity(CakeDTO cakeDTO);

    @Named("flavorName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    FlavorDTO toDtoFlavorName(Flavor flavor);

    @Named("flavorNameSet")
    default Set<FlavorDTO> toDtoFlavorNameSet(Set<Flavor> flavor) {
        return flavor.stream().map(this::toDtoFlavorName).collect(Collectors.toSet());
    }

    @Named("colorName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ColorDTO toDtoColorName(Color color);

    @Named("colorNameSet")
    default Set<ColorDTO> toDtoColorNameSet(Set<Color> color) {
        return color.stream().map(this::toDtoColorName).collect(Collectors.toSet());
    }

    @Named("icingId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IcingDTO toDtoIcingId(Icing icing);
}
