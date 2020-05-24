import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import utilmport.generateBugs;
import utilmport.getJIRAInfo;
import utils.ArgParser;
import utils.Bug;
import utils.CommitSZZ;

public class Main {

	public static void main(String[] args) {
		
		/*testing(args);
		System.out.println("Test OK");
		if(true) {return;}*/
		
		//parse parameters args
		Map<String, String> parsedArg = ArgParser.parseArg(args);

		String pathToResults = parsedArg.get("-results");
		getJIRAInfo.setUpReader(parsedArg.get("-truth"), parsedArg.get("-repo"));
		System.out.println("Setup OK");
		
		Integer wellDetected = 0;
		//Generate Bugs from result JSON
		List<Bug> bugs;
		bugs = generateBugs.readJsonResults(pathToResults);
		System.out.println("Bug import OK");
		//For each bug, do evaluation of earliest bug appearance and get commit introducers
		Integer earliestBugAppearanceCounter = 0;
		
		Set<CommitSZZ> commitIntroducerList = new LinkedHashSet<CommitSZZ>();
		List<Integer> timeSpanPotentials = new LinkedList<Integer>();
		for(Bug bug : bugs) {
			if(bug.hasWellPredicted()) {wellDetected++;}
			
			if(bug.earliestBugAppearance()) {earliestBugAppearanceCounter++;}
			commitIntroducerList.addAll(bug.commitIntroducersSZZ);
			
			Integer ts = bug.potentialIntroducerTimeSpan();
			//TODO : A enlever
			if(ts != 0) {timeSpanPotentials.add(ts);}
		}
		
		float earliestBugAppearanceRatio = ((float)earliestBugAppearanceCounter)/((float)bugs.size());
		
		//For each distinct introducing commit, count the number of bugs and generate the list of timespans
		Integer moreBugsThanCap = 0;
		Integer bugCap = 1;
		List<Integer> futureImpacts = new LinkedList<Integer>();
		List<Integer> timeSpanImpacts = new LinkedList<Integer>();
		
		for(CommitSZZ introducer : commitIntroducerList) {
			Integer impacts = introducer.numberOfAssociated();
			futureImpacts.add(impacts);
			if(impacts>bugCap) {moreBugsThanCap++;}
			Integer timeSpanImpact = introducer.daysTimeSpan();
			if(timeSpanImpact != null) {timeSpanImpacts.add(introducer.daysTimeSpan());}
		}
		
		
		//Using time spans of future impacts, generate the upper median average deviance and get the number of commit introducing more bug than it
		Integer upperMADImpacts = upperMAD(timeSpanImpacts);
		Integer upMADCounterImpacts = 0;
		for(Integer timeSpanImpact : timeSpanImpacts) {
			if(timeSpanImpact>upperMADImpacts) {upMADCounterImpacts++;}
		}
		
		//Using the time span of potential introducers, get the number of them up of upMAD
		Integer upperMADPotentials = upperMAD(timeSpanPotentials);
		Integer upMADCounterPotentials = 0;
		for(Integer timeSpanPotential : timeSpanPotentials) {
			if(timeSpanPotential>upperMADPotentials) {upMADCounterPotentials++;}
		}
		
		//Output Values:
		//Données globale
		System.out.println("Bugs analyzed by SZZ : " + Integer.toString(bugs.size()));
		System.out.println("Bugs well detected by SZZ : " + Integer.toString(wellDetected));
		System.out.println("Precision : " + String.valueOf(((float)wellDetected)/((float)bugs.size())));
		
		//Feature 1 : Earliest bug appearance
		System.out.println("Earliest Bug Appearance Ratio : " + String.valueOf(earliestBugAppearanceRatio));
		
		
		//Feature 2.1 : Impact of changes : number of future bugs
		System.out.println("Average number bugs added by a single commit : " + String.valueOf(mean(futureImpacts)));
		System.out.println("Number of commit adding more than X bugs : " + Integer.toString(moreBugsThanCap) + " / " + Integer.toString(bugs.size()));
		
		//Feature 2.2 : Impact of changes : time span of future bugs
		System.out.println("Median time span of future impacts : " + String.valueOf(median(timeSpanImpacts)));
		System.out.println("Value of the upper median absolute deviation of time span : " + Integer.toString(upperMADImpacts));
		System.out.println("Number of commit which time span of induced bugs is more than upper MAD : " + Integer.toString(upMADCounterImpacts) + " / " + Integer.toString(commitIntroducerList.size()));
		
		//Feature 3 : Time span of potential introducer
		System.out.println("Median time span of potential introducer : " + String.valueOf(median(timeSpanPotentials)));
		System.out.println("Value of the upper median absolute deviation of time span : " + Integer.toString(upperMADPotentials));
		System.out.println("Number of commit which time span of induced bugs is more than upper MAD : " + Integer.toString(upMADCounterPotentials) + " / " + Integer.toString(bugs.size()));
		
	}
	
	
	public static Integer upperMAD(List<Integer> timeSpans) {
		Integer median = median(timeSpans);
		List<Integer> deviations = new LinkedList<Integer>();
		for(Integer timeSpan : timeSpans) {
			deviations.add(Math.abs(timeSpan - median));
		}
		return median + median(deviations);
	}
	
	public static Integer median(List<Integer> list) {
		Collections.sort(list);
		Integer index = list.size();
		if(index%2 == 0) { return list.get(index/2);}
		if((index/2)-1 < 0){return list.get(index/2);}
		return (list.get((index/2)-1) + list.get(index/2))/2;
	}
	
	public static double mean(List<Integer> integers) {
		Integer sum = 0;
		Integer size = integers.size();
		if(!integers.isEmpty()) {
		    for (int i = 0; i < size; i++) {
		        sum += integers.get(i);
		    }
		    return sum.doubleValue() / size;
		}
		return sum;
	}
	
	//ONLY TESTING
	public static int testing(String[] args) {
		return 0;
	}
}
