package com.example.bootIML;

import com.example.bootIML.interpretator.Interpretator;
import com.example.bootIML.interpretator.StatD;
import com.example.bootIML.service.ArrayFilFiles;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class BootImlApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("TEST: contexLoads");
	}

	@Test
	void testInterpretator() {
		String srcCode = System.getProperty("user.dir") + System.getProperty("file.separator") + "ext-gcd.txt";
		System.out.println("TEST: testInterpretator");

		StatD.TID = new ArrayList<>();
		StatD.restArg = new ArrayList<>();
		ArrayFilFiles.filFiles = new ArrayList();

		System.out.println(srcCode);
		Interpretator I = new Interpretator(srcCode);
		I.interpretation();

		ArrayFilFiles.filFiles.forEach(s -> System.out.println(s));
		List<Integer> tstFiles = List.of(5, 3, 1, -1, 2);
		Assertions.assertArrayEquals(ArrayFilFiles.filFiles.toArray(), tstFiles.toArray());
	}
}
