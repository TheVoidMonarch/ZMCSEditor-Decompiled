package ui;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.miginfocom.swing.MigLayout;

public class ZMCScript extends JPanel {
   public static final int EDITABLE = 1;
   public static final int REFERENCE = 2;
   private ZMCView view;
   private int id;
   private List<JTextPane> textPaneList;
   private List<List<JTextPane>> textPaneTable;
   private List<JLabel> rowLabels;
   private boolean listenerEnabled = false;
   private JTextPane selectedTextPane;

   public ZMCScript(int id, ZMCView view) {
      this.id = id;
      this.view = view;
      this.setLayout(new MigLayout("gap -1, insets 0", "[][][]", "[]"));
      this.textPaneTable = new ArrayList();
      this.textPaneTable.add(new ArrayList());
      this.textPaneTable.add(new ArrayList());
      this.rowLabels = new ArrayList();
      this.addMouseListener(new MouseAdapter() {
         public void mouseEntered(MouseEvent e) {
            ZMCScript.this.selectedTextPane.requestFocus();
         }
      });
   }

   public JTextPane getFirstTextPane() {
      return (JTextPane)this.textPaneList.get(0);
   }

   public JTextPane getLastTextPane() {
      return (JTextPane)this.textPaneList.get(this.textPaneList.size() - 1);
   }

   public JTextPane getNextTextPane(JTextPane textPane) {
      int index = this.textPaneList.indexOf(textPane);
      ++index;
      if (index >= this.textPaneList.size()) {
         index = 0;
      }

      return (JTextPane)this.textPaneList.get(index);
   }

   public JTextPane getPreviousTextPane(JTextPane textPane) {
      int index = this.textPaneList.indexOf(textPane);
      --index;
      if (index < 0) {
         index = this.textPaneList.size() - 1;
      }

      return (JTextPane)this.textPaneList.get(index);
   }

   public boolean isLastTextPane(JTextPane textPane) {
      return this.textPaneList.lastIndexOf(textPane) == this.textPaneList.size() - 1;
   }

   public boolean isFirstTextPane(JTextPane textPane) {
      return this.textPaneList.indexOf(textPane) == 0;
   }

   public void setScript(List<String> script, int column) {
      this.listenerEnabled = false;
      List<JTextPane> textPanes = (List)this.textPaneTable.get(column);

      for(int j = 0; j < script.size(); ++j) {
         JTextPane textPane = null;
         if (j >= textPanes.size()) {
            textPane = new JTextPane();
            textPanes.add(textPane);
            textPane.addFocusListener(new FocusAdapter() {
               public void focusGained(FocusEvent e) {
                  if (ZMCScript.this.listenerEnabled) {
                     ZMCScript.this.selectedTextPane = (JTextPane)e.getSource();
                     ZMCScript.this.view.getDialogPreview().setDialogText(ZMCScript.this.selectedTextPane.getText());
                  }

               }
            });
            textPane.getDocument().addDocumentListener(new DocumentListener() {
               public void removeUpdate(DocumentEvent e) {
                  if (ZMCScript.this.listenerEnabled) {
                     ZMCScript.this.view.getDialogPreview().setDialogText(ZMCScript.this.selectedTextPane.getText());
                  }

               }

               public void insertUpdate(DocumentEvent e) {
                  if (ZMCScript.this.listenerEnabled) {
                     ZMCScript.this.view.getDialogPreview().setDialogText(ZMCScript.this.selectedTextPane.getText());
                  }

               }

               public void changedUpdate(DocumentEvent e) {
               }
            });
            textPane.setEditable(column == 1);
            this.add(textPane, String.format("cell %d %d, grow", column + 1, j));
         } else {
            textPane = (JTextPane)textPanes.get(j);
         }

         textPane.setText((String)script.get(j));
         if (j >= this.rowLabels.size()) {
            JLabel label = new JLabel(String.format("%3d ", j + 1));
            label.setBorder(UIManager.getBorder("TextField.border"));
            this.rowLabels.add(label);
            this.add(label, String.format("cell 0 %d, grow", j));
         }
      }

      this.updateTextPaneList();
      if (this.selectedTextPane == null) {
         this.selectedTextPane = (JTextPane)this.textPaneList.get(0);
      }

      this.listenerEnabled = true;
   }

   private void updateTextPaneList() {
      this.textPaneList = new ArrayList();
      boolean[] listEnds = new boolean[this.textPaneTable.size()];
      boolean end = false;

      for(int c = 0; !end; ++c) {
         int i;
         for(i = 0; i < this.textPaneTable.size(); ++i) {
            if (c < ((List)this.textPaneTable.get(i)).size()) {
               this.textPaneList.add(((List)this.textPaneTable.get(i)).get(c));
            } else {
               listEnds[i] = true;
            }
         }

         end = true;

         for(i = 0; i < listEnds.length; ++i) {
            end &= listEnds[i];
         }
      }

   }

   public List<String> getScript(int col) {
      List<String> script = new ArrayList();
      Iterator var3 = ((List)this.textPaneTable.get(col)).iterator();

      while(var3.hasNext()) {
         JTextPane jTextPane = (JTextPane)var3.next();
         script.add(jTextPane.getText());
      }

      return script;
   }

   public JTextPane getSelectedTextPane() {
      return this.selectedTextPane;
   }

   public void setSelectedTextPane(JTextPane selectedTextPane) {
      this.selectedTextPane = selectedTextPane;
   }

   public List<JTextPane> getTextPaneList(int col) {
      return (List)this.textPaneTable.get(col);
   }

   public String toString() {
      return "Script #" + this.id;
   }
}
