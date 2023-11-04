package com.d103.dddev.api.issue.model.message;

public class CheckMessage {

    public static String create(){
        return "체크 포인트 문서 생성 완료";
    }

    public static String list(){
        return "체크 포인트 문서 목록 조회 완료";
    }
    public static String emptyList(){return "체크 포인트 문서 조회 완료, 결과 없음";}

    public static String detail(){
        return "체크 포인트 문서 상세 조회 완료";
    }

    public static String delete(){
        return "체크 포인트 문서 삭제 완료";
    }

    public static String update(){
        return "체크 포인트 문서 수정 완료";
    }

    public static String contains() {return "이미 연결된 문서입니다.";}
    public static String notContains() {return "연결된 문서가 아닙니다.";}

    public static String connect(){return "문서의 연결이 완료 되었습니다.";}
}
