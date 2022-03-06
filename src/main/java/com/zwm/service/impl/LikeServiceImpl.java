package com.zwm.service.impl;

import com.zwm.service.LikeService;
import com.zwm.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    //实现给帖子或者评论点赞功能
    public void like(int userId, int entityType, int entityId) {
        String entityKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        //是点赞还是取消点赞？
        boolean isLike = redisTemplate.opsForSet().isMember(entityKey, userId);
        if (isLike) {
            //说明已经点赞了，所以这里是取消点赞
            redisTemplate.opsForSet().remove(entityKey, userId);
        } else {
            //说明需要点赞
            //这里表示在 entityKey 中存放了
            redisTemplate.opsForSet().add(entityKey, userId);
        }
    }

    //点赞的数量，点赞上述已经完成，这里需要查询点赞的数量
    public long findLikeNumbers(int entityType, int entityId) {
        //返回点赞的数据
        return redisTemplate.opsForSet().size(RedisKeyUtil.getEntityLikeKey(entityType, entityId));
    }

    //点赞的状态，查询某个用户是否点赞了，如果点赞前端将显示不同的样式
    /*public boolean findLikeStatus(int userId, int entityType, int entityId) {
        return redisTemplate.opsForSet().isMember(RedisKeyUtil.getEntityLikeKey(entityType, entityId), userId);
    }*/

    //试想，点赞的状态只有点赞这种吗？难道我不能点踩吗？所以用 boolean 数据类型无法满足需求，这里可以巧妙地转换成 int 类型，根据不同的数字判断是点赞还是点踩等
    public int findLikeStatus(int userId, int entityType, int entityId) {
        return redisTemplate.opsForSet().isMember(RedisKeyUtil.getEntityLikeKey(entityType, entityId), userId) ? 1 : 0;
    }
}