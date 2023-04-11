package com.cake.ordering.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Color.
 */
@Entity
@Table(name = "color")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Color implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "colors")
    @JsonIgnoreProperties(value = { "orders", "flavors", "colors", "icing" }, allowSetters = true)
    private Set<Cake> cakes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Color id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Color name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Cake> getCakes() {
        return this.cakes;
    }

    public void setCakes(Set<Cake> cakes) {
        if (this.cakes != null) {
            this.cakes.forEach(i -> i.removeColor(this));
        }
        if (cakes != null) {
            cakes.forEach(i -> i.addColor(this));
        }
        this.cakes = cakes;
    }

    public Color cakes(Set<Cake> cakes) {
        this.setCakes(cakes);
        return this;
    }

    public Color addCake(Cake cake) {
        this.cakes.add(cake);
        cake.getColors().add(this);
        return this;
    }

    public Color removeCake(Cake cake) {
        this.cakes.remove(cake);
        cake.getColors().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Color)) {
            return false;
        }
        return id != null && id.equals(((Color) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Color{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
