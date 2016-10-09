/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameBattler;
import java.util.ArrayList;

/**
 * This class creates a tree of height at most 8 that simulates all possible futures and then attempts to reach the optimal one.
 * @author Nadav
 */
abstract public class Node {

    /**
     * The active Team (whose turn it is).
     * Points to all fighters and determines this position in the game, should be a copy by value.
     */
    protected Team t;

    /**
     * Stores children Nodes.
     */
    protected ArrayList<Node> future;
    
    /**
     * A reference to this Node's parent.
     */
    private Node parent;

    /**
     * The depth in which this Node resides within the tree.
     */
    protected int where;
    
    /**
     * True if this Node is an end position of the game i.e. a Team is empty.
     */
    private boolean end;

    /**
     * The Move this AI has decided to make.
     * If this Node is not the root, then this is the Move that led to this Node's creation.
     */
    public Move move;

    /**
     * The Fighter this AI has switched to, if move != Move.SWITCH this should be null.
     * If this Node is not the root, then this is the Fighter switched to leading to this Node's creation.
     */
    public Fighter switched;

    /**
     * How good this position is to the computer as perceived by a Strategy.
     */
    protected double metric;
      
    /**
     * Constructs a tree rooted at this Node and makes a decision for t.
     * @param t The Team this computer is controlling.
     */
    protected Node(Team t)
    {
        this(copyInitial(t),null,Move.FIRST,false,null);
    }
    
    /**
     * Copies t by value with its enemyTeam also by value.
     * @param t The Team to be copied.
     * @return A copy of t by value.
     */
    private static Team copyInitial(Team t)
    {
        if(t == null)
            return null;
        Team tCopy = t.copy();
        Team oppCopy = t.enemyTeam.copy();
        tCopy.setEnemyTeam(oppCopy);
        oppCopy.setEnemyTeam(tCopy);
        return tCopy;
    }
    
    /**
     * Creates a Node within the tree which then makes its own children.
     * @param t The Team whose turn it is.
     * @param parent A reference to the Node that created this one.
     * @param move The Move that led to the creation of this Node.
     * @param last True if this Node is an end position of the game.
     * @param switched The Fighter switched to if move == Move.SWITCH, null otherwise.
     */
    protected Node(Team t, Node parent, Move move, boolean last, Fighter switched)
    {
        this.t = t;
        this.move = move;
        this.switched = switched;
        future = new ArrayList();
        where = (parent != null)?parent.where+1:1;
        end = last;
        if(t == null)
            return;
        if(!end && where != 8)
        {
            setFuture();
            setMetric();
        }
        else
        {
            setEndMetric();
        }
    }
    
    /**
     * Calls on addToFuture for all possible Moves that could be made by t.
     */
    private void setFuture()
    {
        //Create SWITCH children
        for(int i = 1; i<t.size();i++)
        {
            addToFuture(Move.SWITCH, t.get(i));
        }
        //Create Knight Move children
        if(t.active() instanceof Knight)
        {
            for(Move m: Move.allKnightMoves())
            {
                addToFuture(m,null);
            }
        }
        //Create Wizard Move children
        if(t.active() instanceof Wizard)
        {
            for(Move m: Move.allWizardMoves())
            {
                addToFuture(m,null);
            }
        }
    }
    
    /**
     * This method should initializes metric for the leaves of this tree.
     * This ultimately codes the strategy being used by the AI.
     * Keep in mind when deciding on a formula for metric, this is looking ahead 8 turns, so there is no option of programming on-the-spot decisions e.g. trying to switch out a Knight when he is about to die.
     */
    abstract protected void setEndMetric();
    
    /**
     * Initializes metric for all non-leaf Nodes and sets move and switched for the root.
     */
    private void setMetric()
    {
        //If this is the root, choose the move that will go to the best Node in future
        if(where == 1)
        {
            Node maxNode = future.get(0);
            for(Node n: future)
            {
                if(n.metric>maxNode.metric)
                    maxNode = n;
            }
            move = maxNode.move;
            switched = maxNode.switched;
        }
        //If this is the same Team as the root, set this metric to be that of the best future Node as this would be the decision
        else if(where%2 == 1)
        {
            Node maxNode = future.get(0);
            for(Node n: future)
            {
                if(n.metric>maxNode.metric)
                    maxNode = n;
            }
            metric = maxNode.metric;
        }
        //If this is not the same Team as the root, set this metric to be that of the worst future Node as this would be the decision by assumption
        else
        {
            Node minNode = future.get(0);
            for(Node n: future)
            {
                if(n.metric<minNode.metric)
                    minNode = n;
            }
            metric = minNode.metric;
        }
    }
    
    /**
     * This method must copyT and then simulate m happening and then add a new Node of the subtype's type to future.
     * @param m The Move simulated.
     * @param s The Fighter switched to if m == Move.SWITCH, null otherwise.
     */
    abstract protected void addToFuture(Move m,Fighter s);
    
    /**
     * Copies t by value with a copy of opp as an enemyTeam also by value.
     * @return A copy of t by value.
     */
    protected Team copyT()
    {
        Team tCopy = t.copy();
        Team oppCopy = t.enemyTeam.copy();
        tCopy.setEnemyTeam(oppCopy);
        oppCopy.setEnemyTeam(tCopy);
        return tCopy;
    }
    
    /**
     * Updates this Node to a position in the game after move has been done and then m by the enemy.
     * @param m Move done by the enemy player.
     * @param s Fighter switched to if m == Move.SWITCH, null otherwise.
     */
    public void updateTree(Move m, Fighter s)
    {
        int i = -1;
        for(Node n:future)
        {
            if((n.move == Move.SWITCH && n.switched.equals(switched)) || n.move==this.move)
                i = future.indexOf(n);
        }
        //next is the Node of the position after the computer has moved assuming it has taken Node's advice
        Node next = future.get(i);
        i=-1;
        for(Node n: next.future)
        {
            if((n.move == Move.SWITCH && n.switched.equals(s)) || n.move==m)
                i = next.future.indexOf(n);
        }
        Node nowGame = next.future.get(i);
        nowGame.parent = null;
        nowGame.update2Root();
        nowGame.extend();
        setThisTo(nowGame);
    }
    
    /**
     * Makes this Node into n.
     * @param n the Node this should be set to.
     */
    private void setThisTo(Node n)
    {
        end = n.end;
        future = n.future;
        for(Node child:future)
            child.parent = this;
        metric = n.metric;
        move = n.move;
        parent = n.parent;
        switched = n.switched;
        t = n.t;
        where = n.where;
    }
    
    /**
     * Updates where of the new tree after updateTree has been called by subtracting 2.
     */
    private void update2Root()
    {
        if(where != 8 && !end)
        {
            for(Node n:future)
                n.update2Root();
        }
        where-=2;
    }
    
    /**
     * If this tree is of height 6, this method extends this tree to be of height 8 and recalculates metric accordingly.
     */
    private void extend()
    {
        if(where == 6 && !end)
        {
            setFuture();
            setMetric();
        }
        else if (!end)
        {
            for(Node n:future)
                n.extend();
            setMetric();
        }
    }
}
