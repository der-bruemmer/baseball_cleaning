import java.awt.BufferCapabilities;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ProjectOxfordQuery {
	
	public ProjectOxfordQuery() {
		
	}
	
	private List<String> getFileNames(String inFile, BufferedWriter errors) {
		List<String> filenames = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inFile));
			String line = "";
			reader.readLine();
			while((line = reader.readLine())!=null) {
				String[] fields = line.split("\t");
				if(!fields[2].trim().isEmpty())
					filenames.add(fields[2]);
				else {
					errors.append("No picture: "+line);
					errors.newLine();
					errors.flush();
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collections.sort(filenames);
		return filenames;
	}
	
	public String queryPicture(File imageFile, String apiKey, BufferedWriter errors) {
		
		HttpClient httpclient = HttpClients.createDefault();
		String data = "";
        try
        {
            URIBuilder builder = new URIBuilder("https://api.projectoxford.ai/emotion/v1.0/recognize");

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            //header 
            request.addHeader("Content-Type", "application/octet-stream");
            request.addHeader("Ocp-Apim-Subscription-Key", apiKey);
            
            // Request body
//            StringEntity reqEntity = new StringEntity("{body}");
            FileEntity reqEntity = new FileEntity(imageFile);
            request.setEntity(reqEntity);
           
            
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) 
            {
                data = EntityUtils.toString(entity);
            }
        }
        catch (Exception e)
        {
        	System.out.println("Error retrieving: "+imageFile.getName());
            System.out.println(e.getMessage());
            try {
            	errors.append("Error retrieving: "+imageFile.getName()+e.getMessage().replace("\n", " ") );
				errors.newLine();
				errors.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        return data;
	}
		
	public String parseJson(String fileName,String data) throws Exception {
		String line = "";
		JsonArray jsonArray = new JsonParser().parse(data).getAsJsonArray();
		JsonObject scores = jsonArray.get(0).getAsJsonObject().get("scores").getAsJsonObject(); 
		line += fileName + "\t" + scores.get("anger").toString() + 
				"\t" + scores.get("contempt").toString() +
				"\t" + scores.get("disgust").toString() + 
				"\t" + scores.get("fear").toString() + 
				"\t" + scores.get("happiness").toString() + 
				"\t" + scores.get("neutral").toString() + 
				"\t" + scores.get("sadness").toString() + 
				"\t" + scores.get("surprise").toString() +"\n";
		return line;
	}
	
	public static void main(String[] args) {
		String apiKey = args[0];
		String inFile = args[1];
		String inFolder = args[2];
		String outFile = args[3];
		
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
			BufferedWriter errors = new BufferedWriter(new FileWriter("./error.log"));
			ProjectOxfordQuery query = new ProjectOxfordQuery();
			List<String> fileNames = query.getFileNames(inFile,errors);
			writer.append("filename\tanger\tcontempt\tdisgust\tfear\thappiness\tneutral\tsadness\tsurprise\n");
			int count = 0;
			for(String filename : fileNames) {
				count ++;
				if(count<9000) {
					File picture = new File(inFolder+filename);
					System.out.println(count+" "+picture);
					String data = query.queryPicture(picture, apiKey, errors);
					try {
						writer.append(query.parseJson(filename, data));
						writer.flush();
					} catch (Exception e) {
						System.out.println("Error parsing: "+filename);
						errors.append(filename+"\t"+data);
						errors.newLine();
						errors.flush();
					}
					Thread.sleep(3200);
				} else {
					break;
				}
				
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
