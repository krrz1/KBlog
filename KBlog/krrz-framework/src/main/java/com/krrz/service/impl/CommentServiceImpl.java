package com.krrz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krrz.constans.SystemConstants;
import com.krrz.domain.ResponseResult;
import com.krrz.domain.entity.Article;
import com.krrz.domain.entity.Comment;
import com.krrz.domain.entity.User;
import com.krrz.domain.vo.CommentVo;
import com.krrz.domain.vo.PageVo;
import com.krrz.enums.AppHttpCodeEnum;
import com.krrz.exception.SystemException;
import com.krrz.mapper.CommentMapper;
import com.krrz.service.UserService;
import com.krrz.utils.BeanCopyUtils;
import com.krrz.utils.SecurityUtils;
import com.krrz.utils.SensitiveFilter;
import io.jsonwebtoken.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.krrz.service.CommentService;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2022-11-10 13:52:31
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private UserService userService;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Override
    public ResponseResult commentList(String type,Long articleId, Integer pageNum, Integer pageSize) {
        //查询对应文章的评论
        //对articleId进行判断
        LambdaQueryWrapper<Comment> wrapper=new LambdaQueryWrapper<>();

        wrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(type),Comment::getArticleId,articleId);
        //根评论
        wrapper.eq(Comment::getRootId,-1);

        //评论类型
        wrapper.eq(Comment::getType,type);

        //分页查询
        Page<Comment> page=new Page<>(pageNum,pageSize);
        page(page,wrapper);
        List<CommentVo> commentVos= toCommentVoList(page.getRecords());
        //查询所有根评论对应的子评论集合
        for (CommentVo commentVo : commentVos) {
            //查询对应子评论
            List<CommentVo> children=getChildren(commentVo.getId());
            commentVo.setChildren(children);
        }
        return ResponseResult.okResult(new PageVo(commentVos,page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        //评论内容不能为空
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        //过滤敏感词
        String commentText=comment.getContent();
        String filter = sensitiveFilter.filter(commentText);
        comment.setContent(filter);

        save(comment);
        return ResponseResult.okResult();
    }

    @Override
    public void deleteCommentByArticleId(Long id) {
        getBaseMapper().deleteCommentByArticleId(id);
    }

    private List<CommentVo> toCommentVoList(List<Comment> comments){
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(comments, CommentVo.class);
        //遍历Vo集合
        for (CommentVo commentVo : commentVos) {
            String name= userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setToCommentUserName(name);
            if(commentVo.getToCommentUserId()!=-1){
                String nickName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(nickName);
            }
        }
        return commentVos;
    }
    private List<CommentVo> getChildren(Long id){
        LambdaQueryWrapper<Comment> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getRootId,id);
        wrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> list = list(wrapper);
        List<CommentVo> commentVos = toCommentVoList(list);

        return commentVos;
    }
}

