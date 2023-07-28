package com.example.bootIML.interpretator;

import com.example.bootIML.service.GraphicsService;
import com.example.bootIML.service.ImlParamServiceImpl;

import java.util.ArrayList;
import java.util.Deque;

public class StatD {

	public static ImlParamServiceImpl imlParamServiceImpl;
	public static GraphicsService graphicsService;
	public static ArrayList<Ident> TID;
	public static ArrayList<String> restArg;
	
	static <T> T fromStack(Deque<T> stack) {
		T stackTop = stack.remove();
		return stackTop;
	}

}
