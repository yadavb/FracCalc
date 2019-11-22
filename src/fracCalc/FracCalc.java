package fracCalc;

import java.util.*;

public class FracCalc {

	public static void main(String[] args) {
		// TODO: Read the input from the user and call produceAnswer with an equation
		Scanner scanner = new Scanner(System.in);

		// String request = scanner.nextLine();
		System.out.println(produceAnswer("1/2 23 4 5/2 * - +"));
		System.out.println(produceAnswer("1/2 * 23"));

		scanner.close();

	}

	// ** IMPORTANT ** DO NOT DELETE THIS FUNCTION. This function will be used to
	// test your code
	// This function takes a String 'input' and produces the result
	//
	// input is a fraction string that needs to be evaluated. For your program, this
	// will be the user input.
	// e.g. input ==> "1/2 + 3/4"
	//
	// The function should return the result of the fraction after it has been
	// calculated
	// e.g. return ==> "1_1/4"
	public static String produceAnswer(String input) {

		int firstSpaceIndex = input.indexOf(' ');
		String firstOperand = input.substring(0, firstSpaceIndex);
		System.out.printf("Input: %s\n", input);
		
		// parse the input into tokens
		int secondSpaceIndex = input.indexOf(' ', firstSpaceIndex + 1);
		String operator = input.substring(firstSpaceIndex+1, secondSpaceIndex);
		String secondOperand = input.substring(secondSpaceIndex + 1);
		if (secondOperand.indexOf(' ') != -1) {
			input = secondOperand.substring(secondOperand.indexOf(' ') + 1);
			secondOperand = secondOperand.substring(0, secondOperand.indexOf(' '));
		}
		else {
			input = "";
		}
		
		firstOperand = computeFraction(firstOperand, operator, secondOperand);
		// continue until the input string is exhausted
		while (!input.isBlank()) {
			firstSpaceIndex = input.indexOf(' ');
			operator = input.substring(0, firstSpaceIndex);
			secondOperand = input.substring(firstSpaceIndex + 1);
			if (secondOperand.indexOf(' ') != -1) {
				input = secondOperand.substring(secondOperand.indexOf(' ') + 1);
				secondOperand = secondOperand.substring(0, secondOperand.indexOf(' '));			}
			else {
				input = "";
			}
			System.out.printf("%s %s %s\n", firstOperand, operator, secondOperand);
			firstOperand = computeFraction(firstOperand, operator, secondOperand);
		}
		
		return firstOperand;
	}

	private static String computeFraction(String firstOperand, String operator, String secondOperand) {
		String result = new String();

		int firstWhole = getWholeNumber(firstOperand);
		int firstNumerator = getNumerator(firstOperand);
		int firstDenominator = getDenominator(firstOperand);

		int secondWhole = getWholeNumber(secondOperand);
		int secondNumerator = getNumerator(secondOperand);
		int secondDenominator = getDenominator(secondOperand);

		switch (operator) {
		case "+":
			result = addOperands(firstWhole, firstNumerator, firstDenominator, secondWhole, secondNumerator,
					secondDenominator);
			break;
		case "-":
			result = subtractOperands(firstWhole, firstNumerator, firstDenominator, secondWhole, secondNumerator,
					secondDenominator);
			break;
		case "*":
			result = multiplyOperands(firstWhole, firstNumerator, firstDenominator, secondWhole, secondNumerator,
					secondDenominator);
			break;
		case "/":
			result = divideOperands(firstWhole, firstNumerator, firstDenominator, secondWhole, secondNumerator,
					secondDenominator);
			break;
		default:
			return "Not a valid operator";
		}

		return result;
	}

	private static String divideOperands(int firstWhole, int firstNumerator, int firstDenominator, int secondWhole,
			int secondNumerator, int secondDenominator) {
		int firstSign = firstWhole >= 0 ? 1 : -1;
		int secondSign = secondWhole >= 0 ? 1 : -1;

		firstNumerator += firstWhole * firstDenominator * firstSign;
		secondNumerator += secondWhole * secondDenominator * secondSign;

		int num = firstNumerator * secondDenominator * firstSign * secondSign;
		int denom = firstDenominator * secondNumerator;
		return reduce(num, denom);
	}

