package com.krrz.controller;

import com.krrz.constans.SystemConstants;
import com.krrz.domain.ResponseResult;
import com.krrz.domain.dto.AddCommentDto;
import com.krrz.domain.dto.DeleteCommentDto;
import com.krrz.domain.entity.Comment;
import com.krrz.enums.AppHttpCodeEnum;
import com.krrz.exception.SystemException;
import com.krrz.service.CommentService;
import com.krrz.utils.BeanCopyUtils;
import com.krrz.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论" ,description = "评论相关接口")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,pageSize);
    }
    @PostMapping
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto){
        Comment comment= BeanCopyUtils.copyBean(addCommentDto,Comment.class);
        return commentService.addComment(comment);
    }
    @GetMapping("/linkCommentList")
    @ApiOperation(value = "友联评论列表" ,notes = "获取一页友联评论")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "pageNum" ,value = "页号"),
            @ApiImplicitParam(name = "pageSize",value = "每页大小")
            }
    )
    public ResponseResult linkCommentList(Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.LINK_COMMENT,null,pageNum,pageSize);
    }
    @DeleteMapping("/deleteComment")
    public ResponseResult deleteComment(@RequestBody DeleteCommentDto deleteCommentDto){
        //判断是否有权限删除
        Long userId = SecurityUtils.getUserId();
        if(userId==1l || deleteCommentDto.getCreateBy().equals(userId)){
            //有权利
            return commentService.deleteComment(deleteCommentDto);
        }
        //没权力
        throw new SystemException(AppHttpCodeEnum.COMMENT_DELETE_REFUSE);
    }
}
