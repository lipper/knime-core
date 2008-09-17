/*
 * ------------------------------------------------------------------
 * This source code, its documentation and all appendant files
 * are protected by copyright law. All rights reserved.
 *
 * Copyright, 2003 - 2008
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
 * ---------------------------------------------------------------------
 *
 * History
 *   29.01.2008 (cebron): created
 */
package org.knime.base.node.mine.cluster.assign;

import org.knime.core.node.GenericNodeFactory;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeView;

/**
 *
 * @author cebron, University of Konstanz
 */
public class ClusterAssignerNodeFactory extends
        GenericNodeFactory<ClusterAssignerNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected NodeDialogPane createNodeDialogPane() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClusterAssignerNodeModel createNodeModel() {
        return new ClusterAssignerNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView createNodeView(final int viewIndex,
            final ClusterAssignerNodeModel nodeModel) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getNrNodeViews() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean hasDialog() {
        return false;
    }

}
