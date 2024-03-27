package org.example.repositoryDAO;

import java.util.List;
import java.util.Optional;

public interface DAO<Entity, Key> {
    Entity save(Entity entity);

    void update(Entity entity);

    boolean deleteById(Key id);

    Optional<Entity> findById(Key id);

    List<Entity> findAll();

    boolean existsById(Key id);
}
