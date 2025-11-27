package com.yushan.analytics_service.client;

import com.yushan.analytics_service.config.FeignAuthConfig;
import com.yushan.analytics_service.dto.ApiResponse;
import com.yushan.analytics_service.dto.PageResponseDTO;
import com.yushan.analytics_service.dto.UserProfileResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(
        name = "user-service",
        url = "${services.user.url:http://yushan-user-service:8081}",
        configuration = FeignAuthConfig.class,
        fallback = UserServiceClient.UserServiceFallback.class
)
public interface UserServiceClient {

    Logger log = LoggerFactory.getLogger(UserServiceClient.class);

    @GetMapping("/api/v1/users/{userId}")
    ApiResponse<UserProfileResponseDTO> getUser(@PathVariable("userId") UUID userId);

    @PostMapping("/api/v1/users/batch/get")
    ApiResponse<List<UserProfileResponseDTO>> getUsersBatch(@RequestBody List<UUID> userIds);

    @GetMapping("/api/v1/admin/users")
    ApiResponse<PageResponseDTO<UserProfileResponseDTO>> getAllUsersForRanking(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "100") Integer size,
            @RequestParam(value = "sortBy", defaultValue = "createTime") String sortBy,
            @RequestParam(value = "sortOrder", defaultValue = "desc") String sortOrder
    );

    default boolean validateUser(UUID userId) {
        try {
            ApiResponse<UserProfileResponseDTO> response = getUser(userId);
            return response != null && response.getCode() != null && response.getCode().equals(200) && response.getData() != null;
        } catch (Exception e) {
            return false;
        }
    }

    default String getUsernameById(UUID userId) {
        try {
            ApiResponse<UserProfileResponseDTO> response = getUser(userId);
            if (response != null && response.getData() != null) {
                return response.getData().getUsername();
            }
            return "Unknown User";
        } catch (Exception e) {
            log.error("Error getting username for user {}: {}", userId, e.getMessage());
            return "Unknown User";
        }
    }


    /**
     * Fallback class for UserServiceClient.
     * This class will be instantiated if the user-service is down or responses with an error.
     */
    @Component
    class UserServiceFallback implements UserServiceClient {
        private static final Logger logger = LoggerFactory.getLogger(UserServiceFallback.class);

        @Override
        public ApiResponse<UserProfileResponseDTO> getUser(UUID userId) {
            logger.error("Circuit breaker opened for user-service. Falling back for getUser request with {} id.", userId);
            return ApiResponse.error(503, "User service temporarily unavailable");
        }

        @Override
        public ApiResponse<List<UserProfileResponseDTO>> getUsersBatch(List<UUID> userIds) {
            logger.error("Circuit breaker opened for user-service. Falling back for getUsersBatch request with {} ids.", userIds.size());
            return ApiResponse.error(503, "User service temporarily unavailable");
        }

        @Override
        public ApiResponse<PageResponseDTO<UserProfileResponseDTO>> getAllUsersForRanking(Integer page, Integer size, String sortBy, String sortOrder) {
            logger.error("Circuit breaker opened for user-service. Falling back for getAllUsersForRanking request.");
            return ApiResponse.error(503, "User service temporarily unavailable");
        }
    }
}