package com.yushan.analytics_service.client;

import com.yushan.analytics_service.config.FeignAuthConfig;
import com.yushan.analytics_service.dto.ApiResponse;
import com.yushan.analytics_service.dto.PageResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "engagement-service",
        url = "${services.engagement.url:http://yushan-engagement-service:8084}",
        configuration = FeignAuthConfig.class,
        fallback = EngagementServiceClient.EngagementServiceFallback.class
)
public interface EngagementServiceClient {

    Logger log = LoggerFactory.getLogger(EngagementServiceClient.class);

    @GetMapping("/api/v1/reviews/novel/{novelId}/rating-stats")
    ApiResponse<RatingStats> getNovelRatingStats(@PathVariable("novelId") Integer novelId);

    @GetMapping("/api/v1/reviews/novel/{novelId}")
    ApiResponse<PageResponseDTO<Review>> getNovelReviews(
            @PathVariable("novelId") Integer novelId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    );

    @GetMapping("/api/v1/comments/chapter/{chapterId}/statistics")
    ApiResponse<CommentStatistics> getChapterCommentStats(@PathVariable("chapterId") Integer chapterId);

    @GetMapping("/api/v1/comments/admin/statistics")
    ApiResponse<ModerationStatistics> getModerationStatistics();

    // Nested classes for response types - fields are used by Jackson for deserialization
    @SuppressWarnings({"unused", "checkstyle:VisibilityModifier"})
    class RatingStats {
        public Double averageRating;
        public Integer totalReviews;
        public Integer rating1Count;
        public Integer rating2Count;
        public Integer rating3Count;
        public Integer rating4Count;
        public Integer rating5Count;
    }

    @SuppressWarnings({"unused", "checkstyle:VisibilityModifier"})
    class Review {
        public Integer id;
        public Integer novelId;
        public String userId;
        public Integer rating;
        public String content;
        public Integer likeCount;
        public String createTime;
    }

    @SuppressWarnings({"unused", "checkstyle:VisibilityModifier"})
    class CommentStatistics {
        public Integer totalComments;
        public Integer spoilerComments;
        public Integer recentComments;
    }

    @SuppressWarnings({"unused", "checkstyle:VisibilityModifier"})
    class ModerationStatistics {
        public Long totalComments;
        public Long pendingReports;
        public Long resolvedReports;
        public Long flaggedComments;
    }


    /**
     * Fallback class for EngagementServiceClient.
     * This class will be instantiated if the engagement-service is down or responses with an error.
     */
    @Component
    class EngagementServiceFallback implements EngagementServiceClient {
        private static final Logger logger = LoggerFactory.getLogger(EngagementServiceFallback.class);

        @Override
        public ApiResponse<RatingStats> getNovelRatingStats(Integer novelId) {
            logger.error("Circuit breaker opened for engagement-service. Falling back for getNovelRatingStats request with {} id.", novelId);
            return ApiResponse.error(503, "Engagement service temporarily unavailable");
        }

        @Override
        public ApiResponse<PageResponseDTO<Review>> getNovelReviews(Integer novelId, Integer page, Integer size) {
            logger.error("Circuit breaker opened for engagement-service. Falling back for getNovelReviews request with {} id.", novelId);
            return ApiResponse.error(503, "Engagement service temporarily unavailable");
        }

        @Override
        public ApiResponse<CommentStatistics> getChapterCommentStats(Integer chapterId) {
            logger.error("Circuit breaker opened for engagement-service. Falling back for getChapterCommentStats request with {} id.", chapterId);
            return ApiResponse.error(503, "Engagement service temporarily unavailable");
        }

        @Override
        public ApiResponse<ModerationStatistics> getModerationStatistics() {
            logger.error("Circuit breaker opened for engagement-service. Falling back for getModerationStatistics request.");
            return ApiResponse.error(503, "Engagement service temporarily unavailable");
        }
    }
}
