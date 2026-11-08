package org.com.recipes.user.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.com.recipes.core.repository.Repository;
import org.com.recipes.user.entity.User;
import org.com.recipes.user.entity.id.UserId;

import java.util.Collection;
import java.util.Optional;

@ApplicationScoped
@Transactional
@NoArgsConstructor
public class UserRepository implements Repository<User, UserId> {

    private EntityManager entityManager;

    @Inject
    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<User> findById(UserId id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public Collection<User> findAll() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    public User save(User entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void delete(User entity) {
        entityManager.remove(entity);
    }
}
