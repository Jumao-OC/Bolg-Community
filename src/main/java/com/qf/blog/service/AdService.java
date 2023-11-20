package com.qf.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qf.blog.entity.AdEntity;

import java.util.List;

public interface AdService extends IService<AdEntity> {
    public List<AdEntity> findAll();
}
