package utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import utilmport.getJIRAInfo;

public class Bug { //TODO : comm
	public List<CommitSZZ> commitIntroducersSZZ;
	public Commit commitIntroducerReal;
	public Commit commitFix;
	
	public Bug(String hashFix, List<CommitSZZ> commitIntroducersSZZ) {
		this.commitFix = new Commit(hashFix);
		this.commitIntroducersSZZ = new LinkedList<CommitSZZ>();
		
		this.commitIntroducersSZZ = commitIntroducersSZZ;
		
		this.commitIntroducerReal = new Commit(getJIRAInfo.getRealIntroducer(hashFix));
		
	}
	
	public boolean earliestBugAppearance() {
		for(CommitSZZ introducer : commitIntroducersSZZ) {
			if(introducer.date.after(commitIntroducerReal.date)) {
				return true;
			}
		}		
		return false;
	}
	
	public boolean hasWellPredicted() {
		
		for(CommitSZZ introducer : commitIntroducersSZZ) {
			if(introducer.hashCode.equals(commitIntroducerReal.hashCode)) {return true;}
		}
		return false;
	}
	
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
