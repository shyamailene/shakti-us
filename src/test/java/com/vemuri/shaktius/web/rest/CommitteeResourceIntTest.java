package com.vemuri.shaktius.web.rest;

import com.vemuri.shaktius.ShaktiusApp;

import com.vemuri.shaktius.domain.Committee;
import com.vemuri.shaktius.repository.CommitteeRepository;
import com.vemuri.shaktius.repository.search.CommitteeSearchRepository;
import com.vemuri.shaktius.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.vemuri.shaktius.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CommitteeResource REST controller.
 *
 * @see CommitteeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShaktiusApp.class)
public class CommitteeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE = "BBBBBBBBBB";

    @Autowired
    private CommitteeRepository committeeRepository;

    @Autowired
    private CommitteeSearchRepository committeeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCommitteeMockMvc;

    private Committee committee;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CommitteeResource committeeResource = new CommitteeResource(committeeRepository, committeeSearchRepository);
        this.restCommitteeMockMvc = MockMvcBuilders.standaloneSetup(committeeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Committee createEntity(EntityManager em) {
        Committee committee = new Committee()
            .name(DEFAULT_NAME)
            .title(DEFAULT_TITLE)
            .email(DEFAULT_EMAIL)
            .mobile(DEFAULT_MOBILE);
        return committee;
    }

    @Before
    public void initTest() {
        committeeSearchRepository.deleteAll();
        committee = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommittee() throws Exception {
        int databaseSizeBeforeCreate = committeeRepository.findAll().size();

        // Create the Committee
        restCommitteeMockMvc.perform(post("/api/committees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(committee)))
            .andExpect(status().isCreated());

        // Validate the Committee in the database
        List<Committee> committeeList = committeeRepository.findAll();
        assertThat(committeeList).hasSize(databaseSizeBeforeCreate + 1);
        Committee testCommittee = committeeList.get(committeeList.size() - 1);
        assertThat(testCommittee.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCommittee.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCommittee.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCommittee.getMobile()).isEqualTo(DEFAULT_MOBILE);

        // Validate the Committee in Elasticsearch
        Committee committeeEs = committeeSearchRepository.findOne(testCommittee.getId());
        assertThat(committeeEs).isEqualToIgnoringGivenFields(testCommittee);
    }

    @Test
    @Transactional
    public void createCommitteeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = committeeRepository.findAll().size();

        // Create the Committee with an existing ID
        committee.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommitteeMockMvc.perform(post("/api/committees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(committee)))
            .andExpect(status().isBadRequest());

        // Validate the Committee in the database
        List<Committee> committeeList = committeeRepository.findAll();
        assertThat(committeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = committeeRepository.findAll().size();
        // set the field null
        committee.setName(null);

        // Create the Committee, which fails.

        restCommitteeMockMvc.perform(post("/api/committees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(committee)))
            .andExpect(status().isBadRequest());

        List<Committee> committeeList = committeeRepository.findAll();
        assertThat(committeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCommittees() throws Exception {
        // Initialize the database
        committeeRepository.saveAndFlush(committee);

        // Get all the committeeList
        restCommitteeMockMvc.perform(get("/api/committees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(committee.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE.toString())));
    }

    @Test
    @Transactional
    public void getCommittee() throws Exception {
        // Initialize the database
        committeeRepository.saveAndFlush(committee);

        // Get the committee
        restCommitteeMockMvc.perform(get("/api/committees/{id}", committee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(committee.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCommittee() throws Exception {
        // Get the committee
        restCommitteeMockMvc.perform(get("/api/committees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommittee() throws Exception {
        // Initialize the database
        committeeRepository.saveAndFlush(committee);
        committeeSearchRepository.save(committee);
        int databaseSizeBeforeUpdate = committeeRepository.findAll().size();

        // Update the committee
        Committee updatedCommittee = committeeRepository.findOne(committee.getId());
        // Disconnect from session so that the updates on updatedCommittee are not directly saved in db
        em.detach(updatedCommittee);
        updatedCommittee
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .email(UPDATED_EMAIL)
            .mobile(UPDATED_MOBILE);

        restCommitteeMockMvc.perform(put("/api/committees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCommittee)))
            .andExpect(status().isOk());

        // Validate the Committee in the database
        List<Committee> committeeList = committeeRepository.findAll();
        assertThat(committeeList).hasSize(databaseSizeBeforeUpdate);
        Committee testCommittee = committeeList.get(committeeList.size() - 1);
        assertThat(testCommittee.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCommittee.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCommittee.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCommittee.getMobile()).isEqualTo(UPDATED_MOBILE);

        // Validate the Committee in Elasticsearch
        Committee committeeEs = committeeSearchRepository.findOne(testCommittee.getId());
        assertThat(committeeEs).isEqualToIgnoringGivenFields(testCommittee);
    }

    @Test
    @Transactional
    public void updateNonExistingCommittee() throws Exception {
        int databaseSizeBeforeUpdate = committeeRepository.findAll().size();

        // Create the Committee

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCommitteeMockMvc.perform(put("/api/committees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(committee)))
            .andExpect(status().isCreated());

        // Validate the Committee in the database
        List<Committee> committeeList = committeeRepository.findAll();
        assertThat(committeeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCommittee() throws Exception {
        // Initialize the database
        committeeRepository.saveAndFlush(committee);
        committeeSearchRepository.save(committee);
        int databaseSizeBeforeDelete = committeeRepository.findAll().size();

        // Get the committee
        restCommitteeMockMvc.perform(delete("/api/committees/{id}", committee.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean committeeExistsInEs = committeeSearchRepository.exists(committee.getId());
        assertThat(committeeExistsInEs).isFalse();

        // Validate the database is empty
        List<Committee> committeeList = committeeRepository.findAll();
        assertThat(committeeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCommittee() throws Exception {
        // Initialize the database
        committeeRepository.saveAndFlush(committee);
        committeeSearchRepository.save(committee);

        // Search the committee
        restCommitteeMockMvc.perform(get("/api/_search/committees?query=id:" + committee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(committee.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Committee.class);
        Committee committee1 = new Committee();
        committee1.setId(1L);
        Committee committee2 = new Committee();
        committee2.setId(committee1.getId());
        assertThat(committee1).isEqualTo(committee2);
        committee2.setId(2L);
        assertThat(committee1).isNotEqualTo(committee2);
        committee1.setId(null);
        assertThat(committee1).isNotEqualTo(committee2);
    }
}
