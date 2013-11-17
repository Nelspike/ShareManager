package share.manager.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.os.Environment;
import android.util.Pair;

public class FileWriter {

	static private final String filename="portfolio.txt", directoryName = "ShareManager";
	static private final File directory=null;
	static private FileOutputStream fileOutStream;


	static public ArrayList<Pair<String,Integer> >readFile()
	{
		ArrayList<Pair<String,Integer> > arr=new  ArrayList<Pair<String,Integer> >();
		Pair<String,Integer> row=null;
		getFileStream();
		File file = new File(directory, filename);
		String scan;
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
			}
		}
		
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fileReader);

		try {
			while ((scan = br.readLine()) != null)
			{
					row=new Pair<String, Integer>(scan.split("-.-")[0],Integer.getInteger(scan.split("-.-")[1]) );
				arr.add(row);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return arr;
	}
	
	static private void createFile() {
		/*String toWrite = "Ticket purchased in ";
		toWrite += new Date() + " by " + username + '\n';
		try {
			fileOutStream.write(toWrite.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	static public void writeToFile() {
		String toWrite = "Ticket validated in ";
		toWrite += "\n";
		try {
			fileOutStream.write(toWrite.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	static public void getFileStream() {
		File directory = getAlbumStorageDir(directoryName);
		File file = new File(directory, filename);

		fileOutStream = null;
		try {
			fileOutStream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			createFile();
		}
	}
	
	static private File getAlbumStorageDir(String filename) {
		File file = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS),
				filename);
		if (!file.mkdirs()) {
		}
		return file;
	}
}
