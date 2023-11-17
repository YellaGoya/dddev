package com.dddev.log.repository;

import com.dddev.log.dto.ElasticSearchLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.time.LocalDateTime;

public interface ElasticSearchLogRepository extends ElasticsearchRepository<ElasticSearchLog, LocalDateTime>{


}
