package sample.lazyblob.service.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sample.lazyblob.domain.Rate} entity.
 */
@ApiModel(description = "Entity Rate")
public class RateDTO implements Serializable {

    private Long id;

    /**
     * Rate
     */
    @Min(value = 0)
    @Max(value = 5)
    @ApiModelProperty(value = "Rate")
    private Integer rate;

    /**
     * Creation date
     */
    @NotNull
    @ApiModelProperty(value = "Creation date", required = true)
    private Instant createdAt;

    /**
     * Update date
     */
    @ApiModelProperty(value = "Update date")
    private Instant updatedAt;


    private Long photoId;

    private String photoTitle;

    private Long fromId;

    private String fromLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }

    public String getPhotoTitle() {
        return photoTitle;
    }

    public void setPhotoTitle(String photoTitle) {
        this.photoTitle = photoTitle;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long userId) {
        this.fromId = userId;
    }

    public String getFromLogin() {
        return fromLogin;
    }

    public void setFromLogin(String userLogin) {
        this.fromLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RateDTO rateDTO = (RateDTO) o;
        if (rateDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rateDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RateDTO{" +
            "id=" + getId() +
            ", rate=" + getRate() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", photo=" + getPhotoId() +
            ", photo='" + getPhotoTitle() + "'" +
            ", from=" + getFromId() +
            ", from='" + getFromLogin() + "'" +
            "}";
    }
}
