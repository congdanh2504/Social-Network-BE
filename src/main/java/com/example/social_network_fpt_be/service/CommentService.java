package com.example.social_network_fpt_be.service;

import com.example.social_network_fpt_be.models.Comment;
import com.example.social_network_fpt_be.models.User;
import com.example.social_network_fpt_be.repository.CommentRepository;
import com.example.social_network_fpt_be.service.dtos.CommentDto;
import com.example.social_network_fpt_be.util.ImageType;
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
import java.util.Optional;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final ImageService imageService;
    private final UserService userService;

    @Autowired
    public CommentService(CommentRepository commentRepository, ImageService imageService,@Lazy UserService userService) {
        this.commentRepository = commentRepository;
        this.imageService = imageService;
        this.userService = userService;
    }

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

    public int getCommentCountByPostId(Long post_id) {
        return commentRepository.getCommentByPostId(post_id).size();
    }

    public List<Hashtable<String, Object>> getCommentChild(Long id_comment_father){
        List<Hashtable<String, Object>> result = new ArrayList<>();
        List<Comment> comments = commentRepository.getCommentByFather(id_comment_father);
        for (Comment comment: comments) {
            Hashtable<String, Object> commentList = new Hashtable<>();
            List<Hashtable<String, Object>> commentChild = null;
            if (comment.getId_comment_father() != null) {
                System.out.println();
                commentChild = getCommentChild(comment.getId_comment());
            }
            commentList.put("id_comment", comment.getId_comment());
            commentList.put("id_post", comment.getId_post());
            commentList.put("id_user_comment", comment.getId_user_comment());
            commentList.put("comment", comment.getComment());
            commentList.put("id_comment_child", commentChild == null ? "" : commentChild);
            commentList.put("create_date", comment.getCreate_date());
            List<Hashtable<String, Object>> imageListAll = new ArrayList<>();
            Hashtable<String,Object> imageList = new Hashtable<>();
            for (Object image : imageService.findImageByTypeAndId(ImageType.COMMENT_IMAGE.toString(), comment.getId_comment())) {
                imageList.put("id_image", ((Object[]) image)[0]);
                imageList.put("url", ((Object[]) image)[1]);
                imageList.put("create_date", ((Object[]) image)[2]);
                imageList.put("type", ((Object[]) image)[3]);
                imageList.put("id", ((Object[]) image)[4]);
                imageListAll.add(imageList);
                commentList.put("image_list", imageListAll);
            }
            result.add(commentList);
        }
        return result;
    }

    public Hashtable<String, Object> getCommentById(Long id_comment){
        try {
            Comment comment = commentRepository.findById(id_comment).get();
            Hashtable<String, Object> commemtList = new Hashtable<>();
            commemtList.put("id_comment", comment.getId_comment());
            commemtList.put("id_post", comment.getId_post());
            commemtList.put("id_user_comment", comment.getId_user_comment());
            commemtList.put("comment", comment.getComment());
            commemtList.put("id_comment_child", getCommentChild(comment.getId_comment()));
            commemtList.put("create_date", comment.getCreate_date());
            List<Hashtable<String, Object>> imageListAll = new ArrayList<>();
            Hashtable<String,Object> imageList = new Hashtable<>();
            for (Object image : imageService.findImageByTypeAndId(ImageType.COMMENT_IMAGE.toString(), comment.getId_comment())) {
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

    public Comment createComment(Long id_post, String text, String username) {
        User user = userService.getUserByUsername(username);
        Comment comment = new Comment();
        comment.setId_post(id_post);
        comment.setComment(text);
        comment.setId_user_comment(user.getId());
        comment.setCreate_date(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public Comment replyComment(Long comment_father_id, String text, String username) {
        User user = userService.getUserByUsername(username);
        Comment comment = new Comment();
        comment.setId_comment_father(comment_father_id);
        comment.setComment(text);
        comment.setId_user_comment(user.getId());
        comment.setCreate_date(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public Hashtable<String, Object> createComment(MultipartFile comment_image, Long id_post, Long id_user_comment, Long id_comment_father,String comment) throws IOException {
        Comment cmt = new Comment();
        cmt.setId_post(id_post);
        cmt.setId_user_comment(id_user_comment);
        cmt.setId_comment_father(id_comment_father);
        cmt.setComment(comment);
        cmt.setCreate_date(LocalDateTime.now());
        commentRepository.save(cmt);
        if (comment_image.getContentType() != null){
            imageService.createImage(comment_image, "comment_image", cmt.getId_comment());
        }
        return getCommentById(cmt.getId_comment());
    }

    public Hashtable<String, Object> updateComment(Long id_comment, MultipartFile comment_image, String comment, Long like_number) throws IOException {
        Optional<Comment> commentOld = commentRepository.findById(id_comment);
        if (commentOld.isPresent()){
            commentOld.get().setComment(comment);
            commentOld.get().setCreate_date(LocalDateTime.now());
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

    public void deleteComment(Long id_comment){
        Comment comment = commentRepository.getReferenceById(id_comment);
        List<Comment> children = commentRepository.getCommentByFather(id_comment);
        children.stream().forEach((cmt) -> {
            deleteComment(cmt.getId_comment());
        });
        commentRepository.deleteById(id_comment);
    }


    public List<CommentDto> getCommentsByPostId(Long post_id) {
        List<Comment> comments = commentRepository.getRootCommentByPostId(post_id);
        List<CommentDto> result = new ArrayList<>();
        for (Comment comment: comments) {
            CommentDto commentDto = new CommentDto();
            commentDto.setText(comment.getComment());
            commentDto.setId(comment.getId_comment());
            commentDto.setUser(userService.getUserById(comment.getId_user_comment()));
            commentDto.setCreate_date(comment.getCreate_date());
            commentDto.setChildren(getChildrenComment(comment.getId_comment()));
            result.add(commentDto);
        }
        return result;
    }

    public List<Comment> getRootCommentByPostId(Long post_id) {
        return commentRepository.getRootCommentByPostId(post_id);
    }

    public List<CommentDto> getChildrenComment(Long id_father) {
        List<Comment> comments = commentRepository.getCommentByFather(id_father);
        List<CommentDto> result = new ArrayList<>();
        for (Comment comment: comments) {
            CommentDto commentDto = new CommentDto();
            commentDto.setText(comment.getComment());
            commentDto.setId(comment.getId_comment());
            commentDto.setUser(userService.getUserById(comment.getId_user_comment()));
            commentDto.setCreate_date(comment.getCreate_date());
            commentDto.setChildren(getChildrenComment(comment.getId_comment()));
            result.add(commentDto);
        }
        return result;
    }
}
