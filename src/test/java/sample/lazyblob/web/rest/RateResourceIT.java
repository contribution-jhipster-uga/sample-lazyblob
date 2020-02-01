package sample.lazyblob.web.rest;

import sample.lazyblob.LazyblobApp;
import sample.lazyblob.domain.Rate;
import sample.lazyblob.domain.Photo;
import sample.lazyblob.domain.User;
import sample.lazyblob.repository.RateRepository;
import sample.lazyblob.service.RateService;
import sample.lazyblob.service.dto.RateDTO;
import sample.lazyblob.service.mapper.RateMapper;
import sample.lazyblob.web.rest.errors.ExceptionTranslator;
import sample.lazyblob.service.dto.RateCriteria;
import sample.lazyblob.service.RateQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static sample.lazyblob.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RateResource} REST controller.
 */
@SpringBootTest(classes = LazyblobApp.class)
public class RateResourceIT {

    private static final Integer DEFAULT_RATE = 0;
    private static final Integer UPDATED_RATE = 1;
    private static final Integer SMALLER_RATE = 0 - 1;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private RateRepository rateRepository;

    @Autowired
    private RateMapper rateMapper;

    @Autowired
    private RateService rateService;

    @Autowired
    private RateQueryService rateQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restRateMockMvc;

    private Rate rate;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RateResource rateResource = new RateResource(rateService, rateQueryService);
        this.restRateMockMvc = MockMvcBuilders.standaloneSetup(rateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rate createEntity(EntityManager em) {
        Rate rate = new Rate()
            .rate(DEFAULT_RATE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return rate;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rate createUpdatedEntity(EntityManager em) {
        Rate rate = new Rate()
            .rate(UPDATED_RATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return rate;
    }

    @BeforeEach
    public void initTest() {
        rate = createEntity(em);
    }

    @Test
    @Transactional
    public void createRate() throws Exception {
        int databaseSizeBeforeCreate = rateRepository.findAll().size();

        // Create the Rate
        RateDTO rateDTO = rateMapper.toDto(rate);
        restRateMockMvc.perform(post("/api/rates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rateDTO)))
            .andExpect(status().isCreated());

        // Validate the Rate in the database
        List<Rate> rateList = rateRepository.findAll();
        assertThat(rateList).hasSize(databaseSizeBeforeCreate + 1);
        Rate testRate = rateList.get(rateList.size() - 1);
        assertThat(testRate.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testRate.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testRate.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    public void createRateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rateRepository.findAll().size();

        // Create the Rate with an existing ID
        rate.setId(1L);
        RateDTO rateDTO = rateMapper.toDto(rate);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRateMockMvc.perform(post("/api/rates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Rate in the database
        List<Rate> rateList = rateRepository.findAll();
        assertThat(rateList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = rateRepository.findAll().size();
        // set the field null
        rate.setCreatedAt(null);

        // Create the Rate, which fails.
        RateDTO rateDTO = rateMapper.toDto(rate);

        restRateMockMvc.perform(post("/api/rates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rateDTO)))
            .andExpect(status().isBadRequest());

        List<Rate> rateList = rateRepository.findAll();
        assertThat(rateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRates() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);

        // Get all the rateList
        restRateMockMvc.perform(get("/api/rates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rate.getId().intValue())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getRate() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);

        // Get the rate
        restRateMockMvc.perform(get("/api/rates/{id}", rate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rate.getId().intValue()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }


    @Test
    @Transactional
    public void getRatesByIdFiltering() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);

        Long id = rate.getId();

        defaultRateShouldBeFound("id.equals=" + id);
        defaultRateShouldNotBeFound("id.notEquals=" + id);

        defaultRateShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRateShouldNotBeFound("id.greaterThan=" + id);

        defaultRateShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRateShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllRatesByRateIsEqualToSomething() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);

        // Get all the rateList where rate equals to DEFAULT_RATE
        defaultRateShouldBeFound("rate.equals=" + DEFAULT_RATE);

        // Get all the rateList where rate equals to UPDATED_RATE
        defaultRateShouldNotBeFound("rate.equals=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    public void getAllRatesByRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);

        // Get all the rateList where rate not equals to DEFAULT_RATE
        defaultRateShouldNotBeFound("rate.notEquals=" + DEFAULT_RATE);

        // Get all the rateList where rate not equals to UPDATED_RATE
        defaultRateShouldBeFound("rate.notEquals=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    public void getAllRatesByRateIsInShouldWork() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);

        // Get all the rateList where rate in DEFAULT_RATE or UPDATED_RATE
        defaultRateShouldBeFound("rate.in=" + DEFAULT_RATE + "," + UPDATED_RATE);

        // Get all the rateList where rate equals to UPDATED_RATE
        defaultRateShouldNotBeFound("rate.in=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    public void getAllRatesByRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);

        // Get all the rateList where rate is not null
        defaultRateShouldBeFound("rate.specified=true");

        // Get all the rateList where rate is null
        defaultRateShouldNotBeFound("rate.specified=false");
    }

    @Test
    @Transactional
    public void getAllRatesByRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);

        // Get all the rateList where rate is greater than or equal to DEFAULT_RATE
        defaultRateShouldBeFound("rate.greaterThanOrEqual=" + DEFAULT_RATE);

        // Get all the rateList where rate is greater than or equal to (DEFAULT_RATE + 1)
        defaultRateShouldNotBeFound("rate.greaterThanOrEqual=" + (DEFAULT_RATE + 1));
    }

    @Test
    @Transactional
    public void getAllRatesByRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);

        // Get all the rateList where rate is less than or equal to DEFAULT_RATE
        defaultRateShouldBeFound("rate.lessThanOrEqual=" + DEFAULT_RATE);

        // Get all the rateList where rate is less than or equal to SMALLER_RATE
        defaultRateShouldNotBeFound("rate.lessThanOrEqual=" + SMALLER_RATE);
    }

    @Test
    @Transactional
    public void getAllRatesByRateIsLessThanSomething() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);

        // Get all the rateList where rate is less than DEFAULT_RATE
        defaultRateShouldNotBeFound("rate.lessThan=" + DEFAULT_RATE);

        // Get all the rateList where rate is less than (DEFAULT_RATE + 1)
        defaultRateShouldBeFound("rate.lessThan=" + (DEFAULT_RATE + 1));
    }

    @Test
    @Transactional
    public void getAllRatesByRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);

        // Get all the rateList where rate is greater than DEFAULT_RATE
        defaultRateShouldNotBeFound("rate.greaterThan=" + DEFAULT_RATE);

        // Get all the rateList where rate is greater than SMALLER_RATE
        defaultRateShouldBeFound("rate.greaterThan=" + SMALLER_RATE);
    }


    @Test
    @Transactional
    public void getAllRatesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);

        // Get all the rateList where createdAt equals to DEFAULT_CREATED_AT
        defaultRateShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the rateList where createdAt equals to UPDATED_CREATED_AT
        defaultRateShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllRatesByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);

        // Get all the rateList where createdAt not equals to DEFAULT_CREATED_AT
        defaultRateShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the rateList where createdAt not equals to UPDATED_CREATED_AT
        defaultRateShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllRatesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);

        // Get all the rateList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultRateShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the rateList where createdAt equals to UPDATED_CREATED_AT
        defaultRateShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllRatesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);

        // Get all the rateList where createdAt is not null
        defaultRateShouldBeFound("createdAt.specified=true");

        // Get all the rateList where createdAt is null
        defaultRateShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllRatesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);

        // Get all the rateList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultRateShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the rateList where updatedAt equals to UPDATED_UPDATED_AT
        defaultRateShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllRatesByUpdatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);

        // Get all the rateList where updatedAt not equals to DEFAULT_UPDATED_AT
        defaultRateShouldNotBeFound("updatedAt.notEquals=" + DEFAULT_UPDATED_AT);

        // Get all the rateList where updatedAt not equals to UPDATED_UPDATED_AT
        defaultRateShouldBeFound("updatedAt.notEquals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllRatesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);

        // Get all the rateList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultRateShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the rateList where updatedAt equals to UPDATED_UPDATED_AT
        defaultRateShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllRatesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);

        // Get all the rateList where updatedAt is not null
        defaultRateShouldBeFound("updatedAt.specified=true");

        // Get all the rateList where updatedAt is null
        defaultRateShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllRatesByPhotoIsEqualToSomething() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);
        Photo photo = PhotoResourceIT.createEntity(em);
        em.persist(photo);
        em.flush();
        rate.setPhoto(photo);
        rateRepository.saveAndFlush(rate);
        Long photoId = photo.getId();

        // Get all the rateList where photo equals to photoId
        defaultRateShouldBeFound("photoId.equals=" + photoId);

        // Get all the rateList where photo equals to photoId + 1
        defaultRateShouldNotBeFound("photoId.equals=" + (photoId + 1));
    }


