package sample.lazyblob.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link sample.lazyblob.domain.Comment} entity. This class is used
 * in {@link sample.lazyblob.web.rest.CommentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /comments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CommentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter text;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter fromId;

    private LongFilter photoId;

    public CommentCriteria(){
    }

    public CommentCriteria(CommentCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.text = other.text == null ? null : other.text.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.fromId = other.fromId == null ? null : other.fromId.copy();
        this.photoId = other.photoId == null ? null : other.photoId.copy();
    }

    @Override
    public CommentCriteria copy() {
        return new CommentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getText() {
        return text;
    }

    public void setText(StringFilter text) {
        this.text = text;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public InstantFilter getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(InstantFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getFromId() {
        return fromId;
    }

    public void setFromId(LongFilter fromId) {
        this.fromId = fromId;
    }

    public LongFilter getPhotoId() {
        return photoId;
    }

    public void setPhotoId(LongFilter photoId) {
        this.photoId = photoId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CommentCriteria that = (CommentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(text, that.text) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(fromId, that.fromId) &&
            Objects.equals(photoId, that.photoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        text,
        createdAt,
        updatedAt,
        fromId,
        photoId
        );
    }

    @Override
    public String toString() {
        return "CommentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (text != null ? "text=" + text + ", " : "") +
                (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
                (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
                (fromId != null ? "fromId=" + fromId + ", " : "") +
                (photoId != null ? "photoId=" + photoId + ", " : "") +
            "}";
    }

}
