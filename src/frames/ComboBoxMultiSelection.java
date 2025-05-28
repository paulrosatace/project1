/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package frames;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatComboBoxUI;
import com.formdev.flatlaf.util.UIScale;
import com.formdev.flatlaf.icons.FlatCheckBoxIcon; // Correct import for FlatCheckBoxIcon
import com.formdev.flatlaf.ui.FlatUIUtils;
import net.miginfocom.swing.MigLayout; // This import requires miglayout-swing.jar and miglayout-core.jar

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.plaf.basic.ComboPopup;

/**
 * Custom JComboBox that allows multiple selections, leveraging FlatLaf for UI.
 *
 * @author ADMIN
 * @param <E> The type of items in the combo box.
 * **Important:** For correct behavior with `equals()` and `contains()`,
 * the class `E` MUST properly override `equals()` and `hashCode()`.
 */
public class ComboBoxMultiSelection<E> extends JComboBox<E> {

    private final List<E> selectedItems = new ArrayList<>();
    private final ComboBoxMultiCellEditor comboBoxMultiCellEditor;
    private Component comboList; // Represents the JList inside the popup

    /**
     * Returns the list of currently selected items.
     * @return A List of selected items. Returns a copy to prevent external modification.
     */
    public List<E> getSelectedItems() {
        return new ArrayList<>(selectedItems);
    }

    /**
     * Sets the selected items in the combo box, clearing previous selections.
     * @param itemsToSelect The list of items to be selected.
     */
    public void setSelectedItems(List<E> itemsToSelect) {
        clearSelectedItems(); // Clear existing selections

        // Iterate through all items in the combo box model and add if they are in itemsToSelect
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            E comboItem = getItemAt(i);
            if (itemsToSelect.contains(comboItem)) {
                addItemObject(comboItem); // Add to internal selectedItems list and editor
            }
        }
        // Ensure the editor and list are repainted to reflect changes
        revalidate();
        repaint();
        if (comboList != null) {
            comboList.repaint();
        }
    }

    /**
     * Clears all selected items from the combo box and updates the editor's display.
     */
    public void clearSelectedItems() {
        selectedItems.clear();
        // Access the editor component to clear its visual representation
        Component editorCom = getEditor().getEditorComponent();
        if (editorCom instanceof JScrollPane) {
            JScrollPane scroll = (JScrollPane) editorCom;
            JPanel panel = (JPanel) scroll.getViewport().getView(); // Use getView()
            if (panel != null) {
                panel.removeAll();
                panel.revalidate();
                panel.repaint();
            }
        }
        // Repaint the dropdown list if it's visible, to ensure checkboxes are unchecked
        if (comboList != null) {
            comboList.repaint();
        }
    }

    /**
     * Removes an item from the selected items list and updates the UI.
     * @param obj The item to remove.
     */
    private void removeItemObject(E obj) {
        // Only remove if present to avoid unnecessary operations
        selectedItems.remove(obj);
        comboBoxMultiCellEditor.removeItem(obj);
        if (selectedItems.remove(obj)) {
            comboBoxMultiCellEditor.removeItem(obj);
            if (comboList != null) {
                comboList.repaint();
            }
            // Update the main combo box display (editor)
            comboBoxMultiCellEditor.getEditorComponent().revalidate();
            comboBoxMultiCellEditor.getEditorComponent().repaint();
        }
    }

    /**
     * Adds an item to the selected items list and updates the UI.
     * Prevents adding duplicate items to the internal list.
     * @param obj The item to add.
     */
    private void addItemObject(E obj) {
        if (!selectedItems.contains(obj)) { // Prevent duplicates in selectedItems list
            selectedItems.add(obj);
            comboBoxMultiCellEditor.addItem(obj);
            if (comboList != null) {
                comboList.repaint();
            }
            // Update the main combo box display (editor)
            comboBoxMultiCellEditor.getEditorComponent().revalidate();
            comboBoxMultiCellEditor.getEditorComponent().repaint();
        }
    }

    /**
     * Constructor for ComboBoxMultiSelection. Initializes the UI delegates,
     * editor, renderer, and adds the action listener for selection logic.
     */
    public ComboBoxMultiSelection() {
        setUI(new ComboBoxMultiUI()); // Apply custom UI for FlatLaf integration
        comboBoxMultiCellEditor = new ComboBoxMultiCellEditor();
        setRenderer(new ComboBoxMultiCellRenderer()); // Use custom renderer for checkboxes
        setEditor(comboBoxMultiCellEditor); // Use custom editor for tags
        setEditable(true); // Must be editable to use a custom editor

        // ActionListener to handle item selection/deselection when clicked in the popup
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if the action originated from a mouse click (to filter out programmatic changes)
                if ((e.getModifiers() & ActionEvent.MOUSE_EVENT_MASK) != 0) {
                    JComboBox<?> combo = (JComboBox<?>) e.getSource();
                    @SuppressWarnings("unchecked")
                    E obj = (E) combo.getSelectedItem(); // Get the selected item from the popup

                    if (obj != null) { // Ensure an item was actually selected (not null from deselection)
                        if (selectedItems.contains(obj)) {
                            removeItemObject(obj);
                        } else {
                            addItemObject(obj);
                        }
                    }
                }
            }
        });
    }

    /**
     * Convenience constructor to initialize the combo box with an array of items.
     * @param items An array of items to add to the combo box model.
     */
    public ComboBoxMultiSelection(E[] items) {
        super(items);
            setUI(new ComboBoxMultiUI());
            comboBoxMultiCellEditor = new ComboBoxMultiCellEditor();
            setRenderer(new ComboBoxMultiCellRenderer());
            setEditor(comboBoxMultiCellEditor);
            setEditable(true); // Must be true for custom editor to be visible

            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if ((e.getModifiers() & ActionEvent.MOUSE_EVENT_MASK) != 0) {
                        JComboBox<?> combo = (JComboBox<?>) e.getSource();
                        @SuppressWarnings("unchecked")
                        E obj = (E) combo.getSelectedItem();

                        if (obj != null) {
                            if (selectedItems.contains(obj)) {
                                removeItemObject(obj);
                            } else {
                                addItemObject(obj);
                            }
                        }
                    }
                }
            });
    }

    /**
     * Convenience constructor to initialize the combo box with a Vector of items.
     * @param items A Vector of items to add to the combo box model.
     */
