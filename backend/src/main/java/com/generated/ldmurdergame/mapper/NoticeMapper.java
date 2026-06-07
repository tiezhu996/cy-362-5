package com.generated.ldmurdergame.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.generated.ldmurdergame.model.Notice;

@Mapper
public interface NoticeMapper {
  List<Notice> findAllActive();

  List<Notice> findAll();

  Notice findById(@Param("id") Long id);

  int insert(Notice notice);

  int update(Notice notice);

  int deleteById(@Param("id") Long id);
}
