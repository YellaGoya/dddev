package com.d103.dddev.api.repository.collection;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "repository_files")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepositoryFiles {
	@Id
	private String id;
	private String name;
	private String path;
	private String type;
	private String sha;
	private List<RepositoryFiles> children;
}
