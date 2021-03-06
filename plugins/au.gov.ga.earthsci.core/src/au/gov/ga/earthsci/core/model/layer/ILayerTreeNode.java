/*******************************************************************************
 * Copyright 2012 Geoscience Australia
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
package au.gov.ga.earthsci.core.model.layer;

import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.terrain.CompoundElevationModel;

import java.net.URI;
import java.net.URL;
import java.util.List;

import org.eclipse.core.runtime.content.IContentType;

import au.gov.ga.earthsci.common.util.IEnableable;
import au.gov.ga.earthsci.common.util.IInformationed;
import au.gov.ga.earthsci.common.util.ILabelable;
import au.gov.ga.earthsci.common.util.INameable;
import au.gov.ga.earthsci.common.util.IPropertyChangeBean;
import au.gov.ga.earthsci.core.model.IStatused;
import au.gov.ga.earthsci.core.tree.ITreeNode;

/**
 * Represents a tree node value in the layer tree.
 * 
 * @author Michael de Hoog (michael.dehoog@ga.gov.au)
 */
public interface ILayerTreeNode extends ITreeNode<ILayerTreeNode>, IPropertyChangeBean, ILabelable, INameable,
		IStatused, IInformationed
{
	/**
	 * @return A {@link LayerList} that contains all layers in the tree at and
	 *         below this node.
	 */
	LayerList getLayerList();

	/**
	 * @return A {@link CompoundElevationModel} that contains all elevation
	 *         models in the tree at and below this node.
	 */
	CompoundElevationModel getElevationModels();

	/**
	 * Return true if this node's URI matches the given URI, or any of this
	 * node's descendant's URI matches the given URI.
	 * 
	 * @param uri
	 *            URI to match
	 * @return True if there are any descendants with a matching URI
	 */
	boolean hasNodesForURI(URI uri);

	/**
	 * Return an array of {@link ILayerTreeNode}s that are descendants of this
	 * node which have URIs that match the given URI. If there are no
	 * descendants with the given URI, the returned array is empty. The returned
	 * array could also possibly contain this node if this node's URI matches.
	 * <p/>
	 * The results from this method are cached, and updated when the node's
	 * descendants change.
	 * 
	 * @param uri
	 *            URI to match
	 * @return Array of nodes that have a matching URI
	 */
	ILayerTreeNode[] getNodesForURI(URI uri);

	/**
	 * @return Are any of this node's children enabled?
	 */
	boolean isAnyChildrenEnabled();

	/**
	 * @return Are all of this node's children enabled?
	 */
	boolean isAllChildrenEnabled();

	/**
	 * Does the enabled state of any children of this node equal the given
	 * value?
	 * 
	 * @param enabled
	 *            Value to test
	 * @return True if any children's enabled equals the given value.
	 */
	boolean anyChildrenEnabledEquals(boolean enabled);

	/**
	 * Enable/disable this node (if {@link IEnableable}), and enable/disable all
	 * {@link IEnableable} children.
	 * 
	 * @param enabled
	 */
	void enableChildren(boolean enabled);

	/**
	 * Notify property listeners that this node's enable state has changed.
	 * Should only be called internally.
	 */
	void enabledChanged();

	/**
	 * Notify property listeners that this node's children have changed. Should
	 * only be called internally.
	 * 
	 * @param oldChildren
	 * @param newChildren
	 */
	void childrenChanged(List<ILayerTreeNode> oldChildren, List<ILayerTreeNode> newChildren);

	/**
	 * @return The URL pointing to this node's legend.
	 */
	URL getLegendURL();

	/**
	 * @return The URL pointing to this node's icon.
	 */
	URL getIconURL();

	/**
	 * @return Is this tree node expanded?
	 */
	boolean isExpanded();

	/**
	 * Mark this tree node as expanded.
	 * 
	 * @param expanded
	 *            Expanded state
	 */
	void setExpanded(boolean expanded);

	/**
	 * @return URI that uniquely identifies this layer node, and optionally
	 *         locates a resource for layer creation.
	 */
	URI getURI();

	/**
	 * Set this node's URI, which uniquely identifies this node. This is also
	 * used by certain nodes for layer creation.
	 * 
	 * @param uri
	 */
	void setURI(URI uri);

	/**
	 * @return The Content Type of this node's data. Used by the Intent system
	 *         during layer creation.
	 */
	IContentType getContentType();

	/**
	 * Set this node's data's Content Type.
	 * 
	 * @param contentType
	 */
	void setContentType(IContentType contentType);

}
