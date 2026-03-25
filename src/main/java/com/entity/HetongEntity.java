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
 * 合同
 */
@TableName("hetong")
public class HetongEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;


	public HetongEntity() {

	}

	public HetongEntity(T t) {
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


    @ColumnInfo(comment="合同标题",type="varchar(200)")
    @TableField(value = "hetong_name")

    private String hetongName;


    @ColumnInfo(comment="上传合同",type="varchar(200)")
    @TableField(value = "hetong_file")

    private String hetongFile;


    @ColumnInfo(comment="备注",type="text")
    @TableField(value = "hetong_text")

    private String hetongText;


    @ColumnInfo(comment="逻辑删除",type="int(11)")
    @TableField(value = "hetong_delete")

    private Integer hetongDelete;


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
    public String getHetongName() {
        return hetongName;
    }
    public void setHetongName(String hetongName) {
        this.hetongName = hetongName;
    }
    public String getHetongFile() {
        return hetongFile;
    }
    public void setHetongFile(String hetongFile) {
        this.hetongFile = hetongFile;
    }
    public String getHetongText() {
        return hetongText;
    }
    public void setHetongText(String hetongText) {
        this.hetongText = hetongText;
    }
    public Integer getHetongDelete() {
        return hetongDelete;
    }
    public void setHetongDelete(Integer hetongDelete) {
        this.hetongDelete = hetongDelete;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Hetong{" +
            ", id=" + id +
            ", yonghuId=" + yonghuId +
            ", hetongName=" + hetongName +
            ", hetongFile=" + hetongFile +
            ", hetongText=" + hetongText +
            ", hetongDelete=" + hetongDelete +
            ", createTime=" + DateUtil.convertString(createTime,"yyyy-MM-dd") +
        "}";
    }
}

