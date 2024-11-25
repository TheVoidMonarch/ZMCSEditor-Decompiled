package ui;

import java.awt.Color;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

public class ZMCSearch extends JDialog {
   public static final int FIND_DIALOG = 0;
   public static final int REPLACE_DIALOG = 1;
   public static final int FIND_ACTION = 0;
   public static final int REPLACE_ACTION = 1;
   public static final int REPLACE_ALL_ACTION = 2;
   public static final String NO_REPLACE_NO_OCURRENCE_MSG = "no ocurrence was found.";
   public static final String REPLACE_OCURRENCE_MSG = "1 ocurrence was replaced, the next ocurrence found.";
   public static final String REPLACE_NO_OCURRENCE_MSG = "1 ocurrence was replaced, no more ocurrence were found.";
   public static final String FIND_NO_OCURRENCE_MSG = "Can't find the text \"%s\"";
   public static final String REPLACE_ALL_NO_OCURRENCE_MSG = "Can't find the text \"%s\"";
   public static final String REPLACE_ALL_OCURRENCE_SCRIPT_MSG = "%d ocurrences were replaced in selected script.";
   public static final String REPLACE_ALL_OCURRENCE_MSG = "%d ocurrences were replaced in all scripts.";
   private int dialogType;
   private int firedAction;
   private int replacedOcurrences;
   private int foundOcurrences;
   private JTextField findTextField;
   private JTextField replaceTextField;
   private JButton findNextButton;
   private JCheckBox backwardCheckBox;
   private JCheckBox wrapAroundCheckBox;
   private JRadioButton allScopeRadioButton;
   private JLabel messageLabel;
   private JButton cancelButton;
   private JButton replaceButton;
   private JButton replaceAllButton;

   private ZMCSearch(ZMCView owner, int type) {
      super(owner);
      this.setLocation(owner.getX() + 100, owner.getY() + 100);
      this.setResizable(false);
      this.setAlwaysOnTop(true);
      this.setDefaultCloseOperation(2);
      this.dialogType = type;
      if (type == 1) {
         this.initReplaceComponents();
      } else {
         this.initFindComponents();
      }

   }

   private void initListeners() {
      this.findNextButton.addActionListener((e) -> {
         this.firedAction = 0;
         ((ZMCView)this.getOwner()).doFindAction();
      });
      this.cancelButton.addActionListener((e) -> {
         this.setVisible(false);
      });
      if (this.dialogType == 1) {
         this.replaceButton.addActionListener((e) -> {
            this.firedAction = 1;
            ((ZMCView)this.getOwner()).doReplaceAction();
         });
         this.replaceAllButton.addActionListener((e) -> {
            this.firedAction = 2;
            ((ZMCView)this.getOwner()).doReplaceAllAction();
         });
      }

   }

   private void initFindComponents() {
      this.setTitle("Find");
      this.setSize(500, 180);
      this.getContentPane().setLayout(new MigLayout("gap 2", "[][158.00,grow][]", "[][][shrink 10][][grow,bottom]"));
      JLabel scopeLabel = new JLabel("Find:");
      this.getContentPane().add(scopeLabel, "cell 0 0,alignx trailing");
      this.findTextField = new JTextField();
      this.findTextField.setColumns(10);
      this.getContentPane().add(this.findTextField, "cell 1 0,growx");
      this.messageLabel = new JLabel();
      this.getContentPane().add(this.messageLabel, "cell 0 4 3 1");
      scopeLabel = new JLabel("Scope:");
      this.getContentPane().add(scopeLabel, "cell 0 1,alignx trailing");
      this.allScopeRadioButton = new JRadioButton("All");
      this.getContentPane().add(this.allScopeRadioButton, "flowx,cell 1 1");
      JRadioButton scriptScopeRadioButton = new JRadioButton("Script");
      this.getContentPane().add(scriptScopeRadioButton, "cell 1 1");
      ButtonGroup buttonGroup = new ButtonGroup();
      buttonGroup.add(this.allScopeRadioButton);
      buttonGroup.add(scriptScopeRadioButton);
      this.allScopeRadioButton.setSelected(true);
      this.backwardCheckBox = new JCheckBox("Backward direction");
      this.getContentPane().add(this.backwardCheckBox, "flowx,cell 1 2");
      this.wrapAroundCheckBox = new JCheckBox("Wrap around");
      this.getContentPane().add(this.wrapAroundCheckBox, "cell 1 3");
      this.findNextButton = new JButton("Find Next");
      this.getContentPane().add(this.findNextButton, "cell 2 0,growx");
      this.cancelButton = new JButton("Cancel");
      this.getContentPane().add(this.cancelButton, "cell 2 1,growx");
      this.initListeners();
   }

