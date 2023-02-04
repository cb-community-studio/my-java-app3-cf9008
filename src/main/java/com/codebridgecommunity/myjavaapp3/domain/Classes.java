package com.codebridgecommunity.myjavaapp3.domain;

import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Classes.
 */
@Document(collection = "classes")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Classes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("class_name")
    private String className;

    @Field("time")
    private Instant time;

    @Field("teacher")
    private String teacher;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Classes id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return this.className;
    }

    public Classes className(String className) {
        this.setClassName(className);
        return this;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Instant getTime() {
        return this.time;
    }

    public Classes time(Instant time) {
        this.setTime(time);
        return this;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getTeacher() {
        return this.teacher;
    }

    public Classes teacher(String teacher) {
        this.setTeacher(teacher);
        return this;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Classes)) {
            return false;
        }
        return id != null && id.equals(((Classes) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Classes{" +
            "id=" + getId() +
            ", className='" + getClassName() + "'" +
            ", time='" + getTime() + "'" +
            ", teacher='" + getTeacher() + "'" +
            "}";
    }
}
