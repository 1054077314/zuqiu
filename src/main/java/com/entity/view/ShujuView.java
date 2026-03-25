package com.entity.view;

import com.annotation.ColumnInfo;
import com.entity.ShujuEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;

/**
 * 球员数据视图
 */
@TableName("shuju")
public class ShujuView extends ShujuEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@ColumnInfo(comment="球员数据类型值",type="varchar(200)")
	private String shujuValue;

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

	public ShujuView() {

	}

	public ShujuView(ShujuEntity shujuEntity) {
		try {
			BeanUtils.copyProperties(this, shujuEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public String getShujuValue() {
		return shujuValue;
	}
	public void setShujuValue(String shujuValue) {
		this.shujuValue = shujuValue;
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
		return "ShujuView{" +
			", shujuValue=" + shujuValue +
			", yonghuUuidNumber=" + yonghuUuidNumber +
			", yonghuName=" + yonghuName +
			", yonghuPhone=" + yonghuPhone +
			", yonghuIdNumber=" + yonghuIdNumber +
			", yonghuPhoto=" + yonghuPhoto +
			", yonghuEmail=" + yonghuEmail +
			"} " + super.toString();
	}
}

