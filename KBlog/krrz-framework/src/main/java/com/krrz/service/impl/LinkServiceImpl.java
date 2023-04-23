package com.krrz.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krrz.constans.SystemConstants;
import com.krrz.domain.ResponseResult;
import com.krrz.domain.dto.ListLinkDto;
import com.krrz.domain.entity.Link;
import com.krrz.domain.entity.Tag;
import com.krrz.domain.vo.GetLinkVo;
import com.krrz.domain.vo.LinkVo;
import com.krrz.domain.vo.PageVo;
import com.krrz.mapper.LinkMapper;
import com.krrz.service.LinkService;
import com.krrz.utils.BeanCopyUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2022-11-09 18:23:35
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        //查询所有审核通过的
        LambdaQueryWrapper<Link> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Link::getStatus , SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links=list(lambdaQueryWrapper);
        //封装VO
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        //封装返回
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult listLink(Long pageNum, Long pageSize, ListLinkDto listLinkDto) {
        //分页查询
        Page<Link> page=new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        LambdaQueryWrapper<Link> wrapper=new LambdaQueryWrapper<>();

        wrapper.eq(StringUtils.hasText(listLinkDto.getName()),Link::getName,listLinkDto.getName());
        wrapper.eq(StringUtils.hasText(listLinkDto.getStatus()),Link::getStatus,listLinkDto.getStatus());

        page(page, wrapper);
        //封装数据返回
        PageVo pageVo=new PageVo(page.getRecords(),page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addLink(Link link) {
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getLinkById(Long id) {
        Link link = getById(id);
        GetLinkVo linkVo = BeanCopyUtils.copyBean(link, GetLinkVo.class);
        return ResponseResult.okResult(linkVo);
    }

    @Override
    public ResponseResult updateLink(GetLinkVo linkDto) {
        Link link = BeanCopyUtils.copyBean(linkDto, Link.class);
        updateById(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteById(Long id) {
        getBaseMapper().deleteById(id);
        return ResponseResult.okResult();
    }
}

