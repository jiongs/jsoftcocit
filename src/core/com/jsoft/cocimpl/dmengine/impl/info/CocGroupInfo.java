// $codepro.audit.disable unnecessaryCast
package com.jsoft.cocimpl.dmengine.impl.info;

import java.util.ArrayList;
import java.util.List;

import com.jsoft.cocit.baseentity.coc.ICocGroupEntity;
import com.jsoft.cocit.dmengine.info.ICocEntityInfo;
import com.jsoft.cocit.dmengine.info.ICocFieldInfo;
import com.jsoft.cocit.dmengine.info.ICocGroupInfo;

public class CocGroupInfo extends NamedEntityInfo<ICocGroupEntity> implements ICocGroupInfo {
	private ICocEntityInfo module;

	private List<ICocFieldInfo> fields;

	CocGroupInfo(ICocGroupEntity obj, ICocEntityInfo module) {
		super(obj);
		this.module = module;
		fields = new ArrayList();
	}

	@Override
	public void release() {
		super.release();

		this.module = null;
		if (fields != null) {
			fields.clear();
			fields = null;
		}
	}

	void addField(ICocFieldInfo f) {
		this.fields.add(f);
	}

	public String getCocEntityCode() {
		return entityData.getCocEntityCode();
	}

	public String getMode() {
		return entityData.getMode();
	}

	public ICocEntityInfo getEntity() {
		return module;
	}

	public List<ICocFieldInfo> getFields() {
		return fields;
	}

	public String getModeByAction(String actionMode) {
		// return moduleEngine.parseMode(actionMode, mode);
		return getMode();
	}
}
