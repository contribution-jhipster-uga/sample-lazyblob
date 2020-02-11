package sample.lazyblob.service.impl;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drew.imaging.ImageProcessingException;

import sample.lazyblob.domain.Photo;
import sample.lazyblob.repository.PhotoRepository;
import sample.lazyblob.service.PhotoService;
import sample.lazyblob.service.dto.PhotoDTO;
import sample.lazyblob.service.mapper.PhotoMapper;
import sample.lazyblob.service.util.MetadataUtil;
import sample.lazyblob.service.util.MimeTypes;
import sample.lazyblob.service.util.SHAUtil;
import sample.lazyblob.service.util.ThumbnailUtil;

/**
 * Service Implementation for managing {@link Photo}.
 */
@Service
@Transactional
public class PhotoServiceImpl implements PhotoService {

    private final Logger log = LoggerFactory.getLogger(PhotoServiceImpl.class);

    private final PhotoRepository photoRepository;

    private final PhotoMapper photoMapper;

    @NotNull(message = "thumbnail.x1.maxDim can not be null")
    @Value("${thumbnail.x1.maxDim}")
    private int x1MaxDim;

    @NotNull(message = "thumbnail.x2.maxDim can not be null")
    @Value("${thumbnail.x2.maxDim}")
    private int x2MaxDim;

    public PhotoServiceImpl(PhotoRepository photoRepository, PhotoMapper photoMapper) {
        this.photoRepository = photoRepository;
        this.photoMapper = photoMapper;
    }

    private void reset(PhotoDTO photoDTO) {
        photoDTO.setThumbnailx1(null);
        photoDTO.setThumbnailx1Sha1(null);
        photoDTO.setThumbnailx1ContentType(null);
        photoDTO.setThumbnailx2(null);
        photoDTO.setThumbnailx2Sha1(null);
        photoDTO.setThumbnailx2ContentType(null);
        photoDTO.setExif(null);
        photoDTO.setExtractedText(null);
        photoDTO.setDetectedObjects(null);
    }

    /**
     * Save a photo.
     *
     * @param photoDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PhotoDTO save(PhotoDTO photoDTO) {
        log.debug("Request to save Photo : {}", photoDTO);

        Photo photo = null;
        Instant now = Instant.now();
        if (photoDTO.getId() == null) {
            // create entity
            photoDTO.setCreatedAt(now);
            photoDTO.setUpdatedAt(now);
        } else {
            // update entity
            Long id = photoDTO.getId();
            if (id != null) {
                Optional<Photo> opt = photoRepository.findById(id);
                if (opt.isPresent()) {
                    photo = opt.get();
                } else {
                    log.error("Photo {} does not exist", id);
                    return null;
                }
            }
            photoDTO.setCreatedAt(photo.getCreatedAt());
            photoDTO.setUpdatedAt(now);
        }
        byte[] image = photoDTO.getImage();
        if (image != null) {
            String sha1Image = SHAUtil.hash((image));
            System.out.println();
            if (photo == null || !photo.getImageSha1().equals(sha1Image)) {
                photoDTO.setImageSha1(sha1Image);
                try {
                    String mimeType = photoDTO.getImageContentType();
                    String formatName = MimeTypes.lookupExt(mimeType);
                    photoDTO.setThumbnailx1(ThumbnailUtil.scale(photoDTO.getImage(), x1MaxDim, formatName));
                    photoDTO.setThumbnailx1Sha1(SHAUtil.hash(photoDTO.getThumbnailx1()));
                    photoDTO.setThumbnailx1ContentType(mimeType);

                    photoDTO.setThumbnailx2(ThumbnailUtil.scale(photoDTO.getImage(), x2MaxDim, formatName));
                    photoDTO.setThumbnailx2Sha1(SHAUtil.hash(photoDTO.getThumbnailx2()));
                    photoDTO.setThumbnailx2ContentType(mimeType);

                    // Extract EXIF
                    try {
                        photoDTO.setExif(MetadataUtil.extract(image));
                    } catch (ImageProcessingException e) {
                        log.warn("Can not extract the image metadata", e);
                    }
                    // TODO Extract GPS tag from Metadata
                    // TODO Extract Text with OCR
                    // TODO Extract Objects with ImageAI

                } catch (IOException e) {
                    log.warn("Can not thumbnail the image ", e);
                    reset(photoDTO);
                }
            }else {
                photoDTO.setImageSha1(sha1Image);
                photoDTO.setThumbnailx1(photo.getThumbnailx1());
                photoDTO.setThumbnailx1Sha1(photo.getThumbnailx1Sha1());
                photoDTO.setThumbnailx1ContentType(photo.getThumbnailx2ContentType());
                photoDTO.setThumbnailx2(photo.getThumbnailx2());
                photoDTO.setThumbnailx2Sha1(photo.getThumbnailx2Sha1());
                photoDTO.setThumbnailx2ContentType(photo.getThumbnailx2ContentType());
                photoDTO.setExif(photo.getExif());
                photoDTO.setExtractedText(photo.getExtractedText());
                photoDTO.setDetectedObjects(photo.getDetectedObjects());
            }
        } else {
            reset(photoDTO);
        }

        // photoDTO.setBelongTo

        Photo phototostore = photoMapper.toEntity(photoDTO);
        phototostore = photoRepository.save(phototostore);
        return photoMapper.toDto(phototostore);
    }

    /**
     * Get all the photos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PhotoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Photos");
        return photoRepository.findAll(pageable)
            .map(photoMapper::toDto);
    }


    /**
     * Get one photo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PhotoDTO> findOne(Long id) {
        log.debug("Request to get Photo : {}", id);
        return photoRepository.findById(id)
            .map(photoMapper::toDto);
    }

    /**
     * Delete the photo by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Photo : {}", id);
        photoRepository.deleteById(id);
    }
}
