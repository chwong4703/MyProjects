// ***************************************************************************
// VirtualMachine.java
// Author: CheukHo Wong
// Class: CSCI 4200
// Professor: Dr.Salimi
// Date: Dec 6, 2017
// ***************************************************************************
package vmPackage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VirtualMachine {
	private static int MAX_MEMORY_SIZE = 500;
	private static String[] Instructions = new String[MAX_MEMORY_SIZE];
	private static int iCounter = 0;		// count number of instructions
	private static int programCounter;
	private static int sum = -1;
	private static int num = -1;
	private static List<Label> LabelList= new ArrayList<Label>();
	public static void main(String[] args) {
		new VirtualMachine();
	}
	
	// ***************************************************************************
	// please Initialize all values here
	private static int increment;
	private static int number;
	private static int tempNum;
	// ***************************************************************************
	
	public VirtualMachine() {
		// TODO Auto-generated method stub
		try (BufferedReader br = new BufferedReader(new FileReader("src\\vmPackage\\mySubLC3.txt"))) {
			// get each line in mySubLC3.text and save them into "Instructions" array
		    String line;
			for(int i = 0; (line = br.readLine()) != null; i++) {
				String firstLetter = String.valueOf(line.charAt(0));
				// check if the file reaches the end of txt
				if(line.equals("HALT")) {
					break;
				}
				// check if the first letter is ";" 
				else if(!firstLetter.equals(";")) {
					// add valid instruction to Instruction list
					Instructions[iCounter] = line;
					// count number of instructions
					iCounter++;
				}
			}
			
			// find out all of the label location first
			for(int location = 0; location < iCounter; location++) {
				String instruction = Instructions[location];
		    	// split the instruction with while space
		    	String[] splitStr = instruction.split("\\s+");
		    	// if there is one word in the instruction, that instruction can be considered as Label
		    	if(splitStr.length == 1) {
			    	String label = splitStr[0];
		    		setLabel(label, location);
		    	}
			}
					
			// retrieve each value inside "Instructions" array
			for(programCounter = 0; programCounter < iCounter; programCounter++) {
				String instruction = Instructions[programCounter];
		    	// split the instruction with while space
		    	// save each word in the string into an array
		    	String[] splitStr = instruction.split("\\s+");
		    	// execute the operation
		    	String operation = splitStr[0];
		    	switch(operation) {
		    		case "ADD": ADD(splitStr); break;
		    		case "SUB": SUB(splitStr); break;
		    		case "MUL": MUL(splitStr); break;
		    		case "DIV": DIV(splitStr); break;
		    		case "IN": IN(splitStr[1]); break;
		    		case "OUT": OUT(splitStr); break;
		    		case "STO": STO(splitStr); break;
		    		case "BRn": BRn(splitStr); break;
		    		case "BRz": BRz(splitStr); break;
		    		case "BRp": BRp(splitStr); break;
		    		case "BRzp": BRzp(splitStr); break;
		    		case "BRzn": BRzp(splitStr); break;
		    		case "JMP": JMP(splitStr[1]); break;
		    	}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// ADD function
	// ADD accepts two integer values, Source1 and Source2, and stores their sum in the Destination variable, Destination.
	// Each Source can be either a variable or an integer constant.
	public void ADD(String[] instruction) {
		// declare value for destination, source 1, source 2 from instruction
		String destination = instruction[1];
		String source1 = instruction[2];
		String source2 = instruction[3];
		// convert String source1, source 2 to Integer source1Int, source2Int and calculate the answer
		int source1Int = 0, source2Int = 0, answer = 0;
		// Set up a variarblePattern allows program distinguish if it is a variable or value
		String variarblePattern = "^[A-Za-z][A-Za-z0-9]+";
		// If it is just a normal numeric value, convert it from string to integer
		// Otherwise, directly get value from that specific variable
		if(source1.matches(variarblePattern)) 
			source1Int = getIntField(source1);
		else
			source1Int = Integer.parseInt(source1);
		if(source2.matches(variarblePattern)) 
			source2Int = getIntField(source2);
		else
			source2Int = Integer.parseInt(source2);
		// calculate the answer
		answer = source1Int + source2Int;
		// save the answer to the desired destination
		setIntField(destination, answer);
	}
	
	// SUB function
	// SUB accepts two integer values, Source1 and Source2, and stores the result of (Source1 ¡V Source2) in the Destination variable, Destination.
	// Each Source can be either a variable or an integer constant.
	public void SUB(String[] instruction) {
		// declare value for destination, source 1, source 2 from instruction
		String destination = instruction[1];
		String source1 = instruction[2];
		String source2 = instruction[3];
		// convert String source1, source 2 to Integer source1Int, source2Int and calculate the answer
		int source1Int = 0, source2Int = 0, answer = 0;
		// Set up a variarblePattern allows program distinguish if it is a variable or value
		String variarblePattern = "^[A-Za-z][A-Za-z0-9]+";
		// If it is just a numeric value, convert it from string to integer
		// Otherwise, directly get value from that specific variable
		if(source1.matches(variarblePattern)) 
			source1Int = getIntField(source1);
		else
			source1Int = Integer.parseInt(source1);
		if(source2.matches(variarblePattern)) 
			source2Int = getIntField(source2);
		else
			source2Int = Integer.parseInt(source2);
		// calculate the answer
		answer = source1Int - source2Int;
		// save the answer to the desired destination
		setIntField(destination, answer);
	}
	
	// MUL function
	// MUL accepts two integer values, Source1 and Source2, and stores the result of (Source1 * Source2) in the Destination variable, Destination.
	// Each Source can be either a variable or an integer constant.
	public void MUL(String[] instruction) {
		// declare value for destination, source 1, source 2 from instruction
		String destination = instruction[1];
		String source1 = instruction[2];
		String source2 = instruction[3];
		// convert String source1, source 2 to Integer source1Int, source2Int and calculate the answer
		int source1Int = 0, source2Int = 0, answer = 0;
		// Set up a variarblePattern allows program distinguish if it is a variable or value
		String variarblePattern = "^[A-Za-z][A-Za-z0-9]+";
		// If it is just a numeric value, convert it from string to integer
		// Otherwise, directly get value from that specific variable
		if(source1.matches(variarblePattern)) 
			source1Int = getIntField(source1);
		else
			source1Int = Integer.parseInt(source1);
		if(source2.matches(variarblePattern)) 
			source2Int = getIntField(source2);
		else
			source2Int = Integer.parseInt(source2);
		// calculate the answer
		answer = source1Int * source2Int;
		// save the answer to the desired destination
		setIntField(destination, answer);
	}
	
	// DIV function
	// DIV accepts two integer values, Source1 and Source2, and stores the result of (Source1 / Source2) in the Destination variable, Destination.
	// Each Source can be either a variable or an integer constant.
	public void DIV(String[] instruction) {
		// declare value for destination, source 1, source 2 from instruction
		String destination = instruction[1];
		String source1 = instruction[2];
		String source2 = instruction[3];
		// convert String source1, source 2 to Integer source1Int, source2Int and calculate the answer
		int source1Int = 0, source2Int = 0, answer = 0;
		// Set up a variarblePattern allows program distinguish if it is a variable or value
		String variarblePattern = "^[A-Za-z][A-Za-z0-9]+";
		// If it is just a numeric value, convert it from string to integer
		// Otherwise, directly get value from that specific variable
		if(source1.matches(variarblePattern)) 
			source1Int = getIntField(source1);
		else
			source1Int = Integer.parseInt(source1);
		if(source2.matches(variarblePattern)) 
			source2Int = getIntField(source2);
		else
			source2Int = Integer.parseInt(source2);
		// calculate the answer
		answer = source1Int / source2Int;
		// save the answer to the desired destination
		setIntField(destination, answer);
	}
	
	// IN function
	// Inputs an integer value and stores it in Variable.
	public void IN(String destination) {
		Scanner scan = new Scanner(System.in);
		// ask user for an integer
		int input = scan.nextInt();
		scan.close();
		setIntField(destination, input);
	}
	
	// OUT function
	// Display Value.
	// Value can be either an integer variable or a string of characters enclosed in quotation marks (" ")
	public void OUT(String[] instruction) {
		// get instruction list length
		int iLength = instruction.length - 1; // ignore the first operation string like "OUT"
		// Set up a variarblePattern allows program distinguish if it is a variable or value
		String variarblePattern = "^[A-Za-z][A-Za-z0-9]+";
		// mark sure if it is a variable
		if(iLength == 1 && instruction[1].matches(variarblePattern)) {
			int value = getIntField(instruction[1]);
			System.out.println(value);
		}
		else {
			// get the original string 
			String str = instruction[1];
			for(int i = 2; i <= iLength; i++) {
				str += " " + instruction[i];
			}
			// check if the original string is enclosed in double quotation mark
			if(str.startsWith("\"") && str.endsWith("\"")) {
				String newStr = str.replaceAll("\"", "");
				System.out.println(newStr);
			}else
				System.out.println("You statement is missing double quotation mark!");
		}
	}
	
	// STO function
	// The STO instruction stores the value of Source in Destination variable.
	// Source can be either a variable or an integer constant.
	public void STO(String[] instruction) {
		// declare value for destination, source from instruction
		String destination = instruction[1];
		String source = instruction[2];
		// initialize sourceInt
		int sourceInt = 0;
		// Set up a variarblePattern allows program distinguish if it is a variable or value
		String variarblePattern = "^[A-Za-z][A-Za-z0-9]+";
		// If it is just a numeric value, simply convert it from string to integer
		// Otherwise, directly get value from that specific variable
		if(source.matches(variarblePattern)) 
			sourceInt = getIntField(source);
		else
			sourceInt = Integer.parseInt(source);
		setIntField(destination, sourceInt);
	}
	
	// a function save int value to a specific variable
	public void setIntField(String fieldName, int value) {
	    try {
		    Field field = getClass().getDeclaredField(fieldName);
		    field.setInt(this, value);
	    } catch (Exception e) {
			e.printStackTrace();
	    }
	}
	// a function get int value from a specific variable
	public int getIntField(String fieldName) {
		int value = 0;
		try {
			Field field = getClass().getDeclaredField(fieldName);
			value = field.getInt(field);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}
	
	// save the label information and location
	public void setLabel(String name, int location) {
		String labelPattern = "^[A-Za-z]+";
		if(name.matches(labelPattern)) {
			LabelList.add(new Label(name, location));
		}
	}
	
	// BRn
	// if the value of variable is negative, jump to label
	public void BRn(String[] instruction) {
		String variable = instruction[1];
		String requiredLabel = instruction[2];
		int value = getIntField(variable);
		if(value < 0)
			JMP(requiredLabel);
	}
	
	// BRz
	// if the value of variable is zero, jump to label
	public void BRz(String[] instruction) {
		String variable = instruction[1];
		String requiredLabel = instruction[2];
		int value = getIntField(variable);
		if(value ==  0)
			JMP(requiredLabel);
	}
	
	// BRp
	// if the value of variable is positive, jump to label
	public void BRp(String[] instruction) {
		String variable = instruction[1];
		String requiredLabel = instruction[2];
		int value = getIntField(variable);
		if(value > 0)
			JMP(requiredLabel);
	}
	
	// BRzp
	// if the value of variable is zero or positive, jump to label
	public void BRzp(String[] instruction) {
		String variable = instruction[1];
		String requiredLabel = instruction[2];
		int value = getIntField(variable);
		if(value >=  0)
			JMP(requiredLabel);
	}
	
	// BRzp
	// if the value of variable is zero or negative, jump to label
	public void BRzn(String[] instruction) {
		String variable = instruction[1];
		String requiredLabel = instruction[2];
		int value = getIntField(variable);
		if(value <=  0)
			JMP(requiredLabel);
	}
	
	// JMP function
	// Jump to label
	public void JMP(String selectedLabel) {
		for(Label label : LabelList) 
		{
			if(label.getName().equals(selectedLabel)) {
				programCounter = label.getLocation();
			}
		}
	}
	
	// Label object
	public class Label {
		String name;
		int location;
		public Label(String name, int location) {
			this.name = name;
			this.location = location;
		}
		public String getName() {
			return name;
		}
		public int getLocation() {
			return location;
		}
	}
}