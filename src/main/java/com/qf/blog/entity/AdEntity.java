package com.qf.blog.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_ad")
public class AdEntity {
    @TableId
    private Integer did;
    private String gcontent;
}
