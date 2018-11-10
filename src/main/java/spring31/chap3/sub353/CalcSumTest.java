package spring31.chap3.sub353;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class CalcSumTest {
	Calculator calculator;
	String numFilepath;
	
	@Before public void setUp() {
		this.calculator = new Calculator();
		this.numFilepath = getClass().getResource("numbers.text").getPath();
	}
	
	@Test public void sumOfNumbers() throws IOException {
		assertThat(calculator.calcSum(this.numFilepath), equalTo(10));
	}
	
	@Test public void multiplyOfNumbers() throws IOException {
		assertThat(calculator.calcMultiply(this.numFilepath), equalTo(24));
	}
}
