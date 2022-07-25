package com.example.social_network_fpt_be.service;

import com.example.social_network_fpt_be.models.Comment;
import com.example.social_network_fpt_be.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ImageService imageService;

    public List<Hashtable<String, Object>> getAllComments() {
        try {
            List<Hashtable<String, Object>> result = new ArrayList<>();
            List<Comment> comments = commentRepository.getCommentRoot();
            for (Comment comment: comments){
                Hashtable<String, Object> commemtList = new Hashtable<>();
                commemtList.put("id_comment", comment.getId_comment());
                commemtList.put("id_post", comment.getId_post());
                commemtList.put("id_user_comment", comment.getId_user_comment());
                commemtList.put("comment", comment.getComment());
                commemtList.put("id_comment_child", getCommentChild(comment.getId_comment()));
                commemtList.put("create_date", comment.getCreate_date());
                commemtList.put("like_number", comment.getLike_number());
                List<Hashtable<String, Object>> imageListAll = new ArrayList<>();
                Hashtable<String,Object> imageList = new Hashtable<>();
                for (Object image : imageService.findImageByTypeAndId("comment_image", comment.getId_comment())) {
                    imageList.put("id_image", ((Object[]) image)[0]);
                    imageList.put("url", ((Object[]) image)[1]);
                    imageList.put("create_date", ((Object[]) image)[2]);
                    imageList.put("type", ((Object[]) image)[3]);
                    imageList.put("id", ((Object[]) image)[4]);
                    imageListAll.add(imageList);
                    commemtList.put("image_list", imageListAll);
                }
                result.add(commemtList);
            }
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    public List<Hashtable<String, Object>> getCommentChild(Integer id_comment_father){
        List<Hashtable<String, Object>> result = new ArrayList<>();
        List<Comment> comments = commentRepository.getCommentByFather(id_comment_father);
        for (Comment comment: comments) {
            Hashtable<String, Object> commemtList = new Hashtable<>();
            List<Hashtable<String, Object>> commentChild = null;
            if (comment.getId_comment_father() != null) {
                System.out.println();
                commentChild = getCommentChild(comment.getId_comment());
            }
            commemtList.put("id_comment", comment.getId_comment());
            commemtList.put("id_post", comment.getId_post());
            commemtList.put("id_user_comment", comment.getId_user_comment());
            commemtList.put("comment", comment.getComment());
            commemtList.put("id_comment_child", commentChild == null ? "" : commentChild);
            commemtList.put("create_date", comment.getCreate_date());
            commemtList.put("like_number", comment.getLike_number());
            List<Hashtable<String, Object>> imageListAll = new ArrayList<>();
            Hashtable<String,Object> imageList = new Hashtable<>();
            for (Object image : imageService.findImageByTypeAndId("comment_image", comment.getId_comment())) {
                imageList.put("id_image", ((Object[]) image)[0]);
                imageList.put("url", ((Object[]) image)[1]);
                imageList.put("create_date", ((Object[]) image)[2]);
                imageList.put("type", ((Object[]) image)[3]);
                imageList.put("id", ((Object[]) image)[4]);
                imageListAll.add(imageList);
                commemtList.put("image_list", imageListAll);
            }
            result.add(commemtList);
        }
        return result;
    }

    public Hashtable<String, Object> getCommentById(Integer id_comment){
        try {
            Comment comment = commentRepository.findById(id_comment).get();
            Hashtable<String, Object> commemtList = new Hashtable<>();
            commemtList.put("id_comment", comment.getId_comment());
            commemtList.put("id_post", comment.getId_post());
            commemtList.put("id_user_comment", comment.getId_user_comment());
            commemtList.put("comment", comment.getComment());
            commemtList.put("id_comment_child", getCommentChild(comment.getId_comment()));
            commemtList.put("create_date", comment.getCreate_date());
            commemtList.put("like_number", comment.getLike_number());
            List<Hashtable<String, Object>> imageListAll = new ArrayList<>();
            Hashtable<String,Object> imageList = new Hashtable<>();
            for (Object image : imageService.findImageByTypeAndId("comment_image", comment.getId_comment())) {
                imageList.put("id_image", ((Object[]) image)[0]);
                imageList.put("url", ((Object[]) image)[1]);
                imageList.put("create_date", ((Object[]) image)[2]);
                imageList.put("type", ((Object[]) image)[3]);
                imageList.put("id", ((Object[]) image)[4]);
                imageListAll.add(imageList);
                commemtList.put("image_list", imageListAll);
            }
            return commemtList;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    public Hashtable<String, Object> createComment(MultipartFile comment_image, Integer id_post, Integer id_user_comment, Integer id_comment_father,String comment) throws IOException {
        Comment cmt = new Comment();
        cmt.setId_post(id_post);
        cmt.setId_user_comment(id_user_comment);
        cmt.setId_comment_father(id_comment_father);
        cmt.setComment(comment);
        cmt.setCreate_date(LocalDateTime.now());
        cmt.setLike_number(0L);
        commentRepository.save(cmt);
        if (comment_image.getContentType() != null){
            imageService.createImage(comment_image, "comment_image", cmt.getId_comment());
        }
        return getCommentById(cmt.getId_comment());
    }

    public Hashtable<String, Object> updateComment(Integer id_comment, MultipartFile comment_image, String comment, Long like_number) throws IOException {
        Optional<Comment> commentOld = commentRepository.findById(id_comment);
        if (commentOld.isPresent()){
            commentOld.get().setComment(comment);
            commentOld.get().setCreate_date(LocalDateTime.now());
            commentOld.get().setLike_number(like_number);
            if (comment_image.getContentType() != null){
                imageService.deleteImageByTypeAndId("comment_image", id_comment);
                imageService.createImage(comment_image, "comment_image", commentOld.get().getId_comment());
            } else {
                imageService.deleteImageByTypeAndId("comment_image", id_comment);
            }
            commentRepository.save(commentOld.get());
        }
        return getCommentById(id_comment);
    }

    public String deleteComment(Integer id_comment){
        try{
            commentRepository.deleteById(id_comment);
            imageService.deleteImageByTypeAndId("comment_image", id_comment);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }


}
