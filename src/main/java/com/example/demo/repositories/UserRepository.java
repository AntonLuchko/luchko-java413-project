package com.example.demo.repositories;

import com.example.demo.entity.Users;
import com.example.demo.enums.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<Users, Long> {

    boolean existsByEmail(String email);
    Optional<Users> findByEmail(String email);
    List<Users> findAllByIsBlockedTrueAndRole(Role role, Pageable pageable);
    List<Users> findAllByIsBlockedFalseAndRole(Role role,Pageable pageable);
    List<Users> findAllByLockedTrueAndRole(Role role,Pageable pageable);
    List<Users> findAllByRole(Role role, Pageable pageable);
    int countAllByRole(Role role);
    int countAllByIsBlockedTrueAndRole(Role role);
    int countAllByIsBlockedFalseAndRole(Role role);
    int countAllByLockedTrueAndRole(Role role);
    List<Users> findAllByIdAndRole(Long id, Role role);
    List<Users> findAllByUsernameContainingIgnoreCaseAndRole(String name, Role role, Pageable pageable);
    List<Users> findAllByEmailContainingIgnoreCaseAndRole(String email, Role role, Pageable pageable);
int countAllByUsernameContainingIgnoreCaseAndRole(String username,Role role);
int countAllByEmailContainingIgnoreCaseAndRole(String email,Role role);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET is_blocked = 1, why_blocked=:reason  WHERE email = :email", nativeQuery = true)
    int blockUser(@Param("email") String email, @Param("reason") String reason);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET is_blocked = 0  WHERE email = :email", nativeQuery = true)
    int unblockUser(String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET locked = 0, lock_time=NULL  WHERE email = :email", nativeQuery = true)
    int unblockLockedUser(String email);


    @Modifying
    @Transactional
    @Query(value = "UPDATE Users  SET locked = true, lock_time = :lockTime WHERE email = :email",nativeQuery = true)
    int lockUser(@Param("email") String email, @Param("lockTime") LocalDateTime lockTime);

}
