import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Timo Bergerbusch on 17.02.2018.
 */
public class MaterialsMovePanel extends JPanel {

    private JButton moveUp, moveDown;
    private MaterialsPanel parentPanel;

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
