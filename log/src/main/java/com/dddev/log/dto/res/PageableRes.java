package com.dddev.log.dto.res;

import com.dddev.log.dto.ElasticSearchLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableRes {
    private int pageNumber;
    private int pageSize;
    private List<ElasticSearchLog> logs;
}
