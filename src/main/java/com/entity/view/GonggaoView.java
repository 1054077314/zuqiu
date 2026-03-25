package com.entity.view;

import com.annotation.ColumnInfo;
import com.entity.GonggaoEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;

/**
 * 公告信息视图
 */
@TableName("gonggao")
public class GonggaoView extends GonggaoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ColumnInfo(comment="公告类型值", type="varchar(200)")
    private String gonggaoValue;

    public GonggaoView() {
    }

    public GonggaoView(GonggaoEntity gonggaoEntity) {
        try {
            BeanUtils.copyProperties(this, gonggaoEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public String getGonggaoValue() {
        return gonggaoValue;
    }

    public void setGonggaoValue(String gonggaoValue) {
        this.gonggaoValue = gonggaoValue;
    }

    @Override
    public String toString() {
        return "GonggaoView{" +
            ", gonggaoValue=" + gonggaoValue +
            "} " + super.toString();
    }
}
