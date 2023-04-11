package com.cake.ordering.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.cake.ordering.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CakeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cake.class);
        Cake cake1 = new Cake();
        cake1.setId(1L);
        Cake cake2 = new Cake();
        cake2.setId(cake1.getId());
        assertThat(cake1).isEqualTo(cake2);
        cake2.setId(2L);
        assertThat(cake1).isNotEqualTo(cake2);
        cake1.setId(null);
        assertThat(cake1).isNotEqualTo(cake2);
    }
}
