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
 * 用户
 */
@TableName("yonghu")
public class YonghuEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;


	public YonghuEntity() {

	}

	public YonghuEntity(T t) {
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


    @ColumnInfo(comment="账号",type="varchar(200)")
    @TableField(value = "username")

    private String username;


    @ColumnInfo(comment="密码",type="varchar(200)")
    @TableField(value = "password")

    private String password;


    @ColumnInfo(comment="用户编号",type="varchar(200)")
    @TableField(value = "yonghu_uuid_number")

    private String yonghuUuidNumber;


    @ColumnInfo(comment="用户姓名",type="varchar(200)")
    @TableField(value = "yonghu_name")

    private String yonghuName;


    @ColumnInfo(comment="用户手机号",type="varchar(200)")
    @TableField(value = "yonghu_phone")

    private String yonghuPhone;


    @ColumnInfo(comment="用户身份证号",type="varchar(200)")
    @TableField(value = "yonghu_id_number")

    private String yonghuIdNumber;


    @ColumnInfo(comment="用户头像",type="varchar(200)")
    @TableField(value = "yonghu_photo")

    private String yonghuPhoto;


    @ColumnInfo(comment="性别",type="int(11)")
    @TableField(value = "sex_types")

    private Integer sexTypes;


    @ColumnInfo(comment="用户邮箱",type="varchar(200)")
    @TableField(value = "yonghu_email")

    private String yonghuEmail;


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
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getYonghuUuidNumber() {
        return yonghuUuidNumber;
    }
    public void setYonghuUuidNumber(String yonghuUuidNumber) {
        this.yonghuUuidNumber = yonghuUuidNumber;
    }
    public String getYonghuName() {
        return yonghuName;
    }
    public void setYonghuName(String yonghuName) {
        this.yonghuName = yonghuName;
    }
    public String getYonghuPhone() {
        return yonghuPhone;
    }
    public void setYonghuPhone(String yonghuPhone) {
        this.yonghuPhone = yonghuPhone;
    }
    public String getYonghuIdNumber() {
        return yonghuIdNumber;
    }
    public void setYonghuIdNumber(String yonghuIdNumber) {
        this.yonghuIdNumber = yonghuIdNumber;
    }
    public String getYonghuPhoto() {
        return yonghuPhoto;
    }
    public void setYonghuPhoto(String yonghuPhoto) {
        this.yonghuPhoto = yonghuPhoto;
    }
    public Integer getSexTypes() {
        return sexTypes;
    }
    public void setSexTypes(Integer sexTypes) {
        this.sexTypes = sexTypes;
    }
    public String getYonghuEmail() {
        return yonghuEmail;
    }
    public void setYonghuEmail(String yonghuEmail) {
        this.yonghuEmail = yonghuEmail;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Yonghu{" +
            ", id=" + id +
            ", username=" + username +
            ", password=" + password +
            ", yonghuUuidNumber=" + yonghuUuidNumber +
            ", yonghuName=" + yonghuName +
            ", yonghuPhone=" + yonghuPhone +
            ", yonghuIdNumber=" + yonghuIdNumber +
            ", yonghuPhoto=" + yonghuPhoto +
            ", sexTypes=" + sexTypes +
            ", yonghuEmail=" + yonghuEmail +
            ", createTime=" + DateUtil.convertString(createTime,"yyyy-MM-dd") +
        "}";
    }
}

