/*******************************************************************************
 * Copyright (c) 2015-2016 Matt Tropiano
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package net.mtrop.doom.map.binary;

import java.io.IOException;
import java.io.InputStream;

import net.mtrop.doom.WadFile;
import net.mtrop.doom.LoggingFactory;
import net.mtrop.doom.LoggingFactory.Logger;
import net.mtrop.doom.util.Utils;

public class DoomThingTest
{
	public static void main(String[] args) throws IOException
	{
		Logger logger = LoggingFactory.createConsoleLoggerFor(DoomThingTest.class);
		
		WadFile wad = new WadFile(args[0]);
		InputStream in = wad.getInputStream("THINGS");

		int i = 0;
		byte[] b = new byte[10];
		while (in.read(b) > 0)
			logger.info((i++) + " " + DoomThing.create(b));
		
		Utils.close(in);
		Utils.close(wad);
	}
}
