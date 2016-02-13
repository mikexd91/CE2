import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * This class can be used to add, delete, clear, 
 * display lines of text entered by user by using 
 * the above mentioned user command. output is stored 
 * in a file defined by user at the start of the program 
 * as an argument
 * 
 * "Exit" to quit the program

c:> TextBuddy.exe mytextfile.txt
(OR c:> java TextBuddy mytextfile.txt)
WElCOME TO TEXTBUDDY! mytextfile.txt is ready for use
Command: add little brown fox
Added to mytextfile.txt: "little brown fox"
Command: display
1. little brown fox
Command: add jumped over the moon
Added to mytextfile.txt: "jumped over the moon"
Command: display
1. little brown fox
2. jumped over the moon
Command: delete 2
Deleted from mytextfile.txt: "jumped over the moon"
Command: display
1. little brown fox
Command: clear
All content deleted from mytextfile.txt
Command: display
mytextfile.txt is empty
Command: exit

 */
public class TextBuddy{ 
	
	private static File file;
	private static Scanner sc;
	public ArrayList<String> texts;
	
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
    
	//This is to indicate there is no existing index in the arraylist
	private static final int INVALID_INDEX = -1;
	
	//These are the possible command types user can manipulate
	enum COMMAND_INPUT{
		ADD,DISPLAY,DELETE,CLEAR,EXIT,INVALID,SORT,SEARCH
	};
	
	private Path path;
	
	public TextBuddy(){
		path = Paths.get("testfile.txt");
		texts = new ArrayList<String>();
	}
	public TextBuddy(Path _path){
		path = _path;
		texts = new ArrayList<String>();
	}

	public static void main(String[] args) throws IOException{
		exitIfIncorrectArguments(args);
		TextBuddy buddy = new TextBuddy(Paths.get(args[0]));
		buddy.setEnvironment();
		buddy.printWelcomeMessage();
		buddy.executeCommandsUntilExitCommand();
	}
	
	/**
	 * This operation is used to see if argument input by user is valid
	 * 
	 * @param args
	 *            contains file name
	 */
	private static void exitIfIncorrectArguments(String[] args) throws IOException{
		exitIfNoArguments(args);
		checkFileName(args[0]);
	}
	
	/**
	 * This operation is used to see if user input any argument
	 * 
	 * @param args
	 *            contains file name
	 */
	private static void exitIfNoArguments(String[] args){
		if(args.length == 0){
			ErrorMessageAndExit("Please provide a filename");
		}
	}
	
	/**
	 * This operation is used to see if file is already created if not create it
	 * 
	 * @param args
	 *            contains file name
	 */
	private static void checkFileName(String fileName)throws IOException{
		File f = new File(fileName);
		if(!f.exists()){
			try{
				f.createNewFile();
		    }catch(IOException e){
			    ErrorMessageAndExit("Cannot create file");
		    }
		}
	}
	
	private static void ErrorMessageAndExit(String message){
		System.out.println(message);
		System.exit(0);
	}
	
	private void setEnvironment(){
		setUpForInput();
		readFromFile(file);
	}
	
	private void setUpForInput(){
		texts = new ArrayList<String>();
		file = new File(path.toString());
		sc = new Scanner(System.in);
	}
	
	private void readFromFile(File file){
		try{
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String line;
			
			while(br.readLine()!=null){
				line = br.readLine();
				texts.add(line);
			}
		}catch(IOException e){
			ErrorMessageAndExit("Error reading from file");
		}
	}
	
	private void printWelcomeMessage(){
		outputMessage(WELCOME,path.toString());
	}
	
	private void executeCommandsUntilExitCommand() throws IOException{
	
		   while (true) {
		 		outputMessage(NEXT_COMMAND_MESSAGE);
		 		String userCommand = sc.nextLine().trim();
		 		executeCommand(userCommand);
		 		writeToFile();
		 	}
	}
	
