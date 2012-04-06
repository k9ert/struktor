
import java.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Command extends StrukElement{

public Command(int posx,int posy, int width, int height, String Label)
{
 super(posx,posy, width, height, Label);
}


public void rebuild(Vector StrukList)
{
       StrukElement e1=null;
       int bestdist = Integer.MAX_VALUE;
       for (Enumeration el=StrukList.elements(); el.hasMoreElements();)
       {
            StrukElement n = (StrukElement)el.nextElement();
            if (n == this)
               continue;
            if (n.x() < x)
               continue;
            if (n.y() < y)
               continue;
            int dist = (n.x() - x) * (n.x() - x) + (n.y() - y) * (n.y() - y);
            if (dist < bestdist) {
                e1 = n;
                bestdist = dist;
            }
       }
   // und zuweisen
   //falls doch falsch lieber auf null setzen
   if ((e1.x() < x) || (e1.y() < y))
      e1=null;
   if (e1!=null)
   {
      next(e1);
      next().rebuild(StrukList);
   }
}

public void paint(Graphics g) {
       super.paint(g);   // DragPoint und Target-Point
       g.drawString(Label,x+(width()/3),y+15);
       g.drawRect(x, y, width(), height());
}
public void paintAll(Graphics g) {
       paint(g);
       if (next()!=null)
       {
          calcNext();
          next().paintAll(g);
       }
}
public void paintStrukPoint(Graphics g)
{      super.paintStrukPoint(g); // Target-Point
       g.setColor(new Color(255,0,0));
       // der Source-Point in rot
       if (next()==null)
           g.drawRect(x+getWidth()-10,y+getHeight()-10,10,10);
       else
           g.fillRect(x+getWidth()-10,y+getHeight()-10,10,10);
       g.setColor(new Color(0,0,0));
}

// Event-Handling
public void mousePressed(MouseEvent e) {
       super.mousePressed(e);
       struktor.repaint();
       e.consume();
}
protected int mouseOnCorner(MouseEvent e)
{
// Right Bottom Corner (Eventuell gleich Right Top Corner eines anderen Elementes ! )
       if( (Math.abs(e.getX()-x-getWidth()+10) <= 10) && (Math.abs(e.getY()-y-getHeight()+10) <= 10) )
       {
           System.out.println("Right Bottom Corner hit e:"+e.getX()+","+e.getY()+" objekt:"+x+","+y);
           return 3;
       }
       int i=0;
       i=super.mouseOnCorner(e);
       if (i!=0)
           return i;
       return 0;

}

// Ende der Klasse
}