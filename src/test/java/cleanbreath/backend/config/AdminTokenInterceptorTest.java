package cleanbreath.backend.config;

import cleanbreath.backend.controller.FeedbackController;
import cleanbreath.backend.exception.GlobalExceptionHandler;
import cleanbreath.backend.service.FeedbackService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * WebConfig에 등록한 관리용 피드백 API 토큰 검사 테스트.
 */
class AdminTokenInterceptorTest {

  private static final String TOKEN = "test-admin-token";

  // InterceptorRegistry의 protected 조회를 테스트에서 열어줌
  static class ExposedInterceptorRegistry extends InterceptorRegistry {
    List<Object> interceptors() {
      return getInterceptors();
    }
  }

  private MockMvc mockMvcWith(String adminToken) {
    WebConfig config = new WebConfig();
    ReflectionTestUtils.setField(config, "adminToken", adminToken);
    ExposedInterceptorRegistry registry = new ExposedInterceptorRegistry();
    config.addInterceptors(registry);

    return MockMvcBuilders.standaloneSetup(new FeedbackController(mock(FeedbackService.class)))
        .setControllerAdvice(new GlobalExceptionHandler())
        .addInterceptors(registry.interceptors().stream().map(HandlerInterceptor.class::cast)
            .toArray(HandlerInterceptor[]::new))
        .build();
  }

  @Test
  @DisplayName("토큰 없이 피드백 목록·상세·수정·삭제를 요청하면 401")
  void adminEndpoints_withoutToken_return401() throws Exception {
    MockMvc mockMvc = mockMvcWith(TOKEN);

    mockMvc.perform(get("/v1/feedback-list")).andExpect(status().isUnauthorized());
    mockMvc.perform(get("/v1/feedback/1")).andExpect(status().isUnauthorized());
    mockMvc.perform(put("/v1/feedback/1").contentType(MediaType.APPLICATION_JSON).content("{}"))
        .andExpect(status().isUnauthorized());
    mockMvc.perform(delete("/v1/feedback/1")).andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("틀린 토큰은 401, 맞는 토큰은 통과")
  void adminEndpoints_checkToken() throws Exception {
    MockMvc mockMvc = mockMvcWith(TOKEN);

    mockMvc.perform(get("/v1/feedback-list").header("Authorization", "Bearer wrong"))
        .andExpect(status().isUnauthorized());
    mockMvc.perform(get("/v1/feedback-list").header("Authorization", "Bearer " + TOKEN))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("ADMIN_TOKEN이 비어 있으면 빈 Bearer 토큰도 거부한다")
  void emptyConfiguredToken_rejectsAll() throws Exception {
    MockMvc mockMvc = mockMvcWith("");

    mockMvc.perform(get("/v1/feedback-list").header("Authorization", "Bearer "))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("피드백 등록은 토큰 없이 가능하다")
  void addFeedback_isPublic() throws Exception {
    MockMvc mockMvc = mockMvcWith(TOKEN);

    mockMvc.perform(post("/v1/feedback/add").contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\"제목\",\"content\":\"내용\"}"))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("제목이나 내용이 비어 있는 피드백 등록은 400")
  void addFeedback_blankFields_return400() throws Exception {
    MockMvc mockMvc = mockMvcWith(TOKEN);

    mockMvc.perform(post("/v1/feedback/add").contentType(MediaType.APPLICATION_JSON).content("{}"))
        .andExpect(status().isBadRequest());
    mockMvc.perform(post("/v1/feedback/add").contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\":\" \",\"content\":\"내용\"}"))
        .andExpect(status().isBadRequest());
  }
}
