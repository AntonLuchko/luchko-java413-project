package com.example.demo.repositories;

import com.example.demo.entity.Notifications;
import com.example.demo.entity.Reviews;
import com.example.demo.entity.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications,Long> {
    List<Notifications> findAllByUserAndDeleetFalse(Users user);
    int countAllByUserAndDeleetFalse(Users user);
int countByUserAndActiveTrueAndDeleetFalse(Users user);

    @Modifying
    @Transactional
    @Query(value = "UPDATE notifications  SET active = 0 WHERE id = :id",nativeQuery = true)
    int noActive(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE  notifications  SET deleet =1  WHERE id = :id",nativeQuery = true)
    int delNotific(@Param("id") Long id);


    @Modifying
    @Transactional
    @Query(value = "DELETE  FROM notifications WHERE active=0",nativeQuery = true)
    int deleteNoActive();


}


