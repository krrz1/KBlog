package com.krrz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.krrz.domain.ResponseResult;
import com.krrz.domain.dto.DeleteCommentDto;
import com.krrz.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2022-11-10 13:52:30
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String type,Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);

    void deleteCommentByArticleId(Long id);

    ResponseResult deleteComment(DeleteCommentDto deleteCommentDto);
}
