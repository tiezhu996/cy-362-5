package com.generated.ldmurdergame.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.generated.ldmurdergame.model.Notice;
import com.generated.ldmurdergame.model.NoticeRequest;
import com.generated.ldmurdergame.service.NoticeService;

@RestController
@RequestMapping({"/api/notices", "/notices"})
public class NoticeController {
  private final NoticeService noticeService;

  public NoticeController(NoticeService noticeService) {
    this.noticeService = noticeService;
  }

  @GetMapping
  public List<Notice> getActiveNotices() {
    return noticeService.getActiveNotices();
  }

  @GetMapping("/all")
  public List<Notice> getAllNotices() {
    return noticeService.getAllNotices();
  }

  @GetMapping("/{id}")
  public Notice getNoticeById(@PathVariable Long id) {
    return noticeService.getNoticeById(id);
  }

  @PostMapping
  public Notice createNotice(@Valid @RequestBody NoticeRequest request) {
    return noticeService.createNotice(request);
  }

  @PutMapping("/{id}")
  public Notice updateNotice(@PathVariable Long id, @Valid @RequestBody NoticeRequest request) {
    return noticeService.updateNotice(id, request);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Map<String, String>> deleteNotice(@PathVariable Long id) {
    noticeService.deleteNotice(id);
    Map<String, String> response = new HashMap<>();
    response.put("message", "删除成功");
    return ResponseEntity.ok(response);
  }
}
