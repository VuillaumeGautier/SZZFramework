package utilmport;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CommandRunner {//Object to print and read commands in console
	
	private String os; //os name
	private String pathRepo; //path to the repo, used to read commit date
	
	public CommandRunner(String pathRepo) {
		this.pathRepo = pathRepo;
		if(System.getProperty("os.name").toLowerCase().startsWith("windows")) {os = "windows";}
		//if(System.getProperty("os.name").toLowerCase().startsWith("windows")) {os = "windows";}//TODO: LINUX
		
	}

	public String read(String hash) {//redirect the read to the corresponding os. unused
		if(os.equals("windows")) {return readWindows(hash);}
		return readWindows(hash);
		//return null;
	}
	
	public String readWindows(String hash){
		
		Runtime rt = Runtime.getRuntime(); //get the os runtime
		String commands = "cmd /c cd /d" + pathRepo + " && " + "git show -s --format=%ci " + hash; //write the command to send
		// "cmd /c" opens a cmd instance for windows and send the follow command
		// "cd /d" with a path change to the directory in a path with the disk
		// "git show -s --format=%ci " with a hash gets the date of a commit
		
		String s = null;
		try {
			//use command in the runtime
			Process proc = rt.exec(commands);
			
			BufferedReader stdInput = new BufferedReader(new 
			     InputStreamReader(proc.getInputStream()));
			
			//gets first line of output
			s = stdInput.readLine();
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return s.split(" ")[0];//splits the output line to only get the date
		
	}
}
