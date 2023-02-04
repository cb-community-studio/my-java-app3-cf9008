package com.codebridgecommunity.myjavaapp3.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.codebridgecommunity.myjavaapp3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassesDTO.class);
        ClassesDTO classesDTO1 = new ClassesDTO();
        classesDTO1.setId("id1");
        ClassesDTO classesDTO2 = new ClassesDTO();
        assertThat(classesDTO1).isNotEqualTo(classesDTO2);
        classesDTO2.setId(classesDTO1.getId());
        assertThat(classesDTO1).isEqualTo(classesDTO2);
        classesDTO2.setId("id2");
        assertThat(classesDTO1).isNotEqualTo(classesDTO2);
        classesDTO1.setId(null);
        assertThat(classesDTO1).isNotEqualTo(classesDTO2);
    }
}
