package com.reliaquest.api.Beans;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Employee {

    @JsonAlias("id")
    private String id;

    @JsonAlias("employee_name")
    private String name;

    @JsonAlias("employee_salary")
    private Integer salary;

    @JsonAlias("employee_age")
    private Integer age;

    @JsonAlias("employee_title")
    private String title;

    @JsonAlias("employee_email")
    private String email;

    @JsonCreator
    public Employee(@JsonProperty("id") String id, @JsonProperty("employee_name") String name,
                    @JsonProperty("employee_salary") Integer salary,@JsonProperty("employee_age") Integer age,
                    @JsonProperty("employee_title") String title,@JsonProperty("employee_email") String email) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.age = age;
        this.title = title;
        this.email = email;
    }
}