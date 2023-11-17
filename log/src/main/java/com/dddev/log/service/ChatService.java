package com.dddev.log.service;

import com.dddev.log.dto.ElasticSearchLog;
import com.dddev.log.dto.req.ChatReq;
import com.dddev.log.dto.res.ChatRes;
import com.dddev.log.exception.ElasticSearchException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.NoSuchIndexException;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.search.sort.SortBuilders.fieldSort;
import static org.elasticsearch.search.sort.SortOrder.DESC;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    @Qualifier("openaiRestTemplate")
    private final RestTemplate restTemplate;
    private final Environment env;
    private final ElasticsearchOperations elasticsearchOperations;
    
    //CHAT GPT에 일반 질문
    public String chatGpt(String ground_id, String prompt){
        IndexOperations indexOperations = elasticsearchOperations.indexOps(IndexCoordinates.of(ground_id));
        if (!indexOperations.exists()) {
            throw new ElasticSearchException.NoIndexException("해당 인덱스가 없습니다.");
        }
        ChatReq request = new ChatReq(env.getProperty("openai.model"), prompt, "Please explain the current question " +
                "in detail with an easy example. Tell me in Korean");
        // call the API
        ChatRes response = restTemplate.postForObject(env.getProperty("openai.api.url"), request, ChatRes.class);
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return "No response";
        }
        // return the first response
        return response.getChoices().get(0).getMessage().getContent().replace("\\n", "\n");
    }

    //CHAT GPT에 로그 관련 질문
    public String chatGptLog(String ground_id, String prompt){
        IndexOperations indexOperations = elasticsearchOperations.indexOps(IndexCoordinates.of(ground_id));
        if (!indexOperations.exists()) {
            throw new ElasticSearchException.NoIndexException("해당 인덱스가 없습니다.");
        }
        ChatReq request = new ChatReq(env.getProperty("openai.model"), prompt, "You're a log analysis expert. " +
                "I know all the logs of any program. Kind and easy for developers trying to catch this error," +
                " let me know the solution. Tell me Only in Korean.");
        // call the API
        ChatRes response = restTemplate.postForObject(env.getProperty("openai.api.url"), request, ChatRes.class);
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return "No response";
        }
        // return the first response
        return response.getChoices().get(0).getMessage().getContent().replace("\\n", "\n");
    }

    //CHAT GPT에 정규표현식 관련 질문
    public String chatGptExp(String prompt){
        // create a request
        ChatReq request = new ChatReq(env.getProperty("openai.model"), prompt,"You are a regular expression expert in elasticsearch. " +
                "Please change the regular expression to the elasticserach regular expression. " +
                "However, the answer does not include any other rhetoric, just tell me the answer.");
        // call the API
        ChatRes response = restTemplate.postForObject(env.getProperty("openai.api.url"), request, ChatRes.class);

        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return "No response";
        }
        // return the first response
        return response.getChoices().get(0).getMessage().getContent().replace("\\n", "\n");
    }

    //최근 로그 불러서 분석
    public String chatGptLogAuto(String groupId) throws NoSuchIndexException {
        SearchHits<ElasticSearchLog> searchHits = elasticsearchOperations.search(
                new NativeSearchQueryBuilder()
                        .withQuery(matchAllQuery())
                        .withSort(fieldSort("localDateTime").order(DESC))
                        .withPageable(PageRequest.of(0, 20))
                        .build(), ElasticSearchLog.class, IndexCoordinates.of(groupId));

        if (searchHits.isEmpty()) throw new ElasticSearchException.NoContentException("저장된 로그가 없습니다.");
        StringBuilder temp = new StringBuilder();
        searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList()).forEach(a -> {
            temp.append(a.getLog()).append("\n");
        });
        ChatReq request = new ChatReq(env.getProperty("openai.model"), temp.toString(), "You're a log analysis expert. " +
                "I know all the logs of any program. " +
                "Kind and easy for developers trying to catch this error, let me know the solution. Tell me Only in Korean." +
                "And put \\n in the right place to answer");
        ChatRes response = restTemplate.postForObject(env.getProperty("openai.api.url"), request, ChatRes.class);
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return "No response";
        }
        return response.getChoices().get(0).getMessage().getContent().replace("\\n", "\n");
    }
}
