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

/**
 * Base interface for all WAD file type implementations for reading and writing to WAD structures, either in memory or
 * on disk.
 * <p>
 * All entries in a WAD are abstracted as WadEntry objects, which contain the name and offsets for the corresponding
 * data in a WAD file. Note that there may be several entries in a WAD that have the same name; entry "equality" should
 * be determined by name, size and offset.
 * <p>
 * Entries are by no means attached to their source WADs. Attempting to retrieve content from one WAD using entry data
 * from another WAD may have unintended consequences!
 * <p>
 * There may be some implementations of this structure that do not support certain operations, so in those cases, those
 * methods may throw an {@link UnsupportedOperationException}. Also, certain implementations may be more suited for
 * better tasks, so be sure to figure out which implementation suits your needs!
 * 
 * @author Matthew Tropiano
 */
public interface Wad extends Iterable<WadEntry>
{
	/**
	 * Checks if this WAD is an Information WAD.
	 * @return true if so, false if not.
	 */
	public boolean isIWAD();
	
	/**
	 * Checks if this WAD is a Patch WAD.
	 * @return true if so, false if not.
	 */
	public boolean isPWAD();
	
	/**
	 * Gets the WadEntry at index n.
	 * 
	 * @param n the index of the entry in the entry list.
	 * @throws ArrayIndexOutOfBoundsException if n &lt; 0 or &gt; size.
	 * @return the entry at <code>n</code>.
	 */
	public WadEntry getEntry(int n);

	/**
	 * Gets the first WadEntry named <code>entryName</code>.
	 * 
	 * @param entryName the name of the entry.
	 * @return the first entry named <code>entryName</code> or <code>null</code> if not found.
	 */
	public WadEntry getEntry(String entryName);

	/**
	 * Gets the first WadEntry named <code>entryName</code>, starting from a particular index.
	 * 
	 * @param entryName the name of the entry.
	 * @param startIndex the starting index to search from.
	 * @return the first entry named <code>entryName</code> or <code>null</code> if not found.
	 */
	public WadEntry getEntry(String entryName, int startIndex);

	/**
	 * Gets the n-th WadEntry named <code>entryName</code>.
	 * 
	 * @param entryName the name of the entry.
	 * @param n the n-th occurrence to find, 0-based (0 is first, 1 is second, and so on).
	 * @return the n-th entry named <code>entryName</code> or <code>null</code> if not found.
	 */
	public WadEntry getNthEntry(String entryName, int n);

	/**
	 * Gets the last WadEntry named <code>entryName</code>.
	 * 
	 * @param entryName the name of the entry.
	 * @return the last entry named <code>entryName</code> or <code>null</code> if not found.
	 */
	public WadEntry getLastEntry(String entryName);

	/**
	 * Returns all WadEntry objects.
	 * 
	 * @return an array of all of the WadEntry objects.
	 */
	public WadEntry[] getAllEntries();

	/**
	 * Returns all WadEntry objects named <code>entryName</code>.
	 * 
	 * @param entryName the name of the entry.
	 * @return an array of all of the WadEntry objects with the name <code>entryName</code>.
	 */
	public WadEntry[] getAllEntries(String entryName);

	/**
	 * Gets the indices of all WadEntry objects named <code>entryName</code>.
	 * 
	 * @param entryName the name of the entry.
	 * @return an array of all of the WadEntry objects with the name <code>entryName</code>.
	 */
	public int[] getAllEntryIndices(String entryName);

	/**
	 * Gets the first index of an entry of name <code>entryName</code>.
	 * 
	 * @param entryName the name of the entry to find.
	 * @return the index of the entry in this file, or -1 if not found.
	 * @throws NullPointerException if <code>entryName</code> is <code>null</code>.
	 */
	public int getIndexOf(String entryName);

	/**
	 * Gets the first index of an entry of name "entryName" from a starting point.
	 * 
	 * @param entryName the name of the entry to find.
	 * @param start the index with which to start the search.
	 * @return the index of the entry in this file, or -1 if not found.
	 * @throws ArrayIndexOutOfBoundsException if start &lt; 0 or &gt; size.
	 * @throws NullPointerException if <code>entryName</code> is <code>null</code>.
	 */
	public int getIndexOf(String entryName, int start);

	/**
	 * Gets the last index of an entry of name <code>entryName</code>.
	 * 
	 * @param entryName the name of the entry to find.
	 * @return the index of the entry in this file, or -1 if not found.
	 * @throws NullPointerException if <code>entryName</code> is <code>null</code>.
	 */
	public int getLastIndexOf(String entryName);

	/**
	 * Retrieves the data of a particular entry index.
	 * 
	 * @param n the index of the entry in the file.
	 * @throws IOException if the data couldn't be retrieved.
	 * @throws ArrayIndexOutOfBoundsException if n &lt; 0 or &gt; size.
	 * @return a byte array of the data.
	 */
	public byte[] getData(int n) throws IOException;

