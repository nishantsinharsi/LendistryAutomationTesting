package DBTransaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Test {

	public static void main(String[] args) {
		copyEmailPassword();

	}

	public static synchronized void copyEmailPassword() {

		String mysourcestring = "New username and temporary password:\r\n" + "\r\n"
				+ "Username: test080722489179@yopmail.com\r\n" + "Temporary Password: hpMqlq5kgk.";

		String[] source = mysourcestring.split(": ");
		System.out.println("*" + source[1]);
		System.out.println("*"+source[2]);

		String ss = source[1].substring(0,28);
		System.out.println("---------------");
		System.out.println(ss);

//		String substring = mysourcestring.substring(10, mysourcestring.indexOf(":"));
//
//		System.out.println(substring);

	}

}
