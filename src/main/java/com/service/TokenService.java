package com.service;

import com.entity.TokenEntity;

/**
 * 令牌
 */
public interface TokenService extends LegacyIService<TokenEntity> {
    String generateToken(Integer userid, String username, String tableName, String role);

    TokenEntity getTokenEntity(String token);
}
