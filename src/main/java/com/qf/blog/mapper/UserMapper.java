package com.qf.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qf.blog.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
    @Update("update t_user set statu=#{status} where id = #{uid} and activate_code = #{code} and statu = 0")
//redis中有激活码对应的id
    Integer updateUserStatus(@Param("status") Integer status,
                             @Param("uid") String userId,
                             @Param("code") String code);

}
