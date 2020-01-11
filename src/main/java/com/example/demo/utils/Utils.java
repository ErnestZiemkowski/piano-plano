package com.example.demo.utils;

import java.util.HashSet;
import java.util.Set;

public class Utils {
		
	public static <T> Set<T> mergeSet(Set<T> a, Set<T> b) {
		Set<T> mergeSet = new HashSet<T>();
		mergeSet.addAll(a);
		mergeSet.addAll(b);
		
		return mergeSet;
	}
}
