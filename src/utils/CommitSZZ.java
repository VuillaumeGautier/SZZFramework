package utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import utilmport.getJIRAInfo;

public class CommitSZZ extends Commit {//TODO : comm
	
	public List<Commit> associatedBugs;

	public CommitSZZ(String hashCode) {
		super(hashCode);
		associatedBugs = new LinkedList<Commit>();
		List<String> associatedBugsHash = getJIRAInfo.getBugsLinkedToCommit(hashCode);
		//System.out.print(this.hashCode + " -> ");
		//System.out.println(associatedBugsHash);
		for(String hash : associatedBugsHash) {
			this.associatedBugs.add(new Commit(hash));
		}
	}

	public Integer numberOfAssociated() {
		return associatedBugs.size();
	}

	public Integer daysTimeSpan() {
		
		if(associatedBugs.size() == 0) {return null;}
		
		List<Date> dates = new LinkedList<Date>();
		
		for(Commit commit : associatedBugs) {
			dates.add(commit.date);
		}
		
		Collections.sort(dates);
		
		@SuppressWarnings("deprecation")
		LocalDateTime date2 = dates.get(dates.size() - 1).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	    LocalDateTime date1 = dates.get(0).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	    Integer daysBetween = (int) Duration.between(date1, date2).toDays();
		
		return daysBetween;
	}
	
}
