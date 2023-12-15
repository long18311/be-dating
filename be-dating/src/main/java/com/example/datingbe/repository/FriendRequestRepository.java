package com.example.datingbe.repository;

import com.example.datingbe.entity.Friend;
import com.example.datingbe.entity.FriendStatus;
import com.example.datingbe.entity.Images;
import com.example.datingbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Repository
public interface FriendRequestRepository extends JpaRepository<Friend, Integer> {
    @Transactional
    default Friend sendFriendRequest(User senderUser, User receiverUser) {
        Friend friend = new Friend();
        friend.setSender_user_id(senderUser);
        friend.setReceiver_user_id(receiverUser);
        friend.setStatus(FriendStatus.pending);
        friend.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        return save(friend);
    }
    @Transactional
    @Query("SELECT u FROM Friend f " +
            "JOIN f.sender_user_id u " +
            "WHERE f.receiver_user_id = :user AND f.status = 'PENDING'")
    List<User> findPendingFriendRequestsForUser(@Param("user") User user);


    @Modifying
    @Transactional
    @Query("UPDATE Friend SET status = 'ACCEPTED', createdAt = CURRENT_TIMESTAMP " +
            "WHERE sender_user_id = :senderUser AND receiver_user_id = :receiverUser")
    void acceptFriendRequest(@Param("senderUser") User senderUser, @Param("receiverUser") User receiverUser);


    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN Friend f ON (u.id = f.sender_user_id.id OR u.id = f.receiver_user_id.id) " +
            "WHERE (f.sender_user_id = :user OR f.receiver_user_id = :user) " +
            "AND f.status = 'ACCEPTED' AND u <> :user")
    List<User> findFriendsForUser(@Param("user") User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM Friend f " +
            "WHERE (f.sender_user_id.id = :user1Id AND f.receiver_user_id.id = :user2Id) " +
            "OR (f.sender_user_id.id = :user2Id AND f.receiver_user_id.id = :user1Id)")
    void unfriend(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Friend f " +
            "WHERE f.sender_user_id.id = :senderUserId " +
            "AND f.receiver_user_id.id = :receiverUserId " +
            "AND f.status = 'PENDING'")
    void rejectFriendRequest(@Param("senderUserId") Long senderUserId, @Param("receiverUserId") Long receiverUserId);



    @Query("SELECT f.status FROM Friend f " +
            "WHERE (f.sender_user_id.id = :user1Id AND f.receiver_user_id.id = :user2Id) " +
            "OR (f.sender_user_id.id = :user2Id AND f.receiver_user_id.id = :user1Id)")
    FriendStatus findFriendStatus(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);

    @Query("SELECT f.receiver_user_id FROM Friend f WHERE f.sender_user_id.id = :senderUserId AND f.status = 'pending'")
    List<User> findUsersWithPendingFriendRequestsBySenderUserId(@Param("senderUserId") Long senderUserId);
    @Query(value ="SELECT i FROM Friend i WHERE (i.sender_user_id.id = :userId OR i.receiver_user_id.id = :userId) AND i.status = 'PENDING'")
    Set<Friend> findByUsers(@Param("userId") Long userId);



}