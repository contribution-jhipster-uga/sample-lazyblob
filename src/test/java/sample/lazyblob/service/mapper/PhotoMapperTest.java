package sample.lazyblob.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class PhotoMapperTest {

    private PhotoMapperLazy photoMapper;

    @BeforeEach
    public void setUp() {
        photoMapper = new PhotoMapperLazy();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(photoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(photoMapper.fromId(null)).isNull();
    }
}
