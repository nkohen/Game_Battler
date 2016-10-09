/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameBattler;

/**
 * Contains all moves available to any Fighter.
 * @author Nadav
 */
public enum Move {
    FIRST("First"), MELEE("Melee"), SACRIFICE("Sacrifice"), HEAL("Heal"), FLAME("Flame"), GROUPHEAL("Group Heal"), FIREWORKS("Fireworks"), FUMES("Fumes"), SWITCH("Switch");
    private final String name;
    private Move(String n){name = n;}
    @Override
    public String toString()
    {
        return "Move: " + name;
    }
    /**
     * @return An array containing all types of Moves in existence.
     */
    public static Move[] allMoves()
    {
        Move[] ans = {FIRST, MELEE, SACRIFICE, HEAL, FLAME, GROUPHEAL, FIREWORKS, FUMES, SWITCH};
        return ans;
    }
    /**
     * @return An array containing all Moves associated with the Knight class.
     */
    public static Move[] allKnightMoves()
    {
        Move[] ans = {MELEE, SACRIFICE, HEAL};
        return ans;
    }
    /**
     * @return An array containing all Moves associated with the Wizard class.
     */
    public static Move[] allWizardMoves()
    {
        Move[] ans = {FLAME, GROUPHEAL, FIREWORKS, FUMES};
        return ans;
    }

    /**
     * @return The name of this Move.
     */
    public String getName()
    {
        return name;
    }
}
