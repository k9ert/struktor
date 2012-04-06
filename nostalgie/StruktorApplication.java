import java.applet.*;
import java.awt.*;

public class StruktorApplication
extends struktor
{
 public static void main(String args[])
 {
 Applet applet = new StruktorApplication();
 Frame frame = new AppLed("Struktor",applet,500,500);
 }

 StruktorApplication()
 {}
}

class AppLed extends Frame
{
      public AppLed (String Titel, Applet applet, int breite, int hoehe)
      {
      super(Titel);
      this.resize(breite,hoehe);
      this.show();
      applet.isVisible();
      applet.start();
      }
}