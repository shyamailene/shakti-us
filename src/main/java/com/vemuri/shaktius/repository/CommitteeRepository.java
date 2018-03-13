package com.vemuri.shaktius.repository;

import com.vemuri.shaktius.domain.Committee;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Committee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommitteeRepository extends JpaRepository<Committee, Long> {

}
