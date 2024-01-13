package com.example.datingbe.rest;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.datingbe.dto.UserProfileDto;
import com.example.datingbe.entity.*;
import com.example.datingbe.repository.UserRepository;
import com.example.datingbe.service.UserProfileServce;
import com.example.datingbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/userProfile")
public class UserProfileController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileServce userProfileServce;
    @Autowired
    private Cloudinary cloudinary;
    @GetMapping()
    public ResponseEntity<UserProfileDto> getUserProfile(){
        User user = userService.getUserWithAuthority();
//        User rec = userRepository.getOne(userId);
        return ResponseEntity.ok(userProfileServce.getUserProfile(user.getId()));
    }
    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserProfileDto> findProfileUser(@PathVariable Long userId){
        if (userService.getOne(userId)!=null){
            return ResponseEntity.ok(userProfileServce.getUserProfile(userId));
        }
        return ResponseEntity.notFound().build();

    }
    @GetMapping("/informationFields")
    public ResponseEntity<List<InformationField>> getInformationFields(){
//        User sen = userService.getUserWithAuthority();
//        User rec = userRepository.getOne(userId);
        return ResponseEntity.ok(userProfileServce.getInformationFields());
    }
    @PostMapping("/register-profile")
    public ResponseEntity<String> registerProfile(
            @RequestParam("firstname") String firstname,
            @RequestParam("lastname") String lastname,
            @RequestPart("avatar") MultipartFile avatar,
            @RequestPart("images") List<MultipartFile> images,
            @RequestParam("informationOptions") List<Long> informationOptions,
            @RequestParam("cover") MultipartFile cover,
            @RequestParam("birthdate") String birthdate,
            @RequestParam("sex") String sex,
            @RequestParam("maritalstatus") String maritalstatus,
            @RequestParam("about") String about,
            @RequestParam("id_user") String id,
            @RequestParam("city") String city,
            @RequestParam("ward") String ward,
            @RequestParam("district") String district,
            @RequestParam("nickname") String nickname,
            @RequestParam("height") float height,
            @RequestParam("weight") float weight,
            @RequestParam("latitude") String latitude,
            @RequestParam("longitude") String longitude,
            @RequestParam("verify") String verify

    ) {
        try {

            User user = userService.getOne(Long.parseLong(id));

            // avt user
            Map<String, String> avatarUploadResult = cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.emptyMap());
            String avatarPublicId = avatarUploadResult.get("public_id");
            String avatarUrl = cloudinary.url().generate(avatarPublicId);
            // cover user
            Map<String, String> coverUploadResult = cloudinary.uploader().upload(cover.getBytes(), ObjectUtils.emptyMap());
            String coverPublicId = coverUploadResult.get("public_id");
            String coverUrl = cloudinary.url().generate(coverPublicId);
            user.setLastname(lastname);
            user.setFirstname(firstname);
            user.setAbout(about);
            user.setSex(sex);
            user.setDistrict(district);
            user.setCity(city);
            user.setWard(ward);
            user.setHeight(height);
            user.setWeight(weight);
            user.setLatitude(Double.parseDouble(latitude));
            user.setLongitude(Double.parseDouble(longitude));
            user.setVerify(verify);
            user.setNickname(nickname);
            user.setMaritalstatus(maritalstatus);
            String dateString = birthdate;
            // Định dạng của chuỗi ngày
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                // Chuyển đổi chuỗi thành kiểu Date
                Date date = dateFormat.parse(dateString);
                user.setBirthday(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            user.setCover(coverUrl);
            user.setAvatar(avatarUrl);

            List<Images> imageUrls = new ArrayList<>();
            for (MultipartFile photo : images) {
                Map uploadResult = cloudinary.uploader().upload(photo.getBytes(), ObjectUtils.emptyMap());
                String imageUrl = (String) uploadResult.get("url");
                imageUrls.add(new Images(imageUrl));
            }
            for (Images o: imageUrls  ) {
                o.setUser(user);
            }
            user.setImages(imageUrls);
            Set<InformationOption> informationOptionsUser = userProfileServce.getinformationOptions(informationOptions).stream().collect(Collectors.toSet());

            user.setInformationOptions(informationOptionsUser);

            userRepository.save(user);

            return new ResponseEntity<>("Hồ sơ của bạn đã được cập nhật", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Lỗi :"+ e.getMessage());
            return new ResponseEntity<>("Lỗi cập nhật hồ sơ , vui lòng liên hệ admin", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping(value ="/updateUserProfile")
    public ResponseEntity<UserProfileDto> updateUserProfile(@RequestBody UserProfileDto userProfile){
        User user = userService.getUserWithAuthority();

        return ResponseEntity.ok(userProfileServce.updateUserProfile(user.getId(), userProfile));

    }
    @PutMapping(value ="/updateUserAvatar")
    public ResponseEntity<String> updateUserAvatar(@RequestParam("file") MultipartFile file){
        try {
            User user = userService.getUserWithAuthority();
            Map<String, String> avatarUploadResult = null;
            avatarUploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String avatarPublicId = avatarUploadResult.get("public_id");
            String avatarUrl = cloudinary.url().generate(avatarPublicId);
            user.setAvatar(avatarUrl);
            user = userRepository.save(user);
            return ResponseEntity.ok(avatarUrl);
        } catch (IOException e) {
            System.out.println("Lỗi :"+ e.getMessage());
            return new ResponseEntity<>("Lỗi cập nhật hồ sơ , vui lòng liên hệ admin", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping(value ="/updateUserCover")
    public ResponseEntity<String> updateUserCover(@RequestParam("file") MultipartFile file){
        try {
            User user = userService.getUserWithAuthority();
            Map<String, String> avatarUploadResult = null;
            avatarUploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String avatarPublicId = avatarUploadResult.get("public_id");
            String coverUrl = cloudinary.url().generate(avatarPublicId);
            user.setCover(coverUrl);
            user = userRepository.save(user);
            return ResponseEntity.ok(coverUrl);
        } catch (IOException e) {
            System.out.println("Lỗi :"+ e.getMessage());
            return new ResponseEntity<>("Lỗi cập nhật hồ sơ , vui lòng liên hệ admin", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PostMapping(value ="/createUserImage")
    public ResponseEntity<?> createUserImage(@RequestParam("file") MultipartFile file){
        try {
            User user = userService.getUserWithAuthority();
            Map<String, String> avatarUploadResult = null;
            avatarUploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String avatarPublicId = avatarUploadResult.get("public_id");
            String image = cloudinary.url().generate(avatarPublicId);
            user = userProfileServce.saveImage(user, image);
            return ResponseEntity.ok(user.getImages());
        } catch (IOException e) {
            System.out.println("Lỗi :"+ e.getMessage());
            return new ResponseEntity<>("Lỗi cập nhật hồ sơ , vui lòng liên hệ admin", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
//    @DeleteMapping(value ="/deleteUserImage/{imgId}")
//    public ResponseEntity<?> createUserImage(@PathVariable Long imgId){
//        User user = userService.getUserWithAuthority();
//        int result = userProfileServce.deleteImage(user,imgId);
//        if(result == 1){
//            user = userService.getUserWithAuthority();
//            return ResponseEntity.ok(user.getImages());
//        } else if(result == 0) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ảnh không tồn tại");
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ảnh này không thuộc về hồ sơ của bạn");
//        }
//    }

    @DeleteMapping(value = "/deleteImageUser/{imgId}")
    public ResponseEntity<?> deleteUserImage(@PathVariable Long imgId) {
        User user = userService.getUserWithAuthority();
        Images image = userProfileServce.getImageById(imgId); // Giả định rằng bạn có phương thức này để lấy ảnh từ DB

        if (image != null && image.getUser().equals(user)) {
            String url = image.getUrl();
            String avatarPublicId = extractPublicIdFromUrl(url); // Bạn cần viết phương thức này để lấy public_id từ URL

            try {
                Map deleteParams = ObjectUtils.asMap("public_id", avatarPublicId);
                cloudinary.uploader().destroy(avatarPublicId, deleteParams);

                userProfileServce.deleteImage(image); // Giả định rằng bạn có phương thức này để xóa ảnh từ DB
                user = userService.getUserWithAuthority();
                return ResponseEntity.ok(user.getImages());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa ảnh: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ảnh không được tìm thấy hoặc không thuộc về bạn");
        }
    }
    private String extractPublicIdFromUrl(String url) {
        try {
            URI uri = new URI(url);
            String path = uri.getPath();
            String[] pathParts = path.split("/");
            // public_id thường nằm ở phần tử cuối cùng trước phần mở rộng
            String publicIdWithExtension = pathParts[pathParts.length - 1];
            // Loại bỏ phần mở rộng file
            String publicId = publicIdWithExtension.substring(0, publicIdWithExtension.lastIndexOf('.'));
            return publicId;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
