package com.d103.dddev.api.common.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * document 생성 또는 연결 시 체크 사항 필터
 *
 * 생성
 * 1. 지정한 Tag와 연결한 문서의 Tag가 동일하거나 역전되는 경우 deny
 *
 * 연결
 * 1. 연결하는 상위 문서의 Tag가 동일하거나 역전되는 경우 deny
 *
 *
 *
 *
 * */

public class DocumentFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //
    }
}
