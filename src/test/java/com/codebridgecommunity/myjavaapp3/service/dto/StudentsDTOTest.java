package com.codebridgecommunity.myjavaapp3.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.codebridgecommunity.myjavaapp3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentsDTO.class);
        StudentsDTO studentsDTO1 = new StudentsDTO();
        studentsDTO1.setId("id1");
        StudentsDTO studentsDTO2 = new StudentsDTO();
        assertThat(studentsDTO1).isNotEqualTo(studentsDTO2);
        studentsDTO2.setId(studentsDTO1.getId());
        assertThat(studentsDTO1).isEqualTo(studentsDTO2);
        studentsDTO2.setId("id2");
        assertThat(studentsDTO1).isNotEqualTo(studentsDTO2);
        studentsDTO1.setId(null);
        assertThat(studentsDTO1).isNotEqualTo(studentsDTO2);
    }
}
