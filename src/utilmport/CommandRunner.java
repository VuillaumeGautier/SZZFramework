package utilmport;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CommandRunner {//TODO : comm
	
	private String os;
	private String pathRepo;
	
	public CommandRunner(String pathRepo) {
		this.pathRepo = pathRepo;
		if(System.getProperty("os.name").toLowerCase().startsWith("windows")) {os = "windows";}
		//if(System.getProperty("os.name").toLowerCase().startsWith("windows")) {os = "windows";}//TODO: LINUX
		
	}

	public String read(String hash) {
		if(os.equals("windows")) {return readWindows(hash);}
		return readWindows(hash);
		//return null;
	}
	
	public String readWindows(String hash){
		
		Runtime rt = Runtime.getRuntime();
		String commands = "cmd /c cd /d" + pathRepo + " && " + "git show -s --format=%ci " + hash;
		String s = null;
		try {
			Process proc = rt.exec(commands);
			
			BufferedReader stdInput = new BufferedReader(new 
			     InputStreamReader(proc.getInputStream()));
			
			s = stdInput.readLine();
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return s.split(" ")[0];
		
	}
}
