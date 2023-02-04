package com.codebridgecommunity.myjavaapp3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.codebridgecommunity.myjavaapp3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Classes.class);
        Classes classes1 = new Classes();
        classes1.setId("id1");
        Classes classes2 = new Classes();
        classes2.setId(classes1.getId());
        assertThat(classes1).isEqualTo(classes2);
        classes2.setId("id2");
        assertThat(classes1).isNotEqualTo(classes2);
        classes1.setId(null);
        assertThat(classes1).isNotEqualTo(classes2);
    }
}
