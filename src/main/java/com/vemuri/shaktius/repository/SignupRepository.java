package com.vemuri.shaktius.repository;

import com.vemuri.shaktius.domain.Signup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Signup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SignupRepository extends JpaRepository<Signup, Long> {

}
