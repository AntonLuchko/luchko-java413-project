package com.example.demo.services;

import com.example.demo.dto.*;
import com.example.demo.entity.Reviews;
import com.example.demo.entity.Users;
import com.example.demo.enums.Role;
import com.example.demo.repositories.ReviewsRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.utils.ConverterDTO;
import com.example.demo.utils.ConverterReview;
import com.example.demo.utils.ConverterReviewAdmin;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDate;
import java.util.List;

@Service
public class AdminService {
 private final UserRepository userRepository;
 private final ConverterDTO converterDTO;
 private final ReviewsRepository reviewsRepository;
 private final ConverterReviewAdmin converterReviewAdmin;
private final int pageSize=10;
    private final UserService userService;

    public AdminService(UserRepository userRepository, ConverterDTO converterDTO, ReviewsRepository reviewsRepository, ConverterReviewAdmin converterReviewAdmin, UserService userService) {
        this.userRepository = userRepository;
        this.converterDTO = converterDTO;
        this.reviewsRepository = reviewsRepository;
       this.converterReviewAdmin = converterReviewAdmin;
        this.userService = userService;
    }

    public List<UsersDTO> allUsers(int page) {
        Pageable pageable = PageRequest.of(page,pageSize);
        return userRepository.findAllByRole(Role.USER,pageable).stream()
                .map(x->converterDTO.convert(x))
                .toList();
    }

    public List<UsersDTO> allBlockedUsers(int page) {
        Pageable pageable = PageRequest.of(page,pageSize);
        return userRepository.findAllByIsBlockedTrueAndRole(Role.USER,pageable).stream()
                .map(x->converterDTO.convert(x))
                .toList();
    }

    public List<UsersDTO> allUnBlockedUsers(int page) {
        Pageable pageable = PageRequest.of(page,pageSize);
        return userRepository.findAllByIsBlockedFalseAndRole(Role.USER,pageable).stream()
                .map(x->converterDTO.convert(x))
                .toList();
    }

    public List<UsersDTO> allLockedUsers(int page) {
        Pageable pageable = PageRequest.of(page,pageSize);
        return userRepository.findAllByLockedTrueAndRole(Role.USER,pageable).stream()
                .map(x->converterDTO.convert(x))
                .toList();
    }


    public int countAll(){
        return userRepository.countAllByRole(Role.USER);
    }

    public int countAllBlocked(){
        return userRepository.countAllByIsBlockedTrueAndRole(Role.USER);
    }

    public int countAllUnBlocked(){
        return userRepository.countAllByIsBlockedFalseAndRole(Role.USER);
    }

    public int countAllLocked(){
        return userRepository.countAllByLockedTrueAndRole(Role.USER);
    }

 public int blockUser(List<EmailBlocked> list){
        int count=0;
        for (EmailBlocked emailBlocked : list) {
            count+= userRepository.blockUser(emailBlocked.getEmail(), emailBlocked.getResone());
        }
        return count;
 }

 public int unBlockUser(List<EmailUnBlocked> list){
     int count=0;
     for (EmailUnBlocked emailUnBlocked : list) {
         count+= userRepository.unblockUser(emailUnBlocked.getEmail());
     }
     return count;
 }

 public int unblockLockedUser(List<EmailUnBlocked> list){
     int count=0;
     for (EmailUnBlocked emailUnBlocked : list) {
         count+= userRepository.unblockLockedUser(emailUnBlocked.getEmail());
     }
     return count;
 }
 public List<UsersDTO> find(int page,String param,String value){
        Pageable pageable = PageRequest.of(page,pageSize);
        if(param.equalsIgnoreCase("id")){
            Long id=Long.parseLong(value);
        return userRepository.findAllByIdAndRole(id,Role.USER).stream()
                .map(x->converterDTO.convert(x))
                .toList();
        }
     if(param.equalsIgnoreCase("name")){
         return userRepository.findAllByUsernameContainingIgnoreCaseAndRole(value,Role.USER,pageable).stream()
                 .map(x->converterDTO.convert(x))
                 .toList();
     }
     return userRepository.findAllByEmailContainingIgnoreCaseAndRole(value,Role.USER,pageable).stream()
             .map(x->converterDTO.convert(x))
             .toList();
    }

    public int countFind(String param,String value){
        if(param.equalsIgnoreCase("name")){
            return userRepository.countAllByUsernameContainingIgnoreCaseAndRole(value,Role.USER);
        }
        if(param.equalsIgnoreCase("email")){
            return userRepository.countAllByEmailContainingIgnoreCaseAndRole(value,Role.USER);
        }else{
            Long id=Long.parseLong(value);
            if(userRepository.findById(id).isPresent()) {
                return 1;
            }
        return 0;
    }}

    public List<ReviewAdminDto> filterReviews(int page){
        Pageable pageable = PageRequest.of(page,pageSize);
        return reviewsRepository.findAllByStatusFalseAndActiveTrue(pageable).stream().map(x->converterReviewAdmin.convert(x)).toList();
    }

    public int countReviewsByFilter(){
        return reviewsRepository.countByStatusFalse();
    }

//    @Transactional
//    public int goodReview(List<Long>listId){
//        int count=0;
//        for(long id:listId){
//reviewsRepository.goodReview(id);
//count+=1;
//        }
//        return count;
//    }

    @Transactional
    public int approveReviews(List<Long> reviewIds) {
        int count = 0;

        // Загружаем все отзывы одним запросом
        List<Reviews> reviews = reviewsRepository.findAllById(reviewIds);

        for (Reviews review : reviews) {
            review.setStatus(true); // меняем статус
            count++;

            // Создаём финальные переменные для использования внутри анонимного класса
            final Users user = review.getUser();
            final String reviewText = review.getText();
            final LocalDate reviewDate = review.getDate();

            // Отложенное уведомление после успешного коммита
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            userService.notifyUser(
                                    user,
                                    "Ваш отзыв: \"" + reviewText + "\" от " + reviewDate + " одобрен!"
                            );
                        }
                    }
            );
        }

        // saveAll не нужен, JPA автоматически сохранит изменения в рамках @Transactional
        return count;
    }
}
