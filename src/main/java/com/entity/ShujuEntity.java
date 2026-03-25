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
 * 球员数据
 */
@TableName("shuju")
public class ShujuEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;


	public ShujuEntity() {

	}

	public ShujuEntity(T t) {
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


    @ColumnInfo(comment="球员数据名称",type="varchar(200)")
    @TableField(value = "shuju_name")

    private String shujuName;


    @ColumnInfo(comment="球员数据编号",type="varchar(200)")
    @TableField(value = "shuju_uuid_number")

    private String shujuUuidNumber;


    @ColumnInfo(comment="球员数据照片",type="varchar(200)")
    @TableField(value = "shuju_photo")

    private String shujuPhoto;


    @ColumnInfo(comment="球员数据类型",type="int(11)")
    @TableField(value = "shuju_types")

    private Integer shujuTypes;


    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
	@DateTimeFormat
    @ColumnInfo(comment="日期",type="date")
    @TableField(value = "shuju_time")

    private Date shujuTime;


    @ColumnInfo(comment="球员数据介绍",type="longtext")
    @TableField(value = "shuju_content")

    private String shujuContent;


    @ColumnInfo(comment="逻辑删除",type="int(11)")
    @TableField(value = "shuju_delete")

    private Integer shujuDelete;


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
    public String getShujuName() {
        return shujuName;
    }
    public void setShujuName(String shujuName) {
        this.shujuName = shujuName;
    }
    public String getShujuUuidNumber() {
        return shujuUuidNumber;
    }
    public void setShujuUuidNumber(String shujuUuidNumber) {
        this.shujuUuidNumber = shujuUuidNumber;
    }
    public String getShujuPhoto() {
        return shujuPhoto;
    }
    public void setShujuPhoto(String shujuPhoto) {
        this.shujuPhoto = shujuPhoto;
    }
    public Integer getShujuTypes() {
        return shujuTypes;
    }
    public void setShujuTypes(Integer shujuTypes) {
        this.shujuTypes = shujuTypes;
    }
    public Date getShujuTime() {
        return shujuTime;
    }
    public void setShujuTime(Date shujuTime) {
        this.shujuTime = shujuTime;
    }
    public String getShujuContent() {
        return shujuContent;
    }
    public void setShujuContent(String shujuContent) {
        this.shujuContent = shujuContent;
    }
    public Integer getShujuDelete() {
        return shujuDelete;
    }
    public void setShujuDelete(Integer shujuDelete) {
        this.shujuDelete = shujuDelete;
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
        return "Shuju{" +
            ", id=" + id +
            ", yonghuId=" + yonghuId +
            ", shujuName=" + shujuName +
            ", shujuUuidNumber=" + shujuUuidNumber +
            ", shujuPhoto=" + shujuPhoto +
            ", shujuTypes=" + shujuTypes +
            ", shujuTime=" + DateUtil.convertString(shujuTime,"yyyy-MM-dd") +
            ", shujuContent=" + shujuContent +
            ", shujuDelete=" + shujuDelete +
            ", insertTime=" + DateUtil.convertString(insertTime,"yyyy-MM-dd") +
            ", createTime=" + DateUtil.convertString(createTime,"yyyy-MM-dd") +
        "}";
    }
}

