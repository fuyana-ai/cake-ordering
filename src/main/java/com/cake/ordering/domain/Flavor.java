package com.cake.ordering.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Flavor.
 */
@Entity
@Table(name = "flavor")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Flavor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "flavors")
    @JsonIgnoreProperties(value = { "orders", "flavors", "colors", "icing" }, allowSetters = true)
    private Set<Cake> cakes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Flavor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Flavor name(String name) {
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
            this.cakes.forEach(i -> i.removeFlavor(this));
        }
        if (cakes != null) {
            cakes.forEach(i -> i.addFlavor(this));
        }
        this.cakes = cakes;
    }

    public Flavor cakes(Set<Cake> cakes) {
        this.setCakes(cakes);
        return this;
    }

    public Flavor addCake(Cake cake) {
        this.cakes.add(cake);
        cake.getFlavors().add(this);
        return this;
    }

    public Flavor removeCake(Cake cake) {
        this.cakes.remove(cake);
        cake.getFlavors().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Flavor)) {
            return false;
        }
        return id != null && id.equals(((Flavor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Flavor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
