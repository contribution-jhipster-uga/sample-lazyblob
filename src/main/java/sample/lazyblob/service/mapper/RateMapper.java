package sample.lazyblob.service.mapper;

import sample.lazyblob.domain.*;
import sample.lazyblob.service.dto.RateDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Rate} and its DTO {@link RateDTO}.
 */
@Mapper(componentModel = "spring", uses = {PhotoMapper.class, UserMapper.class})
public interface RateMapper extends EntityMapper<RateDTO, Rate> {

    @Mapping(source = "photo.id", target = "photoId")
    @Mapping(source = "photo.title", target = "photoTitle")
    @Mapping(source = "from.id", target = "fromId")
    @Mapping(source = "from.login", target = "fromLogin")
    RateDTO toDto(Rate rate);

    @Mapping(source = "photoId", target = "photo")
    @Mapping(source = "fromId", target = "from")
    Rate toEntity(RateDTO rateDTO);

    default Rate fromId(Long id) {
        if (id == null) {
            return null;
        }
        Rate rate = new Rate();
        rate.setId(id);
        return rate;
    }
}
