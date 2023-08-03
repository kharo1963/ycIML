package com.example.bootIML;

import com.example.bootIML.interpretator.Interpretator;
import com.example.bootIML.interpretator.SourceProgram;
import com.example.bootIML.service.GraphicsService;
import com.example.bootIML.service.ImlParamServiceImpl;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
class BootImlApplicationTests {
	private final ImlParamServiceImpl imlParamServiceImpl;
	private final GraphicsService graphicsService;
	@Test
	void contextLoads() {
		log.info("TEST: contexLoads");
	}

	@Test
	void testInterpretator() {
		String srcCode = System.getProperty("user.dir") + System.getProperty("file.separator") + "ext-gcd.txt";
		log.info("TEST: testInterpretator");
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
		SourceProgram sourceProgram = new SourceProgram(sourceText.toCharArray(),imlParamServiceImpl, graphicsService);
		sourceProgram.restArg = new ArrayList<>();
		sourceProgram.TID = new ArrayList<>();
		sourceProgram.resultList = new ArrayList();
		Interpretator interpretator = new Interpretator(sourceProgram);
		interpretator.interpretation();
		sourceProgram.resultList.forEach(s -> System.out.println(s));
		List<Integer> tstFiles = List.of(5, 3, 1, -1, 2);
		Assertions.assertArrayEquals(sourceProgram.resultList.toArray(), tstFiles.toArray());
	}
}
