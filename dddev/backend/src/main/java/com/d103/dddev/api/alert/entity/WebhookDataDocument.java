package com.d103.dddev.api.alert.entity;

import java.util.Date;

import com.d103.dddev.api.user.repository.dto.UserDto;
import com.d103.dddev.api.user.repository.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class WebhookDataDocument {

	private String id;	// commit SHA / pull request id
	private UserDto author;
	private String message;
	private String branch;
	private Date timestamp;
	private String url;
	private Integer groundId;
	private String type;
}
