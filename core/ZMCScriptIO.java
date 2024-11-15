package core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ZMCScriptIO {
   public static final int JP_LOCALE = 0;
   public static final int EN_LOCALE = 1;
   public static final int START_OFFSET_EN = 10165648;
   public static final int REALOC_OFFSET_EN = 14581156;
   public static final int END_OFFSET_EN = 10467184;
   public static final int JP_OFFSET = 1;

   public static List<List<String>> read(File file, int offset, int locale) throws IOException {
      List<List<String>> scripts = new ArrayList();
      RandomAccessFile fileReader = new RandomAccessFile(file, "r");
      List<Integer> scriptPts = readPointers(fileReader, offset, 4, 80);

      for(int i = 0; i < scriptPts.size(); ++i) {
         List<Integer> dialogPts = readPointers(fileReader, (Integer)scriptPts.get(i), 4, -1);
         List<String> dialog = new ArrayList();
         scripts.add(dialog);

         for(int j = 0; j < dialogPts.size(); ++j) {
            fileReader.seek((long)(Integer)dialogPts.get(j));
            int value = fileReader.read();

            StringBuilder string;
            for(string = new StringBuilder(); value != 0; value = fileReader.read()) {
               int code;
               switch(value) {
               case 1:
               case 6:
               case 8:
               case 9:
                  code = fileReader.read();
                  string.append(ZMCTables.formatTagCode(value, code, 1));
                  break;
               case 2:
                  code = fileReader.read() & 7;
                  string.append(String.format("[%s]", ZMCTables.table02XX[code]));
                  break;
               case 3:
               case 7:
                  code = fileReader.readShort() & '\uffff';
                  string.append(ZMCTables.formatTagCode(value, code, 2));
                  break;
               case 4:
                  code = fileReader.read();
                  if (code == 16) {
                     code = code << 8 | fileReader.read();
                     string.append(ZMCTables.formatTagCode(value, code, 2));
                  } else {
                     string.append(ZMCTables.formatTagCode(value, code, 1));
                  }
                  break;
               case 5:
                  code = fileReader.read();
                  if (code == 255) {
                     string.append(ZMCTables.formatTagCode(value, code, 1));
                  } else {
                     code = code << 8 | fileReader.read();
                     string.append(ZMCTables.formatTagCode(value, code, 2));
                  }
                  break;
               case 10:
                  string.append("\n");
                  break;
               case 11:
               default:
                  int idx = 16;
                  if (locale == 0 && value >= 128) {
                     idx = 128;
                  }

                  value -= idx;
                  if (value >= ZMCTables.getCharTable(idx).length) {
                     String strHex = String.format("[0x%02X]", value + idx);
                     string.append(strHex);
                  } else {
                     string.append(ZMCTables.getCharTable(idx)[value]);
                  }
                  break;
               case 12:
               case 13:
               case 14:
               case 15:
                  code = fileReader.read();
                  if (value == 14) {
                     code += 217;
                  }

                  if (code >= ZMCTables.getCharTable(value).length) {
                     String hexTag = String.format("[0x%04X]", value << 8 | code);
                     string.append(hexTag);
                  } else {
                     string.append(ZMCTables.getCharTable(value)[code]);
                  }
               }
            }

            dialog.add(string.toString());
         }
      }

      fileReader.close();
      return scripts;
   }

   public static void write(List<List<String>> scripts, File file, int offset) throws IOException {
      byte[][] binaryScripts = new byte[scripts.size()][];

      int i;
      int j;
      for(int si = 0; si < scripts.size(); ++si) {
         List<String> script = (List)scripts.get(si);
         ArrayList<List<Integer>> scriptData = new ArrayList();
         Iterator var7 = script.iterator();

         while(var7.hasNext()) {
            String dialog = (String)var7.next();
            ArrayList<Integer> dialogData = new ArrayList();
            i = 0;

            while(true) {
               while(i < dialog.length()) {
                  if (dialog.charAt(i) == '[') {
                     j = i + 1;
                     StringBuilder tag = new StringBuilder();

                     boolean tagFound;
                     for(tagFound = false; j < dialog.length(); ++j) {
                        if (dialog.charAt(j) == '\n') {
                           ++i;
                           break;
                        }

                        if (dialog.charAt(j) == ']') {
                           tagFound = true;
                           i = j + 1;
                           break;
                        }

                        tag.append(dialog.charAt(j));
                     }

                     if (tagFound) {
                        int value;
                        if (tag.toString().startsWith("0x")) {
                           String strHex = tag.toString().substring(2);
                           value = Integer.parseInt(strHex, 16);
                           if (value > 255) {
                              dialogData.add(value >>> 8);
                           }

                           dialogData.add(value & 255);
                        } else if (ZMCTables.dict02XX.containsKey(tag.toString())) {
                           dialogData.add(2);
                           dialogData.add(ZMCTables.dict02XX.get(tag.toString()));
                        } else {
                           String[] parts = tag.toString().split(":");
                           if (ZMCTables.dictCodes.containsKey(parts[0])) {
                              dialogData.add(ZMCTables.dictCodes.get(parts[0].trim()));
                              value = Integer.parseInt(parts[1].trim(), 16);
                              if (parts[1].length() > 2) {
                                 dialogData.add(value >> 8 & 255);
                              }

                              dialogData.add(value & 255);
                           }
                        }
                     }
                  } else {
                     String chr = dialog.charAt(i) + "";
                     if (chr.equals("♪")) {
                        dialogData.add(15);
                        dialogData.add(11);
                     } else if (ZMCTables.dict10.containsKey(chr)) {
                        dialogData.add(ZMCTables.dict10.get(chr));
                     } else if (ZMCTables.dict0CXX.containsKey(chr)) {
                        dialogData.add(12);
                        dialogData.add(ZMCTables.dict0CXX.get(chr));
                     } else if (ZMCTables.dict0FXX.containsKey(chr)) {
                        dialogData.add(15);
                        dialogData.add(ZMCTables.dict0FXX.get(chr));
                     } else if (ZMCTables.dict0DXX.containsKey(chr)) {
                        dialogData.add(13);
                        dialogData.add(ZMCTables.dict0DXX.get(chr));
                     }

                     ++i;
                  }
               }

               dialogData.add(0);
               scriptData.add(dialogData);
               break;
            }
         }

         binaryScripts[si] = toBinaryScript(scriptData);
      }

      boolean realocData = false;
      int memAvaliable = 301536;
      int pointer = binaryScripts.length * 4;
      int[] blockPointers = new int[binaryScripts.length];

      for(int i = 0; i < blockPointers.length; ++i) {
         if (!realocData && memAvaliable < binaryScripts[i].length) {
            realocData = true;
            pointer = 4415508;
         }

         blockPointers[i] = pointer;
         pointer += binaryScripts[i].length;
         memAvaliable -= binaryScripts[i].length;
      }

      RandomAccessFile raf = new RandomAccessFile(file, "rw");
      raf.seek((long)offset);
      int[] var22 = blockPointers;
      i = blockPointers.length;

      for(j = 0; j < i; ++j) {
         Integer pt = var22[j];
         raf.writeInt(Integer.reverseBytes(pt));
      }

      realocData = false;
      memAvaliable = 301536;
      byte[][] var23 = binaryScripts;
      i = binaryScripts.length;

      for(j = 0; j < i; ++j) {
         byte[] bScript = var23[j];
         if (!realocData && memAvaliable < bScript.length) {
            realocData = true;
            raf.seek(14581156L);
         }

         raf.write(bScript);
         memAvaliable -= bScript.length;
      }

      raf.close();
   }

   private static byte[] toBinaryScript(ArrayList<List<Integer>> scriptData) throws IOException {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      addPointers(scriptData, bos, 4);
      Iterator var2 = scriptData.iterator();

      while(var2.hasNext()) {
         List<Integer> dialogData = (List)var2.next();
         Iterator var4 = dialogData.iterator();

         while(var4.hasNext()) {
            Integer data = (Integer)var4.next();
            bos.write(data);
         }
      }

      int align = 4;
      int rest = bos.size() % align;
      if (rest > 0) {
         align -= rest;
      }

      while(align > 0) {
         bos.write(255);
         --align;
      }

      byte[] binaryScript = bos.toByteArray();
      bos.close();
      return binaryScript;
   }

   private static void writeValueLE(int srcVal, OutputStream os, int len) throws IOException {
      for(int i = 0; i < len; ++i) {
         byte b = (byte)(srcVal >> i * 8 & 255);
         os.write(b);
      }

   }

   private static void addPointers(ArrayList<List<Integer>> scriptData, ByteArrayOutputStream bout, int ptSize) throws IOException {
      int pointer = scriptData.size() * ptSize;
      writeValueLE(pointer, bout, ptSize);

      for(int i = 0; i < scriptData.size() - 1; ++i) {
         pointer += ((List)scriptData.get(i)).size();
         writeValueLE(pointer, bout, ptSize);
      }

   }

   private static List<Integer> readPointers(RandomAccessFile fileReader, int fileOffset, int ptSize, int ptLen) throws IOException {
      List<Integer> pointers = new ArrayList();
      fileReader.seek((long)fileOffset);
      int firstPointer = readNumber(fileReader, ptSize);
      pointers.add(firstPointer + fileOffset);
      if (ptLen <= 0) {
         ptLen = firstPointer / ptSize;
      }

      for(int i = 0; i < ptLen - 1; ++i) {
         pointers.add(readNumber(fileReader, ptSize) + fileOffset);
      }

      return pointers;
   }

   private static int readNumber(RandomAccessFile fileReader, int size) throws IOException {
      int result = 0;

      for(int i = 0; i < size; ++i) {
         result |= fileReader.read() << i * 8;
      }

      return result;
   }
}
