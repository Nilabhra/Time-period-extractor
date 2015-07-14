package test;

import java.util.List;
import java.util.Scanner;

import engine.TPeriod;
import engine.Utility;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		List<String[]> l = Utility.findRange(sc.nextLine());
		for (String[] temp : l) {
			for (String t : temp) {
				System.out.print(t + " ");
			}
			System.out.println();
		}
		List<TPeriod> periods = Utility.getPeriods(l);
		for (TPeriod temp : periods) {
			System.out.println(temp.getStart() + " to " + temp.getEnd() + " {"
					+ temp.getVal() + " days}");
		}
	}
}
