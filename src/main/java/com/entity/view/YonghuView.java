package com.entity.view;

import com.annotation.ColumnInfo;
import com.entity.YonghuEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;

/**
 * 用户视图
 */
@TableName("yonghu")
public class YonghuView extends YonghuEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ColumnInfo(comment="性别值", type="varchar(200)")
    private String sexValue;

    public YonghuView() {
    }

    public YonghuView(YonghuEntity yonghuEntity) {
        try {
            BeanUtils.copyProperties(this, yonghuEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public String getSexValue() {
        return sexValue;
    }

    public void setSexValue(String sexValue) {
        this.sexValue = sexValue;
    }

    @Override
    public String toString() {
        return "YonghuView{" +
            ", sexValue=" + sexValue +
            "} " + super.toString();
    }
}
