/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME AG, Zurich, Switzerland
 *  Website: http://www.knime.com; Email: contact@knime.com
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
 *  KNIME and ECLIPSE being a combined program, KNIME AG herewith grants
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
 * History
 *   3 Apr 2017 (albrecht): created
 */
package org.knime.core.wizard;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.property.hilite.HiLiteManager;
import org.knime.core.node.property.hilite.HiLiteTranslator;
import org.knime.core.node.util.CheckUtils;
import org.knime.core.node.web.ValidationError;
import org.knime.core.node.web.WebResourceLocator;
import org.knime.core.node.web.WebResourceLocator.WebResourceType;
import org.knime.core.node.web.WebTemplate;
import org.knime.core.node.web.WebViewContent;
import org.knime.core.node.wizard.CSSModifiable;
import org.knime.core.node.wizard.WizardNode;
import org.knime.core.node.workflow.NodeContainer;
import org.knime.core.node.workflow.NodeContainerState;
import org.knime.core.node.workflow.NodeID;
import org.knime.core.node.workflow.NodeID.NodeIDSuffix;
import org.knime.core.node.workflow.NodeMessage;
import org.knime.core.node.workflow.SubNodeContainer;
import org.knime.core.node.workflow.WebResourceController;
import org.knime.core.node.workflow.WebResourceController.WizardPageContent;
import org.knime.core.node.workflow.WebResourceController.WizardPageContent.WizardPageNodeInfo;
import org.knime.core.node.workflow.WorkflowLock;
import org.knime.core.node.workflow.WorkflowManager;
import org.knime.js.core.JSONViewContent;
import org.knime.js.core.JSONWebNode;
import org.knime.js.core.JSONWebNodeInfo;
import org.knime.js.core.JSONWebNodeInfo.JSONNodeState;
import org.knime.js.core.JSONWebNodePage;
import org.knime.js.core.JSONWebNodePageConfiguration;
import org.knime.js.core.layout.bs.JSONLayoutColumn;
import org.knime.js.core.layout.bs.JSONLayoutContent;
import org.knime.js.core.layout.bs.JSONLayoutPage;
import org.knime.js.core.layout.bs.JSONLayoutRow;
import org.knime.js.core.layout.bs.JSONLayoutViewContent;
import org.knime.js.core.layout.bs.JSONNestedLayout;
import org.knime.js.core.selections.json.JSONSelectionTranslator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

/**
 * Abstract utility class providing base functionality to handle serialization/deserialization of meta node or wizard views.
 *
 * @author Christian Albrecht, KNIME.com GmbH, Konstanz, Germany
 * @since 3.4
 */
public abstract class AbstractPageManager {

    private static final NodeLogger LOGGER = NodeLogger.getLogger(AbstractPageManager.class);

    private final WorkflowManager m_wfm;

    /**
     * Creates a new page manager
     *
     * @param workflowManager the underlying {@link WorkflowManager} used e.g. for execution related tasks
     */
    public AbstractPageManager(final WorkflowManager workflowManager) {
        m_wfm = CheckUtils.checkArgumentNotNull(workflowManager);
    }

    /**
     * @return The underlying {@link WorkflowManager} instance, not null.
     */
    public final WorkflowManager getWorkflowManager() {
        return m_wfm;
    }

