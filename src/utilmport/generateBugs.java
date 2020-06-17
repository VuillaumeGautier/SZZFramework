package utilmport;

import java.io.*;
import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import utils.Bug;
import utils.CommitSZZ;


public class generateBugs {
	
	//A map containing a link to each flagged as introdcuer commit. Avoid duplicates
	private static Map<String, CommitSZZ> generatedIntroducers = new HashMap<String, CommitSZZ>();
	
	//Create Bugs objects based on the JSON of the results
	public static List<Bug> readJsonResults(String pathResults) {
	    
		
	    List<Bug> bugs = new LinkedList<Bug>();
		
		JSONParser commitParser = new JSONParser();
		
	    try {
	    	//Prepare JSON Object from the JSON file
	    	FileReader fr = new FileReader(pathResults);
		    JSONObject object = (JSONObject) commitParser.parse(fr);
		    
		    Bug bug;
		    
		     
		    for (Object pair : object.keySet()) {
		    	
		    	//For each entry in the JSON Object, get the list of introducers following the data format
		    	JSONObject pairHash = (JSONObject) object.get(pair);
		    	Map<String, String> value = (Map<String, String>) pairHash.get("introducers");
		    	
		    	List<String> introducers = new LinkedList<String>( ((Map<String, String>) pairHash.get("introducers")).values());
		    	
		    	
		    	//Prepare the list of potential introducers objects used as parameters in the Bug object
		    	List<CommitSZZ> introducersObjects = new LinkedList<CommitSZZ>();
		    	//For each commit, avoid duplicates using generatedIntroducers map.
		    	for(String hashIntroducer : introducers) {
					if(!generatedIntroducers.containsKey(hashIntroducer)) {
						generatedIntroducers.put(hashIntroducer, new CommitSZZ(hashIntroducer));
					}
					introducersObjects.add(generatedIntroducers.get(hashIntroducer));
				}
		    	
		    	
		    	//Create Bug object and loop again
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
