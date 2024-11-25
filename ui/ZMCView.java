package ui;

import core.ZMCPaletteReader;
import core.ZMCScriptIO;
import core.ZMCScriptListModel;
import core.ZMCTileReader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FontUIResource;

public class ZMCView extends JFrame {
   public static final String PROGRAM_NAME = "ZELDA: The Minish Cap Script Editor Beta 0.2";
   public static final int REFERENCE_ROM = 0;
   public static final int WORKING_ROM = 1;
   private String rootDirectoryPath = "C:/Users/josea/Desktop/Zelda Minish/analysis";
   private File romFile;
   private JFileChooser fileOpenChooser;
   private JPanel dialogPane;
   private JButton openReferenceButton;
   private JButton openWorkingButton;
   private JButton saveWorkingButton;
   private JButton findButton;
   private JButton replaceButton;
   private JToggleButton previewButton;
   private JToolBar toolBar;
   private ZMCSearch searchDialog;
   private ZMCScript selectedScript;
   private JProgressBar progressBar;
   private ZMCScriptListModel scriptListModel;
   private JList<ZMCScript> scriptList;
   private List<List<String>> romScripts;
   private JScrollPane dialogScrollPane;
   private ZMCPreview dialogPreview;
   private ZMCPreviewProperties dialogPreviewProp;

   public ZMCView() {
      this.fileOpenChooser = new JFileChooser(this.rootDirectoryPath);
      this.fileOpenChooser.setAcceptAllFileFilterUsed(false);
      this.fileOpenChooser.addChoosableFileFilter(new FileNameExtensionFilter("GBA ROMs (*.gba)", new String[]{"gba"}));
      this.progressBar = new JProgressBar();
      this.progressBar.setVisible(false);
      this.setTitle("ZELDA: The Minish Cap Script Editor Beta 0.2");
      this.setSize(800, 600);
      this.setDefaultCloseOperation(3);
      this.getContentPane().setLayout(new BorderLayout(3, 0));
      ((JComponent)this.getContentPane()).setBorder(BorderFactory.createEmptyBorder(0, 3, 3, 3));
      this.toolBar = new JToolBar();
      this.toolBar.setFloatable(false);
      this.getContentPane().add(this.toolBar, "North");
      this.openWorkingButton = new JButton("Open ROM");
      this.toolBar.add(this.openWorkingButton);
      this.openWorkingButton.addActionListener((e) -> {
         this.openRomFile(1);
      });
      this.openReferenceButton = new JButton("Open Reference ROM");
      this.toolBar.add(this.openReferenceButton);
      this.openReferenceButton.addActionListener((e) -> {
         this.openRomFile(0);
      });
      this.openReferenceButton.setVisible(false);
      this.saveWorkingButton = new JButton("Save Changes");
      this.toolBar.add(this.saveWorkingButton);
      this.saveWorkingButton.addActionListener((e) -> {
         this.saveTranslatedRomFile();
      });
      this.saveWorkingButton.setVisible(false);
      this.toolBar.addSeparator();
      this.findButton = new JButton("Find");
      this.toolBar.add(this.findButton);
      this.findButton.setVisible(false);
      this.replaceButton = new JButton("Replace");
      this.toolBar.add(this.replaceButton);
      this.replaceButton.setVisible(false);
      this.toolBar.addSeparator();
      this.previewButton = new JToggleButton("Preview");
      this.toolBar.add(this.previewButton);
      this.previewButton.setVisible(false);
      this.previewButton.setSelected(true);
      this.dialogPreviewProp = new ZMCPreviewProperties(this);
      this.toolBar.add(this.dialogPreviewProp);
      this.dialogPreviewProp.setVisible(false);
      this.scriptListModel = new ZMCScriptListModel();
      this.scriptList = new JList(this.scriptListModel);
      this.scriptList.setSelectionMode(0);
      JScrollPane scrollPane = new JScrollPane(this.scriptList);
      this.getContentPane().add(scrollPane, "West");
      this.dialogPane = new JPanel();
      this.dialogPane.setLayout(new BoxLayout(this.dialogPane, 1));
      this.dialogScrollPane = new JScrollPane();
      this.getContentPane().add(this.dialogScrollPane, "Center");
      this.dialogPreview = new ZMCPreview();
      this.getContentPane().add(this.dialogPreview, "East");
      JPanel statusPane = new JPanel();
      FlowLayout fl_statusPane = (FlowLayout)statusPane.getLayout();
      fl_statusPane.setAlignment(0);
      this.getContentPane().add(statusPane, "South");
      statusPane.add(this.progressBar);
      this.initListeners();
      this.setVisible(true);
   }

