package engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utility {
	final static String startWords[] = { "for", "since", "from", "between",
			"next", "by" };
	final static String calRef[] = { "year", "month", "day", "mon", "tue",
			"wed", "thu", "fri", "sat", "sun", "jan", "feb", "mar", "apr",
			"may", "jun", "jul", "aug", "sep", "oct", "nov", "dec" };

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
}
