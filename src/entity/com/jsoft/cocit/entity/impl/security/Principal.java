package com.jsoft.cocit.entity.impl.security;

import com.jsoft.cocit.entity.TenantOwnerNamedEntity;
import com.jsoft.cocit.entity.security.IPrincipal;

/**
 * 
 * @author Ji Yongshan
 * @preserve all
 *
 */
public abstract class Principal extends TenantOwnerNamedEntity implements IPrincipal {
	protected String referencedKey;

	public String getReferencedKey() {
		return referencedKey;
	}

	public void setReferencedKey(String referencedKey) {
		this.referencedKey = referencedKey;
	}

}
