package cleanbreath.backend.controller;

import cleanbreath.backend.dto.common.MessageResponse;
import cleanbreath.backend.exception.ErrorCode;
import cleanbreath.backend.exception.GlobalExceptionHandler;
import cleanbreath.backend.service.FeedbackService;
import cleanbreath.backend.service.PendingAddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 익명 쓰기 엔드포인트의 요청 본문 검증 테스트. 잘못된 요청은 서비스에 닿기 전에 400이 된다.
 */
class RequestValidationTest {

  private static final String PATH = "{\"divisionArea\":\"NON_SMOKING_ZONE\","
      + "\"pathLat\":\"37.1,37.2,37.3\",\"pathLng\":\"127.1,127.2,127.3\"}";

  private PendingAddressService pendingService;
  private FeedbackService feedbackService;
  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    pendingService = mock(PendingAddressService.class);
    feedbackService = mock(FeedbackService.class);
    when(pendingService.saveAddressData(any())).thenReturn(MessageResponse.of("ok"));
    when(pendingService.vote(anyLong(), any())).thenReturn(MessageResponse.of("ok"));
    mockMvc = MockMvcBuilders
        .standaloneSetup(new PendingAddressController(pendingService), new FeedbackController(feedbackService))
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  private ResultActions postJson(String url, String body) throws Exception {
    return mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(body));
  }

  private static String addBody(String paths) {
    return "{\"updateAt\":\"2026-10-01 12:00:00\",\"addressName\":\"주소\",\"buildingName\":\"건물\","
        + "\"latitude\":37.2,\"longitude\":127.2,\"category\":\"NON_SMOKING\",\"paths\":[" + paths + "]}";
  }

  @Test
  @DisplayName("정상 투표 요청은 통과한다")
  void vote_valid_ok() throws Exception {
    postJson("/v1/smokingArea/1/vote", "{\"clientToken\":\"abc\",\"isTruth\":true}")
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("clientToken이 비었거나 64자를 넘으면 400을 반환한다")
  void vote_badToken_returns400() throws Exception {
    postJson("/v1/smokingArea/1/vote", "{\"isTruth\":true}")
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT.getMessage()));
    postJson("/v1/smokingArea/1/vote", "{\"clientToken\":\"  \",\"isTruth\":true}")
        .andExpect(status().isBadRequest());
    postJson("/v1/smokingArea/1/vote", "{\"clientToken\":\"" + "a".repeat(65) + "\",\"isTruth\":true}")
        .andExpect(status().isBadRequest());
    verify(pendingService, never()).vote(anyLong(), any());
  }

  @Test
  @DisplayName("정상 구역 등록 요청은 통과한다")
  void add_valid_ok() throws Exception {
    postJson("/v1/smokingArea/add", addBody(PATH)).andExpect(status().isOk());
  }

  @Test
  @DisplayName("필수 필드가 없으면 NPE(500) 대신 400을 반환한다")
  void add_missingFields_returns400() throws Exception {
    postJson("/v1/smokingArea/add", "{}").andExpect(status().isBadRequest());
    postJson("/v1/smokingArea/add", addBody(PATH).replace("\"latitude\":37.2,", ""))
        .andExpect(status().isBadRequest());
    postJson("/v1/smokingArea/add", addBody("")).andExpect(status().isBadRequest());
    postJson("/v1/smokingArea/add", addBody("{\"pathLat\":\"37.1,37.2,37.3\",\"pathLng\":\"127.1,127.2,127.3\"}"))
        .andExpect(status().isBadRequest());
    verify(pendingService, never()).saveAddressData(any());
  }

  @Test
  @DisplayName("영역이 10개를 넘거나 좌표·문자열이 범위를 넘으면 400을 반환한다")
  void add_oversized_returns400() throws Exception {
    postJson("/v1/smokingArea/add", addBody(String.join(",", Collections.nCopies(11, PATH))))
        .andExpect(status().isBadRequest());
    postJson("/v1/smokingArea/add", addBody(PATH).replace("\"latitude\":37.2", "\"latitude\":91"))
        .andExpect(status().isBadRequest());
    postJson("/v1/smokingArea/add", addBody(PATH).replace("주소", "a".repeat(256)))
        .andExpect(status().isBadRequest());
    postJson("/v1/smokingArea/add", addBody(PATH).replace("37.1,", "1".repeat(20001) + ","))
        .andExpect(status().isBadRequest());
    verify(pendingService, never()).saveAddressData(any());
  }

  @Test
  @DisplayName("피드백 제목·내용이 길이 제한을 넘으면 400을 반환한다")
  void feedback_oversized_returns400() throws Exception {
    postJson("/v1/feedback/add", "{\"title\":\"t\",\"content\":\"" + "a".repeat(2001) + "\"}")
        .andExpect(status().isBadRequest());
    postJson("/v1/feedback/add", "{\"title\":\"" + "t".repeat(101) + "\",\"content\":\"c\"}")
        .andExpect(status().isBadRequest());
    mockMvc.perform(put("/v1/feedback/1").contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\"t\",\"content\":\"" + "a".repeat(2001) + "\"}"))
        .andExpect(status().isBadRequest());
    verify(feedbackService, never()).save(any());
    verify(feedbackService, never()).updateFeedBack(anyLong(), any());
  }
}
