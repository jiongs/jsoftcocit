package com.jsoft.cocimpl.securityengine.impl;

import java.util.Iterator;
import java.util.List;

import com.jsoft.cocit.exception.CocSecurityException;
import com.jsoft.cocit.securityengine.AccessDecisionManager;
import com.jsoft.cocit.securityengine.AccessDecisionVoter;
import com.jsoft.cocit.securityengine.AuthorizedObject;
import com.jsoft.cocit.securityengine.AuthorizedObjectDefinition;

public abstract class AbstractAccessDecisionManager implements AccessDecisionManager {

	private List<AccessDecisionVoter> decisionVoters;

	private boolean allowIfAllAbstainDecisions = false;

	public List<AccessDecisionVoter> getDecisionVoters() {
		return this.decisionVoters;
	}

	public boolean isAllowIfAllAbstainDecisions() {
		return allowIfAllAbstainDecisions;
	}

	public void setAllowIfAllAbstainDecisions(boolean allowIfAllAbstainDecisions) {
		this.allowIfAllAbstainDecisions = allowIfAllAbstainDecisions;
	}

	public void setDecisionVoters(List<AccessDecisionVoter> newList) {
		this.decisionVoters = newList;
	}

	public boolean supports(AuthorizedObjectDefinition attribute) {
		Iterator iter = this.decisionVoters.iterator();

		while (iter.hasNext()) {
			AccessDecisionVoter voter = (AccessDecisionVoter) iter.next();

			for (AuthorizedObject ao : attribute.getAuthorizedObjects()) {
				if (voter.supports(ao)) {
					return true;
				} else {
					break;
				}
			}
		}

		return false;
	}

	public boolean supports(Class clazz) {
		Iterator iter = this.decisionVoters.iterator();

		while (iter.hasNext()) {
			AccessDecisionVoter voter = (AccessDecisionVoter) iter.next();

			if (!voter.supports(clazz)) {
				return false;
			}
		}

		return true;
	}

	protected final void checkAllowIfAllAbstainDecisions() {
		if (!this.isAllowIfAllAbstainDecisions()) {
			throw new CocSecurityException("您没有足够的权限！");
		}
	}
}