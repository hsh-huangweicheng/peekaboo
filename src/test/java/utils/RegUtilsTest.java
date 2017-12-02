package utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bean.RegPair;

public class RegUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		List<RegPair> list = new ArrayList<>();

		list.add(new RegPair("USA", new String[] { "USA" }));

		assertEquals("USA", Utils.getMatchedKey("USA", list));
		assertEquals("USA", Utils.getMatchedKey("usa", list));
		assertEquals("USA", Utils.getMatchedKey(" usa]", list));
	}

}
