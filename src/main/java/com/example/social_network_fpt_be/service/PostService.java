package com.example.social_network_fpt_be.service;

import com.example.social_network_fpt_be.models.Post;
import com.example.social_network_fpt_be.models.User;
import com.example.social_network_fpt_be.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;

    private final ImageService imageService;

    private final VideoService videoService;

    private final UserService userService;

    private final LikeRecordService likeRecordService;

    private final CommentService commentService;

    @Autowired
    public PostService(PostRepository postRepository, ImageService imageService, VideoService videoService, @Lazy UserService userService, LikeRecordService likeRecordService, CommentService commentService) {
        this.postRepository = postRepository;
        this.imageService = imageService;
        this.videoService = videoService;
        this.userService = userService;
        this.likeRecordService = likeRecordService;
        this.commentService = commentService;
    }

    public List<Hashtable<String, Object>> getPostList() {
        List<Object> posts = postRepository.getAllPost();
        return getPostDetail(posts);
    }

    public List<Hashtable<String, Object>> getFiveLatestPosts() {
        List<Object> posts = postRepository.getFiveLatestPosts();
        return getPostDetail(posts);
    }

    private List<Hashtable<String, Object>> getPostDetail(List<Object> posts) {
        List<Hashtable<String, Object>> result = new ArrayList<>();
        if (posts == null) {
            return null;
        }
        for (Object post : posts) {
            // output the first value in posts
            Hashtable<String, Object> postList = new Hashtable<>();
            postList.put("id_post", ((Object[]) post)[0]);
            Hashtable<String, Object> userMap = new Hashtable<>();
            User user = userService.getUserById(Long.parseLong(String.valueOf(((Object[]) post)[1])));
            int likeCount = likeRecordService.getLikeCountByIdPost(Long.parseLong(String.valueOf(((Object[]) post)[0])));
            int commentCount = commentService.getCommentCountByPostId(Long.parseLong(String.valueOf(((Object[]) post)[0])));
            userMap.put("id", user.getId());
            userMap.put("email", user.getEmail());
            userMap.put("username", user.getUsername());
            userMap.put("firstName", user.getFirstName());
            userMap.put("lastName", user.getLastName());
            userMap.put("avt", imageService.getAvatarByUser(user.getId()));
            postList.put("user", userMap);
            postList.put("title", ((Object[]) post)[2]);
            postList.put("description", ((Object[]) post)[3]);
            postList.put("like", likeCount);
            postList.put("comment", commentCount);
            postList.put("create_date", ((Object[]) post)[4]);
            if (((Object[]) post)[5] != null) {
                postList.put("post_video", ((Object[]) post)[5]);
                postList.put("url_video", ((Object[]) post)[6]);
            } else {
                postList.put("post_video", "");
                postList.put("url_video", "");
            }
            List<Hashtable<String, Object>> imageListAll = new ArrayList<>();

            List<Object> images = imageService.findImageByTypeAndId("post_image", Long.parseLong(String.valueOf(postList.get("id_post"))));
            for (Object image : images) {
                Hashtable<String, Object> imageList = new Hashtable<>();
                imageList.put("id_image", ((Object[]) image)[0]);
                imageList.put("url", ((Object[]) image)[1]);
                imageList.put("create_date", ((Object[]) image)[2]);
                imageList.put("type", ((Object[]) image)[3]);
                imageList.put("id", ((Object[]) image)[4]);
                imageListAll.add(imageList);
            }
            postList.put("image_list", imageListAll);
            result.add(postList);
        }
        return result;
    }

    public Hashtable<String, Object> getPostByID(Long id_post) {
        Hashtable<String, Object> postList = new Hashtable<>();
        Hashtable<String, Object> userMap = new Hashtable<>();
        Object post = postRepository.getPostBy(id_post);
        if (post == null) {
            return null;
        }
        postList.put("id_post", ((Object[]) post)[0]);
        postList.put("id_user", ((Object[]) post)[1]);
        postList.put("title", ((Object[]) post)[2]);
        postList.put("description", ((Object[]) post)[3]);
        postList.put("user", userMap);
        postList.put("create_date", ((Object[]) post)[4]);
        if (((Object[]) post)[5] != null) {
            postList.put("post_video", ((Object[]) post)[5]);
            postList.put("url_video", ((Object[]) post)[6]);
        } else {
            postList.put("post_video", "");
            postList.put("url_video", "");
        }
        List<Hashtable<String, Object>> imageListAll = new ArrayList<>();
        Hashtable<String, Object> imageList = new Hashtable<>();
        List<Object> images = imageService.findImageByTypeAndId("post_image", Long.parseLong(String.valueOf(postList.get("id_post"))));
        for (Object image : images) {
            imageList.put("id_image", ((Object[]) image)[0]);
            imageList.put("url", ((Object[]) image)[1]);
            imageList.put("create_date", ((Object[]) image)[2]);
            imageList.put("type", ((Object[]) image)[3]);
            imageList.put("id", ((Object[]) image)[4]);
            imageListAll.add(imageList);
            postList.put("image_list", imageListAll);
        }
        return postList;
    }

    public Post createPost(List<MultipartFile> post_image, Long id_user, String title, String description) throws IOException {
//        Long id_video = null;
//        if (post_video.getContentType() != null) {
//            id_video = videoService.createVideo(post_video).getId_video();
//        }
        Post post = new Post();
        post.setId_user(id_user);
        post.setTitle(title);
        post.setDescription(description);
        post.setCreate_date(LocalDateTime.now());
        postRepository.save(post);
//        System.out.println(post);
//        System.out.println(post_image.size());
        for (MultipartFile multipartFile : post_image) {
            if (multipartFile.getContentType() != null) {
                imageService.createImage(multipartFile, "post_image", post.getId_post());
            }
        }
        return post;
    }

    public Hashtable<String, Object> updatePost(Long id_post, MultipartFile post_video, List<MultipartFile> post_image, Long id_user, String title, String description) throws IOException {
        Long id_video = null;
        Post postOld = postRepository.findById(id_post).orElse(null);
        if (post_video.getContentType() != null) {
            if (postOld != null && postOld.getPost_video() != null) {
                videoService.updateVideo(postOld.getPost_video(), post_video);
            } else {
                id_video = videoService.createVideo(post_video).getId_video();
                System.out.println("id_video: " + id_video);
            }
        } else if (postOld.getPost_video() != null) {
            videoService.deleteVideo(postOld.getPost_video());
        }

        if (imageService.findImageByTypeAndId("video_image", id_post) != null) {
            imageService.deleteImageByTypeAndId("post_image", id_post);
        }
        if (post_image.get(0).getContentType() != null) {
            for (MultipartFile multipartFile : post_image) {
                imageService.createImage(multipartFile, "post_image", id_post);
            }
        }
        if (postOld != null) {
            postOld.setId_user(id_user);
            postOld.setTitle(title);
            postOld.setDescription(description);
            postOld.setCreate_date(LocalDateTime.now());
            postOld.setPost_video(id_video);
            postRepository.save(postOld);
        }
        return getPostByID(id_post);

    }

    public String deletePost(Long id_post) {
        try {
            postRepository.deleteById(id_post);
            imageService.deleteImageByTypeAndId("post_image", id_post);
            videoService.deleteVideo(id_post);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }
}
