/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameBattler;
import java.awt.Color;
import java.util.ArrayList;
import java.awt.Graphics;

/**
 * Fighter is the super class of all battlers in this game.
 * @author Nadav
 */
abstract public class Fighter {
    /**
     * How many health points this Fighter has.
     */
    protected int hp;
    /**
     * Represents the rate at which this fighter takes physical damage.
     */
    protected double prate;
    /**
     * Represents the rate at which this fighter takes magic damage.
     */
    protected double mrate;

    /**
     * All Effects this Fighter is affected by.
     */
    protected ArrayList<Effect> effects;
    
    /**
     * Constructs a Fighter with full hp (100) and no effects.
     * @param prate the rate at which this fighter takes physical damage.
     * @param mrate the rate at which this fighter takes magical damage.
     */
    public Fighter(double prate, double mrate)
    {
        this.prate = prate;
        this.mrate = mrate;
        hp = 100;
        effects = new ArrayList();
    }
    
    /**
     * Constructs a Fighter with the given hp and effects.
     * @param prate the rate at which this fighter takes physical damage.
     * @param mrate the rate at which this fighter takes magical damage.
     * @param hp the hp of this Fighter.
     * @param effects a list of effects on this Fighter.
     */
    public Fighter(double prate, double mrate, int hp, ArrayList<Effect> effects)
    {
        this.prate = prate;
        this.mrate = mrate;
        this.hp = hp;
        this.effects = effects;
    }
    
    /**
     * @return True if this Fighter is alive.
     */
    public boolean alive()
    {
        return hp != 0;
    }
    
    /**
     *Kills this Fighter by setting hp to zero.
     */
    public void kill()
    {
        hp = 0;
    }
    
    /**
     * Takes physical damage accounting for prate.
     * @param damage inflicted in a physical manner.
     */
    public void takePhysical(int damage)
    {
        hp -= (int)(prate*damage);
        if(hp<=0)
            kill();
    }
    
    /**
     * Takes magical damage accounting for mrate.
     * @param damage inflicted in a magical manner.
     */
    public void takeMagic(int damage)
    {
        hp -= (int)(mrate*damage);
        if(hp<=0)
            kill();
    }
    
    /**
     * Takes damage without accounting for mrate and prate.
     * @param damage inflicted.
     */
    public void takeDamage(int damage)
    {
        hp -= damage;
        if(hp<=0)
            kill();
    }
    
    /**
     * Heals this Fighter.
     * @param health to be added to hp.
     */
    public void heal(int health)
    {
        hp += health;
    }
    
    /**
     * Adds a given Effect to effects.
     * @param e the Effect to be added to this Fighter.
     */
    public void addEffect(Effect e)
    {
        effects.add(e);
    }
    
    /**
     * Activates all Effects in effects and inflicts pure damage, and removes Effects with duration 0.
     */
    public void activateEffects()
    {
        for(int i = 0; i<effects.size(); i++)
        {
            Effect e = effects.get(i);
            takeDamage(e.activate());
            if(!e.inEffect())
            {
                effects.remove(e);
                i--;
            }
        }
    }
    
    /**
     * @return A copy of this Fighter by value keeping only the information of their health and effects.
     */
    abstract public Fighter copy();
    
    /**
     * Draws a Fighter with a given Graphics object.
     * @param g Graphics object to draw with.
     * @param x x coordinate of top left corner of head.
     * @param y y coordinate of top left corner of head.
     * @param r radius of head, the rest stretches accordingly.
     * @param extra extra space needed above head for hp bar.
     */
    public void drawFighter(Graphics g, int x, int y, int r, int extra)
    {
        //the person will be this color
        g.setColor(Color.BLUE);
        //head
        g.fillOval(x, y, 2 * r, 2 * r);
        //neck and body
        g.drawLine(x + r, y + 2 * r, x + r, y + (6 * r));
        //right leg
        g.drawLine(x + r, y + (6 * r), x + (5 * r / 2), y + (int) (r * ((12 + 3 * Math.sqrt(3)) / 2)));
        //left leg
        g.drawLine(x + r, y + (6 * r), x - (r / 2), y + (int) (r * ((12 + 3 * Math.sqrt(3)) / 2)));
        //shoulders
        g.drawLine(x, y + (11 * r / 4), x + (2 * r), y + (11 * r / 4));
        //right arm
        g.drawLine(x + (2 * r), y + (11 * r / 4), x + (7 * r / 2), y + (17 * r / 4));
        //left arm
        g.drawLine(x, y + (11 * r / 4), x - (3 * r / 2), y + (17 * r / 4));
         //hp bar above head
        g.setColor(Color.RED);
        g.drawRect(x, y-r - extra, 2*r, r/2);
        g.fillRect(x, y-r - extra, (2*r*hp/100), r/2);
        //Effects
        for(int i = 0; i < effects.size(); i++)
        {
            g.drawString(effects.get(i).toString(), x-r/2, y+r*(9+i));
        }
    }
    
    /**
     * @param f The Fighter being compared to this Fighter.
     * @return True if f is a Fighter of the exact same state as this Fighter.
     */
    public boolean equals(Fighter f)
    {
        if(f==null)
            return false;
        return (hp == f.hp && mrate == f.mrate && prate == f.prate && sameEffects(effects, f.effects));
    }
    
    /**
     * Determines weather two lists of Effects are equivalent.
     * @param a an ArrayList of Effects.
     * @param b another ArrayList of Effects.
     * @return True if a and b are equal.
     */
    public static boolean sameEffects(ArrayList<Effect> a, ArrayList<Effect> b)
    {
        if(a.size() != b.size())
            return false;
        ArrayList<Effect> b2 = new ArrayList();
        for(Effect e:b)
        {
            b2.add(e.copy());
        }
        for(Effect e: a)
        {
            for(Effect e2:b2)
            {
                if(e.equals(e2))
                {
                    b2.remove(e2);
                    break;
                }
            }
        }
        if(b2.isEmpty())
            return true;
        else
            return false;
    }
    
    /**
     * @return A String listing this Fighter's hp and effects.
     */
    @Override
    public String toString()
    {
        String ans = "HP - " + hp + ", Effects - ";
        for(int i = 0;i<effects.size(); i++)
        {
            Effect e = effects.get(i);
            ans += e.toString();
            if(i+1 != effects.size())
                ans += ", ";
        }
        if(effects.isEmpty())
            ans += "None";
        return ans;
    }
}
