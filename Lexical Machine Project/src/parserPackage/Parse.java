// ----------------------------------------------------------------------------
// Parse.Java
// Author: CheukHo Wong
// Professor: Dr.Salimi
// Class: CSCI 4200
// ----------------------------------------------------------------------------

package parserPackage;
import java.io.*;

public class Parse {
	static final int MAX_LEXEME_LENGTH = 100;
	static final int MAX_TOKEN_LENGTH = 100;
	static int charClass;
	static char[] lexeme = new char[MAX_LEXEME_LENGTH];
	static char nextChar;
	static int lexLen;
	static int token;
	static int nextToken;
	static String nextLine;
	static FileReader file;
	static FileReader file2;
	static BufferedReader reader;
	static BufferedReader r2;
	static PrintStream originalStream;
	
	/*Character classes*/
	static final int LETTER = 0;
	static final int DIGIT = 1;
	static final int UNKNOWN = 99;
	
	/*Token codes*/
	static String[] token_dict = new String[MAX_TOKEN_LENGTH];
	static final int INT_LIT = 10;
	static final int IDENT = 11;
	static final int ASSIGN_OP = 20;
	static final int ADD_OP = 21;
	static final int SUB_OP = 22;
	static final int MULT_OP = 23;
	static final int DIV_OP = 24;
	static final int LEFT_PAREN = 25;
	static final int RIGHT_PAREN = 26;
	static final int END_OF_LINE = 97;
	static final int END_OF_FILE = 98;
	
/********************************************************************/

