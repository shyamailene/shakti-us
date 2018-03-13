package com.vemuri.shaktius.web.rest;

import com.vemuri.shaktius.ShaktiusApp;

import com.vemuri.shaktius.domain.Signup;
import com.vemuri.shaktius.repository.SignupRepository;
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
 * Test class for the SignupResource REST controller.
 *
 * @see SignupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShaktiusApp.class)
public class SignupResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_ZIPCODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIPCODE = "BBBBBBBBBB";

    private static final String DEFAULT_PARENT_F_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_F_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PARENT_L_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_L_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PARENT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PARENT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_PHONE = "BBBBBBBBBB";

    @Autowired
    private SignupRepository signupRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSignupMockMvc;

    private Signup signup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SignupResource signupResource = new SignupResource(signupRepository);
        this.restSignupMockMvc = MockMvcBuilders.standaloneSetup(signupResource)
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
    public static Signup createEntity(EntityManager em) {
        Signup signup = new Signup()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .line1(DEFAULT_LINE_1)
            .line2(DEFAULT_LINE_2)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .country(DEFAULT_COUNTRY)
            .zipcode(DEFAULT_ZIPCODE)
            .parentFName(DEFAULT_PARENT_F_NAME)
            .parentLName(DEFAULT_PARENT_L_NAME)
            .parentEmail(DEFAULT_PARENT_EMAIL)
            .parentPhone(DEFAULT_PARENT_PHONE);
        return signup;
    }

    @Before
    public void initTest() {
        signup = createEntity(em);
    }

    @Test
    @Transactional
    public void createSignup() throws Exception {
        int databaseSizeBeforeCreate = signupRepository.findAll().size();

        // Create the Signup
        restSignupMockMvc.perform(post("/api/signups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(signup)))
            .andExpect(status().isCreated());

        // Validate the Signup in the database
        List<Signup> signupList = signupRepository.findAll();
        assertThat(signupList).hasSize(databaseSizeBeforeCreate + 1);
        Signup testSignup = signupList.get(signupList.size() - 1);
        assertThat(testSignup.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testSignup.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testSignup.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSignup.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testSignup.getLine1()).isEqualTo(DEFAULT_LINE_1);
        assertThat(testSignup.getLine2()).isEqualTo(DEFAULT_LINE_2);
        assertThat(testSignup.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testSignup.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testSignup.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testSignup.getZipcode()).isEqualTo(DEFAULT_ZIPCODE);
        assertThat(testSignup.getParentFName()).isEqualTo(DEFAULT_PARENT_F_NAME);
        assertThat(testSignup.getParentLName()).isEqualTo(DEFAULT_PARENT_L_NAME);
        assertThat(testSignup.getParentEmail()).isEqualTo(DEFAULT_PARENT_EMAIL);
        assertThat(testSignup.getParentPhone()).isEqualTo(DEFAULT_PARENT_PHONE);
    }

    @Test
    @Transactional
    public void createSignupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = signupRepository.findAll().size();

        // Create the Signup with an existing ID
        signup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSignupMockMvc.perform(post("/api/signups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(signup)))
            .andExpect(status().isBadRequest());

        // Validate the Signup in the database
        List<Signup> signupList = signupRepository.findAll();
        assertThat(signupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSignups() throws Exception {
        // Initialize the database
        signupRepository.saveAndFlush(signup);

        // Get all the signupList
        restSignupMockMvc.perform(get("/api/signups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(signup.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].line1").value(hasItem(DEFAULT_LINE_1.toString())))
            .andExpect(jsonPath("$.[*].line2").value(hasItem(DEFAULT_LINE_2.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].zipcode").value(hasItem(DEFAULT_ZIPCODE.toString())))
            .andExpect(jsonPath("$.[*].parentFName").value(hasItem(DEFAULT_PARENT_F_NAME.toString())))
            .andExpect(jsonPath("$.[*].parentLName").value(hasItem(DEFAULT_PARENT_L_NAME.toString())))
            .andExpect(jsonPath("$.[*].parentEmail").value(hasItem(DEFAULT_PARENT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].parentPhone").value(hasItem(DEFAULT_PARENT_PHONE.toString())));
    }

    @Test
    @Transactional
    public void getSignup() throws Exception {
        // Initialize the database
        signupRepository.saveAndFlush(signup);

        // Get the signup
        restSignupMockMvc.perform(get("/api/signups/{id}", signup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(signup.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.line1").value(DEFAULT_LINE_1.toString()))
            .andExpect(jsonPath("$.line2").value(DEFAULT_LINE_2.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.zipcode").value(DEFAULT_ZIPCODE.toString()))
            .andExpect(jsonPath("$.parentFName").value(DEFAULT_PARENT_F_NAME.toString()))
            .andExpect(jsonPath("$.parentLName").value(DEFAULT_PARENT_L_NAME.toString()))
            .andExpect(jsonPath("$.parentEmail").value(DEFAULT_PARENT_EMAIL.toString()))
            .andExpect(jsonPath("$.parentPhone").value(DEFAULT_PARENT_PHONE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSignup() throws Exception {
        // Get the signup
        restSignupMockMvc.perform(get("/api/signups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSignup() throws Exception {
        // Initialize the database
        signupRepository.saveAndFlush(signup);
        int databaseSizeBeforeUpdate = signupRepository.findAll().size();

        // Update the signup
        Signup updatedSignup = signupRepository.findOne(signup.getId());
        // Disconnect from session so that the updates on updatedSignup are not directly saved in db
        em.detach(updatedSignup);
        updatedSignup
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .line1(UPDATED_LINE_1)
            .line2(UPDATED_LINE_2)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .country(UPDATED_COUNTRY)
            .zipcode(UPDATED_ZIPCODE)
            .parentFName(UPDATED_PARENT_F_NAME)
            .parentLName(UPDATED_PARENT_L_NAME)
            .parentEmail(UPDATED_PARENT_EMAIL)
            .parentPhone(UPDATED_PARENT_PHONE);

        restSignupMockMvc.perform(put("/api/signups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSignup)))
            .andExpect(status().isOk());

        // Validate the Signup in the database
        List<Signup> signupList = signupRepository.findAll();
        assertThat(signupList).hasSize(databaseSizeBeforeUpdate);
        Signup testSignup = signupList.get(signupList.size() - 1);
        assertThat(testSignup.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testSignup.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testSignup.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSignup.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testSignup.getLine1()).isEqualTo(UPDATED_LINE_1);
        assertThat(testSignup.getLine2()).isEqualTo(UPDATED_LINE_2);
        assertThat(testSignup.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testSignup.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testSignup.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testSignup.getZipcode()).isEqualTo(UPDATED_ZIPCODE);
        assertThat(testSignup.getParentFName()).isEqualTo(UPDATED_PARENT_F_NAME);
        assertThat(testSignup.getParentLName()).isEqualTo(UPDATED_PARENT_L_NAME);
        assertThat(testSignup.getParentEmail()).isEqualTo(UPDATED_PARENT_EMAIL);
        assertThat(testSignup.getParentPhone()).isEqualTo(UPDATED_PARENT_PHONE);
    }

    @Test
    @Transactional
    public void updateNonExistingSignup() throws Exception {
        int databaseSizeBeforeUpdate = signupRepository.findAll().size();

        // Create the Signup

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSignupMockMvc.perform(put("/api/signups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(signup)))
            .andExpect(status().isCreated());

        // Validate the Signup in the database
        List<Signup> signupList = signupRepository.findAll();
        assertThat(signupList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSignup() throws Exception {
        // Initialize the database
        signupRepository.saveAndFlush(signup);
        int databaseSizeBeforeDelete = signupRepository.findAll().size();

        // Get the signup
        restSignupMockMvc.perform(delete("/api/signups/{id}", signup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Signup> signupList = signupRepository.findAll();
        assertThat(signupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Signup.class);
        Signup signup1 = new Signup();
        signup1.setId(1L);
        Signup signup2 = new Signup();
        signup2.setId(signup1.getId());
        assertThat(signup1).isEqualTo(signup2);
        signup2.setId(2L);
        assertThat(signup1).isNotEqualTo(signup2);
        signup1.setId(null);
        assertThat(signup1).isNotEqualTo(signup2);
    }
}
