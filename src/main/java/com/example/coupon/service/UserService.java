package com.example.coupon.service;

import com.example.coupon.domain.user.CreateUserRequest;
import com.example.coupon.domain.user.User;
import com.example.coupon.domain.user.UserResponse;
import com.example.coupon.domain.user.UserUpdateRequest;
import com.example.coupon.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Transactional
    public UserResponse register(CreateUserRequest request) {
        String email = request.getEmail();
        checkDuplicatedEmail(email);

        String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
        User user = User.builder()
                .email(email)
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .password(encodedPassword)
                .build();

        User savedUser = userRepository.save(user);

        return UserResponse.from(savedUser);

    }

    public Page<UserResponse> findAll(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);
        return page.map(UserResponse::from);
    }

    public UserResponse findUserBy(String email) {
        User findUser = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("해당 유저가 존재하지 않습니다.")
        );

        return UserResponse.from(findUser);

    }

    private void checkDuplicatedEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EntityExistsException("이미 존재하는 이메일입니다. 다른 이메일로 시도해주세요.");
        }
    }

    @Transactional
    public UserResponse updateUserBy(Long userId, UserUpdateRequest request) {
        User findUser = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        /**
         * 유지보수 측면에서 굉장히 안좋아보임.
         */
        Optional.ofNullable(request.getEmail()).ifPresent(findUser::changeEmail);
        Optional.ofNullable(request.getPhoneNumber()).ifPresent(findUser::changePhoneNumber);
        Optional.ofNullable(request.getName()).ifPresent(findUser::changeUsername);

        return UserResponse.from(findUser);

    }

    @Transactional
    public UserResponse deactivateUserBy(Long userId) {

        User findUser = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        findUser.deactivate();

        return UserResponse.from(findUser);
    }

    public UserResponse activateUserBy(Long userId) {
        User findUser = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        findUser.activate();

        return UserResponse.from(findUser);
    }
}
