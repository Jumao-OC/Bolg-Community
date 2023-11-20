package com.qf.blog.service.imp;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qf.blog.common.dto.BlogPage;
import com.qf.blog.common.help.UserHelp;
import com.qf.blog.common.status.BlogStatus;
import com.qf.blog.entity.CommentEntity;
import com.qf.blog.mapper.CommentMapper;
import com.qf.blog.service.CommentService;
import com.qf.blog.service.ILikeService;
import com.qf.blog.vo.CommentVo;
import com.qf.blog.vo.LikeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImp extends ServiceImpl<CommentMapper, CommentEntity> implements CommentService {
    @Autowired
    private UserHelp userHelp;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ILikeService iLikeService;

    @Override
    public BlogPage<CommentVo> getCommentListByTid(BlogPage<CommentVo> page, Integer id) {//查完一级评论查看二级评论
        //查询一级评论（对帖子评论）
        page = baseMapper.selectCommentListTid(page, id, BlogStatus.TOPIC_COMMENT.getCode());
        //回复列表
        BlogPage newReplyPage = new BlogPage();
        LikeVo likeVo = new LikeVo();
        likeVo.setEntityType(2);
        if (userHelp.get() != null) {
            likeVo.setUserId((userHelp.get().getUid()));
        }
        //查看二级评论 (对评论的回复)
        List<CommentVo> records = page.getRecords();
        for (CommentVo comment : records) {
            //循环一级评论 查询二级评论
            likeVo.setEntityId(comment.getCommentEntity().getId());

            //查询一级评论的点赞信息
            findCommentLikeInfo(comment, likeVo);
            //查询某一个一级评论的所有二级评论
            Integer cid = comment.getCommentEntity().getId();
            Page<CommentVo> replyPage = baseMapper
                    .selectCommentListTid(newReplyPage, cid, BlogStatus.TOPIC_REPLY.getCode());
            List<CommentVo> records1 = replyPage.getRecords();
            for (CommentVo commentVo : records1) {
                //设置二级评论的id
                likeVo.setEntityId(commentVo.getCommentEntity().getId());
                //查询二级评论的点赞信息
                findReplyLikeInfo(commentVo, likeVo);
            }
            comment.setReplyList(records1);
        }

        return page;
    }

    @Override
    public Long getCommentCountByTopicId(Integer type, Integer id) {

        return baseMapper.getCommentCountByTopicId(type, id);
    }

    public void findCommentLikeInfo(CommentVo comment, LikeVo likeVo) {
        //查询评论的点赞数量，点赞状态（当前用户是否对该评论进行过点赞）
        comment.setLikeCount(iLikeService.LikeCount(likeVo));
        if (likeVo.getUserId() != null) {
            comment.setLikeStatus(iLikeService.likeStatus(likeVo) ? 1 : 0);
        }
    }

    public void findReplyLikeInfo(CommentVo reply, LikeVo likeVo) {
        reply.setLikeCount(iLikeService.LikeCount(likeVo));
        if (likeVo.getUserId() != null) {
            reply.setLikeStatus(iLikeService.likeStatus(likeVo) ? 1 : 0);
        }
    }
}
