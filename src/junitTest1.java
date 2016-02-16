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
	
	private void populateListNumber(int numberOfItems){
		for (int i = 0; i < numberOfItems; i++) {
			expectedList.add(String.valueOf(i+1));
		}
	}
	
	private void populateListAlphabet(int numberOfItems){
		for (int i = 0; i < numberOfItems; i++) {
			String toAdd = ((char)(i+65)) + "";
			expectedList.add(toAdd);
		}
	}
	
	@Test
	public void testAdd() throws IOException {
		TextBuddy buddy = new TextBuddy();
		expectedList.clear();
		populateListNumber(1);
		assertEquals(String.format(ADD_MESSAGE, buddy.showPath(), "1"), buddy.executeCommand("add 1"));
		assertEquals(expectedList,buddy.texts);
	}
	
	@Test
	public void testDelete() throws IOException {
		TextBuddy buddy = new TextBuddy();
		expectedList.clear();
		populateListNumber(3);
		expectedList.remove(1);
		buddy.executeCommand("add 1");
		buddy.executeCommand("add 2");
		buddy.executeCommand("add 3");
	    assertEquals(String.format(DELETE_MESSAGE,buddy.showPath(),"2"), buddy.executeCommand("delete 2"));
		assertEquals(expectedList,buddy.texts);
	}
	
	@Test
	public void testDeleteEmptyList() throws IOException {
		TextBuddy buddy = new TextBuddy();
		expectedList.clear();
		populateListNumber(0);
		assertEquals(DELETE_EMPTY, buddy.executeCommand("delete 4"));
		assertEquals(expectedList, buddy.texts);
	}
	
	@Test
	public void testClear() throws IOException {
		TextBuddy buddy = new TextBuddy();
		expectedList.clear();
		buddy.executeCommand("add 1");
		buddy.executeCommand("add 2");
		buddy.executeCommand("add 3");
		assertEquals(String.format(CLEAR_MESSAGE,buddy.showPath()), buddy.executeCommand("clear"));
		assertEquals(expectedList, buddy.texts);
	}
	

	
	@Test
	public void testSort() throws IOException {
		TextBuddy buddy = new TextBuddy();
		expectedList.clear();
		populateListAlphabet(5);
		buddy.executeCommand("add A");
		buddy.executeCommand("add B");
		buddy.executeCommand("add C");
		buddy.executeCommand("add D");
		buddy.executeCommand("add E");
		//assertEquals(MESSAGE_TASKS_SORTED, obj.performCommand("sort"));
		assertEquals(expectedList, buddy.texts);
	}
	
	@Test
	public void testSearch() throws IOException {
		TextBuddy buddy = new TextBuddy();
		expectedList.clear();
		expectedList.add("i have a kitten");
		expectedList.add("name is kitten");
		buddy.executeCommand("i have a kitten");
		buddy.executeCommand("name is kitten");
		assertEquals(expectedList, buddy.tempList);
	}

}
