package com.sprint.mission.discodeit.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    T save(T entity);
    Optional<T> findById(ID id);
    boolean deleteById(ID id);
    List<T> findAll();
//    boolean existsById(ID id); // TODO: 추후 리펙토리, 또는 사용이 빈번할때 추가예정

}
