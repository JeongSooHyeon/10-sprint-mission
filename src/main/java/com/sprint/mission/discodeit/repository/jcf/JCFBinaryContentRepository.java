package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.util.*;

public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> data;

    public JCFBinaryContentRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        data.put((binaryContent.getId()), binaryContent);
        return  binaryContent;
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> idList) {
        return idList.stream()
                .map(data::get)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public void deleteByIds(List<UUID> idList) {
        idList.forEach(data::remove);

    }
}
