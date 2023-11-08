package com.d103.dddev.api.issue.model.message;

import lombok.Builder;

public class TargetMessage {

    public static String create(){
        return "목표 문서 생성 완료";
    }

    public static String list(){
        return "목표 문서 목록 조회 완료";
    }

    public static String emptyList(){return "목표 문서 조회 완료, 결과 없음";}

    public static String detail(){
        return "목표 문서 상세 조회 완료";
    }

    public static String delete(){
        return "목표 문서 삭제 완료";
    }

    public static String update(){
        return "목표 문서 수정 완료";
    }

    public static String tree(){
        return "그라운드 전체 문서 트리";
    }

}
