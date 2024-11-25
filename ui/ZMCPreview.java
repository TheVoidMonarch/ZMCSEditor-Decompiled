package ui;

import core.ZMCTileReader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ZMCPreview extends JPanel {
   private ZMCTileCanvas canvas = new ZMCTileCanvas();
   private JScrollPane scrollPane = new JScrollPane(22, 31);

   public ZMCPreview() {
      super(new BorderLayout());
      this.scrollPane.setViewportView(this.canvas);
      this.add(this.scrollPane, "Center");
   }

   public ZMCTileCanvas getCanvas() {
      return this.canvas;
   }

   public void putReader(int id, ZMCTileReader tileReader) {
      this.canvas.putReader(id, tileReader);
   }

   public void putPalette(int id, List<Color> tileReader) {
      this.canvas.putPalette(id, tileReader);
   }

   public void setDialogText(String text) {
      this.canvas.setDialogText(text);
   }

   public void setWindowType(int index) {
      this.canvas.setWindowType(index);
      this.updateDimension();
   }

   public void setDialogCols(int cols) {
      this.canvas.setDialogCols(cols);
      this.updateDimension();
   }

   public void setDialogRows(int rows) {
      this.canvas.setDialogRows(rows);
      this.updateDimension();
   }

   public void setScale(int scale) {
      this.canvas.setScale(scale);
      this.updateDimension();
   }

   public void updateDimension() {
      Dimension dimension = this.canvas.getPreferredSize();
      int canvasW = this.canvas.getPreferredSize().width;
      int canvasH = this.canvas.getPreferredSize().height;
      if (canvasH > this.getHeight()) {
         int scrollBarW = this.scrollPane.getVerticalScrollBar().getPreferredSize().width;
         dimension = new Dimension(canvasW + scrollBarW, canvasH);
      }

      this.scrollPane.getViewport().setPreferredSize(dimension);
      this.getParent().revalidate();
   }
}
