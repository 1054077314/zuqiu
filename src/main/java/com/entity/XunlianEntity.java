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
 * 训练计划
 */
@TableName("xunlian")
public class XunlianEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;


	public XunlianEntity() {

	}

	public XunlianEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}


    @TableId(type = IdType.AUTO)
    @ColumnInfo(comment="主键",type="int(11)")
    @TableField(value = "id")

    private Integer id;


    @ColumnInfo(comment="用户",type="int(11)")
    @TableField(value = "yonghu_id")

    private Integer yonghuId;


    @ColumnInfo(comment="训练计划名称",type="varchar(200)")
    @TableField(value = "xunlian_name")

    private String xunlianName;


    @ColumnInfo(comment="训练计划编号",type="varchar(200)")
    @TableField(value = "xunlian_uuid_number")

    private String xunlianUuidNumber;


    @ColumnInfo(comment="训练计划照片",type="varchar(200)")
    @TableField(value = "xunlian_photo")

    private String xunlianPhoto;


    @ColumnInfo(comment="训练计划类型",type="int(11)")
    @TableField(value = "xunlian_types")

    private Integer xunlianTypes;


    @ColumnInfo(comment="训练科目",type="varchar(200)")
    @TableField(value = "xunlian_kemu")

    private String xunlianKemu;


    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
	@DateTimeFormat
    @ColumnInfo(comment="日期",type="date")
    @TableField(value = "xunlian_time")

    private Date xunlianTime;


    @ColumnInfo(comment="训练计划介绍",type="longtext")
    @TableField(value = "xunlian_content")

    private String xunlianContent;


    @ColumnInfo(comment="逻辑删除",type="int(11)")
    @TableField(value = "xunlian_delete")

    private Integer xunlianDelete;


    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @ColumnInfo(comment="录入时间",type="timestamp")
    @TableField(value = "insert_time",fill = FieldFill.INSERT)

    private Date insertTime;


    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @ColumnInfo(comment="创建时间",type="timestamp")
    @TableField(value = "create_time",fill = FieldFill.INSERT)

    private Date createTime;


    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getYonghuId() {
        return yonghuId;
    }
    public void setYonghuId(Integer yonghuId) {
        this.yonghuId = yonghuId;
    }
    public String getXunlianName() {
        return xunlianName;
    }
    public void setXunlianName(String xunlianName) {
        this.xunlianName = xunlianName;
    }
    public String getXunlianUuidNumber() {
        return xunlianUuidNumber;
    }
    public void setXunlianUuidNumber(String xunlianUuidNumber) {
        this.xunlianUuidNumber = xunlianUuidNumber;
    }
    public String getXunlianPhoto() {
        return xunlianPhoto;
    }
    public void setXunlianPhoto(String xunlianPhoto) {
        this.xunlianPhoto = xunlianPhoto;
    }
    public Integer getXunlianTypes() {
        return xunlianTypes;
    }
    public void setXunlianTypes(Integer xunlianTypes) {
        this.xunlianTypes = xunlianTypes;
    }
    public String getXunlianKemu() {
        return xunlianKemu;
    }
    public void setXunlianKemu(String xunlianKemu) {
        this.xunlianKemu = xunlianKemu;
    }
    public Date getXunlianTime() {
        return xunlianTime;
    }
    public void setXunlianTime(Date xunlianTime) {
        this.xunlianTime = xunlianTime;
    }
    public String getXunlianContent() {
        return xunlianContent;
    }
    public void setXunlianContent(String xunlianContent) {
        this.xunlianContent = xunlianContent;
    }
    public Integer getXunlianDelete() {
        return xunlianDelete;
    }
    public void setXunlianDelete(Integer xunlianDelete) {
        this.xunlianDelete = xunlianDelete;
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
        return "Xunlian{" +
            ", id=" + id +
            ", yonghuId=" + yonghuId +
            ", xunlianName=" + xunlianName +
            ", xunlianUuidNumber=" + xunlianUuidNumber +
            ", xunlianPhoto=" + xunlianPhoto +
            ", xunlianTypes=" + xunlianTypes +
            ", xunlianKemu=" + xunlianKemu +
            ", xunlianTime=" + DateUtil.convertString(xunlianTime,"yyyy-MM-dd") +
            ", xunlianContent=" + xunlianContent +
            ", xunlianDelete=" + xunlianDelete +
            ", insertTime=" + DateUtil.convertString(insertTime,"yyyy-MM-dd") +
            ", createTime=" + DateUtil.convertString(createTime,"yyyy-MM-dd") +
        "}";
    }
}

