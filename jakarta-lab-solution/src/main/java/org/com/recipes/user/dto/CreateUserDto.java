package org.com.recipes.user.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CreateUserDto(String id, String username, String password, String role, LocalDate registrationDate) {
}
