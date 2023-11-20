package com.yhk.reggie.dto;

import com.yhk.reggie.bean.Setmeal;
import com.yhk.reggie.bean.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
