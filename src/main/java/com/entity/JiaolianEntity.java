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
 * 教练
 */
@TableName("jiaolian")
public class JiaolianEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;


	public JiaolianEntity() {

	}

	public JiaolianEntity(T t) {
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


    @ColumnInfo(comment="教练编号",type="varchar(200)")
    @TableField(value = "jiaolian_uuid_number")

    private String jiaolianUuidNumber;


    @ColumnInfo(comment="教练姓名",type="varchar(200)")
    @TableField(value = "jiaolian_name")

    private String jiaolianName;


    @ColumnInfo(comment="教练手机号",type="varchar(200)")
    @TableField(value = "jiaolian_phone")

    private String jiaolianPhone;


    @ColumnInfo(comment="教练身份证号",type="varchar(200)")
    @TableField(value = "jiaolian_id_number")

    private String jiaolianIdNumber;


    @ColumnInfo(comment="教练头像",type="varchar(200)")
    @TableField(value = "jiaolian_photo")

    private String jiaolianPhoto;


    @ColumnInfo(comment="性别",type="int(11)")
    @TableField(value = "sex_types")

    private Integer sexTypes;


    @ColumnInfo(comment="教练邮箱",type="varchar(200)")
    @TableField(value = "jiaolian_email")

    private String jiaolianEmail;


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
    public String getJiaolianUuidNumber() {
        return jiaolianUuidNumber;
    }
    public void setJiaolianUuidNumber(String jiaolianUuidNumber) {
        this.jiaolianUuidNumber = jiaolianUuidNumber;
    }
    public String getJiaolianName() {
        return jiaolianName;
    }
    public void setJiaolianName(String jiaolianName) {
        this.jiaolianName = jiaolianName;
    }
    public String getJiaolianPhone() {
        return jiaolianPhone;
    }
    public void setJiaolianPhone(String jiaolianPhone) {
        this.jiaolianPhone = jiaolianPhone;
    }
    public String getJiaolianIdNumber() {
        return jiaolianIdNumber;
    }
    public void setJiaolianIdNumber(String jiaolianIdNumber) {
        this.jiaolianIdNumber = jiaolianIdNumber;
    }
    public String getJiaolianPhoto() {
        return jiaolianPhoto;
    }
    public void setJiaolianPhoto(String jiaolianPhoto) {
        this.jiaolianPhoto = jiaolianPhoto;
    }
    public Integer getSexTypes() {
        return sexTypes;
    }
    public void setSexTypes(Integer sexTypes) {
        this.sexTypes = sexTypes;
    }
    public String getJiaolianEmail() {
        return jiaolianEmail;
    }
    public void setJiaolianEmail(String jiaolianEmail) {
        this.jiaolianEmail = jiaolianEmail;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Jiaolian{" +
            ", id=" + id +
            ", username=" + username +
            ", password=" + password +
            ", jiaolianUuidNumber=" + jiaolianUuidNumber +
            ", jiaolianName=" + jiaolianName +
            ", jiaolianPhone=" + jiaolianPhone +
            ", jiaolianIdNumber=" + jiaolianIdNumber +
            ", jiaolianPhoto=" + jiaolianPhoto +
            ", sexTypes=" + sexTypes +
            ", jiaolianEmail=" + jiaolianEmail +
            ", createTime=" + DateUtil.convertString(createTime,"yyyy-MM-dd") +
        "}";
    }
}