   private void initSearchDialog(int type) {
      if (this.searchDialog == null) {
         this.searchDialog = ZMCSearch.createSearchDialog(this, type);
      } else if (this.searchDialog.getDialogType() != type) {
         this.searchDialog.setVisible(false);
         this.searchDialog = ZMCSearch.createSearchDialog(this, type);
      }

      JTextPane textPane = this.selectedScript.getSelectedTextPane();
      if (textPane != null) {
         String selectedText = textPane.getSelectedText();
         if (selectedText != null && !selectedText.isEmpty()) {
            this.searchDialog.setFindText(selectedText);
         }
      }

      this.searchDialog.setVisible(true);
   }

   private void initListeners() {
      this.findButton.addActionListener((e) -> {
         this.initSearchDialog(0);
      });
      this.replaceButton.addActionListener((e) -> {
         this.initSearchDialog(1);
      });
      this.previewButton.addActionListener((e) -> {
         this.dialogPreview.setVisible(this.previewButton.isSelected());
         this.dialogPreviewProp.setVisible(this.previewButton.isSelected());
         this.validate();
      });
      this.scriptList.addListSelectionListener((e) -> {
         if (e.getValueIsAdjusting()) {
            this.selectScript((ZMCScript)null);
         }

      });
      this.scriptList.addKeyListener(new KeyAdapter() {
         public void keyPressed(KeyEvent e) {
            ZMCScript nextScript;
            switch(e.getKeyCode()) {
            case 38:
               if (!ZMCView.this.scriptListModel.isFirstScript((ZMCScript)ZMCView.this.scriptList.getSelectedValue())) {
                  nextScript = ZMCView.this.scriptListModel.getPreviousScript((ZMCScript)ZMCView.this.scriptList.getSelectedValue());
                  ZMCView.this.selectScript(nextScript);
               }
               break;
            case 40:
               if (!ZMCView.this.scriptListModel.isLastScript((ZMCScript)ZMCView.this.scriptList.getSelectedValue())) {
                  nextScript = ZMCView.this.scriptListModel.getNextScript((ZMCScript)ZMCView.this.scriptList.getSelectedValue());
                  ZMCView.this.selectScript(nextScript);
               }
            }

         }
      });
   }

   public void doReplaceAction() {
      String findText = this.searchDialog.getFindText();
      String replaceText = this.searchDialog.getReplaceText();
      String selectedText = this.selectedScript.getSelectedTextPane().getSelectedText();
      this.searchDialog.setReplacedOcurrences(0);
      if (findText.equals(selectedText) && this.selectedScript.getSelectedTextPane().isEditable()) {
         this.selectedScript.getSelectedTextPane().replaceSelection(replaceText);
         this.searchDialog.setReplacedOcurrences(1);
      }

      this.doFindAction();
   }

   public void doReplaceAllAction() {
      boolean isScopeAll = this.searchDialog.isScopeAll();
      String findText = this.searchDialog.getFindText();
      String replaceText = this.searchDialog.getReplaceText();
      Pattern findPattern = Pattern.compile(findText);
      int i;
      if (isScopeAll) {
         int totalOcurrences = 0;

         for(i = 0; i < this.scriptListModel.getSize(); ++i) {
            ZMCScript script = (ZMCScript)this.scriptListModel.get(i);
            List<JTextPane> textPanes = script.getTextPaneList(1);
            totalOcurrences += this.replaceScript(findPattern, replaceText, textPanes);
         }

         this.searchDialog.setReplacedOcurrences(totalOcurrences);
         this.searchDialog.showStatusMessage();
      } else {
         List<JTextPane> textPanes = this.selectedScript.getTextPaneList(1);
         i = this.replaceScript(findPattern, replaceText, textPanes);
         this.searchDialog.setReplacedOcurrences(i);
         this.searchDialog.showStatusMessage();
      }

   }

