package core;

public enum ZMCByteCode {
   UN1("??1", 1, 1),
   CLR("clr", 2, 1),
   SND("snd", 3, 2),
   UN4("??4", 4, 1),
   OPT("opt", 5, 1),
   VAR("var", 6, 1),
   PTR("var", 7, 1),
   DLY("dly", 8, 1),
   DLD("dld", 9, 1);

   private String desc;
   private int code;
   private int size;

   private ZMCByteCode(String desc, int code, int size) {
      this.desc = desc;
      this.code = code;
      this.size = size;
   }

   public String getDesc() {
      return this.desc;
   }

   public int getCode() {
      return this.code;
   }

   public int getSize() {
      return this.size;
   }
}
