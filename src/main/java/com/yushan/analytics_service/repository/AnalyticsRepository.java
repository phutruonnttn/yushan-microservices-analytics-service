package com.yushan.analytics_service.repository;

import com.yushan.analytics_service.dto.AnalyticsTrendResponseDTO;
import com.yushan.analytics_service.dto.DailyActiveUsersResponseDTO;
import com.yushan.analytics_service.dto.ReadingActivityResponseDTO;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Analytics queries.
 * This repository provides read-only analytics queries based on History aggregate.
 * Note: Analytics queries are derived from History data, not a separate aggregate.
 */
public interface AnalyticsRepository {
    
    // User activity trends (based on history table)
    List<AnalyticsTrendResponseDTO.TrendDataPoint> getUserActivityTrends(
            Date startDate,
            Date endDate,
            String period);
    
    // Reading activity trends  
    List<ReadingActivityResponseDTO.ActivityDataPoint> getReadingActivityTrends(
            Date startDate,
            Date endDate,
            String period);
    
    // Active user counts
    Long getActiveUserCount(Date startDate, Date endDate);
    
    Long getDailyActiveUsers(Date date);
    
    Long getWeeklyActiveUsers(Date startDate, Date endDate);
    
    Long getMonthlyActiveUsers(Date startDate, Date endDate);
    
    // Hourly active users breakdown
    List<DailyActiveUsersResponseDTO.ActivityDataPoint> getHourlyActiveUsers(Date date);
    
    // Unique novel counts from history
    Long getUniqueNovelsRead(Date startDate, Date endDate);
    
    // Total history records (reading sessions)
    Long getTotalReadingSessions(Date startDate, Date endDate);
    
    // Most read novels from history
    List<Integer> getMostReadNovelIds(Integer limit);
    
    // Most active users
    List<UUID> getMostActiveUserIds(Integer limit);
    
    // Most read novels in date range
    List<Integer> getMostReadNovelIdsByDateRange(
            Date startDate, 
            Date endDate,
            Integer limit);
}

