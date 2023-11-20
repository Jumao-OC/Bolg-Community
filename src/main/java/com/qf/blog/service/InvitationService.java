package com.qf.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qf.blog.common.dto.BlogPage;
import com.qf.blog.entity.InvitationEntity;
import com.qf.blog.vo.IndexDataVo;

public interface InvitationService extends IService<InvitationEntity> {
    Page<IndexDataVo> indexPage(BlogPage<IndexDataVo> page);
}
