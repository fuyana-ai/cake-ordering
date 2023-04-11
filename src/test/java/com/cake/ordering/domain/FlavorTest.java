package com.cake.ordering.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.cake.ordering.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FlavorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Flavor.class);
        Flavor flavor1 = new Flavor();
        flavor1.setId(1L);
        Flavor flavor2 = new Flavor();
        flavor2.setId(flavor1.getId());
        assertThat(flavor1).isEqualTo(flavor2);
        flavor2.setId(2L);
        assertThat(flavor1).isNotEqualTo(flavor2);
        flavor1.setId(null);
        assertThat(flavor1).isNotEqualTo(flavor2);
    }
}
