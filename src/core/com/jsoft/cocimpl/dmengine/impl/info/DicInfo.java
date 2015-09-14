package com.jsoft.cocimpl.dmengine.impl.info;

import java.util.ArrayList;
import java.util.List;

import com.jsoft.cocit.baseentity.config.IDicEntity;
import com.jsoft.cocit.baseentity.config.IDicItemEntity;
import com.jsoft.cocit.constant.EntityTypes;
import com.jsoft.cocit.dmengine.info.IDicInfo;
import com.jsoft.cocit.dmengine.info.IDicItemInfo;
import com.jsoft.cocit.orm.expr.Expr;

/**
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
public class DicInfo extends NamedEntityInfo<IDicEntity> implements IDicInfo {

	private List<IDicItemInfo> items;

	DicInfo(IDicEntity obj) {
		super(obj);
	}

	@Override
	public void release() {
		super.release();

		if (items != null) {
			for (IDicItemInfo item : items) {
				item.release();
			}
			items.clear();
			items = null;
		}
	}

	private void initItems() {
		List<IDicItemEntity> list = (List<IDicItemEntity>) orm().query(EntityTypes.DicItem, Expr.eq("dicCode", this.getCode()));

		items = new ArrayList();
		for (IDicItemEntity obj : list) {
			items.add(new DicItemInfo(obj, this));
		}
	}

	public String getParentCode() {
		return entityData.getParentCode();
	}

//	public String getParentName() {
//		return entityData.getParentName();
//	}

	public List<IDicItemInfo> getItems() {
		if (items == null)
			initItems();

		return items;
	}

}
