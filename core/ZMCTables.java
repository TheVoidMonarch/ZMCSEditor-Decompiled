package core;

import java.util.HashMap;
import java.util.Map;

public class ZMCTables {
   public static Map<Integer, String[]> charMap;
   public static String[] table10 = new String[]{"あ", "ア", "い", "イ", "う", "を", "え", "エ", "お", "オ", "ぁ", "ァ", "ぅ", "ィ", "ぅ", "ゥ", " ", "!", "\"", "わ", "ワ", "っ", "ッ", "'", "(", ")", "、", "。", ",", "-", ".", "ヲ", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ":", "ぇ", "ェ", "ぉ", "ォ", "?", "~", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "め", "や", "ヤ", "ゆ", "ユ", "`", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "ヌ", "よ", "ヨ", "ん", "ン", "〿", "〿", "‚", "〿", "„", "…", "〿", "〿", "〿", "〿", "‹", "Š", "Œ", "〿", "Ž", "〿", "〿", "‘", "’", "“", "”", "•", "〿", "〿", "〿", "™", "š", "›", "œ", "〿", "ž", "Ÿ", "〿", "¡", "〿", "♪", "〿", "〿", "〿", "〿", "〿", "〿", "ª", "«", "〿", "〿", "〿", "〿", "°", "〿", "〿", "〿", "⌜", "〿", "〿", "·", "〿", "〿", "º", "»", "〿", "〿", "〿", "¿", "À", "Á", "Â", "Ã", "Ä", "Å", "Æ", "Ç", "È", "É", "Ê", "Ë", "Ì", "Í", "Î", "Ï", "Ð", "Ñ", "Ò", "Ó", "Ô", "Õ", "Ö", "×", "Ø", "Ù", "Ú", "Û", "Ü", "Ý", "Þ", "ß", "à", "á", "â", "ã", "ä", "å", "æ", "ç", "è", "é", "ê", "ë", "ì", "í", "î", "ï", "ð", "ñ", "ò", "ó", "ô", "õ", "ö", "÷", "ø", "ù", "ú", "û", "ü", "ý", "þ", "ÿ"};
   public static Map<String, Integer> dict10;
   public static String[] table80 = new String[]{"な", "ナ", "に", "ニ", "ー", "…", "ね", "ネ", "の", "ノ", "ま", "マ", "み", "ミ", "む", "ム", "め", "メ", "も", "モ", "ら", "ラ", "り", "リ", "る", "ル", "れ", "レ", "ろ", "ロ", "ゃ", "ャ", "ゅ", "ュ", "ょ", "ョ", "ヴ", "が", "ガ", "ぎ", "ギ", "ぐ", "グ", "げ", "ゲ", "ご", "ゴ", "ざ", "ザ", "じ", "ジ", "ず", "ズ", "ぜ", "ゼ", "ぞ", "ゾ", "だ", "ダ", "ぢ", "ヂ", "づ", "ヅ", "で", "デ", "ど", "ド", "ば", "バ", "び", "ビ", "ぶ", "ブ", "べ", "ベ", "ぼ", "ボ", "ぱ", "パ", "ぴ", "ピ", "ぷ", "プ", "ぺ", "ペ", "ぽ", "ポ", "ウ", "か", "カ", "き", "キ", "く", "ク", "け", "ケ", "こ", "コ", "さ", "サ", "し", "シ", "す", "ス", "せ", "セ", "そ", "ソ", "た", "タ", "ち", "チ", "つ", "ツ", "て", "テ", "と", "ト", "は", "ハ", "ひ", "ヒ", "ふ", "フ", "へ", "ヘ", "ほ", "ホ"};
   public static Map<String, Integer> dict80;
   public static String[] table02XX = new String[]{"white", "red", "green", "blue", "black", "white_nobg", "white_redbg", "red_bgred"};
   public static Map<String, Integer> dict02XX;
   public static String[] table0CXX = new String[]{"Ⓐ", "Ⓑ", "Ⓛ", "Ⓡ", "\u2b89", "\u2b88", "\u2b8b", "\u2b8a", "✚"};
   public static Map<String, Integer> dict0CXX;
   public static String[] table0DXX = new String[]{"　", "冒", "険", "者", "洞", "行", "世", "界", "集", "必", "要", "教", "覚", "今", "時", "石", "持", "上", "全", "員", "見", "中", "自", "動", "的", "目", "復", "活", "髙", "気", "伝", "蝕", "人", "勇", "出", "木", "実", "知", "赤", "カ", "青", "守", "緑", "歩", "速", "段", "階", "押", "一", "度", "投", "使", "体", "別", "剣", "以", "説", "方", "合", "弓", "矢", "意", "町", "何", "道", "大", "弱", "話", "風", "魔", "封", "印", "最", "近", "臣", "変", "感", "心", "配", "言", "強", "暴", "々", "災", "身", "戦", "少", "離", "用", "王", "断", "無", "礼", "手", "作", "美", "残", "念", "分", "我", "名", "連", "思", "年", "良", "花", "日", "宮", "殿", "助", "小", "姫", "向", "借", "抜", "去", "倒", "先", "住", "妖", "牆", "呪", "示", "会", "刻", "入", "真", "開", "画", "面", "右", "下", "早", "立", "再", "前", "受", "落", "長", "羽", "越", "果", "火", "解", "回", "帰", "救", "老", "古", "後", "光", "攻", "根", "特", "指", "事", "取", "盾", "域", "乗", "森", "聖", "昔", "足", "待", "達", "誰", "弾", "追", "敵", "当", "湖", "弟", "倍", "物", "壁", "返", "本", "満", "戻", "役", "神", "数", "応", "図", "専", "湯", "民", "電", "源", "次", "東", "子", "確", "他", "遊", "消", "氷", "炎", "多", "探", "©", "®", "左", "地", "悪", "善", "遠", "低", "水", "部", "西", "南", "北", "紫", "黄", "白", "黒", "岩", "山", "窟", "銀", "金", "川", "空", "海", "城", "色", "同", "庭", "引", "両", "点", "法", "穴", "場", "所", "台", "音", "転", "斬", "宝", "箱", "家", "草", "間", "彼", "式", "屋", "国", "武", "術", "質", "平", "苦", "和", "聞", "工", "ロ", "里", "折", "秘", "技", "優", "膀", "邪", "突", "兵", "原", "店", "主", "現", "女", "牧", "基", "流", "谷", "闇", "雲", "滝", "娘", "撃", "通", "信", "滅", "続", "士", "境", "丘", "林"};
   public static Map<String, Integer> dict0DXX;
   public static Map<String, Integer> dict0EXX;
   public static String[] table0FXX = new String[]{" ", "▶", "「", "」", "“", "×", "·", "↑", "↓", "←", "→", "♪", "❤", "&", "\"", "°", "#", "$", "%", "*", "+", "/", ";", "<", "=", ">", "@", "[", "¥", "]", "^", "_"};
   public static String[] tableCodes = new String[]{"end", "unk", "clr", "snd", "msc", "opt", "var", "ptr", "dly", "dld"};
   public static Map<String, Integer> dict0FXX;
   public static Map<String, Integer> dictCodes;

