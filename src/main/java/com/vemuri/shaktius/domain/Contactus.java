package com.vemuri.shaktius.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Contactus.
 */
@Entity
@Table(name = "contactus")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Contactus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "relatedto")
    private String relatedto;

    @Column(name = "content")
    private String content;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

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

    public Contactus email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public Contactus mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRelatedto() {
        return relatedto;
    }

    public Contactus relatedto(String relatedto) {
        this.relatedto = relatedto;
        return this;
    }

    public void setRelatedto(String relatedto) {
        this.relatedto = relatedto;
    }

    public String getContent() {
        return content;
    }

    public Contactus content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public Contactus name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
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
        Contactus contactus = (Contactus) o;
        if (contactus.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contactus.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Contactus{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", mobile='" + getMobile() + "'" +
            ", relatedto='" + getRelatedto() + "'" +
            ", content='" + getContent() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