   private int replaceScript(Pattern findPattern, String replacement, List<JTextPane> script) {
      int totalMatches = 0;

      int countMatches;
      for(Iterator var5 = script.iterator(); var5.hasNext(); totalMatches += countMatches) {
         JTextPane jTextPane = (JTextPane)var5.next();
         String dialog = jTextPane.getText();
         Matcher matcher = findPattern.matcher(dialog);

         for(countMatches = 0; matcher.find(); ++countMatches) {
         }

         if (countMatches > 0) {
            String replacedDialog = matcher.replaceAll(replacement);
            jTextPane.setText(replacedDialog);
         }
      }

      return totalMatches;
   }

   public void doFindAction() {
      this.searchDialog.clearMessage();
      this.searchDialog.setFoundOcurrences(0);
      ZMCScript refScript = this.selectedScript;
      ZMCScript currScript = this.selectedScript;
      JTextPane refTextPane = currScript.getSelectedTextPane();
      JTextPane currTextPane = refTextPane;
      JTextPane lastTextPane = refTextPane;
      String findText = this.searchDialog.getFindText();
      boolean isBackward = this.searchDialog.isBackward();
      boolean isWrapAround = this.searchDialog.isWrapAround();
      boolean isScopeAll = this.searchDialog.isScopeAll();
      int refTextPos = isBackward ? refTextPane.getSelectionStart() : refTextPane.getSelectionEnd();
      int currTextPos = refTextPos;
      boolean searchWraped = false;

      while(true) {
         MatchResult mResult = this.search(findText, currTextPane.getText(), currTextPos, isBackward);
         if (mResult != null) {
            if (currScript != refScript) {
               this.scriptList.setSelectedValue(currScript, true);
               this.selectScript(currScript);
               currScript.setSelectedTextPane(currTextPane);
            }

            int cp = lastTextPane.getCaretPosition();
            lastTextPane.select(cp, cp);
            currTextPane.requestFocus();
            currTextPane.select(mResult.start(), mResult.end());
            currScript.setSelectedTextPane(currTextPane);
            currTextPos = mResult.end();
            if (isBackward) {
               currTextPos = mResult.start();
            }

            final int y = currTextPane.getY();
            SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                  ZMCView.this.dialogScrollPane.getVerticalScrollBar().setValue(y);
               }
            });
            this.searchDialog.setFoundOcurrences(1);
            if (this.searchDialog.getFiredAction() == 1 && this.searchDialog.getReplacedOcurrences() > 0) {
               this.searchDialog.showStatusMessage();
            }

