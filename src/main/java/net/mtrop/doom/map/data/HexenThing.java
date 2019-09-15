/*******************************************************************************
 * Copyright (c) 2015-2019 Matt Tropiano
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package net.mtrop.doom.map.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import net.mtrop.doom.object.BinaryObject;
import net.mtrop.doom.struct.io.SerialReader;
import net.mtrop.doom.struct.io.SerialWriter;
import net.mtrop.doom.util.RangeUtils;

/**
 * Hexen 20-byte format implementation of Thing.
 * @author Matthew Tropiano
 */
public class HexenThing extends CommonThing implements BinaryObject
{
	/** Byte length of this object. */
	public static final int LENGTH = 20;

	/** Thing ID. */
	protected int id;
	/** Thing Z position relative to sector plane. */
	protected int z;

	/** Flag: Thing is dormant. */
	protected boolean dormant;
	/** Flag: Thing appears for fighters. */
	protected boolean fighter;
	/** Flag: Thing appears for cleric. */
	protected boolean cleric;
	/** Flag: Thing appears for mage. */
	protected boolean mage;
	/** Flag: Thing appears for single player. */
	protected boolean singlePlayer;
	/** Flag: Thing appears for cooperative. */
	protected boolean cooperative;
	/** Flag: Thing appears for deathmatch. */
	protected boolean deathmatch;
	
	/** Thing action special. */
	protected int special;
	/** Thing action special arguments. */
	protected int[] arguments;
	
	/**
	 * Creates a new thing.
	 */
	public HexenThing()
	{
		super();
		this.id = 0;
		this.z = 0;

		this.dormant = false;
		this.fighter = false;
		this.cleric = false;
		this.mage = false;
		this.singlePlayer = false;
		this.cooperative = false;
		this.deathmatch = false;
		
		this.special = 0;
		this.arguments = new int[5];
	}

	/**
	 * @return the Z position relative to sector plane.
	 */
	public int getZ()
	{
		return z;
	}

	/**
	 * Sets the Z position relative to sector plane.
	 * @param z the new Z position.
	 * @throws IllegalArgumentException if <code>z</code> is not between -32768 and 32767.
	 */
	public void setZ(int z)
	{
		RangeUtils.checkShort("Position Z", z);
		this.z = z;
	}

	/**
	 * @return the thing's id (for tagged specials).
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * Sets the thing's id. 
	 * @param id the new id.
	 * @throws IllegalArgumentException if <code>id</code> is not between 0 and 65535.
	 */
	public void setId(int id)
	{
		RangeUtils.checkShortUnsigned("Thing ID", id);
		this.id = id;
	}

	/**
	 * @return true if this is dormant, false if not.
	 */
	public boolean isDormant()
	{
		return dormant;
	}

	/**
	 * Sets if this is dormant.
	 * @param dormant true to set, false to clear.
	 */
	public void setDormant(boolean dormant)
	{
		this.dormant = dormant;
	}

	/**
	 * @return true if this appears on single player, false if not.
	 */
	public boolean isSinglePlayer()
	{
		return singlePlayer;
	}

	/**
	 * Sets if this appears on single player.
	 * @param singlePlayer true to set, false to clear.
	 */
	public void setSinglePlayer(boolean singlePlayer)
	{
		this.singlePlayer = singlePlayer;
	}

	/**
	 * @return true if this appears on cooperative, false if not.
	 */
	public boolean isCooperative()
	{
		return cooperative;
	}

	/**
	 * Sets if this appears on cooperative.
	 * @param cooperative true to set, false to clear.
	 */
	public void setCooperative(boolean cooperative)
	{
		this.cooperative = cooperative;
	}

	/**
	 * @return true if this appears on deathmatch, false if not.
	 */
	public boolean isDeathmatch()
	{
		return deathmatch;
	}

	/**
	 * Sets if this appears on deathmatch.
	 * @param deathmatch true to set, false to clear.
	 */
	public void setDeathmatch(boolean deathmatch)
	{
		this.deathmatch = deathmatch;
	}

	/**
	 * @return true if this appears for Fighters, false if not.
	 */
	public boolean isFighter()
	{
		return fighter;
	}
	
	/**
	 * Sets if this appears for Fighters.
	 * @param fighter true to set, false to clear.
	 */
	public void setFighter(boolean fighter)
	{
		this.fighter = fighter;
	}
	
