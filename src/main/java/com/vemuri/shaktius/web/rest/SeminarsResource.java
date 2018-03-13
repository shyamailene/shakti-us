package com.vemuri.shaktius.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vemuri.shaktius.domain.Seminars;

import com.vemuri.shaktius.repository.SeminarsRepository;
import com.vemuri.shaktius.web.rest.errors.BadRequestAlertException;
import com.vemuri.shaktius.web.rest.util.HeaderUtil;
import com.vemuri.shaktius.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Seminars.
 */
@RestController
@RequestMapping("/api")
public class SeminarsResource {

    private final Logger log = LoggerFactory.getLogger(SeminarsResource.class);

    private static final String ENTITY_NAME = "seminars";

    private final SeminarsRepository seminarsRepository;

    public SeminarsResource(SeminarsRepository seminarsRepository) {
        this.seminarsRepository = seminarsRepository;
    }

    /**
     * POST  /seminars : Create a new seminars.
     *
     * @param seminars the seminars to create
     * @return the ResponseEntity with status 201 (Created) and with body the new seminars, or with status 400 (Bad Request) if the seminars has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/seminars")
    @Timed
    public ResponseEntity<Seminars> createSeminars(@Valid @RequestBody Seminars seminars) throws URISyntaxException {
        log.debug("REST request to save Seminars : {}", seminars);
        if (seminars.getId() != null) {
            throw new BadRequestAlertException("A new seminars cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Seminars result = seminarsRepository.save(seminars);
        return ResponseEntity.created(new URI("/api/seminars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /seminars : Updates an existing seminars.
     *
     * @param seminars the seminars to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated seminars,
     * or with status 400 (Bad Request) if the seminars is not valid,
     * or with status 500 (Internal Server Error) if the seminars couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/seminars")
    @Timed
    public ResponseEntity<Seminars> updateSeminars(@Valid @RequestBody Seminars seminars) throws URISyntaxException {
        log.debug("REST request to update Seminars : {}", seminars);
        if (seminars.getId() == null) {
            return createSeminars(seminars);
        }
        Seminars result = seminarsRepository.save(seminars);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, seminars.getId().toString()))
            .body(result);
    }

    /**
     * GET  /seminars : get all the seminars.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of seminars in body
     */
    @GetMapping("/seminars")
    @Timed
    public ResponseEntity<List<Seminars>> getAllSeminars(Pageable pageable) {
        log.debug("REST request to get a page of Seminars");
        Page<Seminars> page = seminarsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/seminars");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /seminars/:id : get the "id" seminars.
     *
     * @param id the id of the seminars to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the seminars, or with status 404 (Not Found)
     */
    @GetMapping("/seminars/{id}")
    @Timed
    public ResponseEntity<Seminars> getSeminars(@PathVariable Long id) {
        log.debug("REST request to get Seminars : {}", id);
        Seminars seminars = seminarsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(seminars));
    }

    /**
     * DELETE  /seminars/:id : delete the "id" seminars.
     *
     * @param id the id of the seminars to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/seminars/{id}")
    @Timed
    public ResponseEntity<Void> deleteSeminars(@PathVariable Long id) {
        log.debug("REST request to delete Seminars : {}", id);
        seminarsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
