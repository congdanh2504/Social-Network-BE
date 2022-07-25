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

@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ImageService imageService;

    public List<Hashtable<String, Object>> getAllComments() {
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
            result.add(commemtList);
        }
        return result;
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
            result.add(commemtList);
        }
        return result;
    }

    public Hashtable<String, Object> getCommentById(Integer id_comment){
        Comment comment = commentRepository.findById(id_comment).get();
        Hashtable<String, Object> commemtList = new Hashtable<>();
        commemtList.put("id_comment", comment.getId_comment());
        commemtList.put("id_post", comment.getId_post());
        commemtList.put("id_user_comment", comment.getId_user_comment());
        commemtList.put("comment", comment.getComment());
        commemtList.put("id_comment_child", getCommentChild(comment.getId_comment()));
        commemtList.put("create_date", comment.getCreate_date());
        commemtList.put("like_number", comment.getLike_number());
        return commemtList;
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
}
