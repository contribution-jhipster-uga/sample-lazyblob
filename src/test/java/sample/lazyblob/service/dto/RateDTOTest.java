package sample.lazyblob.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import sample.lazyblob.web.rest.TestUtil;

public class RateDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RateDTO.class);
        RateDTO rateDTO1 = new RateDTO();
        rateDTO1.setId(1L);
        RateDTO rateDTO2 = new RateDTO();
        assertThat(rateDTO1).isNotEqualTo(rateDTO2);
        rateDTO2.setId(rateDTO1.getId());
        assertThat(rateDTO1).isEqualTo(rateDTO2);
        rateDTO2.setId(2L);
        assertThat(rateDTO1).isNotEqualTo(rateDTO2);
        rateDTO1.setId(null);
        assertThat(rateDTO1).isNotEqualTo(rateDTO2);
    }
}
