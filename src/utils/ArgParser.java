package utils;

import java.util.HashMap;
import java.util.Map;

public class ArgParser {//class used to get arguments from the command execution
	
	private static String[] argList = {"-results", "-repo", "-truth"}; 
	
	public static Map<String, String> parseArg(String[] args){
		
		Map<String, String> parsedArgs = new HashMap<String, String>();
		//Checks each value of the args from the execution
		for(int i=0; i<args.length;i++) {
			//checks if the -name is existing, if so add it 
			if(isArg(args[i])) {parsedArgs.put(args[i], args[i+1]);}
		}
		
		return parsedArgs;
	}
	
	//checking array of names if the parameters is listed
	private static boolean isArg(String arg) {
		for(String argElem : argList) {
			if(arg.equals(argElem)) {return true;}
		}
		return false;
	}
}
