package net.mtrop.doom;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import net.mtrop.doom.exception.WadException;

import com.blackrook.commons.linkedlist.Queue;
import com.blackrook.commons.list.List;
import com.blackrook.io.SuperReader;

/**
 * This is just a basic mapping of WAD entries to a file.
 * The file is NOT kept open after the read, and the file or
 * stream used to gather the WAD metadata is not kept.
 * <p>
 * This may not be added to or changed, and its data may not be read directly,
 * because this is just a mapping of entries. Individual entries may be read
 * for data offset information and then read from the corresponding file or
 * stream.
 * <p>
 * Despite the name, this is not a structure that reads Doom Map information.
 * Use {@link DoomMap} for that purpose.  
 * @author Matthew Tropiano
 */
public class WadMap implements Wad
{
	/** Type of Wad File (IWAD or PWAD). */
	private Type type;
	/** The list of entries. */
	protected List<WadEntry> entries;

	private WadMap()
	{
		entries = new List<WadEntry>();
	}
	
	/**
	 * Creates a new WadMap using the contents of a file, denoted by the path.
	 * @param path the path to the file to read.
	 */
	public WadMap(String path) throws IOException
	{
		this(new File(path));
	}
	
	/**
	 * Creates a new WadMap using the contents of a file.
	 * @param f the file to read.
	 */
	public WadMap(File f) throws IOException
	{
		this();
		FileInputStream fis = new FileInputStream(f);
		readWad(fis);
		fis.close();
	}
	
	/**
	 * Creates a new WadMap.
	 * @param in the input stream.
	 */
	public WadMap(InputStream in) throws IOException
	{
		this();
		readWad(in);
	}

	/**
	 * Reads in a WAD structure from an InputStream.
	 * @param in
	 */
	private void readWad(InputStream in) throws IOException
	{
		SuperReader sr = new SuperReader(in, SuperReader.LITTLE_ENDIAN);
		entries.clear();

		try {
			type = Type.valueOf(sr.readASCIIString(4));
		} catch (IllegalArgumentException e) {
			throw new WadException("Not a WAD file.");
		}
		int entryCount = sr.readInt();
		int contentsize = sr.readInt() - 12;
		
		entries.setCapacity(entryCount);
		
		// skip content.
		in.skip(contentsize);
		
		byte[] entrybuffer = new byte[16];
		for (int x = 0; x < entryCount; x++)
		{
			sr.readBytes(entrybuffer);
			WadEntry entry = WadEntry.create(entrybuffer);
			entries.add(entry);
		}
	}

	@Override
	public boolean isIWAD()
	{
		return type == Type.IWAD;
	}

	@Override
	public boolean isPWAD()
	{
		return type == Type.PWAD;
	}

	@Override
	public int getSize()
	{
		return entries.size();
	}

	@Override
	public WadEntry addData(String entryName, byte[] data) throws IOException
	{
		throw new UnsupportedOperationException("This class does not support getDataAsStream()");
	}

	@Override
	public WadEntry addDataAt(int index, String entryName, byte[] data) throws IOException
	{
		throw new UnsupportedOperationException("This class does not support getDataAsStream()");
	}

	@Override
	public WadEntry[] addAllData(String[] entryNames, byte[][] data) throws IOException
	{
		throw new UnsupportedOperationException("This class does not support getDataAsStream()");
	}

	@Override
	public WadEntry[] addAllDataAt(int index, String[] entryNames, byte[][] data) throws IOException
	{
		throw new UnsupportedOperationException("This class does not support getDataAsStream()");
	}

	@Override
	public WadEntry addMarker(String name) throws IOException
	{
		throw new UnsupportedOperationException("This class does not support getDataAsStream()");
	}

	@Override
	public WadEntry addMarkerAt(int index, String name) throws IOException
	{
		throw new UnsupportedOperationException("This class does not support getDataAsStream()");
	}

	@Override	
	public boolean contains(String entryName)
	{
		return getIndexOf(entryName, 0) > -1;
	}

	@Override	
	public boolean contains(String entryName, int index)
	{
		return getIndexOf(entryName, index) > -1;
	}

	@Override
	public void deleteEntry(int n) throws IOException
	{
		throw new UnsupportedOperationException("This class does not support getDataAsStream()");
	}

	@Override
	public void renameEntry(int index, String newName) throws IOException
	{
		throw new UnsupportedOperationException("This class does not support getDataAsStream()");
	}

	@Override
	public void replaceEntry(int index, byte[] data) throws IOException
	{
		throw new UnsupportedOperationException("This class does not support getDataAsStream()");
	}

