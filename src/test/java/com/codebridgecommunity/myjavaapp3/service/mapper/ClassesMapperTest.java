package com.codebridgecommunity.myjavaapp3.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClassesMapperTest {

    private ClassesMapper classesMapper;

    @BeforeEach
    public void setUp() {
        classesMapper = new ClassesMapperImpl();
    }
}
