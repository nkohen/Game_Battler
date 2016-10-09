/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameBattler;
import java.awt.Graphics;
import java.awt.Color;

/**
 * This Panel displays the Fighters in Interface's game.
 * @author Nadav
 */
public class GamePanel extends java.awt.Panel{
    private Team left = Interface.left;
    private Team right = Interface.right;
    private Integer r = Interface.r;
    
    @Override
    public void paint(Graphics g)
    {
        g.setColor(Color.yellow);
        g.fillRect(0, 0, getWidth() / 2, getHeight());
        g.setColor(Color.orange);
        g.fillRect(getWidth() / 2, 0, getWidth() / 2, getHeight());
        g.setColor(Color.BLACK);
        g.drawString("Your Team", getWidth() / 4, getHeight() / 4);

        Fighter f = left.active();
        if (f instanceof Knight) {
            ((Knight) f).drawKnight(g, true, getWidth() / 2 - (100 + (3 * r / 2)), getHeight() / 2 - 50, 3*r/2);
        } else if (f instanceof Wizard) {
            ((Wizard) f).drawWizard(g, getWidth() / 2 - (100 + (3 * r / 2)), getHeight() / 2 - 50, 3*r/2);
        }
        f = right.active();
        if (f instanceof Knight) {
            ((Knight) f).drawKnight(g, false, getWidth() / 2 + (100 - (3 * r / 2)), getHeight() / 2 - 50, 3*r/2);
        } else if (f instanceof Wizard) {
            ((Wizard) f).drawWizard(g, getWidth() / 2 + (100 - (3 * r / 2)), getHeight() / 2 - 50, 3*r/2);
        }

        for (int i = 1; i < left.size(); i++) {
            int y = (i - 1) * getHeight() / (left.size() - 1) + 50;
            f = left.get(i);
            if (f instanceof Knight) {
                ((Knight) f).drawKnight(g, true, 20, y, r);
            } else if (f instanceof Wizard) {
                ((Wizard) f).drawWizard(g, 20, y, r);
            }
        }
        for (int i = 1; i < right.size(); i++) {
            int y = (i - 1) * getHeight() / (right.size() - 1) + 50;
            f = right.get(i);
            if (f instanceof Knight) {
                ((Knight) f).drawKnight(g, false, getWidth() - (20 + 3 * r), y, r);
            } else if (f instanceof Wizard) {
                ((Wizard) f).drawWizard(g, getWidth() - (20 + 3 * r), y, r);
            }
        }
        
        if(left.isEmpty() || right.isEmpty())
        {
            g.setColor(Color.black);
            g.setFont(new java.awt.Font("Times New Roman",java.awt.Font.BOLD, 48));
            if(left.isEmpty() && right.isEmpty())
                g.drawString("It's a Draw", getWidth()/2-75, getHeight()-300);
            else if(left.isEmpty())
                g.drawString("You Lost", 100, getHeight()-300);
            else
                g.drawString("You Won!", getWidth()-250, getHeight()-300);
        }
    }
}
