package edu.tcu.cs.hogwartsartifactsonline.hogwartsuser.dto;

import jakarta.validation.constraints.NotEmpty;

public record UserDto(Integer id,
                      @NotEmpty(message = "Username cannot be empty")
                      String username,

                      boolean enabled,

                      @NotEmpty(message = "Role cannot be empty")
                        String roles
                      ) {
}
