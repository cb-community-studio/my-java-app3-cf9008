package com.codebridgecommunity.myjavaapp3.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.codebridgecommunity.myjavaapp3.domain.Classes} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClassesDTO implements Serializable {

    private String id;

    private String className;

    private Instant time;

    private String teacher;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassesDTO)) {
            return false;
        }

        ClassesDTO classesDTO = (ClassesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, classesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassesDTO{" +
            "id='" + getId() + "'" +
            ", className='" + getClassName() + "'" +
            ", time='" + getTime() + "'" +
            ", teacher='" + getTeacher() + "'" +
            "}";
    }
}
