package core;

import javax.swing.DefaultListModel;
import ui.ZMCScript;

public class ZMCScriptListModel extends DefaultListModel<ZMCScript> {
   public ZMCScript getNextScript(ZMCScript script) {
      int index = this.indexOf(script) + 1;
      if (index >= this.getSize()) {
         index = 0;
      }

      return (ZMCScript)this.get(index);
   }

   public ZMCScript getPreviousScript(ZMCScript script) {
      int index = this.indexOf(script) - 1;
      if (index < 0) {
         index = this.getSize() == 0 ? 0 : this.getSize() - 1;
      }

      return (ZMCScript)this.get(index);
   }

   public boolean isLastScript(ZMCScript script) {
      return this.indexOf(script) == this.getSize() - 1;
   }

   public boolean isFirstScript(ZMCScript script) {
      return this.indexOf(script) == 0;
   }

   public void add(ZMCScript element) {
      super.addElement(element);
   }
}
