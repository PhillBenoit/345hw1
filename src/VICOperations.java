public class VICOperations {

	public static String noCarryAddition (String x, String y) {
		if (x.length() > y.length()) y = padZeroes(y, x.length());
		else if (y.length() > x.length()) x = padZeroes(x, y.length());

		int step = x.length();
		char[] returnString = new char[step--]; 
		for (; step > -1; step--)
			returnString[step] = (char)(((
					getInt(x, step) + getInt(y, step))%10)+'0');
		return String.copyValueOf(returnString);
	}

	public static String chainAddition (String s, int newLength) {
		int step = s.length();
		s=padZeroes(s, 0 - newLength);
		char[] returnString = s.toCharArray();
		if (step == 1) {
			returnString[1] = returnString[0];
			step = 2;
		}
		int original = step;
		for (; step < newLength; step++) returnString[step] = noCarryAddition(
				String.valueOf(returnString[step-original]), String.valueOf(
						returnString[step-original+1])).charAt(0);
		return String.copyValueOf(returnString);
	}

	private static String padZeroes(String s, int newLength) {
		s = String.format("%1$" + newLength + "s", s);
		return s.replace(' ', '0');
	}

	private static int getInt(String s, int location) {
		return Integer.parseInt(String.valueOf(s.charAt(location)));
	}

}