   private void initReplaceComponents() {
      this.setTitle("Replace");
      this.setSize(500, 190);
      this.getContentPane().setLayout(new MigLayout("gap 2", "[][158.00,grow][]", "[][][][][][]"));
      JLabel scopeLabel = new JLabel("Find:");
      this.getContentPane().add(scopeLabel, "cell 0 0,alignx trailing");
      this.findTextField = new JTextField();
      this.getContentPane().add(this.findTextField, "cell 1 0,growx");
      this.findTextField.setColumns(10);
      JLabel replaceLabel = new JLabel("Replace:");
      this.getContentPane().add(replaceLabel, "cell 0 1,alignx trailing");
      this.replaceTextField = new JTextField();
      this.getContentPane().add(this.replaceTextField, "cell 1 1,growx");
      this.replaceTextField.setColumns(10);
      this.messageLabel = new JLabel("");
      this.getContentPane().add(this.messageLabel, "cell 0 5 3 1");
      scopeLabel = new JLabel("Scope:");
      this.getContentPane().add(scopeLabel, "cell 0 2, alignx trailing");
      this.allScopeRadioButton = new JRadioButton("All");
      this.getContentPane().add(this.allScopeRadioButton, "flowx,cell 1 2");
      JRadioButton scriptScopeRadioButton = new JRadioButton("Script");
      this.getContentPane().add(scriptScopeRadioButton, "cell 1 2");
      ButtonGroup buttonGroup = new ButtonGroup();
      buttonGroup.add(this.allScopeRadioButton);
      buttonGroup.add(scriptScopeRadioButton);
      this.allScopeRadioButton.setSelected(true);
      this.backwardCheckBox = new JCheckBox("Backward direction");
      this.getContentPane().add(this.backwardCheckBox, "cell 1 3");
      this.wrapAroundCheckBox = new JCheckBox("Wrap around");
      this.getContentPane().add(this.wrapAroundCheckBox, "cell 1 4");
      this.findNextButton = new JButton("Find Next");
      this.getContentPane().add(this.findNextButton, "cell 2 0,growx");
      this.replaceButton = new JButton("Replace");
      this.getContentPane().add(this.replaceButton, "cell 2 1,growx");
      this.replaceAllButton = new JButton("Replace All");
      this.getContentPane().add(this.replaceAllButton, "cell 2 2,growx");
      this.cancelButton = new JButton("Cancel");
      this.getContentPane().add(this.cancelButton, "cell 2 3,growx");
      this.initListeners();
   }

   public static ZMCSearch createSearchDialog(ZMCView owner, int type) {
      ZMCSearch zmcSearch = new ZMCSearch(owner, type);
      return zmcSearch;
   }

   public int getDialogType() {
      return this.dialogType;
   }

   public String getFindText() {
      return this.findTextField.getText();
   }

   public void setFindText(String text) {
      this.findTextField.setText(text);
   }

   public String getReplaceText() {
      return this.replaceTextField.getText();
   }

   public JButton getFindNextButton() {
      return this.findNextButton;
   }

   public boolean isScopeAll() {
      return this.allScopeRadioButton.isSelected();
   }

   public boolean isBackward() {
      return this.backwardCheckBox.isSelected();
   }

   public boolean isWrapAround() {
      return this.wrapAroundCheckBox.isSelected();
   }

   public void clearMessage() {
      this.messageLabel.setText("");
   }

   public int getFiredAction() {
      return this.firedAction;
   }

   public int getReplacedOcurrences() {
      return this.replacedOcurrences;
   }

   public void setReplacedOcurrences(int replacedOcurrences) {
      this.replacedOcurrences = replacedOcurrences;
   }

   public int getFoundOcurrences() {
      return this.foundOcurrences;
   }

   public void setFoundOcurrences(int foundOcurrences) {
      this.foundOcurrences = foundOcurrences;
   }

   public void showStatusMessage() {
      StringBuilder msg = new StringBuilder();
      if (this.firedAction == 0) {
         this.messageLabel.setForeground(Color.RED);
         msg.append("Find: ");
         msg.append(String.format("Can't find the text \"%s\"", this.findTextField.getText()));
      }

      if (this.firedAction == 1) {
         this.messageLabel.setForeground(Color.BLUE);
         msg.append("Replace: ");
         if (this.replacedOcurrences < 1) {
            msg.append("no ocurrence was found.");
         } else if (this.foundOcurrences > 0) {
            msg.append("1 ocurrence was replaced, the next ocurrence found.");
         } else {
            msg.append("1 ocurrence was replaced, no more ocurrence were found.");
         }
      }

      if (this.firedAction == 2) {
         this.messageLabel.setForeground(Color.BLUE);
         msg.append("Replace All: ");
         if (this.isScopeAll()) {
            msg.append(String.format("%d ocurrences were replaced in all scripts.", this.replacedOcurrences));
         } else {
            msg.append(String.format("%d ocurrences were replaced in selected script.", this.replacedOcurrences));
         }
      }

      this.messageLabel.setText(msg.toString());
   }
}
