import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

public class junitTest1 {
	
	private static final String BAD_ARGUMENTS = "BAD ARGUMENT";
	private static final String WELCOME = "WELCOME TO TEXTBUDDY! %s is ready for use.";
	private static final String ADD_MESSAGE = "Added to %s: \"%s\"";
	private static final String DELETE_MESSAGE = "Deleted from %s: \"%s\""; 
	private static final String CLEAR_MESSAGE = "All content deleted from %s";
	private static final String EMPTY_FILE_MESSAGE = "%s is empty";
	private static final String DISPLAY_MESSAGE = "%d. %s";
	private static final String NEXT_COMMAND_MESSAGE = "Command: ";
	private static final String PARAMETER_NOT_NUMBER = "\"%s\" is not a number";
	private static final String INVALID_INDEX_MESSAGE = "%d is an invalid index";
	private static final String DELETE_EMPTY = "No content to delete";

	ArrayList<String> expectedList;
	
	public junitTest1(){
		expectedList = new ArrayList<String>();
	}
	
	private void populateList(int numberOfItems){
		for (int i = 0; i < numberOfItems; i++) {
			expectedList.add(String.valueOf(i+1));
		}
	}
	@Test
	public void test() {
		TextBuddy buddy = new TextBuddy();
		expectedList.clear();
		populateList(1);
		try {
			buddy.executeCommand("add 1");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(expectedList,buddy.texts);
	}

}
