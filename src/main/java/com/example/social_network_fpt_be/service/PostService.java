package com.example.social_network_fpt_be.service;

import com.example.social_network_fpt_be.model.Post;
import com.example.social_network_fpt_be.model.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private VideoService videoService;


    public List<Hashtable<String, Object>> getPostList() {
        List<Hashtable<String, Object>> result = new ArrayList<>();
        List<Object> posts = postRepository.getAllPost();
        if (posts == null){
            return null;
        }
        for (Object post: posts) {
            // output the first value in posts
            Hashtable<String, Object> postList = new Hashtable<>();
            postList.put("id_post", ((Object[]) post)[0]);
            postList.put("id_user", ((Object[]) post)[1]);
            postList.put("title", ((Object[]) post)[2]);
            postList.put("description", ((Object[]) post)[3]);
            postList.put("create_date", ((Object[]) post)[4]);
            if (((Object[]) post)[5] != null) {
                postList.put("post_video", ((Object[]) post)[5]);
                postList.put("url_video", ((Object[]) post)[6]);
            } else {
                postList.put("post_video", "");
                postList.put("url_video", "");
            }
            List<Hashtable<String, Object>> imageListAll = new ArrayList<>();
            Hashtable<String,Object> imageList = new Hashtable<>();
            List<Object> images = imageService.findImageForPost("post_image", (int) postList.get("id_post"));
            for (Object image: images) {
                imageList.put("id_image", ((Object[]) image)[0]);
                imageList.put("url", ((Object[]) image)[1]);
                imageList.put("create_date", ((Object[]) image)[2]);
                imageList.put("type", ((Object[]) image)[3]);
                imageList.put("id", ((Object[]) image)[4]);
                imageListAll.add(imageList);
                postList.put("image_list", imageListAll);
            }
            result.add(postList);
        }
        return result;
    }

    public Hashtable<String, Object> getPostByID(Integer id_post) {
        Hashtable<String, Object> postList = new Hashtable<>();
        Object post = postRepository.getPostBy(id_post);
        if (post == null){
            return null;
        }
        postList.put("id_post", ((Object[]) post)[0]);
        postList.put("id_user", ((Object[]) post)[1]);
        postList.put("title", ((Object[]) post)[2]);
        postList.put("description", ((Object[]) post)[3]);
        postList.put("create_date", ((Object[]) post)[4]);
        if (((Object[]) post)[5] != null) {
            postList.put("post_video", ((Object[]) post)[5]);
            postList.put("url_video", ((Object[]) post)[6]);
        } else {
            postList.put("post_video", "");
            postList.put("url_video", "");
        }
        List<Hashtable<String, Object>> imageListAll = new ArrayList<>();
        Hashtable<String,Object> imageList = new Hashtable<>();
        List<Object> images = imageService.findImageForPost("post_image", (int) postList.get("id_post"));
        for (Object image: images) {
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

    public Hashtable<String, Object> createPost(MultipartFile post_video, List<MultipartFile> post_image, Integer id_user, String title, String description) throws IOException {
        Integer id_video = null;
        if (post_video.getContentType() != null) {
            id_video = videoService.createVideo(post_video).getId_video();
        }
        Post post = new Post();
        post.setId_user(id_user);
        post.setTitle(title);
        post.setDescription(description);
        post.setCreate_date(LocalDateTime.now());
        post.setPost_video(id_video);
        postRepository.save(post);
        for (MultipartFile multipartFile: post_image){
            if (multipartFile.getContentType() != null) {
                imageService.createImage(multipartFile, "post_image", post.getId_post());
            }
        }
        return getPostByID(post.getId_post());
    }

    public Hashtable<String, Object> updatePost(Integer id_post, MultipartFile post_video, List<MultipartFile> post_image, Integer id_user, String title, String description) throws IOException {
        Integer id_video = null;
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

        if (imageService.findImageForPost("video_image", id_post) != null){
            imageService.deleteImageByTypeAndId("post_image", id_post);
        }
        if (post_image.get(0).getContentType() != null) {
            for (MultipartFile multipartFile: post_image){
                imageService.createImage(multipartFile, "post_image", id_post);
            }
        }
        if (postOld != null){
            postOld.setId_user(id_user);
            postOld.setTitle(title);
            postOld.setDescription(description);
            postOld.setCreate_date(LocalDateTime.now());
            postOld.setPost_video(id_video);
            postRepository.save(postOld);
        }
        return getPostByID(id_post);

    }

    public String deletePost(int id_post){
        try{
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
