package com.cake.ordering.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.cake.ordering.domain.Color} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ColorDTO implements Serializable {

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
        if (!(o instanceof ColorDTO)) {
            return false;
        }

        ColorDTO colorDTO = (ColorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, colorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ColorDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
