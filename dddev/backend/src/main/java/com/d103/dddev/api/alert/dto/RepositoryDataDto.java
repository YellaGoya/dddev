package com.d103.dddev.api.alert.dto;

import lombok.Data;

@Data
public class RepositoryDataDto {
	Integer id;
	String nodeId;
	String name;
	String fullName;
	/*
	* "repository": {
    	"id": 707050040,
    	"node_id": "R_kgDOKiS6OA",
    	"name": "webhook-test",
    	"full_name": "gayun0303/webhook-test",
    	"private": false,
    	"owner": {}
    * }
	* */
}
