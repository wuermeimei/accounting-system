package com.example.accounting.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.accounting.dto.RecordCreateRequest;
import com.example.accounting.dto.RecordQueryRequest;
import com.example.accounting.dto.RecordUpdateRequest;
import com.example.accounting.entity.Record;
import com.example.accounting.mapper.RecordMapper;
import com.example.accounting.vo.RecordVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecordService {

    @Autowired
    private RecordMapper recordMapper;

    @Transactional
    public Long createRecord(Long userId, RecordCreateRequest request) {
        Record record = new Record();
        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory());
        record.setDescription(request.getDescription());
        record.setUserId(userId);

        // 处理创建时间
        if (request.getCreateDate() != null) {
            record.setCreateTime(request.getCreateDate().atStartOfDay());
        } else {
            record.setCreateTime(LocalDateTime.now());
        }
        record.setUpdateTime(LocalDateTime.now());

        recordMapper.insert(record);
        return record.getId();
    }

    @Transactional(readOnly = true)
    public List<RecordVO> getRecords(Long userId, RecordQueryRequest request) {
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("deleted", 0);

        if (request.getType() != null) {
            queryWrapper.eq("type", request.getType());
        }
        if (request.getCategory() != null) {
            queryWrapper.eq("category", request.getCategory());
        }
        if (request.getStartDate() != null) {
            queryWrapper.ge("create_time", request.getStartDate());
        }
        if (request.getEndDate() != null) {
            queryWrapper.le("create_time", request.getEndDate());
        }

        queryWrapper.orderByDesc("create_time");

        List<Record> records = recordMapper.selectList(queryWrapper);

        return records.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Transactional
    public boolean updateRecord(Long userId, Long recordId, RecordUpdateRequest request) {
        Record record = recordMapper.selectById(recordId);
        if (record == null || !record.getUserId().equals(userId) || record.getDeleted() == 1) {
            return false;
        }

        BeanUtils.copyProperties(request, record);
        record.setUpdateTime(LocalDateTime.now());
        return recordMapper.updateById(record) > 0;
    }

    @Transactional
    public boolean deleteRecord(Long userId, Long recordId) {
        Record record = recordMapper.selectById(recordId);
        if (record == null || !record.getUserId().equals(userId) || record.getDeleted() == 1) {
            return false;
        }

        record.setDeleted(1);
        record.setUpdateTime(LocalDateTime.now());
        return recordMapper.updateById(record) > 0;
    }

    @Transactional(readOnly = true)
    public RecordVO getRecordById(Long userId, Long recordId) {
        Record record = recordMapper.selectById(recordId);
        if (record == null || !record.getUserId().equals(userId) || record.getDeleted() == 1) {
            return null;
        }
        return convertToVO(record);
    }

    private RecordVO convertToVO(Record record) {
        RecordVO vo = new RecordVO();
        BeanUtils.copyProperties(record, vo);
        return vo;
    }
}