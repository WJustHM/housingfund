package com.handge.housingfund;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
	}

	public static void main(String[] args) {
		String[] glbs = { "市直", "七星关区", "大方县", "黔西县", "金沙县", "织金县", "纳雍县", "威宁县", "赫章县", "百管委" };
		String[] roles = { "管理部主任", "管理部副主任", "管理部操作员" };
		int i = 16;
		for (String glb : glbs) {
			for (String role : roles) {
				System.out.println("INSERT INTO `c_role` (`id`, `created_at`, `deleted`, `deleted_at`, `updated_at`, `role_name`, `role_note`) VALUES ('" + i
						+ "', '2017-10-12 06:26:41', b'0', null, '2017-10-17 11:44:31', '" + glb + role + "', '" + glb
						+ role + "');");
				i++;
			}
		}
	}
}
