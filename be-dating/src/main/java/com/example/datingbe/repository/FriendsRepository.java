package com.example.datingbe.repository;

import com.example.datingbe.entity.Friend;
import com.example.datingbe.entity.User;
import com.example.datingbe.entity.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FriendsRepository extends JpaRepository<Friend, Integer> {

    // @Query("SELECT f FROM Friend f WHERE " +
    //         "((f.sender_user_id.id = :userId1 AND f.receiver_user_id.id = :userId2) OR " +
    //         "(f.sender_user_id.id = :userId2 AND f.receiver_user_id.id = :userId1)) AND " +
    //         "f.status = 'accepted'")
    // Optional<Friend> findAcceptedFriendship(@Param("userId1") long userId1, @Param("userId2") long userId2);

    boolean existsBySenderUserAndReceiverUserAndStatus(User senderUser, User receiverUser, FriendStatus status);
    // Other methods if needed
}