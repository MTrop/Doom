/*******************************************************************************
 * Copyright (c) 2015-2019 Matt Tropiano
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package net.mtrop.doom.map.data.flags;

/**
 * Thing flag constants for Doom/Heretic.
 * The constant value is how many places to bit shift 1 to equal the flag bit.  
 * @author Matthew Tropiano
 */
public interface DoomThingFlags extends ThingFlags
{
	/** Thing flag: Ambushes players. */
	public static final int AMBUSH = 3;
	/** Thing flag: Does not appear in single player. */
	public static final int NOT_SINGLEPLAYER = 4;
	
}
