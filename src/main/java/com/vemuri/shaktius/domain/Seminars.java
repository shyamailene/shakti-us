package com.vemuri.shaktius.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Seminars.
 */
@Entity
@Table(name = "seminars")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Seminars implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "topic", nullable = false)
    private String topic;

    @NotNull
    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "contact")
    private String contact;

    @Column(name = "month")
    private String month;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public Seminars topic(String topic) {
        this.topic = topic;
        return this;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getLocation() {
        return location;
    }

    public Seminars location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public Seminars contact(String contact) {
        this.contact = contact;
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMonth() {
        return month;
    }

    public Seminars month(String month) {
        this.month = month;
        return this;
    }

    public void setMonth(String month) {
        this.month = month;
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
        Seminars seminars = (Seminars) o;
        if (seminars.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), seminars.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Seminars{" +
            "id=" + getId() +
            ", topic='" + getTopic() + "'" +
            ", location='" + getLocation() + "'" +
            ", contact='" + getContact() + "'" +
            ", month='" + getMonth() + "'" +
            "}";
    }
}
