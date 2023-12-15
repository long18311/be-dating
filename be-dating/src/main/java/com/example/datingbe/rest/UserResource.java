package com.example.datingbe.rest;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.datingbe.dto.CombinedData;
import com.example.datingbe.dto.CustomUserDetails;
import com.example.datingbe.dto.UserDto;
import com.example.datingbe.entity.Images;
import com.example.datingbe.entity.User;
import com.example.datingbe.jwt.JwtTokenProvider;
import com.example.datingbe.repository.UserRepository;
import com.example.datingbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserResource {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;



    @Autowired
    private Cloudinary cloudinary;


    public UserResource(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, UserService userService  ) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;

    }
    //API liên quan đến việc đăng nhập bằng goole
    @PostMapping("/custom-authenticate")
    public ResponseEntity<String> custom_authenticate(@RequestBody User user) throws URISyntaxException {
        Optional<User> users = userService.findByEmail(user.getEmail(), user.getPassword());
        if(users.isEmpty()){
            return ResponseEntity.status(401)
                    .body(null);
        }
        CustomUserDetails customUserDetails = new CustomUserDetails(users.get());
        String token = jwtTokenProvider.generateToken(customUserDetails);
        return ResponseEntity
                .ok(token);
    }
    //API liên quan đến việc đăng nhập bằng account & password
    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody User user) throws URISyntaxException {
        Optional<User> users = userService.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if(users.isEmpty()){
            return ResponseEntity.status(401)
                    .body(null);
        }
        CustomUserDetails customUserDetails = new CustomUserDetails(users.get());
        String token = jwtTokenProvider.generateToken(customUserDetails);
        return ResponseEntity
                .ok(token);
    }

    //api đăng kí tài khoản
    @PostMapping("/register")
    public ResponseEntity<Integer> save(@RequestBody User user) throws URISyntaxException {
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            HttpHeaders headers = new HttpHeaders();
            return ResponseEntity.status(500).headers(headers)
                    .body(2);
        }
        User result = userService.save(user);

        return ResponseEntity
                .ok().body(0);
    }
    // api update user
    @PostMapping("/updateUser")
    public ResponseEntity<Integer> update(@RequestBody User user) throws URISyntaxException {
        User u = userService.getUserWithAuthority();
        if(u.getId() != user.getId()){
            return ResponseEntity.status(500).body(0);
        }
        if (user.getPhone()==null){
            u.setPhone(u.getPhone());
        } else{
            u.setPhone(user.getPhone());
        }
        User result = userRepository.save(u);

        return ResponseEntity.status(200).body(1);
    }
    // api check user đang login trên system
    @GetMapping("/userlogged")
    public UserDto getUserLogged(){
        return new UserDto(userService.getUserWithAuthority());
    }
    // api tìm user theo id
    @GetMapping("/public/findUserById")
    public UserDto findById(@RequestParam("id") Long id) {
        return new UserDto(userRepository.findById(id).get());
    }

    //api check xem user đã đăng kí chưa ( dùng trong login google )
    @PostMapping ("/checkRegisterUser")
    public Boolean CheckRegisterUser(@RequestBody User user) {
        Optional<User> User =userRepository.findByEmailAndUID(user.getEmail(),user.getActivation_key());

        return ! User.isEmpty();
    }
   // api lấy ra user không phải admin
    @GetMapping("/admin/getUserNotAdmin")
    public Page<User> getUserNotAdmin(Pageable pageable) {
        return userRepository.findUserNotAdmin("ROLE_ADMIN",pageable);
    }
// api khóa tài khoản user không cho login system
    @PostMapping("/admin/activeUser")
    public void activeOrUnactiveUser(@RequestParam("id") Long id){
        User user = userRepository.findById(id).get();
        if(user.getActived() == 1){
            user.setActived(0);
        }
        else{
            user.setActived(1);
        }
        userRepository.save(user);
    }

    // api lấy ra user không phải user dto

    @GetMapping("/public/findUserNotDtoById")
    public User findUserById(@RequestParam("id") Long id) {
        return userRepository.findById(id).get();
    }


    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserDto> findProfileUser(@PathVariable Long userId) {

        if (userService.getOne(userId)!=null){
            return ResponseEntity.ok(new UserDto(userService.getOne(userId)));
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/getAllUser")
    public ResponseEntity<List<UserDto>> getAllUser() {

           List<UserDto> ListUserDto = new ArrayList<>();

        for (User o :userRepository.findAll()) {
            ListUserDto.add(new UserDto(o));
        }
            return ResponseEntity.ok(ListUserDto);
    }





    //api tạo hồ sơ
    @PostMapping("/register-profile")
    public ResponseEntity<String> registerProfile(
            @RequestParam("firstname") String firstname,
            @RequestParam("lastname") String lastname,
            @RequestPart("avatar") MultipartFile avatar,
            @RequestPart("images") List<MultipartFile> images,
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
            @RequestParam("latitude") String latitude,
            @RequestParam("longitude") String longitude

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
            user.setLatitude(Double.parseDouble(latitude));
            user.setLongitude(Double.parseDouble(longitude));
            user.setNickname(nickname);
            user.setMaritalstatus(maritalstatus);
            String dateString = birthdate;
            // Định dạng của chuỗi ngày
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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


            userRepository.save(user);

            return new ResponseEntity<>("Hồ sơ của bạn đã được cập nhật", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi cập nhật hồ sơ , vui lòng liên hệ admin", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/users-by-gender")
    public List<User> getUsersByGender(@RequestParam String gender) {
        return userService.getFemaleUsers(gender);
    }
    @PostMapping("/users-by-gender-age-location")
    public List<CombinedData> getUsersByGenderAndAge(
            @RequestParam Long id,
            @RequestParam String gender,
            @RequestParam int ageMin,
            @RequestParam int ageMax,
            @RequestParam double locationMin,
            @RequestParam double locationMax
            )
    {
        List<CombinedData> newList = new ArrayList<>();
        User userLogged = userService.getOne(id);
        List<User> usersFind =  userService.findUsersByGenderAndAgeRange(gender.trim(),ageMin,ageMax);

        for(User o : usersFind) {
           double kms = userService.getDistanceBetweenPointsNew(userLogged.getLatitude(),userLogged.getLongitude(),o.getLatitude(),o.getLongitude(),"kilometers");
           if (kms>= locationMin && kms<=locationMax){
               newList.add(new CombinedData(userLogged,o,kms));
           }
        }
        return newList;
      }
    @PostMapping("/admin/addAdmin/{userId}")
    public ResponseEntity<?> addAdmin(
            @PathVariable Long userId
    ) {
        UserDto user = userService.addAdmin(userId);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
    @DeleteMapping("/admin/deleteAdmin/{userId}")
    public ResponseEntity<UserDto> deleteAdmin(
            @PathVariable Long userId
    ) {
        UserDto user = userService.deleteAdmin(userId);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }


}
