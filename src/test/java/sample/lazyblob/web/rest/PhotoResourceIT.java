package sample.lazyblob.web.rest;

import sample.lazyblob.LazyblobApp;
import sample.lazyblob.domain.Photo;
import sample.lazyblob.domain.Album;
import sample.lazyblob.repository.PhotoRepository;
import sample.lazyblob.service.PhotoService;
import sample.lazyblob.service.dto.PhotoDTO;
import sample.lazyblob.service.mapper.PhotoMapper;
import sample.lazyblob.web.rest.errors.ExceptionTranslator;
import sample.lazyblob.service.dto.PhotoCriteria;
import sample.lazyblob.service.PhotoQueryService;

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
import org.springframework.util.Base64Utils;
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
 * Integration tests for the {@link PhotoResource} REST controller.
 */
@SpringBootTest(classes = LazyblobApp.class)
public class PhotoResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_IMAGE_SHA_1 = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_IMAGE_SHA_1 = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final byte[] DEFAULT_THUMBNAILX_1 = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_THUMBNAILX_1 = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_THUMBNAILX_1_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_THUMBNAILX_1_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_THUMBNAILX_1_SHA_1 = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_THUMBNAILX_1_SHA_1 = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final byte[] DEFAULT_THUMBNAILX_2 = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_THUMBNAILX_2 = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_THUMBNAILX_2_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_THUMBNAILX_2_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_THUMBNAILX_2_SHA_1 = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_THUMBNAILX_2_SHA_1 = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final String DEFAULT_EXIF = "AAAAAAAAAA";
    private static final String UPDATED_EXIF = "BBBBBBBBBB";

    private static final String DEFAULT_EXTRACTED_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_EXTRACTED_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_DETECTED_OBJECTS = "AAAAAAAAAA";
    private static final String UPDATED_DETECTED_OBJECTS = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PhotoMapper photoMapper;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private PhotoQueryService photoQueryService;

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

    private MockMvc restPhotoMockMvc;

    private Photo photo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PhotoResource photoResource = new PhotoResource(photoService, photoQueryService);
        this.restPhotoMockMvc = MockMvcBuilders.standaloneSetup(photoResource)
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
    public static Photo createEntity(EntityManager em) {
        Photo photo = new Photo()
            .title(DEFAULT_TITLE)
            .note(DEFAULT_NOTE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .imageSha1(DEFAULT_IMAGE_SHA_1)
            .thumbnailx1(DEFAULT_THUMBNAILX_1)
            .thumbnailx1ContentType(DEFAULT_THUMBNAILX_1_CONTENT_TYPE)
            .thumbnailx1Sha1(DEFAULT_THUMBNAILX_1_SHA_1)
            .thumbnailx2(DEFAULT_THUMBNAILX_2)
            .thumbnailx2ContentType(DEFAULT_THUMBNAILX_2_CONTENT_TYPE)
            .thumbnailx2Sha1(DEFAULT_THUMBNAILX_2_SHA_1)
            .exif(DEFAULT_EXIF)
            .extractedText(DEFAULT_EXTRACTED_TEXT)
            .detectedObjects(DEFAULT_DETECTED_OBJECTS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return photo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Photo createUpdatedEntity(EntityManager em) {
        Photo photo = new Photo()
            .title(UPDATED_TITLE)
            .note(UPDATED_NOTE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .imageSha1(UPDATED_IMAGE_SHA_1)
            .thumbnailx1(UPDATED_THUMBNAILX_1)
            .thumbnailx1ContentType(UPDATED_THUMBNAILX_1_CONTENT_TYPE)
            .thumbnailx1Sha1(UPDATED_THUMBNAILX_1_SHA_1)
            .thumbnailx2(UPDATED_THUMBNAILX_2)
            .thumbnailx2ContentType(UPDATED_THUMBNAILX_2_CONTENT_TYPE)
            .thumbnailx2Sha1(UPDATED_THUMBNAILX_2_SHA_1)
            .exif(UPDATED_EXIF)
            .extractedText(UPDATED_EXTRACTED_TEXT)
            .detectedObjects(UPDATED_DETECTED_OBJECTS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return photo;
    }

    @BeforeEach
    public void initTest() {
        photo = createEntity(em);
    }

    @Test
    @Transactional
    public void createPhoto() throws Exception {
        int databaseSizeBeforeCreate = photoRepository.findAll().size();

        // Create the Photo
        PhotoDTO photoDTO = photoMapper.toDto(photo);
        restPhotoMockMvc.perform(post("/api/photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(photoDTO)))
            .andExpect(status().isCreated());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeCreate + 1);
        Photo testPhoto = photoList.get(photoList.size() - 1);
        assertThat(testPhoto.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPhoto.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testPhoto.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testPhoto.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testPhoto.getImageSha1()).isEqualTo(DEFAULT_IMAGE_SHA_1);
        assertThat(testPhoto.getThumbnailx1()).isEqualTo(DEFAULT_THUMBNAILX_1);
        assertThat(testPhoto.getThumbnailx1ContentType()).isEqualTo(DEFAULT_THUMBNAILX_1_CONTENT_TYPE);
        assertThat(testPhoto.getThumbnailx1Sha1()).isEqualTo(DEFAULT_THUMBNAILX_1_SHA_1);
        assertThat(testPhoto.getThumbnailx2()).isEqualTo(DEFAULT_THUMBNAILX_2);
        assertThat(testPhoto.getThumbnailx2ContentType()).isEqualTo(DEFAULT_THUMBNAILX_2_CONTENT_TYPE);
        assertThat(testPhoto.getThumbnailx2Sha1()).isEqualTo(DEFAULT_THUMBNAILX_2_SHA_1);
        assertThat(testPhoto.getExif()).isEqualTo(DEFAULT_EXIF);
        assertThat(testPhoto.getExtractedText()).isEqualTo(DEFAULT_EXTRACTED_TEXT);
        assertThat(testPhoto.getDetectedObjects()).isEqualTo(DEFAULT_DETECTED_OBJECTS);
        assertThat(testPhoto.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testPhoto.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    public void createPhotoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = photoRepository.findAll().size();

        // Create the Photo with an existing ID
        photo.setId(1L);
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhotoMockMvc.perform(post("/api/photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(photoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoRepository.findAll().size();
        // set the field null
        photo.setCreatedAt(null);

        // Create the Photo, which fails.
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        restPhotoMockMvc.perform(post("/api/photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(photoDTO)))
            .andExpect(status().isBadRequest());

        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPhotos() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList
        restPhotoMockMvc.perform(get("/api/photos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(photo.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].imageSha1").value(hasItem(DEFAULT_IMAGE_SHA_1)))
            .andExpect(jsonPath("$.[*].thumbnailx1ContentType").value(hasItem(DEFAULT_THUMBNAILX_1_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnailx1").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAILX_1))))
            .andExpect(jsonPath("$.[*].thumbnailx1Sha1").value(hasItem(DEFAULT_THUMBNAILX_1_SHA_1)))
            .andExpect(jsonPath("$.[*].thumbnailx2ContentType").value(hasItem(DEFAULT_THUMBNAILX_2_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnailx2").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAILX_2))))
            .andExpect(jsonPath("$.[*].thumbnailx2Sha1").value(hasItem(DEFAULT_THUMBNAILX_2_SHA_1)))
            .andExpect(jsonPath("$.[*].exif").value(hasItem(DEFAULT_EXIF.toString())))
            .andExpect(jsonPath("$.[*].extractedText").value(hasItem(DEFAULT_EXTRACTED_TEXT.toString())))
            .andExpect(jsonPath("$.[*].detectedObjects").value(hasItem(DEFAULT_DETECTED_OBJECTS.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getPhoto() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get the photo
        restPhotoMockMvc.perform(get("/api/photos/{id}", photo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(photo.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.imageSha1").value(DEFAULT_IMAGE_SHA_1))
            .andExpect(jsonPath("$.thumbnailx1ContentType").value(DEFAULT_THUMBNAILX_1_CONTENT_TYPE))
            .andExpect(jsonPath("$.thumbnailx1").value(Base64Utils.encodeToString(DEFAULT_THUMBNAILX_1)))
            .andExpect(jsonPath("$.thumbnailx1Sha1").value(DEFAULT_THUMBNAILX_1_SHA_1))
            .andExpect(jsonPath("$.thumbnailx2ContentType").value(DEFAULT_THUMBNAILX_2_CONTENT_TYPE))
            .andExpect(jsonPath("$.thumbnailx2").value(Base64Utils.encodeToString(DEFAULT_THUMBNAILX_2)))
            .andExpect(jsonPath("$.thumbnailx2Sha1").value(DEFAULT_THUMBNAILX_2_SHA_1))
            .andExpect(jsonPath("$.exif").value(DEFAULT_EXIF.toString()))
            .andExpect(jsonPath("$.extractedText").value(DEFAULT_EXTRACTED_TEXT.toString()))
            .andExpect(jsonPath("$.detectedObjects").value(DEFAULT_DETECTED_OBJECTS.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }


    @Test
    @Transactional
    public void getPhotosByIdFiltering() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        Long id = photo.getId();

        defaultPhotoShouldBeFound("id.equals=" + id);
        defaultPhotoShouldNotBeFound("id.notEquals=" + id);

        defaultPhotoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPhotoShouldNotBeFound("id.greaterThan=" + id);

        defaultPhotoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPhotoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPhotosByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where title equals to DEFAULT_TITLE
        defaultPhotoShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the photoList where title equals to UPDATED_TITLE
        defaultPhotoShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllPhotosByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where title not equals to DEFAULT_TITLE
        defaultPhotoShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the photoList where title not equals to UPDATED_TITLE
        defaultPhotoShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllPhotosByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultPhotoShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the photoList where title equals to UPDATED_TITLE
        defaultPhotoShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllPhotosByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where title is not null
        defaultPhotoShouldBeFound("title.specified=true");

        // Get all the photoList where title is null
        defaultPhotoShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllPhotosByTitleContainsSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where title contains DEFAULT_TITLE
        defaultPhotoShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the photoList where title contains UPDATED_TITLE
        defaultPhotoShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllPhotosByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where title does not contain DEFAULT_TITLE
        defaultPhotoShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the photoList where title does not contain UPDATED_TITLE
        defaultPhotoShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllPhotosByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where note equals to DEFAULT_NOTE
        defaultPhotoShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the photoList where note equals to UPDATED_NOTE
        defaultPhotoShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllPhotosByNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where note not equals to DEFAULT_NOTE
        defaultPhotoShouldNotBeFound("note.notEquals=" + DEFAULT_NOTE);

        // Get all the photoList where note not equals to UPDATED_NOTE
        defaultPhotoShouldBeFound("note.notEquals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllPhotosByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultPhotoShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the photoList where note equals to UPDATED_NOTE
        defaultPhotoShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllPhotosByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where note is not null
        defaultPhotoShouldBeFound("note.specified=true");

        // Get all the photoList where note is null
        defaultPhotoShouldNotBeFound("note.specified=false");
    }
                @Test
    @Transactional
    public void getAllPhotosByNoteContainsSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where note contains DEFAULT_NOTE
        defaultPhotoShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the photoList where note contains UPDATED_NOTE
        defaultPhotoShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllPhotosByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where note does not contain DEFAULT_NOTE
        defaultPhotoShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the photoList where note does not contain UPDATED_NOTE
        defaultPhotoShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }


    @Test
    @Transactional
    public void getAllPhotosByImageSha1IsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where imageSha1 equals to DEFAULT_IMAGE_SHA_1
        defaultPhotoShouldBeFound("imageSha1.equals=" + DEFAULT_IMAGE_SHA_1);

        // Get all the photoList where imageSha1 equals to UPDATED_IMAGE_SHA_1
        defaultPhotoShouldNotBeFound("imageSha1.equals=" + UPDATED_IMAGE_SHA_1);
    }

    @Test
    @Transactional
    public void getAllPhotosByImageSha1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where imageSha1 not equals to DEFAULT_IMAGE_SHA_1
        defaultPhotoShouldNotBeFound("imageSha1.notEquals=" + DEFAULT_IMAGE_SHA_1);

        // Get all the photoList where imageSha1 not equals to UPDATED_IMAGE_SHA_1
        defaultPhotoShouldBeFound("imageSha1.notEquals=" + UPDATED_IMAGE_SHA_1);
    }

    @Test
    @Transactional
    public void getAllPhotosByImageSha1IsInShouldWork() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where imageSha1 in DEFAULT_IMAGE_SHA_1 or UPDATED_IMAGE_SHA_1
        defaultPhotoShouldBeFound("imageSha1.in=" + DEFAULT_IMAGE_SHA_1 + "," + UPDATED_IMAGE_SHA_1);

        // Get all the photoList where imageSha1 equals to UPDATED_IMAGE_SHA_1
        defaultPhotoShouldNotBeFound("imageSha1.in=" + UPDATED_IMAGE_SHA_1);
    }

    @Test
    @Transactional
    public void getAllPhotosByImageSha1IsNullOrNotNull() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where imageSha1 is not null
        defaultPhotoShouldBeFound("imageSha1.specified=true");

        // Get all the photoList where imageSha1 is null
        defaultPhotoShouldNotBeFound("imageSha1.specified=false");
    }
                @Test
    @Transactional
    public void getAllPhotosByImageSha1ContainsSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where imageSha1 contains DEFAULT_IMAGE_SHA_1
        defaultPhotoShouldBeFound("imageSha1.contains=" + DEFAULT_IMAGE_SHA_1);

        // Get all the photoList where imageSha1 contains UPDATED_IMAGE_SHA_1
        defaultPhotoShouldNotBeFound("imageSha1.contains=" + UPDATED_IMAGE_SHA_1);
    }

    @Test
    @Transactional
    public void getAllPhotosByImageSha1NotContainsSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where imageSha1 does not contain DEFAULT_IMAGE_SHA_1
        defaultPhotoShouldNotBeFound("imageSha1.doesNotContain=" + DEFAULT_IMAGE_SHA_1);

        // Get all the photoList where imageSha1 does not contain UPDATED_IMAGE_SHA_1
        defaultPhotoShouldBeFound("imageSha1.doesNotContain=" + UPDATED_IMAGE_SHA_1);
    }


    @Test
    @Transactional
    public void getAllPhotosByThumbnailx1Sha1IsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where thumbnailx1Sha1 equals to DEFAULT_THUMBNAILX_1_SHA_1
        defaultPhotoShouldBeFound("thumbnailx1Sha1.equals=" + DEFAULT_THUMBNAILX_1_SHA_1);

        // Get all the photoList where thumbnailx1Sha1 equals to UPDATED_THUMBNAILX_1_SHA_1
        defaultPhotoShouldNotBeFound("thumbnailx1Sha1.equals=" + UPDATED_THUMBNAILX_1_SHA_1);
    }

    @Test
    @Transactional
    public void getAllPhotosByThumbnailx1Sha1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where thumbnailx1Sha1 not equals to DEFAULT_THUMBNAILX_1_SHA_1
        defaultPhotoShouldNotBeFound("thumbnailx1Sha1.notEquals=" + DEFAULT_THUMBNAILX_1_SHA_1);

        // Get all the photoList where thumbnailx1Sha1 not equals to UPDATED_THUMBNAILX_1_SHA_1
        defaultPhotoShouldBeFound("thumbnailx1Sha1.notEquals=" + UPDATED_THUMBNAILX_1_SHA_1);
    }

    @Test
    @Transactional
    public void getAllPhotosByThumbnailx1Sha1IsInShouldWork() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where thumbnailx1Sha1 in DEFAULT_THUMBNAILX_1_SHA_1 or UPDATED_THUMBNAILX_1_SHA_1
        defaultPhotoShouldBeFound("thumbnailx1Sha1.in=" + DEFAULT_THUMBNAILX_1_SHA_1 + "," + UPDATED_THUMBNAILX_1_SHA_1);

        // Get all the photoList where thumbnailx1Sha1 equals to UPDATED_THUMBNAILX_1_SHA_1
        defaultPhotoShouldNotBeFound("thumbnailx1Sha1.in=" + UPDATED_THUMBNAILX_1_SHA_1);
    }

    @Test
    @Transactional
    public void getAllPhotosByThumbnailx1Sha1IsNullOrNotNull() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where thumbnailx1Sha1 is not null
        defaultPhotoShouldBeFound("thumbnailx1Sha1.specified=true");

        // Get all the photoList where thumbnailx1Sha1 is null
        defaultPhotoShouldNotBeFound("thumbnailx1Sha1.specified=false");
    }
                @Test
    @Transactional
    public void getAllPhotosByThumbnailx1Sha1ContainsSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where thumbnailx1Sha1 contains DEFAULT_THUMBNAILX_1_SHA_1
        defaultPhotoShouldBeFound("thumbnailx1Sha1.contains=" + DEFAULT_THUMBNAILX_1_SHA_1);

        // Get all the photoList where thumbnailx1Sha1 contains UPDATED_THUMBNAILX_1_SHA_1
        defaultPhotoShouldNotBeFound("thumbnailx1Sha1.contains=" + UPDATED_THUMBNAILX_1_SHA_1);
    }

    @Test
    @Transactional
    public void getAllPhotosByThumbnailx1Sha1NotContainsSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where thumbnailx1Sha1 does not contain DEFAULT_THUMBNAILX_1_SHA_1
        defaultPhotoShouldNotBeFound("thumbnailx1Sha1.doesNotContain=" + DEFAULT_THUMBNAILX_1_SHA_1);

        // Get all the photoList where thumbnailx1Sha1 does not contain UPDATED_THUMBNAILX_1_SHA_1
        defaultPhotoShouldBeFound("thumbnailx1Sha1.doesNotContain=" + UPDATED_THUMBNAILX_1_SHA_1);
    }


    @Test
    @Transactional
    public void getAllPhotosByThumbnailx2Sha1IsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where thumbnailx2Sha1 equals to DEFAULT_THUMBNAILX_2_SHA_1
        defaultPhotoShouldBeFound("thumbnailx2Sha1.equals=" + DEFAULT_THUMBNAILX_2_SHA_1);

        // Get all the photoList where thumbnailx2Sha1 equals to UPDATED_THUMBNAILX_2_SHA_1
        defaultPhotoShouldNotBeFound("thumbnailx2Sha1.equals=" + UPDATED_THUMBNAILX_2_SHA_1);
    }

    @Test
    @Transactional
    public void getAllPhotosByThumbnailx2Sha1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where thumbnailx2Sha1 not equals to DEFAULT_THUMBNAILX_2_SHA_1
        defaultPhotoShouldNotBeFound("thumbnailx2Sha1.notEquals=" + DEFAULT_THUMBNAILX_2_SHA_1);

        // Get all the photoList where thumbnailx2Sha1 not equals to UPDATED_THUMBNAILX_2_SHA_1
        defaultPhotoShouldBeFound("thumbnailx2Sha1.notEquals=" + UPDATED_THUMBNAILX_2_SHA_1);
    }

    @Test
    @Transactional
    public void getAllPhotosByThumbnailx2Sha1IsInShouldWork() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where thumbnailx2Sha1 in DEFAULT_THUMBNAILX_2_SHA_1 or UPDATED_THUMBNAILX_2_SHA_1
        defaultPhotoShouldBeFound("thumbnailx2Sha1.in=" + DEFAULT_THUMBNAILX_2_SHA_1 + "," + UPDATED_THUMBNAILX_2_SHA_1);

        // Get all the photoList where thumbnailx2Sha1 equals to UPDATED_THUMBNAILX_2_SHA_1
        defaultPhotoShouldNotBeFound("thumbnailx2Sha1.in=" + UPDATED_THUMBNAILX_2_SHA_1);
    }

    @Test
    @Transactional
    public void getAllPhotosByThumbnailx2Sha1IsNullOrNotNull() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where thumbnailx2Sha1 is not null
        defaultPhotoShouldBeFound("thumbnailx2Sha1.specified=true");

        // Get all the photoList where thumbnailx2Sha1 is null
        defaultPhotoShouldNotBeFound("thumbnailx2Sha1.specified=false");
    }
                @Test
    @Transactional
    public void getAllPhotosByThumbnailx2Sha1ContainsSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where thumbnailx2Sha1 contains DEFAULT_THUMBNAILX_2_SHA_1
        defaultPhotoShouldBeFound("thumbnailx2Sha1.contains=" + DEFAULT_THUMBNAILX_2_SHA_1);

        // Get all the photoList where thumbnailx2Sha1 contains UPDATED_THUMBNAILX_2_SHA_1
        defaultPhotoShouldNotBeFound("thumbnailx2Sha1.contains=" + UPDATED_THUMBNAILX_2_SHA_1);
    }

    @Test
    @Transactional
    public void getAllPhotosByThumbnailx2Sha1NotContainsSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where thumbnailx2Sha1 does not contain DEFAULT_THUMBNAILX_2_SHA_1
        defaultPhotoShouldNotBeFound("thumbnailx2Sha1.doesNotContain=" + DEFAULT_THUMBNAILX_2_SHA_1);

        // Get all the photoList where thumbnailx2Sha1 does not contain UPDATED_THUMBNAILX_2_SHA_1
        defaultPhotoShouldBeFound("thumbnailx2Sha1.doesNotContain=" + UPDATED_THUMBNAILX_2_SHA_1);
    }


    @Test
    @Transactional
    public void getAllPhotosByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where createdAt equals to DEFAULT_CREATED_AT
        defaultPhotoShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the photoList where createdAt equals to UPDATED_CREATED_AT
        defaultPhotoShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllPhotosByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where createdAt not equals to DEFAULT_CREATED_AT
        defaultPhotoShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the photoList where createdAt not equals to UPDATED_CREATED_AT
        defaultPhotoShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllPhotosByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultPhotoShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the photoList where createdAt equals to UPDATED_CREATED_AT
        defaultPhotoShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllPhotosByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where createdAt is not null
        defaultPhotoShouldBeFound("createdAt.specified=true");

        // Get all the photoList where createdAt is null
        defaultPhotoShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllPhotosByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultPhotoShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the photoList where updatedAt equals to UPDATED_UPDATED_AT
        defaultPhotoShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllPhotosByUpdatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where updatedAt not equals to DEFAULT_UPDATED_AT
        defaultPhotoShouldNotBeFound("updatedAt.notEquals=" + DEFAULT_UPDATED_AT);

        // Get all the photoList where updatedAt not equals to UPDATED_UPDATED_AT
        defaultPhotoShouldBeFound("updatedAt.notEquals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllPhotosByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultPhotoShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the photoList where updatedAt equals to UPDATED_UPDATED_AT
        defaultPhotoShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllPhotosByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList where updatedAt is not null
        defaultPhotoShouldBeFound("updatedAt.specified=true");

        // Get all the photoList where updatedAt is null
        defaultPhotoShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllPhotosByBelongToIsEqualToSomething() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);
        Album belongTo = AlbumResourceIT.createEntity(em);
        em.persist(belongTo);
        em.flush();
        photo.setBelongTo(belongTo);
        photoRepository.saveAndFlush(photo);
        Long belongToId = belongTo.getId();

        // Get all the photoList where belongTo equals to belongToId
        defaultPhotoShouldBeFound("belongToId.equals=" + belongToId);

        // Get all the photoList where belongTo equals to belongToId + 1
        defaultPhotoShouldNotBeFound("belongToId.equals=" + (belongToId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPhotoShouldBeFound(String filter) throws Exception {
        restPhotoMockMvc.perform(get("/api/photos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(photo.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].imageSha1").value(hasItem(DEFAULT_IMAGE_SHA_1)))
            .andExpect(jsonPath("$.[*].thumbnailx1ContentType").value(hasItem(DEFAULT_THUMBNAILX_1_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnailx1").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAILX_1))))
            .andExpect(jsonPath("$.[*].thumbnailx1Sha1").value(hasItem(DEFAULT_THUMBNAILX_1_SHA_1)))
            .andExpect(jsonPath("$.[*].thumbnailx2ContentType").value(hasItem(DEFAULT_THUMBNAILX_2_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnailx2").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAILX_2))))
            .andExpect(jsonPath("$.[*].thumbnailx2Sha1").value(hasItem(DEFAULT_THUMBNAILX_2_SHA_1)))
            .andExpect(jsonPath("$.[*].exif").value(hasItem(DEFAULT_EXIF.toString())))
            .andExpect(jsonPath("$.[*].extractedText").value(hasItem(DEFAULT_EXTRACTED_TEXT.toString())))
            .andExpect(jsonPath("$.[*].detectedObjects").value(hasItem(DEFAULT_DETECTED_OBJECTS.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restPhotoMockMvc.perform(get("/api/photos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPhotoShouldNotBeFound(String filter) throws Exception {
        restPhotoMockMvc.perform(get("/api/photos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPhotoMockMvc.perform(get("/api/photos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPhoto() throws Exception {
        // Get the photo
        restPhotoMockMvc.perform(get("/api/photos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePhoto() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        int databaseSizeBeforeUpdate = photoRepository.findAll().size();

        // Update the photo
        Photo updatedPhoto = photoRepository.findById(photo.getId()).get();
        // Disconnect from session so that the updates on updatedPhoto are not directly saved in db
        em.detach(updatedPhoto);
        updatedPhoto
            .title(UPDATED_TITLE)
            .note(UPDATED_NOTE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .imageSha1(UPDATED_IMAGE_SHA_1)
            .thumbnailx1(UPDATED_THUMBNAILX_1)
            .thumbnailx1ContentType(UPDATED_THUMBNAILX_1_CONTENT_TYPE)
            .thumbnailx1Sha1(UPDATED_THUMBNAILX_1_SHA_1)
            .thumbnailx2(UPDATED_THUMBNAILX_2)
            .thumbnailx2ContentType(UPDATED_THUMBNAILX_2_CONTENT_TYPE)
            .thumbnailx2Sha1(UPDATED_THUMBNAILX_2_SHA_1)
            .exif(UPDATED_EXIF)
            .extractedText(UPDATED_EXTRACTED_TEXT)
            .detectedObjects(UPDATED_DETECTED_OBJECTS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        PhotoDTO photoDTO = photoMapper.toDto(updatedPhoto);

        restPhotoMockMvc.perform(put("/api/photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(photoDTO)))
            .andExpect(status().isOk());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);
        Photo testPhoto = photoList.get(photoList.size() - 1);
        assertThat(testPhoto.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPhoto.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testPhoto.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testPhoto.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testPhoto.getImageSha1()).isEqualTo(UPDATED_IMAGE_SHA_1);
        assertThat(testPhoto.getThumbnailx1()).isEqualTo(UPDATED_THUMBNAILX_1);
        assertThat(testPhoto.getThumbnailx1ContentType()).isEqualTo(UPDATED_THUMBNAILX_1_CONTENT_TYPE);
        assertThat(testPhoto.getThumbnailx1Sha1()).isEqualTo(UPDATED_THUMBNAILX_1_SHA_1);
        assertThat(testPhoto.getThumbnailx2()).isEqualTo(UPDATED_THUMBNAILX_2);
        assertThat(testPhoto.getThumbnailx2ContentType()).isEqualTo(UPDATED_THUMBNAILX_2_CONTENT_TYPE);
        assertThat(testPhoto.getThumbnailx2Sha1()).isEqualTo(UPDATED_THUMBNAILX_2_SHA_1);
        assertThat(testPhoto.getExif()).isEqualTo(UPDATED_EXIF);
        assertThat(testPhoto.getExtractedText()).isEqualTo(UPDATED_EXTRACTED_TEXT);
        assertThat(testPhoto.getDetectedObjects()).isEqualTo(UPDATED_DETECTED_OBJECTS);
        assertThat(testPhoto.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPhoto.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingPhoto() throws Exception {
        int databaseSizeBeforeUpdate = photoRepository.findAll().size();

        // Create the Photo
        PhotoDTO photoDTO = photoMapper.toDto(photo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhotoMockMvc.perform(put("/api/photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(photoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePhoto() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        int databaseSizeBeforeDelete = photoRepository.findAll().size();

        // Delete the photo
        restPhotoMockMvc.perform(delete("/api/photos/{id}", photo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
