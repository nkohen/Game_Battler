/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameBattler;

/**
 * Part of a failed attempt to calculate an optimal metric for the AI to use.
 * @author Nadav
 */
public class FindStrat extends Node{
    double[] coefs;
    public static double[] c;
    
    /**
     *  Constructs this Node and makes a decision for t.
     * @param t The computer's Team to make a move.
     */
    public FindStrat(Team t)
    {
        super(t);
        coefs = c;
    }
    
    private FindStrat(Team t,FindStrat parent, Move m, boolean last, Fighter switched)
    {
        super(t,parent,m,last,switched);
        coefs = parent.coefs;
    }
    
    @Override
    protected void addToFuture(Move m,Fighter s)
    {
        Team t = copyT();
        boolean last = CalcStrat.turn(t,m,s);
        future.add(new FindStrat(t.enemyTeam,this,m,last,s));
    }
    
    @Override
    protected void setEndMetric()
    {
        metric = 0;
        metric += c[0]*t.totalHP()*10;
        metric -= c[1]*t.enemyTeam.totalHP()*10;
        metric += c[2]*t.size();
        metric -= c[3]*t.enemyTeam.size();
        metric += c[4]*aveHP(t)*5;
        metric -= c[5]*aveHP(t.enemyTeam)*5;
        metric += c[6]*over100(t);
        metric -= c[7]*over100(t.enemyTeam);
        metric -= c[8]*under10(t)*3;
        metric += c[9]*under10(t.enemyTeam)*3;
        metric -= c[10]/(1+t.totalHP())*10;
        metric += c[11]/(1+t.enemyTeam.totalHP())*10;
        metric -= c[12]/(1+t.size());
        metric += c[13]/(1+t.enemyTeam.size());
        metric -= c[14]/(1+aveHP(t))*5;
        metric += c[15]/(1+aveHP(t.enemyTeam))*5;
        metric -= c[16]/(1+over100(t));
        metric += c[17]/(1+over100(t.enemyTeam));
        metric += c[18]/(1+under10(t))*3;
        metric -= c[19]/(1+under10(t.enemyTeam))*3;
        if(where%2 == 0)
            metric *= -1;
    }
    
    private static int under10(Team t)
    {
        int n = 0;
        for(Fighter f: t.fighters)
            n += (f.hp<10)?1:0;
        return n;
    }
    
    private static int over100(Team t)
    {
        int n = 0;
        for(Fighter f: t.fighters)
            n += (f.hp>100)?1:0;
        return n;
    }
    
    private static double aveHP(Team t)
    {
        int n = 0;
        for(Fighter f: t.fighters)
            n += f.hp;
        return (double)n/t.size();
    }
    
    public int health()
    {
        return t.totalHP();
    }
    
    @Override
    public void updateTree(Move m, Fighter s)
    {
        c = coefs;
        super.updateTree(m, s);
    }
}
