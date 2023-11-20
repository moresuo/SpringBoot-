package com.yhk.reggie.dto;
import com.yhk.reggie.bean.Dish;
import com.yhk.reggie.bean.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;//分类名称

    private Integer copies;
}
