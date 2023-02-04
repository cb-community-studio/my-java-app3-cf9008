package com.codebridgecommunity.myjavaapp3.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.codebridgecommunity.myjavaapp3.domain.Students} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentsDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    private String name;

    @NotNull(message = "must not be null")
    private String email;

    private Integer age;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentsDTO)) {
            return false;
        }

        StudentsDTO studentsDTO = (StudentsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, studentsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentsDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", age=" + getAge() +
            "}";
    }
}
