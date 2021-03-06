/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameBattler;
import javax.swing.JOptionPane;

/**
 * Part of a failed attempt to calculate an optimal metric for the AI to use.
 * @author Nadav
 */
public class CalcStrat {
    public static void main(String[] args)//need to avoid infinite loop of healing
    {
        FindStrat[] s = new FindStrat[12];
        
        for(int iterations = 1; iterations <6; iterations++)
        {
            for (int index = 0; index < 12; index += 2) {
                System.out.println("Begin game " + (index/2+1));
                Team t1 = new Team();
                Team t2 = new Team(t1);
                t1.setEnemyTeam(t2);
                int gameSize = 2;
                for (int i = 0; i < gameSize; i++) {
                    t1.add((((int) (Math.random() * 2) == 0) ? new Knight() : new Wizard()));
                    t2.add((((int) (Math.random() * 2) == 0) ? new Knight() : new Wizard()));
                }

                FindStrat s1 = s[index];
                FindStrat s2 = s[index + 1];

                int turn = 1;
                Fighter switchTo = null;
                Move m = null;
                while (true) {
                    //System.out.println("Begin turn " + turn);
                    //if(turn%1 == 0)
                        //displayTeams(t1);
                    if (turn == 1) {
                        if(iterations == 1){
                            double[] c = new double[20];
                            for (int i = 0; i < c.length; i++) {
                                c[i] = Math.random();
                            }
                            FindStrat.c = c;
                        }
                        else
                            FindStrat.c = s1.coefs;
                        s1 = new FindStrat(t1);
                    } else {
                        s1.updateTree(m, switchTo);
                    }
                    m = s1.move;
                    switchTo = s1.switched;
                    if (turn(t1, m, switchTo)) {
                        s[index] = s1;
                        s[index + 1] = s2;
                        break;
                    }
                    
                    if (turn == 1) {
                        if(iterations == 1){
                            double[] c = new double[20];
                            for (int i = 0; i < c.length; i++) {
                                c[i] = Math.random();
                            }
                            FindStrat.c = c;
                        }
                        else
                            FindStrat.c = s2.coefs;
                        s2 = new FindStrat(t2);
                    } else {
                        s2.updateTree(m, switchTo);
                    }
                    m = s2.move;
                    switchTo = s2.switched;
                    if (turn(t2, m, switchTo)) {
                        s[index] = s1;
                        s[index + 1] = s2;
                        break;
                    }
                    
                    turn++;
                    if(turn == 50)
                    {
                        t1 = new Team();
                        t2 = new Team(t1);
                        t1.setEnemyTeam(t2);
                        s[index] = s1;
                        s[index + 1] = s2;
                        break;
                    }
                }
            }
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 11 - i; j++) {
                    if (s[j].health() > s[j + 1].health()) {
                        FindStrat temp = s[j];
                        s[j] = s[j + 1];
                        s[j + 1] = temp;
                    }
                }
            }
            for (int i = 0; i < 6; i++) {
                if (s[i].health() == 0) {
                    double[] c = new double[20];
                    for (int k = 0; k < c.length; k++) {
                        c[k] = Math.random();
                    }
                    FindStrat.c = c;
                    s[i] = new FindStrat(new Team());
                }
            }
            for (int i = 6; i < 12; i++) {
                double[] c = new double[s[i - 6].coefs.length];
                System.arraycopy(s[i - 6].coefs, 0, c, 0, s[i - 6].coefs.length);
                for (int j = 0; j < c.length; j++) {
                    c[j] = c[j] + (Math.random() / 10 - .05);
                }
                FindStrat.c = c;
                s[i] = new FindStrat(null);
            }
            System.out.println("End of Iteration "+ iterations);
        }
        for(int i = 0; i<s.length; i++)
        {
            System.out.print("Strategy " + i + ": ");
            for(int j = 0; j<s[i].coefs.length; j++)
                System.out.print(s[i].coefs[j] + ((j==s[i].coefs.length - 1)?"\n":", "));
        }
    }
    
    /**
     * Asks the user for a Fighter type.
     * @param teamName The Team that will be asked.
     * @return A new Fighter of the type chosen.
     */
    public static Fighter getType(String teamName)
    {
        boolean flag = true;
        Fighter f = null;
        Object[] possibilities = {"Knight", "Wizard"};
        while(flag)
        {
            String str = (String) JOptionPane.showInputDialog(
                null,
                teamName + ", what type of Fighter do you want? Remember, order matters!",
                "Choose",
                JOptionPane.QUESTION_MESSAGE,
                null,
                possibilities,
                possibilities[0]);
            if(str == null)
                JOptionPane.showMessageDialog(null, "Please select a Fighter.");
            else if(str.equals("Knight"))
            {
                f = new Knight();
                flag = false;
            }
            else if(str.equals("Wizard"))
            {
                f = new Wizard();
                flag = false;
            }
            else
                JOptionPane.showMessageDialog(null, "Please select a Fighter.");
        }
        return f;
    }
    
    /**
     * Asks for the number of Fighters that will be on each Team for this game.
     * @return The number of Fighters to be used on each Team.
     */
    public static int getGameSize()
    {
        boolean flag = true;
        int n = 0;
        while(flag)
        {
            String s = JOptionPane.showInputDialog(null, "How many Fighters on each Team?", "Game Size", JOptionPane.QUESTION_MESSAGE);
            try{
                n = Integer.parseInt(s);
            }
            catch(Exception e){}
            if(n<=0)
                JOptionPane.showMessageDialog(null, "Please enter a number.");
            else
                flag = false;
        }
        return n;
    }
    
    /**
     * Displays the enemy's move in a Message JDialog.
     * @param m The Move done by the enemy.
     * @param s The Fighter switched to if m == Move.SWITCH, null otherwise.
     */
    public static void displayMove(Move m, Fighter s)
    {
        if(m != Move.SWITCH)
            JOptionPane.showMessageDialog(null, "The enemy has used " + m.getName());
        else
            JOptionPane.showMessageDialog(null, "Enemy has switched to\n" + s);
    }
    
    /**
     * Ask the user for their next Move.
     * @param t The user's team.
     * @return The Move they selected.
     */
    public static Move getMove(Team t)
    {
        String s = "";
        boolean flag = true;
        while(flag)
        {
            if(t.active() instanceof Knight)
                s = askKnight();
            else if(t.active() instanceof Wizard)
                s = askWizard();
            
            if(s != null || s.length() > 0)
                flag = false;
            else
                JOptionPane.showMessageDialog(null, "Please Select a Move.");
            if(s.equals("Please Show Teams Again"))
            {
                displayTeams(t);
                flag = true;
            }
        }
        return toMove(s);
    }
    
    /**
     * Simulates a turn for Team t.
     * @param t The Team whose turn is ending.
     * @param m The Move to be performed.
     * @param s The Fighter to be switched to if the move is Move.SWITCH.
     * @return True if the game has ended.
     */
    public static boolean turn(Team t, Move m, Fighter s)
    {
        if(m != Move.SWITCH)
            doMove(t,m);
        else
            t.switchActive(s);
        t.activateEffects();
        t.enemyTeam.activateEffects();
        boolean t1 = t.buryDead();
        boolean t2 = t.enemyTeam.buryDead();
        for(Fighter f:t.fighters)
        {
            if(f.hp >200)
                f.hp = 200;
        }
        for(Fighter f:t.enemyTeam.fighters)
        {
            if(f.hp >200)
                f.hp = 200;
        }
        if (t.isEmpty() || t.enemyTeam.isEmpty())
            return true;
        /*
        else
        {
            if(t1)
            {
                Fighter newActive = choose(t,0);
                t.switchActive(newActive);
            }
            if(t2)
            {
                Fighter newActive = choose(t.enemyTeam,0);
                t.enemyTeam.switchActive(newActive);
            }
        }
        */
        return false;
    }
    
    /**
     * Performs a Move.
     * @param t The Team performing a Move.
     * @param m The Move being performed.
     */
    public static void doMove(Team t, Move m)
    {
        Fighter p = t.active();
        Fighter opp = t.enemyTeam.active();
        switch(m)
        {
            case MELEE: 
                ((Knight)p).melee(opp);
                break;
            case SACRIFICE:
                ((Knight)p).sacrifice(opp);
                break;
            case HEAL: 
                ((Knight)p).heal();
                break;
            case FLAME: 
                ((Wizard)p).flame(opp);
                break;
            case FIREWORKS: 
                ((Wizard)p).fireworks(t.enemyTeam);
                break;
            case GROUPHEAL: 
                ((Wizard)p).groupHeal(t);
                break;
            case FUMES: 
                ((Wizard)p).fumes(t.enemyTeam);
                break;
            case FIRST:
                JOptionPane.showMessageDialog(null, "You forgot to initialize move...");
                break;
            default:
                JOptionPane.showMessageDialog(null, "You forgot a break...");
        }
    }
    
    /**
     *  End the game.
     * @param t The last Team to Move.
     */
    public static void endGame(Team t)
    {
        if (t.enemyTeam.isEmpty() && t.isEmpty())
            JOptionPane.showMessageDialog(null, "The battle ends in a Draw!");
        else if(t.isEmpty())
            JOptionPane.showMessageDialog(null, t.enemyTeam.name + " has Won!");
        else
            JOptionPane.showMessageDialog(null, t.name + " has Won!");
    }
    
    /**
     * Converts a String into a Move.
     * @param s The String to be converted.
     * @return The Move stored in the String.
     */
    public static Move toMove(String s)
    {
        Move move = Move.FIRST;
        for(Move m: Move.allMoves())
        {
            if(m.getName().equals(s))
                move = m;
        }
        return move;
    }
    
    /**
     * Displays a Team and its enemy.
     * @param t Team to be displayed with their enemyTeam.
     */
    public static void displayTeams(Team t)
    {
        String teams = t.name + ":\n" + t + "\n";
        teams += "\n" + t.enemyTeam.name + ":\n" + t.enemyTeam + "\n";
        teams += "\nSelect OK when you are ready to make a move.";
        JOptionPane.showMessageDialog(null, teams);
    }
    
    /**
     * Asks for user input for a Knight's move.
     * @return The move as a String.
     */
    public static String askKnight()
    {
        Object[] possibilities = {"Melee", "Sacrifice", "Heal", "Switch", "Please Show Teams Again"};
        return (String) JOptionPane.showInputDialog(
                null,
                "Knight, what is your move?",
                "Your Turn",
                JOptionPane.PLAIN_MESSAGE,
                null,
                possibilities,
                "Melee");
    }
    
    /**
     * Asks for user input for a Wizard's move.
     * @return The move as a String.
     */
    public static String askWizard()
    {
        Object[] possibilities = {"Flame", "Fireworks", "Group Heal", "Fumes", "Switch", "Please Show Teams Again"};
        return (String) JOptionPane.showInputDialog(
                null,
                "Wizard, what is your move?",
                "Your Turn",
                JOptionPane.PLAIN_MESSAGE,
                null,
                possibilities,
                "Flame");
    }
    
    //first = 0 if want to choose from all.
    //first = 1 if don't want active fighter on list.

    /**
     * Asks the user what Fighter will replace their active Fighter
     * @param t The Team being asked
     * @param first 1 if the user is switching Fighters, 0 if their active Fighter has just died
     * @return The Fighter chosen
     */
    public static Fighter choose(Team t, int first)
    {
        if (t.size() < 2) {
            return null;
        }
        Object[] possibilities = new Object[t.size() - first];
        for (int i = 0; i < possibilities.length; i++) {
            possibilities[i] = t.get(i + first).toString();
        }
        String str = (String) JOptionPane.showInputDialog(
                null,
                t.name + ", who will be your new active Fighter?",
                "Switch",
                JOptionPane.PLAIN_MESSAGE,
                null,
                possibilities,
                possibilities[0]);
        if (str == null)
            return null;
        for (int i = 0; i < t.size(); i++) {
            if (str.equals(t.get(i).toString())) {
                return t.get(i);
            }
        }
        return null;
    }
}
