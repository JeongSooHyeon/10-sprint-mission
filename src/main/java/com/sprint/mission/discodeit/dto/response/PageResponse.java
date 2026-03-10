package com.sprint.mission.discodeit.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class PageResponse<T> {

  List<T> content;
  Object nextCursor;
  int size;
  boolean hasNext;
  Long totalElements;
}
