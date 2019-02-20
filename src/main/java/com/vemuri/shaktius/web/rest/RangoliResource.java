package com.vemuri.shaktius.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vemuri.shaktius.domain.Rangoli;
import com.vemuri.shaktius.service.RangoliService;
import com.vemuri.shaktius.web.rest.errors.BadRequestAlertException;
import com.vemuri.shaktius.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Rangoli.
 */
@RestController
@RequestMapping("/api")
public class RangoliResource {

    private final Logger log = LoggerFactory.getLogger(RangoliResource.class);

    private static final String ENTITY_NAME = "rangoli";

    private final RangoliService rangoliService;

    public RangoliResource(RangoliService rangoliService) {
        this.rangoliService = rangoliService;
    }

    /**
     * POST  /rangolis : Create a new rangoli.
     *
     * @param rangoli the rangoli to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rangoli, or with status 400 (Bad Request) if the rangoli has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rangolis")
    @Timed
    public ResponseEntity<Rangoli> createRangoli(@Valid @RequestBody Rangoli rangoli) throws URISyntaxException {
        log.debug("REST request to save Rangoli : {}", rangoli);
        if (rangoli.getId() != null) {
            throw new BadRequestAlertException("A new rangoli cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Rangoli result = rangoliService.save(rangoli);
        return ResponseEntity.created(new URI("/api/rangolis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rangolis : Updates an existing rangoli.
     *
     * @param rangoli the rangoli to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rangoli,
     * or with status 400 (Bad Request) if the rangoli is not valid,
     * or with status 500 (Internal Server Error) if the rangoli couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rangolis")
    @Timed
    public ResponseEntity<Rangoli> updateRangoli(@Valid @RequestBody Rangoli rangoli) throws URISyntaxException {
        log.debug("REST request to update Rangoli : {}", rangoli);
        if (rangoli.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Rangoli result = rangoliService.save(rangoli);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rangoli.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rangolis : get all the rangolis.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of rangolis in body
     */
    @GetMapping("/rangolis")
    @Timed
    public List<Rangoli> getAllRangolis() {
        log.debug("REST request to get all Rangolis");
        return rangoliService.findAll();
    }

    /**
     * GET  /rangolis/:id : get the "id" rangoli.
     *
     * @param id the id of the rangoli to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rangoli, or with status 404 (Not Found)
     */
    @GetMapping("/rangolis/{id}")
    @Timed
    public ResponseEntity<Rangoli> getRangoli(@PathVariable Long id) {
        log.debug("REST request to get Rangoli : {}", id);
        Optional<Rangoli> rangoli = rangoliService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rangoli);
    }

    /**
     * DELETE  /rangolis/:id : delete the "id" rangoli.
     *
     * @param id the id of the rangoli to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rangolis/{id}")
    @Timed
    public ResponseEntity<Void> deleteRangoli(@PathVariable Long id) {
        log.debug("REST request to delete Rangoli : {}", id);
        rangoliService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
