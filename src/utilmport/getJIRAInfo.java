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

public class getJIRAInfo {//TODO : comm
	
	private static String pathGroundTruth;
	private static String pathGitRepo;
	private static Map<String, String> groundTruth;
	private static CommandRunner cr;

	public static void setUpReader(String pathGroundTruth, String pathGitRepo) {
		getJIRAInfo.pathGroundTruth = pathGroundTruth;
		getJIRAInfo.pathGitRepo = pathGitRepo;
		setGroundTruth();
		cr = new CommandRunner(getJIRAInfo.pathGitRepo);
	}
	
	private static void setGroundTruth() {
		groundTruth = new HashMap<String, String>();
		
		JSONParser commitParser = new JSONParser();
		
	    try {
	    	FileReader fr = new FileReader(pathGroundTruth);
	    	
		    JSONObject object = (JSONObject) commitParser.parse(fr);
		    for (Object pair : object.keySet()) {
		    	@SuppressWarnings("unchecked")
				Map<String, String> issueInfo = (Map<String, String>) object.get(pair);
		    	groundTruth.put(issueInfo.get("fix"), issueInfo.get("introducer"));
		    }
	    }catch(IOException e) {
	    	System.out.println("Bug reading JSON of bug ground truth (IO)");
	    } catch (org.json.simple.parser.ParseException e) {
	    	System.out.println("Bug reading JSON of bug ground truth (Parse)");
			e.printStackTrace();
		}
	}
	
	
	public static List<String> getBugsLinkedToCommit(String hashIntroducer) {
		List<String> keys = new LinkedList<String>();
	    for (Entry<String, String> entry : groundTruth.entrySet()) {
	        if (hashIntroducer.equals(entry.getValue())) {
	            keys.add(entry.getKey());
	        }
	    }
	    return keys;
	}
	
	public static Date getDateOfCommit(String hash) {
		String dateText = cr.read(hash);
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(dateText);
		} catch (ParseException e) {
			e.printStackTrace();
		};
		return null;
	}
	
	public static String getRealIntroducer(String hashFix) {	
		if(groundTruth.containsKey(hashFix)) {
			return groundTruth.get(hashFix);
		} else {
			System.out.println("Commit : " + hashFix + " has no data in the ground truth");
			return null;
		}
		
	}
	
}
