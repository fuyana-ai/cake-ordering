package com.cake.ordering.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.cake.ordering.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CakeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CakeDTO.class);
        CakeDTO cakeDTO1 = new CakeDTO();
        cakeDTO1.setId(1L);
        CakeDTO cakeDTO2 = new CakeDTO();
        assertThat(cakeDTO1).isNotEqualTo(cakeDTO2);
        cakeDTO2.setId(cakeDTO1.getId());
        assertThat(cakeDTO1).isEqualTo(cakeDTO2);
        cakeDTO2.setId(2L);
        assertThat(cakeDTO1).isNotEqualTo(cakeDTO2);
        cakeDTO1.setId(null);
        assertThat(cakeDTO1).isNotEqualTo(cakeDTO2);
    }
}
