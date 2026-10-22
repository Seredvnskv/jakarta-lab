package org.com.recipes.user.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.com.recipes.core.repository.Repository;
import org.com.recipes.user.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class UserRepository implements Repository<User, UUID> {

    private EntityManager em;

    protected UserRepository() {}

    @Inject
    public UserRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public User save(User user) {
        return em.merge(user);
    }

    @Override
    public void saveAll(Collection<User> e) {
        e.forEach(this::save);
    }

    @Override
    public void delete(User user) {
        em.remove(em.merge(user));
    }

    @Override
    public void deleteAll() {
        em.createQuery("DELETE FROM User").executeUpdate();
    }

    public byte[] findAvatar(User user) {
        return user.getAvatar();
    }

    public void updateAvatar(User user, byte[] avatar) {
        user.setAvatar(avatar);
        em.merge(user);
    }

    public void deleteAvatar(User user) {
        user.setAvatar(null);
        em.merge(user);
    }
}
