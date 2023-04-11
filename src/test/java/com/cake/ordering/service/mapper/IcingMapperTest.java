package com.cake.ordering.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IcingMapperTest {

    private IcingMapper icingMapper;

    @BeforeEach
    public void setUp() {
        icingMapper = new IcingMapperImpl();
    }
}
