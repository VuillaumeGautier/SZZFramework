package utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import utilmport.getJIRAInfo;

public class Bug {
	public List<CommitSZZ> commitIntroducersSZZ; //Potential introducers flagged by SZZ
	public Commit commitIntroducerReal; //Real introducer from the database
	public Commit commitFix;	//Fixing commit
	
	public Bug(String hashFix, List<CommitSZZ> commitIntroducersSZZ) {
		this.commitFix = new Commit(hashFix);
		this.commitIntroducersSZZ = commitIntroducersSZZ;
		this.commitIntroducerReal = new Commit(getJIRAInfo.getRealIntroducer(hashFix));
	}
	
	//Compute earliest bug appearance, checking if potential commits are younger than introducer commit
	public boolean earliestBugAppearance() {
		for(CommitSZZ introducer : commitIntroducersSZZ) {
			if(introducer.date.after(commitIntroducerReal.date)) {
				return true;
			}
		}		
		return false;
	}
	
	//Compute single bug precision
	public boolean hasWellPredicted() {
		for(CommitSZZ introducer : commitIntroducersSZZ) {
			if(introducer.hashCode.equals(commitIntroducerReal.hashCode)) {return true;}
		}
		return false;
	}
	
	//Compute time span between the oldest and the youngest potential introducer of the bug
	public Integer potentialIntroducerTimeSpan() {
		
		List<Date> dates = new LinkedList<Date>();
		
		for(Commit commit : commitIntroducersSZZ) {
			dates.add(commit.date);
		}
		
		Collections.sort(dates);
		
		LocalDateTime date2 = dates.get(dates.size() - 1).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	    LocalDateTime date1 = dates.get(0).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	    Integer daysBetween = (int) Duration.between(date1, date2).toDays();
	    
		return daysBetween;
	}
	
}
