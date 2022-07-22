import static org.junit.Assert.*;

import org.junit.Test;

/**
 * There are two ways to write partition methods to use for testing. One is to
 * make individual classes with a partition() method that implement Partitioner
 *
 */
class PartitionFromClass implements Partitioner {
	public void swap(String[] array, int i1, int i2) {
		String temp = array[i1];
		array[i1] = array[i2];
		array[i2] = temp;
	}
	public int partition(String[] array, int low, int high) {
		int pivotIndex = high - 1;
		String pivot = array[pivotIndex];
		int smallerBefore = low;
		int largerAfter = high - 2;
		while (smallerBefore < largerAfter) {
			if (array[smallerBefore].compareTo(pivot) < 0) {
				smallerBefore += 1;
			} else {
				swap(array, smallerBefore, largerAfter);
				largerAfter -= 1;
			}
		}
		if (array[smallerBefore].compareTo(pivot) < 0) {
			swap(array, smallerBefore + 1, pivotIndex);
			return smallerBefore + 1;
		} else {
			swap(array, smallerBefore, pivotIndex);
			return smallerBefore;
		}
	}
}

class CopyFirstElementPartition implements Partitioner {
	public int partition(String[] strs, int low, int high) {
		if (high - low < 1)
			return 0;
		for (int i = 0; i < strs.length; i += 1) {
			strs[i] = strs[0];
		}
		return 0;
	}
}

class OkayPartitionImplementation implements Partitioner {
	public void swap(String[] array, int i1, int i2) {
		String temp = array[i1];
		array[i1] = array[i2];
		array[i2] = temp;
	}
	public int partition(String[] array, int low, int high) {
		int pivotIndex = high - 1;
		String pivot = array[pivotIndex];
		int smallerBefore = 0;
		int largerAfter = array.length - 1;
		while (smallerBefore < largerAfter) {
			if (array[smallerBefore].compareTo(pivot) < 0) {
				smallerBefore += 1;
			} else {
				swap(array, smallerBefore, largerAfter);
				largerAfter -= 1;
			}
		}
		if (array[smallerBefore].compareTo(pivot) < 0) {
			swap(array, smallerBefore + 1, pivotIndex);
			return smallerBefore + 1;
		} else {
			swap(array, smallerBefore, pivotIndex);
			return smallerBefore;
		}
	}
}

class NoChangePartitionerImplementation implements Partitioner {
	public int partition(String[] array, int low, int high) {
		return 0;
	}
}

public class TestPartitionOracle {
	
	@Test
	public void testClassPartition() {
		CounterExample ce = PartitionOracle.findCounterExample(new PartitionFromClass());
		System.out.println(ce);
		assertNull(ce);
	}

	@Test
	public void testCopyFirstElementPartition() {
		CounterExample ce = PartitionOracle.findCounterExample(new CopyFirstElementPartition());
		System.out.println(ce);
		assertNotNull(ce);
	}
	
	@Test
	public void testGenerateInputSizeZero() {
		String[] testChars = PartitionOracle.generateInput(0);
		assertEquals(testChars.length, 0);
		for (int i = 0; i < testChars.length; i++) {
			System.out.println("this shouldn't print");
		}
	}
	
	@Test
	public void testGenerateInputMedArray() {
		String[] testChars = PartitionOracle.generateInput(10);
		assertEquals(testChars.length, 10);
//		for (int i = 0; i < testChars.length; i++) {
//			System.out.println(testChars[i]);
//		}
	}
	
	@Test
	public void testGenerateInputLargeArray() {
		String[] testChars = PartitionOracle.generateInput(25);
		assertEquals(testChars.length, 25);
//		for (int i = 0; i < testChars.length; i++) {
//			System.out.println(testChars[i]);
//		}
	}
	
	@Test
	public void testIsValidPartitionResultOrdered() {
		String[] beforeEx = {"a","b","c","d","e","f","g","h","i","j","k","l","m"};
		String[] afterEx = {"a","b","c","d","e","f","g","h","i","j","k","l","m"};
		String result = PartitionOracle.isValidPartitionResult(beforeEx, 3, 10, 7, afterEx);
		assertNull(result);
	}
	
	@Test
	public void testIsValidPartitionResultUnequalArrays() {
		String[] beforeEx = {"a","b","c","d","e","f","g","h","i","j","k","l"};
		String[] afterEx = {"a","b","c","d","e","f","g","h","i","j","k","l","m"};
		String result = PartitionOracle.isValidPartitionResult(beforeEx, 3, 10, 7, afterEx);
		assertEquals(result, "Partition Failed: unequal before and after array lengths");
	}
	
