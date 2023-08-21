package com.stackoverflow.Server.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_EXIST(409, "Member exists"),
    MEMBER_NOT_FOUND(404, "Member not found"),
    QUESTION_NOT_FOUND(405, "Question not found"),
    COMMENT_NOT_FOUND(405, "comment not found"),
    COMMENT_EXISTS(400,"comment exists");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }
}
