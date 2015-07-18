package engine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Utility {
	final static String startWords[] = { "last", "past", "since", "from",
			"for", "between", "next", "by", "coming", "within" };
	final static String calRef[] = { "year", "month", "day", "week", "mon",
			"tue", "wed", "thu", "fri", "sat", "sun", "jan", "feb", "mar",
			"apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec" };
	final static String offsetIndicator[] = { "last", "past", "next", "coming",
			"by", "within" };
	final static String rangeIndicator[] = { "to", "-", "and", "of" };

	static enum days {
		Mon("mon", 1), Tue("tue", 2), Wed("wed", 3), Thu("thu", 4), Fri("fri",
				5), Sat("sat", 6), Sun("sun", 7);
		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public int getVal() {
			return val;
		}

		public void setVal(int val) {
			this.val = val;
		}

		private String text;
		private int val;

		days(String text, int val) {
			this.text = text;
			this.val = val;
		}

	}

	static enum months {
		Jan("jan", 1), Feb("feb", 2), Mar("mar", 3), Apr("apr", 4), May("may",
				5), Jun("jun", 6), Jul("jul", 7), Aug("aug", 8), Sep("sep", 9), Oct(
				"oct", 10), Nov("nov", 11), Dec("dec", 12);
		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public int getVal() {
			return val;
		}

		public void setVal(int val) {
			this.val = val;
		}

		private String text;
		private int val;

		months(String text, int val) {
			this.text = text;
			this.val = val;
		}

	}

	public static List<Integer> findAnchors(String s) {

		List<Integer> startIndices = new ArrayList<>();
		for (int i = -1; (i = s.indexOf(" for ", i + 1)) != -1;) {
			System.out.println(i);
			startIndices.add(i);
		}
		return startIndices;
	}

	public static List<String[]> findRange(String s) {
		List<Integer> startIndices = new ArrayList<>();
		String[] words = s.split(" ");
		for (int i = 0; i < words.length; ++i) {
			for (String temp : startWords) {
				if (words[i].toLowerCase().equals(temp)) {
					startIndices.add(i);
				}
			}
		}
		List<Integer> endIndices = new ArrayList<>();
		for (int i = 0; i < startIndices.size(); ++i) {
			boolean wasFound = false;
			int tend = 0;
			for (int j = startIndices.get(i) + 1; j < Math.min(i < startIndices
					.size() - 1 ? startIndices.get(i + 1) : words.length,
					words.length); ++j) {
				for (String temp : calRef) {
					if (words[j].toLowerCase().contains(temp)) {
						wasFound = true;
						tend = j;
					}
				}
			}
			if (wasFound) {
				endIndices.add(tend);
			} else {
				startIndices.remove(i);
				--i;
			}
		}
		if (startIndices.size() != endIndices.size()) {
			System.out.println("PANICK!");
			return null;
		}
		List<String[]> ret = new ArrayList<>();
		for (int i = 0; i < startIndices.size(); i++) {
			ret.add(Arrays.copyOfRange(words, startIndices.get(i),
					endIndices.get(i) + 1));
		}

		return ret;
	}

	public static List<TPeriod> getPeriods(List<String[]> seg) {

		List<TPeriod> ret = new ArrayList<>();
		final String[] prev = { "last", "past", "previous" };
		for (String[] s : seg) {
			int dir = 1;
			for (String temp : prev) {
				if (s[0].toLowerCase().equals(temp)) {
					dir = -1;
				}
			}
			int val = 0;
			// TODO: Write code to check if duration is in ranged form
			if (dir == 1 || dir == -1) {
				int in = 1;
				int cal = 0;
				for (int i = 0; i < 4; ++i) {
					if (s[s.length - 1].contains(calRef[i].toLowerCase())) {
						cal = i + 1;
						break;
					}
				}
				if (cal != 0) {
					if (s[1].matches("\\d+")) {
						val = Integer.parseInt(s[1]);
						in++;
					} else if (s[1].matches("^[0-9]*(nd|rd|th)$")) {
						val = Integer.parseInt(s[1].substring(0,
								s[1].length() - 2));
						in++;
					} else {
						String temp = "";
						while (in < s.length) {
							if (NumberText.isNumeric(s[in])) {
								temp += s[in] + " ";

							}
							in++;
						}
						val = Integer.parseInt(NumberText.replaceNumbers(temp));
					}

				}
				// TODO: if the last word is a month or an year (like 2012) then
				// use calender to find the duration
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
				if (s[s.length - 1].toLowerCase().contains("week")) {
					val *= 7;
				} else if (s[s.length - 1].toLowerCase().contains("month")) {
					val *= 30;
				} else if (s[s.length - 1].toLowerCase().contains("year")) {
					val *= 365;
				} else {

				}

				Calendar calc = GregorianCalendar.getInstance();
				Date curr = GregorianCalendar.getInstance().getTime();
				calc.add(Calendar.DAY_OF_YEAR, dir * val);
				Date nDaysAgo = calc.getTime();
				TPeriod temp = new TPeriod();
				temp.setVal(val);
				if (curr.after(nDaysAgo)) {
					temp.setStart(dateFormat.format(nDaysAgo));
					temp.setEnd(dateFormat.format(curr));
				} else {
					temp.setStart(dateFormat.format(curr));
					temp.setEnd(dateFormat.format(nDaysAgo));
				}
				ret.add(temp);
			}
		}

		return ret;
	}
}
