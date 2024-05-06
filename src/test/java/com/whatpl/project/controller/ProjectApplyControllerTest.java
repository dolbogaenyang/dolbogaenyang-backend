package com.whatpl.project.controller;

import com.whatpl.ApiDocTag;
import com.whatpl.BaseSecurityWebMvcTest;
import com.whatpl.global.common.domain.enums.Job;
import com.whatpl.global.security.model.WithMockWhatplMember;
import com.whatpl.project.domain.enums.ApplyStatus;
import com.whatpl.project.domain.enums.ApplyType;
import com.whatpl.project.dto.ApplyResponse;
import com.whatpl.project.dto.ProjectApplyRequest;
import com.whatpl.project.dto.ProjectApplyStatusRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
class ProjectApplyControllerTest extends BaseSecurityWebMvcTest {

    @Test
    @WithMockWhatplMember
    @DisplayName("프로젝트 지원 API Docs")
    void apply() throws Exception {
        // given
        ProjectApplyRequest request = new ProjectApplyRequest(Job.BACKEND_DEVELOPER, "<p>테스트 콘텐츠 HTML<p>", ApplyType.APPLY);
        String requestJson = objectMapper.writeValueAsString(request);
        long projectId = 1L;
        long applyId = 1L;
        long chatRoomId = 1L;
        ApplyResponse applyResponse = ApplyResponse.builder()
                .applyId(applyId)
                .projectId(projectId)
                .chatRoomId(chatRoomId)
                .build();
        when(projectApplyService.apply(any(ProjectApplyRequest.class), anyLong(), anyLong()))
                .thenReturn(applyResponse);

        // expected
        mockMvc.perform(post("/projects/{projectId}/apply", projectId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {AccessToken}")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson)
                )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.applyId").value(applyId),
                        jsonPath("$.projectId").value(projectId),
                        jsonPath("$.chatRoomId").value(chatRoomId)
                )
                .andDo(print())
                .andDo(document("create-project-apply",
                        resourceDetails().tag(ApiDocTag.PROJECT.getTag())
                                .summary("프로젝트 지원")
                                .description("""
                                        프로젝트에 지원 또는 참여제안 API입니다.
                                                                                
                                        삭제된 프로젝트는 지원 불가
                                                                                
                                        모집완료된 프로젝트는 지원 불가
                                                                                
                                        프로젝트 등록자는 본인이 등록한 프로젝트에 지원 불가(지원일 경우)
                                                                                
                                        모집직군에 지원하는 직무가 없을 경우, 모집직군에 지원하는 직무가 마감된 경우 지원 불가
                                                                                
                                        이미 지원한 프로젝트는 지원 불가
                                        """),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        requestFields(
                                fieldWithPath("applyJob").type(JsonFieldType.STRING).description("지원 직무"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("지원 글"),
                                fieldWithPath("applyType").type(JsonFieldType.STRING).description("지원 타입 ( 지원 or 참여 제안 [APPLY, OFFER] )")
                        ),
                        responseFields(
                                fieldWithPath("applyId").type(JsonFieldType.NUMBER).description("지원 ID"),
                                fieldWithPath("projectId").type(JsonFieldType.NUMBER).description("프로젝트 ID"),
                                fieldWithPath("chatRoomId").type(JsonFieldType.NUMBER).description("채팅방 ID")
                        )
                ));
    }

    @Test
    @WithMockWhatplMember
    @DisplayName("프로젝트 지원서 승인/거절 API Docs")
    void applyStatus() throws Exception {
        // given
        long projectId = 1L;
        long applyId = 1L;
        ProjectApplyStatusRequest request = new ProjectApplyStatusRequest(ApplyStatus.ACCEPTED);
        String requestJson = objectMapper.writeValueAsString(request);
        doNothing().when(projectApplyService).status(anyLong(), anyLong(), any(ApplyStatus.class));

        // expected
        mockMvc.perform(patch("/projects/{projectId}/apply/{applyId}/status", projectId, applyId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {AccessToken}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                )
                .andExpectAll(
                        status().isNoContent()
                )
                .andDo(print())
                .andDo(document("update-project-apply-status",
                        resourceDetails().tag(ApiDocTag.PROJECT.getTag())
                                .summary("프로젝트 지원서 승인/거절")
                                .description("""
                                        프로젝트 지원서를 승인/거절합니다.
                                                                                
                                        프로젝트의 모집자만 승인/거절 가능합니다.
                                                                                
                                        승인 대기 상태로는 변경할 수 없습니다. (승인/거절만 가능)
                                        """),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 ID"),
                                parameterWithName("applyId").description("지원서 ID")
                        ),
                        requestFields(
                                fieldWithPath("applyStatus").type(JsonFieldType.STRING).description("지원 상태")
                        )
                ));
    }

}