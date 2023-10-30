package com.dddev.log.service;

import com.dddev.log.dto.ElasticSearchLog;
import com.dddev.log.exception.ElasticSearchException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.search.sort.SortBuilders.fieldSort;
import static org.elasticsearch.search.sort.SortOrder.DESC;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElasticSearchLogService {

    private final ElasticsearchOperations elasticsearchOperations;

    //로그 등록
    public void save(String groupId, ElasticSearchLog elasticSearchLog){
        IndexOperations indexOperations = elasticsearchOperations.indexOps(IndexCoordinates.of(groupId));
        // 인덱스가 존재하지 않으면 동적으로 인덱스를 생성
        if (!indexOperations.exists()) {
            indexOperations.create();
        }
        IndexQuery indexQuery = new IndexQuery();
        indexQuery.setObject(elasticSearchLog);
        elasticsearchOperations.index(indexQuery, IndexCoordinates.of(groupId));
    }

    //인덱스별 정해진 특정 개수 만큼 가져오기
    public List<ElasticSearchLog> getLatestLogs(String groupId, int size) {
        SearchHits<ElasticSearchLog> searchHits = elasticsearchOperations.search(
                new NativeSearchQueryBuilder()
                        .withQuery(matchAllQuery())
                        .withSort(fieldSort("localDateTime").order(DESC))
                        .withPageable(PageRequest.of(0, size))
                        .build(), ElasticSearchLog.class, IndexCoordinates.of(groupId));
        if (searchHits.isEmpty()) throw new  ElasticSearchException.NoContentException("저장된 로그가 없습니다.");
        return searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    //인덱스별 키워드 가져오기
    public List<ElasticSearchLog> getKeywordtLogs(String groupId, String keyword) {
        SearchHits<ElasticSearchLog> searchHits = elasticsearchOperations.search(
                new NativeSearchQueryBuilder()
                        .withQuery(queryStringQuery("*" + keyword + "*").field("log"))
                        .withSort(fieldSort("localDateTime").order(DESC))
                        .build(), ElasticSearchLog.class, IndexCoordinates.of(groupId));
        if (searchHits.isEmpty()) throw new ElasticSearchException.NoContentException("저장된 로그가 없습니다.");
        return searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    //정규표현식으로 가져오기
    public List<ElasticSearchLog> getRegexptLogs(String groupId, String regexp) {
        log.info(regexp);
        SearchHits<ElasticSearchLog> searchHits = elasticsearchOperations.search(
                new NativeSearchQueryBuilder()
                        .withQuery(regexpQuery("log", regexp))
                        .withSort(fieldSort("localDateTime").order(DESC))
                        .build(), ElasticSearchLog.class, IndexCoordinates.of(groupId));
        if (searchHits.isEmpty()) throw new  ElasticSearchException.NoContentException("저장된 로그가 없습니다.");
        return searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    //시간대별로 가져오기
    public List<ElasticSearchLog> getTimetLogs(String groupId, LocalDateTime startDateTime, LocalDateTime endDateTime)  {
        SearchHits<ElasticSearchLog> searchHits = elasticsearchOperations.search(
                new NativeSearchQueryBuilder()
                        .withQuery(rangeQuery("localDateTime")
                                .gte(startDateTime.format(DateTimeFormatter.ISO_DATE_TIME))
                                .lte(endDateTime.format(DateTimeFormatter.ISO_DATE_TIME))
                        )
                        .withSort(fieldSort("localDateTime").order(DESC))
                        .build(), ElasticSearchLog.class, IndexCoordinates.of(groupId));
        if (searchHits.isEmpty()) throw new  ElasticSearchException.NoContentException("저장된 로그가 없습니다.");
        return searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

//    public List<ElasticSearchLog> getRecentLogs(int numLogs) {
//        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
//                .withPageable(PageRequest.of(0, numLogs))
//                .withSort(SortBuilders.fieldSort("localDateTime").order(SortOrder.DESC));
//
//        return elasticsearchRestTemplate.queryForList(queryBuilder.build(), ElasticSearchLog.class);
//    }

}