    /**
     * Performs a transformation from {@link WizardPageContent} to {@link JSONWebNodePage} which can be used for serialization.
     * @param page the {@link WizardPageContent} to transform
     * @return the transformed {@link JSONWebNodePage}
     * @throws IOException if layout of page can not be generated
     */
    protected JSONWebNodePage createWizardPageInternal(final WizardPageContent page) throws IOException {
        // process layout
        JSONLayoutPage layout = new JSONLayoutPage();
        try {
            String lString = page.getLayoutInfo();
            if (StringUtils.isNotEmpty(lString)) {
                layout = getJSONLayoutFromSubnode(page.getPageNodeID(), page.getLayoutInfo());
            }
        } catch (IOException e) {
            throw new IOException("Layout for page could not be generated: " + e.getMessage(), e);
        }

        // process selection translators
        List<JSONSelectionTranslator> selectionTranslators = new ArrayList<JSONSelectionTranslator>();
        if (page.getHiLiteTranslators() != null) {
            for (HiLiteTranslator hiLiteTranslator : page.getHiLiteTranslators()) {
                if (hiLiteTranslator != null) {
                    selectionTranslators.add(new JSONSelectionTranslator(hiLiteTranslator));
                }
            }
        }
        if (page.getHiliteManagers() != null) {
            for (HiLiteManager hiLiteManager : page.getHiliteManagers()) {
                if (hiLiteManager != null) {
                    selectionTranslators.add(new JSONSelectionTranslator(hiLiteManager));
                }
            }
        }
        if (selectionTranslators.size() < 1) {
            selectionTranslators = null;
        }
        JSONWebNodePageConfiguration pageConfig = new JSONWebNodePageConfiguration(layout, null, selectionTranslators);
        Map<String, JSONWebNode> nodes = new HashMap<String, JSONWebNode>();
        for (Map.Entry<NodeIDSuffix, WizardPageNodeInfo> e : page.getInfoMap().entrySet()) {
            WizardPageNodeInfo pInfo = e.getValue();
            JSONWebNode jsonNode = new JSONWebNode();
            JSONWebNodeInfo info = new JSONWebNodeInfo();
            info.setNodeName(pInfo.getNodeName());
            info.setNodeAnnotation(pInfo.getNodeAnnotation());
            NodeContainerState state = pInfo.getNodeState();
            if (state.isIdle()) {
                info.setNodeState(JSONNodeState.IDLE);
            }
            if (state.isConfigured()) {
                info.setNodeState(JSONNodeState.CONFIGURED);
            }
            if (state.isExecutionInProgress() || state.isExecutingRemotely()) {
                info.setNodeState(JSONNodeState.EXECUTING);
            }
            if (state.isExecuted()) {
                info.setNodeState(JSONNodeState.EXECUTED);
            }
            NodeMessage message = pInfo.getNodeMessage();
            if (org.knime.core.node.workflow.NodeMessage.Type.ERROR.equals(message.getMessageType())) {
                info.setNodeErrorMessage(message.getMessage());
            }
            if (org.knime.core.node.workflow.NodeMessage.Type.WARNING.equals(message.getMessageType())) {
                info.setNodeWarnMessage(message.getMessage());
            }
            WizardNode<?, ?> wizardNode = page.getPageMap().get(e.getKey());
            if (wizardNode == null) {
                info.setDisplayPossible(false);
            } else {
                info.setDisplayPossible(true);
                WebTemplate template =
                        WebResourceController.getWebTemplateFromJSObjectID(wizardNode.getJavascriptObjectID());
                    List<String> jsList = new ArrayList<String>();
                    List<String> cssList = new ArrayList<String>();
                    for (WebResourceLocator locator : template.getWebResources()) {
                        if (locator.getType() == WebResourceType.JAVASCRIPT) {
                            jsList.add(locator.getRelativePathTarget());
                        } else if (locator.getType() == WebResourceType.CSS) {
                            cssList.add(locator.getRelativePathTarget());
                        }
                    }
                    jsonNode.setJavascriptLibraries(jsList);
                    jsonNode.setStylesheets(cssList);
                    jsonNode.setNamespace(template.getNamespace());
                    jsonNode.setInitMethodName(template.getInitMethodName());
                    jsonNode.setValidateMethodName(template.getValidateMethodName());
                    jsonNode.setSetValidationErrorMethodName(template.getSetValidationErrorMethodName());
                    jsonNode.setGetViewValueMethodName(template.getPullViewContentMethodName());
                    jsonNode.setViewRepresentation((JSONViewContent)wizardNode.getViewRepresentation());
                    jsonNode.setViewValue((JSONViewContent)wizardNode.getViewValue());

                    if (wizardNode instanceof CSSModifiable) {
                        jsonNode.setCustomCSS(((CSSModifiable)wizardNode).getCssStyles());
                    }
            }
            jsonNode.setNodeInfo(info);
            nodes.put(e.getKey().toString(), jsonNode);
        }
        return new JSONWebNodePage(pageConfig, nodes);
    }

    private JSONLayoutPage getJSONLayoutFromSubnode(final NodeIDSuffix pageID, final String layoutInfo)
            throws IOException {
        ObjectMapper mapper = JSONLayoutPage.getConfiguredVerboseObjectMapper();
        ObjectReader reader = mapper.readerForUpdating(new JSONLayoutPage());
        JSONLayoutPage page = reader.readValue(layoutInfo);
        SubNodeContainer subNodeContainer =
            m_wfm.getNodeContainer(pageID.prependParent(m_wfm.getID()), SubNodeContainer.class, true);
        if (page != null && page.getRows() != null) {
            for (JSONLayoutRow row : page.getRows()) {
                setNodeIDInContent(row, subNodeContainer.getWorkflowManager());
            }
        }
        return page;
    }