            return;
         }

         if (isBackward) {
            if (isScopeAll) {
               if (!isWrapAround && currScript.isFirstTextPane(currTextPane) && this.scriptListModel.isFirstScript(currScript)) {
                  this.searchDialog.showStatusMessage();
                  return;
               }

               if (searchWraped && currTextPane == refTextPane && currScript == refScript) {
                  this.searchDialog.showStatusMessage();
                  return;
               }

               if (currScript.isFirstTextPane(currTextPane) && this.scriptListModel.isFirstScript(currScript)) {
                  searchWraped = true;
               }

               lastTextPane = currTextPane;
               if (currScript.isFirstTextPane(currTextPane)) {
                  currScript = this.scriptListModel.getPreviousScript(currScript);
                  currTextPane = currScript.getLastTextPane();
               } else {
                  currTextPane = currScript.getPreviousTextPane(currTextPane);
               }

               currTextPos = currTextPane.getText().length() - 1;
               if (currTextPos < 0) {
                  currTextPos = 0;
               }
            } else {
               if (!isWrapAround && currScript.isFirstTextPane(currTextPane)) {
                  this.searchDialog.showStatusMessage();
                  return;
               }

               if (searchWraped && currTextPane == refTextPane) {
                  this.searchDialog.showStatusMessage();
                  return;
               }

               if (currScript.isFirstTextPane(currTextPane)) {
                  searchWraped = true;
               }

               currTextPane = currScript.getPreviousTextPane(currTextPane);
               currTextPos = currTextPane.getText().length() - 1;
               if (currTextPos < 0) {
                  currTextPos = 0;
               }
            }
         } else if (isScopeAll) {
            if (!isWrapAround && currScript.isLastTextPane(currTextPane) && this.scriptListModel.isLastScript(currScript)) {
               this.searchDialog.showStatusMessage();
               return;
            }

            if (searchWraped && currTextPane == refTextPane && currScript == refScript) {
               this.searchDialog.showStatusMessage();
               return;
            }

            if (currScript.isLastTextPane(currTextPane) && this.scriptListModel.isLastScript(currScript)) {
               searchWraped = true;
            }

            if (currScript.isLastTextPane(currTextPane)) {
               currScript = this.scriptListModel.getNextScript(currScript);
               currTextPane = currScript.getFirstTextPane();
            } else {
               currTextPane = currScript.getNextTextPane(currTextPane);
            }

            currTextPos = 0;
         } else {
            if (!isWrapAround && currScript.isLastTextPane(currTextPane)) {
               this.searchDialog.showStatusMessage();
               return;
            }

            if (searchWraped && currTextPane == refTextPane) {
               this.searchDialog.showStatusMessage();
               return;
            }

            if (currScript.isLastTextPane(currTextPane)) {
               searchWraped = true;
            }

            currTextPane = currScript.getNextTextPane(currTextPane);
            currTextPos = 0;
         }
      }
   }

   private MatchResult search(String regex, String text, int pos, boolean isBackward) {
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(text);
      MatchResult result = null;
      if (isBackward) {
         matcher.region(0, pos);

         while(matcher.find()) {
            result = matcher.toMatchResult();
         }
      } else if (matcher.find(pos)) {
         result = matcher.toMatchResult();
      }

      return result;
   }

   private void selectScript(ZMCScript script) {
      if (script == null) {
         script = (ZMCScript)this.scriptList.getSelectedValue();
      }

      this.selectedScript = script;
      this.dialogScrollPane.setViewportView(this.selectedScript);
   }

   private void saveTranslatedRomFile() {
      for(int i = 0; i < this.romScripts.size(); ++i) {
         ZMCScript script = (ZMCScript)this.scriptList.getModel().getElementAt(i);
         this.romScripts.set(i, script.getScript(1));
      }

      try {
         ZMCScriptIO.write(this.romScripts, this.romFile, 10165648);
         JOptionPane.showMessageDialog(this, "Working ROM saved successfully.", "Information", 1);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   private void openRomFile(final int romType) {
      if (romType == 0) {
         this.fileOpenChooser.setDialogTitle("Open Rom");
      } else {
         this.fileOpenChooser.setDialogTitle("Open Reference Rom");
      }

      int opt = this.fileOpenChooser.showOpenDialog(this);
      if (opt == 0) {
         File file = this.fileOpenChooser.getSelectedFile();

         try {
            final List<List<String>> scripts = ZMCScriptIO.read(file, 10165648, 1);

            for(int i = 0; i < scripts.size(); ++i) {
               if (i >= this.scriptListModel.size()) {
                  this.scriptListModel.addElement(new ZMCScript(i, this));
               }
            }

            if (romType == 1) {
               this.romScripts = scripts;
               this.romFile = file;
               this.setTitle("ZELDA: The Minish Cap Script Editor Beta 0.2 - " + this.romFile.getPath());
               this.initTileReaders();
               this.initPalettes();
               this.openReferenceButton.setVisible(true);
               this.saveWorkingButton.setVisible(true);
               this.findButton.setVisible(true);
               this.replaceButton.setVisible(true);
               this.previewButton.setVisible(true);
               this.dialogPreviewProp.setVisible(true);
            }

            if (this.scriptList.getSelectedIndex() == -1) {
               this.scriptList.setSelectedIndex(0);
            }

            this.selectScript((ZMCScript)null);
            SwingWorker worker = new SwingWorker() {
               protected Object doInBackground() throws Exception {
                  ZMCView.this.dialogPane.setEnabled(false);
                  ZMCView.this.scriptList.setEnabled(false);
                  ZMCView.this.progressBar.setValue(0);
                  ZMCView.this.progressBar.setVisible(true);

                  for(int i = 0; i < scripts.size(); ++i) {
                     if (i >= ZMCView.this.scriptListModel.size()) {
                        ZMCView.this.scriptListModel.addElement(new ZMCScript(i, ZMCView.this));
                     }

                     ((ZMCScript)ZMCView.this.scriptListModel.get(i)).setScript((List)scripts.get(i), romType);
                     int per = (int)((float)i / (float)scripts.size() * 100.0F);
                     ZMCView.this.progressBar.setValue(per);
                  }

                  ZMCView.this.progressBar.setVisible(false);
                  ZMCView.this.scriptList.setEnabled(true);
                  ZMCView.this.dialogPane.setEnabled(false);
                  return null;
               }
            };
            worker.execute();
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      }

   }

   public void initTileReaders() throws IOException {
      ZMCTileReader tileReader = new ZMCTileReader(this.romFile, 6894432, 16, 31, 112, 4);
      this.dialogPreview.putReader(16, tileReader);
      tileReader = new ZMCTileReader(this.romFile, 6912224, 16, 32, 128, 4);
      this.dialogPreview.putReader(128, tileReader);
      tileReader = new ZMCTileReader(this.romFile, 6909792, 16, 2, 32, 4);
      this.dialogPreview.putReader(15, tileReader);
      tileReader = new ZMCTileReader(this.romFile, 6957664, 18, 2, 9, 4);
      this.dialogPreview.putReader(12, tileReader);
      tileReader = new ZMCTileReader(this.romFile, 6891168, 7, 10, 70, 4);
      this.dialogPreview.putReader(0, tileReader);
   }

   public void initPalettes() throws IOException {
      ZMCPaletteReader paletteReader = new ZMCPaletteReader(this.romFile);
      int offsPal = 5910528;
      List<Color> whitePal = paletteReader.readPalette(offsPal, 16, true);
      this.dialogPreview.putPalette(0, whitePal);
      List<Color> redPal = paletteReader.readPalette(offsPal, 16, true);
      redPal.set(12, whitePal.get(7));
      redPal.set(13, whitePal.get(8));
      redPal.set(14, whitePal.get(9));
      this.dialogPreview.putPalette(1, redPal);
      List<Color> greenPal = paletteReader.readPalette(offsPal, 16, true);
      greenPal.set(12, whitePal.get(1));
      greenPal.set(13, whitePal.get(2));
      greenPal.set(14, whitePal.get(3));
      this.dialogPreview.putPalette(2, greenPal);
      List<Color> bluePal = paletteReader.readPalette(offsPal, 16, true);
      bluePal.set(12, whitePal.get(4));
      bluePal.set(13, whitePal.get(5));
      bluePal.set(14, whitePal.get(6));
      this.dialogPreview.putPalette(3, bluePal);
      List<Color> blackPal = paletteReader.readPalette(offsPal, 16, true);
      blackPal.set(13, whitePal.get(11));
      blackPal.set(14, whitePal.get(15));
      this.dialogPreview.putPalette(4, blackPal);
   }

   public ZMCPreview getDialogPreview() {
      return this.dialogPreview;
   }

   public static void main(String[] args) {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         UIManager.put("TextPane.font", new FontUIResource("Segoe UI", 0, 14));
         UIManager.put("TextPane.background", UIManager.getColor("TextField.background"));
         UIManager.put("TextPane.border", UIManager.getBorder("TextField.border"));
         EventQueue.invokeLater(new Runnable() {
            public void run() {
               new ZMCView();
            }
         });
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
