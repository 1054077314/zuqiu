package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.entity.TokenEntity;

/**
 * 令牌
 */
public interface TokenService extends IService<TokenEntity> {
    String generateToken(Integer userid, String username, String tableName, String role);

    TokenEntity getTokenEntity(String token);
}
