package com.qf.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qf.blog.entity.AdEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdMapper extends BaseMapper<AdEntity> {
    @Select("SELECT * from t_ad")
    public List<AdEntity> selectAll();
}
