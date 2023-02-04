package com.codebridgecommunity.myjavaapp3.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentsMapperTest {

    private StudentsMapper studentsMapper;

    @BeforeEach
    public void setUp() {
        studentsMapper = new StudentsMapperImpl();
    }
}
