package com.jsoft.cocimpl.dmengine.impl.info;

import com.jsoft.cocit.baseentity.config.IDicItemEntity;
import com.jsoft.cocit.dmengine.info.IDicInfo;
import com.jsoft.cocit.dmengine.info.IDicItemInfo;

/**
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
public class DicItemInfo extends NamedEntityInfo<IDicItemEntity> implements IDicItemInfo {

	private IDicInfo dic;

	DicItemInfo(IDicItemEntity obj, IDicInfo dic) {
		super(obj);
		this.dic = dic;
	}

	@Override
	public void release() {
		super.release();

		this.dic = null;
	}

	public String getDicCode() {
		return entityData.getDicCode();
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

	public IDicInfo getDic() {
		return dic;
	}

	@Override
	public String getValueText() {
		return entityData.getValue();
	}

}
