package ui;

import core.ZMCTables;
import core.ZMCTileReader;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

public class ZMCTileCanvas extends JPanel {
   private Map<Integer, ZMCTileReader> tileReaderMap = new HashMap();
   private Map<Integer, List<Color>> paletteMap = new HashMap();
   private BufferedImage screen;
   private int windowType = 0;
   private int dialogRows = 2;
   private int dialogCols = 26;
   private int scale = 1;
   private String dialogText = "Fugiu mais uma vez do castelo e veio\nsozinha até aqui?\nO ministro deve estar muito preocupado\ncom você! Sabe como ele é, né?!";
   private boolean isShowPalette = false;

   public ZMCTileCanvas() {
      this.setPreferredSize(new Dimension(240 * this.scale, 0));
   }

   public void putReader(Integer id, ZMCTileReader tileReader) {
      this.tileReaderMap.put(id, tileReader);
   }

   public void putPalette(Integer id, List<Color> palette) {
      this.paletteMap.put(id, palette);
   }

   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (this.isShowPalette) {
         BufferedImage palleteImg = this.getPaletteImg();
         g.drawImage(palleteImg, 0, 0, (ImageObserver)null);
      }

      if (this.screen != null) {
         int dw = this.screen.getWidth() * this.scale;
         int dh = this.screen.getHeight() * this.scale;
         g.drawImage(this.screen, 0, 0, dw, dh, (ImageObserver)null);
      }

   }

   public BufferedImage getPaletteImg() {
      List<Color> pallete = (List)this.paletteMap.get(0);
      BufferedImage bip = new BufferedImage(pallete.size() * 8, 8, 2);
      Graphics grp = bip.getGraphics();

      for(int p = 0; p < pallete.size(); ++p) {
         grp.setColor(new Color(((Color)pallete.get(p)).getRGB()));
         grp.fillRect(p * 8, 0, 8, 8);
      }

      return bip;
   }

   public void drawScreen() {
      if (this.dialogText.isEmpty()) {
         this.screen = new BufferedImage((this.dialogCols + 4) * 8, 1, 2);
      } else {
         String[] lines = this.dialogText.split("\n");
         List<String> dialogues = new ArrayList();
         StringBuilder dialog = new StringBuilder();
         int cl = 0;

         for(int i = 0; i < lines.length; ++i) {
            dialog.append(lines[i]);
            ++cl;
            if (cl < this.dialogRows && i < lines.length - 1) {
               dialog.append("\n");
            } else {
               dialogues.add(dialog.toString());
               dialog = new StringBuilder();
               cl = 0;
            }
         }

         BufferedImage windowImg = this.createWindowImage();
         int screenW = windowImg.getWidth() + 16;
         int screenH = (windowImg.getHeight() + 8) * dialogues.size();
         this.screen = new BufferedImage(screenW, screenH, 2);
         Graphics screenG = this.screen.getGraphics();
         int dx = 8;
         int dy = 8;

         for(Iterator var11 = dialogues.iterator(); var11.hasNext(); dy += windowImg.getHeight() + 8) {
            String string = (String)var11.next();
            BufferedImage dialogImg = this.createDialogImage(string);
            screenG.drawImage(windowImg, dx, dy, (ImageObserver)null);
            screenG.drawImage(dialogImg, dx + 8, dy + 9, (ImageObserver)null);
         }

      }
   }

   private BufferedImage createWindowImage() {
      int winW = (this.dialogCols + 2) * 8;
      int winH = (this.dialogRows + 1) * 16;
      int tileW = 8;
      int tileH = 8;
      int type = this.windowType * 16;
      List<Color> pallete = new ArrayList((Collection)this.paletteMap.get(0));
      pallete.set(10, Color.BLACK);
      ZMCTileReader tileReader = (ZMCTileReader)this.tileReaderMap.get(0);
      BufferedImage window = new BufferedImage(winW, winH, 2);
      Graphics windowG = window.getGraphics();
      windowG.setColor(Color.BLACK);
      windowG.fillRect(8, 8, winW - 8, winH - 8);
      int[][] borderTiles = new int[20][];

      int ln;
      for(ln = 0; ln < 7; ++ln) {
         borderTiles[ln] = tileReader.readBlockTile(ln + type, 1, 1);
      }

      borderTiles[7] = this.flipHorizontal(borderTiles[0], tileW, tileH);
      borderTiles[8] = this.flipHorizontal(borderTiles[1], tileW, tileH);
      borderTiles[9] = this.flipHorizontal(borderTiles[3], tileW, tileH);
      borderTiles[10] = this.flipHorizontal(borderTiles[4], tileW, tileH);
      borderTiles[11] = this.flipVertical(borderTiles[0], tileW, tileH);
      borderTiles[12] = this.flipVertical(borderTiles[1], tileW, tileH);
      borderTiles[13] = this.flipVertical(borderTiles[2], tileW, tileH);
      borderTiles[14] = this.flipVertical(borderTiles[3], tileW, tileH);
      borderTiles[15] = this.flipVertical(borderTiles[7], tileW, tileH);
      borderTiles[16] = this.flipVertical(borderTiles[8], tileW, tileH);
      borderTiles[17] = this.flipVertical(borderTiles[9], tileW, tileH);
      this.paintTile(borderTiles[0], tileW, tileH, window, 0, 0, pallete);
      this.paintTile(borderTiles[1], tileW, tileH, window, 8, 0, pallete);

      for(ln = 0; ln < winW - 32; ln += 8) {
         this.paintTile(borderTiles[2], tileW, tileH, window, ln + 16, 0, pallete);
      }

      this.paintTile(borderTiles[8], tileW, tileH, window, winW - 16, 0, pallete);
      this.paintTile(borderTiles[7], tileW, tileH, window, winW - 8, 0, pallete);
      this.paintTile(borderTiles[3], tileW, tileH, window, 0, 8, pallete);
      this.paintTile(borderTiles[9], tileW, tileH, window, winW - 8, 8, pallete);
      ln = (this.dialogRows - 1) * 2;

      int i;
      for(i = 0; i < ln; ++i) {
         int y = i * 8 + 16;
         this.paintTile(borderTiles[4], tileW, tileH, window, 0, y, pallete);
         this.paintTile(borderTiles[10], tileW, tileH, window, winW - 8, y, pallete);
      }

      this.paintTile(borderTiles[14], tileW, tileH, window, 0, winH - 16, pallete);
      this.paintTile(borderTiles[17], tileW, tileH, window, winW - 8, winH - 16, pallete);
      this.paintTile(borderTiles[11], tileW, tileH, window, 0, winH - 8, pallete);
      this.paintTile(borderTiles[12], tileW, tileH, window, 8, winH - 8, pallete);

      for(i = 0; i < winW - 32; i += 8) {
         this.paintTile(borderTiles[13], tileW, tileH, window, i + 16, winH - 8, pallete);
      }

      this.paintTile(borderTiles[16], tileW, tileH, window, winW - 16, winH - 8, pallete);
      this.paintTile(borderTiles[15], tileW, tileH, window, winW - 8, winH - 8, pallete);
      return window;
   }

   public int[] flipHorizontal(int[] src, int w, int h) {
      int[] dest = new int[src.length];
      int si = 0;

      for(int y = 0; y < h; ++y) {
         int di = si + h;

         for(int x = 0; x < w; ++x) {
            --di;
            dest[di] = src[si++];
         }
      }

      return dest;
   }

   public int[] flipVertical(int[] src, int w, int h) {
      int[] dest = new int[src.length];
      int si = 0;

      for(int y = 0; y < h; ++y) {
         int di = w * (h - y - 1);

         for(int x = 0; x < w; ++x) {
            dest[di++] = src[si++];
         }
      }

      return dest;
   }

   private void paintTile(int[] src, int w, int h, BufferedImage img, int dx, int dy, List<Color> palette) {
      int ti = 0;

      for(int y = 0; y < h; ++y) {
         for(int x = 0; x < w; ++x) {
            img.setRGB(dx + x, dy + y, ((Color)palette.get(src[ti++])).getRGB());
         }
      }

   }

   private BufferedImage createDialogImage(String dialog) {
      int dialogW = this.dialogCols * 8;
      int dialogH = this.dialogRows * 16;
      int tileWidth = 8;
      int tileHeight = 16;
      BufferedImage dialogImage = new BufferedImage(dialogW, dialogH, 2);
      int dx = 0;
      int dy = 0;
      List<Color> curPalette = (List)this.paletteMap.get(0);
      dialog = dialog.replaceAll("\\[var:00\\]", "[green]Link[white]");

      for(int di = 0; di < dialog.length(); ++di) {
         String chr = dialog.charAt(di) + "";
         if (chr.equals("\n")) {
            dy += tileHeight;
            dx = 0;
         } else {
            if (chr.equals("[")) {
               int ti = di + 1;
               StringBuilder sbTag = new StringBuilder();

               boolean tagFound;
               for(tagFound = false; !tagFound; ++ti) {
                  if (dialog.charAt(ti) == ']') {
                     tagFound = true;
                     di = ti;
                     break;
                  }

                  if (ti == dialog.length() || dialog.charAt(ti) == '\n') {
                     break;
                  }

                  sbTag.append(dialog.charAt(ti));
               }

               if (tagFound) {
                  if (ZMCTables.dict02XX.containsKey(sbTag.toString())) {
                     curPalette = (List)this.paletteMap.get(ZMCTables.dict02XX.get(sbTag.toString()));
                  }
                  continue;
               }
            }

            int[] tileIndexes = new int[tileWidth * tileHeight];
            int idxBl = false;
            tileWidth = 8;
            int idxBl;
            if (ZMCTables.dict10.containsKey(chr)) {
               idxBl = (Integer)ZMCTables.dict10.get(chr);
               int idxTileMap = 16;
               if (idxBl >= 128) {
                  idxTileMap = 128;
               }

               tileIndexes = ((ZMCTileReader)this.tileReaderMap.get(Integer.valueOf(idxTileMap))).readBlockTile(idxBl - idxTileMap, 1, 2);
            } else if (ZMCTables.dict0FXX.containsKey(chr)) {
               idxBl = (Integer)ZMCTables.dict0FXX.get(chr);
               tileIndexes = ((ZMCTileReader)this.tileReaderMap.get(15)).readBlockTile(idxBl, 1, 2);
            } else if (ZMCTables.dict0CXX.containsKey(chr)) {
               idxBl = (Integer)ZMCTables.dict0CXX.get(chr);
               tileIndexes = ((ZMCTileReader)this.tileReaderMap.get(12)).readBlockTile(idxBl, 2, 2);
               tileWidth = 16;
            }

            int startPos = 0;
            int endPos = tileWidth;
            boolean findStart = false;

            int vwf;
            for(vwf = 0; vwf < tileWidth; ++vwf) {
               if (!findStart) {
                  if (tileIndexes[vwf] == 15) {
                     ++startPos;
                  } else {
                     findStart = true;
                     endPos = startPos;
                  }
               } else {
                  if (tileIndexes[vwf] == 15) {
                     break;
                  }

                  ++endPos;
               }
            }

            vwf = endPos - startPos + 1;
            int ti = startPos;

            for(int ty = 0; ty < tileHeight; ++ty) {
               for(int tx = 0; tx < vwf; ++tx) {
                  if (dx + tx < dialogImage.getWidth() && dy + ty < dialogImage.getHeight()) {
                     int colorIndex = tileIndexes[ti + tx];
                     dialogImage.setRGB(dx + tx, dy + ty, ((Color)curPalette.get(colorIndex)).getRGB());
                  }
               }

               ti += tileWidth;
            }

            dx += vwf;
         }
      }

      return dialogImage;
   }

   private void updateDimension() {
      this.setPreferredSize(new Dimension(this.screen.getWidth() * this.scale, this.screen.getHeight() * this.scale));
   }

   public void setDialogText(String dialogText) {
      this.dialogText = dialogText;
      this.refresh();
   }

   public void refresh() {
      this.drawScreen();
      this.repaint();
      this.updateDimension();
      this.revalidate();
   }

   public int getScale() {
      return this.scale;
   }

   public void setScale(int scale) {
      this.scale = scale;
      this.refresh();
   }

   public int getWindowType() {
      return this.windowType;
   }

   public void setWindowType(int windowType) {
      this.windowType = windowType;
      this.refresh();
   }

   public int getDialogCols() {
      return this.dialogCols;
   }

   public void setDialogCols(int dialogCols) {
      this.dialogCols = dialogCols;
      this.refresh();
   }

   public int getDialogRows() {
      return this.dialogRows;
   }

   public void setDialogRows(int dialogRows) {
      this.dialogRows = dialogRows;
      this.refresh();
   }
}
