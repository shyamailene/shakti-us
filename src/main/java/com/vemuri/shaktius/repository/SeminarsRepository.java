package com.vemuri.shaktius.repository;

import com.vemuri.shaktius.domain.Seminars;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Seminars entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SeminarsRepository extends JpaRepository<Seminars, Long> {

}
