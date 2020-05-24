package utils;

import java.util.HashMap;
import java.util.Map;

public class ArgParser {
	
	private static String[] argList = {"-results", "-repo", "-truth"}; 
	
	public static Map<String, String> parseArg(String[] args){
		
		Map<String, String> parsedArgs = new HashMap<String, String>();
		for(int i=0; i<args.length;i++) {
				if(isArg(args[i])) {parsedArgs.put(args[i], args[i+1]);
			}
		}
		
		return parsedArgs;
	}
	
	private static boolean isArg(String arg) {
		
		for(String argElem : argList) {
			if(arg.equals(argElem)) {return true;}
		}
		
		return false;
	}
}
