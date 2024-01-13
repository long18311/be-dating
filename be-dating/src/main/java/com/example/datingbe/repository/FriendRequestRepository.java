package com.example.datingbe.repository;

import com.example.datingbe.entity.Friend;
import com.example.datingbe.entity.FriendStatus;
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
        friend.setSenderUser(senderUser);
        friend.setReceiverUser(receiverUser);
        friend.setStatus(FriendStatus.pending);
        friend.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        return save(friend);
    }

    @Transactional
    @Query("SELECT f.senderUser FROM Friend f " +
            "WHERE f.receiverUser = :user AND f.status = 'PENDING'")
    List<User> findPendingFriendRequestsForUser(@Param("user") User user);

    @Modifying
    @Transactional
    @Query("UPDATE Friend SET status = 'ACCEPTED', createdAt = CURRENT_TIMESTAMP " +
            "WHERE senderUser = :senderUser AND receiverUser = :receiverUser")
    void acceptFriendRequest(@Param("senderUser") User senderUser, @Param("receiverUser") User receiverUser);

    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN Friend f ON (u.id = f.senderUser.id OR u.id = f.receiverUser.id) " +
            "WHERE (f.senderUser = :user OR f.receiverUser = :user) " +
            "AND f.status = 'ACCEPTED' AND u <> :user")
    List<User> findFriendsForUser(@Param("user") User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM Friend f " +
            "WHERE (f.senderUser.id = :user1Id AND f.receiverUser.id = :user2Id) " +
            "OR (f.senderUser.id = :user2Id AND f.receiverUser.id = :user1Id)")
    void unfriend(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Friend f " +
            "WHERE f.senderUser.id = :senderUserId " +
            "AND f.receiverUser.id = :receiverUserId " +
            "AND f.status = 'PENDING'")
    void rejectFriendRequest(@Param("senderUserId") Long senderUserId, @Param("receiverUserId") Long receiverUserId);

    @Query("SELECT f.status FROM Friend f " +
            "WHERE (f.senderUser.id = :user1Id AND f.receiverUser.id = :user2Id) " +
            "OR (f.senderUser.id = :user2Id AND f.receiverUser.id = :user1Id)")
    FriendStatus findFriendStatus(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);

    @Query("SELECT f.receiverUser FROM Friend f WHERE f.senderUser.id = :senderUserId AND f.status = 'PENDING'")
    List<User> findUsersWithPendingFriendRequestsBySenderUserId(@Param("senderUserId") Long senderUserId);

    @Query(value ="SELECT f FROM Friend f WHERE (f.senderUser.id = :userId OR f.receiverUser.id = :userId) AND f.status = 'PENDING'")
    Set<Friend> findByUsers(@Param("userId") Long userId);

    @Query("SELECT COUNT(f) > 0 FROM Friend f WHERE f.senderUser = :senderUser AND f.receiverUser = :receiverUser AND f.status = :status")
    boolean existsBySenderUserAndReceiverUserAndStatus(@Param("senderUser") User senderUser, @Param("receiverUser") User receiverUser, @Param("status") FriendStatus status);


}