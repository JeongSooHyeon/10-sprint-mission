package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;


@Mapper(componentModel = "spring")
public interface PageResponseMapper {

  default <T> PageResponse<T> fromSlice(Slice<T> slice) {
    return PageResponse.<T>builder()
        .content(slice.getContent())
        .nextCursor(slice.getNumber())
        .size(slice.getSize())
        .hasNext(slice.hasNext())
        .build();
  }

  default <T> PageResponse<T> fromPage(Page<T> page) {
    return PageResponse.<T>builder()
        .content(page.getContent())
        .nextCursor(page.getNumber())
        .size(page.getSize())
        .hasNext(page.hasNext())
        .totalElements(page.getTotalElements())
        .build();
  }
}