package com.example.datingbe.service;

import com.example.datingbe.dto.PostDto;
import com.example.datingbe.entity.Post;
import com.example.datingbe.entity.PostImage;
import com.example.datingbe.entity.User;
import com.example.datingbe.repository.PostImageRepository;
import com.example.datingbe.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostImageRepository postImageRepository;
    public List<PostDto> getAllPosts(User user) {
        // Lấy danh sách các Post từ Repository
        Sort sort = Sort.by(Sort.Direction.DESC, "createDate");
        List<Post> posts = postRepository.findAll(sort);

        // Chuyển đổi mỗi Post thành PostDto và thu thập vào danh sách
        return posts.stream()
                .map(post -> new PostDto(post, user)) // Sử dụng constructor PostDto(Post post, User user) để chuyển đổi
                .collect(Collectors.toList());
    }
    public List<PostDto> getPostsByType(User user,String type) {
        // Lấy danh sách các Post từ Repository
        Sort sort = Sort.by(Sort.Direction.DESC, "createDate");
        List<Post> posts = postRepository.findByType(sort,type);

        // Chuyển đổi mỗi Post thành PostDto và thu thập vào danh sách
        return posts.stream()
                .map(post -> new PostDto(post, user)) // Sử dụng constructor PostDto(Post post, User user) để chuyển đổi
                .collect(Collectors.toList());
    }
    public Page<PostDto> getPagePosts(User user, int page, int size) {
        // Lấy danh sách các Post từ Repository
        Sort sort = Sort.by(Sort.Direction.DESC, "createDate");
        Pageable pageable = PageRequest.of(page, size, sort);

        // Lấy Page<Post> từ repository
        Page<Post> postPage = postRepository.findAllByOrderByCreateDateDesc(pageable);

        // Chuyển đổi Page<Post> thành Page<PostDto>
        return postPage.map(post -> new PostDto(post, user));
    }
    public PostDto getPost(Long id,User user) {
        return new PostDto(postRepository.getReferenceById(id),user) ;
    }

    public PostDto createPost(User user, String title, String content,String type, PostImage image) {
        Post post = new Post(null, user, title, content, type, new Date(), null, null);
        post = postRepository.save(post);
        image.setPost(post);
        image = postImageRepository.save(image);
        post.setImages(new HashSet<PostImage>(Set.of(image)));
        return new PostDto(post,user);
    }
    public PostDto updatePost(Long id,User user, String title, String content,String type) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isEmpty()) {
            // Xử lý trường hợp không tìm thấy Post, ví dụ ném ra ngoại lệ
            return null;
        }
        Post post = postOptional.get();
        post.setTitle(title);
        post.setType(type);
        post.setContent(content);
        post = postRepository.save(post);
        return new PostDto(post,user);
    }
    public PostImage updateimgPost(Long id,String imageUrl) {
        Optional<PostImage> postImageOptional = postImageRepository.findById(id);
        if (postImageOptional.isEmpty()) {
            // Xử lý trường hợp không tìm thấy Post, ví dụ ném ra ngoại lệ
            return null;
        }
        PostImage postImage =  postImageOptional.get();
        postImage.setImageUrl(imageUrl);
        postImage = postImageRepository.save(postImage);

        return postImage;
    }
    public PostDto createPost(User user, String title, String content,String type,List<PostImage> images) {
        Post post = new Post(null,user,title,content, type,new Date(), null, null);
        post = postRepository.save(post);
        final Post finalPost = post;
        // Cập nhật thông tin Post cho mỗi PostImage và thu thập vào một list mới
        images.forEach(image -> image.setPost(finalPost));

        images =postImageRepository.saveAll(images);
        post.setImages(new HashSet<>(images));
        return new PostDto(post,user);
    }
    public int deletePost(User user,Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();

            if (post.getUser().getId() == user.getId()) {
                postRepository.deleteById(post.getId());
                return 1; // Xóa thành công
            } else {
                return 2; // Không có quyền xóa
            }
        } else {
            return 0; // Không tìm thấy bài viết
        }
    }

}
