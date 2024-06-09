package com.hks.blog.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;
import java.util.Set;
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {
    private Integer errorCode;
    private String errorDescription;
    private String error;
    private Set<String> validationError;
    private Map<String,String> errors;
}
