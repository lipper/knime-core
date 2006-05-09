/* @(#)$RCSfile$ 
 * $Revision$ $Date$ $Author$
 * 
 * -------------------------------------------------------------------
 * This source code, its documentation and all appendant files
 * are protected by copyright law. All rights reserved.
 * 
 * Copyright, 2003 - 2006
 * Universitaet Konstanz, Germany.
 * Lehrstuhl fuer Angewandte Informatik
 * Prof. Dr. Michael R. Berthold
 * 
 * You may not modify, publish, transmit, transfer or sell, reproduce,
 * create derivative works from, distribute, perform, display, or in
 * any way exploit any of the content, in whole or in part, except as
 * otherwise expressly permitted in writing by the copyright owner.
 * -------------------------------------------------------------------
 * 
 * History
 *   30.05.2005 (Florian Georg): created
 */
package de.unikn.knime.workbench.editor2.extrainfo;

import de.unikn.knime.core.node.InvalidSettingsException;
import de.unikn.knime.core.node.NodeSettings;
import de.unikn.knime.core.node.workflow.NodeExtraInfo;

/**
 * Special <code>NodeExtraInfo</code> object used by the workflow editor.
 * Basically this stores the visual bounds of the node in the workflow editor
 * pane. Note: To be independent of draw2d/GEF this doesn't use the "natural"
 * <code>Rectangle</code> object, but simply stores an <code>int[]</code>.
 * 
 * TODO This needs to be in "core", as by now the WFM tries to make instances of
 * this class while <code>load()</code>ing.
 * 
 * 
 * see org.eclipse.draw2d.geometry.Rectangle
 * 
 * @author Florian Georg, University of Konstanz
 */
public class ModellingNodeExtraInfo implements NodeExtraInfo {

    /** The key under which the bounds are registered. * */
    public static final String KEY_BOUNDS = "extrainfo.node.bounds";

    /** The key under which the factory name is registered. * */
    public static final String KEY_FACTORY = "extrainfo.node.factory";

    /** The key under which the pluginID is registered. * */
    public static final String KEY_PLUGIN = "extrainfo.node.pluginID";

    /** The key under which the icon path is registered. * */
    public static final String KEY_ICON = "extrainfo.node.icon";

    /** The key under which the type is registered. * */
    public static final String KEY_TYPE = "extrainfo.node.type";

    /** The key under which the description is registered. * */
    public static final String KEY_DESCRIPTION = "extrainfo.node.description";

    private int[] m_bounds = new int[]{0, 0, -1, -1};

    private String m_pluginID;

    private String m_factoryName;

    private String m_iconPath;

    private String m_type;

    /**
     * @see de.unikn.knime.core.node.workflow.NodeExtraInfo
     *      #save(de.unikn.knime.core.node.NodeSettings)
     */
    public void save(final NodeSettings config) {
        config.addIntArray(KEY_BOUNDS, m_bounds);
        config.addString(KEY_FACTORY, m_factoryName);
        config.addString(KEY_PLUGIN, m_pluginID);
        config.addString(KEY_ICON, m_iconPath);
        config.addString(KEY_TYPE, m_type);
    }

    /**
     * @see de.unikn.knime.core.node.workflow.NodeExtraInfo
     *      #load(de.unikn.knime.core.node.NodeSettings)
     */
    public void load(final NodeSettings config) throws InvalidSettingsException {
        m_bounds = config.getIntArray(KEY_BOUNDS);
        m_factoryName = config.getString(KEY_FACTORY);
        m_pluginID = config.getString(KEY_PLUGIN);
        m_iconPath = config.getString(KEY_ICON);
        m_type = config.getString(KEY_TYPE);
    }

    /**
     * @see de.unikn.knime.core.node.workflow.NodeExtraInfo#isFilledProperly()
     */
    public boolean isFilledProperly() {

        if (m_bounds == null) {
            return false;
        }
        if (m_pluginID == null || m_pluginID.trim().equals("")) {
            return false;
        }
        if (m_factoryName == null || m_factoryName.trim().equals("")) {
            return false;
        }
        if (m_iconPath == null || m_iconPath.trim().equals("")) {
            return false;
        }

        return true;
    }

    /**
     * Sets the location. *
     * 
     * @param x x-ccordinate
     * @param y y-coordinate
     * @param w width
     * @param h height
     * 
     */
    public void setNodeLocation(final int x, final int y, final int w,
            final int h) {
        m_bounds[0] = x;
        m_bounds[1] = y;
        m_bounds[2] = w;
        m_bounds[3] = h;
    }

    /**
     * @return Returns the bounds.
     */
    public int[] getBounds() {
        return m_bounds;
    }

    /**
     * @param bounds The bounds to set.
     */
    public void setBounds(final int[] bounds) {
        m_bounds = bounds;
    }

    /**
     * @return Returns the factoryName.
     */
    public String getFactoryName() {
        return m_factoryName;
    }

    /**
     * @param factoryName The factoryName to set.
     */
    public void setFactoryName(final String factoryName) {
        m_factoryName = factoryName;
    }

    /**
     * @return Returns the iconPath.
     */
    public String getIconPath() {
        return m_iconPath;
    }

    /**
     * @param iconPath The iconPath to set.
     */
    public void setIconPath(final String iconPath) {
        m_iconPath = iconPath;
    }

    /**
     * @return Returns the pluginID.
     */
    public String getPluginID() {
        return m_pluginID;
    }

    /**
     * @param pluginID The pluginID to set.
     */
    public void setPluginID(final String pluginID) {
        m_pluginID = pluginID;
    }

    /**
     * @return Returns the type.
     */
    public String getType() {
        return m_type;
    }

    /**
     * @param type The type to set.
     */
    public void setType(final String type) {
        m_type = type;
    }

    /**
     * Changes the position by setting the bounds left top corner according to
     * the given moving distance.
     * 
     * @param moveDist the distance to change the left top corner
     */
    public void changePosition(final int moveDist) {
        
        // first change the x value
        m_bounds[0] = m_bounds[0] + moveDist;
        m_bounds[1] = m_bounds[1] + moveDist;
    }

}
