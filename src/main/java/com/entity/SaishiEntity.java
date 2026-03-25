package com.entity;

import com.annotation.ColumnInfo;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.utils.DateUtil;

/**
 * 赛事
 */
@TableName("saishi")
public class SaishiEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public SaishiEntity() {
    }

    public SaishiEntity(T t) {
        try {
            BeanUtils.copyProperties(this, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @TableId(type = IdType.AUTO)
    @ColumnInfo(comment = "主键", type = "int(11)")
    @TableField(value = "id")
    private Integer id;

    @ColumnInfo(comment = "赛事名称", type = "varchar(200)")
    @TableField(value = "saishi_name")
    private String saishiName;

    @ColumnInfo(comment = "赛事图片", type = "varchar(200)")
    @TableField(value = "saishi_photo")
    private String saishiPhoto;

    @ColumnInfo(comment = "赛事地点", type = "varchar(200)")
    @TableField(value = "saishi_address")
    private String saishiAddress;

    @ColumnInfo(comment = "赛事类型", type = "int(11)")
    @TableField(value = "saishi_types")
    private Integer saishiTypes;

    @ColumnInfo(comment = "赛事介绍", type = "longtext")
    @TableField(value = "saishi_content")
    private String saishiContent;

    @ColumnInfo(comment = "逻辑删除", type = "int(11)")
    @TableField(value = "saishi_delete")
    private Integer saishiDelete;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    @ColumnInfo(comment = "录入时间", type = "timestamp")
    @TableField(value = "insert_time", fill = FieldFill.INSERT)
    private Date insertTime;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    @ColumnInfo(comment = "创建时间", type = "timestamp")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSaishiName() {
        return saishiName;
    }

    public void setSaishiName(String saishiName) {
        this.saishiName = saishiName;
    }

    public String getSaishiPhoto() {
        return saishiPhoto;
    }

    public void setSaishiPhoto(String saishiPhoto) {
        this.saishiPhoto = saishiPhoto;
    }

    public String getSaishiAddress() {
        return saishiAddress;
    }

    public void setSaishiAddress(String saishiAddress) {
        this.saishiAddress = saishiAddress;
    }

    public Integer getSaishiTypes() {
        return saishiTypes;
    }

    public void setSaishiTypes(Integer saishiTypes) {
        this.saishiTypes = saishiTypes;
    }

    public String getSaishiContent() {
        return saishiContent;
    }

    public void setSaishiContent(String saishiContent) {
        this.saishiContent = saishiContent;
    }

    public Integer getSaishiDelete() {
        return saishiDelete;
    }

    public void setSaishiDelete(Integer saishiDelete) {
        this.saishiDelete = saishiDelete;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Saishi{" +
            ", id=" + id +
            ", saishiName=" + saishiName +
            ", saishiPhoto=" + saishiPhoto +
            ", saishiAddress=" + saishiAddress +
            ", saishiTypes=" + saishiTypes +
            ", saishiContent=" + saishiContent +
            ", saishiDelete=" + saishiDelete +
            ", insertTime=" + DateUtil.convertString(insertTime, "yyyy-MM-dd") +
            ", createTime=" + DateUtil.convertString(createTime, "yyyy-MM-dd") +
            "}";
    }
}
