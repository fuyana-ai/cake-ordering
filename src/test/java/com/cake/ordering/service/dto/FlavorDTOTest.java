package com.cake.ordering.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.cake.ordering.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FlavorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FlavorDTO.class);
        FlavorDTO flavorDTO1 = new FlavorDTO();
        flavorDTO1.setId(1L);
        FlavorDTO flavorDTO2 = new FlavorDTO();
        assertThat(flavorDTO1).isNotEqualTo(flavorDTO2);
        flavorDTO2.setId(flavorDTO1.getId());
        assertThat(flavorDTO1).isEqualTo(flavorDTO2);
        flavorDTO2.setId(2L);
        assertThat(flavorDTO1).isNotEqualTo(flavorDTO2);
        flavorDTO1.setId(null);
        assertThat(flavorDTO1).isNotEqualTo(flavorDTO2);
    }
}
