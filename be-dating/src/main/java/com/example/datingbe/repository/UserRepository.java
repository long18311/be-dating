package com.example.datingbe.repository;

import com.example.datingbe.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface UserRepository extends JpaRepository<User,Long> {

//    @Query(value = "select u.* from user u",nativeQuery = true)
//    Page<User> findAll(Pageable pageable);

//    @Query(value = "select u.* from user u where u.username = ?1 and u.password = ?2 and actived = 1", nativeQuery = true)
//    Optional<User> findByUsernameAndPassword(String username, String password);

    @Query(value = "select u.* from user u where u.email = ?1 and u.activation_key = ?2 and actived = 1", nativeQuery = true)
    Optional<User> findByEmailAndUID(String email, String activation_key);

    @Query(value = "select u.* from user u where u.username = ?1 and u.actived = 1", nativeQuery = true)
    Optional<User> findByUsername(String username);
    @Query(value = "select u.* from user u where u.email = ?1", nativeQuery = true)
    Optional<User> findByEmail(String email);

//     @Query(value = "select u.* from user u where u.username = ?1", nativeQuery = true)
//     User findByUserName(String username);

//
//    @Query(value = "select u.* from user u where u.id = ?1", nativeQuery = true)
//    Optional<User> findById(Long id);
    @Query(value = "select * from user WHERE activation_key =?1", nativeQuery = true)
    Optional<User> getUserByActivationKey(String activationkey);

//    @Query(value = "select * from user WHERE remember_key =?1", nativeQuery = true)
//    Optional<User> getUserByRememberKey(String key);

    @Query(value = "select u.* from user u inner join user_authority a on a.user_id = u.id where a.authority_name != ?1",nativeQuery = true)
    Page<User> findUserNotAdmin(String role,Pageable pageable);
    List<User> findBySex(String sex);


    @Query("SELECT u FROM User u WHERE u.sex = :gender AND YEAR(CURRENT_DATE) - YEAR(u.birthday) BETWEEN :tuoiMin AND :tuoiMax")
    List<User> findUsersByGenderAndAgeRange(String gender, int tuoiMin, int tuoiMax);

}
