package com.dddev.log.config;

import com.dddev.log.dto.ElasticSearchLog;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@RequiredArgsConstructor
@EnableElasticsearchRepositories(basePackageClasses = {ElasticSearchLog.class})
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

    private final Environment env;

    @Override
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("k9d103a.p.ssafy.io:9200")
                .withBasicAuth(env.getProperty("elasticSearch.id"), env.getProperty("elasticSearch.password"))
                .build();
        return RestClients.create(clientConfiguration).rest();
    }
}