	/**
	 * Retrieves the data of the first occurrence of a particular entry.
	 * 
	 * @param entryName the name of the entry to find.
	 * @return a byte array of the data, or null if the entry doesn't exist.
	 * @throws IOException if the data couldn't be retrieved.
	 * @throws NullPointerException if <code>entryName</code> is <code>null</code>.
	 */
	public byte[] getData(String entryName) throws IOException;

	/**
	 * Retrieves the data of the first occurrence of a particular entry from a starting index.
	 * 
	 * @param entryName the name of the entry to find.
	 * @param start the index with which to start the search.
	 * @return a byte array of the data, or null if the entry doesn't exist.
	 * @throws IOException if the data couldn't be retrieved.
	 * @throws NullPointerException if <code>entryName</code> is <code>null</code>.
	 * @throws ArrayIndexOutOfBoundsException if start &lt; 0 or &gt; size.
	 */
	public byte[] getData(String entryName, int start) throws IOException;

	/**
	 * Retrieves the data of the specified entry.
	 * 
	 * @param entry the entry to use.
	 * @return a byte array of the data.
	 * @throws IOException if the data couldn't be retrieved or the entry's offsets breach the file extents.
	 * @throws NullPointerException if <code>entry</code> is <code>null</code>.
	 */
	public byte[] getData(WadEntry entry) throws IOException;

	/**
	 * Retrieves the data of a particular entry index and returns it as a stream.
	 * 
	 * @param n the index of the entry in the file.
	 * @return a ByteArrayInputStream of the data, or null if it can't be retrieved.
	 * @throws IOException if the data couldn't be retrieved.
	 * @throws ArrayIndexOutOfBoundsException if n &lt; 0 or &gt; size.
	 */
	public InputStream getInputStream(int n) throws IOException;

	/**
	 * Retrieves the data of the first occurrence of a particular entry and returns it as a stream.
	 * 
	 * @param entryName the name of the entry to find.
	 * @return a ByteArrayInputStream of the data, or null if it can't be retrieved.
	 * @throws IOException if the data couldn't be retrieved.
	 * @throws NullPointerException if <code>entryName</code> is <code>null</code>.
	 */
	public InputStream getInputStream(String entryName) throws IOException;

	/**
	 * Retrieves the data of the first occurrence of a particular entry from a starting index and returns it as a stream.
	 * 
	 * @param entryName the name of the entry to find.
	 * @param start the index with which to start the search.
	 * @return a ByteArrayInputStream of the data, or null if it can't be retrieved.
	 * @throws IOException if the data couldn't be retrieved.
	 * @throws NullPointerException if <code>entryName</code> is <code>null</code>.
	 * @throws ArrayIndexOutOfBoundsException if start &lt; 0 or &gt; size.
	 */
	public InputStream getInputStream(String entryName, int start) throws IOException;

	/**
	 * Retrieves the data of the specified entry from a starting index and returns it as a stream.
	 * 
	 * @param entry the entry to use.
	 * @return a ByteArrayInputStream of the data.
	 * @throws IOException if the data couldn't be retrieved or the entry's offsets breach the file extents.
	 * @throws NullPointerException if <code>entry</code> is <code>null</code>.
	 */
	public InputStream getInputStream(WadEntry entry) throws IOException;

	/**
	 * @return the number of entries in this Wad.
	 */
	public int getSize();

	/**
	 * Checks if this Wad contains a particular entry, false otherwise.
	 * The name is case-sensitive. 
	 * @param entryName the name of the entry.
	 * @return true if so, false if not.
	 */
	public boolean contains(String entryName);

	/**
	 * Checks if this Wad contains a particular entry from a starting entry index, false otherwise.
	 * The name is case-sensitive. 
	 * 
	 * @param entry the name of the entry.
	 * @param index the index 
	 * @return true if so, false if not.
	 */
	public boolean contains(String entry, int index);

	/**
	 * Adds data to this Wad, using entryName as the name of the entry. The overhead for multiple additions may be
	 * expensive I/O-wise depending on the DoomWad implementation.
	 * 
	 * @param entryName the name of the entry to add this as.
	 * @param data the bytes of data to add as this wad's data.
	 * @return a WadEntry that describes the added data.
	 * @throws IllegalArgumentException if the provided name is not a valid name.
	 * @throws IOException if the data cannot be written.
	 * @throws NullPointerException if <code>entryName</code> or <code>data</code> is <code>null</code>.
	 */
	public WadEntry addData(String entryName, byte[] data) throws IOException;

	/**
	 * Adds data to this Wad at a particular entry offset, using entryName as the name of the entry. The rest of the
	 * entries in the wad are shifted down one index. The overhead for multiple additions may be expensive I/O-wise
	 * depending on the DoomWad implementation.
	 * 
	 * @param index the index at which to add the entry.
	 * @param entryName the name of the entry to add this as.
	 * @param data the bytes of data to add as this wad's data.
	 * @return a WadEntry that describes the added data.
	 * @throws IllegalArgumentException if the provided name is not a valid name.
	 * @throws IOException if the data cannot be written.
	 * @throws NullPointerException if <code>entryName</code> or <code>data</code> is <code>null</code>.
	 */
	public WadEntry addDataAt(int index, String entryName, byte[] data) throws IOException;

