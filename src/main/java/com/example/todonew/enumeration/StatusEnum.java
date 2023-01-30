package com.example.todonew.enumeration;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum StatusEnum {
    @JsonProperty("COMPLETED")

    COMPLETED,
    NOT_COMPLETED
}
