/*
 * ------------------------------------------------------------------------
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
 * ------------------------------------------------------------------------
 */
package org.knime.base.node.viz.property.color;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.colorchooser.AbstractColorChooserPanel;

import org.knime.base.node.viz.property.color.ColorManager2NodeModel.PaletteOption;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;

/**
 * A default panel to show two color palettes.
 *
 * @author Johannes Schweig, KNIME AG
 */
final class DefaultPalettesColorPanel extends AbstractColorChooserPanel {

    private final JRadioButton m_set1RadioButton = new JRadioButton();

    private final JRadioButton m_set2RadioButton = new JRadioButton();

    private final JRadioButton m_set3RadioButton = new JRadioButton();

    private final JButton m_set1Button = new JButton("Apply to columns");

    private final JButton m_set2Button = new JButton("Apply to columns");

    private final JButton m_set3Button = new JButton("Apply to columns");

    private final String[] m_paletteSet1;

    private final String[] m_paletteSet2;

    private final String[] m_paletteSet3;

    /** Size of the individual elements of a palette. */
    private static final int PALETTE_ELEMENT_SIZE = 20;

    /** Spacing between the individual elements of a palette. */
    private static final int PALETTE_ELEMENT_SPACING = 4;

    /** Current setting of the palette option. */
    private static PaletteOption m_paletteOption = null;

    /**
     * @param paletteSet1 the first, default color palette
     * @param paletteSet2 the second color palette
     */
    DefaultPalettesColorPanel(final String[] paletteSet1, final String[] paletteSet2, final String[] paletteSet3) {
        m_paletteSet1 = paletteSet1;
        m_paletteSet2 = paletteSet2;
        m_paletteSet3 = paletteSet3;
    }

    /**
     * Overwrites the default JPanel to notify the ColorSelectionModel of changes.
     */
    private class PaletteElement extends JPanel {

        private Color m_color;

