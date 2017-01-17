package org.cocome.cloud.registry.service;

import java.net.URI;
import java.rmi.NotBoundException;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;

@WebService(serviceName = "IURIRegistryManagerService", name = "IURIRegistryManager", endpointInterface = "org.cocome.cloud.registry.service.IURIRegistryManager", targetNamespace = "http://registry.webservice.logic.cocome.org/")
@Stateless
public class URIRegistryManager implements IURIRegistryManager {

	@Inject
	IRegistry<URI> registry;

	@Override
	public void rebind(final String name, final URI location) {
		this.registry.rebind(name, location);
	}

	@Override
	public boolean unbind(final String name) {
		return this.registry.unbind(name);
	}

	@Override
	public URI lookup(final String name) throws NotBoundException {
		return this.registry.lookup(name);
	}

	@Override
	public Set<RegistryEntry> getBoundNames() {
		final Set<Entry<String, URI>> registryEntries = this.registry.getEntries();
		final Set<RegistryEntry> entries = new LinkedHashSet<>(registryEntries.size(), 1);
		for (final Entry<String, URI> entry : registryEntries) {
			final RegistryEntry regEntry = new RegistryEntry();
			regEntry.setLocation(entry.getValue());
			regEntry.setName(entry.getKey());
			entries.add(regEntry);
		}
		return entries;
	}

}
