package core;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ZMCPaletteReader {
   private File romFile;

   public ZMCPaletteReader(File file) {
      this.romFile = file;
   }

   public List<Color> readPalette(int offset, int numColors, boolean isFirstAlpha) throws IOException {
      FileInputStream inSt = new FileInputStream(this.romFile);
      inSt.skip((long)offset);
      List<Color> palette = new ArrayList();

      int rgb;
      for(rgb = 0; rgb < numColors; ++rgb) {
         int l = inSt.read();
         int h = inSt.read();
         int rgb = h << 8 | l;
         int b = (rgb >> 10 & 31) * 8;
         int g = (rgb >> 5 & 31) * 8;
         int r = (rgb >> 0 & 31) * 8;
         Color color = new Color(r, g, b, 255);
         palette.add(color);
      }

      inSt.close();
      if (isFirstAlpha && !palette.isEmpty()) {
         rgb = ((Color)palette.get(0)).getRGB();
         palette.set(0, new Color(rgb & 16777215, true));
      }

      return palette;
   }
}
