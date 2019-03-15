import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			String aggregateFileDir = null;
			String sCurrentLine;
		File file2 = new File("/Users/venkatakrishnamohansunkara/Desktop/Data_Mining/project/Gdelt_data_all");
		File[] listofFiles = file2.listFiles();
		for(int i=0;i<listofFiles.length;i++) {
			File file1 = listofFiles[i];
		BufferedReader br = new BufferedReader(new FileReader(file1));
		System.out.println(file1.getName());
		String name = file1.getName();
		aggregateFileDir = "/Users/venkatakrishnamohansunkara/Desktop/Data_Mining/project/new_gdelt/" + name;
		PrintWriter writer = new PrintWriter(new FileWriter(aggregateFileDir));
		while ((sCurrentLine = br.readLine()) != null) {
			 String[] attr = sCurrentLine.split(",");
			 if(attr.length<13) {
				 continue;
			 }
			 writer.println(sCurrentLine);
		}
		br.close();
		writer.close();
		}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

}
