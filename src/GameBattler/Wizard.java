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
 * A Wizard is a magical Fighter.
 * @author Nadav
 */
public class Wizard extends Fighter {

    /**
     * Constructs a Wizard with full hp (100) and no effects.
     */
    public Wizard()
    {
        super(1, .5);
    }
    
    /**
     * Constructs a Wizard with the given hp and effects.
     * @param hp the hp of this Knight.
     * @param effects a list of effects on this Knight.
     */
    public Wizard(int hp, ArrayList<Effect> effects)
    {
        super(1, .5, hp, effects);
    }
    
    /**
     * The Wizard performs a flame attack on their enemy.
     * @param enemy victim of this attack.
     */
    public void flame(Fighter enemy)
    {
        enemy.takeMagic(40);
    }
    
    /**
     * The Wizard performs a flame attack on their enemy's team.
     * @param enemyTeam victims of this attack.
     */
    public void fireworks(Team enemyTeam)
    {
        for(Fighter f: enemyTeam.fighters)
            f.takeMagic(20);
    }
    
    /**
     * The Wizard heals their team.
     * @param friends Fighters to be healed.
     */
    public void groupHeal(Team friends)
    {
        for(Fighter f: friends.fighters)
            f.heal(15);
    }
    
    /**
     * The Wizard performs a fumes attack on their enemy's team.
     * @param enemyTeam victims of this attack.
     */
    public void fumes(Team enemyTeam)
    {
        for(Fighter f: enemyTeam.fighters)
            f.addEffect(new Effect(4,5,"Fumes"));
    }
    
    /**
     * @return A copy of this Wizard by value keeping only the information of their health and effects.
     */
    @Override
    public Wizard copy()
    {
        ArrayList<Effect> e = new ArrayList();
        for(Effect x:effects)
            e.add(x.copy());
        return new Wizard(hp,e);
    }
    
    /**
     * Draws a Wizard with a given Graphics object.
     * @param g the Graphics object to draw this Wizard.
     * @param x the x coordinate of the top left corner of this Wizard's head.
     * @param y the y coordinate of the top left corner of this Wizard's head.
     * @param r the radius of this Wizard's head, this value also stretches the rest of this Wizard accordingly.
     */
    public void drawWizard(Graphics g, int x, int y, int r)
    {
        drawFighter(g, x, y, r,2*r);
        //the person's hat will be this color
        g.setColor(Color.GREEN);
        //hat
        int[] xpoints = {x, x + 2 * r, x + r};
        int[] ypoints = {y, y, y - 2 * r};
        g.fillPolygon(xpoints, ypoints, 3);

        //the person's star will be this color
        g.setColor(Color.RED);
        //star thing on hat
        xpoints = new int[10];
        ypoints = new int[10];
        for (int i = 0; i < 5; i++) {
            xpoints[2 * i] = x + r + (int) (r * (Math.cos((5 - 4 * i) * Math.PI / 10)) / 2);
            ypoints[2 * i] = y - r - (int) (r * (Math.sin((5 - 4 * i) * Math.PI / 10)) / 2) + r / 4;
        }
        for (int i = 0; i < 5; i++) {
            xpoints[2 * i + 1] = x + r + (int) (r * (Math.cos((5 - 4 * (2 * i + 1)) * Math.PI / 10)) / 4);
            ypoints[2 * i + 1] = y - r - (int) (r * (Math.sin((5 - 4 * (2 * i + 1)) * Math.PI / 10)) / 4) + r / 4;
        }
        g.fillPolygon(xpoints, ypoints, 10);
    }
    
    @Override
    public String toString()
    {
        return "Wizard: " + super.toString();
    }
}
