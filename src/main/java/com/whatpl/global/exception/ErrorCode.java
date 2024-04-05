package com.whatpl.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // TOKEN
    INVALID_TOKEN("TKN1", 401, "토큰정보가 유효하지 않습니다."),
    EXPIRED_TOKEN("TKN2", 401, "만료된 토큰입니다."),
    MALFORMED_TOKEN("TKN3", 401, "잘못된 형식의 토큰입니다."),
    INVALID_SIGNATURE("TKN4", 401, "변조된 토큰입니다."),

    // MEMBER
    NOT_FOUND_MEMBER("MBR1", 404, "사용자를 찾을 수 없습니다."),
    LOGIN_FAILED("MBR2", 401, "로그인에 실패했습니다."),
    NO_AUTHENTICATION("MBR3", 401, "인증되지 않은 사용자입니다."),
    NO_AUTHORIZATION("MBR4", 403, "접근권한이 없습니다."),
    HAS_NO_PROFILE("MBR5", 401, "프로필이 작성되지 않은 사용자입니다."),
    MAX_PORTFOLIO_SIZE_EXCEED("MBR6", 400, "포트폴리오는 최대 5개 첨부 가능합니다."),
    MAX_REFERENCE_SIZE_EXCEED("MBR7", 400, "참고링크는 최대 3개 첨부 가능합니다."),
    MAX_SUBJECT_SIZE_EXCEED("MBR8", 400, "관심주제는 최대 5개 입력 가능합니다."),

    // FILE
    NOT_FOUND_FILE("FILE1", 404, "파일을 찾을 수 없습니다."),
    FILE_SIZE_EXCEED("FILE2", 400, "파일 사이즈를 초과하였습니다."),
    FILE_TYPE_NOT_ALLOWED("FILE3", 400, "허용되지 않은 파일 타입입니다."),
    NOT_IMAGE_FILE("FILE4", 400, "이미지 파일이 아닙니다."),

    // COMMON
    GLOBAL_EXCEPTION("CMM1", 500, "서버에서 에러가 발생하였습니다."),
    NOT_FOUND_DATA("CMM2", 404, "데이터가 존재하지 않습니다."),
    MISSING_PARAMETER("CMM3", 400, "필수 파라미터가 존재하지 않습니다."),
    NOT_FOUND_API("CMM4", 404, "요청하신 API를 찾을 수 없습니다."),
    REQUEST_VALUE_INVALID("CMM5", 400, "입력값이 올바르지 않습니다."),
    REQUIRED_PARAMETER_MISSING("CMM6", 400, "필수 파라미터가 존재하지 않습니다."),
    HTTP_MESSAGE_NOT_READABLE("CMM7", 400, "요청 메시지를 읽을 수 없습니다. 요청 형식을 확인해 주세요."),

    // DOMAIN
    SKILL_NOT_VALID("DMN1", 400, "기술스택에 유효하지 않은 값이 존재합니다."),
    JOB_NOT_VALID("DMN2", 400, "직무에 유효하지 않은 값이 존재합니다."),
    CAREER_NOT_VALID("DMN3", 400, "경력에 유효하지 않은 값이 존재합니다."),
    SUBJECT_NOT_VALID("DMN4", 400, "주제에 유효하지 않은 값이 존재합니다."),
    WORK_TIME_NOT_VALID("DMN5", 400, "작업시간에 유효하지 않은 값이 존재합니다."),
    UP_DOWN_NOT_VALID("DMN6", 400, "이상/이하에 유효하지 않은 값이 존재합니다."),
    MEETING_TYPE_NOT_VALID("DMN7", 400, "모임 방식에 유효하지 않은 값이 존재합니다.");

    private final String code;
    private final int status;
    private final String message;
}
