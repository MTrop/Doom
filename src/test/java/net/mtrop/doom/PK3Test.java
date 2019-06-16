/*******************************************************************************
 * Copyright (c) 2015-2019 Matt Tropiano
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package net.mtrop.doom;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import net.mtrop.doom.TestUtils.AfterAllTests;
import net.mtrop.doom.TestUtils.AfterEachTest;
import net.mtrop.doom.TestUtils.BeforeAllTests;
import net.mtrop.doom.TestUtils.BeforeEachTest;
import net.mtrop.doom.TestUtils.Test;

import static net.mtrop.doom.TestUtils.assertEqual;

public final class PK3Test
{
	private static final File TEST_DIR = new File("test");
	private static final File TEST_PK3 = new File("src/test/resources/viscerus.pk3");
	
	private static DoomPK3 pk3;
	
	@BeforeAllTests
	public static void beforeAllTests() throws Exception
	{
		pk3 = new DoomPK3(TEST_PK3);
		assertEqual(TEST_DIR.mkdirs(), true);
	}

	@AfterAllTests
	public static void afterAllTests() throws Exception
	{
		assertEqual(TEST_DIR.delete(), true);
		pk3.close();
	}
	
	@BeforeEachTest
	public void beforeEachTest() throws Exception
	{
		// Nothing.
	}
	
	@AfterEachTest
	public void afterEachTest() throws Exception
	{
		for (File f : TEST_DIR.listFiles())
			f.delete();
	}
	
	@Test
	public void getFilePath() throws Exception
	{
		assertEqual(pk3.getFilePath(), TEST_PK3.getPath());
	}

	@Test
	public void getFileName() throws Exception
	{
		assertEqual(pk3.getFileName(), TEST_PK3.getName());
	}

	@Test
	public void getEntriesStartingWith() throws Exception
	{
		List<String> entries = pk3.getEntriesStartingWith("maps/");
		assertEqual(entries.get(0), "maps/map01.wad");
		assertEqual(entries.get(1), "maps/map02.wad");
		assertEqual(entries.get(2), "maps/map03.wad");
		assertEqual(entries.get(3), "maps/map04.wad");
		assertEqual(entries.get(4), "maps/map05.wad");
		assertEqual(entries.get(5), "maps/map06.wad");
		assertEqual(entries.get(6), "maps/map07.wad");
	}

	@Test
	public void getEntryCount() throws Exception
	{
		assertEqual(pk3.getEntryCount(), 82);
	}

	@Test
	public void containsEntry() throws Exception
	{
		assertEqual(pk3.contains("maps/map01.wad"), true);
		assertEqual(pk3.contains("maps/map08.wad"), false);
	}

	@Test
	public void getData() throws Exception
	{
		byte[] data = pk3.getData("maps/map02.wad");
		assertEqual(data.length, 250034);
	}

	@Test
	public void getInputStream() throws Exception
	{
		InputStream in = pk3.getInputStream("maps/map02.wad");
		byte[] buf = new byte[8192];
		int b = 0;
		int out = 0;
		while ((b = in.read(buf)) > 0)
			out += b;
		in.close();
		assertEqual(out, 250034);
	}

	/*
	getDataAs(String, Class<BO>)
	getDataAs(String, Class<BO>, int)
	getDataAsList(String, Class<BO>, int)
	getDataAsWadMap(String)
	getDataAsTempWadFile(String, String)
	getDataAsWadBuffer(String)
	getFile(String, String)
	getTextData(String, Charset)
	getTextDataAs(String, Charset, Class<TO>)
	getReader(String, Charset)
	getScanner(String, Class<BO>, int)
	getInlineScanner(String, Class<BO>, int)
	 */
}