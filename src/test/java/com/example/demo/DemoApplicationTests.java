package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class DemoApplicationTests {

	Calculator calculatorUnderTest = new Calculator();

	@Test
	void itShouldAddTwoNumbers() {
		// given
		int valOne = 20;
		int valTwo = 25;

		// when
		int result = calculatorUnderTest.add(valOne, valTwo);

		// then
		int expected = 45;
		assertThat(result)
				.isNotNull()
				.isNotNegative()
				.isInstanceOf(Integer.class)
				.isEqualTo(expected);
	}

	static class Calculator {

		public int add(int firstValue, int secondValue) {
			return firstValue + secondValue;
		}
	}
}
