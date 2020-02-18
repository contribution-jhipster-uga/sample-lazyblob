package sample.lazyblob.service.mapper;

import sample.lazyblob.domain.*;
import sample.lazyblob.service.dto.PhotoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PhotoLite} and its DTO {@link PhotoDTO}.
 */
@Mapper(componentModel = "spring", uses = {AlbumMapper.class})
public interface PhotoLiteMapper extends EntityMapper<PhotoDTO, PhotoLite> {

    PhotoDTO toDto(PhotoLite photoLite);



    PhotoLite toEntity(PhotoDTO photoDTO);

    default PhotoLite fromId(Long id) {
        if (id == null) {
            return null;
        }
        PhotoLite photoLite = new PhotoLite();
        photoLite.setId(id);
        return photoLite;
    }
}
