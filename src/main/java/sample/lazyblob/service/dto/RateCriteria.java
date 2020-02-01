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
 * Criteria class for the {@link sample.lazyblob.domain.Rate} entity. This class is used
 * in {@link sample.lazyblob.web.rest.RateResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rates?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RateCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter rate;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter photoId;

    private LongFilter fromId;

    public RateCriteria(){
    }

    public RateCriteria(RateCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.rate = other.rate == null ? null : other.rate.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.photoId = other.photoId == null ? null : other.photoId.copy();
        this.fromId = other.fromId == null ? null : other.fromId.copy();
    }

    @Override
    public RateCriteria copy() {
        return new RateCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getRate() {
        return rate;
    }

    public void setRate(IntegerFilter rate) {
        this.rate = rate;
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

    public LongFilter getPhotoId() {
        return photoId;
    }

    public void setPhotoId(LongFilter photoId) {
        this.photoId = photoId;
    }

    public LongFilter getFromId() {
        return fromId;
    }

    public void setFromId(LongFilter fromId) {
        this.fromId = fromId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RateCriteria that = (RateCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(rate, that.rate) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(photoId, that.photoId) &&
            Objects.equals(fromId, that.fromId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        rate,
        createdAt,
        updatedAt,
        photoId,
        fromId
        );
    }

    @Override
    public String toString() {
        return "RateCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (rate != null ? "rate=" + rate + ", " : "") +
                (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
                (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
                (photoId != null ? "photoId=" + photoId + ", " : "") +
                (fromId != null ? "fromId=" + fromId + ", " : "") +
            "}";
    }

}
