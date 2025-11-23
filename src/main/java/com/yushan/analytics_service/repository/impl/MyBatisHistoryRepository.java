package com.yushan.analytics_service.repository.impl;

import com.yushan.analytics_service.dao.HistoryMapper;
import com.yushan.analytics_service.entity.History;
import com.yushan.analytics_service.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * MyBatis implementation of HistoryRepository.
 */
@Repository
public class MyBatisHistoryRepository implements HistoryRepository {
    
    @Autowired
    private HistoryMapper historyMapper;
    
    @Override
    public History findById(Integer id) {
        return historyMapper.selectByPrimaryKey(id);
    }
    
    @Override
    public History save(History history) {
        if (history.getId() == null) {
            // Insert new history
            historyMapper.insertSelective(history);
        } else {
            // Update existing history
            historyMapper.updateByPrimaryKeySelective(history);
        }
        return history;
    }
    
    @Override
    public void delete(Integer id) {
        historyMapper.deleteByPrimaryKey(id);
    }
    
    @Override
    public History findByUserAndNovel(UUID userId, Integer novelId) {
        return historyMapper.selectByUserAndNovel(userId, novelId);
    }
    
    @Override
    public List<History> findByUserIdWithPagination(UUID userId, int offset, int size) {
        return historyMapper.selectByUserIdWithPagination(userId, offset, size);
    }
    
    @Override
    public long countByUserId(UUID userId) {
        return historyMapper.countByUserId(userId);
    }
    
    @Override
    public void deleteByUserId(UUID userId) {
        historyMapper.deleteByUserId(userId);
    }
}

