package com.vemuri.shaktius.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "payment_token")
    private String paymentToken;

    @Column(name = "order_id")
    private String orderID;

    @Column(name = "payer_id")
    private String payerID;

    @Column(name = "payment_id")
    private String paymentID;

    @OneToOne
    @JoinColumn(unique = true)
    private Signup signup;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentToken() {
        return paymentToken;
    }

    public Payment paymentToken(String paymentToken) {
        this.paymentToken = paymentToken;
        return this;
    }

    public void setPaymentToken(String paymentToken) {
        this.paymentToken = paymentToken;
    }

    public String getOrderID() {
        return orderID;
    }

    public Payment orderID(String orderID) {
        this.orderID = orderID;
        return this;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getPayerID() {
        return payerID;
    }

    public Payment payerID(String payerID) {
        this.payerID = payerID;
        return this;
    }

    public void setPayerID(String payerID) {
        this.payerID = payerID;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public Payment paymentID(String paymentID) {
        this.paymentID = paymentID;
        return this;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public Signup getSignup() {
        return signup;
    }

    public Payment signup(Signup signup) {
        this.signup = signup;
        return this;
    }

    public void setSignup(Signup signup) {
        this.signup = signup;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Payment payment = (Payment) o;
        if (payment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), payment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", paymentToken='" + getPaymentToken() + "'" +
            ", orderID='" + getOrderID() + "'" +
            ", payerID='" + getPayerID() + "'" +
            ", paymentID='" + getPaymentID() + "'" +
            "}";
    }
}
