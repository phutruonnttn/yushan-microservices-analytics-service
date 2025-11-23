package com.yushan.analytics_service.repository;

import com.yushan.analytics_service.entity.History;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for History aggregate.
 * Abstracts data access operations for History entity.
 */
public interface HistoryRepository {
    
    // Basic CRUD operations
    History findById(Integer id);
    
    History save(History history);
    
    void delete(Integer id);
    
    // Find by foreign keys
    History findByUserAndNovel(UUID userId, Integer novelId);
    
    // Paginated queries
    List<History> findByUserIdWithPagination(UUID userId, int offset, int size);
    
    // Count queries
    long countByUserId(UUID userId);
    
    // Delete operations
    void deleteByUserId(UUID userId);
}

