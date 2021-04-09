package com.traffic.report;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;
@SpringBootTest
class MonitorApplicationTests {

	@Test
	void contextLoads() {
		String s = "2021-03-31T15:50:10.747";
		int index = s.lastIndexOf("-")+1;
		assertEquals("31", s.substring(index, index+2));
	}

}
