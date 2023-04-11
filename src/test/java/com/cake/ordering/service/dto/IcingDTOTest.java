package com.cake.ordering.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.cake.ordering.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IcingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IcingDTO.class);
        IcingDTO icingDTO1 = new IcingDTO();
        icingDTO1.setId(1L);
        IcingDTO icingDTO2 = new IcingDTO();
        assertThat(icingDTO1).isNotEqualTo(icingDTO2);
        icingDTO2.setId(icingDTO1.getId());
        assertThat(icingDTO1).isEqualTo(icingDTO2);
        icingDTO2.setId(2L);
        assertThat(icingDTO1).isNotEqualTo(icingDTO2);
        icingDTO1.setId(null);
        assertThat(icingDTO1).isNotEqualTo(icingDTO2);
    }
}
