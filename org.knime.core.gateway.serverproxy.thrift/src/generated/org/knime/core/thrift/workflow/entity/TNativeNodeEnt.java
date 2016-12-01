/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ---------------------------------------------------------------------
 *
 */
package org.knime.core.thrift.workflow.entity;

import org.knime.core.gateway.v0.workflow.entity.NodeFactoryIDEnt;
import org.knime.core.gateway.v0.workflow.entity.EntityID;
import org.knime.core.gateway.v0.workflow.entity.JobManagerEnt;
import org.knime.core.gateway.v0.workflow.entity.NodeMessageEnt;
import org.knime.core.gateway.v0.workflow.entity.NodeInPortEnt;
import org.knime.core.gateway.v0.workflow.entity.NodeOutPortEnt;
import org.knime.core.gateway.v0.workflow.entity.BoundsEnt;
import java.util.List;

import com.facebook.swift.codec.ThriftConstructor;
import com.facebook.swift.codec.ThriftField;
import com.facebook.swift.codec.ThriftStruct;

import org.knime.core.gateway.serverproxy.entity.AbstractNativeNodeEnt;
import org.knime.core.gateway.v0.workflow.entity.NativeNodeEnt;
import org.knime.core.thrift.workflow.entity.TNativeNodeEnt.TNativeNodeEntBuilder;


/**
 *
 * @author Martin Horn, University of Konstanz
 */
@ThriftStruct(builder = TNativeNodeEntBuilder.class)
public class TNativeNodeEnt extends AbstractNativeNodeEnt {

    /**
     * @param builder
     */
    protected TNativeNodeEnt(final AbstractNativeNodeEntBuilder builder) {
        super(builder);
    }

    @Override
    @ThriftField
    public NodeFactoryIDEnt getNodeFactoryID() {
        return super.getNodeFactoryID();
    }
    
    @Override
    @ThriftField
    public EntityID getParent() {
        return super.getParent();
    }
    
    @Override
    @ThriftField
    public JobManagerEnt getJobManager() {
        return super.getJobManager();
    }
    
    @Override
    @ThriftField
    public NodeMessageEnt getNodeMessage() {
        return super.getNodeMessage();
    }
    
    @Override
    @ThriftField
    public List<NodeInPortEnt> getInPorts() {
        return super.getInPorts();
    }
    
    @Override
    @ThriftField
    public List<NodeOutPortEnt> getOutPorts() {
        return super.getOutPorts();
    }
    
    @Override
    @ThriftField
    public String getName() {
        return super.getName();
    }
    
    @Override
    @ThriftField
    public String getNodeID() {
        return super.getNodeID();
    }
    
    @Override
    @ThriftField
    public String getNodeType() {
        return super.getNodeType();
    }
    
    @Override
    @ThriftField
    public BoundsEnt getBounds() {
        return super.getBounds();
    }
    
    @Override
    @ThriftField
    public boolean getIsDeletable() {
        return super.getIsDeletable();
    }
    
    @Override
    @ThriftField
    public String getNodeState() {
        return super.getNodeState();
    }
    

    public static class TNativeNodeEntBuilder extends AbstractNativeNodeEntBuilder {

        @Override
        @ThriftConstructor
        public TNativeNodeEnt build() {
            return new TNativeNodeEnt(this);
        }

        @Override
        @ThriftField
        public TNativeNodeEntBuilder setNodeFactoryID(final NodeFactoryIDEnt NodeFactoryID) {
            super.setNodeFactoryID(NodeFactoryID);
            return this;
        }
        
        @Override
        @ThriftField
        public TNativeNodeEntBuilder setParent(final EntityID Parent) {
            super.setParent(Parent);
            return this;
        }
        
        @Override
        @ThriftField
        public TNativeNodeEntBuilder setJobManager(final JobManagerEnt JobManager) {
            super.setJobManager(JobManager);
            return this;
        }
        
        @Override
        @ThriftField
        public TNativeNodeEntBuilder setNodeMessage(final NodeMessageEnt NodeMessage) {
            super.setNodeMessage(NodeMessage);
            return this;
        }
        
        @Override
        @ThriftField
        public TNativeNodeEntBuilder setInPorts(final List<NodeInPortEnt> InPorts) {
            super.setInPorts(InPorts);
            return this;
        }
        
        @Override
        @ThriftField
        public TNativeNodeEntBuilder setOutPorts(final List<NodeOutPortEnt> OutPorts) {
            super.setOutPorts(OutPorts);
            return this;
        }
        
        @Override
        @ThriftField
        public TNativeNodeEntBuilder setName(final String Name) {
            super.setName(Name);
            return this;
        }
        
        @Override
        @ThriftField
        public TNativeNodeEntBuilder setNodeID(final String NodeID) {
            super.setNodeID(NodeID);
            return this;
        }
        
        @Override
        @ThriftField
        public TNativeNodeEntBuilder setNodeType(final String NodeType) {
            super.setNodeType(NodeType);
            return this;
        }
        
        @Override
        @ThriftField
        public TNativeNodeEntBuilder setBounds(final BoundsEnt Bounds) {
            super.setBounds(Bounds);
            return this;
        }
        
        @Override
        @ThriftField
        public TNativeNodeEntBuilder setIsDeletable(final boolean IsDeletable) {
            super.setIsDeletable(IsDeletable);
            return this;
        }
        
        @Override
        @ThriftField
        public TNativeNodeEntBuilder setNodeState(final String NodeState) {
            super.setNodeState(NodeState);
            return this;
        }
        
    }

}
