package com.dddev.log.dto.res;

import com.dddev.log.dto.ElasticSearchLog;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "현재 페이지 번호", example = "1")
    private int pageNumber;
    @ApiModelProperty(value = "전체 페이지 수", example = "3")
    private int pageSize;
    @ApiModelProperty(value = "로그 데이터", example = " {\n" +
            "        \"localDateTime\": \"2023-11-14T23:47:46.725\",\n" +
            "        \"log\": \"1234\"\n" +
            "      },")
    private List<ElasticSearchLog> logs;
}
