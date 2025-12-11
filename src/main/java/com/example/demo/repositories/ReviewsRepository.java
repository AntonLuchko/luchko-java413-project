package com.example.demo.repositories;

import com.example.demo.entity.Reviews;
import com.example.demo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import jakarta.transaction.Transactional;
import java.util.List;

@Repository
public interface ReviewsRepository extends JpaRepository<Reviews,Long> {

    List<Reviews> findAllByUserAndStatusTrueAndActiveTrue(Users user,Pageable pageable);
    List<Reviews> findAllByStatusTrueAndActiveTrue(Pageable pageable);
    List<Reviews> findAllByStatusFalseAndActiveTrue(Pageable pageable);
    int countByUserAndStatusTrueAndActiveTrue(Users user);
    int countByStatusTrueAndActiveTrue();
    int countByStatusFalse();


    @Modifying
    @Transactional
    @Query(value = "UPDATE reviews r SET r.status = true WHERE r.id = :id",nativeQuery = true)
    int goodReview(@Param("id") Long id);
}
