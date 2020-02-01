package sample.lazyblob.service.mapper;

import sample.lazyblob.domain.*;
import sample.lazyblob.service.dto.CommentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Comment} and its DTO {@link CommentDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, PhotoMapper.class})
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {

    @Mapping(source = "from.id", target = "fromId")
    @Mapping(source = "from.login", target = "fromLogin")
    @Mapping(source = "photo.id", target = "photoId")
    @Mapping(source = "photo.title", target = "photoTitle")
    CommentDTO toDto(Comment comment);

    @Mapping(source = "fromId", target = "from")
    @Mapping(source = "photoId", target = "photo")
    Comment toEntity(CommentDTO commentDTO);

    default Comment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setId(id);
        return comment;
    }
}
