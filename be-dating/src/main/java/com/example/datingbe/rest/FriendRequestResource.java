package com.example.datingbe.rest;
import com.example.datingbe.entity.Friend;
import com.example.datingbe.entity.FriendStatus;
import com.example.datingbe.entity.Notification;
import com.example.datingbe.entity.User;
import com.example.datingbe.repository.FriendRequestRepository;
import com.example.datingbe.repository.UserRepository;
import com.example.datingbe.service.FriendRequestService;
import com.example.datingbe.service.NotificationService;
import com.example.datingbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/")
public class FriendRequestResource {
    @Autowired
    FriendRequestService friendRequestService;
    @Autowired
    FriendRequestRepository friendRequestRepositoryRepo;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    NotificationService notificationService;
    @Autowired
    private SimpMessagingTemplate template;


    @PostMapping("friend/addfriend/{userId}")
    public Friend sendFriendRequest(@PathVariable Long userId) {
        // lấy ra User gửi
        User senderUser = userService.getUserWithAuthority();
        // lấy ra User nhận
        User receiverUser = userService.getOne(userId);
        // Kiểm tra kết quả
        Friend result = friendRequestService.sendFriendRequest(senderUser, receiverUser);
        if (result != null) { // Nếu lời mời kết bạn được gửi thành công
            // Tạo ra nội dung thông báo
            String notificationContent = "Người dùng " + senderUser.getLastname() + senderUser.getFirstname() + " muốn kết bạn với bạn.";
            // Lưu thông báo vào bảng Notification
            Notification notification = new Notification();
            notification.setMessage(notificationContent);
            notification.setSender(senderUser);
            notification.setStatus(0);
            notification.setRecipient(receiverUser);
            notification.setTimestamp(new Timestamp(System.currentTimeMillis())); // Thiết lập thời gian hiện tại cho thông báo
            notificationService.saveNotification(notification);
            template.convertAndSend("/topic/notifications/" + receiverUser.getId(), notificationContent);
        }
        return result;
    }

    @GetMapping("friend/pendingrequests/{userId}")
    public List<User> getPendingFriendRequests(@PathVariable User userId) {
        // Gọi phương thức service để lấy danh sách người gửi yêu cầu kết bạn đang chờ duyệt
        List<User> pendingRequests = friendRequestService.findPendingFriendRequestsForUser(userId);
        return pendingRequests;
    }

    @PostMapping("friend/acceptfriend/{userId}")
    public ResponseEntity<String> acceptFriendRequest(@PathVariable Long userId) {
        User senderUser = userService.getOne(userId);
        User receiverUser = userService.getUserWithAuthority();
        friendRequestService.acceptFriendRequest(senderUser, receiverUser);
        // Tạo thông báo cho người gửi lời mời
        String notificationContent = "Người dùng " + receiverUser.getLastname() + " " + receiverUser.getFirstname() + " đã chấp nhận lời mời kết bạn của bạn.";
        Notification notification = new Notification();
        notification.setMessage(notificationContent);
        notification.setStatus(0);
        notification.setSender(receiverUser); // Đặt người gửi là người chấp nhận lời mời
        notification.setRecipient(senderUser); // Đặt người nhận là người đã gửi lời mời
        notification.setTimestamp(new Timestamp(System.currentTimeMillis()));
        notificationService.saveNotification(notification);
        return ResponseEntity.ok("Friend request accepted.");
    }

    @GetMapping("friend/friends/{userId}")
    public ResponseEntity<List<User>> getFriendsForUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null); // Lấy thông tin User bằng ID
        if (user == null) {
            // Xử lý nếu không tìm thấy User với ID tương ứng
            // Ví dụ: Trả về lỗi 404 Not Found
            return ResponseEntity.notFound().build();
        }
        List<User> friends = friendRequestService.findFriendsForUser(user);
        return ResponseEntity.ok(friends);
    }

    @DeleteMapping("friend/unfriend")
    public ResponseEntity<String> unfriend(@RequestParam Long user1Id, @RequestParam Long user2Id) {
        // Gọi phương thức service để thực hiện việc hủy kết bạn giữa hai người dùng
        friendRequestService.unfriend(user1Id, user2Id);
        return ResponseEntity.ok("Unfriended successfully.");
    }

    @DeleteMapping("friend/reject")
    public ResponseEntity<String> rejectFriendRequest(@RequestParam Long senderUserId, @RequestParam Long receiverUserId) {
        // Gọi phương thức service để từ chối lời mời kết bạn giữa hai người dùng
        friendRequestService.rejectFriendRequest(senderUserId, receiverUserId);
        return ResponseEntity.ok("Friend request rejected successfully.");
    }

    @GetMapping("/status")
    public ResponseEntity<FriendStatus> getFriendStatus(@RequestParam("senderId") Long senderId, @RequestParam("receiverId") Long receiverId) {
        FriendStatus status = friendRequestRepositoryRepo.findFriendStatus(senderId, receiverId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
    @GetMapping("/pending/{senderUserId}")
    public List<User> getPendingFriendRequests(@PathVariable Long senderUserId) {
        return friendRequestRepositoryRepo.findUsersWithPendingFriendRequestsBySenderUserId(senderUserId);
    }
}