    @Test
    @Transactional
    public void getAllRatesByFromIsEqualToSomething() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);
        User from = UserResourceIT.createEntity(em);
        em.persist(from);
        em.flush();
        rate.setFrom(from);
        rateRepository.saveAndFlush(rate);
        Long fromId = from.getId();

        // Get all the rateList where from equals to fromId
        defaultRateShouldBeFound("fromId.equals=" + fromId);

        // Get all the rateList where from equals to fromId + 1
        defaultRateShouldNotBeFound("fromId.equals=" + (fromId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRateShouldBeFound(String filter) throws Exception {
        restRateMockMvc.perform(get("/api/rates?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rate.getId().intValue())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restRateMockMvc.perform(get("/api/rates/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRateShouldNotBeFound(String filter) throws Exception {
        restRateMockMvc.perform(get("/api/rates?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRateMockMvc.perform(get("/api/rates/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRate() throws Exception {
        // Get the rate
        restRateMockMvc.perform(get("/api/rates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRate() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);

        int databaseSizeBeforeUpdate = rateRepository.findAll().size();

        // Update the rate
        Rate updatedRate = rateRepository.findById(rate.getId()).get();
        // Disconnect from session so that the updates on updatedRate are not directly saved in db
        em.detach(updatedRate);
        updatedRate
            .rate(UPDATED_RATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        RateDTO rateDTO = rateMapper.toDto(updatedRate);

        restRateMockMvc.perform(put("/api/rates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rateDTO)))
            .andExpect(status().isOk());

        // Validate the Rate in the database
        List<Rate> rateList = rateRepository.findAll();
        assertThat(rateList).hasSize(databaseSizeBeforeUpdate);
        Rate testRate = rateList.get(rateList.size() - 1);
        assertThat(testRate.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testRate.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testRate.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingRate() throws Exception {
        int databaseSizeBeforeUpdate = rateRepository.findAll().size();

        // Create the Rate
        RateDTO rateDTO = rateMapper.toDto(rate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRateMockMvc.perform(put("/api/rates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Rate in the database
        List<Rate> rateList = rateRepository.findAll();
        assertThat(rateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRate() throws Exception {
        // Initialize the database
        rateRepository.saveAndFlush(rate);

        int databaseSizeBeforeDelete = rateRepository.findAll().size();

        // Delete the rate
        restRateMockMvc.perform(delete("/api/rates/{id}", rate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rate> rateList = rateRepository.findAll();
        assertThat(rateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
