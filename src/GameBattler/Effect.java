/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameBattler;

/**
 * An Effect is the result of an attack that does damage over time.
 * @author Nadav
 */
public class Effect{
    private int duration;
    private final int damage;
    String name;
    
    /**
     * Initializes an Effect object.
     * @param duration of this Effect.
     * @param damage done by this Effect every turn.
     * @param name of this Effect.
     */
    public Effect(int duration, int damage, String name)
    {
        this.duration = duration;
        this.damage = damage;
        this.name = name;
    }
    
    /**
     * Decrements duration.
     * @return Damage inflicted by this effect, this is to be used by the Fighter.
     */
    public int activate()
    {
        duration--;
        return damage;
    }
    
    /**
     * Determines weather this Effect is still in effect.
     * @return Whether or not duration is zero.
     */
    public boolean inEffect()
    {
        return duration != 0;
    }
    
    /**
     * @return A copy of this Effect by value.
     */
    public Effect copy()
    {
        return new Effect(duration, damage, name);
    }
    
    /**
     * Determines weather another Effect is logically equivalent to this one.
     * @param e The effect to be checked.
     * @return True if e is logically equivalent to this Effect.
     */
    public boolean equals(Effect e)
    {
        return (e.duration == duration && e.damage == damage && e.name.equals(name));
    }
    
    /**
     * @return The name of this Effect and its duration.
     */
    @Override
    public String toString()
    {
        return name + "(" + duration + ")";
    }
}
