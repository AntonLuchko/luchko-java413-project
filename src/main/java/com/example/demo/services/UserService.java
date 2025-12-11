package com.example.demo.services;

import com.example.demo.dto.*;
import com.example.demo.entity.Notifications;
import com.example.demo.entity.Reviews;
import com.example.demo.entity.Users;

import com.example.demo.exception.EmailException;
import com.example.demo.repositories.NotificationRepository;
import com.example.demo.repositories.ReviewsRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.utils.ConverterReview;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;


@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ReviewsRepository reviewsRepository;
    private  final ConverterReview converterReview;
    private final NotificationRepository notificationRepository;
    private final int pageSize=10;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService, AuthenticationManager authenticationManager, ReviewsRepository reviewsRepository, ConverterReview converterReview, NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.reviewsRepository = reviewsRepository;
        this.converterReview = converterReview;
        this.notificationRepository = notificationRepository;
    }

    public AuthResponse register(RegisterRequest request) {

            if (userRepository.existsByEmail(request.getEmail())) {
                throw new EmailException("Email already exists");
            }

            Users user = new Users();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            userRepository.save(user);
            String token =jwtService.generateToken(user);
            return new AuthResponse(token);


    }
    public AuthResponse authenticate(AuthRequest request) {

        // 1️⃣ Проверка существования пользователя
        Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        // 3️⃣ Проверка блокировки
        if (user.getIsBlocked()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is blocked by administrator");
        }

        if (!user.isAccountNonLocked()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User account is locked");
        } else {if(user.isLocked()==true&&user.getLockTime()!=null){
            user.setLockTime(null);
            user.setLocked(false);
            userRepository.save(user);
        }}

        // 2️⃣ Проверка пароля через AuthenticationManager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );



        // 4️⃣ Генерация JWT
        String token = jwtService.generateToken(user);

        return new AuthResponse(token,"ROLE_" + user.getRole().name(), user.getEmail());
    }

public int lockedUser(String email){
        return userRepository.lockUser(email, LocalDateTime.now(ZoneOffset.UTC));
}


    public  int saveReview(TextReview textReview, UserDetails userDetails) {
        Users user=userRepository.findByEmail(userDetails.getUsername())
                 .orElseThrow(() -> new RuntimeException("User not found"));

        Reviews reviews = new Reviews();
        reviews.setText(textReview.getText());
        reviews.setUser(user);
        reviewsRepository.save(reviews);
        return 1;
    }


    public List<GetReview> myReviews(UserDetails userDetails,int page) {
        Users user=userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Pageable pageable = PageRequest.of(page,pageSize);
        return reviewsRepository.findAllByUserAndStatusTrueAndActiveTrue(user,pageable).stream().map(x->converterReview.convert(x)).toList();
    }

    public List<GetReview> allReviews(int page) {
        Pageable pageable = PageRequest.of(page,pageSize);
        return reviewsRepository.findAllByStatusTrueAndActiveTrue(pageable).stream().map(x->converterReview.convert(x)).toList();
    }

    public int countMyReview(UserDetails userDetails) {
        Users user=userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return reviewsRepository.countByUserAndStatusTrueAndActiveTrue(user);
    }

    public int countAllReview() {
        return reviewsRepository.countByStatusTrueAndActiveTrue();
    }


    @Async
    public void notifyUser(Users user, String message) {
        Notifications notification = new Notifications(message, user, true);
        notificationRepository.save(notification);

        // Дополнительно можно отправлять email, push и т.д.
        System.out.println("Notification sent to " + user.getEmail() + ": " + message);
    }
}


