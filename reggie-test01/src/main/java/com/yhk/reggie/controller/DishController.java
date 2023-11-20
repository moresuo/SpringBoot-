package com.yhk.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yhk.reggie.bean.Category;
import com.yhk.reggie.bean.Dish;
import com.yhk.reggie.bean.DishFlavor;
import com.yhk.reggie.common.R;
import com.yhk.reggie.dto.DishDto;
import com.yhk.reggie.service.CategoryService;
import com.yhk.reggie.service.DishFlavorService;
import com.yhk.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @Author moresuo
 * @Date 2023/6/12 16:15
 * @注释
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("信息添加成功");
    }

    /**
     * 菜品分页
     * 如果用dish对象作为分页返回对象，那么菜品分类名称那一列将无法显示
     * 因此将dishDto作为分页返回对象，既封装了dish对象，又包含category_name字段
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        //返回结果
        Page<DishDto> dishDtoPage=new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        //过滤条件
        queryWrapper.like((name != null && !"".equals(name)), Dish::getName, name);
        //排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);//根据修改时间降序排序
        //执行分页查询
        dishService.page(pageInfo, queryWrapper);
        //拷贝分页对象
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");//将pageInfo拷贝给dishDtoPage除了records属性
        //获取原records对象
        List<Dish> records = pageInfo.getRecords();
        //遍历每一条records
       List<DishDto> list= records.stream().map((item)->{
            DishDto dishDto=new DishDto();
            //将item赋给dishDto
            BeanUtils.copyProperties(item, dishDto);
            //获取菜品分类id
            Long categoryId = item.getCategoryId();
            //根据菜品分类id得到菜品分类名称
            Category category = categoryService.getById(categoryId);
            String categoryName=category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    /**
     * 根据id菜品信息及相应口味进行数据回显
     * 不能将dish对象作为返回对象，因为包含口味这一项，因此将dishDto作为返回对象
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto=dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 修改菜品，不但要修改dish表还要修改dish_flavor表
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }

    /**
     * 根据分类id获取对应分类的菜品集合
     * 菜品展示
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper();
        //设置条件
        queryWrapper.eq((dish.getCategoryId() != null), Dish::getCategoryId, dish.getCategoryId());
        //只筛选status为1的菜品（起售状态）
        queryWrapper.eq(Dish::getStatus,1);
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        //将相同分类的菜品存入同一个集合
        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtoList=list.stream().map((item)->{
            //返回对象
            DishDto dishDto=new DishDto();
            //将item拷贝给dishDto
            BeanUtils.copyProperties(item, dishDto);
            //根据分类id获取对应分类名称
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category!=null){
                //取出categoryName赋给dishDto
                dishDto.setCategoryName(category.getName());
            }
            //还要将口味集合赋给dishDto
            //根据dishId去dish_flavor表中查找对应的口味数据
            Long id = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,id);
            List<DishFlavor> dishFlavors = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavors);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }

}
