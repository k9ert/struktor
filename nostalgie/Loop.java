import java.applet.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class Loop extends StrukElement {

StrukElement inside=null;

public Loop()
{
 super(10,10, 300, 300, "");
}

public Loop(int posx,int posy, int width, int height, String Label)
{
 super(posx,posy, width, height, Label);
}

public void inside(StrukElement inside)
{
       if (inside!=null)
          inside.delConnection();
       if (this.inside!=null)
          this.inside.delConnection();
       this.inside=inside;
       // Schauen wir lieber nach ob zitkuläre Verknüpfungen vorliegen ...
       if (this.inside!=null)
       {
           try {
              struktogramm.checkStruktogramm(this.inside);
              }
           // Ohh, Meldung ausgeben ... und eigene next-Verknüpgung rückgängig machen
           catch(AllreadyPaintedException e){
                 System.out.println("Something wrong here !");
                 this.inside=null;
           }
       }

       if (this.inside!=null)
       {
          this.inside.connectedFrom=this;
          this.inside.connectionType=2;
       }
}
public StrukElement inside()
{
       return inside;
}
public boolean hasSubConnection()
{
 if (inside!=null)
    return true;
 if (super.hasSubConnection()==true)
    return true;
 else
    return false;
}

public void dissolve()
{
   StrukElement e;
   if (inside!=null)
   {
      e=inside;
      inside(null);
      e.dissolve();
   }
   super.dissolve();
}

public void checkStruktogramm()
throws AllreadyPaintedException
{
 if (painted==true)
    throw new AllreadyPaintedException();
 if (inside!=null)
    inside.checkStruktogramm();
 super.checkStruktogramm();
}
public void rebuild(Vector StrukList)
{
       StrukElement e1=null,e2=null, temp=null;
       int dist1=0,dist2=0;
       int bestdist = Integer.MAX_VALUE;
       for (Enumeration el=StrukList.elements(); el.hasMoreElements();)
       {
            StrukElement n = (StrukElement)el.nextElement();
            if (n == this)
               continue;
            if ((n.x < x) || (n.y < y))
               continue;
            int dist = (n.x - x) * (n.x - x) + (n.y - y) * (n.y - y);
            if (dist < bestdist) {
                e2 = e1;
                dist2=dist1;
                e1 = n;
                dist1=dist;
                bestdist = dist;
            }
       }
// Sortieren
       if (dist1 < dist2)
       {
          temp=e1;
          e1=e2;
          e2=temp;
       }
       if(e1!=null)
          inside(e1);
       else
          inside(null);
       if (e2!=null)
          next(e2);
       else
          next(null);
       if(next()!=null)
          next().rebuild(StrukList);
}

// Ab Hier Grafik + Events

private int insidex(){
       return x+getHorWidth();
}

private int insidey(){
       return y+getVerHeight();
}

private int insidemaxheight(){
       return getSize().height-getVerHeight();
}

private int insidemaxwidth(){
       return getSize().width-getHorWidth();
}
private int getHorWidth() {
       return getWidth()/10;
}
private int getVerHeight() {
       return getHorWidth();
}
public void calcInside(){
       if (inside!=null)
       {
        inside.x=this.insidex();
        inside.y=this.insidey();
        inside.setWidth(this.insidemaxwidth());
       }
}
public void paintAll(Graphics g)
{
       if (inside!=null)
       {
          calcInside();
          setHeight(getVerHeight()+inside.calcHeight());
          inside.paintAll(g);
       }
       paint(g);
       if(next()!=null)
       {
          calcNext();
          next().paintAll(g);
       }
}
public void paint(Graphics g)
{
       int width = getSize().width;
       int height = getSize().height;
       super.paint(g);
       g.drawString(Label,x+(width/3),y+15);
       // nach rechts
       g.drawLine(x, y, x+width, y);
       // kleines Stück runter
       g.drawLine(x+width, y, x+width, y+getVerHeight());
       // wieder nach links fast soweit
       g.drawLine(x+width, y+getVerHeight(), x+getHorWidth(), y+getVerHeight());
       // runter
       g.drawLine(x+getHorWidth(), y+getVerHeight(), x+getHorWidth(), y+height);
       // kleines Stück links)
       g.drawLine(x+getHorWidth(), y+height, x, y+height);
       // und wieder rauf
       g.drawLine(x, y+height, x, y);
}


public void paintStrukPoint(Graphics g)
{
     super.paintStrukPoint(g); // Target-Point
     // der Next-Point in rot
     g.setColor(new Color(255,0,0));
     if (next()==null)
        g.drawRect(x+getWidth()-10,insidey()-10,10,10);
     else
        g.fillRect(x+getWidth()-10,insidey()-10,10,10);
     // Der Inside-Point in rot
     if (inside==null)
        g.drawRect(insidex()-10, insidey()-10, 10,10);
     else
        g.fillRect(insidex()-10, insidey()-10, 10,10);
     g.setColor(new Color(0,0,0));
}
// Eventhandling

protected void insidePointPressed(MouseEvent e)  {
     if (struktogramm.strukPoint==true)
             {  if (e.isShiftDown()==true)
                {
                   inside(null);
                   System.out.println("inside Reset to null");
                }
                else
                {
                    struktogramm.source(this);
                    struktogramm.setConnectType(2);
                    System.out.println("Loop inside Point hit ! ConnectType now "+struktogramm.getConnectType());
                }
             }
}
public void mousePressed(MouseEvent e) {
     switch (mouseOnCorner(e))
     {
     case 11:
             insidePointPressed(e);
             break;
      case 3:
             nextPointPressed(e);
             break;
     }
     super.mousePressed(e);
     struktor.repaint();
     e.consume();
}
protected int mouseOnCorner(MouseEvent e)
{
     // Next Corner of Loop
     if ( (Math.abs(e.getX()-x-getWidth()+5) < sensible) && Math.abs(e.getY()-insidey()+10) < sensible )
        {System.out.println("Loop Next Corner hit e:"+e.getX()+","+e.getY()+" objekt:"+x+","+y);
        return 3;}
     // Inside Corner
     if((Math.abs(e.getX()-insidex()+5) < sensible) && (Math.abs(e.getY()-insidey()+5) < sensible))
         {System.out.println("Loop Inside Corner hit e:"+e.getX()+","+e.getY()+" objekt:"+x+","+y);
         return 11;}

     int i=0;
     i=super.mouseOnCorner(e);
     if (i!=0)
        return i;
     return 0;
}
}