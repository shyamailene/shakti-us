package com.vemuri.shaktius.repository;

import com.vemuri.shaktius.domain.Locations;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Locations entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationsRepository extends JpaRepository<Locations, Long> {

}
