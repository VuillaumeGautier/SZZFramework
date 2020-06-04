package utilmport;

import java.io.*;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import utils.Bug;
import utils.CommitSZZ;


public class generateBugs {//TODO : comm
	
	//A map containing a link to each flagged as introdcuer commit. Avoid duplicates
	private static Map<String, CommitSZZ> generatedIntroducers = new HashMap<String, CommitSZZ>();
	
	public static List<Bug> readJsonResults(String pathResults) {//TODO : nouveau format
	    
	    List<Bug> bugs = new LinkedList<Bug>();
		
		
		JSONParser commitParser = new JSONParser();
		Bug bug;
	    try {
	    	FileReader fr = new FileReader(pathResults);
	    	
		    JSONObject object = (JSONObject) commitParser.parse(fr);
		    for (Object pair : object.keySet()) {
		    	
		    	JSONObject pairHash = (JSONObject) object.get(pair);
		    	Map<String, String> value = (Map<String, String>) pairHash.get("introducers");
		    	
		    	List<String> introducers = new LinkedList<String>( ((Map<String, String>) pairHash.get("introducers")).values());
		    	
		    	List<CommitSZZ> introducersObjects = new LinkedList<CommitSZZ>();
		    	for(String hashIntroducer : introducers) {
					if(!generatedIntroducers.containsKey(hashIntroducer)) {
						generatedIntroducers.put(hashIntroducer, new CommitSZZ(hashIntroducer));
					}
					introducersObjects.add(generatedIntroducers.get(hashIntroducer));
				}
		    	
		    	bug = new Bug(pairHash.get("fix").toString(), introducersObjects );
		    	bugs.add(bug);
		    }
	    }catch(IOException e) {
	    	System.out.println("Bug reading JSON of bug ground truth (IO)");
	    } catch (org.json.simple.parser.ParseException e) {
	    	System.out.println("Bug reading JSON of bug ground truth (Parse)");
			e.printStackTrace();
		}
	    
	    return bugs;
	   
	}
	
}
