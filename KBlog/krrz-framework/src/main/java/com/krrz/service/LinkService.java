package com.krrz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.krrz.domain.ResponseResult;
import com.krrz.domain.dto.ListLinkDto;
import com.krrz.domain.entity.Link;
import com.krrz.domain.vo.GetLinkVo;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2022-11-09 18:23:35
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult listLink(Long pageNum, Long pageSize, ListLinkDto listLinkDto);

    ResponseResult addLink(Link link);

    ResponseResult getLinkById(Long id);

    ResponseResult updateLink(GetLinkVo linkDto);

    ResponseResult deleteById(Long id);
}
