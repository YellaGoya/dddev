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
    public static String contains() {return "이미 연결 된 문서.";}
    public static String notContains() {return "연결되지 않은 문서";}
    public static String connect(){return "체크 포인트 문서 연결 변경 완료";}

    public static String title() {return "체크 포인트 문서 제목 수정 완료";
    }
}
