package com.github.xaxys.simpleprotect.common.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionParser {
	private final Map<String, FilterMode> filters;
	private static final String[] SeperateKey = { "=", ":" };

	public OptionParser() {
		this.filters = new HashMap<String, FilterMode>();
	}

	public void addFilter(String option, FilterMode mode) {
		this.filters.put(option, mode);
	}

	public ParsedOption parse(String args) {
		String[] strings = args.split(" ");
		if (strings.length > 0) {
			return parse(strings);
		}
		return parse(new String[] { args });
	}

	public ParsedOption parse(String[] args) {
		return parse(Arrays.asList(args));
	}

	public ParsedOption parse(List<String> args) {
		ParsedOption option = new ParsedOption();
		List<String> temp = new ArrayList<String>();
		for (String arg : args) {
			if (temp.size() > 0) {
				option.put((String) temp.remove(0), arg);
			} else if (isWithoutArgument(arg)) {
				option.put(arg, null);
			} else if (isFilterString(arg)) {
				temp.add(arg);
			} else if (hasFilterString(arg)) {
				String[] s = splitArgument(arg);
				option.put(s[0], s[1]);
			} else {
				option.put(arg);
			}
		}
		return option;
	}

	private String[] splitArgument(String arg) {
		for (String key : SeperateKey) {
			String[] s = arg.split(key, 2);
			if (this.filters.containsKey(s[0])) {
				return s;
			}
		}
		return null;
	}

	private boolean hasFilterString(String arg) {
		for (String key : SeperateKey) {
			String[] s = arg.split(key, 2);
			if (this.filters.containsKey(s[0])) {
				return true;
			}
		}
		return false;
	}

	private boolean isFilterString(String option) {
		return this.filters.containsKey(option);
	}

	private boolean isWithoutArgument(String option) {
		return (isFilterString(option))
				&& (((FilterMode) this.filters.get(option)).equals(FilterMode.WITHOUT_ARGUMENT));
	}

	public class ParsedOption {
		private final Map<String, String> optionMap = new HashMap<String, String>();
		private final List<String> single = new ArrayList<String>();

		private void put(String option, String argument) {
			this.optionMap.put(option, argument);
		}

		private void put(String arg) {
			this.single.add(arg);
		}

		public boolean isInteger(String option) {
			try {
				new Integer((String) this.optionMap.get(option));
				return true;
			} catch (NumberFormatException e) {
			}
			return false;
		}

		public boolean isLong(String option) {
			try {
				new Long((String) this.optionMap.get(option));
				return true;
			} catch (NumberFormatException e) {
			}
			return false;
		}

		public boolean isDouble(String option) {
			try {
				new Double((String) this.optionMap.get(option));
				return true;
			} catch (NumberFormatException e) {
			}
			return false;
		}

		public boolean isNumber(String option) {
			try {
				new BigDecimal((String) this.optionMap.get(option));
				return true;
			} catch (NumberFormatException e) {
			}
			return false;
		}

		public int getInteger(String option) {
			return new Integer((String) this.optionMap.get(option)).intValue();
		}

		public long getLong(String option) {
			return new Long((String) this.optionMap.get(option)).longValue();
		}

		public double getDouble(String option) {
			return new Double((String) this.optionMap.get(option)).doubleValue();
		}

		public BigDecimal getBigDecimal(String option) {
			return new BigDecimal((String) this.optionMap.get(option));
		}

		public boolean has(String option) {
			return this.optionMap.containsKey(option);
		}

		public boolean hasArgument(String option) {
			return this.optionMap.get(option) != null;
		}

		public String getString(String option) {
			return (String) this.optionMap.get(option);
		}

		public List<String> getSingleList() {
			return new ArrayList<String>(this.single);
		}
	}

	public static enum FilterMode {
		WITH_ARGUMENT, WITHOUT_ARGUMENT;
	}
}
