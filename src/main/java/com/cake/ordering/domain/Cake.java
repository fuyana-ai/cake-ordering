package com.cake.ordering.domain;

import com.cake.ordering.domain.enumeration.CakeSize;
import com.cake.ordering.domain.enumeration.Shape;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Cake.
 */
@Entity
@Table(name = "cake")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cake implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Float price;

    @Enumerated(EnumType.STRING)
    @Column(name = "shape")
    private Shape shape;

    @Enumerated(EnumType.STRING)
    @Column(name = "cake_size")
    private CakeSize cakeSize;

    @OneToMany(mappedBy = "cake")
    @JsonIgnoreProperties(value = { "cake" }, allowSetters = true)
    private Set<Order> orders = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "rel_cake__flavor", joinColumns = @JoinColumn(name = "cake_id"), inverseJoinColumns = @JoinColumn(name = "flavor_id"))
    @JsonIgnoreProperties(value = { "cakes" }, allowSetters = true)
    private Set<Flavor> flavors = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "rel_cake__color", joinColumns = @JoinColumn(name = "cake_id"), inverseJoinColumns = @JoinColumn(name = "color_id"))
    @JsonIgnoreProperties(value = { "cakes" }, allowSetters = true)
    private Set<Color> colors = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "cakes" }, allowSetters = true)
    private Icing icing;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cake id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Cake name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Cake description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return this.price;
    }

    public Cake price(Float price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Shape getShape() {
        return this.shape;
    }

    public Cake shape(Shape shape) {
        this.setShape(shape);
        return this;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public CakeSize getCakeSize() {
        return this.cakeSize;
    }

    public Cake cakeSize(CakeSize cakeSize) {
        this.setCakeSize(cakeSize);
        return this;
    }

    public void setCakeSize(CakeSize cakeSize) {
        this.cakeSize = cakeSize;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Order> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setCake(null));
        }
        if (orders != null) {
            orders.forEach(i -> i.setCake(this));
        }
        this.orders = orders;
    }

    public Cake orders(Set<Order> orders) {
        this.setOrders(orders);
        return this;
    }

    public Cake addOrder(Order order) {
        this.orders.add(order);
        order.setCake(this);
        return this;
    }

    public Cake removeOrder(Order order) {
        this.orders.remove(order);
        order.setCake(null);
        return this;
    }

    public Set<Flavor> getFlavors() {
        return this.flavors;
    }

    public void setFlavors(Set<Flavor> flavors) {
        this.flavors = flavors;
    }

    public Cake flavors(Set<Flavor> flavors) {
        this.setFlavors(flavors);
        return this;
    }

    public Cake addFlavor(Flavor flavor) {
        this.flavors.add(flavor);
        flavor.getCakes().add(this);
        return this;
    }

    public Cake removeFlavor(Flavor flavor) {
        this.flavors.remove(flavor);
        flavor.getCakes().remove(this);
        return this;
    }

    public Set<Color> getColors() {
        return this.colors;
    }

    public void setColors(Set<Color> colors) {
        this.colors = colors;
    }

    public Cake colors(Set<Color> colors) {
        this.setColors(colors);
        return this;
    }

    public Cake addColor(Color color) {
        this.colors.add(color);
        color.getCakes().add(this);
        return this;
    }

    public Cake removeColor(Color color) {
        this.colors.remove(color);
        color.getCakes().remove(this);
        return this;
    }

    public Icing getIcing() {
        return this.icing;
    }

    public void setIcing(Icing icing) {
        this.icing = icing;
    }

    public Cake icing(Icing icing) {
        this.setIcing(icing);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cake)) {
            return false;
        }
        return id != null && id.equals(((Cake) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cake{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            ", shape='" + getShape() + "'" +
            ", cakeSize='" + getCakeSize() + "'" +
            "}";
    }
}