	/**
	 * This operation is extract the first word from userCommand
	 * 
	 * @param args
	 *            contains userCommand
	 * @return
	 *            command keyword/first word
	 */
	private static String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}
	
	//This method convert the userCommand to lowercase
	private static String getLowerCase(String userCommand){
		return userCommand.toLowerCase();
	}
	
	public void executeCommand(String userCommand) throws IOException{

		String commandTypeString = getFirstWord(userCommand);
        String lowerCaseCommand = getLowerCase(commandTypeString);
		COMMAND_INPUT commandType = determineCommandType(lowerCaseCommand);

		switch (commandType) {
		case ADD:
			addMessage(userCommand);
			break;
		case DISPLAY:
			displayMessage();
			break;
		case DELETE:
			deleteMessage(userCommand);
			break;
		case CLEAR:
			clearTexts();
			break;
		case INVALID:
			return;
		case SORT:
			sortMessage();
			break;
		case SEARCH:
			searchMessage(userCommand);
			break;
		case EXIT:
			System.exit(0);
		default:
			//throw an error if the command is not recognized
			throw new Error("Unrecognized command type");
		}
		
	}
	
	private static COMMAND_INPUT determineCommandType(String commandTypeString) {
		if (commandTypeString == null)
			throw new Error("Command type string cannot be null!");

		if (commandTypeString.equals("add")) {
			return COMMAND_INPUT.ADD;
		} else if (commandTypeString.equals("display")) {
			return COMMAND_INPUT.DISPLAY;
		} else if (commandTypeString.equals("delete")) {
		 	return COMMAND_INPUT.DELETE;
		} else if (commandTypeString.equals("clear")) {
			return COMMAND_INPUT.CLEAR;
		} else if (commandTypeString.equals("exit")) {
		 	return COMMAND_INPUT.EXIT;
		} else if (commandTypeString.equals("sort")) {
		 	return COMMAND_INPUT.SORT;
		} else if (commandTypeString.equals("search")) {
		 	return COMMAND_INPUT.SEARCH;
		} else {
			return COMMAND_INPUT.INVALID;
		}
	}
	private static String removeCommandKeyword(String line){
		int spaceIndex = line.indexOf(" ");
	    line.substring(0,spaceIndex);
		return line.substring(spaceIndex+1);
	}
	
	private void addMessage(String line){
		if(line == null)
			return;	
		String commandWithoutKeyword = removeCommandKeyword(line);
		texts.add(commandWithoutKeyword);
		outputMessage(ADD_MESSAGE,path.toString(),commandWithoutKeyword);
	}
	
	private void displayMessage(){
		checkForEmptyFile();
		for(int i = 0; i < texts.size(); i++){
			outputMessage(DISPLAY_MESSAGE, i+1, texts.get(i));
		}
	}
	private void displayMessage(ArrayList<String> list){
		checkForEmptyFile();
		for(int i = 0; i < list.size(); i++){
			outputMessage(DISPLAY_MESSAGE, i+1, list.get(i));
		}
	}
	
	private void sortMessage(){
		Collections.sort(texts,String.CASE_INSENSITIVE_ORDER);
		displayMessage();
	}
	
	private void searchMessage(String word){
		String commandWithoutKeyword = removeCommandKeyword(word);
		ArrayList<String> tempList = new ArrayList<String>();
		for(String line :texts){
		    if(line.contains(commandWithoutKeyword))	{
		    	tempList.add(line);
				displayMessage(tempList);
		    }
		}
	}
	

	private void checkForEmptyFile() {
		if(texts.size()== 0){
	        outputMessage(EMPTY_FILE_MESSAGE,file.getName());
		}
	}
	
	private void deleteMessage(String userInput){
		String commandWithoutKeyword = removeCommandKeyword(userInput);
		if(texts.size() == 0){
			outputMessage(DELETE_EMPTY);
			return;
		}
		int index = getIndex(commandWithoutKeyword);
		removeTextAtIndex(index);
	}
	
	private static int getIndex(String line){
		try{
			return Integer.parseInt(line)-1;
		}catch(NumberFormatException e){
			outputMessage(PARAMETER_NOT_NUMBER,line);
		}
		return INVALID_INDEX;
	}
	
	private void removeTextAtIndex(int index){
		if(index == INVALID_INDEX){
			return;
		}
		try{
			String content = texts.get(index);
			texts.remove(index);
			outputMessage(DELETE_MESSAGE,file.getName(),content);
		}catch(IndexOutOfBoundsException e){
			outputMessage(INVALID_INDEX_MESSAGE,index,1,texts.size());
		}
	}
	
	private void clearTexts(){
		texts.clear();
		outputMessage(CLEAR_MESSAGE,file.getName());
	}
	
	private void writeToFile() throws IOException{
		try{
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			for(String line: texts){
				bw.write(line);
				bw.newLine();
			}
			bw.close();
		}catch(FileNotFoundException e){
		}
	}
	
	private static void outputMessage(String message, Object... args){
		System.out.println(String.format(message, args));
	}
}
   
