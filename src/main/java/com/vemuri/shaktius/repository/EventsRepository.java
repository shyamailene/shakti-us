package com.vemuri.shaktius.repository;

import com.vemuri.shaktius.domain.Events;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Events entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventsRepository extends JpaRepository<Events, Long> {

}
