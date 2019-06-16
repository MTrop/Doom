/*******************************************************************************
 * Copyright (c) 2015-2019 Matt Tropiano
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package net.mtrop.doom.sound;

import java.io.IOException;

import net.mtrop.doom.WadFile;
import net.mtrop.doom.sound.MUS;
import net.mtrop.doom.LoggingFactory;
import net.mtrop.doom.LoggingFactory.Logger;


public final class MUSTest
{
	public static void main(String[] args) throws IOException
	{
		Logger logger = LoggingFactory.createConsoleLoggerFor(MUSTest.class);
		WadFile wad = new WadFile(args[0]);
		for (MUS.Event event : wad.getDataAs("D_RUNNIN", MUS.class))
			logger.info(event);
		wad.close();
	}
}