package com.yhk.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhk.reggie.bean.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * @version 1.0
 * @Author moresuo
 * @Date 2023/6/15 14:56
 * @注释
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
