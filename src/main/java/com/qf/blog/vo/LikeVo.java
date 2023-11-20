package com.qf.blog.vo;

import lombok.Data;

//点赞信息
@Data
public class LikeVo {
    private Integer entityType;//2 对回复点赞 1 对帖子 3 对人
    private Integer entityId;
    private Integer entityUserId;//对目标用户
    private Integer userId;//当前用户

}
