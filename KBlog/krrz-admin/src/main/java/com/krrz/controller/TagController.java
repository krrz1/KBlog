package com.krrz.controller;

import com.krrz.domain.ResponseResult;
import com.krrz.domain.dto.AddTagDto;
import com.krrz.domain.dto.TagListDto;
import com.krrz.domain.dto.TagUpdateDto;
import com.krrz.domain.entity.Tag;
import com.krrz.domain.vo.TagVo;
import com.krrz.service.TagService;
import com.krrz.utils.BeanCopyUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;
    @PreAuthorize("@ps.hasPermission('content:tag:index')")
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }
    @PostMapping("")
    public ResponseResult addTag(@RequestBody AddTagDto addTagDto){
        Tag tag = BeanCopyUtils.copyBean(addTagDto, Tag.class);
        return tagService.addTag(tag);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable("id")Long id){
        return tagService.deleteTag(id);
    }
    @GetMapping("/{id}")
    public ResponseResult findOneTag(@PathVariable("id")Long id){
        return tagService.findOneTag(id);
    }
    @PutMapping("")
    public ResponseResult updateTag(@RequestBody TagUpdateDto tagUpdateDto){
        Tag tag = BeanCopyUtils.copyBean(tagUpdateDto, Tag.class);
        return tagService.updateTag(tag);
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<TagVo> list=tagService.listAllTag();
        return ResponseResult.okResult(list);
    }

}
