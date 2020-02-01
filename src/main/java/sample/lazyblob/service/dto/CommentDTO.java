package sample.lazyblob.service.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sample.lazyblob.domain.Comment} entity.
 */
@ApiModel(description = "Entity Comment")
public class CommentDTO implements Serializable {

    private Long id;

    /**
     * Text
     */
    @NotNull
    @Size(min = 5)
    @ApiModelProperty(value = "Text", required = true)
    private String text;

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


    private Long fromId;

    private String fromLogin;

    private Long photoId;

    private String photoTitle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CommentDTO commentDTO = (CommentDTO) o;
        if (commentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), commentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", from=" + getFromId() +
            ", from='" + getFromLogin() + "'" +
            ", photo=" + getPhotoId() +
            ", photo='" + getPhotoTitle() + "'" +
            "}";
    }
}