	@Override
	public WadEntry[] mapEntries(int startIndex, int maxLength)
	{
		if (startIndex < 0)
			throw new IllegalArgumentException("Starting index cannot be less than 0.");
	
		int len = Math.min(maxLength, getSize() - startIndex);
		if (len <= 0)
			return new WadEntry[0];
		WadEntry[] out = new WadEntry[len];
		for (int i = 0; i < len; i++)
			out[i] = getEntry(startIndex + i);
		return out;
	}

	@Override
	public void unmapEntries(int startIndex, WadEntry[] entryList) throws IOException
	{
		throw new UnsupportedOperationException("This class does not support getDataAsStream()");
	}

	@Override
	public void setEntries(WadEntry[] entryList) throws IOException
	{
		throw new UnsupportedOperationException("This class does not support getDataAsStream()");
	}

	@Override	
	public byte[] getData(int n) throws IOException
	{
		throw new UnsupportedOperationException("This class does not support getDataAsStream()");
	}

	@Override	
	public byte[] getData(String entryName) throws IOException
	{
		throw new UnsupportedOperationException("This class does not support getDataAsStream()");
	}

	@Override	
	public byte[] getData(String entryName, int start) throws IOException
	{
		throw new UnsupportedOperationException("This class does not support getDataAsStream()");
	}

	@Override	
	public byte[] getData(WadEntry entry) throws IOException
	{
		throw new UnsupportedOperationException("This class does not support getDataAsStream()");
	}

	@Override	
	public InputStream getDataAsStream(int n) throws IOException
	{
		throw new UnsupportedOperationException("This class does not support getDataAsStream()");
	}

	@Override	
	public InputStream getDataAsStream(String WadEntry) throws IOException
	{
		throw new UnsupportedOperationException("This class does not support getDataAsStream()");
	}

	@Override	
	public InputStream getDataAsStream(String WadEntry, int start) throws IOException
	{
		throw new UnsupportedOperationException("This class does not support getDataAsStream()");
	}

	@Override	
	public InputStream getDataAsStream(WadEntry WadEntry) throws IOException
	{
		throw new UnsupportedOperationException("This class does not support getDataAsStream()");
	}

	@Override	
	public WadEntry getEntry(int n)
	{
		return entries.getByIndex(n);
	}

	@Override	
	public WadEntry getEntry(String entryName)
	{
		int i = getIndexOf(entryName, 0);
		return i != -1 ? getEntry(i) : null;
	}

	@Override	
	public WadEntry getEntry(String entryName, int startingIndex)
	{
		int i = getIndexOf(entryName, startingIndex);
		return i != -1 ? getEntry(i) : null;
	}

	@Override	
	public WadEntry getNthEntry(String entryName, int n)
	{
		int x = 0;
		for (int i = 0; i < entries.size(); i++)
		{
			WadEntry entry = entries.getByIndex(i);
			if (entry.getName().equals(entryName))
			{
				if (x++ == n)
					return entry;
			}
		}
		return null;
	}

	@Override	
	public WadEntry getLastEntry(String entryName)
	{
		for (int i = entries.size() - 1; i >= 0; i--)
		{
			WadEntry entry = entries.getByIndex(i);
			if (entry.getName().equals(entryName))
				return entry;
		}
		return null;
	}

	@Override	
	public WadEntry[] getAllEntries()
	{
		WadEntry[] out = new WadEntry[entries.size()];
		entries.toArray(out);
		return out;
	}

	@Override	
	public WadEntry[] getAllEntries(String entryName)
	{
		Queue<WadEntry> w = new Queue<WadEntry>();
		
		for (int i = 0; i < entries.size(); i++)
		{
			WadEntry entry = entries.getByIndex(i);
			if (entry.getName().equals(entryName))
				w.enqueue(entry);
		}
		
		WadEntry[] out = new WadEntry[w.size()];
		w.toArray(out);
		return out;
	}

	@Override
	public int[] getAllEntryIndices(String entryName)
	{
		Queue<Integer> w = new Queue<Integer>();
		
		for (int i = 0; i < entries.size(); i++)
		{
			WadEntry entry = entries.getByIndex(i);
			if (entry.getName().equals(entryName))
				w.enqueue(i);
		}
		
		int[] out = new int[w.size()];
		for (int i = 0; i < entries.size(); i++)
			out[i] = w.dequeue();
		return out;
	}

	@Override	
	public int getIndexOf(String entryName)
	{
		return getIndexOf(entryName, 0);
	}

	@Override	
	public int getIndexOf(String entryName, int start)
	{
		for (int i = start; i < entries.size(); i++)
			if (entries.getByIndex(i).getName().equals(entryName))
				return i;
		return -1;
	}

	@Override	
	public int getLastIndexOf(String entryName)
	{
		int out = -1;
		for (int i = 0; i < entries.size(); i++)
			if (entries.getByIndex(i).getName().equals(entryName))
				out = i;
		return out;
	}

	@Override
	public Iterator<WadEntry> iterator()
	{
		return entries.iterator();
	}
}