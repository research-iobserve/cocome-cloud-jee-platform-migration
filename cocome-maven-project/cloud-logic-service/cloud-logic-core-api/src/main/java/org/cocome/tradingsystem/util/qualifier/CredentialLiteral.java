package org.cocome.tradingsystem.util.qualifier;

import javax.enterprise.util.AnnotationLiteral;

import org.cocome.tradingsystem.inventory.application.usermanager.CredentialType;

public class CredentialLiteral extends AnnotationLiteral<Credential> implements Credential {
	private static final long serialVersionUID = 1L;

	private final CredentialType value;

	public CredentialLiteral(final CredentialType value) {
		this.value = value;
	}

	@Override
	public CredentialType value() {
		return this.value;
	}

}
