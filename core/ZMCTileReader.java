package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ZMCTileReader {
   private final int tileW = 8;
   private final int tileH = 8;
   private byte[] dataBuffer;
   private int rows;
   private int cols;
   private int bpp;
   private int length;
   private int offset;
   private int blockWidth;
   private int blockHeight;
   private int tileSize;

   public ZMCTileReader(File file, int offset, int cols, int rows, int length, int bpp) throws IOException {
      this.rows = rows;
      this.cols = cols;
      this.bpp = bpp;
      this.length = length;
      this.offset = offset;
      this.initDataBuffer(file, offset, cols, rows, length, bpp);
   }

   private void initDataBuffer(File file, int offset, int cols, int rows, int len, int bpp) throws IOException {
      FileInputStream fin = new FileInputStream(file);
      fin.skip((long)offset);
      this.tileSize = 64 / (8 / bpp);
      this.dataBuffer = new byte[this.tileSize * cols * rows * len];
      fin.read(this.dataBuffer);
      fin.close();
   }

   private void printHexBlockData(int[] blockData, int width, int height) {
      int i = 0;

      for(int y = 0; y < height; ++y) {
         for(int x = 0; x < width; ++x) {
            System.out.print(String.format("%02X", blockData[i++]));
         }

         System.out.println();
      }

      System.out.println();
   }

   public int[] readBlockTile(int index, int cols, int rows) {
      int tx = index & 15;
      int ty = index >> 4 & 15;
      int tileSize = 64;
      int blockSize = tileSize * rows * cols;
      int bufOffset = (tx + ty * this.cols) * (blockSize / 2);
      int stride = cols * 8;
      int[] blockData = new int[blockSize];

      for(int c = 0; c < cols; ++c) {
         int cx = c * 8;

         for(int r = 0; r < rows; ++r) {
            int xy = r * cols * tileSize + cx;

            for(int y = 0; y < 8; ++y) {
               int dxy = xy + y * stride;

               for(int x = 0; x < 8; x += 2) {
                  int texel = this.dataBuffer[bufOffset] & 255;
                  ++bufOffset;
                  int px = texel & 15;
                  blockData[dxy + x] = px;
                  px = texel >> 4 & 15;
                  blockData[dxy + x + 1] = px;
               }
            }
         }
      }

      return blockData;
   }
}
