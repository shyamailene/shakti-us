package com.vemuri.shaktius.repository;

import com.vemuri.shaktius.domain.Usersignup;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Usersignup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UsersignupRepository extends JpaRepository<Usersignup, Long> {

}
