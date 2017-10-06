package utils;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TableTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test1() {

		Table table = new Table();
		table.add("a");
		table.add("b");

		List<List<String>> trList = table.getTrList();

		assertArrayEquals("", trList.get(0).toArray(), new String[] { "a", "1" });
		assertArrayEquals("", trList.get(1).toArray(), new String[] { "b", "1" });
	}

	@Test
	public void test2() {

		Table table = new Table();
		table.add("test");
		table.add("test");
		List<List<String>> trList = table.getTrList();

		assertArrayEquals("", trList.get(0).toArray(), new String[] { "test", "2" });
	}

	@Test
	public void test3() {

		Table table = new Table();
		table.add("a").add("b");
		table.add("a").add("b");
		List<List<String>> trList = table.getTrList();

		assertArrayEquals("", trList.get(0).toArray(), new String[] { "a", "b", "2" });

	}

	@Test
	public void test4() {

		Table table = new Table();
		table.add("a").add("b").add("c");
		table.add("a").add("b").add("d");
		table.add("a").add("e");
		List<List<String>> trList = table.getTrList();

		assertArrayEquals("", trList.get(0).toArray(), new String[] { "a", "b", "c", "1" });
		assertArrayEquals("", trList.get(1).toArray(), new String[] { "a", "b", "d", "1" });
		assertArrayEquals("", trList.get(2).toArray(), new String[] { "a", "e", "1" });

	}

}
