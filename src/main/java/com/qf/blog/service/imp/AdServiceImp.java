package com.qf.blog.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qf.blog.entity.AdEntity;
import com.qf.blog.mapper.AdMapper;
import com.qf.blog.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdServiceImp extends ServiceImpl<AdMapper, AdEntity> implements AdService {
    @Autowired
    private AdMapper adMapper;

    @Override
    public List<AdEntity> findAll() {

        return adMapper.selectAll();
    }
}
