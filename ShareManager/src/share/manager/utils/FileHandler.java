
package share.manager.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.os.Environment;

public class FileHandler {

	private static final String filename = "portfolio.txt",
			directoryName = "ShareManager", temporaryName = "port2.txt";
	private static File directory = null;

	public static ArrayList<String> readFile() {
		String scan;
		ArrayList<String> ret = new ArrayList<String>();
		directory = getAlbumStorageDir(directoryName);
		File file = new File(directory, filename);

		if (!file.exists()) {
			try {
				file.createNewFile();
			}
			catch (IOException e) {

			}
		}

		FileReader fileReader = null;
		try {
			fileReader = new FileReader(file);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fileReader);

		try {
			while ((scan = br.readLine()) != null)
				ret.add(scan);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		try {
			br.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static void writeToFile(String coded) {
		FileWriter out = null;
		File file = new File(directory, filename);
		coded += "\n";
		try {
			out = new FileWriter(file, true);
			out.write(coded);
			out.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String[] getNames() {
		ArrayList<String> info = readFile(), names = new ArrayList<String>();
		for (String s : info)
			names.add(s.split("\\|")[0]);
		String[] ret = new String[names.size()];
		return names.toArray(ret);
	}

	public static ArrayList<String> getNamesString() {
		ArrayList<String> info = readFile(), names = new ArrayList<String>();
		for (String s : info)
			names.add(s.split("\\|")[0]);
		return names;
	}

	public static String[] getRegions() {
		ArrayList<String> info = readFile(), names = new ArrayList<String>();
		for (String s : info)
			names.add(s.split("\\|")[2]);
		String[] ret = new String[names.size()];
		return names.toArray(ret);
	}

	public static String[] getShares() {
		ArrayList<String> info = readFile(), names = new ArrayList<String>();
		for (String s : info)
			names.add(s.split("\\|")[3]);
		String[] ret = new String[names.size()];
		return names.toArray(ret);
	}

	public static String[] getTicks() {
		ArrayList<String> info = readFile(), names = new ArrayList<String>();
		for (String s : info)
			names.add(s.split("\\|")[1]);
		String[] ret = new String[names.size()];
		return names.toArray(ret);
	}

	public static ArrayList<Float> getNumeratedShares() {
		ArrayList<String> info = readFile();
		ArrayList<Float> numShares = new ArrayList<Float>();
		for (String s : info)
			numShares.add(Float.parseFloat(s.split("\\|")[3]));
		return numShares;
	}

	public static ArrayList<Integer> getSharePercentages() {
		ArrayList<String> info = readFile();
		ArrayList<Integer> totals = new ArrayList<Integer>();
		float total = getSharesSum();

		for (String s : info)
			totals
					.add(Math.round((Float.parseFloat(s.split("\\|")[3]) / total) * 100));

		return totals;
	}

	public static void changeShare(String tick, boolean operation) {
		ArrayList<String> info = readFile();
		FileWriter out = null;
		File file = new File(directory, temporaryName);
		File oldFile = new File(directory, filename);

		try {
			out = new FileWriter(file, true);
			for (String s : info) {
				String[] split = s.split("\\|");
				if (split[1].equals(tick)) {
					int newShares = operation ? Integer.parseInt(split[3]) + 1 : Integer
							.parseInt(split[3]) - 1;
					out.write(split[0] + "|" + split[1] + "|" + split[2] + "|"
							+ newShares + "\n");
				}
				else out.write(s + "\n");
			}
			out.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		oldFile.delete();
		file.renameTo(oldFile);
	}

	public static String getTickFromName(String name) {
		ArrayList<String> info = readFile();
		String ret = "";

		for (String s : info) {
			String[] infoSplit = s.split("\\|");
			if (infoSplit[0].equals(name)) {
				ret = infoSplit[1];
				break;
			}
		}

		return ret;
	}

	public static String getInfoFromTick(String tick) {
		ArrayList<String> info = readFile();
		String ret = "";

		for (String s : info) {
			String[] infoSplit = s.split("\\|");
			if (infoSplit[1].equals(tick)) {
				ret = s;
				break;
			}
		}

		return ret;
	}

	public static String getSharesFromTick(String tick) {
		ArrayList<String> info = readFile();
		String ret = "";

		for (String s : info) {
			String[] infoSplit = s.split("\\|");
			if (infoSplit[1].equals(tick)) {
				ret = infoSplit[3];
				break;
			}
		}

		return ret;
	}

	private static File getAlbumStorageDir(String filename) {
		File file = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS),
				filename);
		if (!file.mkdirs()) {

		}
		return file;
	}

	private static float getSharesSum() {
		ArrayList<String> info = readFile();
		float ret = 0.0f;
		for (String s : info)
			ret += Float.parseFloat(s.split("\\|")[3]);
		return ret;
	}
}
