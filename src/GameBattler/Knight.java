/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameBattler;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * A Knight is a physical Fighter.
 * @author Nadav
 */
public class Knight extends Fighter {

    /**
     * Constructs a Knight with full hp (100) and no effects.
     */
    public Knight()
    {
        super(.5, 1);
    }
    
    /**
     * Constructs a Knight with the given hp and effects.
     * @param hp the hp of this Knight.
     * @param effects a list of effects on this Knight.
     */
    public Knight(int hp, ArrayList<Effect> effects)
    {
        super(.5,1,hp,effects);
    }
    
    /**
     * The Knight performs a melee attack on their enemy.
     * @param enemy victim of this attack.
     */
    public void melee(Fighter enemy)
    {
        enemy.takePhysical(40);
    }
    
    /**
     * The Knight performs a sacrifice attack.
     * @param enemy victim of this attack.
     */
    public void sacrifice(Fighter enemy)
    {
        kill();
        enemy.kill();
    }
    
    /**
     * The Knight heals his/her self.
     */
    public void heal()
    {
        super.heal(30);
    }
    
    /**
     * @return A copy of this Knight by value keeping only the information of their hp and effects.
     */
    @Override
    public Knight copy()
    {
        ArrayList<Effect> e = new ArrayList();
        for(Effect x:effects)
            e.add(x.copy());
        return new Knight(hp,e);
    }
    
    /**
     * Draws a Knight with a given Graphics object.
     * @param g the Graphics object to draw this Knight.
     * @param right True if this Knight's sword is in his right hand.
     * @param x the x coordinate of the top left corner of this Knight's head.
     * @param y the y coordinate of the top left corner of this Knight's head.
     * @param r the radius of this Knight's head, this value also stretches the rest of this Knight accordingly.
     */
    public void drawKnight(Graphics g, boolean right, int x, int y, int r)
    {
        drawFighter(g, x, y, r,0);
        g.setColor(Color.RED);
        if(right)
        {
            //right sword
            g.drawLine(x + (3 * r), y + (19 * r / 4), x + (15 * r / 2), y + (r / 4));
            //right sword's gaurd
            g.drawLine(x + (7 * r / 2), y + (int) (r * (17 - 2 * Math.sqrt(2)) / 4), x + (int) (r * (7 + Math.sqrt(2)) / 2), y + (17 * r / 4));
        }
        else
        {
            //left sword
            g.drawLine(x - r, y + (19 * r / 4), x - (11 * r / 2), y + (r / 4));
            //left sword's gaurd
            g.drawLine(x - (3 * r / 2), y + (int) (r * (17 - 2 * Math.sqrt(2)) / 4), x - (int) (r * (3 + Math.sqrt(2)) / 2), y + (17 * r / 4));
        }
    }
    
    @Override
    public String toString()
    {
        return "Knight: " + super.toString();
    }
}
