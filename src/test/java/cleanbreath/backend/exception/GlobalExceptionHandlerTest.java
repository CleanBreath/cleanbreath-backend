package cleanbreath.backend.exception;

import cleanbreath.backend.dto.AddressDto;
import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.data.core.TypeInformation;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * GlobalExceptionHandler 상태 코드 매핑 테스트.
 */
class GlobalExceptionHandlerTest {

  @RestController
  static class TestController {
    @PostMapping("/v1/updateDate")
    String update(@Valid @RequestBody AddressDto.CheckUpdate body) {
      return "ok";
    }

    @GetMapping("/v1/number")
    String number(@RequestParam("n") Integer n) {
      return "ok";
    }

    // 없는 sort 속성이면 Spring Data가 쿼리를 만들다 던진다
    @GetMapping("/v1/sorted")
    String sorted() {
      throw new PropertyReferenceException("nope", TypeInformation.of(String.class), List.of());
    }

    @GetMapping("/v1/duplicate")
    String duplicate() {
      throw new DataIntegrityViolationException("Duplicate entry 'x' for key 'uk_client_token_target_id'");
    }

    @GetMapping("/v1/boom")
    String boom() {
      throw new IllegalStateException("secret detail");
    }
  }

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  @Test
  @DisplayName("POST 전용 경로에 GET을 보내면 405와 Allow 헤더를 반환한다")
  void methodNotSupported_returns405() throws Exception {
    mockMvc.perform(get("/v1/updateDate"))
        .andExpect(status().isMethodNotAllowed())
        .andExpect(header().string("Allow", "POST"))
        .andExpect(jsonPath("$.message").value("지원하지 않는 요청 메서드입니다."));
  }

  @Test
  @DisplayName("지원하지 않는 Content-Type이면 415를 반환한다")
  void mediaTypeNotSupported_returns415() throws Exception {
    mockMvc.perform(post("/v1/updateDate").contentType(MediaType.TEXT_PLAIN).content("x"))
        .andExpect(status().isUnsupportedMediaType());
  }

  @Test
  @DisplayName("본문을 읽을 수 없으면 400을 반환한다")
  void unreadableBody_returns400() throws Exception {
    mockMvc.perform(post("/v1/updateDate")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"updateDate\":\"2026-10-01\"}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT.getMessage()));
  }

  @Test
  @DisplayName("필수 필드가 없으면 검증 실패로 400을 반환한다")
  void missingField_returns400() throws Exception {
    mockMvc.perform(post("/v1/updateDate").contentType(MediaType.APPLICATION_JSON).content("{}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("파라미터가 없거나 타입이 맞지 않으면 400을 반환한다")
  void badParam_returns400() throws Exception {
    mockMvc.perform(get("/v1/number")).andExpect(status().isBadRequest());
    mockMvc.perform(get("/v1/number").param("n", "abc")).andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("없는 경로는 404를 반환한다")
  void unknownPath_returns404() throws Exception {
    mockMvc.perform(get("/v1/nope"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("요청한 경로를 찾을 수 없습니다."));
  }

  @Test
  @DisplayName("없는 정렬 속성이면 500 대신 400을 반환한다")
  void badSortProperty_returns400() throws Exception {
    mockMvc.perform(get("/v1/sorted"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT.getMessage()));
  }

  @Test
  @DisplayName("DB 제약 위반은 SQL 내용 없이 409를 반환한다")
  void dataIntegrityViolation_returns409() throws Exception {
    mockMvc.perform(get("/v1/duplicate"))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("요청이 기존 데이터와 충돌합니다."));
  }

  @Test
  @DisplayName("예상치 못한 예외는 상세 내용 없이 500을 반환한다")
  void unexpected_returns500() throws Exception {
    mockMvc.perform(get("/v1/boom"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.message").value("서버 내부 오류가 발생했습니다."));
  }
}
