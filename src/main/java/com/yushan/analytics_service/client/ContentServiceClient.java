package com.yushan.analytics_service.client;

import com.yushan.analytics_service.config.FeignAuthConfig;
import com.yushan.analytics_service.dto.ApiResponse;
import com.yushan.analytics_service.dto.CategoryDTO;
import com.yushan.analytics_service.dto.ChapterDTO;
import com.yushan.analytics_service.dto.NovelDetailResponseDTO;
import com.yushan.analytics_service.dto.PageResponseDTO;
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
        name = "content-service",
        url = "${services.content.url:http://yushan-content-service:8082}",
        configuration = FeignAuthConfig.class,
        fallback = ContentServiceClient.ContentServiceFallback.class
)
public interface ContentServiceClient {

    Logger log = LoggerFactory.getLogger(ContentServiceClient.class);

    @GetMapping("/api/v1/novels/{id}")
    ApiResponse<NovelDetailResponseDTO> getNovelById(@PathVariable("id") Integer id);

    @PostMapping("/api/v1/novels/batch/get")
    ApiResponse<List<NovelDetailResponseDTO>> getNovelsBatch(@RequestBody List<Integer> novelIds);

    @GetMapping("/api/v1/novels/admin/all")
    ApiResponse<PageResponseDTO<NovelDetailResponseDTO>> getNovels(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "50") Integer size,
            @RequestParam(value = "sort", defaultValue = "createTime") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order
    );

    @GetMapping("/api/v1/novels/count")
    ApiResponse<Long> getNovelCount();

    @GetMapping("/api/v1/novels/author/{authorId}")
    ApiResponse<PageResponseDTO<NovelDetailResponseDTO>> getNovelsByAuthor(
            @PathVariable("authorId") UUID authorId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "50") Integer size
    );

    @GetMapping("/api/v1/novels/category/{categoryId}")
    ApiResponse<PageResponseDTO<NovelDetailResponseDTO>> getNovelsByCategory(
            @PathVariable("categoryId") Integer categoryId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "50") Integer size
    );

    @GetMapping("/api/v1/categories/{id}")
    ApiResponse<CategoryDTO> getCategoryById(@PathVariable("id") Integer id);

    @GetMapping("/api/v1/categories")
    ApiResponse<List<CategoryDTO>> getAllCategories();

    @GetMapping("/api/v1/categories/{id}/statistics")
    ApiResponse<CategoryStatistics> getCategoryStatistics(@PathVariable("id") Integer id);

    // Chapter endpoints (needed for history service)
    @GetMapping("/api/v1/chapters/{uuid}")
    ApiResponse<ChapterDTO> getChapterByUuid(@PathVariable("uuid") UUID uuid);

    @PostMapping("/api/v1/chapters/batch/get")
    ApiResponse<List<ChapterDTO>> getChaptersBatch(@RequestBody List<Integer> chapterIds);
    
    // Nested class for category statistics
    // Fields are used by Jackson for deserialization
    @SuppressWarnings({"unused", "checkstyle:VisibilityModifier"})
    class CategoryStatistics {
        public Integer novelCount;
        public Integer totalViews;
        public Integer totalChapters;
    }

    /**
     * Fallback class for ContentServiceClient.
     * This class will be instantiated if the content-service is down or responses with an error.
     */
    @Component
    class ContentServiceFallback implements ContentServiceClient {
        private static final Logger logger = LoggerFactory.getLogger(ContentServiceFallback.class);

        @Override
        public ApiResponse<NovelDetailResponseDTO> getNovelById(Integer id) {
            logger.error("Circuit breaker opened for content-service. Falling back for getNovelById request with {} id.", id);
            return ApiResponse.error(503, "Content service temporarily unavailable");
        }

        @Override
        public ApiResponse<List<NovelDetailResponseDTO>> getNovelsBatch(List<Integer> novelIds) {
            logger.error("Circuit breaker opened for content-service. Falling back for getNovelsBatch request with {} ids.", novelIds.size());
            return ApiResponse.error(503, "Content service temporarily unavailable");
        }

        @Override
        public ApiResponse<PageResponseDTO<NovelDetailResponseDTO>> getNovels(Integer page, Integer size, String sort, String order) {
            logger.error("Circuit breaker opened for content-service. Falling back for getNovels request.");
            return ApiResponse.error(503, "Content service temporarily unavailable");
        }

        @Override
        public ApiResponse<Long> getNovelCount() {
            logger.error("Circuit breaker opened for content-service. Falling back for getNovelCount request.");
            return ApiResponse.error(503, "Content service temporarily unavailable");
        }

        @Override
        public ApiResponse<PageResponseDTO<NovelDetailResponseDTO>> getNovelsByAuthor(UUID authorId, Integer page, Integer size) {
            logger.error("Circuit breaker opened for content-service. Falling back for getNovelsByAuthor request with {} id.", authorId);
            return ApiResponse.error(503, "Content service temporarily unavailable");
        }

        @Override
        public ApiResponse<PageResponseDTO<NovelDetailResponseDTO>> getNovelsByCategory(Integer categoryId, Integer page, Integer size) {
            logger.error("Circuit breaker opened for content-service. Falling back for getNovelsByCategory request with {} id.", categoryId);
            return ApiResponse.error(503, "Content service temporarily unavailable");
        }

        @Override
        public ApiResponse<CategoryDTO> getCategoryById(Integer id) {
            logger.error("Circuit breaker opened for content-service. Falling back for getCategoryById request with {} id.", id);
            return ApiResponse.error(503, "Content service temporarily unavailable");
        }

        @Override
        public ApiResponse<List<CategoryDTO>> getAllCategories() {
            logger.error("Circuit breaker opened for content-service. Falling back for getAllCategories request.");
            return ApiResponse.error(503, "Content service temporarily unavailable");
        }

        @Override
        public ApiResponse<CategoryStatistics> getCategoryStatistics(Integer id) {
            logger.error("Circuit breaker opened for content-service. Falling back for getCategoryStatistics request with {} id.", id);
            return ApiResponse.error(503, "Content service temporarily unavailable");
        }

        @Override
        public ApiResponse<ChapterDTO> getChapterByUuid(UUID uuid) {
            logger.error("Circuit breaker opened for content-service. Falling back for getChapterByUuid request with {} id.", uuid);
            return ApiResponse.error(503, "Content service temporarily unavailable");
        }

        @Override
        public ApiResponse<List<ChapterDTO>> getChaptersBatch(List<Integer> chapterIds) {
            logger.error("Circuit breaker opened for content-service. Falling back for getChaptersBatch request with {} ids.", chapterIds.size());
            return ApiResponse.error(503, "Content service temporarily unavailable");
        }
    }
}
