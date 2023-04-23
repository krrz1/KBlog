package com.krrz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.krrz.domain.entity.Comment;
import org.apache.ibatis.annotations.Param;


/**
 * 评论表(Comment)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-10 13:52:31
 */
public interface CommentMapper extends BaseMapper<Comment> {

    void deleteCommentByArticleId(@Param("id")Long id);
}
