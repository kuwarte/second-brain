package com.kuwarte.api.profile;

import com.kuwarte.api.auth.User;
import com.kuwarte.api.auth.UserRepository;
import com.kuwarte.api.auth.exception.EmailAlreadyExistsException;
import com.kuwarte.api.profile.dto.UpdateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    @Transactional
    public User updateProfile(User user, UpdateProfileRequest request) {
        if (request.email() != null && !request.email().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.email())) {
                throw new EmailAlreadyExistsException(request.email());
            }
            user.setEmail(request.email());
        }
        return userRepository.save(user);
    }
}
