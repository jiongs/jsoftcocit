package com.jsoft.cocimpl.entityengine.impl.service;

import com.jsoft.cocit.entity.config.IDicItem;
import com.jsoft.cocit.entityengine.service.DicItemService;
import com.jsoft.cocit.entityengine.service.DicService;

/**
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
public class DicItemServiceImpl extends NamedEntityServiceImpl<IDicItem> implements DicItemService {

	private DicService dic;

	DicItemServiceImpl(IDicItem obj, DicService dic) {
		super(obj);
		this.dic = dic;
	}

	@Override
	public void release() {
		super.release();

		this.dic = null;
	}

	public String getDicKey() {
		return entityData.getDicKey();
	}

	//
	// public String getDicName() {
	// return entity.getDicName();
	// }

	public String getText() {
		return entityData.getText();
	}

	public String getValue() {
		return entityData.getValue();
	}

	// public String getDescription() {
	// return entity.getDescription();
	// }

	public DicService getDic() {
		return dic;
	}

}
