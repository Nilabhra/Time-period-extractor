package test;

import java.util.List;

import engine.Utility;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String[]> l = Utility
				.findRange("for the last 3 years they worked between february to march of each year and for the last 6 months I worked between february to aug of each year and then I went on a vacation on Jan for coming 5 months.");
		for (String[] temp : l) {
			for (String t : temp) {
				System.out.print(t + " ");
			}
			System.out.println();
		}
	}
}
