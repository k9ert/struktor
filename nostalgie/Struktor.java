import java.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.Image;
import java.awt.event.*;

public class Struktor extends Applet
implements ActionListener, ItemListener
{
Button repaint = new Button("repaint");
Button newCommand = new Button("newCommand");
Button newLoop = new Button("newLoop");
Button newCondition = new Button("newCondition");
Button rebuild = new Button("rebuild");
Button dissolve = new Button("dissolve");
Checkbox strukPoint = new Checkbox("StrukPoints",false);
Checkbox glue = new Checkbox("glue",true);
Struktogramm s = new Struktogramm(this);
Image dispose;
public void init()
{
    dispose = getImage(getDocumentBase(),"dispose.jpg");
    Struktogramm s = new Struktogramm(this);
    add(repaint);
    repaint.addActionListener(this);
    add(rebuild);
    rebuild.addActionListener(this);
    add(dissolve);
    dissolve.addActionListener(this);
    add(newCommand);
    newCommand.addActionListener(this);
    add(newLoop);
    newLoop.addActionListener(this);
    add(newCondition);
    newCondition.addActionListener(this);
    add(strukPoint);
    strukPoint.addItemListener(this);
    add(glue);
    glue.addItemListener(this);
}
public void start()
{
    Command C1,C2,C3;
    Loop L1;

    C1=s.addCommand(100,50,400,40,"C1");
    C2=s.addCommand(150,350,70,40,"C2");

    C3=s.addCommand(150,350,50,40,"C3");

    L1=s.addLoop(150,350,250,50,"L1");
    C1.next(L1);
    L1.inside(C2);
    L1.next(C3);
    s.first(C1);
    s.last(C3);
}
public void paint(Graphics g)
{
  s.paint(g);
  g.drawImage(dispose, 660, 350,this);
}

// Event Handling
public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == repaint) {
           s.flagzero();
           repaint();
        }
        if (src == rebuild)
           s.rebuild();
        if (src == dissolve){
           s.dissolve();
           repaint();
        }
        if (src == newCommand) {
           StrukElement C;
           C=s.addCommand(0,0,100,100,"new Command");
           s.last.next(C);
           s.last(C);
           s.repaint();
        }
        if (src == newLoop)
        {
           StrukElement L;
           L=s.addLoop(0,0,100,100,"new Loop");
           s.last.next(L);
           s.last(L);
           s.repaint();
        }
        if (src == newCondition)
        {
           StrukElement C;
           C=s.addCondition(0,0,100,100,"new Condition");
           s.last.next(C);
           s.last(C);
           s.repaint();
        }
       }
public void itemStateChanged(ItemEvent e)
{
       Object src = e.getSource();
       if (src == strukPoint)
       {
           s.strukPoint=strukPoint.getState();
           repaint();
       }
       if (src == glue)
       {
           s.glue=glue.getState();
           repaint();
       }
}

}