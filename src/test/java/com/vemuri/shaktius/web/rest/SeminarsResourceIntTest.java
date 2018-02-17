package com.vemuri.shaktius.web.rest;

import com.vemuri.shaktius.ShaktiusApp;

import com.vemuri.shaktius.domain.Seminars;
import com.vemuri.shaktius.repository.SeminarsRepository;
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
 * Test class for the SeminarsResource REST controller.
 *
 * @see SeminarsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShaktiusApp.class)
public class SeminarsResourceIntTest {

    private static final String DEFAULT_TOPIC = "AAAAAAAAAA";
    private static final String UPDATED_TOPIC = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final String DEFAULT_MONTH = "AAAAAAAAAA";
    private static final String UPDATED_MONTH = "BBBBBBBBBB";

    @Autowired
    private SeminarsRepository seminarsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSeminarsMockMvc;

    private Seminars seminars;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SeminarsResource seminarsResource = new SeminarsResource(seminarsRepository);
        this.restSeminarsMockMvc = MockMvcBuilders.standaloneSetup(seminarsResource)
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
    public static Seminars createEntity(EntityManager em) {
        Seminars seminars = new Seminars()
            .topic(DEFAULT_TOPIC)
            .location(DEFAULT_LOCATION)
            .contact(DEFAULT_CONTACT)
            .month(DEFAULT_MONTH);
        return seminars;
    }

    @Before
    public void initTest() {
        seminars = createEntity(em);
    }

    @Test
    @Transactional
    public void createSeminars() throws Exception {
        int databaseSizeBeforeCreate = seminarsRepository.findAll().size();

        // Create the Seminars
        restSeminarsMockMvc.perform(post("/api/seminars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(seminars)))
            .andExpect(status().isCreated());

        // Validate the Seminars in the database
        List<Seminars> seminarsList = seminarsRepository.findAll();
        assertThat(seminarsList).hasSize(databaseSizeBeforeCreate + 1);
        Seminars testSeminars = seminarsList.get(seminarsList.size() - 1);
        assertThat(testSeminars.getTopic()).isEqualTo(DEFAULT_TOPIC);
        assertThat(testSeminars.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testSeminars.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testSeminars.getMonth()).isEqualTo(DEFAULT_MONTH);
    }

    @Test
    @Transactional
    public void createSeminarsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = seminarsRepository.findAll().size();

        // Create the Seminars with an existing ID
        seminars.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSeminarsMockMvc.perform(post("/api/seminars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(seminars)))
            .andExpect(status().isBadRequest());

        // Validate the Seminars in the database
        List<Seminars> seminarsList = seminarsRepository.findAll();
        assertThat(seminarsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTopicIsRequired() throws Exception {
        int databaseSizeBeforeTest = seminarsRepository.findAll().size();
        // set the field null
        seminars.setTopic(null);

        // Create the Seminars, which fails.

        restSeminarsMockMvc.perform(post("/api/seminars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(seminars)))
            .andExpect(status().isBadRequest());

        List<Seminars> seminarsList = seminarsRepository.findAll();
        assertThat(seminarsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = seminarsRepository.findAll().size();
        // set the field null
        seminars.setLocation(null);

        // Create the Seminars, which fails.

        restSeminarsMockMvc.perform(post("/api/seminars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(seminars)))
            .andExpect(status().isBadRequest());

        List<Seminars> seminarsList = seminarsRepository.findAll();
        assertThat(seminarsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSeminars() throws Exception {
        // Initialize the database
        seminarsRepository.saveAndFlush(seminars);

        // Get all the seminarsList
        restSeminarsMockMvc.perform(get("/api/seminars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(seminars.getId().intValue())))
            .andExpect(jsonPath("$.[*].topic").value(hasItem(DEFAULT_TOPIC.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT.toString())))
            .andExpect(jsonPath("$.[*].month").value(hasItem(DEFAULT_MONTH.toString())));
    }

    @Test
    @Transactional
    public void getSeminars() throws Exception {
        // Initialize the database
        seminarsRepository.saveAndFlush(seminars);

        // Get the seminars
        restSeminarsMockMvc.perform(get("/api/seminars/{id}", seminars.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(seminars.getId().intValue()))
            .andExpect(jsonPath("$.topic").value(DEFAULT_TOPIC.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT.toString()))
            .andExpect(jsonPath("$.month").value(DEFAULT_MONTH.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSeminars() throws Exception {
        // Get the seminars
        restSeminarsMockMvc.perform(get("/api/seminars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSeminars() throws Exception {
        // Initialize the database
        seminarsRepository.saveAndFlush(seminars);
        int databaseSizeBeforeUpdate = seminarsRepository.findAll().size();

        // Update the seminars
        Seminars updatedSeminars = seminarsRepository.findOne(seminars.getId());
        // Disconnect from session so that the updates on updatedSeminars are not directly saved in db
        em.detach(updatedSeminars);
        updatedSeminars
            .topic(UPDATED_TOPIC)
            .location(UPDATED_LOCATION)
            .contact(UPDATED_CONTACT)
            .month(UPDATED_MONTH);

        restSeminarsMockMvc.perform(put("/api/seminars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSeminars)))
            .andExpect(status().isOk());

        // Validate the Seminars in the database
        List<Seminars> seminarsList = seminarsRepository.findAll();
        assertThat(seminarsList).hasSize(databaseSizeBeforeUpdate);
        Seminars testSeminars = seminarsList.get(seminarsList.size() - 1);
        assertThat(testSeminars.getTopic()).isEqualTo(UPDATED_TOPIC);
        assertThat(testSeminars.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testSeminars.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testSeminars.getMonth()).isEqualTo(UPDATED_MONTH);
    }

    @Test
    @Transactional
    public void updateNonExistingSeminars() throws Exception {
        int databaseSizeBeforeUpdate = seminarsRepository.findAll().size();

        // Create the Seminars

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSeminarsMockMvc.perform(put("/api/seminars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(seminars)))
            .andExpect(status().isCreated());

        // Validate the Seminars in the database
        List<Seminars> seminarsList = seminarsRepository.findAll();
        assertThat(seminarsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSeminars() throws Exception {
        // Initialize the database
        seminarsRepository.saveAndFlush(seminars);
        int databaseSizeBeforeDelete = seminarsRepository.findAll().size();

        // Get the seminars
        restSeminarsMockMvc.perform(delete("/api/seminars/{id}", seminars.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Seminars> seminarsList = seminarsRepository.findAll();
        assertThat(seminarsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Seminars.class);
        Seminars seminars1 = new Seminars();
        seminars1.setId(1L);
        Seminars seminars2 = new Seminars();
        seminars2.setId(seminars1.getId());
        assertThat(seminars1).isEqualTo(seminars2);
        seminars2.setId(2L);
        assertThat(seminars1).isNotEqualTo(seminars2);
        seminars1.setId(null);
        assertThat(seminars1).isNotEqualTo(seminars2);
    }
}
