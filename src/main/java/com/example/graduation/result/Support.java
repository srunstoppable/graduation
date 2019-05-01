package com.example.graduation.result;

import com.example.graduation.entity.Lot;
import com.example.graduation.entity.Park;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author s r
 * @date 2019/4/19
 */
@Data
@Accessors(chain = true)
public class Support {
    private List<Park> park;
    private int vertex;
    private List path;
    private List<Double> length;
    private List<Lot> lot;


}
