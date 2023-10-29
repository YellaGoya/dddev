package com.d103.dddev.api.alert.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class CommitDataDto {
	String id;
	String treeId;
	String distinct;
	/*
	* commit message
	* "Update README.md"
	*/
	String message;
	LocalDateTime timestamp;
	/*
	* github url : commit SHA url
	* "https://github.com/gayun0303/webhook-test/commit/1be5342d1cc8d8e0f39edbaeb924d0b450abde83"
	*/
	String url;

	/*
	* "author": {
        "name": "gayun0303",
        "email": "111165249+gayun0303@users.noreply.github.com",
        "username": "gayun0303"
      },
	* */
	Map<String, Object> author;

	/*
	* changed file list
	* */
	List<String> added;
	List<String> removed;
	List<String> modified;

}
