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
 * 字典
 */
@TableName("dictionary")
public class DictionaryEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;


	public DictionaryEntity() {

	}

	public DictionaryEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}


    @TableId(type = IdType.AUTO)
    @ColumnInfo(comment="主键",type="bigint(20)")
    @TableField(value = "id")

    private Long id;


    @ColumnInfo(comment="字段",type="varchar(200)")
    @TableField(value = "dic_code")

    private String dicCode;


    @ColumnInfo(comment="字段名",type="varchar(200)")
    @TableField(value = "dic_name")

    private String dicName;


    @ColumnInfo(comment="编码",type="int(11)")
    @TableField(value = "code_index")

    private Integer codeIndex;


    @ColumnInfo(comment="编码名称",type="varchar(200)")
    @TableField(value = "index_name")

    private String indexName;


    @ColumnInfo(comment="父级ID",type="int(11)")
    @TableField(value = "super_id")

    private Integer superId;


    @ColumnInfo(comment="备注",type="varchar(200)")
    @TableField(value = "beizhu")

    private String beizhu;


    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @ColumnInfo(comment="创建时间",type="timestamp")
    @TableField(value = "create_time",fill = FieldFill.INSERT)

    private Date createTime;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDicCode() {
        return dicCode;
    }
    public void setDicCode(String dicCode) {
        this.dicCode = dicCode;
    }
    public String getDicName() {
        return dicName;
    }
    public void setDicName(String dicName) {
        this.dicName = dicName;
    }
    public Integer getCodeIndex() {
        return codeIndex;
    }
    public void setCodeIndex(Integer codeIndex) {
        this.codeIndex = codeIndex;
    }
    public String getIndexName() {
        return indexName;
    }
    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }
    public Integer getSuperId() {
        return superId;
    }
    public void setSuperId(Integer superId) {
        this.superId = superId;
    }
    public String getBeizhu() {
        return beizhu;
    }
    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Dictionary{" +
            ", id=" + id +
            ", dicCode=" + dicCode +
            ", dicName=" + dicName +
            ", codeIndex=" + codeIndex +
            ", indexName=" + indexName +
            ", superId=" + superId +
            ", beizhu=" + beizhu +
            ", createTime=" + DateUtil.convertString(createTime,"yyyy-MM-dd") +
        "}";
    }
}

