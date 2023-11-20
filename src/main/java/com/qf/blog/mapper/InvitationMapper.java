package com.qf.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qf.blog.entity.InvitationEntity;
import com.qf.blog.vo.IndexDataVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InvitationMapper extends BaseMapper<InvitationEntity> {
    Page<IndexDataVo> selectIndexData(Page<IndexDataVo> page);
}
