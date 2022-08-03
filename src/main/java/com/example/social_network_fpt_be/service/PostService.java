package com.example.social_network_fpt_be.service;

import com.example.social_network_fpt_be.models.Comment;
import com.example.social_network_fpt_be.models.Image;
import com.example.social_network_fpt_be.models.Post;
import com.example.social_network_fpt_be.models.User;
import com.example.social_network_fpt_be.repository.PostRepository;
import com.example.social_network_fpt_be.service.dtos.CommentDto;
import com.example.social_network_fpt_be.service.dtos.DetailPostDto;
import com.example.social_network_fpt_be.service.dtos.EditPostDto;
import com.example.social_network_fpt_be.service.dtos.UploadPostDto;
import com.example.social_network_fpt_be.util.ImageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<DetailPostDto> getPostList() {
        List<Post> posts = postRepository.getAllPost();
        return getDetailPosts(posts);
    }

    public List<DetailPostDto> getFiveLatestPosts() {
        List<Post> posts = postRepository.getFiveLatestPosts();
        return getDetailPosts(posts);
    }

    public List<DetailPostDto> getUserPosts(Long user_id) {
        List<Post> posts = postRepository.getUserPosts(user_id);
        return getDetailPosts(posts);
    }

    public DetailPostDto getPostById(Long post_id) {
        Post post = postRepository.getReferenceById(post_id);
        return getDetailPost(post);
    }

    public List<DetailPostDto> getFollowingPosts(String username) {
        User user = userService.getUserByUsername(username);
        List<Post> posts = postRepository.getFollowingPosts(user.getId());
        return posts.stream().map((this::getDetailPost)).collect(Collectors.toList());
    }

    private DetailPostDto getDetailPost(Post post) {
        DetailPostDto detailPostDto = new DetailPostDto();
        detailPostDto.setId(post.getId_post());
        detailPostDto.setTitle(post.getTitle());
        detailPostDto.setDescription(post.getDescription());
        detailPostDto.setCreate_date(post.getCreate_date());
        detailPostDto.setUser(userService.getUserById(post.getId_user()));
        detailPostDto.setImages(imageService.findImageByTypeAndId(ImageType.POST_IMAGE.toString(), post.getId_post()));
        detailPostDto.setLikeUsers(likeRecordService.getLikeUsersByPostId(post.getId_post()));
        detailPostDto.setComments(commentService.getCommentsByPostId(post.getId_post()));
        return detailPostDto;
    }

    private List<DetailPostDto> getDetailPosts(List<Post> posts) {
        List<DetailPostDto> result = new ArrayList<>();
        if (posts == null) {
            return result;
        }
        for (Post post : posts) {
            result.add(getDetailPost(post));
        }
        return result;
    }

    //    public Hashtable<String, Object> getPostByID(Long id_post) {
//        Hashtable<String, Object> postList = new Hashtable<>();
//        Hashtable<String, Object> userMap = new Hashtable<>();
//        Object post = postRepository.getPostBy(id_post);
//        if (post == null) {
//            return null;
//        }
//        postList.put("id_post", ((Object[]) post)[0]);
//        postList.put("id_user", ((Object[]) post)[1]);
//        postList.put("title", ((Object[]) post)[2]);
//        postList.put("description", ((Object[]) post)[3]);
//        postList.put("user", userMap);
//        postList.put("create_date", ((Object[]) post)[4]);
//        if (((Object[]) post)[5] != null) {
//            postList.put("post_video", ((Object[]) post)[5]);
//            postList.put("url_video", ((Object[]) post)[6]);
//        } else {
//            postList.put("post_video", "");
//            postList.put("url_video", "");
//        }
//        List<Hashtable<String, Object>> imageListAll = new ArrayList<>();
//        Hashtable<String, Object> imageList = new Hashtable<>();
//        List<Object> images = imageService.findImageByTypeAndId("post_image", Long.parseLong(String.valueOf(postList.get("id_post"))));
//        for (Object image : images) {
//            imageList.put("id_image", ((Object[]) image)[0]);
//            imageList.put("url", ((Object[]) image)[1]);
//            imageList.put("create_date", ((Object[]) image)[2]);
//            imageList.put("type", ((Object[]) image)[3]);
//            imageList.put("id", ((Object[]) image)[4]);
//            imageListAll.add(imageList);
//            postList.put("image_list", imageListAll);
//        }
//        return postList;
//    }
    public Post createPost(UploadPostDto postDto, Long id_user) throws IOException {
        Post post = new Post();
        post.setId_user(id_user);
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setCreate_date(LocalDateTime.now());
        postRepository.save(post);
        for (String url : postDto.getImages()) {
            imageService.createWithURL(url, ImageType.POST_IMAGE.toString(), post.getId_post());
        }
        return post;
    }

    public void editPost(EditPostDto postDto, Long id_post) {
        Post post = postRepository.getReferenceById(id_post);
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        postRepository.save(post);
        List<String> urlOldImage = imageService.findImageByTypeAndId(ImageType.POST_IMAGE.toString(), id_post).stream().map((Image::getUrl)).collect(Collectors.toList());

        urlOldImage.forEach((img) -> {
            if (!postDto.getOldImages().contains(img)) {
                imageService.deleteByUrl(img);
            }
        });
        for (String url : postDto.getNewImages()) {
            imageService.createWithURL(url, ImageType.POST_IMAGE.toString(), id_post);
        }
//        return post;
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

//    public Hashtable<String, Object> updatePost(Long id_post, MultipartFile post_video, List<MultipartFile> post_image, Long id_user, String title, String description) throws IOException {
//        Long id_video = null;
//        Post postOld = postRepository.findById(id_post).orElse(null);
//        if (post_video.getContentType() != null) {
//            if (postOld != null && postOld.getPost_video() != null) {
//                videoService.updateVideo(postOld.getPost_video(), post_video);
//            } else {
//                id_video = videoService.createVideo(post_video).getId_video();
//                System.out.println("id_video: " + id_video);
//            }
//        } else if (postOld.getPost_video() != null) {
//            videoService.deleteVideo(postOld.getPost_video());
//        }
//
//        if (imageService.findImageByTypeAndId("video_image", id_post) != null) {
//            imageService.deleteImageByTypeAndId("post_image", id_post);
//        }
//        if (post_image.get(0).getContentType() != null) {
//            for (MultipartFile multipartFile : post_image) {
//                imageService.createImage(multipartFile, "post_image", id_post);
//            }
//        }
//        if (postOld != null) {
//            postOld.setId_user(id_user);
//            postOld.setTitle(title);
//            postOld.setDescription(description);
//            postOld.setCreate_date(LocalDateTime.now());
//            postOld.setPost_video(id_video);
//            postRepository.save(postOld);
//        }
//        return getPostByID(id_post);
//
//    }

    public void deletePost(Long id_post) {
        List<Comment> comments = commentService.getRootCommentByPostId(id_post);
        comments.forEach((cmt) -> {
            commentService.deleteComment(cmt.getId_comment());
        });
        imageService.deleteImageByTypeAndId(ImageType.POST_IMAGE.toString(), id_post);
        postRepository.deleteById(id_post);
    }
}
