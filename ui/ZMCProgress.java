package ui;

import javax.swing.JDialog;
import javax.swing.JProgressBar;

public class ZMCProgress extends JDialog {
   private JProgressBar bar;

   public ZMCProgress(ZMCView owner) {
      super(owner);
      this.setSize(300, 20);
      this.setUndecorated(true);
      this.setAlwaysOnTop(true);
      this.bar = new JProgressBar();
      this.bar.setBorderPainted(false);
      this.bar.setStringPainted(true);
      this.getContentPane().add(this.bar);
   }

   public void setProgress(int value) {
      this.bar.setValue(value);
   }

   public void setVisible(boolean b) {
      this.bar.setValue(0);
      super.setVisible(b);
   }
}
