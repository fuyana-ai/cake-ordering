package com.cake.ordering.service.dto;

import com.cake.ordering.domain.enumeration.CakeSize;
import com.cake.ordering.domain.enumeration.Shape;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.cake.ordering.domain.Cake} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CakeDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private Float price;

    private Shape shape;

    private CakeSize cakeSize;

    private Set<FlavorDTO> flavors = new HashSet<>();

    private Set<ColorDTO> colors = new HashSet<>();

    private IcingDTO icing;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public CakeSize getCakeSize() {
        return cakeSize;
    }

    public void setCakeSize(CakeSize cakeSize) {
        this.cakeSize = cakeSize;
    }

    public Set<FlavorDTO> getFlavors() {
        return flavors;
    }

    public void setFlavors(Set<FlavorDTO> flavors) {
        this.flavors = flavors;
    }

    public Set<ColorDTO> getColors() {
        return colors;
    }

    public void setColors(Set<ColorDTO> colors) {
        this.colors = colors;
    }

    public IcingDTO getIcing() {
        return icing;
    }

    public void setIcing(IcingDTO icing) {
        this.icing = icing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CakeDTO)) {
            return false;
        }

        CakeDTO cakeDTO = (CakeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cakeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CakeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            ", shape='" + getShape() + "'" +
            ", cakeSize='" + getCakeSize() + "'" +
            ", flavors=" + getFlavors() +
            ", colors=" + getColors() +
            ", icing=" + getIcing() +
            "}";
    }
}
