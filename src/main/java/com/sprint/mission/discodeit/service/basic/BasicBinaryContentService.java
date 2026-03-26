package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage storage;

  @Override
  @Transactional
  public BinaryContentDto create(BinaryContentCreateDto binaryContentCreateDto) {
    log.info("파일 업로드 시작: contentType={}, fileName={}, size={}",
        binaryContentCreateDto.contentType(),
        binaryContentCreateDto.fileName(),
        binaryContentCreateDto.size());

    BinaryContent binaryContent = new BinaryContent(binaryContentCreateDto.contentType(),
        binaryContentCreateDto.size(),
        binaryContentCreateDto.fileName());
    binaryContentRepository.save(binaryContent);
    storage.put(binaryContent.getId(), binaryContentCreateDto.content());

    log.info("파일 업로드 완료: id={}, fileName={}",
        binaryContent.getId(),
        binaryContent.getFileName());

    return binaryContentMapper.toBinaryContentDto(binaryContent);
  }

  @Override
  @Transactional(readOnly = true)
  public BinaryContentDto findById(UUID id) {
    return binaryContentMapper.toBinaryContentDto(binaryContentRepository.findById(id)
        .orElseThrow(() -> new BinaryContentNotFoundException(id)));
  }

  @Override
  @Transactional(readOnly = true)
  public List<BinaryContentDto> findAllById(List<UUID> idList) {
    return binaryContentRepository.findAllById(idList).stream()
        .map(binaryContentMapper::toBinaryContentDto)
        .toList();
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    log.info("파일 삭제 시작: id={}", id);
    if (!binaryContentRepository.existsById(id)) {
      log.warn("파일 삭제 실패 - 존재하지 않는 파일: id={}", id);
      throw new BinaryContentNotFoundException(id);
    }
    binaryContentRepository.deleteById(id);
    log.info("파일 삭제 완료: id={}", id);
  }
}
