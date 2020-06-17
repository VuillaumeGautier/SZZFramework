package utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import utilmport.getJIRAInfo;

public class CommitSZZ extends Commit {
	
	public List<Commit> associatedBugs; //The list of bugfix commits associated to this introducer.

	public CommitSZZ(String hashCode) {
		super(hashCode);//Has same properties as a normal commit object
		
		associatedBugs = new LinkedList<Commit>();
		List<String> associatedBugsHash = getJIRAInfo.getBugsLinkedToCommit(hashCode); //get hashs of bugfixes using the ground truth
		for(String hash : associatedBugsHash) {//generate commit objects corresponding
			this.associatedBugs.add(new Commit(hash));
		}
		
	}

	public Integer numberOfAssociated() {
		return associatedBugs.size();
	}

	public Integer daysTimeSpan() {//Give the time span between the oldest and the youngest bug introduced by this commit
		
		if(associatedBugs.size() == 0) {return null;}
		
		List<Date> dates = new LinkedList<Date>();
		
		for(Commit commit : associatedBugs) {
			dates.add(commit.date);
		}
		
		Collections.sort(dates);
		
		LocalDateTime date2 = dates.get(dates.size() - 1).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	    LocalDateTime date1 = dates.get(0).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	    Integer daysBetween = (int) Duration.between(date1, date2).toDays();
		
		return daysBetween;
	}
	
}
