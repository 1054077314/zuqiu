package com.entity.view;

import com.entity.DictionaryEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;

/**
 * 字典视图
 */
@TableName("dictionary")
public class DictionaryView extends DictionaryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public DictionaryView() {

	}

	public DictionaryView(DictionaryEntity dictionaryEntity) {
		try {
			BeanUtils.copyProperties(this, dictionaryEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "DictionaryView{" +
			"} " + super.toString();
	}
}

