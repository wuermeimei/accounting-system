package com.example.accounting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.accounting.entity.Record;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface RecordMapper extends BaseMapper<Record> {
    BigDecimal sumAmountByUserIdAndTypeAndDateRange(
            @Param("userId") Long userId,
            @Param("type") String type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    List<Map<String, Object>> statisticsByCategory(
            @Param("userId") Long userId,
            @Param("type") String type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    List<Map<String, Object>> monthlyStatistics(
            @Param("userId") Long userId,
            @Param("year") Integer year);
}