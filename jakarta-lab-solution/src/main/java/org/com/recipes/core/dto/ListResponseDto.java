package org.com.recipes.core.dto;

import java.util.List;

public record ListResponseDto<T>(List<T> data) {
}
