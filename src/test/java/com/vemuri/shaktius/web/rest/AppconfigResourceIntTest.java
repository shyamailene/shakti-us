package com.vemuri.shaktius.web.rest;

import com.vemuri.shaktius.ShaktiusApp;

import com.vemuri.shaktius.domain.Appconfig;
import com.vemuri.shaktius.repository.AppconfigRepository;
import com.vemuri.shaktius.service.AppconfigService;
import com.vemuri.shaktius.web.rest.errors.ExceptionTranslator;
import com.vemuri.shaktius.service.dto.AppconfigCriteria;
import com.vemuri.shaktius.service.AppconfigQueryService;

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
 * Test class for the AppconfigResource REST controller.
 *
 * @see AppconfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShaktiusApp.class)
public class AppconfigResourceIntTest {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private AppconfigRepository appconfigRepository;

    @Autowired
    private AppconfigService appconfigService;

    @Autowired
    private AppconfigQueryService appconfigQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAppconfigMockMvc;

    private Appconfig appconfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AppconfigResource appconfigResource = new AppconfigResource(appconfigService, appconfigQueryService);
        this.restAppconfigMockMvc = MockMvcBuilders.standaloneSetup(appconfigResource)
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
    public static Appconfig createEntity(EntityManager em) {
        Appconfig appconfig = new Appconfig()
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE);
        return appconfig;
    }

    @Before
    public void initTest() {
        appconfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createAppconfig() throws Exception {
        int databaseSizeBeforeCreate = appconfigRepository.findAll().size();

        // Create the Appconfig
        restAppconfigMockMvc.perform(post("/api/appconfigs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appconfig)))
            .andExpect(status().isCreated());

        // Validate the Appconfig in the database
        List<Appconfig> appconfigList = appconfigRepository.findAll();
        assertThat(appconfigList).hasSize(databaseSizeBeforeCreate + 1);
        Appconfig testAppconfig = appconfigList.get(appconfigList.size() - 1);
        assertThat(testAppconfig.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testAppconfig.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createAppconfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appconfigRepository.findAll().size();

        // Create the Appconfig with an existing ID
        appconfig.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppconfigMockMvc.perform(post("/api/appconfigs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appconfig)))
            .andExpect(status().isBadRequest());

        // Validate the Appconfig in the database
        List<Appconfig> appconfigList = appconfigRepository.findAll();
        assertThat(appconfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = appconfigRepository.findAll().size();
        // set the field null
        appconfig.setKey(null);

        // Create the Appconfig, which fails.

        restAppconfigMockMvc.perform(post("/api/appconfigs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appconfig)))
            .andExpect(status().isBadRequest());

        List<Appconfig> appconfigList = appconfigRepository.findAll();
        assertThat(appconfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = appconfigRepository.findAll().size();
        // set the field null
        appconfig.setValue(null);

        // Create the Appconfig, which fails.

        restAppconfigMockMvc.perform(post("/api/appconfigs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appconfig)))
            .andExpect(status().isBadRequest());

        List<Appconfig> appconfigList = appconfigRepository.findAll();
        assertThat(appconfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAppconfigs() throws Exception {
        // Initialize the database
        appconfigRepository.saveAndFlush(appconfig);

        // Get all the appconfigList
        restAppconfigMockMvc.perform(get("/api/appconfigs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appconfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getAppconfig() throws Exception {
        // Initialize the database
        appconfigRepository.saveAndFlush(appconfig);

        // Get the appconfig
        restAppconfigMockMvc.perform(get("/api/appconfigs/{id}", appconfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(appconfig.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getAllAppconfigsByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        appconfigRepository.saveAndFlush(appconfig);

        // Get all the appconfigList where key equals to DEFAULT_KEY
        defaultAppconfigShouldBeFound("key.equals=" + DEFAULT_KEY);

        // Get all the appconfigList where key equals to UPDATED_KEY
        defaultAppconfigShouldNotBeFound("key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllAppconfigsByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        appconfigRepository.saveAndFlush(appconfig);

        // Get all the appconfigList where key in DEFAULT_KEY or UPDATED_KEY
        defaultAppconfigShouldBeFound("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY);

        // Get all the appconfigList where key equals to UPDATED_KEY
        defaultAppconfigShouldNotBeFound("key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllAppconfigsByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        appconfigRepository.saveAndFlush(appconfig);

        // Get all the appconfigList where key is not null
        defaultAppconfigShouldBeFound("key.specified=true");

        // Get all the appconfigList where key is null
        defaultAppconfigShouldNotBeFound("key.specified=false");
    }

    @Test
    @Transactional
    public void getAllAppconfigsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        appconfigRepository.saveAndFlush(appconfig);

        // Get all the appconfigList where value equals to DEFAULT_VALUE
        defaultAppconfigShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the appconfigList where value equals to UPDATED_VALUE
        defaultAppconfigShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllAppconfigsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        appconfigRepository.saveAndFlush(appconfig);

        // Get all the appconfigList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultAppconfigShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the appconfigList where value equals to UPDATED_VALUE
        defaultAppconfigShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllAppconfigsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        appconfigRepository.saveAndFlush(appconfig);

        // Get all the appconfigList where value is not null
        defaultAppconfigShouldBeFound("value.specified=true");

        // Get all the appconfigList where value is null
        defaultAppconfigShouldNotBeFound("value.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAppconfigShouldBeFound(String filter) throws Exception {
        restAppconfigMockMvc.perform(get("/api/appconfigs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appconfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAppconfigShouldNotBeFound(String filter) throws Exception {
        restAppconfigMockMvc.perform(get("/api/appconfigs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingAppconfig() throws Exception {
        // Get the appconfig
        restAppconfigMockMvc.perform(get("/api/appconfigs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppconfig() throws Exception {
        // Initialize the database
        appconfigService.save(appconfig);

        int databaseSizeBeforeUpdate = appconfigRepository.findAll().size();

        // Update the appconfig
        Appconfig updatedAppconfig = appconfigRepository.findOne(appconfig.getId());
        // Disconnect from session so that the updates on updatedAppconfig are not directly saved in db
        em.detach(updatedAppconfig);
        updatedAppconfig
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE);

        restAppconfigMockMvc.perform(put("/api/appconfigs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAppconfig)))
            .andExpect(status().isOk());

        // Validate the Appconfig in the database
        List<Appconfig> appconfigList = appconfigRepository.findAll();
        assertThat(appconfigList).hasSize(databaseSizeBeforeUpdate);
        Appconfig testAppconfig = appconfigList.get(appconfigList.size() - 1);
        assertThat(testAppconfig.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testAppconfig.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingAppconfig() throws Exception {
        int databaseSizeBeforeUpdate = appconfigRepository.findAll().size();

        // Create the Appconfig

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAppconfigMockMvc.perform(put("/api/appconfigs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appconfig)))
            .andExpect(status().isCreated());

        // Validate the Appconfig in the database
        List<Appconfig> appconfigList = appconfigRepository.findAll();
        assertThat(appconfigList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAppconfig() throws Exception {
        // Initialize the database
        appconfigService.save(appconfig);

        int databaseSizeBeforeDelete = appconfigRepository.findAll().size();

        // Get the appconfig
        restAppconfigMockMvc.perform(delete("/api/appconfigs/{id}", appconfig.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Appconfig> appconfigList = appconfigRepository.findAll();
        assertThat(appconfigList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Appconfig.class);
        Appconfig appconfig1 = new Appconfig();
        appconfig1.setId(1L);
        Appconfig appconfig2 = new Appconfig();
        appconfig2.setId(appconfig1.getId());
        assertThat(appconfig1).isEqualTo(appconfig2);
        appconfig2.setId(2L);
        assertThat(appconfig1).isNotEqualTo(appconfig2);
        appconfig1.setId(null);
        assertThat(appconfig1).isNotEqualTo(appconfig2);
    }
}
