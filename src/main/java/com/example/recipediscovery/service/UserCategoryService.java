package com.example.recipediscovery.service;

import com.example.recipediscovery.model.UserCategory;
import com.example.recipediscovery.repository.UserCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCategoryService {

    private final UserCategoryRepository repo;

    public UserCategoryService(UserCategoryRepository repo) {
        this.repo = repo;
    }

    public List<UserCategory> getByUser(Long userId) {
        return repo.findByUserId(userId);
    }

    public void create(Long userId, String name, String icon, String color) {
        UserCategory c = new UserCategory();
        c.setUserId(userId);
        c.setName(name);
        c.setIcon(icon);
        c.setColorCode(color);
        repo.save(c);
    }

    public UserCategory getByIdAndUser(Long userId, Long id) {
        return repo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public void update(Long userId, Long id, String name, String icon, String color) {
        UserCategory c = getByIdAndUser(userId, id);
        c.setName(name);
        c.setIcon(icon);
        c.setColorCode(color);
        repo.save(c);
    }

    public void delete(Long userId, Long id) {
        UserCategory c = getByIdAndUser(userId, id);
        repo.delete(c);
    }
}