        PaletteElement(final String c, final int size) {
            m_color = Color.decode(c);
            setPreferredSize(new Dimension(size, size));
            setBackground(m_color);
            addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(final MouseEvent e){
                    getColorSelectionModel().setSelectedColor(m_color);
                }
                @Override
                public void mousePressed(final MouseEvent e){
                  setBorder(BorderFactory.createLineBorder(Color.gray));
                }
                @Override
                public void mouseReleased(final MouseEvent e){
                  setBorder(BorderFactory.createLineBorder(Color.black));
                }
                @Override
                public void mouseEntered(final MouseEvent e){
                  setBorder(BorderFactory.createLineBorder(Color.black));
                }
                @Override
                public void mouseExited(final MouseEvent e){
                  setBorder(null);
                }
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void buildChooser() {
        super.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //JPanels
        JPanel set1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, PALETTE_ELEMENT_SPACING, 0));
        set1Panel.setAlignmentX(LEFT_ALIGNMENT);
        JPanel set2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, PALETTE_ELEMENT_SPACING, 0));
        set2Panel.setAlignmentX(LEFT_ALIGNMENT);
        JPanel set3Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, PALETTE_ELEMENT_SPACING, 0));
        set3Panel.setAlignmentX(LEFT_ALIGNMENT);

        // add radiobuttons
        ButtonGroup bg = new ButtonGroup();
        m_set1RadioButton.addActionListener(e -> setPaletteOption(PaletteOption.SET1));
        m_set2RadioButton.addActionListener(e -> setPaletteOption(PaletteOption.SET2));
        m_set3RadioButton.addActionListener(e -> setPaletteOption(PaletteOption.SET3));
        bg.add(m_set1RadioButton);
        bg.add(m_set2RadioButton);
        bg.add(m_set3RadioButton);
        set1Panel.add(m_set1RadioButton);
        set2Panel.add(m_set2RadioButton);
        set3Panel.add(m_set3RadioButton);
        // Switch to Custom Set when a value is manually set
        getColorSelectionModel().addChangeListener(e -> {
            setPaletteOption(PaletteOption.CUSTOM_SET);
            showButtons();
        });
        //add colored Panels
        for (String s : m_paletteSet1) {
            set1Panel.add(new PaletteElement(s, PALETTE_ELEMENT_SIZE));
        }
        for (String s : m_paletteSet2) {
            set2Panel.add(new PaletteElement(s, PALETTE_ELEMENT_SIZE));
        }
        for (String s : m_paletteSet3) {
            set3Panel.add(new PaletteElement(s, PALETTE_ELEMENT_SIZE));
        }

        //JButtons Apply
        set1Panel.add(new JPanel());
        m_set1Button.setFont(new Font(m_set1Button.getFont().getName(), Font.PLAIN, m_set1Button.getFont().getSize()));
        set1Panel.add(m_set1Button);
        set2Panel.add(new JPanel());
        m_set2Button.setFont(new Font(m_set2Button.getFont().getName(), Font.PLAIN, m_set2Button.getFont().getSize()));
        set2Panel.add(m_set2Button);
        JPanel whitespace = new JPanel();
        whitespace.setPreferredSize(
            new Dimension(5 * (PALETTE_ELEMENT_SIZE + PALETTE_ELEMENT_SPACING) + 10, PALETTE_ELEMENT_SIZE));
        set3Panel.add(whitespace);

        m_set3Button.setFont(new Font(m_set3Button.getFont().getName(), Font.PLAIN, m_set3Button.getFont().getSize()));
        set3Panel.add(m_set3Button);

        //JLabels
        JLabel set1Label = new JLabel("Set 1");
        set1Label.setFont(new Font(set1Label.getFont().getName(), Font.PLAIN, set1Label.getFont().getSize() + 2));
        JLabel set2Label = new JLabel("Set 2");
        set2Label.setFont(new Font(set2Label.getFont().getName(), Font.PLAIN, set2Label.getFont().getSize() + 2));
        JLabel set3Label = new JLabel("Set 3 (colorblind safe)");
        set3Label.setFont(new Font(set3Label.getFont().getName(), Font.PLAIN, set3Label.getFont().getSize() + 2));

        //add panels to layout
        super.add(set1Label);
        super.add(Box.createVerticalStrut(5));
        super.add(set1Panel);
        super.add(Box.createVerticalStrut(20));
        super.add(set2Label);
        super.add(Box.createVerticalStrut(5));
        super.add(set2Panel);
        super.add(Box.createVerticalStrut(20));
        super.add(set3Label);
        super.add(Box.createVerticalStrut(5));
        super.add(set3Panel);

    }


    /**
     * @param al1 the action listener for the first button
     * @param al2 the action listener for the second button
     * @param al3 the action listener for the third button
     */
    void addActionListeners(final ActionListener al1, final ActionListener al2, final ActionListener al3) {
        m_set1Button.addActionListener(al1);
        m_set2Button.addActionListener(al2);
        m_set3Button.addActionListener(al3);
        m_set1RadioButton.addActionListener(al1);
        m_set2RadioButton.addActionListener(al2);
        m_set3RadioButton.addActionListener(al3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName() {
        return "Palettes";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMnemonic() {
        return KeyEvent.VK_P;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDisplayedMnemonicIndex() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Icon getLargeDisplayIcon() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Icon getSmallDisplayIcon() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateChooser() {

    }

    /**
     * Saves the settings.
     *
     * @param settings the settings storage
     */
    void saveSettingsTo(final NodeSettingsWO settings) {
        settings.addString(ColorManager2NodeModel.CFG_PALETTE_OPTION, m_paletteOption.getSettingsName());
    }

    /**
     * Loads the settings.
     *
     * @param settings the settings provider
     * @param specs the data table spec
     * @throws NotConfigurableException if the stored new values option is not associated with a
     *             <code>NewValueOption</code>
     */
    public void loadSettingsFrom(final NodeSettingsRO settings, final DataTableSpec[] specs)
            throws NotConfigurableException {
        try {
            // get color mapping for unassigned colors
            m_paletteOption = PaletteOption.getEnum(
                settings.getString(ColorManager2NodeModel.CFG_PALETTE_OPTION,
                    ColorManager2NodeModel.MISSING_CFG_OPTION.getSettingsName()));

            showButtons();
        } catch (InvalidSettingsException e) {
            throw new NotConfigurableException(e.getMessage());
        }
    }

    /**
     * Shows the radiobuttons or the regular buttons according to the current palette option.
     * @param show_radio if radiobuttons should be shown
     * @param show_buttons if regular buttons should be shown
     */
    void showButtons() {
//        System.out.println("sB: " + m_paletteOption);
        switch (m_paletteOption) {
                case SET1:
                    m_set1RadioButton.setSelected(true);
                    showButtons(true, false);
                    break;
                case SET2:
                    m_set2RadioButton.setSelected(true);
                    showButtons(true, false);
                    break;
                case SET3:
                    m_set3RadioButton.setSelected(true);
                    showButtons(true, false);
                    break;
                case CUSTOM_SET:
                    showButtons(false, true);
                    break;
            }
    }

    /**
     * Shows the radiobuttons or the regular buttons or none in the panel.
     * @param show_radio if radiobuttons should be shown
     * @param show_buttons if regular buttons should be shown
     */
    void showButtons(final boolean show_radio, final boolean show_buttons) {
//        System.out.println("sB: " + show_radio + "/" + show_buttons);
        m_set1RadioButton.setVisible(show_radio);
        m_set2RadioButton.setVisible(show_radio);
        m_set3RadioButton.setVisible(show_radio);

        m_set1Button.setVisible(show_buttons);
        m_set2Button.setVisible(show_buttons);
        m_set3Button.setVisible(show_buttons);
    }

    /**
     * @return the palette option of the currently selected column
     */
    public PaletteOption getPaletteOption() {
        return m_paletteOption;
    }

    /**
     * Updates the palette option with a new palette option.
     * @param po new palette option
     */
    void setPaletteOption(final PaletteOption po) {
        m_paletteOption = po;
    }

}
