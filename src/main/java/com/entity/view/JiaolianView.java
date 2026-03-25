package com.entity.view;

import com.annotation.ColumnInfo;
import com.entity.JiaolianEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;

/**
 * 教练视图
 */
@TableName("jiaolian")
public class JiaolianView extends JiaolianEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ColumnInfo(comment="性别值", type="varchar(200)")
    private String sexValue;

    public JiaolianView() {
    }

    public JiaolianView(JiaolianEntity jiaolianEntity) {
        try {
            BeanUtils.copyProperties(this, jiaolianEntity);
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
        return "JiaolianView{" +
            ", sexValue=" + sexValue +
            "} " + super.toString();
    }
}
