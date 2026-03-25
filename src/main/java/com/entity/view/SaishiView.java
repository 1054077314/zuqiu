package com.entity.view;

import com.annotation.ColumnInfo;
import com.entity.SaishiEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;

/**
 * 赛事视图
 */
@TableName("saishi")
public class SaishiView extends SaishiEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ColumnInfo(comment="赛事类型值", type="varchar(200)")
    private String saishiValue;

    public SaishiView() {
    }

    public SaishiView(SaishiEntity saishiEntity) {
        try {
            BeanUtils.copyProperties(this, saishiEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public String getSaishiValue() {
        return saishiValue;
    }

    public void setSaishiValue(String saishiValue) {
        this.saishiValue = saishiValue;
    }

    @Override
    public String toString() {
        return "SaishiView{" +
            ", saishiValue=" + saishiValue +
            "} " + super.toString();
    }
}
