package com.cake.ordering.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Icing.
 */
@Entity
@Table(name = "icing")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Icing implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "icing")
    @JsonIgnoreProperties(value = { "orders", "flavors", "colors", "icing" }, allowSetters = true)
    private Set<Cake> cakes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Icing id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Icing name(String name) {
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
            this.cakes.forEach(i -> i.setIcing(null));
        }
        if (cakes != null) {
            cakes.forEach(i -> i.setIcing(this));
        }
        this.cakes = cakes;
    }

    public Icing cakes(Set<Cake> cakes) {
        this.setCakes(cakes);
        return this;
    }

    public Icing addCake(Cake cake) {
        this.cakes.add(cake);
        cake.setIcing(this);
        return this;
    }

    public Icing removeCake(Cake cake) {
        this.cakes.remove(cake);
        cake.setIcing(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Icing)) {
            return false;
        }
        return id != null && id.equals(((Icing) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Icing{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
