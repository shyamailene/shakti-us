package com.vemuri.shaktius.repository;

import com.vemuri.shaktius.domain.Contactus;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Contactus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactusRepository extends JpaRepository<Contactus, Long> {

}
