package com.qf.blog.service.imp;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qf.blog.common.dto.BlogPage;
import com.qf.blog.common.status.BlogStatus;
import com.qf.blog.entity.InvitationDataEntity;
import com.qf.blog.entity.InvitationEntity;
import com.qf.blog.mapper.InvitationMapper;
import com.qf.blog.service.CommentService;
import com.qf.blog.service.ILikeService;
import com.qf.blog.service.InvitationService;
import com.qf.blog.vo.IndexDataVo;
import com.qf.blog.vo.LikeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvitationServiceImp extends ServiceImpl<InvitationMapper, InvitationEntity> implements InvitationService {
    @Autowired
    private InvitationMapper invitationMapper;
    @Autowired
    private ILikeService iLikeService;
    @Autowired
    private CommentService commentService;

    @Override
    public Page<IndexDataVo> indexPage(BlogPage<IndexDataVo> blogpage) {


        //使用mapper xml

        //如果帖子详情表没有对应数据
        //可以去统计不同表数据
        //（单个帖子的评论总数 可以 从评论表里查询出来）
        Page<IndexDataVo> page = baseMapper.selectIndexData(blogpage);
        //目前帖子详情表示空的，换一种写法统计帖子的数据

        //循环帖子详情
        for (IndexDataVo indexDataVo : page.getRecords()) {
            //循环每一条帖子的数据
            //新增点赞对象
            LikeVo likeVo = new LikeVo();

            likeVo.setEntityType(1);
            likeVo.setEntityId(indexDataVo.getInvitationEntity().getId());
            //如果当前帖子详情数据为空
            if (indexDataVo.getInvitationDataEntity() == null) {
                //放一个空对象进去
                indexDataVo.setInvitationDataEntity(new InvitationDataEntity());
                //统计对应帖子的点赞数据 放入到详情里面

                //统计对应帖子的评论数据 放入到详情里面

            }
            //查询帖子点赞数量
            Long likeCount = iLikeService.LikeCount(likeVo);
            indexDataVo.getInvitationDataEntity().setLikes(Integer.parseInt(String.valueOf(likeCount)));
            //查询帖子评论数量
            Long commentCount = commentService.getCommentCountByTopicId(
                    BlogStatus.TOPIC_COMMENT.getCode(),
                    indexDataVo.getInvitationEntity().getId()
            );
            indexDataVo.getInvitationDataEntity().setComments(Integer.parseInt(commentCount.toString()));
        }
        return page;
    }
}
