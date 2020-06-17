package utils;

import java.util.Date;

import utilmport.getProjectInfo;

public class Commit {

	
	public String hashCode;
	public Date date;
	
	public boolean isEalrierThan(Commit commitCompare) {
		return this.date.before(commitCompare.date);
	}
	
	public Commit(String hashCode) {
		this.hashCode = hashCode;
		this.date = getProjectInfo.getDateOfCommit(hashCode);
	}
	
}
