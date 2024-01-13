package com.example.datingbe.rest;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.datingbe.dto.PostDto;
import com.example.datingbe.entity.Post;
import com.example.datingbe.entity.PostImage;
import com.example.datingbe.entity.User;
import com.example.datingbe.entity.UserReport;
import com.example.datingbe.service.PostService;
import com.example.datingbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private Cloudinary cloudinary;
    @GetMapping("/getPage")
    public ResponseEntity<Page<PostDto>> getReportsByTypeandStatus(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        User user = userService.getUserWithAuthority();
        Page<PostDto> postPage = postService.getPagePosts( user, page, size);
        return ResponseEntity.ok().body(postPage);
    }
    @GetMapping("/get/getAll")
    public ResponseEntity<List<PostDto>> getAllPosts(){
        User user = userService.getUserWithAuthority();
        return ResponseEntity.ok(postService.getAllPosts(user));
    }
    @GetMapping("/get/{type}")
    public ResponseEntity<List<PostDto>> getListPostByType(@PathVariable String type){
        User user = userService.getUserWithAuthority();
        return ResponseEntity.ok(postService.getPostsByType(user,type));
    }
    @GetMapping("/get/id/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable long postId){
        User user = userService.getUserWithAuthority();
        return ResponseEntity.ok(postService.getPost(postId,user));
    }
    @PostMapping()
    public ResponseEntity<?> postPost(@RequestParam String title,
                                      @RequestParam String type,
                                      @RequestParam String content,
                                      @RequestPart("image") MultipartFile image){
        User user = userService.getUserWithAuthority();
        try {
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap(
                    "resource_type", "auto",
                    "folder", "post_images"
            ));
            // In URL để kiểm tra
            String url = (String) uploadResult.get("url");

            return ResponseEntity.ok(postService.createPost(user, title, content,type, new PostImage(url)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not upload file: " + e.getMessage());
        }
    }
    @PutMapping("/{postId}")
    public ResponseEntity<?> putPost(@PathVariable long postId,
                                     @RequestParam String title,
                                     @RequestParam String type,
                                     @RequestParam String content
                                      ){
        User user = userService.getUserWithAuthority();
        PostDto post = postService.updatePost(postId,user,title,content,type);
        if(post == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("không tìm thấy post");
        }
        return ResponseEntity.ok(post);

    }
    @PutMapping("/img/{postImgId}")
    public ResponseEntity<?> putimGPost(@PathVariable long postImgId,
                                      @RequestPart("image") MultipartFile image){
        User user = userService.getUserWithAuthority();
        try {
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap(
                    "resource_type", "auto",
                    "folder", "post_images"
            ));
            // In URL để kiểm tra
            String url = (String) uploadResult.get("url");
            System.out.println("Uploaded file URL: " + url);

            return ResponseEntity.ok(postService.updateimgPost(postImgId,url));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not upload file: " + e.getMessage());
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable long postId){
        User user = userService.getUserWithAuthority();
        int result = postService.deletePost(user,postId);
        if(result == 1){
            return ResponseEntity.ok("Thành công");
        } else if(result == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Post không tồn tại");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Post này không phải bạn tạo");
        }


    }
}
