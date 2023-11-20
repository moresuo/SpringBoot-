package com.yhk.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhk.reggie.bean.AddressBook;
import com.yhk.reggie.mapper.AddressBookMapper;
import com.yhk.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @Author moresuo
 * @Date 2023/6/15 14:57
 * @注释
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
