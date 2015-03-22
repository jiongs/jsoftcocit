// $codepro.audit.disable unnecessaryCast
package com.jsoft.cocimpl.entityengine.impl.service;

import java.util.ArrayList;
import java.util.List;

import com.jsoft.cocit.entity.coc.ICocGroup;
import com.jsoft.cocit.entityengine.service.CocEntityService;
import com.jsoft.cocit.entityengine.service.CocFieldService;
import com.jsoft.cocit.entityengine.service.CocGroupService;

public class CocGroupServiceImpl extends NamedEntityServiceImpl<ICocGroup> implements CocGroupService {
	private CocEntityService module;

	private List<CocFieldService> fields;

	CocGroupServiceImpl(ICocGroup obj, CocEntityService module) {
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

	void addField(CocFieldService f) {
		this.fields.add(f);
	}

	public String getCocEntityKey() {
		return entityData.getCocEntityKey();
	}

	public String getMode() {
		return entityData.getMode();
	}

	public CocEntityService getEntity() {
		return module;
	}

	public List<CocFieldService> getFields() {
		return fields;
	}

	public String getModeByAction(String actionMode) {
		// return moduleEngine.parseMode(actionMode, mode);
		return getMode();
	}
}
