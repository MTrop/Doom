package net.mtrop.doom.map.binary;

import java.io.IOException;
import java.io.InputStream;

import net.mtrop.doom.WadFile;

import com.blackrook.commons.Common;
import com.blackrook.commons.logging.Logger;
import com.blackrook.commons.logging.LoggingFactory;

public class StrifeThingTest
{
	public static void main(String[] args) throws IOException
	{
		Logger logger = LoggingFactory.createConsoleLoggerFor(StrifeThingTest.class);
		
		WadFile wad = new WadFile(args[0]);
		InputStream in = wad.getDataAsStream("THINGS");

		int i = 0;
		byte[] b = new byte[10];
		while (in.read(b) > 0)
			logger.info((i++) + " " + StrifeThing.create(b));
		
		Common.close(in);
		Common.close(wad);
	}
}