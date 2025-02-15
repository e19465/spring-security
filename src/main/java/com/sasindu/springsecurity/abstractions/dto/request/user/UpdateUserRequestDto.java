package com.sasindu.springsecurity.abstractions.dto.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserRequestDto {
    @NotNull
    private String firstName;

    private String lastName = "";
}
