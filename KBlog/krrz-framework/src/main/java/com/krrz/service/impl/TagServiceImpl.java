package com.krrz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krrz.constans.SystemConstants;
import com.krrz.domain.ResponseResult;
import com.krrz.domain.dto.AddTagDto;
import com.krrz.domain.dto.TagListDto;
import com.krrz.domain.vo.PageVo;
import com.krrz.domain.vo.TagVo;
import com.krrz.enums.AppHttpCodeEnum;
import com.krrz.exception.SystemException;
import com.krrz.utils.BeanCopyUtils;
import com.krrz.utils.SecurityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import com.krrz.service.TagService;
import com.krrz.domain.entity.Tag;
import com.krrz.mapper.TagMapper;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2022-11-13 13:52:05
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        //分页查询
        Page<Tag> page=new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        LambdaQueryWrapper<Tag> wrapper=new LambdaQueryWrapper<>();

        wrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        wrapper.eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());

        page(page, wrapper);
        //封装数据返回
        PageVo pageVo=new PageVo(page.getRecords(),page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(Tag tag) {
        if(!StringUtils.hasText(tag.getName())){
            throw new SystemException(AppHttpCodeEnum.TAG_NAME_NOT_NULL);
        }
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTag(Long id) {
        LambdaUpdateWrapper<Tag> wrapper=new LambdaUpdateWrapper<>();
        wrapper.eq(Tag::getId,id);
        wrapper.set(Tag::getDelFlag,1);
        update(wrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateTag(Tag tag) {
        LambdaUpdateWrapper<Tag> wrapper=new LambdaUpdateWrapper<>();
        wrapper.set(Tag::getName,tag.getName());
        wrapper.set(Tag::getRemark,tag.getRemark());
        wrapper.eq(Tag::getId,tag.getId());

        update(wrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult findOneTag(Long id) {
        LambdaQueryWrapper<Tag> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Tag::getId,id);
        Tag one = getOne(wrapper);
        TagVo tagVo = BeanCopyUtils.copyBean(one, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    @Override
    public List<TagVo> listAllTag() {
        List<Tag> list = list();
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(list, TagVo.class);
        return tagVos;
    }
}

