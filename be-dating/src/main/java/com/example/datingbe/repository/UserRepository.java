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


public interface UserRepository extends JpaRepository<User, Long> {
    long count();
    // @Query(value = "select u.* from user u",nativeQuery = true)
    // Page<User> findAll(Pageable pageable);

    // @Query(value = "select u.* from user u where u.username = ?1 and u.password =
    // ?2 and actived = 1", nativeQuery = true)
    // Optional<User> findByUsernameAndPassword(String username, String password);

    @Query(value = "select u.* from user u where u.email = ?1 and u.activation_key = ?2 and actived = 1", nativeQuery = true)
    Optional<User> findByEmailAndUID(String email, String activation_key);

    @Query(value = "select u.* from user u where u.username = ?1 and u.actived = 1", nativeQuery = true)
    Optional<User> findByUsername(String username);

    @Query(value = "select u.* from user u where u.email = ?1", nativeQuery = true)
    Optional<User> findByEmail(String email);

    // @Query(value = "select u.* from user u where u.username = ?1", nativeQuery =
    // true)
    // User findByUserName(String username);

    //
    // @Query(value = "select u.* from user u where u.id = ?1", nativeQuery = true)
    // Optional<User> findById(Long id);
    @Query(value = "select * from user WHERE activation_key =?1", nativeQuery = true)
    Optional<User> getUserByActivationKey(String activationkey);

    // @Query(value = "select * from user WHERE remember_key =?1", nativeQuery =
    // true)
    // Optional<User> getUserByRememberKey(String key);

    @Query(value = "select u.* from user u inner join user_authority a on a.user_id = u.id where a.authority_name != ?1", nativeQuery = true)
    Page<User> findUserNotAdmin(String role, Pageable pageable);

    List<User> findBySex(String sex);

    @Query("SELECT u FROM User u WHERE u.sex = :gender AND YEAR(CURRENT_DATE) - YEAR(u.birthday) BETWEEN :ageMin AND :ageMax")
    List<User> findUsersByGenderAndAgeRange(String gender, int ageMin, int ageMax);

    @Query("SELECT u FROM User u WHERE u.sex = :gender AND YEAR(CURRENT_DATE) - YEAR(u.birthday) BETWEEN :ageMin AND :ageMax AND "
            +
            "u.height BETWEEN :heightMin AND :heightMax AND " +
            "u.weight BETWEEN :weightMin AND :weightMax AND " +
            "(COALESCE(:city, '') = '' OR u.city = :city) AND " +
            "(COALESCE(:ward, '') = '' OR u.ward = :ward) AND " +
            "(COALESCE(:district, '') = '' OR u.district = :district)")
    List<User> findUsersByGenderAgeHeightWeightCityWardDistrict(
            String gender,
            int ageMin,
            int ageMax,
            @Param("heightMin") float heightMin,
            @Param("heightMax") float heightMax,
            @Param("weightMin") float weightMin,
            @Param("weightMax") float weightMax,
            @Param("city") String city,
            @Param("ward") String ward,
            @Param("district") String district);

}