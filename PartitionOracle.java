// These are some imports that the course staff found useful, feel free to use them
// along with other imports from java.util.
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PartitionOracle {

	/**
	 * Feel free to use this method to call partition. It catches any exceptions or
	 * errors and returns a definitely-invalid pivot (-1) to turn errors into
	 * potential failures. For example, in testPartition, you may use
	 * 
	 * runPartition(p, someArray, someLow, someHigh)
	 * 
	 * instead of
	 * 
	 * p.partition(someArray, someLow, someHigh)
	 * 
	 * @param p 
	 * @param strs
	 * @param low
	 * @param high
	 * @return
	 */
	public static int runPartition(Partitioner p, String[] strs, int low, int high) {
		try {
			return p.partition(strs, low, high);
		} catch (Throwable t) {
			return -1;
		}
	}

	// The three methods below are for you to fill in according to the PA writeup.
	// Feel free to make other helper methods as needed.

	
	/*
	 * Return null if the pivot and after array reflect a correct partitioning of 
	 * the before array between low and high.
	 *
	 * Return a non-null String (your choice) describing why it isn't a valid
	 * partition if it is not a valid result. You might choose Strings like these,
	 * though there may be more you want to report:
	 *
	 * - "after array doesn't have same elements as before"
	 * - "Item before pivot too large"
	 * - "Item after pivot too small"
	 */
	public static String isValidPartitionResult(String[] before, int low, int high, int pivot, String[] after) {
		if (before.length != after.length) {
			return "Partition Failed: unequal before and after array lengths";
		} 
		if (!sameElementsInBothArrays(before, after)) {return "Partition Failed: all elements in before array aren't in after array";}
		String pivotChar = after[pivot];
		for (int i = 0; i < before.length; i++) { 
			String afterChar = after[i];
			if (i < low || i >= high) { 
				if(!before[i].equals(after[i])) {
					return "Partition Failed: a character out of the low-high range was changed";
				}
			}
			if (i >= low && i < pivot) {
				if ((afterChar.compareTo(pivotChar) > 0 )) {

					return "Partition Failed: there is a char that is greater than the pivot before the pivot index";
				}
			}
			if (i == pivot) {continue;}
			
			if (i > pivot && i < high) {
				if (afterChar.compareTo(pivotChar) < 0 ) {
					return "Partition Failed: there is a char after the pivot that is less than the pivot after the pivot index";
				}
			}
		}
		return null;
	}

	public static String[] generateInput(int n) {
		String[] toReturn = new String[n];
		for (int i = 0; i < n; i++) {
			String thisRandomChar = randomChar();
			toReturn[i] = thisRandomChar;
		}
		return toReturn;
	}

	public static CounterExample findCounterExample(Partitioner p) {
		Random r1 = new Random();
		int size = 100;
		for (int i = 0; i < 100; i++) {
			int highAt = r1.nextInt(99) + 1;
			int lowAt = r1.nextInt(highAt);
			String[] Input = generateInput(size);
			String[] origional = Arrays.copyOf(Input, Input.length);
			int pivotAt = runPartition(p, Input, lowAt, highAt);
			if (isValidPartitionResult(origional, lowAt, highAt, pivotAt, Input) != null) {
				String reason = isValidPartitionResult(origional, lowAt, highAt, pivotAt, Input);
				return new CounterExample(origional, lowAt, highAt, pivotAt, Input, reason);
			}
			
		}
		return null;
		
		
		
		
		
//		String[] origionalArr = generateInput(55);
//		String[] toChange = Arrays.copyOf(origionalArr, origionalArr.length);
//		int randLow = 12;
//		int randHigh = 18;
//		int idx = runPartition(p, toChange, randLow, randHigh);
//		String resultingIsValidPartitionResult = isValidPartitionResult(origionalArr, randLow, randHigh, idx, toChange);
//		if(resultingIsValidPartitionResult != null) {
//			CounterExample returned = new CounterExample(origionalArr, randLow, randHigh, idx, toChange, resultingIsValidPartitionResult);
//			return returned;
//		}
//		return null;
	}
	
	/**
	 * Helper method to generate a random lower case character and returns 
	 * a string with one char
	 * 
	 * @return a
	 */
	public static String randomChar() {
        Random random = new Random();
        char randCh = (char) (random.nextInt(26) + 'a');
        return String.valueOf(randCh);
    }
	
	/**
	 * Helper method to check that the same elements are in both arrays
	 * @param b
	 * @param a
	 * @return
	 */
	public static boolean sameElementsInBothArrays(String[] b, String[] a) {
		char[] beforeArray = new char[b.length];
		char[] afterArray = new char[a.length];
		for (int i = 0; i < b.length; i++) {
			beforeArray[i] = b[i].charAt(0);
			afterArray[i] = a[i].charAt(0);
		}
		Arrays.sort(beforeArray); Arrays.sort(afterArray);
		for (int i = 0; i < b.length; i++) {
			if (beforeArray[i] != afterArray[i]) {return false;}
		}
		return true; 
	}

	public static int generateRandomNumber(int rangeMax) {
		Random rand = new Random();
		int rand_int1 = rand.nextInt(rangeMax);
		return rand_int1;
	}
}

