package org.andon.bluetooth_service.dto.TestResult;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.andon.bluetooth_service.base.ResponseEntity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class DTOTestPost {
    private String name;
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
