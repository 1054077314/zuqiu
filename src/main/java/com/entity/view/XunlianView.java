package com.entity.view;

import com.annotation.ColumnInfo;
import com.entity.XunlianEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;

/**
 * 训练计划视图
 */
@TableName("xunlian")
public class XunlianView extends XunlianEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@ColumnInfo(comment="训练计划类型值",type="varchar(200)")
	private String xunlianValue;

		@ColumnInfo(comment="用户编号",type="varchar(200)")
	private String yonghuUuidNumber;
		@ColumnInfo(comment="用户姓名",type="varchar(200)")
	private String yonghuName;
		@ColumnInfo(comment="用户手机号",type="varchar(200)")
	private String yonghuPhone;
		@ColumnInfo(comment="用户身份证号",type="varchar(200)")
	private String yonghuIdNumber;
		@ColumnInfo(comment="用户头像",type="varchar(200)")
	private String yonghuPhoto;
		@ColumnInfo(comment="用户邮箱",type="varchar(200)")
	private String yonghuEmail;

	public XunlianView() {

	}

	public XunlianView(XunlianEntity xunlianEntity) {
		try {
			BeanUtils.copyProperties(this, xunlianEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public String getXunlianValue() {
		return xunlianValue;
	}
	public void setXunlianValue(String xunlianValue) {
		this.xunlianValue = xunlianValue;
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

		public String getYonghuEmail() {
			return yonghuEmail;
		}
		public void setYonghuEmail(String yonghuEmail) {
			this.yonghuEmail = yonghuEmail;
		}

	@Override
	public String toString() {
		return "XunlianView{" +
			", xunlianValue=" + xunlianValue +
			", yonghuUuidNumber=" + yonghuUuidNumber +
			", yonghuName=" + yonghuName +
			", yonghuPhone=" + yonghuPhone +
			", yonghuIdNumber=" + yonghuIdNumber +
			", yonghuPhoto=" + yonghuPhoto +
			", yonghuEmail=" + yonghuEmail +
			"} " + super.toString();
	}
}

