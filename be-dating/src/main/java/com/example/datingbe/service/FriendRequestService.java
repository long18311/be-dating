package com.example.datingbe.service;
import com.example.datingbe.entity.Friend;
import com.example.datingbe.entity.User;
import com.example.datingbe.repository.FriendRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class FriendRequestService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;
    public Friend sendFriendRequest(User senderUser, User receiverUser) {
        return friendRequestRepository.sendFriendRequest(senderUser, receiverUser);
    }

    public List<User> findPendingFriendRequestsForUser(User user){
        return friendRequestRepository.findPendingFriendRequestsForUser(user);
    }

    public void acceptFriendRequest(User senderUser, User receiverUser){
         friendRequestRepository.acceptFriendRequest(senderUser,receiverUser);
    }

    public List<User> findFriendsForUser(User user) {
        return friendRequestRepository.findFriendsForUser(user);
    }

    @Modifying
    @Transactional
    public void unfriend(Long user1Id, Long user2Id) {
        friendRequestRepository.unfriend(user1Id, user2Id);
    }

    @Modifying
    @Transactional
    public void rejectFriendRequest(Long senderUserId, Long receiverUserId) {
        friendRequestRepository.rejectFriendRequest(senderUserId, receiverUserId);
    }


}
