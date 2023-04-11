package com.cake.ordering.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.cake.ordering.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IcingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Icing.class);
        Icing icing1 = new Icing();
        icing1.setId(1L);
        Icing icing2 = new Icing();
        icing2.setId(icing1.getId());
        assertThat(icing1).isEqualTo(icing2);
        icing2.setId(2L);
        assertThat(icing1).isNotEqualTo(icing2);
        icing1.setId(null);
        assertThat(icing1).isNotEqualTo(icing2);
    }
}
