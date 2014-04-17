package TheOdds;

import java.io.File;
import java.util.ArrayList;

public class FileNameResolver {
		
	String path;
	File folder;
	
	public FileNameResolver(String folderPath){
		path = folderPath;
		folder = new File(path);
	}
	
	public ArrayList<File> getFiles(){
		ArrayList<File> result = new ArrayList<File>();
		for (File f : folder.listFiles()){
			if (f.isFile()){
				result.add(f);
			}
		}
		return result;
	}
}
