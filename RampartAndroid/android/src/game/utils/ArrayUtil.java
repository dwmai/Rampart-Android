package game.utils;

import java.util.ArrayList;

public class ArrayUtil {

	public static <T> ArrayList<T> resize(ArrayList<T> list, int size) {
		if (list == null)
			list = new ArrayList<T>(size);
		list.ensureCapacity(size);
		for (int i = 0; i < size; i++)
			list.add(null);
		return list;
	}
}
