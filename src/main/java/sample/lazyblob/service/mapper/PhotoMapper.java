package sample.lazyblob.service.mapper;

import sample.lazyblob.domain.*;
import sample.lazyblob.service.dto.PhotoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Photo} and its DTO {@link PhotoDTO}.
 */
@Mapper(componentModel = "spring", uses = {AlbumMapper.class})
public interface PhotoMapper extends EntityMapper<PhotoDTO, Photo> {

    @Mapping(source = "belongTo.id", target = "belongToId")
    @Mapping(source = "belongTo.title", target = "belongToTitle")
    PhotoDTO toDto(Photo photo);

    @Mapping(source = "belongToId", target = "belongTo")
    Photo toEntity(PhotoDTO photoDTO);

    default Photo fromId(Long id) {
        if (id == null) {
            return null;
        }
        Photo photo = new Photo();
        photo.setId(id);
        return photo;
    }
}
