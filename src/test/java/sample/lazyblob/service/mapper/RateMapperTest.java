package sample.lazyblob.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class RateMapperTest {

    private RateMapper rateMapper;

    @BeforeEach
    public void setUp() {
        rateMapper = new RateMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(rateMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(rateMapper.fromId(null)).isNull();
    }
}
