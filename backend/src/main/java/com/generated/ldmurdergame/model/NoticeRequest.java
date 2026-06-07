package com.generated.ldmurdergame.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NoticeRequest(
  @NotBlank(message = "标题不能为空")
  @Size(max = 200, message = "标题长度不能超过200字符")
  String title,

  @NotBlank(message = "内容不能为空")
  String content,

  String level,

  Boolean isActive,

  Integer sortOrder
) {
}
