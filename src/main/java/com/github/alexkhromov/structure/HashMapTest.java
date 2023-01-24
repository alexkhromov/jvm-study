package com.github.alexkhromov.structure;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static java.lang.System.out;

public class HashMapTest {

	public static void main(String[] args) {

        /*

        ADD TO WATCHES AND NOTE BEHAVIOR
        ((HashMap) map).table.length
        ((HashMap) map).table
        ((HashMap) map).table[1]
        ((HashMap) map).table[1].next
        ((HashMap) map).table[1].next.next
        new Integer(1).hashCode() & (32 - 1)
        new Integer(17).hashCode() & (32 - 1)

        */

		int capacity = 16;
		float loadFactor = 0.75f;

		Map<Integer, String> map = new HashMap<>(capacity, loadFactor);

		for (int i = 0; i < 6; i++) {

			map.put(i, String.valueOf(i));
			out.println(format(
					"Hash# %s; Index in table[]# %s",
					Integer.valueOf(i).hashCode(),
					Integer.valueOf(i).hashCode() & (capacity - 1)));

			map.put(i + capacity, String.valueOf(i + capacity));
			out.println(format(
					"Hash# %s; Index in table[]# %s",
					Integer.valueOf(i + capacity).hashCode(),
					Integer.valueOf(i + capacity).hashCode() & (capacity - 1)));
		}

		//Comment this block to leave default capacity - 16 and watch how linked list grow
		//and uncomment to watch how capacity is changed and rehashing happens
		map.put(6, String.valueOf(6));
		out.println(format(
				"Hash# %s; Index in table[]# %s",
				Integer.valueOf(6).hashCode(),
				Integer.valueOf(6).hashCode() & (capacity - 1)));

		out.println(map);
		out.println(map.size());
	}
}
