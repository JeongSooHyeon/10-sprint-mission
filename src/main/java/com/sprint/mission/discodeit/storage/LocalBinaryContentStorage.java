package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.exception.storage.StorageFileSaveFailedException;
import com.sprint.mission.discodeit.exception.storage.StorageInitFailedException;
import com.sprint.mission.discodeit.exception.storage.StorageFileNotFoundException;
import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
@Slf4j
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  public LocalBinaryContentStorage(@Value("${discodeit.storage.root-path}") String rootPath) {
    this.root = Paths.get(rootPath);
  }

  @PostConstruct
  public void init() {
    try {
      Files.createDirectories(root);  // 파일 시스템에 디렉터리 생성
      log.info("로컬 파일 저장소 초기화 완료: path={}", root.toAbsolutePath());
    } catch (IOException e) {
      log.error("로컬 파일 저장소 초기화 실패: path={}", root.toAbsolutePath(), e);
      throw new StorageInitFailedException(root.toAbsolutePath().toString());
    }
  }

  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    log.debug("파일 저장 시작: id={}, size={}bytes", id, bytes.length);
    try {
      Files.write(resolvePath(id), bytes);
      log.debug("파일 저장 완료: id={}", id);
      return id;
    } catch (IOException e) {
      log.error("파일 저장 실패: id={}", id, e);
      throw new StorageFileSaveFailedException(id);
    }
  }

  @Override
  public InputStream get(UUID id) {
    log.debug("파일 읽기 시작: id={}", id);
    try {
      InputStream inputStream = new FileInputStream(resolvePath(id).toFile());
      log.debug("파일 읽기 완료: id={}", id);
      return inputStream;
    } catch (FileNotFoundException e) {
      log.warn("파일 읽기 실패 - 존재하지 않는 파일: id={}", id);
      throw new StorageFileNotFoundException(id);
    }
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
    log.info("파일 다운로드 시작: id={}, fileName={}", binaryContentDto.id(), binaryContentDto.fileName());
    InputStream inputStream = get(binaryContentDto.id());
    Resource resource = new InputStreamResource(inputStream);
    log.info("파일 다운로드 완료: id={}, fileName={}", binaryContentDto.id(), binaryContentDto.fileName());

    return ResponseEntity
        .status(HttpStatus.OK)
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + binaryContentDto.fileName() + "\"")
        .header(HttpHeaders.CONTENT_TYPE, binaryContentDto.contentType())
        .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(binaryContentDto.size()))
        .body(resource);
  }
}
