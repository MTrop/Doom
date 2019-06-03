/*******************************************************************************
 * Copyright (c) 2015-2016 Matt Tropiano
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package net.mtrop.doom;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Common elements of all objects that are loaded from binary data.
 * This provides a general interface for getting map object data.
 * @author Matthew Tropiano
 */
public interface BinaryObject
{
	/**
	 * Gets the byte representation of this object. 
	 * @return this object as a series of bytes.
	 */
	public byte[] toBytes();

	/**
	 * Reads in the byte representation of this object and sets its fields.
	 * @param data the byte array to read from. 
	 * @throws IOException if a read error occurs.
	 */
	public void fromBytes(byte[] data) throws IOException;
	
	/**
	 * Reads from an {@link InputStream} and sets this object's fields. 
	 * @param in the {@link InputStream} to read from. 
	 * @throws IOException if a read error occurs.
	 */
	public void readBytes(InputStream in) throws IOException;

	/**
	 * Writes this object to an {@link OutputStream}.
	 * @param out the {@link OutputStream} to write to.
	 * @throws IOException if a write error occurs.
	 */
	public void writeBytes(OutputStream out) throws IOException;
	
}