	@Test
	public void testIsValidPartitionResultDiffElems() {
		String[] beforeEx = {"a","b","c","d","e","f","g","h","i","j","k","l"};
		String[] afterEx = {"a","b","c","d","e","f","f","h","i","j","k","l"};
		String result = PartitionOracle.isValidPartitionResult(beforeEx, 3, 10, 7, afterEx);
		assertEquals(result, "Partition Failed: all elements in before array aren't in after array");
	}
	
	@Test
	public void testIsValidPartitionResultRange() {
		//testing off by one errors: j should not be moved 
		String[] beforeEx = {"l","k","j","i","h","g","f","e","d","c","b","a"};
		String[] afterEx = {"l","k","c","d","e","f","g","h","i","j","b","a"};
		String result = PartitionOracle.isValidPartitionResult(beforeEx, 3, 10, 7, afterEx);
		assertEquals(result, "Partition Failed: a character out of the low-high range was changed");
	}
	
	@Test
	public void testIsValidPartitionResultOffByOne() {
		//testing off by one errors: b should not be moved 
		String[] beforeEx = {"l","k","j","i","h","g","f","e","d","c","b","a"};
		String[] afterEx = {"l","k","j","b","c","d","e","f","g","h","i","a"};
		String result = PartitionOracle.isValidPartitionResult(beforeEx, 3, 10, 7, afterEx);
		assertEquals(result, "Partition Failed: a character out of the low-high range was changed");
	}
	
	@Test
	public void testIsValidPartitionResultDuplicatePivot() {
		String[] beforeEx = {"l","k","j","i","e","g","f","e","d","c","b","a"};
		String[] afterEx =  {"l","k","j","c","d","e","e","f","g","i","b","a"};
		String result = PartitionOracle.isValidPartitionResult(beforeEx, 3, 10, 7, afterEx);
		assertNull(result);
	}
	
//	@Test
//	public void testIsValidPartitionResultPivots() {
//		// testing duplicate pivots in diff orders
//		String[] beforeEx = {"l","k","j","i","e","g","f","e","d","c","b","a"};
//		String[] afterEx = {"l","k","j","e","c","d","e","f","g","i","b","a"};
//		String result = PartitionOracle.isValidPartitionResult(beforeEx, 3, 10, 7, afterEx);
//		assertNotNull(result);
//	}
	
//	@Test
//	public void testIsValidPartitionResultWrongSmallCh() {
//		String[] beforeEx = {"l","k","j","i","h","g","f","e","d","c","b","a"};
//		String[] afterEx = {"l","k","j","c","d","f","e","i","h","g","b","a"};
//		String result = PartitionOracle.isValidPartitionResult(beforeEx, 3, 10, 7, afterEx);
//		assertEquals(result, "Partition Failed: there is a char that is greater than the pivot before the pivot index");
//	}
	
	@Test
	public void testIsValidPartitionResultWrongBigCh() {
		String[] beforeEx = {"l","k","j","i","h","g","f","e","d","c","b","a"};
		String[] afterEx = {"l","k","j","c","e","d","f","i","h","g","b","a"};
		String result = PartitionOracle.isValidPartitionResult(beforeEx, 3, 10, 7, afterEx);
		assertEquals(result, "Partition Failed: there is a char after the pivot that is less than the pivot after the pivot index");
	}
	
	@Test
	public void testGenerateLength() {
		String[] result = PartitionOracle.generateInput(15);
		assertEquals(result.length, 15);
	}
	
	@Test
	public void testIsInvalidNotPartitionedSmallerRight() {
		String[] beforeEx = {"l","k","j","b","i","g","f","e","d","c","a"};
		String[] afterEx =  {"l","k","j","b","c","e","f","g","i","d","a"};
		String result = PartitionOracle.isValidPartitionResult(beforeEx, 3, 10, 7, afterEx);
		System.out.println(result);
		assertEquals(result, "Partition Failed: there is a char after the pivot that is less than the pivot after the pivot index");
	}
	
	@Test
	public void testNoChangePartitionerImplementation() {
		// This test usually passes but bc "findCounterExample" makes a random array of characters, it does occasionally pass
		CounterExample ce = PartitionOracle.findCounterExample(new NoChangePartitionerImplementation());
		System.out.println(ce);
		assertNotNull(ce);
	}
	
	@Test
	public void testOkayPartitionImplementation() {
		CounterExample ce = PartitionOracle.findCounterExample(new OkayPartitionImplementation());
		assertNotNull(ce);
	}
}
