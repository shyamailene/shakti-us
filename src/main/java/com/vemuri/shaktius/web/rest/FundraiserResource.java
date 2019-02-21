package com.vemuri.shaktius.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vemuri.shaktius.domain.Fundraiser;
import com.vemuri.shaktius.repository.FundraiserRepository;
import com.vemuri.shaktius.web.rest.errors.BadRequestAlertException;
import com.vemuri.shaktius.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Fundraiser.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://www.shakti-us.org","https://www.shakti-us.org"})
public class FundraiserResource {

    private final Logger log = LoggerFactory.getLogger(FundraiserResource.class);

    private static final String ENTITY_NAME = "fundraiser";

    private final FundraiserRepository fundraiserRepository;

    public FundraiserResource(FundraiserRepository fundraiserRepository) {
        this.fundraiserRepository = fundraiserRepository;
    }

    /**
     * POST  /fundraisers : Create a new fundraiser.
     *
     * @param fundraiser the fundraiser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fundraiser, or with status 400 (Bad Request) if the fundraiser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fundraisers")
    @Timed
    public ResponseEntity<Fundraiser> createFundraiser(@RequestBody Fundraiser fundraiser) throws URISyntaxException {
        log.debug("REST request to save Fundraiser : {}", fundraiser);
        if (fundraiser.getId() != null) {
            throw new BadRequestAlertException("A new fundraiser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Fundraiser result = fundraiserRepository.save(fundraiser);
        return ResponseEntity.created(new URI("/api/fundraisers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fundraisers : Updates an existing fundraiser.
     *
     * @param fundraiser the fundraiser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fundraiser,
     * or with status 400 (Bad Request) if the fundraiser is not valid,
     * or with status 500 (Internal Server Error) if the fundraiser couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fundraisers")
    @Timed
    public ResponseEntity<Fundraiser> updateFundraiser(@RequestBody Fundraiser fundraiser) throws URISyntaxException {
        log.debug("REST request to update Fundraiser : {}", fundraiser);
        if (fundraiser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Fundraiser result = fundraiserRepository.save(fundraiser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fundraiser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fundraisers : get all the fundraisers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of fundraisers in body
     */
    @GetMapping("/fundraisers")
    @Timed
    public List<Fundraiser> getAllFundraisers() {
        log.debug("REST request to get all Fundraisers");
        return fundraiserRepository.findAll();
    }

    /**
     * GET  /fundraisers/:id : get the "id" fundraiser.
     *
     * @param id the id of the fundraiser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fundraiser, or with status 404 (Not Found)
     */
    @GetMapping("/fundraisers/{id}")
    @Timed
    public ResponseEntity<Fundraiser> getFundraiser(@PathVariable Long id) {
        log.debug("REST request to get Fundraiser : {}", id);
        Optional<Fundraiser> fundraiser = fundraiserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fundraiser);
    }

    /**
     * DELETE  /fundraisers/:id : delete the "id" fundraiser.
     *
     * @param id the id of the fundraiser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fundraisers/{id}")
    @Timed
    public ResponseEntity<Void> deleteFundraiser(@PathVariable Long id) {
        log.debug("REST request to delete Fundraiser : {}", id);

        fundraiserRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