//    public ComboBoxMultiSelection(Vector<E> items) {
//        this(); // Call the default constructor to initialize UI components
//        for (E item : items) {
//            this.addItem(item); // Add items to the JComboBox model
//        }
//    }

    /**
     * Overridden to prevent the popup from closing automatically when an item is clicked.
     * The popup should only close when explicitly told to (e.g., clicking outside).
     */
    @Override
    public void setPopupVisible(boolean v) {
        if (!v) { // Allow closing when 'v' is false (e.g., clicking outside the popup)
            super.setPopupVisible(v);
        }
    }

    /**
     * Custom UI delegate for the ComboBoxMultiSelection.
     * Extends FlatComboBoxUI to inherit FlatLaf styling.
     */
    private class ComboBoxMultiUI extends FlatComboBoxUI {

        @Override
        protected ComboPopup createPopup() {
            // Use MultiComboPopup to potentially customize popup behavior further if needed
            return new MultiComboPopup((JComboBox<E>) comboBox);
        }

        private class MultiComboPopup extends FlatComboPopup {
            public MultiComboPopup(JComboBox<E> combo) {
                super(combo);
                // Additional popup customizations can go here
            }
        }

        @Override
        protected Dimension getDisplaySize() {
            // This method affects the size of the "editable" area.
            // Returning 0 width effectively makes the default text display area disappear,
            // relying entirely on the custom editor (JScrollPane with JPanel).
            // If your editor needs to fill the full width, consider returning `super.getDisplaySize().width`
            // instead of 0 for the width. For a tag-based editor, 0 is often desired.
            Dimension size = super.getDisplaySize();
            return new Dimension(0, size.height); // Width 0 means the default text field is hidden
        }
    }

    /**
     * Custom renderer for the dropdown list items. Each item is rendered as a JLabel
     * with a custom FlatLaf-style checkbox icon indicating its selection status.
     */
    private class ComboBoxMultiCellRenderer extends BasicComboBoxRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            // Call super to get default rendering properties (background, foreground, etc.)
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            // Store a reference to the actual JList used in the popup.
            // This is crucial for repainting the list when selections change from the editor.
            if (comboList != list) {
                comboList = list;
            }

            // Set the icon based on whether the item is in the selectedItems list
            setIcon(new CheckBoxIcon(selectedItems.contains(value)));
            return this;
        }
    }

    /**
     * Custom editor for the ComboBoxMultiSelection. Displays selected items as removable tags.
     */
    private class ComboBoxMultiCellEditor extends BasicComboBoxEditor {

        protected final JScrollPane scroll;
        protected final JPanel panel;

        /**
         * Adds an item (as a visual tag) to the editor panel.
         * @param obj The item to add.
         */
        protected void addItem(E obj) {
            Item item = new Item(obj);
            panel.add(item);
            panel.revalidate(); // Revalidate the panel to re-layout components
            panel.repaint();    // Repaint to show changes
        }

        /**
         * Removes an item (visual tag) from the editor panel.
         * @param obj The item to remove.
         */
        @SuppressWarnings("unchecked") // Suppress unchecked warning for instanceof Item
        protected void removeItem(E obj) {
            // Iterate through components to find and remove the matching Item tag
            // Iterate backwards to avoid issues when removing elements from a list you're iterating
            for (int i = panel.getComponentCount() - 1; i >= 0; i--) {
                Component comp = panel.getComponent(i);
                // The warning "Component cannot be safely cast to ComboBoxMultiSelection<E>.Item"
                // is addressed by the @SuppressWarnings("unchecked") annotation above this method.
                // Explicitly check against the raw type of the inner class to satisfy stricter compilers.
                if (comp instanceof ComboBoxMultiSelection.Item) { // <--- CHANGE IS HERE
                    Item item = (Item) comp; // This cast is safe due to the instanceof check and suppression
                    // Use equals() for comparison. Requires proper equals() in type E.
                    if (item.getItem().equals(obj)) {
                        panel.remove(i);
                        panel.revalidate();
                        panel.repaint();
                        break; // Found and removed, exit loop
                    }
                }
            }
        }

        public ComboBoxMultiCellEditor() {
            // Initialize the panel with MigLayout for flexible tag arrangement
            this.panel = new JPanel(new MigLayout("insets 0,filly,gapx 2", "", "fill"));
            this.scroll = new JScrollPane(panel);

            // Apply FlatLaf client properties for styling the scroll pane and panel
            scroll.putClientProperty(FlatClientProperties.STYLE, ""
                    + "border:2,2,2,2;" // Padding around the tags
                    + "background:$ComboBox.editableBackground"); // Use FlatLaf's defined background
            panel.putClientProperty(FlatClientProperties.STYLE, ""
                    + "background:$ComboBox.editableBackground"); // Ensure panel matches scroll background

            // Configure scroll bar behavior
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER); // No vertical scroll
            JScrollBar scrollBar = scroll.getHorizontalScrollBar(); // Get horizontal scroll bar
            scrollBar.putClientProperty(FlatClientProperties.STYLE, ""
                    + "width:3;" // Thin scrollbar
                    + "thumbInsets:0,0,0,1;" // Thumb styling
                    + "hoverTrackColor:null"); // No hover effect on track
            scrollBar.setUnitIncrement(10); // Scrolling speed
        }

        @Override
        public Component getEditorComponent() {
            return scroll; // The editor is the JScrollPane containing the tags
        }
    }

    /**
     * Custom CheckBoxIcon for FlatLaf styling. Draws a checkbox based on selection status.
     */
    private class CheckBoxIcon extends FlatCheckBoxIcon {

        private final boolean selected;

        public CheckBoxIcon(boolean selected) {
            this.selected = selected;
        }

        @Override
        protected boolean isSelected(Component c) {
            // This icon's selection state is explicitly passed in the constructor
            return selected;
        }
    }

    /**
     * Represents a single selected item as a removable tag in the editor.
     * It's a JLabel with a close button and custom painting for the tag background.
     */
    private class Item extends JLabel {
        private final E item; // The actual item this tag represents

        public E getItem() {
            return item; // Getter for the represented item
        }

        public Item(E item) {
            super(item.toString()); // Display the item's string representation
            this.item = item;
            init(); // Initialize the tag's UI
        }

        private void init() {
            // Apply FlatLaf client properties for styling the tag background and border
            putClientProperty(FlatClientProperties.STYLE, ""
                    + "border:0,5,0,20;" // Padding around the text
                    + "background:darken($ComboBox.background,10%);"); // Slightly darker background for tags

            // Create the close button (X)
            JButton cmd = new JButton("X");
            cmd.putClientProperty(FlatClientProperties.STYLE, ""
                    + "arc:999;" // Make it perfectly round
                    + "margin:1,1,1,1;" // Minimal margin
                    + "background:null;" // Transparent background
                    + "focusWidth:0"); // No focus border

            // Add action listener to the close button to remove the item
            cmd.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Call the outer class's method to remove the item
                    ComboBoxMultiSelection.this.removeItemObject(item);
                }
            });
            cmd.setFocusable(false); // Prevent button from gaining focus

            // Use MigLayout for the Item to position the 'X' button
            setLayout(new MigLayout("fill"));
            add(cmd, "pos 1al 0.5al 10 10"); // Position 'X' button in the top-right corner
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            FlatUIUtils.setRenderingHints(g2); // Apply FlatLaf rendering hints
            int arc = UIScale.scale(10); // Rounded corner radius
            g2.setColor(getBackground()); // Use the background color set by client property
            // Paint the rounded rectangle background for the tag
            FlatUIUtils.paintComponentBackground(g2, 0, 0, getWidth(), getHeight(), 0, arc);
            g2.dispose();
            super.paintComponent(g); // Paint the JLabel's content (text)
        }
    }
}
