package converter;

import java.math.RoundingMode;
import java.util.Scanner;
import java.math.BigDecimal;

public class Main {

    public static BigDecimal toDecimal(String source, int target) {

        String str = "0123456789abcdefghijklmnopqrstuvwxyz";

        // Creating array of string length
        char[] hex = new char[str.length()];

        // Copy character by character into array
        for (int i = 0; i < str.length(); i++) {
            hex[i] = str.charAt(i);
        }

        int radixLoc = source.indexOf(".");
        String integralPart = "";
        String fractionPart = "";
        if (radixLoc != -1) {
            integralPart = source.substring(0, radixLoc);
            fractionPart = source.substring(radixLoc + 1, source.length());
        } else {
            integralPart = source;
        }

        BigDecimal result5;
        result5 = BigDecimal.ZERO;
        BigDecimal baseX = new BigDecimal(target);
        BigDecimal decPow = BigDecimal.ZERO;
        int xx = 1;
        for (int x = 0; x < fractionPart.length(); x++) {
            decPow = BigDecimal.ONE.divide(baseX.pow(xx), 9, RoundingMode.HALF_UP);
            // System.out.printf("%d     %35.33f%n", x, decPow);
            int idx2 = str.indexOf(fractionPart.charAt(x));
            BigDecimal result6 = decPow.multiply(BigDecimal.valueOf(idx2));
            result5 = result5.add(result6);
            xx++;
        }


        BigDecimal result;
        result = BigDecimal.ZERO;

        BigDecimal base = BigDecimal.valueOf(target);
        int len = integralPart.length();
        BigDecimal[] value = new BigDecimal[len];
        int y = 0;
        for (int x = len - 1; x > -1; x--) {
            value[y] = base.pow(x);
            // System.out.printf("%f %f %n", value[y], x);
            y++;
        }

        // calculate the decimal number for this base
        for (int x = 0; x < len; x++) {
            int idx = str.indexOf(integralPart.charAt(x));
            BigDecimal result7 = value[x].multiply(BigDecimal.valueOf(idx));
            result = result.add(result7);
        }
        if (fractionPart.length() == 0) {
            result = result.setScale(0);
        } else {
            result = result.add(result5);
        }
        return result;
    }

    public static String toAnyBase(BigDecimal number, int target) {

        String source = number.toString();
        int radixLoc = source.indexOf(".");
        String integralPart = "";
        String fractionPart = "";
        if (radixLoc != -1) {
            integralPart = source.substring(0, radixLoc);
            fractionPart = source.substring(radixLoc + 1, source.length());
        } else {
            integralPart = source;
        }
        BigDecimal fraction = number.remainder(BigDecimal.ONE);

        String result = "";
        String str = "0123456789abcdefghijklmnopqrstuvwxyz";

        // Creating array of string length
        char[] hex = new char[str.length()];

        // Copy character by character into array
        for (int i = 0; i < str.length(); i++) {
            hex[i] = str.charAt(i);
        }

        BigDecimal quotient = new BigDecimal(integralPart);
        BigDecimal target2 = BigDecimal.valueOf(target);

        while (quotient.compareTo(target2) >= 0) {
            BigDecimal bi[] = quotient.divideAndRemainder(target2);
            String sTarget = bi[1].toString();
            int iTarget = Integer.parseInt(sTarget);
            result += String.valueOf(hex[iTarget]);
            quotient = bi[0];
        }

        String sQuotient = quotient.toString();
        int iQuotient = Integer.parseInt(sQuotient);
        if (iQuotient > 0) {
            result += String.valueOf(hex[iQuotient]);
        }
        int size = result.length();
        String answer = "";
        for (int x = size - 1; x > -1; x--) {
            answer += result.charAt(x);
        }

        boolean domore = true;
        BigDecimal  multResult = BigDecimal.ZERO;
        BigDecimal  fraction2 = fraction;
        String fractStr = "";

        int loopCount = 0;
        while (domore) {
            multResult = fraction2.multiply(BigDecimal.valueOf(target));
            BigDecimal bd2[] = multResult.divideAndRemainder(BigDecimal.ONE);
            int whole = bd2[0].intValue();
            fractStr += String.valueOf(hex[whole]);
            fraction2 = bd2[1];
            loopCount++;
            if (loopCount == 5) {
                domore = false;
            }
        }

        if (fractionPart.length() == 0) {
            return answer;
        } else if (integralPart.length() == 0 && fractionPart.length() != 0 ) {
            return ("0." + fractStr);
        } else {
            return (answer + "." + fractStr);
        }
    }

    public static void main(String[] args) {
        // write your code here
        Scanner scanner = new Scanner(System.in);
        boolean more = true;

        while (more) {
            System.out.print("Enter two numbers in format: {source base} {target base} (To quit type /exit) > ");
            if (scanner.hasNext("/exit")) {
                more = false;
            } else {
                int source = scanner.nextInt();
                int target = scanner.nextInt();
                BigDecimal convertThis3 = BigDecimal.ZERO;
                boolean more2 = true;
                // BigInteger convertThis3;
                while (more2) {
                    System.out.printf("Enter the number in base %d to convert to base %d (To go back type /back) > ", source, target);
                    if (scanner.hasNext("/back")) {
                        more2 = false;
                        String discard = scanner.next();
                    } else {
                        if (source == 10 && target != 10) {
                            if (scanner.hasNextBigDecimal()) {
                                convertThis3 = scanner.nextBigDecimal();
                            } else {
                                String inputStuff = scanner.next();
                                System.out.println("Not a BigDecimal number! It's this : " + inputStuff);
                            }
                            String answer4 = toAnyBase(convertThis3, target);
                            System.out.println("Conversion result: " + answer4);
                        } else if (source != 10 && target == 10) {
                            String convertThis = scanner.next().toLowerCase();
                            BigDecimal answer = toDecimal(convertThis, source);
                            BigDecimal bd7[] = answer.divideAndRemainder(BigDecimal.ONE);
                            if (bd7[1] == BigDecimal.ZERO) {
                                System.out.println("Conversion result: " + answer.setScale(0));
                            } else {
                                System.out.println("Conversion result: " + answer.setScale(5, RoundingMode.HALF_DOWN));
                            }
                        } else if (source != 10 && target != 10) {
                            String convertThis2 = scanner.next().toLowerCase();
                            BigDecimal answer3 = toDecimal(convertThis2, source);
                            String answer4 = toAnyBase(answer3, target);
                            System.out.println("Conversion result: " + answer4);
                        }
                    }
                }
            }
        }
    }
}