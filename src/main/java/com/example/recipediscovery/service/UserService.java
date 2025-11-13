package com.example.recipediscovery.service;

import com.example.recipediscovery.model.User;
import com.example.recipediscovery.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Lấy danh sách user phân trang
    public Page<User> getUsers(int page) {
        return userRepository.findAllByOrderByCreatedAtDesc(
                PageRequest.of(page, 10)
        );
    }

    // Tạo user mới
    public User createUser(User u) {

        if (userRepository.existsByEmail(u.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        // 🔥 Lưu mật khẩu thẳng (không mã hóa)
        return userRepository.save(u);
    }

    // Lấy theo id
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
    }

    // Update user
    public User updateUser(Long id, User req) {

        User u = getById(id);

        u.setFullName(req.getFullName());
        u.setEmail(req.getEmail());
        u.setRole(req.getRole());

        // 🔥 Chỉ cập nhật password nếu admin nhập vào
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            u.setPassword(req.getPassword()); // không mã hoá
        }

        return userRepository.save(u);
    }

    // Xóa user
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
