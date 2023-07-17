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
	
	static int from_st_i(Deque<Integer> st) {
	    int i = st.peek();
		st.pop();
	    return i;
	}
	static TypeOfLex from_st_t(Deque<TypeOfLex> st) {
		TypeOfLex i = st.peek();
		st.pop();
	    return i;
	}
}
