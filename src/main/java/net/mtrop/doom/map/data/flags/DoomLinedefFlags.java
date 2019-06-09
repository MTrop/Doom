package net.mtrop.doom.map.data.flags;

/**
 * Linedef flag constants for Doom/Heretic.
 * The constant value is how many places to bit shift 1 to equal the flag bit.  
 * @author Matthew Tropiano
 */
public interface DoomLinedefFlags extends LinedefFlags
{
	/** Linedef flag: Blocks players and monsters. */
	public static final int IMPASSABLE = 0;
	/** Linedef flag: Blocks monsters. */
	public static final int BLOCK_MONSTERS = 1;
	/** Linedef flag: Two-sided. */
	public static final int TWO_SIDED = 2;
	/** Linedef flag: Draw upper texture from top-down. */
	public static final int UNPEG_UPPER = 3;
	/** Linedef flag: Draw lower texture from bottom-up. */
	public static final int UNPEG_LOWER = 4;
	/** Linedef flag: Render as solid wall on automap. */
	public static final int SECRET = 5;
	/** Linedef flag: Blocks sound propagation (needs two). */
	public static final int BLOCK_SOUND = 6;
	/** Linedef flag: Never drawn on automap. */
	public static final int NOT_DRAWN = 7;
	/** Linedef flag: Immediately shown on automap. */
	public static final int MAPPED = 8;
	
}
