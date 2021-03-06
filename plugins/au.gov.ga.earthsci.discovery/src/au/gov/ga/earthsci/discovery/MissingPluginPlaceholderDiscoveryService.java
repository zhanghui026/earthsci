/*******************************************************************************
 * Copyright 2013 Geoscience Australia
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package au.gov.ga.earthsci.discovery;

import java.net.URL;
import java.util.Map;

import org.eclipse.osgi.util.NLS;
import org.w3c.dom.Element;

/**
 * {@link IDiscoveryService} implementation that can be used as a placeholder
 * for discovery services that have been saved but whose contributing plugin is
 * missing.
 * 
 * @author Michael de Hoog (michael.dehoog@ga.gov.au)
 */
public class MissingPluginPlaceholderDiscoveryService implements IDiscoveryService
{
	private final String name;
	private final URL serviceURL;
	private final IDiscoveryProvider provider;
	private final boolean wasEnabled;
	private final Element propertiesElement;

	public MissingPluginPlaceholderDiscoveryService(String providerId, String name, URL serviceURL, boolean wasEnabled,
			Element propertiesElement)
	{
		this.name = name;
		this.serviceURL = serviceURL;
		this.provider = new MissingPluginPlaceholderDiscoveryProvider(providerId);
		this.wasEnabled = wasEnabled;
		this.propertiesElement = propertiesElement;
	}

	@Override
	public URL getServiceURL()
	{
		return serviceURL;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public IDiscoveryProvider getProvider()
	{
		return provider;
	}

	@Override
	public boolean isEnabled()
	{
		return false;
	}

	@Override
	public void setEnabled(boolean enabled)
	{
	}

	public boolean wasEnabled()
	{
		return wasEnabled;
	}

	public Element getPropertiesElement()
	{
		return propertiesElement;
	}

	@Override
	public IDiscovery createDiscovery(IDiscoveryParameters parameters)
	{
		return null;
	}

	private class MissingPluginPlaceholderDiscoveryProvider implements IDiscoveryProvider
	{
		private final String id;

		public MissingPluginPlaceholderDiscoveryProvider(String id)
		{
			this.id = id;
		}

		@Override
		public String getId()
		{
			return id;
		}

		@Override
		public String getName()
		{
			return NLS.bind(Messages.MissingPluginPlaceholderDiscoveryService_PluginMissing, id);
		}

		@Override
		public URL getIconURL()
		{
			return Icons.ERROR;
		}

		@Override
		public IDiscoveryService createService(String name, URL url,
				Map<IDiscoveryServiceProperty<?>, Object> propertyValues)
		{
			return null;
		}

		@Override
		public IDiscoveryServiceProperty<?>[] getProperties()
		{
			return null;
		}

		@Override
		public IDiscoveryResultHandler getHandler()
		{
			return null;
		}
	}
}
