package sample.lazyblob.service.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link sample.lazyblob.domain.Album} entity.
 */
@ApiModel(description = "Entity Album")
public class AlbumDTO implements Serializable {

    private Long id;

    /**
     * Title
     */
    @ApiModelProperty(value = "Title")
    private String title;

    /**
     * Note
     */
    @ApiModelProperty(value = "Note")
    private String note;

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


    private Long ownedById;

    private String ownedByLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public Long getOwnedById() {
        return ownedById;
    }

    public void setOwnedById(Long userId) {
        this.ownedById = userId;
    }

    public String getOwnedByLogin() {
        return ownedByLogin;
    }

    public void setOwnedByLogin(String userLogin) {
        this.ownedByLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AlbumDTO albumDTO = (AlbumDTO) o;
        if (albumDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), albumDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AlbumDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", note='" + getNote() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", ownedBy=" + getOwnedById() +
            ", ownedBy='" + getOwnedByLogin() + "'" +
            "}";
    }
}
