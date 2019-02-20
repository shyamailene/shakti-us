package com.vemuri.shaktius.service;

import com.vemuri.shaktius.domain.Rangoli;
import com.vemuri.shaktius.repository.RangoliRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
/**
 * Service Implementation for managing Rangoli.
 */
@Service
@Transactional
public class RangoliService {

    private final Logger log = LoggerFactory.getLogger(RangoliService.class);

    private final RangoliRepository rangoliRepository;

    public RangoliService(RangoliRepository rangoliRepository) {
        this.rangoliRepository = rangoliRepository;
    }

    /**
     * Save a rangoli.
     *
     * @param rangoli the entity to save
     * @return the persisted entity
     */
    public Rangoli save(Rangoli rangoli) {
        log.debug("Request to save Rangoli : {}", rangoli);        return rangoliRepository.save(rangoli);
    }

    /**
     * Get all the rangolis.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Rangoli> findAll() {
        log.debug("Request to get all Rangolis");
        return rangoliRepository.findAll();
    }


    /**
     * Get one rangoli by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Rangoli> findOne(Long id) {
        log.debug("Request to get Rangoli : {}", id);
        return rangoliRepository.findById(id);
    }

    /**
     * Delete the rangoli by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Rangoli : {}", id);
        rangoliRepository.deleteById(id);
    }
}
