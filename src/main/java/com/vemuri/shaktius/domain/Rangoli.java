package com.vemuri.shaktius.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Rangoli.
 */
@Entity
@Table(name = "rangoli")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Rangoli implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;

    @Column(name = "email_2")
    private String email2;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "interested")
    private Boolean interested;

    @Column(name = "volunteer")
    private String volunteer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public Rangoli email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public Rangoli name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public Rangoli age(Integer age) {
        this.age = age;
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail2() {
        return email2;
    }

    public Rangoli email2(String email2) {
        this.email2 = email2;
        return this;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getAddress() {
        return address;
    }

    public Rangoli address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public Rangoli phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean isInterested() {
        return interested;
    }

    public Rangoli interested(Boolean interested) {
        this.interested = interested;
        return this;
    }

    public void setInterested(Boolean interested) {
        this.interested = interested;
    }

    public String getVolunteer() {
        return volunteer;
    }

    public Rangoli volunteer(String volunteer) {
        this.volunteer = volunteer;
        return this;
    }

    public void setVolunteer(String volunteer) {
        this.volunteer = volunteer;
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
        Rangoli rangoli = (Rangoli) o;
        if (rangoli.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rangoli.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Rangoli{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", name='" + getName() + "'" +
            ", age=" + getAge() +
            ", email2='" + getEmail2() + "'" +
            ", address='" + getAddress() + "'" +
            ", phone='" + getPhone() + "'" +
            ", interested='" + isInterested() + "'" +
            ", volunteer='" + getVolunteer() + "'" +
            "}";
    }
}
