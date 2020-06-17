package utilmport;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class getProjectInfo {//this class is used to get most data from the ground truth and database (like dates)
	
	private static String pathGroundTruth;
	private static String pathGitRepo;
	private static Map<String, String> groundTruth; //a map of commits' hash with the link between the commit of a fix and the commit of the real introducer
	private static CommandRunner cr;

	public static void setUpReader(String pathGroundTruth, String pathGitRepo) {
		getProjectInfo.pathGroundTruth = pathGroundTruth;
		getProjectInfo.pathGitRepo = pathGitRepo;
		setGroundTruth();
		cr = new CommandRunner(getProjectInfo.pathGitRepo);
	}
	
	private static void setGroundTruth() { //generate all the objects based on the ground truth json
		groundTruth = new HashMap<String, String>();
		
		JSONParser commitParser = new JSONParser();//setup json reader
		
	    try {
	    	FileReader fr = new FileReader(pathGroundTruth);
	    	
		    JSONObject object = (JSONObject) commitParser.parse(fr); //generate json object from the file
		    
		    for (Object pair : object.keySet()) {
		    	@SuppressWarnings("unchecked")
				Map<String, String> issueInfo = (Map<String, String>) object.get(pair); //get pairs from the json of fix-introducer hash
		    	groundTruth.put(issueInfo.get("fix"), issueInfo.get("introducer")); //put the pair in the map
		    }
	    }catch(IOException e) {
	    	System.out.println("Bug reading JSON of bug ground truth (IO)");
	    } catch (org.json.simple.parser.ParseException e) {
	    	System.out.println("Bug reading JSON of bug ground truth (Parse)");
			e.printStackTrace();
		}
	}
	
	//Checks the map to get the list of hashs of commits fixing bugs given the hash of a potential introducer
	public static List<String> getBugsLinkedToCommit(String hashIntroducer) {
		List<String> keys = new LinkedList<String>();
		
	    for (Entry<String, String> entry : groundTruth.entrySet()) {
	        if (hashIntroducer.equals(entry.getValue())) {
	            keys.add(entry.getKey());
	        }
	    }
	    
	    return keys;
	}
	
	//use the command runner to get the date of a commit in a Date object
	public static Date getDateOfCommit(String hash) {
		String dateText = cr.read(hash);//use the command runer
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(dateText);//make and return the date object
		} catch (ParseException e) {
			e.printStackTrace();
		};
		return null;
	}
	
	//checks the map to get the hash of the real introducer commit
	public static String getRealIntroducer(String hashFix) {	
		if(groundTruth.containsKey(hashFix)) {
			return groundTruth.get(hashFix);
		} else {
			System.out.println("Commit : " + hashFix + " has no data in the ground truth");
			return null;
		}
		
	}
	
}
