package com.yushan.analytics_service.repository.impl;

import com.yushan.analytics_service.dao.AnalyticsMapper;
import com.yushan.analytics_service.dto.AnalyticsTrendResponseDTO;
import com.yushan.analytics_service.dto.DailyActiveUsersResponseDTO;
import com.yushan.analytics_service.dto.ReadingActivityResponseDTO;
import com.yushan.analytics_service.repository.AnalyticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public class MyBatisAnalyticsRepository implements AnalyticsRepository {

    private final AnalyticsMapper analyticsMapper;

    @Autowired
    public MyBatisAnalyticsRepository(AnalyticsMapper analyticsMapper) {
        this.analyticsMapper = analyticsMapper;
    }

    @Override
    public List<AnalyticsTrendResponseDTO.TrendDataPoint> getUserActivityTrends(
            Date startDate, Date endDate, String period) {
        return analyticsMapper.getUserActivityTrends(startDate, endDate, period);
    }

    @Override
    public List<ReadingActivityResponseDTO.ActivityDataPoint> getReadingActivityTrends(
            Date startDate, Date endDate, String period) {
        return analyticsMapper.getReadingActivityTrends(startDate, endDate, period);
    }

    @Override
    public Long getActiveUserCount(Date startDate, Date endDate) {
        return analyticsMapper.getActiveUserCount(startDate, endDate);
    }

    @Override
    public Long getDailyActiveUsers(Date date) {
        return analyticsMapper.getDailyActiveUsers(date);
    }

    @Override
    public Long getWeeklyActiveUsers(Date startDate, Date endDate) {
        return analyticsMapper.getWeeklyActiveUsers(startDate, endDate);
    }

    @Override
    public Long getMonthlyActiveUsers(Date startDate, Date endDate) {
        return analyticsMapper.getMonthlyActiveUsers(startDate, endDate);
    }

    @Override
    public List<DailyActiveUsersResponseDTO.ActivityDataPoint> getHourlyActiveUsers(Date date) {
        return analyticsMapper.getHourlyActiveUsers(date);
    }

    @Override
    public Long getUniqueNovelsRead(Date startDate, Date endDate) {
        return analyticsMapper.getUniqueNovelsRead(startDate, endDate);
    }

    @Override
    public Long getTotalReadingSessions(Date startDate, Date endDate) {
        return analyticsMapper.getTotalReadingSessions(startDate, endDate);
    }

    @Override
    public List<Integer> getMostReadNovelIds(Integer limit) {
        return analyticsMapper.getMostReadNovelIds(limit);
    }

    @Override
    public List<UUID> getMostActiveUserIds(Integer limit) {
        return analyticsMapper.getMostActiveUserIds(limit);
    }

    @Override
    public List<Integer> getMostReadNovelIdsByDateRange(
            Date startDate, Date endDate, Integer limit) {
        return analyticsMapper.getMostReadNovelIdsByDateRange(startDate, endDate, limit);
    }
}

