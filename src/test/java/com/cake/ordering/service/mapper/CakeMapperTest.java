package com.cake.ordering.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CakeMapperTest {

    private CakeMapper cakeMapper;

    @BeforeEach
    public void setUp() {
        cakeMapper = new CakeMapperImpl();
    }
}
