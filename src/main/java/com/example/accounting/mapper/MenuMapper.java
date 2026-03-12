package com.example.accounting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.accounting.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    List<Menu> selectMenuTreeByUserId(Long userId);

    List<Menu> selectAllMenus();
}