package com.entity.view;

import com.annotation.ColumnInfo;
import com.entity.HetongEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;

/**
 * 合同视图
 */
@TableName("hetong")
public class HetongView extends HetongEntity implements Serializable {
	private static final long serialVersionUID = 1L;

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

	public HetongView() {

	}

	public HetongView(HetongEntity hetongEntity) {
		try {
			BeanUtils.copyProperties(this, hetongEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
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
		return "HetongView{" +
			", yonghuUuidNumber=" + yonghuUuidNumber +
			", yonghuName=" + yonghuName +
			", yonghuPhone=" + yonghuPhone +
			", yonghuIdNumber=" + yonghuIdNumber +
			", yonghuPhoto=" + yonghuPhoto +
			", yonghuEmail=" + yonghuEmail +
			"} " + super.toString();
	}
}

