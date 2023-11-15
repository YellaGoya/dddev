package com.dddev.log.service;

import com.dddev.log.dto.ElasticSearchLog;
import com.dddev.log.exception.ElasticSearchException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.NoSuchIndexException;
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
    private final ChatService chatService;

    //로그 등록
    public void save(String groudId, ElasticSearchLog elasticSearchLog){
        IndexOperations indexOperations = elasticsearchOperations.indexOps(IndexCoordinates.of(groudId));
        // 인덱스가 존재하지 않으면 동적으로 인덱스를 생성
        if (!indexOperations.exists()) {
            indexOperations.create();
        }
        IndexQuery indexQuery = new IndexQuery();
        indexQuery.setObject(elasticSearchLog);
        elasticsearchOperations.index(indexQuery, IndexCoordinates.of(groudId));
    }


    //전체 로그 불러오기
    public Page<ElasticSearchLog> getLatestLogs(String groudId, int page) throws NoSuchIndexException {
        SearchHits<ElasticSearchLog> searchHits = elasticsearchOperations.search(
                new NativeSearchQueryBuilder()
                        .withQuery(matchAllQuery())
                        .withSort(fieldSort("localDateTime").order(DESC))
                        .withPageable(PageRequest.of(page, 30))
                        .build(), ElasticSearchLog.class, IndexCoordinates.of(groudId));
        List<ElasticSearchLog> logs = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
        long totalHits = searchHits.getTotalHits();
        return new PageImpl<>(logs, PageRequest.of(page, 30), totalHits);
    }


    //인덱스별 키워드 가져오기
    public Page<ElasticSearchLog> getKeywordtLogs(String groudId, String keyword, int page) throws NoSuchIndexException {
        try {
            SearchHits<ElasticSearchLog> searchHits = elasticsearchOperations.search(
                    new NativeSearchQueryBuilder()
                            .withQuery(queryStringQuery("*" + keyword + "*").field("log"))
                            .withSort(fieldSort("localDateTime").order(DESC))
                            .withPageable(PageRequest.of(page, 30))
                            .build(), ElasticSearchLog.class, IndexCoordinates.of(groudId));
            List<ElasticSearchLog> logs = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
            long totalHits = searchHits.getTotalHits();
            return new PageImpl<>(logs, PageRequest.of(page, 30), totalHits);
        }catch (ElasticsearchStatusException e){
            log.info(e.getMessage());
            throw new ElasticSearchException.NoIndexException("해당 하는 그룹 id는 없습니다.");
        }
    }

    //정규표현식으로 가져오기
    public Page<ElasticSearchLog> getRegexptLogs(String groudId, String regexp, int page) throws NoSuchIndexException {
        try {
            log.info("요청한 정규표현식은 {}", regexp);
            SearchHits<ElasticSearchLog> searchHits = elasticsearchOperations.search(
                    new NativeSearchQueryBuilder()
                            .withQuery(regexpQuery("log", regexp))
                            .withSort(fieldSort("localDateTime").order(DESC))
                            .withPageable(PageRequest.of(page, 30))
                            .build(), ElasticSearchLog.class, IndexCoordinates.of(groudId));
            List<ElasticSearchLog> logs = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
            long totalHits = searchHits.getTotalHits();
            return new PageImpl<>(logs, PageRequest.of(page, 30), totalHits);
        }catch (ElasticsearchStatusException e){
            log.info(e.getMessage());
            throw new ElasticSearchException.NoIndexException("해당 하는 그룹 id는 없습니다.");
        }
    }

    //시간대별로 가져오기
    public Page<ElasticSearchLog> getTimetLogs(String groudId, LocalDateTime startDateTime, LocalDateTime endDateTime, int page) throws NoSuchIndexException {
        try {
            SearchHits<ElasticSearchLog> searchHits = elasticsearchOperations.search(
                    new NativeSearchQueryBuilder()
                            .withQuery(rangeQuery("localDateTime")
                                    .gte(startDateTime.format(DateTimeFormatter.ISO_DATE_TIME))
                                    .lte(endDateTime.format(DateTimeFormatter.ISO_DATE_TIME))
                            )
                            .withSort(fieldSort("localDateTime").order(DESC))
                            .withPageable(PageRequest.of(page, 30))
                            .build(), ElasticSearchLog.class, IndexCoordinates.of(groudId));
            List<ElasticSearchLog> logs = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
            long totalHits = searchHits.getTotalHits();
            return new PageImpl<>(logs, PageRequest.of(page, 30), totalHits);
        }catch (ElasticsearchStatusException e){
            log.info(e.getMessage());
            throw new ElasticSearchException.NoIndexException("해당 하는 그룹 id는 없습니다.");
        }
    }

    //인덱스 삭제
    public void deleteIndex(String groudId) {
        IndexOperations indexOperations = elasticsearchOperations.indexOps(IndexCoordinates.of(groudId));
        if (!indexOperations.exists()) {
            throw new ElasticSearchException.NoIndexException("해당 인덱스가 없습니다.");
        }
        elasticsearchOperations.indexOps(IndexCoordinates.of(groudId)).delete();
    }

    //시간대별로 가져오기
    public Page<ElasticSearchLog> getTimeAndKeywordLogs(String groudId, LocalDateTime startDateTime, LocalDateTime endDateTime, String keyword, int page) throws NoSuchIndexException {
        try {
            SearchHits<ElasticSearchLog> searchHits = elasticsearchOperations.search(
                    new NativeSearchQueryBuilder()
                            .withQuery(boolQuery()
                                    .must(rangeQuery("localDateTime")
                                            .gte(startDateTime.format(DateTimeFormatter.ISO_DATE_TIME))
                                            .lte(endDateTime.format(DateTimeFormatter.ISO_DATE_TIME))
                                    )
                                    .must(queryStringQuery("*" + keyword + "*").field("log"))
                            )
                            .withSort(fieldSort("localDateTime").order(DESC))
                            .withPageable(PageRequest.of(page, 30))
                            .build(), ElasticSearchLog.class, IndexCoordinates.of(groudId));
            List<ElasticSearchLog> logs = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
            long totalHits = searchHits.getTotalHits();
            return new PageImpl<>(logs, PageRequest.of(page, 30), totalHits);
        }catch (ElasticsearchStatusException e){
            log.info(e.getMessage());
            throw new ElasticSearchException.NoIndexException("해당 하는 그룹 id는 없습니다.");
        }
    }
}
