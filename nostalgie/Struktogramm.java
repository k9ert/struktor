import java.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Struktogramm extends Panel
{
Struktor struktor;
static int Anzahl=0;
int flag=0;
StrukElement first;
StrukElement last;
StrukElement pick;
Vector StrukList = new Vector();
boolean strukPoint;
boolean glue;
boolean paintNumber;
StrukElement source;
int connectType;
StrukElement target;

public Struktogramm(Struktor a)
{
 first=null;
 last=null;
 pick=null;
 Anzahl++;
 this.struktor=a;
}

protected void finalize()
{
  Anzahl--;
}

public void dissolve()
{
     first.dissolve();
}

public void checkStruktogramm(StrukElement s)
throws AllreadyPaintedException
{
     try {
         s.checkStruktogramm();
     }
     catch (AllreadyPaintedException e)
     {
       /*Object anchorpoint = getParent();
       while (!(anchorpoint instanceof Frame))
             anchorpoint = ((Component)anchorpoint).getParent();
             Dialog dl = new Dialog((Frame)anchorpoint, "zirkulaere Verknuefung verursacht ...",true);*/

       throw e;}
     finally {
         for (Enumeration el=StrukList.elements(); el.hasMoreElements(); )
         {
         StrukElement r=(StrukElement)el.nextElement();
         r.painted=false;
         System.out.println("set "+r.Label+"backtozero !");
         }
     }
}
public void rebuild()
{
     dissolve();
     first.rebuild(StrukList);
}

public void paint(Graphics g)
{

     if(flag==0)
         first.paintAll(g);
     for (Enumeration el=StrukList.elements(); el.hasMoreElements(); )
     {
         StrukElement r=(StrukElement)el.nextElement();
         if (r.hasSubConnection()==true)
            if (glue==true)
                r.paintAll(g);
            else
                r.paint(g);
         else
            r.paint(g);
     }
     flag=1;

}

protected void first(StrukElement e)
{
       first=e;
}

protected StrukElement first()
{
       return first;
}
protected void last(StrukElement e)
{
       last=e;
}
protected StrukElement last()
{
       return last;
}
protected void source(StrukElement e)
{
       source=e;
}
protected StrukElement source()
{
       return source;
}
public int getConnectType()
{
       return connectType;
}
public void setConnectType(int i)
{
       connectType=i;
}
protected void pick(StrukElement e)
{
       pick=e;
}

protected void flagzero()
{
       flag=0;
}

protected Command addCommand(int posx,int posy, int width, int height, String Label)
{
      Command C = new Command(posx,posy,width,height,Label);
      StrukList.addElement(C);
      Struktogramm me=this;
      C.setMyChiefs(struktor, me);
      struktor.addMouseListener(C);
      return C;
}

protected Loop addLoop(int posx,int posy, int width, int height, String Label)
{
      Loop L = new Loop(posx,posy,width,height,Label);
      Struktogramm me=this;
      L.setMyChiefs(struktor,me);
      StrukList.addElement(L);
      struktor.addMouseListener(L);
      return L;
}

protected Condition addCondition(int posx,int posy, int width, int height, String Label)
{
      Condition C = new Condition(posx,posy,width,height,Label);
      Struktogramm me=this;
      C.setMyChiefs(struktor,me);
      StrukList.addElement(C);
      struktor.addMouseListener(C);
      return C;
}


protected StrukElement findStrukElement(int j)
{
        for (Enumeration el=StrukList.elements(); el.hasMoreElements(); )
        {
             StrukElement r=(StrukElement)el.nextElement();
             if (r.getNumber()==j)
                return r;
        }
        return null;
}

protected void kill(StrukElement e)
{
       if (e==first)
          first=e.next();
       for (Enumeration el=StrukList.elements(); el.hasMoreElements(); )
       {
             StrukElement r=(StrukElement)el.nextElement();

             if (r.next()==e)
             {
                  r.next(e.next());
                  if (e==last)
                     last=r;
             }
             if (r.getClass().getName().equals("Loop"))
             {
                Loop l = (Loop)r;
                if (l.inside()==e)
                   l.inside(null);
             }
             if (r.getClass().getName().equals("Condition"))
             {
                Condition c = (Condition)r;
                if (c.alt1()==e)
                   c.alt1(null);
                if (c.alt2()==e)
                   c.alt2(null);
             }
       }
       StrukList.removeElement(e);
}

}