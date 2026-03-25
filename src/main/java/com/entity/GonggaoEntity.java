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
 * 公告信息
 */
@TableName("gonggao")
public class GonggaoEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;


	public GonggaoEntity() {

	}

	public GonggaoEntity(T t) {
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


    @ColumnInfo(comment="公告名称",type="varchar(200)")
    @TableField(value = "gonggao_name")

    private String gonggaoName;


    @ColumnInfo(comment="公告图片",type="varchar(200)")
    @TableField(value = "gonggao_photo")

    private String gonggaoPhoto;


    @ColumnInfo(comment="公告类型",type="int(11)")
    @TableField(value = "gonggao_types")

    private Integer gonggaoTypes;


    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @ColumnInfo(comment="发布时间",type="timestamp")
    @TableField(value = "insert_time",fill = FieldFill.INSERT)

    private Date insertTime;


    @ColumnInfo(comment="公告详情",type="longtext")
    @TableField(value = "gonggao_content")

    private String gonggaoContent;


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
    public String getGonggaoName() {
        return gonggaoName;
    }
    public void setGonggaoName(String gonggaoName) {
        this.gonggaoName = gonggaoName;
    }
    public String getGonggaoPhoto() {
        return gonggaoPhoto;
    }
    public void setGonggaoPhoto(String gonggaoPhoto) {
        this.gonggaoPhoto = gonggaoPhoto;
    }
    public Integer getGonggaoTypes() {
        return gonggaoTypes;
    }
    public void setGonggaoTypes(Integer gonggaoTypes) {
        this.gonggaoTypes = gonggaoTypes;
    }
    public Date getInsertTime() {
        return insertTime;
    }
    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
    public String getGonggaoContent() {
        return gonggaoContent;
    }
    public void setGonggaoContent(String gonggaoContent) {
        this.gonggaoContent = gonggaoContent;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Gonggao{" +
            ", id=" + id +
            ", gonggaoName=" + gonggaoName +
            ", gonggaoPhoto=" + gonggaoPhoto +
            ", gonggaoTypes=" + gonggaoTypes +
            ", insertTime=" + DateUtil.convertString(insertTime,"yyyy-MM-dd") +
            ", gonggaoContent=" + gonggaoContent +
            ", createTime=" + DateUtil.convertString(createTime,"yyyy-MM-dd") +
        "}";
    }
}