	/*Open the input data file and process its contents*/
	public static void main(String[] args) throws FileNotFoundException {

		token_dict[INT_LIT] = "INT_LIT\t";
		token_dict[IDENT] = "IDENT\t";
		token_dict[ASSIGN_OP] = "ASSIGN_OP";
		token_dict[ADD_OP] = "ADD_OP\t";
		token_dict[SUB_OP] = "SUB_OP\t";
		token_dict[MULT_OP] = "MULT_OP\t";
		token_dict[DIV_OP] = "DIV_OP\t";
		token_dict[LEFT_PAREN] = "LEFT_PAREN";
		token_dict[RIGHT_PAREN] = "RIGHT_PAREN";
		token_dict[END_OF_LINE] = "END_OF_LINE";
		token_dict[END_OF_FILE] = "END_OF_FILE";

		
		file = new FileReader("src\\parserPackage\\statements.txt");
		file2 = new FileReader("src\\parserPackage\\statements.txt");
		
		reader = new BufferedReader(file);
		r2 = new BufferedReader(file2);
		if (file == null){
			System.out.println("ERROR - cannot open front.in \n");
		}else{
			try {
				while(reader.ready()){
					System.out.println("*****************************************************");
					nextLine = r2.readLine();
					// a save point for printstream
					originalStream = System.out;
					System.out.println("Parsing the statement: " + nextLine + "\n");
					getChar();
					assign();
					// print out normal output stream
					System.setOut(originalStream);
					System.out.println("");
				}
				/*Once all the statments are parsed, print out EOF statement at the end*/
				System.out.println("*****************************************************");
				System.out.printf("Next token is: %s\t\t Next lexeme is %s\n", token_dict[END_OF_FILE], "EOF");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
/********************************************************************/
	
	/*lookup operators and parentheses and return their token value*/
	static int lookup(char ch){
		
		switch (ch) {
		case '(':
			addChar();
			nextToken = LEFT_PAREN;
			break;
			
		case ')':
			addChar();
			nextToken = RIGHT_PAREN;
			break;
			
		case '+':
			addChar();
			nextToken = ADD_OP;
			break;
			
		case '-':
			addChar();
			nextToken = SUB_OP;
			break;
			
		case '*':
			addChar();
			nextToken = MULT_OP;
			break;
			
		case '/':
			addChar();
			nextToken = DIV_OP;
			break;
			
		case '=':
			addChar();
			nextToken = ASSIGN_OP;
			break;
			
		default:
			nextToken = END_OF_LINE;
			break;
		}
		return nextToken;
	}
	
/********************************************************************/
	
	/*adds next char to lexeme*/
	static void addChar(){
		
		if(lexLen <= (MAX_LEXEME_LENGTH-2)){
			lexeme[lexLen++] = nextChar;
			lexeme[lexLen] = 0;
		}else{
			System.out.println("ERROR - lexeme is too long \n");
		}
	}
	
/********************************************************************/
	
	/*get the next char of an input and determine its character class*/
	static void getChar(){
		
		char c = 0;
		try {
			c = (char)reader.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		nextChar = c;
		if((int)nextChar != 0){
			if(isalpha(nextChar)){
				charClass = LETTER;
			}else if(isdigit(nextChar)){
				charClass = DIGIT;
			}else{ charClass = UNKNOWN;}
		}else{
//			charClass = END_OF_FILE;
			charClass = END_OF_LINE;
		}
	}
	
/********************************************************************/
	
	/*call getChar function until it returns a non-whitespace character*/
	static void getNonBlank(){
		
		while(isspace(nextChar)){
			getChar();
		}
	}
	
/********************************************************************/
	
	/*a simple lexical analyzer for arithmetic expressions*/
	static int lex(){
		
		lexLen = 0;
		getNonBlank();
		switch (charClass){
		
		/*Parse identifiers */
		case LETTER:
			addChar();
			getChar();
			while(charClass == LETTER || charClass == DIGIT){
				addChar();
				getChar();
			}
			nextToken = IDENT;
			break;
			
		/*Parse integer literals */
		case DIGIT:
			addChar();
			getChar();
			while(charClass == DIGIT){
				addChar();
				getChar();
			}
			nextToken = INT_LIT;
			break;
			
		/*Parentheses and operators*/
		case UNKNOWN:
			lookup(nextChar);
			getChar();
			break;
			
		/*End of the line*/
		case END_OF_LINE:
			nextToken = END_OF_LINE;
			break;
		}/*End of switch */
		if(nextToken != END_OF_LINE) {
			String s = new String(lexeme);
			s = s.substring(0,lexLen);
			System.out.printf("Next token is: %s\t\t Next lexeme is %s\n", token_dict[nextToken], s);
		}
		return nextToken;
	}/*End of function lex() */
	
/********************************************************************/
	
	/*returns true if char is a letter*/
	static boolean isalpha(char c){
		
		int ascii = (int) c;
		if((ascii > 64 && ascii < 91) || (ascii > 96 && ascii < 123)){
			return true;
		}else {return false;}
	}
	
/********************************************************************/
	
	/*returns true if char is a digit*/
	static boolean isdigit(char c){
		
		int ascii = (int) c;
		if(ascii > 47 && ascii < 58){
			return true;
		}else {return false;}
	}
	
/********************************************************************/
	
	/*returns true if char is a space*/
	static boolean isspace(char c){
		
		int ascii = (int) c;
		if(ascii == 32){
			return true;
		}else {return false;}
	}
	
/********************************************************************/

	/* term
	Parses strings in the language generated by the rule:
	<term> -> <factor> {(* | /) <factor>)
	*/
	static void term() {
		System.out.println("Enter <term>");
	/* Parse the first factor */
		factor();
	/* As long as the next token is * or /, get the
	next token and parse the next factor */
		while (nextToken == MULT_OP || nextToken == DIV_OP) {
			lex();
			factor();
		}
		System.out.println("Exit <term>");
	} /* End of function term */
	
/********************************************************************/

	/* factor
	Parses strings in the language generated by the rule:
	<factor> -> id | int_constant | ( <expr )
	*/
	static void factor() {
		System.out.println("Enter <factor>");
		/* Determine which RHS */
		if (nextToken == IDENT || nextToken == INT_LIT)
		/* Get the next token */
			lex();
		/* If the RHS is ( <expr>), call lex to pass over the
		left parenthesis, call expr, and check for the right
		parenthesis */
		else {
			if (nextToken == LEFT_PAREN) {
				lex();
				expr();
				if (nextToken == RIGHT_PAREN) {
					lex();
				}
				else
					error("right parentheses");
		} /* End of if (nextToken == ... */
		/* It was not an id, an integer literal, or a left
		parenthesis */
			else
				error("left parentheses");
		} /* End of else */
		System.out.println("Exit <factor>");
	} /* End of function factor */
	
/********************************************************************/
	/* expr
	Parses strings in the language generated by the rule:
	<expr> -> <term> {(+ | -) <term>}
	*/
	static void expr() {
		System.out.println("Enter <expr>");
	/* Parse the first term */
		term();
	/* As long as the next token is + or -, get
	the next token and parse the next term */
		while (nextToken == ADD_OP || nextToken == SUB_OP) {
			lex();
			term();
		}
		System.out.println("Exit <expr>");
	} /* End of function expr */

/********************************************************************/
	static void assign() {
		/* parses string by the rule: <assign> -> id = <expr>
		 **/
		/*Assume that LHS is existed*/
		if((nextToken = lex())== IDENT) {
			System.out.println("Enter <assign>");
			/*Assume that assign sign is already existed between LHS and RHS*/
			if((nextToken = lex()) == ASSIGN_OP) {
				do{
					/*parse RHS by <expr> rule*/
					lex();
					expr();
					System.out.println("Exit <assign>");
				}while(nextToken != END_OF_LINE);
			}
		}
	}
/********************************************************************/
	// once the error is detected, the following outputs outputs will be hidden 
	// while operations are still working.
	static void error(String errorType) {
		/*print out the notice about missing element*/
		System.out.println("***Your statment is missing " + errorType + "***");
		/*hide the rest of following <enter> and <exit> statements*/
		PrintStream hideStream = new PrintStream(new OutputStream(){
		    public void write(int b) {
		        // Nothing to print out
		    }
		});
		System.setOut(hideStream);
	}
}