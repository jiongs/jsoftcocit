package com.jsoft.cocimpl.entityengine.impl.service;

import java.util.ArrayList;
import java.util.List;

import com.jsoft.cocit.constant.EntityTypes;
import com.jsoft.cocit.entity.config.IDic;
import com.jsoft.cocit.entity.config.IDicItem;
import com.jsoft.cocit.entityengine.service.DicItemService;
import com.jsoft.cocit.entityengine.service.DicService;
import com.jsoft.cocit.orm.expr.Expr;

/**
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
public class DicServiceImpl extends NamedEntityServiceImpl<IDic> implements DicService {

	private List<DicItemService> items;

	DicServiceImpl(IDic obj) {
		super(obj);
	}

	@Override
	public void release() {
		super.release();

		if (items != null) {
			for (DicItemService item : items) {
				item.release();
			}
			items.clear();
			items = null;
		}
	}

	private void initItems() {
		List<IDicItem> list = (List<IDicItem>) orm().query(EntityTypes.DicItem, Expr.eq("dicKey", this.getKey()));

		items = new ArrayList();
		for (IDicItem obj : list) {
			items.add(new DicItemServiceImpl(obj, this));
		}
	}

	public String getParentKey() {
		return entityData.getParentKey();
	}

	public String getParentName() {
		return entityData.getParentName();
	}

	public List<DicItemService> getItems() {
		if (items == null)
			initItems();

		return items;
	}

}