    /**
     * For layout creation the individual (potentially nested) layouts need their correct node IDs resolved. This method
     * traverses the layout content and sets the correct prefixes for all view and nested content elements.
     * @param content the layout content element to check and traverse
     * @param subNodeManager the {@link WorkflowManager} at the current level in the layout
     */
    private void setNodeIDInContent(final JSONLayoutContent content, final WorkflowManager subNodeManager) {
        if (content instanceof JSONLayoutRow) {
            for (JSONLayoutColumn col : ((JSONLayoutRow)content).getColumns()) {
                if (col != null && col.getContent() != null) {
                    for (JSONLayoutContent subContent : col.getContent()) {
                        setNodeIDInContent(subContent, subNodeManager);
                    }
                }
            }
        } else if (content instanceof JSONLayoutViewContent) {
            JSONLayoutViewContent view = (JSONLayoutViewContent)content;
            String nodeIDString = view.getNodeID();
            view.setNodeID(prependParentNodeID(subNodeManager, nodeIDString));
        } else if (content instanceof JSONNestedLayout) {
            JSONNestedLayout nestedLayout = (JSONNestedLayout)content;
            String nodeIDString = nestedLayout.getNodeID();
            nestedLayout.setNodeID(prependParentNodeID(subNodeManager, nodeIDString));
            JSONLayoutPage nestedPage = nestedLayout.getLayout();
            if (nestedPage != null && nestedPage.getRows() != null) {
                NodeIDSuffix suffix = NodeIDSuffix.fromString(nestedLayout.getNodeID());
                NodeContainer container = subNodeManager.getNodeContainer(suffix.prependParent(m_wfm.getID()));
                if (container != null && container instanceof SubNodeContainer) {
                    WorkflowManager nestedManager = ((SubNodeContainer)container).getWorkflowManager();
                    for (JSONLayoutRow nestedRow : nestedPage.getRows()) {
                        setNodeIDInContent(nestedRow, nestedManager);
                    }
                } else {
                    LOGGER.error("Container for nested layout with id " + nodeIDString + " could not be found. "
                        + "Setting the node ids on nested views failed.");
                }
            }
        }
    }

    /**
     * Creates the node ID to include in the layout, which starts at the subnode that the view is created for
     * @param parentManager the WorkflowManager that the current node id is contained in
     * @param nodeIDString the nodeID as it is contained in a layout, this should usually just be the index of the node
     * id
     * @return the complete node ID string with the correct prefix from the created subnode till the parentManager with
     * the index appended
     */
    private String prependParentNodeID(final WorkflowManager parentManager, final String nodeIDString) {
        String result = nodeIDString;
        if (parentManager != null) {
            NodeID orgID = NodeID.fromString(nodeIDString);
            NodeID layoutNodeID = parentManager.getID().createChild(orgID.getIndex());
            result = NodeIDSuffix.create(m_wfm.getID(), layoutNodeID).toString();
        }
        return result;
    }

    /**
     * Validates a value map using a JSON mapper.
     * @param valueMap the map to validate
     * @return a validated map of view values
     * @throws IOException on serialization error
     */
    protected Map<String, String> validateValueMap(final Map<String, String> valueMap) throws IOException{
        try (WorkflowLock lock = m_wfm.lock()) {
            ObjectMapper mapper = new ObjectMapper();
            for (String key : valueMap.keySet()) {
                String content = mapper.writeValueAsString(valueMap.get(key));
                valueMap.put(key, content);
            }
            return valueMap;
        }
    }

    /**
     * Serializes a map of validation errors into a single JSON string.
     * @param validationResults the map of errors to serialize
     * @return the JSON serialized string
     * @throws IOException on serialization error
     */
    protected String serializeValidationResult(final Map<String, ValidationError> validationResults) throws IOException {
        try (WorkflowLock lock = m_wfm.lock()) {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = null;
            if (validationResults != null && !validationResults.isEmpty()) {
                jsonString = mapper.writeValueAsString(validationResults);
            }
            return jsonString;
        }
    }

    /**
     * Serializes a response for a view request into a JSON string.
     * @param response the response object to serialize
     * @return the serialized JSON string, or null if exception occurs.
     * @since 3.7
     */
    protected String serializeViewResponse(final WebViewContent response) {
        if (response != null) {
            try (OutputStream stream = response.saveToStream()) {
                if (stream instanceof ByteArrayOutputStream) {
                    return ((ByteArrayOutputStream)stream).toString("UTF-8");
                }
            } catch (IOException ex) {
                LOGGER.error("Could not update view: " + ex.getMessage(), ex);
            }
        }
        return null;
    }

}
