package ui;

import java.awt.FlowLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class ZMCPreviewProperties extends JPanel {
   private JComboBox<String> borderComboBox;
   private JSpinner colsSpinner;
   private JSpinner rowsSpinner;
   private JRadioButton z1xRadioButton;
   private JRadioButton z2xRadioButton;
   private ZMCView view;

   public ZMCPreviewProperties(ZMCView view) {
      this.view = view;
      this.setLayout(new FlowLayout(0, 3, 3));
      JLabel borderLabel = new JLabel("Border:");
      this.add(borderLabel);
      this.borderComboBox = new JComboBox();
      this.add(this.borderComboBox);
      JLabel colsLabel = new JLabel("Cols:");
      this.add(colsLabel);
      SpinnerNumberModel numberModel = new SpinnerNumberModel(26, 1, 30, 1);
      this.colsSpinner = new JSpinner(numberModel);
      this.add(this.colsSpinner);
      JLabel rowsLabel = new JLabel("Rows:");
      this.add(rowsLabel);
      numberModel = new SpinnerNumberModel(2, 1, 10, 1);
      this.rowsSpinner = new JSpinner(numberModel);
      this.add(this.rowsSpinner);
      JLabel zoomLabel = new JLabel("Zoom:");
      this.add(zoomLabel);
      ButtonGroup bGroup = new ButtonGroup();
      this.z1xRadioButton = new JRadioButton("1x");
      this.add(this.z1xRadioButton);
      this.z2xRadioButton = new JRadioButton("2x");
      this.add(this.z2xRadioButton);
      bGroup.add(this.z1xRadioButton);
      bGroup.add(this.z2xRadioButton);
      this.z1xRadioButton.setSelected(true);
      this.initBorderCombo();
      this.initListeners();
   }

   private void initBorderCombo() {
      for(int i = 0; i < 10; ++i) {
         this.borderComboBox.addItem("Type #" + (i + 1));
      }

   }

   private void initListeners() {
      this.colsSpinner.addChangeListener((e) -> {
         this.view.getDialogPreview().setDialogCols((Integer)this.colsSpinner.getValue());
      });
      this.rowsSpinner.addChangeListener((e) -> {
         this.view.getDialogPreview().setDialogRows((Integer)this.rowsSpinner.getValue());
      });
      this.borderComboBox.addActionListener((e) -> {
         this.view.getDialogPreview().setWindowType(this.borderComboBox.getSelectedIndex());
      });
      this.z1xRadioButton.addActionListener((e) -> {
         this.view.getDialogPreview().setScale(1);
      });
      this.z2xRadioButton.addActionListener((e) -> {
         this.view.getDialogPreview().setScale(2);
      });
   }
}
