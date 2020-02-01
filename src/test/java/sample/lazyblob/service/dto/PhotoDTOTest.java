package sample.lazyblob.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import sample.lazyblob.web.rest.TestUtil;

public class PhotoDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PhotoDTO.class);
        PhotoDTO photoDTO1 = new PhotoDTO();
        photoDTO1.setId(1L);
        PhotoDTO photoDTO2 = new PhotoDTO();
        assertThat(photoDTO1).isNotEqualTo(photoDTO2);
        photoDTO2.setId(photoDTO1.getId());
        assertThat(photoDTO1).isEqualTo(photoDTO2);
        photoDTO2.setId(2L);
        assertThat(photoDTO1).isNotEqualTo(photoDTO2);
        photoDTO1.setId(null);
        assertThat(photoDTO1).isNotEqualTo(photoDTO2);
    }
}
