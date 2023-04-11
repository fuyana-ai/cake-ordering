package com.cake.ordering.domain;

import com.cake.ordering.domain.enumeration.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * A Order.
 */
@Entity
@Table(name = "jhi_order")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "collection_date")
    private LocalDate collectionDate;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "customer_adress")
    private String customerAdress;

    @ManyToOne
    @JsonIgnoreProperties(value = { "orders", "flavors", "colors", "icing" }, allowSetters = true)
    private Cake cake;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Order id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return this.status;
    }

    public Order status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getCollectionDate() {
        return this.collectionDate;
    }

    public Order collectionDate(LocalDate collectionDate) {
        this.setCollectionDate(collectionDate);
        return this;
    }

    public void setCollectionDate(LocalDate collectionDate) {
        this.collectionDate = collectionDate;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public Order customerName(String customerName) {
        this.setCustomerName(customerName);
        return this;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return this.customerPhone;
    }

    public Order customerPhone(String customerPhone) {
        this.setCustomerPhone(customerPhone);
        return this;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerAdress() {
        return this.customerAdress;
    }

    public Order customerAdress(String customerAdress) {
        this.setCustomerAdress(customerAdress);
        return this;
    }

    public void setCustomerAdress(String customerAdress) {
        this.customerAdress = customerAdress;
    }

    public Cake getCake() {
        return this.cake;
    }

    public void setCake(Cake cake) {
        this.cake = cake;
    }

    public Order cake(Cake cake) {
        this.setCake(cake);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", collectionDate='" + getCollectionDate() + "'" +
            ", customerName='" + getCustomerName() + "'" +
            ", customerPhone='" + getCustomerPhone() + "'" +
            ", customerAdress='" + getCustomerAdress() + "'" +
            "}";
    }
}
