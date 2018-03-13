package com.vemuri.shaktius.repository;

import com.vemuri.shaktius.domain.Activities;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Activities entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivitiesRepository extends JpaRepository<Activities, Long> {

}
