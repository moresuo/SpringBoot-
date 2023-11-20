package com.yhk.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yhk.reggie.bean.Category;
import com.yhk.reggie.bean.Setmeal;
import com.yhk.reggie.common.R;
import com.yhk.reggie.dto.SetmealDto;
import com.yhk.reggie.service.CategoryService;
import com.yhk.reggie.service.SetmealDishService;
import com.yhk.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @Author moresuo
 * @Date 2023/6/13 17:34
 * @注释
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 保存套餐信息
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);
        return R.success("套餐保存成功");
    }

    /**
     * 套餐管理分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //构造分页对象
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        //返回分页对象
        Page<SetmealDto> pageDto = new Page<>(page, pageSize);
        //构造条件对象
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper();
        //设置条件
        queryWrapper.like(name!=null,Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, queryWrapper);
        //分页对象拷贝
        BeanUtils.copyProperties(pageInfo,pageDto,"records");//拷贝pageInfo的属性除了records
        //获取records对象
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list=records.stream().map((item)->{
            //创建dto对象
            SetmealDto setmealDto=new SetmealDto();
            //获取套餐分类id
            Long categoryId = item.getCategoryId();
            //根据套餐分类id得到分类对象
            Category category = categoryService.getById(categoryId);
            //获取分类名称
            String categoryName = category.getName();
            //将item拷贝给dto
            BeanUtils.copyProperties(item,setmealDto);
            //将分类名称赋给dto对象
            setmealDto.setCategoryName(categoryName);
            return setmealDto;
        }).collect(Collectors.toList());
        //设置pageDto的records属性
        pageDto.setRecords(list);
        return R.success(pageDto);
    }

    /**
     * 根据http参数id集合删除套餐，以及套餐菜品关联表
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.deleteWithDish(ids);
        return R.success("删除成功");
    }

    /**
     * 修改状态
     * @param ids
     * @return
     */
    @PostMapping("/status/0")
    public R<List<Setmeal>> close(@RequestParam List<Long> ids){
        //根据id集合查找对应setmeal对象
        List<Setmeal> setmeals = setmealService.listByIds(ids);
        //修改每个对象的status
        setmeals=setmeals.stream().map((item)->{
            item.setStatus(0);
            return item;
        }).collect(Collectors.toList());
        //修改数据库
        setmealService.updateBatchById(setmeals);
        return R.success(setmeals);
    }

    /**
     * 修改状态
     * @param ids
     * @return
     */
    @PostMapping("/status/1")
    public R<List<Setmeal>> open(@RequestParam List<Long> ids){
        //根据id集合查找对应setmeal对象
        List<Setmeal> setmeals = setmealService.listByIds(ids);
        //修改每个对象的status
        setmeals=setmeals.stream().map((item)->{
            item.setStatus(1);
            return item;
        }).collect(Collectors.toList());
        //修改数据库
        setmealService.updateBatchById(setmeals);
        return R.success(setmeals);
    }

    /**
     * 回显要修改的数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable Long id){
        SetmealDto setmealDto=setmealService.getByIdWithDish(id);
        return R.success(setmealDto);
    }

    /**
     * 修改套餐
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.updateWithDish(setmealDto);
        return R.success("修改成功");
    }

    /**
     * 展示套餐分类
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        //构造条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        //设置条件
        queryWrapper.eq(Setmeal::getCategoryId, setmeal.getCategoryId());
        //只展示为起售状态的套餐
        queryWrapper.eq(Setmeal::getStatus,1);
        //排序条件
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        //执行查询
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);

    }

}
