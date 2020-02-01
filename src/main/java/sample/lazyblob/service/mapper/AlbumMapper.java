package sample.lazyblob.service.mapper;

import sample.lazyblob.domain.*;
import sample.lazyblob.service.dto.AlbumDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Album} and its DTO {@link AlbumDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface AlbumMapper extends EntityMapper<AlbumDTO, Album> {

    @Mapping(source = "ownedBy.id", target = "ownedById")
    @Mapping(source = "ownedBy.login", target = "ownedByLogin")
    AlbumDTO toDto(Album album);

    @Mapping(source = "ownedById", target = "ownedBy")
    Album toEntity(AlbumDTO albumDTO);

    default Album fromId(Long id) {
        if (id == null) {
            return null;
        }
        Album album = new Album();
        album.setId(id);
        return album;
    }
}
