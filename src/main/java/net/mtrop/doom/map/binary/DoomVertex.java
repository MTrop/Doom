/*******************************************************************************
 * Copyright (c) 2015-2016 Matt Tropiano
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package net.mtrop.doom.map.binary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.blackrook.commons.Common;
import com.blackrook.io.SuperReader;
import com.blackrook.io.SuperWriter;

import net.mtrop.doom.BinaryObject;
import net.mtrop.doom.util.RangeUtils;

public class DoomVertex implements BinaryObject
{
	/** Byte length of this object. */
	public static final int LENGTH = 4;

	/** Vertex: X-coordinate. */
	private int x;
	/** Vertex: X-coordinate. */
	private int y;
	
	/**
	 * Creates a new vertex with default values set.
	 */
	public DoomVertex()
	{
	}

	/**
	 * Reads and creates a new DoomVertex from an array of bytes.
	 * This reads from the first 4 bytes of the array.
	 * @param bytes the byte array to read.
	 * @return a new DoomVertex with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static DoomVertex create(byte[] bytes) throws IOException
	{
		DoomVertex out = new DoomVertex();
		out.fromBytes(bytes);
		return out;
	}
	
	/**
	 * Reads and creates a new DoomVertex from an {@link InputStream} implementation.
	 * This reads from the stream until enough bytes for a {@link DoomVertex} are read.
	 * The stream is NOT closed at the end.
	 * @param in the open {@link InputStream} to read from.
	 * @return a new DoomVertex with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static DoomVertex read(InputStream in) throws IOException
	{
		DoomVertex out = new DoomVertex();
		out.readBytes(in);
		return out;
	}
	
	/**
	 * Reads and creates new DoomVertexes from an array of bytes.
	 * This reads from the first 4 * <code>count</code> bytes of the array.
	 * @param bytes the byte array to read.
	 * @param count the amount of objects to read.
	 * @return an array of DoomVertex objects with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static DoomVertex[] create(byte[] bytes, int count) throws IOException
	{
		return read(new ByteArrayInputStream(bytes), count);
	}
	
	/**
	 * Reads and creates new DoomVertexes from an {@link InputStream} implementation.
	 * This reads from the stream until enough bytes for <code>count</code> {@link DoomVertex}s are read.
	 * The stream is NOT closed at the end.
	 * @param in the open {@link InputStream} to read from.
	 * @param count the amount of objects to read.
	 * @return an array of DoomVertex objects with its fields set.
	 * @throws IOException if the stream cannot be read.
	 */
	public static DoomVertex[] read(InputStream in, int count) throws IOException
	{
		DoomVertex[] out = new DoomVertex[count];
		for (int i = 0; i < count; i++)
		{
			out[i] = new DoomVertex();
			out[i].readBytes(in);
		}
		return out;
	}
	
	/**
	 * Sets the coordinates of this vertex.
	 * @param x the new x-coordinate value.
	 * @param y the new y-coordinate value.
	 */
	public void set(int x, int y)
	{
		setX(x);
		setY(y);
	}
	
	/**
	 * @return the X-coordinate value of this vertex.
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * Sets the X-coordinate value of this vertex.
	 * @param x the new x-coordinate value.
	 * @throws IllegalArgumentException if x is outside of the range -32768 to 32767.
	 */
	public void setX(int x)
	{
		RangeUtils.checkShort("X-coordinate", x);
		this.x = x;
	}
	
	/**
	 * @return the Y-coordinate value of this vertex.
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * Sets the Y-coordinate value of this vertex.
	 * @param y the new y-coordinate value.
	 * @throws IllegalArgumentException if y is outside of the range -32768 to 32767.
	 */
	public void setY(int y)
	{
		RangeUtils.checkShort("Y-coordinate", y);
		this.y = y;
	}
	
	@Override
	public byte[] toBytes()
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream(LENGTH);
		try { writeBytes(bos); } catch (IOException e) { /* Shouldn't happen. */ }
		return bos.toByteArray();
	}

	@Override
	public void fromBytes(byte[] data) throws IOException
	{
		ByteArrayInputStream bin = new ByteArrayInputStream(data);
		readBytes(bin);
		Common.close(bin);
	}

	@Override
	public void readBytes(InputStream in) throws IOException
	{
		SuperReader sr = new SuperReader(in, SuperReader.LITTLE_ENDIAN);
		x = sr.readShort();
		y = sr.readShort();
	}

	@Override
	public void writeBytes(OutputStream out) throws IOException
	{
		RangeUtils.checkShort("X-coordinate", x);
		RangeUtils.checkShort("Y-coordinate", y);

		SuperWriter sw = new SuperWriter(out, SuperWriter.LITTLE_ENDIAN);
		sw.writeShort((short)x);
		sw.writeShort((short)y);
	}

	@Override
	public String toString()
	{
		return "Vertex (" + x + ", " + y + ")";
	}
	
}
