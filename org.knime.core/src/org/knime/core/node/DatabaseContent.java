/* 
 * ------------------------------------------------------------------
 * This source code, its documentation and all appendant files
 * are protected by copyright law. All rights reserved.
 *
 * Copyright, 2003 - 2007
 * University of Konstanz, Germany
 * Chair for Bioinformatics and Information Mining (Prof. M. Berthold)
 * and KNIME GmbH, Konstanz, Germany
 *
 * You may not modify, publish, transmit, transfer or sell, reproduce,
 * create derivative works from, distribute, perform, display, or in
 * any way exploit any of the content, in whole or in part, except as
 * otherwise expressly permitted in writing by the copyright owner or
 * as specified in the license file distributed with this product.
 *
 * If you have any questions please contact the copyright holder:
 * website: www.knime.org
 * email: contact@knime.org
 * --------------------------------------------------------------------- *
 * 
 * History
 *   13.02.2008 (gabriel): created
 */
package org.knime.core.node;


/**
 * 
 * @author Thomas Gabriel, University of Konstanz
 */
public final class DatabaseContent implements PortObject {

    /**
     * Database port type formed <code>PortObjectSpec.class</code> and 
     * <code>PortObject.class</code> from this class.
     */
    public static final PortType TYPE = 
        new PortType(DatabaseContentSpec.class, DatabaseContent.class);
    
    private final BufferedDataTable m_data;
    
    private final ModelContentRO m_conn;
    
    public DatabaseContent(final BufferedDataTable data, 
            final ModelContentRO conn) {
        m_data = data;
        m_conn = conn;
    }
    
    public BufferedDataTable getDataTable() {
        return m_data;
    }
    
    public ModelContentRO getConnectionModel() {
        return m_conn;
    }
    
    
}
