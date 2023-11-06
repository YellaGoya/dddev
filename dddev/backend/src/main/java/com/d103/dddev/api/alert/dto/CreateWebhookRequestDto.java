package com.d103.dddev.api.alert.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter @Setter @ToString
public class CreateWebhookRequestDto {
	private Integer repositoryId;
	private List<String> keyword;
	private String type;
}
