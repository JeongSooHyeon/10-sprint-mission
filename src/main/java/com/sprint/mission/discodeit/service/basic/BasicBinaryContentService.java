package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage storage;

  @Override
  @Transactional
  public BinaryContentDto create(BinaryContentCreateDto binaryContentCreateDto) {
    BinaryContent binaryContent = new BinaryContent(binaryContentCreateDto.contentType(),
        binaryContentCreateDto.size(),
        binaryContentCreateDto.fileName());
    binaryContentRepository.save(binaryContent);

    storage.put(binaryContent.getId(), binaryContentCreateDto.content());

    return binaryContentMapper.toBinaryContentDto(binaryContent);
  }

  @Override
  @Transactional(readOnly = true)
  public BinaryContentDto findById(UUID id) {
    return binaryContentMapper.toBinaryContentDto(binaryContentRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 파일이 없습니다.")));
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
    if (!binaryContentRepository.existsById(id)) {
      throw new IllegalArgumentException("해당 파일이 존재하지 않아 삭제할 수 없습니다. ID: " + id);
    }
    binaryContentRepository.deleteById(id);
  }
}
