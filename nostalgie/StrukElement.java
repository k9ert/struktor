import java.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public abstract class StrukElement extends Component
implements MouseListener, MouseMotionListener
{
//Instanzenvariablen
static StrukElement StrukElement[] = new StrukElement[100];
static int Anzahl=0;
static int Number=0;
static java.awt.Color defaultColor = java.awt.Color.black;
static Struktor struktor;
// Gibt an wie sensibel die Punkte reagieren
static int sensible=5;
static Struktogramm struktogramm;
// Objektvariablen

int x,y,number;
String Label;
private StrukElement next=null;
StrukElement connectedFrom=null;
int connectionType;
boolean painted=false;
boolean resizeWidth=false;
boolean resizeHeight=false;

protected void setMyChiefs(Struktor s, Struktogramm gramm)
{
 struktor=s;
 struktogramm=gramm;
}

public StrukElement(Struktor s)
{
 x=10;
 y=10;
 Dimension size = new Dimension(300,300);
 setSize(size);
 Label="";
 Anzahl++;
 number=Anzahl;
 struktor=s;
}

public StrukElement(int posx,int posy, int width, int height, String Label)
{
  this.x=posx;
  this.y=posy;
  setSize(new Dimension(width,height));
  this.Label=Label;
  Anzahl++;
  Number++;
  number=Number;
}

public void finalize()
{
  Anzahl--;
}

public int y()
{
  return y;
}

public int x()
{
  return x;
}

public int width()
{
  return getSize().width;
}

public int height()
{
  return getSize().height;
}

public void setWidth(int width)
{
   setSize(new Dimension(width,getSize().height));
}

public void setHeight(int height)
{
   setSize(new Dimension(getSize().width,height));
}

private int nextx()
{
  return x;
}

private int nexty()
{
  return y+getSize().height;
}
/* protected void setConnectedFrom(StrukElement s) {
  connectedFrom=s;
}
protected StrukElement getConnectedFrom(StrukElement s) {
  connectedFrom=s;
}  */
public int getNumber()
{
  return number;
}
public boolean hasSubConnection()
{
 if (next!=null)
    return true;
 else
    return false;
}
public void delConnection() {
if (connectedFrom!=null) // ist bereits verknüpft !!
  {
     switch(connectionType)
     {
     case 1:
          connectedFrom.next=null;
          break;
     case 2:
          Loop l;
          l = (Loop)connectedFrom;
          l.inside=null;
          break;
     case 3:
          Condition c1;
          c1 = (Condition)connectedFrom;
          c1.alt1=null;
          break;
     case 4:
          Condition c2;
          c2 = (Condition)connectedFrom;
          c2.alt2=null;
          break;
     }
     connectedFrom=null;
  }
}
 public void next(StrukElement next)
{
  // Wenn das nächste Element nicht null ist, dann erst mal seine Connection löschen !
  if (next!=null)
     next.delConnection();
  // Wenn das aktuelle nächste Element nicht null ist, auch die Connection löschen !
  if (this.next!=null)
     this.next.delConnection();
  // Jetzt den pot next zum aktuellen next machen
  this.next = next;
  // Schauen wir lieber nach ob zitkuläre Verknüpfungen vorliegen ...
  if (this.next!=null)
  {
      try {
          struktogramm.checkStruktogramm(next);
          }
      // Ohh, Meldung ausgeben ... und eigene next-Verknüpgung rückgängig machen
      catch(AllreadyPaintedException e){
             System.out.println("Something wrong here !");
             this.next=null;
      }
  }
  // und mich bei Ihm bekannt machen !
  if (this.next!=null)
  {
     this.next.connectedFrom=this;
     this.next.connectionType=1;
     if (this==struktogramm.last)
        struktogramm.last(this.next);
  }
  // Wenn ich das letzte Element bin, dann ist next nun das letzte Element !

}
public StrukElement next()
{
  return next;
}
public void checkStruktogramm()
throws AllreadyPaintedException
{
 System.out.println("Check "+this.Label+ "...");
 if (painted==true)
 {
    System.out.println("Something wrong here with "+this.Label);
    throw new AllreadyPaintedException();

 }
 painted=true;
 if (next!=null)
    next.checkStruktogramm();
}

public void dissolve()
{
   StrukElement e;
   if (next!=null)
   {
      e=next;
      next(null);
      e.dissolve();
   }
}
protected int calcHeight()
{
 if (next!=null)
    return (height()+next.calcHeight());
 else
    return (height());
 }



public void calcNext()
{
  if (next!=null)
  {
     next.x=nextx();
     next.y=nexty();
     next.setWidth(this.width());
  }
}

public void paint(Graphics g)
{
   // Das eigentliche Zeichnen wird den Unterklassen überlasssen
   if (struktogramm.strukPoint==false)
      this.paintDragPoint(g);
   else
      this.paintStrukPoint(g);
   if (struktogramm.paintNumber==true)
   {
      // Der Integer muß in einen String konvertiert werden !
      // g.drawString(String.toString(number),x,y);  geht leider nicht
   }
}

public void paintStrukPoint(Graphics g)
{
    // Target-Point
    g.setColor(new Color(0,0,255));
    if (struktogramm.first!=this)
    {
        if (connectedFrom==null)
            g.drawRect(x+getWidth()/2,y,10,10);
        else
            g.fillRect(x+getWidth()/2,y,10,10);
    }
    g.setColor(new Color(0,0,0));
    // Source-Point must added by substructure !
}

protected void paintDragPoint(Graphics g)
{
     g.fillRect(x,y,5,5);
}

abstract public void paintAll(Graphics g);
abstract public void rebuild(Vector v);

// Event-Handling



protected int mouseOnEdge(MouseEvent e)
{
   // Top Edge
   if( (e.getX()<x) && (e.getX()<x+getWidth()) && (Math.abs(e.getY()-y)<=sensible) )
     return 1;
   // Right Edge
   if( (e.getY()>y) && (e.getY()<y+getHeight()) && (Math.abs(e.getX()-x-getWidth())<=sensible))
     return 2;
   // Bottom Edge
   if( (e.getX()>x) && (e.getX()<x+getWidth()) && (Math.abs(e.getY()-y-getHeight())<=sensible))
     return 3;
   // Left Edge
   if( (e.getY()>y) && (e.getY()<y+getHeight()) && (Math.abs(e.getX()-x)<=sensible))
     return 4;
   return 0;
}
protected void targetPointPressed(MouseEvent e)
{
     if (struktogramm.strukPoint==false)
               struktor.addMouseMotionListener(this);
     System.out.println("Left Top Corner hit");
}

protected void nextPointPressed(MouseEvent e)
{
     if (struktogramm.strukPoint==true)
     {
        if (e.isShiftDown()==true)
        {
           next(null);
           System.out.println("Next Reset to null");
        }
        else
        {
            struktogramm.source(this);
            struktogramm.setConnectType(1);
            System.out.println("Source Point hit of "+this.Label);
        }
     }
}
protected void bottomEdgePressed(MouseEvent e)
{
     resizeHeight=true;
     struktor.addMouseMotionListener(this);
     System.out.println("Bottom Edge hit");
}

protected void rightEdgePressed(MouseEvent e)
{
     resizeWidth=true;
     struktor.addMouseMotionListener(this);
     System.out.println("Right Edge hit");
}

protected void targetPointReleased(MouseEvent e)
{
     struktor.removeMouseMotionListener(this);
     if (struktogramm.strukPoint==true)
     {
          System.out.println("ConnectType is "+struktogramm.getConnectType());
          try {
               switch(struktogramm.getConnectType())
               {
               case 1:
                  struktogramm.source().next(this);
                  System.out.println("Next set ! ConnectType was "+struktogramm.getConnectType());
                  break;
               case 2:
                  Loop l = new Loop();
                  l=(Loop)struktogramm.source();
                  l.inside(this);
                  System.out.println("Inside set ! ConnectType was "+struktogramm.getConnectType());
                  break;
               case 3:
                  Condition co1 = new Condition();
                  co1=(Condition)struktogramm.source();
                  co1.alt1(this);
                  System.out.println("Alt1 set ! ConnectType was "+struktogramm.getConnectType());
                  break;
                case 4:
                  Condition co2 = new Condition();
                  co2=(Condition)struktogramm.source();
                  co2.alt2(this);
                  System.out.println("Alt2 set ! ConnectType was "+struktogramm.getConnectType());
                  break;
                }
           } catch(Exception ex){System.out.println("NullPointer!");}
        }
}


public void mousePressed(MouseEvent e) {
        switch (mouseOnCorner(e))
        {
        case 1:
             targetPointPressed(e);
             break;
        case 3:
             nextPointPressed(e);
             break;
        }
        switch (mouseOnEdge(e))
        {
            case 2:
                 rightEdgePressed(e);
                 break;
            case 3:
                 bottomEdgePressed(e);
                 break;
        }
}

public void mouseDragged(MouseEvent e) {

            if (resizeWidth==true)
               setWidth(e.getX()-x);
            if (resizeHeight==true)
               setHeight(e.getY()-y);
            if ((resizeWidth==false) & (resizeHeight==false) & (struktogramm.strukPoint==false))
            {
               x=e.getX();
               y=e.getY();
            }
            if (x>660 && y > 350)
            {
               struktogramm.kill(this);
               removeMouseMotionListener(this);
            }
        /* System.out.println("dragging ..."); */
        struktor.repaint();
        e.consume();
    }



public void mouseReleased(MouseEvent e) {
        struktor.removeMouseMotionListener(this);
        boolean dragPoint=false;
        if (mouseOnCorner(e)==5)
        {
           targetPointReleased(e);
        }
        resizeWidth=false;
        resizeHeight=false;

        struktor.repaint();
        e.consume();
    }

public void mouseMoved(MouseEvent e) {
    }
public void mouseEntered(MouseEvent e) {
    }
public void mouseExited(MouseEvent e) {
    }
public void mouseClicked(MouseEvent e) {
    }
protected int mouseOnCorner(MouseEvent e)
{
   // Left Top Corner
   if((Math.abs(e.getX()-x-10) <= 10) && (Math.abs(e.getY()-y-10) <= 10))
     {System.out.println("Left Top Corner hit e:"+e.getX()+","+e.getY()+" objekt:"+x+","+y);
     return 1;}
   // Right Top Corner
   if( (Math.abs(e.getX()-x-getWidth()+10) <= 10) && (Math.abs(e.getY()-y-10) <= 10))
     {System.out.println("Right Top Corner hit e:"+e.getX()+","+e.getY()+" objekt:"+x+","+y);
     return 2;}
   // Left Bottom Corner
   if( (Math.abs(e.getX()-x-10) <=10 ) && (Math.abs(e.getY()-y-getWidth()+10) <= 10))
     return 4;
   // Special Point at half Top Edge
   if ( (Math.abs(e.getX()-x-getWidth()/2-10) < 10) && (Math.abs(e.getY()-y-10)<=10))
     return 5;
   return 0;
}
}