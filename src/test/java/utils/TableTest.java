package utils;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import table.StatisticTable;

public class TableTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test1() {

		StatisticTable table = new StatisticTable();
		table.add("a").increase();
		table.add("b").increase();

		List<List<String>> trList = table.getTrList();

		assertArrayEquals("", trList.get(0).toArray(), new String[] { "a", "1" });
		assertArrayEquals("", trList.get(1).toArray(), new String[] { "b", "1" });
	}

	@Test
	public void test2() {

		StatisticTable table = new StatisticTable();
		table.add("test").increase();
		table.add("test").increase();
		List<List<String>> trList = table.getTrList();

		assertArrayEquals("", trList.get(0).toArray(), new String[] { "test", "2" });
	}

	@Test
	public void test3() {

		StatisticTable table = new StatisticTable();
		table.add("a").add("b").increase();
		table.add("a").add("b").increase();
		List<List<String>> trList = table.getTrList();

		assertArrayEquals("", trList.get(0).toArray(), new String[] { "a", "b", "2" });

	}

	@Test
	public void test4() {

		StatisticTable table = new StatisticTable();
		table.add("a").add("b").add("c").increase();
		table.add("a").add("b").add("d").increase();
		table.add("a").add("e").increase();
		List<List<String>> trList = table.getTrList();

		assertArrayEquals("", trList.get(0).toArray(), new String[] { "a", "b", "c", "1" });
		assertArrayEquals("", trList.get(1).toArray(), new String[] { "a", "b", "d", "1" });
		assertArrayEquals("", trList.get(2).toArray(), new String[] { "a", "e", "1" });

	}

	@Test
	public void test5() {

		StatisticTable table = new StatisticTable();
		table.add("a").add("b").average(4);
		table.add("a").add("b").average(3);
		table.add("a").add("b").average(2);
		List<List<String>> trList = table.getTrList();

		assertArrayEquals("", trList.get(0).toArray(), new String[] { "a", "b", "3" });

	}

}
