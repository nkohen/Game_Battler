/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameBattler;
import java.util.ArrayList;

/**
 * A collection of Fighters, one of them being the active Fighter.
 * @author Nadav
 */
public class Team {
    /**
     * Contains all of the Fighters on this Team.
     * The first Fighter in this ArrayList is the active Fighter.
     */
    public ArrayList<Fighter> fighters;

    /**
     * A reference to the enemy Team.
     */
    public Team enemyTeam;

    /**
     * The name of this Team to be displayed.
     */
    public String name;
    
    /**
     * How many Teams are in existence, this is used for a default name
     */
    public static int howMany = 0;
    
    /**
     * Constructs an empty Team.
     */
    public Team()
    {
        howMany++;
        name = "Team " + howMany;
        fighters = new ArrayList();
        enemyTeam = null;
    }
    
    /**
     * Constructs an empty Team with an enemy Team.
     * @param enemyTeam The enemy Team.
     */
    public Team(Team enemyTeam)
    {
        howMany++;
        name = "Team " + howMany;
        fighters = new ArrayList();
        this.enemyTeam = enemyTeam;
    }
    
    /**
     * Constructs a Team with no enemy.
     * @param fighters The Fighters on this Team.
     */
    public Team(ArrayList<Fighter> fighters)
    {
        howMany++;
        name = "Team " + howMany;
        this.fighters = fighters;
        enemyTeam = null;
    }
    
    /**
     * Constructs a Team with an enemy Team.
     * @param fighters The Fighters on this Team.
     * @param enemyTeam The enemy Team.
     */
    public Team(ArrayList<Fighter> fighters, Team enemyTeam)
    {
        howMany++;
        name = "Team " + howMany;
        this.fighters = fighters;
        this.enemyTeam = enemyTeam;
    }
    
    /**
     * Sets howMany.
     * @param x The new value of howMany.
     */
    public static void setHowMany(int x)
    {
        howMany = x;
    }
    
    /**
     * @return The active Fighter on this Team.
     */
    public Fighter active()
    {
        if(fighters.isEmpty())
            return null;
        return fighters.get(0);
    }
    
    /**
     * Sets a new active Fighter to replace the current active Fighter.
     * @param newFighter The new active Fighter.
     */
    public void switchActive(Fighter newFighter)
    {
        boolean flag = true;
        int i = 0;
        for(Fighter f: fighters)
        {
            if(f.equals(newFighter))
            {
                i = fighters.indexOf(f);
                flag = false;
                break;
            }
        }
        if(flag)
            return;
        Fighter active = fighters.get(0);
        fighters.set(i, active);
        fighters.set(0, newFighter);
    }
    
    /**
     * Sets a new active Fighter to replace the current active Fighter.
     * @param i The index of the new active Fighter.
     */
    public void switchActive(int i)
    {
        Fighter active = fighters.get(0);
        Fighter newFighter = fighters.get(i);
        fighters.set(i, active);
        fighters.set(0, newFighter);
    }
    
    /**
     * Sets enemyTeam.
     * @param enemyTeam The enemy Team.
     */
    public void setEnemyTeam(Team enemyTeam)
    {
        this.enemyTeam = enemyTeam;
    }
    
    /**
     * Adds a Fighter to this Team.
     * @param f The fighter to be added.
     */
    public void add(Fighter f)
    {
        fighters.add(f);
    }
    
    /**
     * @return A String containing all of the Fighters on this team.
     */
    @Override
    public String toString()
    {
        String team = "";
        for(Fighter f : fighters)
            team += f + "\n";
        return team;
    }
    
    /**
     * @return Size of this Team.
     */
    public int size()
    {
        return fighters.size();
    }
    
    /**
     * @return True if this Team is empty.
     */
    public boolean isEmpty()
    {
        return fighters.isEmpty();
    }
    
    /**
     * Gets a Fighter from this Team.
     * @param i The index of a Fighter on this Team.
     * @return The Fighter at index i.
     */
    public Fighter get(int i)
    {
        return fighters.get(i);
    }
    
    /**
     * @return A copy of this Team by value without an enemy Team.
     */
    public Team copy()
    {
        ArrayList<Fighter> t = new ArrayList();
        for(Fighter f: fighters)
            t.add(f.copy());
        return new Team(t);
    }
    
    /**
     * Activates the Effects of all Fighters on this Team.
     */
    public void activateEffects()
    {
        for(Fighter f: fighters)
            f.activateEffects();
    }
    
    /**
     * Removes dead Fighters from the Team.
     * @return True if the active Fighter has died.
     */
    public boolean buryDead()
    {
        boolean activeDead = false;
        Fighter a = active();
        for(int i = 0; i<fighters.size(); i++)
        {
            Fighter f = fighters.get(i);
            if(!f.alive())
            {
                if(f == a)
                    activeDead = true;
                fighters.remove(f);
                i--;
            }
        }
        return activeDead;
    }
    
    /**
     * Calculates the sum of all HP on this Team.
     * @return The total HP of this Team.
     */
    public int totalHP()
    {
        int x = 0;
        for(Fighter f: fighters)
            x += f.hp;
        return x;
    }
    
    /**
     * Sets the name of this Team to be displayed.
     * @param s The name to be given to this Team.
     */
    public void setName(String s)
    {
        name = s;
    }
}
