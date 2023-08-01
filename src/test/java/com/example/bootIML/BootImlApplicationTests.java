package com.example.bootIML;

import com.example.bootIML.interpretator.Interpretator;
import com.example.bootIML.interpretator.StatD;
import com.example.bootIML.service.ArrayFilFiles;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
class BootImlApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("TEST: contexLoads");
	}

	@Test
	void testInterpretator() {
		String srcCode = System.getProperty("user.dir") + System.getProperty("file.separator") + "ext-gcd.txt";
		log.info("TEST: testInterpretator");

		StatD.TID = new ArrayList<>();
		StatD.restArg = new ArrayList<>();
		ArrayFilFiles.filFiles = new ArrayList();

		log.info(srcCode);
		String sourceText = "";
		Path path = Paths.get(srcCode);
		try {
			List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
			for (String line : lines) {
				sourceText += line + System.lineSeparator();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		StatD.sourceText = sourceText.toCharArray();
		Interpretator I = new Interpretator();
		I.interpretation();

		ArrayFilFiles.filFiles.forEach(s -> System.out.println(s));
		List<Integer> tstFiles = List.of(5, 3, 1, -1, 2);
		Assertions.assertArrayEquals(ArrayFilFiles.filFiles.toArray(), tstFiles.toArray());
	}
}
