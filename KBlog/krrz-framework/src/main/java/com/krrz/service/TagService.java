package com.krrz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.krrz.domain.ResponseResult;
import com.krrz.domain.dto.AddTagDto;
import com.krrz.domain.dto.TagListDto;
import com.krrz.domain.entity.Tag;
import com.krrz.domain.vo.TagVo;

import java.util.List;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2022-11-13 13:52:05
 */
public interface TagService extends IService<Tag> {

    ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(Tag tag);

    ResponseResult deleteTag(Long id);

    ResponseResult updateTag(Tag tag);

    ResponseResult findOneTag(Long id);

    List<TagVo> listAllTag();
}
