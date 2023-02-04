package com.codebridgecommunity.myjavaapp3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.codebridgecommunity.myjavaapp3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StudentsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Students.class);
        Students students1 = new Students();
        students1.setId("id1");
        Students students2 = new Students();
        students2.setId(students1.getId());
        assertThat(students1).isEqualTo(students2);
        students2.setId("id2");
        assertThat(students1).isNotEqualTo(students2);
        students1.setId(null);
        assertThat(students1).isNotEqualTo(students2);
    }
}
