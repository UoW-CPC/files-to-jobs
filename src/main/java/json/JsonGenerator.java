package json;

import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.google.gson.*;

public class JsonGenerator {
	@SuppressWarnings("unchecked")
	 public static void main( String[] args )
	    {
			String input_folder = args[0]; //input folder, "/Users/dangh/eclipse-workspace/muscle-container/data/";
			String output_fname = args[1]; //output file; "muscle_jobs_output.json";
			//System.out.println("args:" + args[0] + "," + args[1]);
			//File folder = new File(base_folder+"input");
			File folder = new File(input_folder);
			int step = 1; //the number of files for each job
			File[] listOfFiles = folder.listFiles(new FilenameFilter() {
		        @Override
		        public boolean accept(File dir, String name) {
		            return !name.equals(".DS_Store");
		        }
		    }); //set filter to avoid ".DS_Store" file
			int noFile = listOfFiles.length;
			System.out.println("Number of files: " + noFile);
			
			
			List<String> cmdList = new ArrayList<String>();
			cmdList.add("./app/execute.sh");
	        
	        JSONArray jobList = new JSONArray();

	        int count=0;
			for (int i = 0; i < listOfFiles.length; i=i+step) {
				List<String> sList = new ArrayList<String>();
				//sList.add(Integer.toString(step)); //number of files for each step
				int k = i;
		        JSONArray taskList = new JSONArray();
				while(k < i + step && k < noFile) {
				//for (int j=0; j<step; j++) {
					//System.out.println(k + "\n");
					if (listOfFiles[k].isFile()) {
						  String fname = listOfFiles[k].getName();
						  char slash = 92;
						  String replacement = String.format("%c ",slash);
						 // System.out.println(charSeq);
						  String new_fname = fname.replace(" ",replacement);
						  //System.out.println(new_fname);
					      sList.add("../data/input/"+new_fname);
					}	
					k = k+1;
				}
				//System.out.println("number of files" + sList.size());
				/*
				if(k < noFile) {
					sList.add(0,Integer.toString(step));
				}
				else {
					if(noFile%step!=0) {
						sList.add(0,Integer.toString(k%step));
					}
					else {
						sList.add(0,Integer.toString(step));
					}
				}*/
				
				JSONObject task = new JSONObject();
				task.put("data",sList);
				task.put("id","task_id_0");
				task.put("command",cmdList);
				//System.out.println(task);
				taskList.add(task);
				
				JSONObject job = new JSONObject();
				job.put("data", new JSONArray());
				job.put("params",new JSONArray());
				String job_id = "job_id_" + Integer.toString(count);
				count++;
				job.put("id", job_id);
				job.put("command",new JSONArray());
				job.put("tasks",taskList);
			      
				jobList.add(job);
			}
	        
	       
	        
	       /*
	        
	        JSONObject task2 = new JSONObject();
	        List<String> sList2 = new ArrayList<String>();
	        sList2.add("2.txt");
	        task2.put("data",sList2);
	        task2.put("id","task_id_0");
	        task2.put("command",cmdList);
	        
	        JSONObject job2 = new JSONObject();
	        job2.put("data", new JSONArray());
	        job2.put("params",new JSONArray());
	        job2.put("id", "job_id_1");
	        job2.put("command",cmdList);
	        job2.put("tasks",task2);*/
	        

	        
	        
	        JSONObject jobsDescription = new JSONObject();
	        jobsDescription.put("container_name", "worker");
	        jobsDescription.put("experiment_deadline", 3895);
	        jobsDescription.put("single_task_duration", 60);
	        jobsDescription.put("params",new JSONArray());
	        jobsDescription.put("command",new JSONArray());
	        jobsDescription.put("jobs",jobList);
	         
	        //Write JSON file
	        try (FileWriter file = new FileWriter(output_fname)) {
	 
	            //file.write(jobsDescription.toJSONString());
	        	//file.write(jobsDescription.toJSONString(4));
	        	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        	String jsonOutput = gson.toJson(jobsDescription);
	        	file.write(jsonOutput);
	            file.flush();
	 
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
}
