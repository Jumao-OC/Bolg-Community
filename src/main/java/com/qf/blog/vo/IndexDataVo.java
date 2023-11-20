package com.qf.blog.vo;

import com.qf.blog.entity.InvitationDataEntity;
import com.qf.blog.entity.InvitationEntity;
import com.qf.blog.entity.UserEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class IndexDataVo implements Serializable {
    private UserEntity userEntity;//返回作者信息
    private InvitationEntity invitationEntity;//返回帖子信息
    private InvitationDataEntity invitationDataEntity;//返回帖子数据
}
