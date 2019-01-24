public class VICOperations {
    
    public static String noCarryAddition (String x, String y) {
        Integer.parseInt(x);
        Integer.parseInt(y);
        String returnString = "";
        int max = Math.max(x.length(), y.length()); 
        for (int step = 1; step <= max; step++) {
            Integer total = (getInt(x, step) + getInt(y, step))%10;
            returnString = total.toString() + returnString;
        }
        return returnString;
    }
    
    private static int getInt(String s, int location) {
        location = s.length() - location;
        if (location < 0) return 0;
        else return Integer.parseInt(String.valueOf(s.charAt(location)));
    }

}