	private static String reduce(int num, int denom) {
		String result = new String();
		int whole = num / denom;
		num %= denom;

		if (whole != 0 && num < 0) {
			num *= -1;
		}

		if (whole != 0 && denom < 0) {
			denom *= -1;
		}

		int gcf = findGcf(num, denom);

		num /= gcf;
		denom /= gcf;

		System.out.printf("Whole: %d, GCF: %d, num: %d, denom: %d\n", whole, gcf, num, denom);

		if (whole != 0) {
			result += whole + "";
		}

		if (num != 0) {
			if (whole != 0) {
				result += "_";
			}
			result += num + "/" + denom;
		}

		if (result.isBlank()) {
			return "0";
		}

		return result;
	}

	private static int findGcf(int num, int denom) {
		if (num == 0)
			return denom;

		return findGcf(denom % num, num);
	}

	private static String multiplyOperands(int firstWhole, int firstNumerator, int firstDenominator, int secondWhole,
			int secondNumerator, int secondDenominator) {
		int firstSign = firstWhole >= 0 ? 1 : -1;
		int secondSign = secondWhole >= 0 ? 1 : -1;

		System.out.printf("whole1: %d, whole2: %d, num1: %d, num2: %d, denom1: %d, denom2: %d\n", firstWhole,
				secondWhole, firstNumerator, secondNumerator, firstDenominator, secondDenominator);

		firstNumerator += firstWhole * firstDenominator * firstSign;
		secondNumerator += secondWhole * secondDenominator * secondSign;

		int num = firstNumerator * secondNumerator * firstSign * secondSign;
		int denom = firstDenominator * secondDenominator;
		System.out.printf("num: %d, denom: %d\n", num, denom);

		return reduce(num, denom);
	}

	private static String subtractOperands(int firstWhole, int firstNumerator, int firstDenominator, int secondWhole,
			int secondNumerator, int secondDenominator) {
		int firstSign = firstWhole >= 0 ? 1 : -1;
		int secondSign = secondWhole >= 0 ? 1 : -1;

		firstNumerator += firstWhole * firstDenominator * firstSign;
		secondNumerator += secondWhole * secondDenominator * secondSign;

		int num = firstNumerator * secondDenominator * firstSign - secondNumerator * firstDenominator * secondSign;
		int denom = firstDenominator * secondDenominator;

		return reduce(num, denom);
	}

	private static String addOperands(int firstWhole, int firstNumerator, int firstDenominator, int secondWhole,
			int secondNumerator, int secondDenominator) {
		int firstSign = firstWhole >= 0 ? 1 : -1;
		int secondSign = secondWhole >= 0 ? 1 : -1;

		firstNumerator += firstWhole * firstDenominator * firstSign;
		secondNumerator += secondWhole * secondDenominator * secondSign;

		int num = firstNumerator * secondDenominator * firstSign + secondNumerator * firstDenominator * secondSign;
		int denom = firstDenominator * secondDenominator;

		return reduce(num, denom);
	}

	private static int getDenominator(String operand) {
		int denominator = 1;
		int indexOfSlash = operand.indexOf('/');
		if (indexOfSlash != -1) {
			String denom = operand.substring(indexOfSlash + 1);
			denominator = Integer.parseInt(denom);
		}

		return denominator;
	}

	private static int getNumerator(String operand) {
		int numerator = 0;
		int indexOfSlash = operand.indexOf('/');
		if (indexOfSlash != -1) {
			int indexOf_ = operand.indexOf('_');
			if (indexOf_ != -1) {
				numerator = Integer.parseInt(operand.substring(indexOf_ + 1, indexOfSlash));
			} else {
				numerator = Integer.parseInt(operand.substring(0, indexOfSlash));
			}
		}

		return numerator;
	}

	private static int getWholeNumber(String operand) {
		int wholeNumber = 0;
		int indexOf_ = operand.indexOf('_');

		if (indexOf_ != -1) {
			wholeNumber = Integer.parseInt(operand.substring(0, indexOf_));
		} else if (operand.indexOf('/') == -1) {
			// if no fraction part exists
			wholeNumber = Integer.parseInt(operand);
		}

		return wholeNumber;
	}

	// TODO: Fill in the space below with any helper methods that you think you will
	// need

}
