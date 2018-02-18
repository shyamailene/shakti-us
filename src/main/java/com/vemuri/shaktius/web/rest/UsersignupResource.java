package com.vemuri.shaktius.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vemuri.shaktius.config.AppConstants;
import com.vemuri.shaktius.domain.Appconfig;
import com.vemuri.shaktius.domain.Usersignup;
import com.vemuri.shaktius.repository.UsersignupRepository;
import com.vemuri.shaktius.service.AppconfigService;
import com.vemuri.shaktius.service.MailService;
import com.vemuri.shaktius.web.rest.errors.BadRequestAlertException;
import com.vemuri.shaktius.web.rest.util.HeaderUtil;
import com.vemuri.shaktius.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * REST controller for managing Usersignup.
 */
@RestController
@RequestMapping("/api")
public class UsersignupResource {

    private final Logger log = LoggerFactory.getLogger(UsersignupResource.class);

    private static final String ENTITY_NAME = "usersignup";

    private final UsersignupRepository usersignupRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private AppconfigService appconfigService;

    public UsersignupResource(UsersignupRepository usersignupRepository) {
        this.usersignupRepository = usersignupRepository;
    }

    /**
     * POST  /usersignups : Create a new usersignup.
     *
     * @param usersignup the usersignup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new usersignup, or with status 400 (Bad Request) if the usersignup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/usersignups")
    @Timed
    public ResponseEntity<Usersignup> createUsersignup(@Valid @RequestBody Usersignup usersignup) throws URISyntaxException {
        log.debug("REST request to save Usersignup : {}", usersignup);
        if (usersignup.getId() != null) {
            throw new BadRequestAlertException("A new usersignup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Usersignup result = usersignupRepository.save(usersignup);
        Optional<Appconfig> appConfig=appconfigService.findByKey(AppConstants.ADMIN_EMAIL);
        if(appConfig!=null) {
            String admin_mail_id = appConfig.get().getValue();
            mailService.sendSignUpEmail(null, result, admin_mail_id);
        }
        return ResponseEntity.created(new URI("/api/usersignups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /usersignups : Updates an existing usersignup.
     *
     * @param usersignup the usersignup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated usersignup,
     * or with status 400 (Bad Request) if the usersignup is not valid,
     * or with status 500 (Internal Server Error) if the usersignup couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/usersignups")
    @Timed
    public ResponseEntity<Usersignup> updateUsersignup(@Valid @RequestBody Usersignup usersignup) throws URISyntaxException {
        log.debug("REST request to update Usersignup : {}", usersignup);
        if (usersignup.getId() == null) {
            return createUsersignup(usersignup);
        }
        Usersignup result = usersignupRepository.save(usersignup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, usersignup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /usersignups : get all the usersignups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of usersignups in body
     */
    @GetMapping("/usersignups")
    @Timed
    public ResponseEntity<List<Usersignup>> getAllUsersignups(Pageable pageable) {
        log.debug("REST request to get a page of Usersignups");
        Page<Usersignup> page = usersignupRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/usersignups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /usersignups/:id : get the "id" usersignup.
     *
     * @param id the id of the usersignup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the usersignup, or with status 404 (Not Found)
     */
    @GetMapping("/usersignups/{id}")
    @Timed
    public ResponseEntity<Usersignup> getUsersignup(@PathVariable Long id) {
        log.debug("REST request to get Usersignup : {}", id);
        Usersignup usersignup = usersignupRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(usersignup));
    }

    /**
     * DELETE  /usersignups/:id : delete the "id" usersignup.
     *
     * @param id the id of the usersignup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/usersignups/{id}")
    @Timed
    public ResponseEntity<Void> deleteUsersignup(@PathVariable Long id) {
        log.debug("REST request to delete Usersignup : {}", id);
        usersignupRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
