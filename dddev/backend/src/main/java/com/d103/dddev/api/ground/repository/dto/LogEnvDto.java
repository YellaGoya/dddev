package com.d103.dddev.api.ground.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogEnvDto {
	private Integer id;
	private String type;
	private String value;
}
