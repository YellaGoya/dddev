package com.d103.dddev.api.alert.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
@Data
public class CreateWebhookResponseDto {
	Integer id; // 439840320
	List<String> events; // ["push", "pull request"]
}
