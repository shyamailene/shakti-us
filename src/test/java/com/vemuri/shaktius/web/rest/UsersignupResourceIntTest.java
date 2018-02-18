package com.vemuri.shaktius.web.rest;

import com.vemuri.shaktius.ShaktiusApp;

import com.vemuri.shaktius.domain.Usersignup;
import com.vemuri.shaktius.repository.UsersignupRepository;
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
 * Test class for the UsersignupResource REST controller.
 *
 * @see UsersignupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShaktiusApp.class)
public class UsersignupResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONENUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONENUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    @Autowired
    private UsersignupRepository usersignupRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUsersignupMockMvc;

    private Usersignup usersignup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UsersignupResource usersignupResource = new UsersignupResource(usersignupRepository);
        this.restUsersignupMockMvc = MockMvcBuilders.standaloneSetup(usersignupResource)
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
    public static Usersignup createEntity(EntityManager em) {
        Usersignup usersignup = new Usersignup()
            .name(DEFAULT_NAME)
            .phonenumber(DEFAULT_PHONENUMBER)
            .email(DEFAULT_EMAIL)
            .notes(DEFAULT_NOTES);
        return usersignup;
    }

    @Before
    public void initTest() {
        usersignup = createEntity(em);
    }

    @Test
    @Transactional
    public void createUsersignup() throws Exception {
        int databaseSizeBeforeCreate = usersignupRepository.findAll().size();

        // Create the Usersignup
        restUsersignupMockMvc.perform(post("/api/usersignups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(usersignup)))
            .andExpect(status().isCreated());

        // Validate the Usersignup in the database
        List<Usersignup> usersignupList = usersignupRepository.findAll();
        assertThat(usersignupList).hasSize(databaseSizeBeforeCreate + 1);
        Usersignup testUsersignup = usersignupList.get(usersignupList.size() - 1);
        assertThat(testUsersignup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUsersignup.getPhonenumber()).isEqualTo(DEFAULT_PHONENUMBER);
        assertThat(testUsersignup.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUsersignup.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    public void createUsersignupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = usersignupRepository.findAll().size();

        // Create the Usersignup with an existing ID
        usersignup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsersignupMockMvc.perform(post("/api/usersignups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(usersignup)))
            .andExpect(status().isBadRequest());

        // Validate the Usersignup in the database
        List<Usersignup> usersignupList = usersignupRepository.findAll();
        assertThat(usersignupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = usersignupRepository.findAll().size();
        // set the field null
        usersignup.setName(null);

        // Create the Usersignup, which fails.

        restUsersignupMockMvc.perform(post("/api/usersignups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(usersignup)))
            .andExpect(status().isBadRequest());

        List<Usersignup> usersignupList = usersignupRepository.findAll();
        assertThat(usersignupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhonenumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = usersignupRepository.findAll().size();
        // set the field null
        usersignup.setPhonenumber(null);

        // Create the Usersignup, which fails.

        restUsersignupMockMvc.perform(post("/api/usersignups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(usersignup)))
            .andExpect(status().isBadRequest());

        List<Usersignup> usersignupList = usersignupRepository.findAll();
        assertThat(usersignupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = usersignupRepository.findAll().size();
        // set the field null
        usersignup.setEmail(null);

        // Create the Usersignup, which fails.

        restUsersignupMockMvc.perform(post("/api/usersignups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(usersignup)))
            .andExpect(status().isBadRequest());

        List<Usersignup> usersignupList = usersignupRepository.findAll();
        assertThat(usersignupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUsersignups() throws Exception {
        // Initialize the database
        usersignupRepository.saveAndFlush(usersignup);

        // Get all the usersignupList
        restUsersignupMockMvc.perform(get("/api/usersignups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usersignup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].phonenumber").value(hasItem(DEFAULT_PHONENUMBER.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @Test
    @Transactional
    public void getUsersignup() throws Exception {
        // Initialize the database
        usersignupRepository.saveAndFlush(usersignup);

        // Get the usersignup
        restUsersignupMockMvc.perform(get("/api/usersignups/{id}", usersignup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(usersignup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.phonenumber").value(DEFAULT_PHONENUMBER.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUsersignup() throws Exception {
        // Get the usersignup
        restUsersignupMockMvc.perform(get("/api/usersignups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUsersignup() throws Exception {
        // Initialize the database
        usersignupRepository.saveAndFlush(usersignup);
        int databaseSizeBeforeUpdate = usersignupRepository.findAll().size();

        // Update the usersignup
        Usersignup updatedUsersignup = usersignupRepository.findOne(usersignup.getId());
        // Disconnect from session so that the updates on updatedUsersignup are not directly saved in db
        em.detach(updatedUsersignup);
        updatedUsersignup
            .name(UPDATED_NAME)
            .phonenumber(UPDATED_PHONENUMBER)
            .email(UPDATED_EMAIL)
            .notes(UPDATED_NOTES);

        restUsersignupMockMvc.perform(put("/api/usersignups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUsersignup)))
            .andExpect(status().isOk());

        // Validate the Usersignup in the database
        List<Usersignup> usersignupList = usersignupRepository.findAll();
        assertThat(usersignupList).hasSize(databaseSizeBeforeUpdate);
        Usersignup testUsersignup = usersignupList.get(usersignupList.size() - 1);
        assertThat(testUsersignup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUsersignup.getPhonenumber()).isEqualTo(UPDATED_PHONENUMBER);
        assertThat(testUsersignup.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUsersignup.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    public void updateNonExistingUsersignup() throws Exception {
        int databaseSizeBeforeUpdate = usersignupRepository.findAll().size();

        // Create the Usersignup

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUsersignupMockMvc.perform(put("/api/usersignups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(usersignup)))
            .andExpect(status().isCreated());

        // Validate the Usersignup in the database
        List<Usersignup> usersignupList = usersignupRepository.findAll();
        assertThat(usersignupList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUsersignup() throws Exception {
        // Initialize the database
        usersignupRepository.saveAndFlush(usersignup);
        int databaseSizeBeforeDelete = usersignupRepository.findAll().size();

        // Get the usersignup
        restUsersignupMockMvc.perform(delete("/api/usersignups/{id}", usersignup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Usersignup> usersignupList = usersignupRepository.findAll();
        assertThat(usersignupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Usersignup.class);
        Usersignup usersignup1 = new Usersignup();
        usersignup1.setId(1L);
        Usersignup usersignup2 = new Usersignup();
        usersignup2.setId(usersignup1.getId());
        assertThat(usersignup1).isEqualTo(usersignup2);
        usersignup2.setId(2L);
        assertThat(usersignup1).isNotEqualTo(usersignup2);
        usersignup1.setId(null);
        assertThat(usersignup1).isNotEqualTo(usersignup2);
    }
}
