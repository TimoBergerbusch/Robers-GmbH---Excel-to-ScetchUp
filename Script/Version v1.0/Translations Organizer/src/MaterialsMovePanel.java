import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This panel is used to change the order of {@link MaterialAssignment MaterialAssignments} within the {@link MaterialsPanel}
 */
class MaterialsMovePanel extends JPanel {

    /**
     * a {@link JButton} to move a selected {@link MaterialAssignment} up by one row
     */
    private final JButton moveUp;
    /**
     * a {@link JButton} to move a selected {@link MaterialAssignment} down by one row
     */
    private final JButton moveDown;
    /**
     * the {@link MaterialsPanel}, which is the hierarchically lowest panel inheriting this panel
     */
    private final MaterialsPanel parentPanel;

    /**
     * a constructor to to create a new {@link MaterialsMovePanel} within the given parentPanel.
     *
     * @param parentPanel the {@link JPanel} this panel is used in
     */
    public MaterialsMovePanel(MaterialsPanel parentPanel) {
        this.parentPanel = parentPanel;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        moveUp = new JButton("\u2191");
        moveUp.addActionListener(new MoveActionListener());
        this.add(moveUp);

        moveDown = new JButton("\u2193");
        moveDown.addActionListener(new MoveActionListener());
        this.add(moveDown);
    }

    /**
     * the {@link ActionListener} to indicate the movement of a {@link MaterialAssignment} using the
     * {@link MaterialsPanel#changeIndices(int)}-Method
     */
    private class MoveActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(moveUp))
                parentPanel.changeIndices(-1);
            else
                parentPanel.changeIndices(1);
        }
    }
}