   private static Map<String, Integer> generateDictionary(String[] table, int startIdx) {
      Map<String, Integer> dict = new HashMap();

      for(int i = 0; i < table.length; ++i) {
         dict.put(table[i], i + startIdx);
      }

      return dict;
   }

   private static Map<String, Integer> generateDictionary(String[] table, int startIdx, int tableIdx, int len) {
      Map<String, Integer> dict = new HashMap();

      for(int i = 0; i < table.length; ++i) {
         dict.put(table[i], i + startIdx);
      }

      return dict;
   }

   public static String formatTagCode(int index, int value, int len) {
      return String.format("[%s:%0" + len * 2 + "X]", tableCodes[index], value);
   }

   public static String[] getCharTable(int idx) {
      return (String[])charMap.get(idx);
   }

   static {
      dict02XX = generateDictionary(table02XX, 0);
      dict0CXX = generateDictionary(table0CXX, 0);
      dict0DXX = generateDictionary(table0DXX, 0);
      dict0EXX = generateDictionary(table0DXX, 0, 217, table0DXX.length);
      dict0FXX = generateDictionary(table0FXX, 0);
      dict10 = generateDictionary(table10, 16);
      dict10.put("\n", 10);
      dictCodes = generateDictionary(tableCodes, 0);
      charMap = new HashMap();
      charMap.put(16, table10);
      charMap.put(128, table80);
      charMap.put(12, table0CXX);
      charMap.put(13, table0DXX);
      charMap.put(14, table0DXX);
      charMap.put(15, table0FXX);
   }
}
