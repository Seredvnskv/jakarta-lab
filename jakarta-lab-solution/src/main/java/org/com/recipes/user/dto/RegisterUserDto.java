package org.com.recipes.user.dto;

import lombok.Builder;

@Builder
public record RegisterUserDto(String username, String password) {
}
