package com.krrz.controller;

import com.krrz.domain.ResponseResult;
import com.krrz.domain.dto.ListLinkDto;
import com.krrz.domain.entity.Link;
import com.krrz.domain.vo.GetLinkVo;
import com.krrz.service.LinkService;
import com.krrz.utils.PathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private LinkService linkService;
    @PreAuthorize("@ps.hasPermission('content:link:list')")
    @GetMapping("/list")
    public ResponseResult list(@RequestParam("pageNum")Long pageNum, @RequestParam("pageSize")Long pageSize, ListLinkDto listLinkDto){
        return linkService.listLink(pageNum,pageSize,listLinkDto);
    }
    @PreAuthorize("@ps.hasPermission('content:link:add')")
    @PostMapping
    public ResponseResult addLink(@RequestBody Link link){
        return linkService.addLink(link);
    }
    @PreAuthorize("@ps.hasPermission('content:link:query')")
    @GetMapping("/{id}")
    public ResponseResult getLinkById(@PathVariable("id")Long id){
        return linkService.getLinkById(id);
    }
    @PreAuthorize("@ps.hasPermission('content:link:edit')")
    @PutMapping
    public ResponseResult updateLink(@RequestBody GetLinkVo linkDto){
        return linkService.updateLink(linkDto);
    }
    @PreAuthorize("@ps.hasPermission('content:link:remove')")
    @DeleteMapping("/{id}")
    public ResponseResult deleteById(@PathVariable("id")Long id){
        return linkService.deleteById(id);
    }
}
