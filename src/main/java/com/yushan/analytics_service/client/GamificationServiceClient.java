package com.yushan.analytics_service.client;

import com.yushan.analytics_service.config.FeignAuthConfig;
import com.yushan.analytics_service.dto.ApiResponse;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Feign client for Gamification Service
 */
@FeignClient(
        name = "gamification-service",
        url = "${services.gamification.url:http://yushan-gamification-service:8085}",
        configuration = FeignAuthConfig.class,
        fallback = GamificationServiceClient.GamificationServiceFallback.class
)
public interface GamificationServiceClient {

    Logger log = LoggerFactory.getLogger(GamificationServiceClient.class);

    @GetMapping("/api/v1/gamification/stats/all")
    ApiResponse<List<GamificationStats>> getAllUsersStats();

    @GetMapping("/api/v1/gamification/stats/userId/{userId}")
    ApiResponse<GamificationStats> getUserStats(@PathVariable("userId") String userId);

    @PostMapping("/api/v1/gamification/stats/batch")
    ApiResponse<List<GamificationStats>> getBatchUsersStats(@RequestBody List<String> userIds);

    // Inner class for gamification stats - fields are used by Jackson for deserialization
    @SuppressWarnings({"unused", "checkstyle:VisibilityModifier"})
    @SuppressFBWarnings(value = "UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD",
            justification = "Fields are populated by Jackson during JSON deserialization")
    class GamificationStats {
        public String userId;
        public Integer level;
        public Integer currentExp;
        public Integer totalExpForNextLevel;
        public Integer yuanBalance;
    }

    /**
     * Fallback class for GamificationServiceClient.
     * This class will be instantiated if the gamification-service is down or responses with an error.
     */
    @Component
    class GamificationServiceFallback implements GamificationServiceClient {
        private static final Logger logger = LoggerFactory.getLogger(GamificationServiceFallback.class);

        @Override
        public ApiResponse<List<GamificationStats>> getAllUsersStats() {
            logger.error("Circuit breaker opened for gamification-service. Falling back for getAllUsersStats request.");
            return ApiResponse.error(503, "Gamification service temporarily unavailable");
        }

        @Override
        public ApiResponse<GamificationStats> getUserStats(String userId) {
            logger.error("Circuit breaker opened for gamification-service. Falling back for getUserStats request with {} id.", userId);
            return ApiResponse.error(503, "Gamification service temporarily unavailable");
        }

        @Override
        public ApiResponse<List<GamificationStats>> getBatchUsersStats(List<String> userIds) {
            logger.error("Circuit breaker opened for gamification-service. Falling back for getBatchUsersStats request with {} ids.", userIds.size());
            return ApiResponse.error(503, "Gamification service temporarily unavailable");
        }
    }
}
