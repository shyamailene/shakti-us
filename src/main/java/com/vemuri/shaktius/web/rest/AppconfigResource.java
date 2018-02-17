package com.vemuri.shaktius.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vemuri.shaktius.domain.Appconfig;
import com.vemuri.shaktius.service.AppconfigService;
import com.vemuri.shaktius.web.rest.errors.BadRequestAlertException;
import com.vemuri.shaktius.web.rest.util.HeaderUtil;
import com.vemuri.shaktius.web.rest.util.PaginationUtil;
import com.vemuri.shaktius.service.dto.AppconfigCriteria;
import com.vemuri.shaktius.service.AppconfigQueryService;
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
 * REST controller for managing Appconfig.
 */
@RestController
@RequestMapping("/api")
public class AppconfigResource {

    private final Logger log = LoggerFactory.getLogger(AppconfigResource.class);

    private static final String ENTITY_NAME = "appconfig";

    private final AppconfigService appconfigService;

    private final AppconfigQueryService appconfigQueryService;

    public AppconfigResource(AppconfigService appconfigService, AppconfigQueryService appconfigQueryService) {
        this.appconfigService = appconfigService;
        this.appconfigQueryService = appconfigQueryService;
    }

    /**
     * POST  /appconfigs : Create a new appconfig.
     *
     * @param appconfig the appconfig to create
     * @return the ResponseEntity with status 201 (Created) and with body the new appconfig, or with status 400 (Bad Request) if the appconfig has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/appconfigs")
    @Timed
    public ResponseEntity<Appconfig> createAppconfig(@Valid @RequestBody Appconfig appconfig) throws URISyntaxException {
        log.debug("REST request to save Appconfig : {}", appconfig);
        if (appconfig.getId() != null) {
            throw new BadRequestAlertException("A new appconfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Appconfig result = appconfigService.save(appconfig);
        return ResponseEntity.created(new URI("/api/appconfigs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /appconfigs : Updates an existing appconfig.
     *
     * @param appconfig the appconfig to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated appconfig,
     * or with status 400 (Bad Request) if the appconfig is not valid,
     * or with status 500 (Internal Server Error) if the appconfig couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/appconfigs")
    @Timed
    public ResponseEntity<Appconfig> updateAppconfig(@Valid @RequestBody Appconfig appconfig) throws URISyntaxException {
        log.debug("REST request to update Appconfig : {}", appconfig);
        if (appconfig.getId() == null) {
            return createAppconfig(appconfig);
        }
        Appconfig result = appconfigService.save(appconfig);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, appconfig.getId().toString()))
            .body(result);
    }

    /**
     * GET  /appconfigs : get all the appconfigs.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of appconfigs in body
     */
    @GetMapping("/appconfigs")
    @Timed
    public ResponseEntity<List<Appconfig>> getAllAppconfigs(AppconfigCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Appconfigs by criteria: {}", criteria);
        Page<Appconfig> page = appconfigQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/appconfigs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /appconfigs/:id : get the "id" appconfig.
     *
     * @param id the id of the appconfig to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the appconfig, or with status 404 (Not Found)
     */
    @GetMapping("/appconfigs/{id}")
    @Timed
    public ResponseEntity<Appconfig> getAppconfig(@PathVariable Long id) {
        log.debug("REST request to get Appconfig : {}", id);
        Appconfig appconfig = appconfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(appconfig));
    }

    /**
     * DELETE  /appconfigs/:id : delete the "id" appconfig.
     *
     * @param id the id of the appconfig to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/appconfigs/{id}")
    @Timed
    public ResponseEntity<Void> deleteAppconfig(@PathVariable Long id) {
        log.debug("REST request to delete Appconfig : {}", id);
        appconfigService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
