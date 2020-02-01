package sample.lazyblob.web.rest;

import sample.lazyblob.LazyblobApp;
import sample.lazyblob.domain.Album;
import sample.lazyblob.domain.User;
import sample.lazyblob.repository.AlbumRepository;
import sample.lazyblob.service.AlbumService;
import sample.lazyblob.service.dto.AlbumDTO;
import sample.lazyblob.service.mapper.AlbumMapper;
import sample.lazyblob.web.rest.errors.ExceptionTranslator;
import sample.lazyblob.service.dto.AlbumCriteria;
import sample.lazyblob.service.AlbumQueryService;

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
 * Integration tests for the {@link AlbumResource} REST controller.
 */
@SpringBootTest(classes = LazyblobApp.class)
public class AlbumResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private AlbumMapper albumMapper;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private AlbumQueryService albumQueryService;

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

    private MockMvc restAlbumMockMvc;

    private Album album;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AlbumResource albumResource = new AlbumResource(albumService, albumQueryService);
        this.restAlbumMockMvc = MockMvcBuilders.standaloneSetup(albumResource)
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
    public static Album createEntity(EntityManager em) {
        Album album = new Album()
            .title(DEFAULT_TITLE)
            .note(DEFAULT_NOTE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return album;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Album createUpdatedEntity(EntityManager em) {
        Album album = new Album()
            .title(UPDATED_TITLE)
            .note(UPDATED_NOTE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return album;
    }

    @BeforeEach
    public void initTest() {
        album = createEntity(em);
    }

    @Test
    @Transactional
    public void createAlbum() throws Exception {
        int databaseSizeBeforeCreate = albumRepository.findAll().size();

        // Create the Album
        AlbumDTO albumDTO = albumMapper.toDto(album);
        restAlbumMockMvc.perform(post("/api/albums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(albumDTO)))
            .andExpect(status().isCreated());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeCreate + 1);
        Album testAlbum = albumList.get(albumList.size() - 1);
        assertThat(testAlbum.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAlbum.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testAlbum.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAlbum.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    public void createAlbumWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = albumRepository.findAll().size();

        // Create the Album with an existing ID
        album.setId(1L);
        AlbumDTO albumDTO = albumMapper.toDto(album);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlbumMockMvc.perform(post("/api/albums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(albumDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = albumRepository.findAll().size();
        // set the field null
        album.setCreatedAt(null);

        // Create the Album, which fails.
        AlbumDTO albumDTO = albumMapper.toDto(album);

        restAlbumMockMvc.perform(post("/api/albums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(albumDTO)))
            .andExpect(status().isBadRequest());

        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAlbums() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList
        restAlbumMockMvc.perform(get("/api/albums?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(album.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getAlbum() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get the album
        restAlbumMockMvc.perform(get("/api/albums/{id}", album.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(album.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }


    @Test
    @Transactional
    public void getAlbumsByIdFiltering() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        Long id = album.getId();

        defaultAlbumShouldBeFound("id.equals=" + id);
        defaultAlbumShouldNotBeFound("id.notEquals=" + id);

        defaultAlbumShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAlbumShouldNotBeFound("id.greaterThan=" + id);

        defaultAlbumShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAlbumShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAlbumsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where title equals to DEFAULT_TITLE
        defaultAlbumShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the albumList where title equals to UPDATED_TITLE
        defaultAlbumShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllAlbumsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where title not equals to DEFAULT_TITLE
        defaultAlbumShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the albumList where title not equals to UPDATED_TITLE
        defaultAlbumShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllAlbumsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultAlbumShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the albumList where title equals to UPDATED_TITLE
        defaultAlbumShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllAlbumsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where title is not null
        defaultAlbumShouldBeFound("title.specified=true");

        // Get all the albumList where title is null
        defaultAlbumShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllAlbumsByTitleContainsSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where title contains DEFAULT_TITLE
        defaultAlbumShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the albumList where title contains UPDATED_TITLE
        defaultAlbumShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllAlbumsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where title does not contain DEFAULT_TITLE
        defaultAlbumShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the albumList where title does not contain UPDATED_TITLE
        defaultAlbumShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllAlbumsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where note equals to DEFAULT_NOTE
        defaultAlbumShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the albumList where note equals to UPDATED_NOTE
        defaultAlbumShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllAlbumsByNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where note not equals to DEFAULT_NOTE
        defaultAlbumShouldNotBeFound("note.notEquals=" + DEFAULT_NOTE);

        // Get all the albumList where note not equals to UPDATED_NOTE
        defaultAlbumShouldBeFound("note.notEquals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllAlbumsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultAlbumShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the albumList where note equals to UPDATED_NOTE
        defaultAlbumShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllAlbumsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where note is not null
        defaultAlbumShouldBeFound("note.specified=true");

        // Get all the albumList where note is null
        defaultAlbumShouldNotBeFound("note.specified=false");
    }
                @Test
    @Transactional
    public void getAllAlbumsByNoteContainsSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where note contains DEFAULT_NOTE
        defaultAlbumShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the albumList where note contains UPDATED_NOTE
        defaultAlbumShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllAlbumsByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where note does not contain DEFAULT_NOTE
        defaultAlbumShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the albumList where note does not contain UPDATED_NOTE
        defaultAlbumShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }


    @Test
    @Transactional
    public void getAllAlbumsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where createdAt equals to DEFAULT_CREATED_AT
        defaultAlbumShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the albumList where createdAt equals to UPDATED_CREATED_AT
        defaultAlbumShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllAlbumsByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where createdAt not equals to DEFAULT_CREATED_AT
        defaultAlbumShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the albumList where createdAt not equals to UPDATED_CREATED_AT
        defaultAlbumShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllAlbumsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultAlbumShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the albumList where createdAt equals to UPDATED_CREATED_AT
        defaultAlbumShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllAlbumsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where createdAt is not null
        defaultAlbumShouldBeFound("createdAt.specified=true");

        // Get all the albumList where createdAt is null
        defaultAlbumShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllAlbumsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultAlbumShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the albumList where updatedAt equals to UPDATED_UPDATED_AT
        defaultAlbumShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllAlbumsByUpdatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where updatedAt not equals to DEFAULT_UPDATED_AT
        defaultAlbumShouldNotBeFound("updatedAt.notEquals=" + DEFAULT_UPDATED_AT);

        // Get all the albumList where updatedAt not equals to UPDATED_UPDATED_AT
        defaultAlbumShouldBeFound("updatedAt.notEquals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllAlbumsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultAlbumShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the albumList where updatedAt equals to UPDATED_UPDATED_AT
        defaultAlbumShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllAlbumsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        // Get all the albumList where updatedAt is not null
        defaultAlbumShouldBeFound("updatedAt.specified=true");

        // Get all the albumList where updatedAt is null
        defaultAlbumShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllAlbumsByOwnedByIsEqualToSomething() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);
        User ownedBy = UserResourceIT.createEntity(em);
        em.persist(ownedBy);
        em.flush();
        album.setOwnedBy(ownedBy);
        albumRepository.saveAndFlush(album);
        Long ownedById = ownedBy.getId();

        // Get all the albumList where ownedBy equals to ownedById
        defaultAlbumShouldBeFound("ownedById.equals=" + ownedById);

        // Get all the albumList where ownedBy equals to ownedById + 1
        defaultAlbumShouldNotBeFound("ownedById.equals=" + (ownedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlbumShouldBeFound(String filter) throws Exception {
        restAlbumMockMvc.perform(get("/api/albums?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(album.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restAlbumMockMvc.perform(get("/api/albums/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlbumShouldNotBeFound(String filter) throws Exception {
        restAlbumMockMvc.perform(get("/api/albums?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlbumMockMvc.perform(get("/api/albums/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAlbum() throws Exception {
        // Get the album
        restAlbumMockMvc.perform(get("/api/albums/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAlbum() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        int databaseSizeBeforeUpdate = albumRepository.findAll().size();

        // Update the album
        Album updatedAlbum = albumRepository.findById(album.getId()).get();
        // Disconnect from session so that the updates on updatedAlbum are not directly saved in db
        em.detach(updatedAlbum);
        updatedAlbum
            .title(UPDATED_TITLE)
            .note(UPDATED_NOTE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        AlbumDTO albumDTO = albumMapper.toDto(updatedAlbum);

        restAlbumMockMvc.perform(put("/api/albums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(albumDTO)))
            .andExpect(status().isOk());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeUpdate);
        Album testAlbum = albumList.get(albumList.size() - 1);
        assertThat(testAlbum.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAlbum.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testAlbum.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAlbum.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingAlbum() throws Exception {
        int databaseSizeBeforeUpdate = albumRepository.findAll().size();

        // Create the Album
        AlbumDTO albumDTO = albumMapper.toDto(album);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlbumMockMvc.perform(put("/api/albums")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(albumDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Album in the database
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAlbum() throws Exception {
        // Initialize the database
        albumRepository.saveAndFlush(album);

        int databaseSizeBeforeDelete = albumRepository.findAll().size();

        // Delete the album
        restAlbumMockMvc.perform(delete("/api/albums/{id}", album.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Album> albumList = albumRepository.findAll();
        assertThat(albumList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
