package com.cake.ordering.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.cake.ordering.domain.Flavor} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FlavorDTO implements Serializable {

    private Long id;

    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FlavorDTO)) {
            return false;
        }

        FlavorDTO flavorDTO = (FlavorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, flavorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FlavorDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
