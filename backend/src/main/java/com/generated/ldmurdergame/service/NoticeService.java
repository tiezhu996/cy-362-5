package com.generated.ldmurdergame.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.generated.ldmurdergame.exception.ApiException;
import com.generated.ldmurdergame.mapper.NoticeMapper;
import com.generated.ldmurdergame.model.Notice;
import com.generated.ldmurdergame.model.NoticeRequest;

@Service
public class NoticeService {
  private final NoticeMapper noticeMapper;

  public NoticeService(NoticeMapper noticeMapper) {
    this.noticeMapper = noticeMapper;
  }

  public List<Notice> getActiveNotices() {
    return noticeMapper.findAllActive();
  }

  public List<Notice> getAllNotices() {
    return noticeMapper.findAll();
  }

  public Notice getNoticeById(Long id) {
    Notice notice = noticeMapper.findById(id);
    if (notice == null) {
      throw new ApiException("公告不存在");
    }
    return notice;
  }

  public Notice createNotice(NoticeRequest request) {
    Notice notice = new Notice();
    notice.setTitle(request.title());
    notice.setContent(request.content());
    notice.setLevel(request.level() != null ? request.level() : "NORMAL");
    notice.setIsActive(request.isActive() != null ? request.isActive() : true);
    notice.setSortOrder(request.sortOrder() != null ? request.sortOrder() : 0);
    notice.setCreatedAt(LocalDateTime.now());
    notice.setUpdatedAt(LocalDateTime.now());
    noticeMapper.insert(notice);
    return noticeMapper.findById(notice.getId());
  }

  public Notice updateNotice(Long id, NoticeRequest request) {
    Notice existing = getNoticeById(id);
    Notice updated = new Notice();
    updated.setId(id);
    updated.setTitle(request.title());
    updated.setContent(request.content());
    updated.setLevel(request.level() != null ? request.level() : existing.getLevel());
    updated.setIsActive(request.isActive() != null ? request.isActive() : existing.getIsActive());
    updated.setSortOrder(request.sortOrder() != null ? request.sortOrder() : existing.getSortOrder());
    updated.setCreatedAt(existing.getCreatedAt());
    updated.setUpdatedAt(LocalDateTime.now());
    noticeMapper.update(updated);
    return noticeMapper.findById(id);
  }

  public void deleteNotice(Long id) {
    getNoticeById(id);
    noticeMapper.deleteById(id);
  }
}
