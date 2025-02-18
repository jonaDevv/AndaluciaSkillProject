package com.jrm.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class TestSimpleDto {

    @JsonProperty("mensaje")
    private String mensaje;
}