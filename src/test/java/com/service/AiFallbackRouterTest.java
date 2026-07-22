package com.service;

import com.service.ai.AiFallbackRouter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AiFallbackRouterTest {

    private final AiFallbackRouter fallbackRouter = new AiFallbackRouter();

    @Test
    void extractsPlayerKeywordFromNaturalProfileQuestion() {
        assertEquals("宋", fallbackRouter.extractKeyword("查宋的球员档案"));
    }

    @Test
    void extractsContractKeywordBeforeContractWord() {
        assertEquals("张三", fallbackRouter.extractContractKeyword("查张三的合同什么时候到期"));
    }
}
