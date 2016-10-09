/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameBattler;

/**
 * This class codes the strategy of the computer for this game.
 * @author Nadav
 */
public class AI extends Node{

    /**
     * The strategy this object will use.
     */
    public static Strategy strat;

    /**
     *  Constructs this Node and makes a decision for t.
     * @param t The computer's Team to make a move.
     */
    public AI(Team t)
    {
        super(t);
    }
    
    private AI(Team t, AI parent, Move m, boolean last, Fighter switched)
    {
        super(t,parent,m,last,switched);
    }
    
    @Override
    protected void addToFuture(Move m,Fighter s)
    {
        Team t = copyT();
        boolean last = Battle.turn(t,m,s);
        future.add(new AI(t.enemyTeam,this,m,last,s));
    }
    
    @Override
    protected void setEndMetric()
    {
        switch(strat)
        {
            case DELTAHP:
                if(where%2 == 1)
                    metric = t.totalHP() - t.enemyTeam.totalHP();
                else
                    metric = t.enemyTeam.totalHP() - t.totalHP();
                break;
            case HPRATIO:
                if(where%2 == 1)
                    metric = (double)t.totalHP()/(t.enemyTeam.totalHP()+1);
                else
                    metric = (double)t.enemyTeam.totalHP()/(t.totalHP()+1);
                break;
            case SURVIVAL:
                if(where%2 == 1)
                    metric = t.totalHP();
                else
                    metric = t.enemyTeam.totalHP();
                break;
            case KILLOPP:
                if(where%2 == 1)
                    metric = 1.0/(t.enemyTeam.totalHP()+1);
                else
                    metric = 1.0/(t.totalHP()+1);
                break;
            case HPWEIGHT:
                if(where%2 == 1)
                {
                    metric = 0;
                    for(Fighter f: t.fighters)
                    {
                        metric+= (f.hp>100)?100+(f.hp-100)/4:f.hp;
                        metric-= (f.hp<=10)?f.hp:0;
                    }
                    for(Fighter f:t.enemyTeam.fighters)
                    {
                        metric-= (f.hp>100)?100+(f.hp-100)/4:f.hp;
                        metric+= (f.hp<=10)?f.hp:0;
                    }
                }
                else
                {
                    metric = 0;
                    for(Fighter f: t.enemyTeam.fighters)
                    {
                        metric+= (f.hp>100)?100+(f.hp-100)/4:f.hp;
                        metric-= (f.hp<=10)?f.hp:0;
                    }
                    for(Fighter f:t.fighters)
                    {
                        metric-= (f.hp>100)?100+(f.hp-100)/4:f.hp;
                        metric+= (f.hp<=10)?f.hp:0;
                    }
                }
                break;
            case RANDOM:
                metric = Math.random();
                break;
        }
    }
}
