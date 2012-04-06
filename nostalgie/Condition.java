import java.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Condition extends StrukElement{

StrukElement alt1=null;
StrukElement alt2=null;

public Condition()
{
 super(10,10, 300, 300, "");
}

public Condition(int posx,int posy, int width, int height, String Label)
{
 super(posx,posy, width, height, Label);
}


public void alt1(StrukElement alt1)
{
       this.alt1=alt1;
}

public void alt2(StrukElement alt2)
{
       this.alt2=alt2;
}

public StrukElement alt1()
{
       return alt1;
}

public StrukElement alt2()
{
       return alt2;
}

public void calcNext()
{
  if (next()!=null)
  {
     next().x=x;
     if (alt1!=null)
        next().y=y+this.height()+alt1.height();;
     next().setWidth(this.width());
  }
}

public void calcAlt(){
       if (alt1!=null)
       {
        alt1.x=this.x;
        alt1.y=this.y+this.height();
        alt1.setWidth(this.width()/2);
       }
       if (alt2!=null)
       {
        alt2.x=this.x+this.width()/2;
        alt2.y=this.y+this.height();
        alt2.setWidth(width()/2);
       }
}

public void rebuild(Vector StrukList)
{
       StrukElement e1=null,e2=null,e3=null, temp=null;
       double dist1=0,dist2=0,dist3=0;
       double bestdist = Double.MAX_VALUE;
       for (Enumeration el=StrukList.elements(); el.hasMoreElements();)
       {
            StrukElement n = (StrukElement)el.nextElement();
            if ((n.x < x) || (n.y < y))
               continue;
            double dist = (n.x - x) * (n.x - x) + (n.y - y) * (n.y - y);
            if (dist < bestdist) {
                e3 = e2;
                dist3=dist2;
                e2 = e1;
                dist2=dist1;
                e1 = n;
                dist1=dist;
                bestdist = dist;
            }
       }
// Sortieren
       if (dist1 > dist2)
       {
          if (dist1 > dist3)
          {
             if (dist2 > dist3)
                 ;
             else
                 { temp=e2; e2=e3; e3=temp;}
          }
          else /* dist3 dist1 dist2 */
          {
              temp=e1; e1=e3; e3=e2; e2=temp;
          }
       }

// und zuweisen
   next(e1);
   alt1=e2;
   alt2=e3;
   next().rebuild(StrukList);
}

public void paint(Graphics g) {
       int width = getSize().width;
       int height = getSize().height;
       super.paint(g);
       g.drawString(Label,x+(width/3),y+15);
       g.drawString("True",x+(width/7),y+height-5);
       g.drawString("False",x+(width/6*4),y+height-5);
       g.drawRect(x, y, width, height);
       g.drawLine(x,y,x+width/2,y+height);
       g.drawLine(x+width/2,y+height,x+width,y);
}
public void paintAll(Graphics g) {
       paint(g);
       calcAlt();
       if (alt1!=null)
          alt1.paintAll(g);
       if (alt2!=null)
          alt2.paintAll(g);
       if (next()!=null)
       {
          calcNext();
          next().paintAll(g);
       }
}

public void paintStrukPoint(Graphics g)
{
     super.paintStrukPoint(g); // Target-Point
     g.setColor(new Color(255,0,0));
     // Der Condition one Point in rot
     if (alt1==null)
         g.drawRect(x+getWidth()/2-10,y+getHeight()-10,10,10);
     else
         g.fillRect(x+getWidth()/2-10,y+getHeight()-10,10,10);
     // Der Condition two Point in rot
     if (alt1==null)
         g.drawRect(x+getWidth()-10,y+getHeight()-10,10,10);
     else
         g.fillRect(x+getWidth()-10,y+getHeight()-10,10,10);
     g.setColor(new Color(0,0,0));
}

// Event-Handling
protected int mouseOnCorner(MouseEvent e)
{
     // Condition one Corner
     if((Math.abs(e.getX()-x-getWidth()/2+10) < sensible) && (Math.abs(e.getY()-y-getHeight()+10) < sensible))
         {System.out.println("Condition one Corner hit e:"+e.getX()+","+e.getY()+" objekt:"+x+","+y);
         return 21;}
     // Condition Two Corner
     if((Math.abs(e.getX()-x-getWidth()+10) < sensible) && (Math.abs(e.getY()-y-getHeight()+10) < sensible))
         {System.out.println("Condition one Corner hit e:"+e.getX()+","+e.getY()+" objekt:"+x+","+y);
         return 22;}
     int i=0;
     i=super.mouseOnCorner(e);
     if (i!=0)
        return i;
     return 0;
}

private void alt1PointPressed(MouseEvent e)
{
     if (struktogramm.strukPoint==true)
     {  struktogramm.source(this);
        struktogramm.setConnectType(3);
        System.out.println("Condition one Point hit ! ConnectType now "+struktogramm.getConnectType());
     }
}
private void alt2PointPressed(MouseEvent e)
{
     if (struktogramm.strukPoint==true)
     {  struktogramm.source(this);
        struktogramm.setConnectType(4);
        System.out.println("Condition two Point hit ! ConnectType now "+struktogramm.getConnectType());
     }
}

public void mousePressed(MouseEvent e) {
     switch (mouseOnCorner(e))
     {
     case 21:
             alt1PointPressed(e);
             break;
     case 22:
             alt2PointPressed(e);
             break;
     }
     super.mousePressed(e);
     struktor.repaint();
     e.consume();
}
}