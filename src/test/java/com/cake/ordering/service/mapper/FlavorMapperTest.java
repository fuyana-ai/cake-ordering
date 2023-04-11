package com.cake.ordering.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FlavorMapperTest {

    private FlavorMapper flavorMapper;

    @BeforeEach
    public void setUp() {
        flavorMapper = new FlavorMapperImpl();
    }
}
