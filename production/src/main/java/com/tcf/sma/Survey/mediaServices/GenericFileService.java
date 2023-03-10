package com.tcf.sma.Survey.mediaServices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

public class GenericFileService {


    public static String loadFile(String path) {

        try {
            File temp_file = new File(path);
            if (temp_file.exists()) {

                FileReader fr = new FileReader(path);
                BufferedReader br = new BufferedReader(fr);
                String s = "";
                String temp;
                do {
                    temp = br.readLine();
                    if (temp != null) {
                        s += temp;
                    }
                } while (temp != null);

	/*      while((s += br.readLine()) != null) {
	       System.out.println(s);
	      }*/
                fr.close();
                return s;
            } else return "";

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }


    }

    public String[] listFile(String folder, String ext) {

        //String FILE_DIR = folder;

        GenericExtFilter filter = new GenericExtFilter(ext);

        File dir = new File(folder);

        if (dir.isDirectory() == false) {
            //System.out.println("Directory does not exists : " + FILE_DIR);
            return null;
        }

        // list out all the file name and filter by the extension
        String[] list = dir.list(filter);

        if (list.length == 0) {
            //System.out.println("no files end with : " + ext);

        }

        return list;
/*		for (String file : list) {
			String temp = new StringBuffer(FILE_DIR).append(File.separator)
					.append(file).toString();
			System.out.println("file : " + temp);
		}*/


    }

    public class GenericExtFilter implements FilenameFilter {

        private String ext;

        public GenericExtFilter(String ext) {
            this.ext = ext;
        }

        public boolean accept(File dir, String name) {
            return (name.endsWith(ext));
        }
    }

}
