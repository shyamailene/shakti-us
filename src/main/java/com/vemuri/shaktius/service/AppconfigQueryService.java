package com.vemuri.shaktius.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.vemuri.shaktius.domain.Appconfig;
import com.vemuri.shaktius.domain.*; // for static metamodels
import com.vemuri.shaktius.repository.AppconfigRepository;
import com.vemuri.shaktius.service.dto.AppconfigCriteria;


/**
 * Service for executing complex queries for Appconfig entities in the database.
 * The main input is a {@link AppconfigCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Appconfig} or a {@link Page} of {@link Appconfig} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AppconfigQueryService extends QueryService<Appconfig> {

    private final Logger log = LoggerFactory.getLogger(AppconfigQueryService.class);


    private final AppconfigRepository appconfigRepository;

    public AppconfigQueryService(AppconfigRepository appconfigRepository) {
        this.appconfigRepository = appconfigRepository;
    }

    /**
     * Return a {@link List} of {@link Appconfig} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Appconfig> findByCriteria(AppconfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Appconfig> specification = createSpecification(criteria);
        return appconfigRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Appconfig} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Appconfig> findByCriteria(AppconfigCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Appconfig> specification = createSpecification(criteria);
        return appconfigRepository.findAll(specification, page);
    }

    /**
     * Function to convert AppconfigCriteria to a {@link Specifications}
     */
    private Specifications<Appconfig> createSpecification(AppconfigCriteria criteria) {
        Specifications<Appconfig> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Appconfig_.id));
            }
            if (criteria.getKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKey(), Appconfig_.key));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), Appconfig_.value));
            }
        }
        return specification;
    }

}