	/**
	 * @return true if this appears for Clerics, false if not.
	 */
	public boolean isCleric()
	{
		return cleric;
	}
	
	/**
	 * Sets if this appears for Cleric.
	 * @param cleric true to set, false to clear.
	 */
	public void setCleric(boolean cleric)
	{
		this.cleric = cleric;
	}
	
	/**
	 * @return true if this appears for Mages, false if not.
	 */
	public boolean isMage()
	{
		return mage;
	}
	
	/**
	 * Sets if this appears for Mages.
	 * @param mage true to set, false to clear.
	 */
	public void setMage(boolean mage)
	{
		this.mage = mage;
	}
	
	/**
	 * @return the special action for this thing.
	 */
	public int getSpecial()
	{
		return special;
	}

	/**
	 * Sets the special action for this thing.
	 * @param special the thing special to call on activation.
	 * @throws IllegalArgumentException if special is outside the range 0 to 255.
	 */
	public void setSpecial(int special)
	{
		RangeUtils.checkByteUnsigned("Special", special);
		this.special = special;
	}
	
	/**
	 * Gets the special arguments copied into a new array. 
	 * @return gets the array of special arguments.
	 */
	public int[] getArguments()
	{
		int[] out = new int[5];
		System.arraycopy(arguments, 0, out, 0, 5);
		return out;
	}

	/**
	 * Gets a special argument.
	 * @param n the argument index (up to 4) 
	 * @return the argument value.
	 * @throws ArrayIndexOutOfBoundsException if <code>n</code> is less than 0 or greater than 4. 
	 */
	public int getArgument(int n)
	{
		return arguments[n];
	}

	/**
	 * Sets the special arguments.
	 * @param arguments the argument values to set.
	 * @throws IllegalArgumentException if length of arguments is greater than 5, or any argument is less than 0 or greater than 255. 
	 */
	public void setArguments(int ... arguments)
	{
		if (arguments.length > 5)
			 throw new IllegalArgumentException("Length of arguments is greater than 5.");

		int i;
		for (i = 0; i < arguments.length; i++)
		{
			RangeUtils.checkByteUnsigned("Argument " + i, arguments[i]);
			this.arguments[i] = arguments[i];
		}
		for (; i < 5; i++)
		{
			this.arguments[i] = 0;
		}
		
	}
	
	@Override
	public void readBytes(InputStream in) throws IOException
	{
		SerialReader sr = new SerialReader(SerialReader.LITTLE_ENDIAN);
		id = sr.readUnsignedShort(in);
		x = sr.readShort(in);
		y = sr.readShort(in);
		z = sr.readShort(in);
		angle = sr.readShort(in);
		type = sr.readShort(in);
		flags = sr.readUnsignedShort(in);
		special = sr.readUnsignedByte(in);
		arguments[0] = sr.readUnsignedByte(in);
		arguments[1] = sr.readUnsignedByte(in);
		arguments[2] = sr.readUnsignedByte(in);
		arguments[3] = sr.readUnsignedByte(in);
		arguments[4] = sr.readUnsignedByte(in);
		
	}

	@Override
	public void writeBytes(OutputStream out) throws IOException
	{
		SerialWriter sw = new SerialWriter(SerialWriter.LITTLE_ENDIAN);
		sw.writeUnsignedShort(out, id);
		sw.writeShort(out, (short)x);
		sw.writeShort(out, (short)y);
		sw.writeShort(out, (short)z);
		sw.writeShort(out, (short)angle);
		sw.writeShort(out, (short)type);
		sw.writeUnsignedShort(out, flags & 0x0FFFF);
		sw.writeByte(out, (byte)special);
		sw.writeByte(out, (byte)arguments[0]);
		sw.writeByte(out, (byte)arguments[1]);
		sw.writeByte(out, (byte)arguments[2]);
		sw.writeByte(out, (byte)arguments[3]);
		sw.writeByte(out, (byte)arguments[4]);
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Thing");
		sb.append(" (").append(x).append(", ").append(y).append(")");
		sb.append(" Z:").append(z);
		sb.append(" Type:").append(type);
		sb.append(" Angle:").append(angle);
		sb.append(" ID:").append(id);
		sb.append(' ').append("Flags 0x").append(String.format("%016x", flags & 0x0FFFF));
		sb.append(" Special ").append(special);
		sb.append(" Args ").append(Arrays.toString(arguments));
		return sb.toString();
	}

}