	/**
	 * Adds multiple entries of data to this Wad, using entryNames as the name of the entry, using the same indices
	 * in the data array as the corresponding data.
	 * 
	 * @param entryNames the names of the entries to add.
	 * @param data the bytes of data to add as each entry's data.
	 * @return an array of WadEntry objects that describe the added data.
	 * @throws IOException if the data cannot be written.
	 * @throws ArrayIndexOutOfBoundsException if the lengths of entryNames and data do not match.
	 * @throws NullPointerException if an object if <code>entryNames</code> or <code>data</code> is <code>null</code>.
	 */
	public WadEntry[] addAllData(String[] entryNames, byte[][] data) throws IOException;

	/**
	 * Adds multiple entries of data to this Wad at a particular entry offset, using entryNames as the name 
	 * of the entry, using the same indices in the data array as the corresponding data.
	 * 
	 * @param index the index to add these entries at.
	 * @param entryNames the names of the entries to add.
	 * @param data the bytes of data to add as each entry's data.
	 * @return an array of WadEntry objects that describe the added data.
	 * @throws IOException if the data cannot be written.
	 * @throws ArrayIndexOutOfBoundsException if the lengths of entryNames and data do not match.
	 * @throws NullPointerException if an object if <code>entryNames</code> or <code>data</code> is <code>null</code>.
	 */
	public WadEntry[] addAllDataAt(int index, String[] entryNames, byte[][] data) throws IOException;

	/**
	 * Adds an entry marker to the Wad (entry with 0 size, arbitrary offset).
	 * 
	 * @param name the name of the entry.
	 * @return the entry that was added.
	 * @throws IllegalArgumentException if the provided name is not a valid name.
	 * @throws IOException if the entry cannot be written.
	 * @throws NullPointerException if <code>name</code> is <code>null</code>.
	 */
	public WadEntry addMarker(String name) throws IOException;

	/**
	 * Adds an entry marker to the Wad (entry with 0 size, arbitrary offset).
	 * 
	 * @param index the index at which to add the marker.
	 * @param name the name of the entry.
	 * @return the entry that was added.
	 * @throws IllegalArgumentException if the provided name is not a valid name.
	 * @throws IOException if the entry cannot be written.
	 * @throws NullPointerException if <code>name</code> is <code>null</code>.
	 */
	public WadEntry addMarkerAt(int index, String name) throws IOException;

	/**
	 * Replaces the entry at an index in the WAD.
	 * 
	 * @param index the index of the entry to replace.
	 * @param data the data to replace the entry with.
	 * @throws IllegalArgumentException if the provided name is not a valid name.
	 * @throws IOException if the entry cannot be written.
	 * @throws NullPointerException if <code>data</code> is <code>null</code>.
	 */
	public void replaceEntry(int index, byte[] data) throws IOException;

	/**
	 * Renames the entry at an index in the WAD.
	 * 
	 * @param index the index of the entry to rename.
	 * @param newName the new name of the entry.
	 * @throws IllegalArgumentException if the provided name is not a valid name.
	 * @throws IOException if the entry cannot be renamed.
	 */
	public void renameEntry(int index, String newName) throws IOException;

	/**
	 * Deletes a Wad's entry and its contents. The overhead for multiple deletions may be expensive I/O-wise.
	 * 
	 * @param n the index of the entry to delete.
	 * @throws ArrayIndexOutOfBoundsException if n &lt; 0 or &gt; size.
	 * @throws IOException if the file cannot be altered in such a manner.
	 */
	public void deleteEntry(int n) throws IOException;

	/**
	 * Retrieves a contiguous set of entries from this Wad, starting from a desired index. If the amount of entries
	 * desired goes outside the Wad's potential set of entries, this will retrieve up to those entries (for example,
	 * <code>mapEntries(5, 10)</code> in an 8-entry Wad will only return 3 entries: 5, 6, and 7).
	 * 
	 * @param startIndex the starting index to map from (inclusive).
	 * @param maxLength the amount of entries to retrieve from the index position.
	 * @return an array of references to {@link WadEntry} objects.
	 * @throws IllegalArgumentException if startIndex is less than 0.
	 */
	public WadEntry[] mapEntries(int startIndex, int maxLength);

	/**
	 * Replaces a series of WadEntry objects in this Wad, using the provided list of entries as the replacement list. If
	 * the list of entries plus the starting index would breach the original list of entries, the excess is appended to
	 * the Wad.
	 * 
	 * @param startIndex the starting index to replace from (inclusive).
	 * @param entryList the set of entries to replace (in order) from the starting index.
	 * @throws IOException if the entries ould not be written.
	 * @throws IllegalArgumentException if startIndex is less than 0.
	 */
	public void unmapEntries(int startIndex, WadEntry[] entryList) throws IOException;

	/**
	 * Completely replaces the list of entries in this Wad with a completely different set of entries.
	 * 
	 * @param entryList the set of entries that will make up this Wad.
	 * @throws IOException if the entries ould not be written.
	 * @throws IllegalArgumentException if startIndex is less than 0.
	 */
	public void setEntries(WadEntry[] entryList) throws IOException;

}
