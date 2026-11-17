package org.com.recipes.user.dto;

import lombok.Builder;

/**
 * Public self-registration request. Role, id and registration date are assigned by the server.
 */
@Builder
public record RegisterUserDto(String username, String password) {
}
