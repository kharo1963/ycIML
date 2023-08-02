package com.example.bootIML.interpretator;

import java.util.ArrayList;
import java.util.Deque;

public class StatD {

	public static ArrayList<String> restArg;

	static <T> T fromStack(Deque<T> stack) {
		T stackTop = stack.remove();
		return stackTop;
	}

}
