import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BaseballCleaner {
	
	private File pictureFolder = null;
	private File outFile = null;
	private File csv = null;
	
	public BaseballCleaner(String pictureFolder, String csvFile, String outFile) {
		this.pictureFolder = new File(pictureFolder);
		this.outFile = new File(outFile);
		this.csv = new File(csvFile);
	}
	
	
	public ArrayList<BaseballPlayer> readCSV() {
		
		ArrayList<BaseballPlayer> csvmap=new ArrayList<BaseballPlayer>();
		
		try {
			InputStream input=new FileInputStream(csv);
			Charset charset=Charset.forName("UTF-8");
			CsvReader reader=new CsvReader(input,",".charAt(0),charset);
			reader.setTextQualifier("\"".charAt(0));
			reader.setUseTextQualifier(true);
			reader.skipRecord();
			while(reader.readRecord()) {
				String[] s = reader.getValues();
				BaseballPlayer player = parseRecord(s);
				csvmap.add(player);
			}
		} catch(UnsupportedEncodingException e) {
			//this doesn't happen
			e.printStackTrace();
		} catch(FileNotFoundException e) {
			System.out.println("Cannot read file: " + csv.getAbsolutePath());
		} catch(IOException e) {
			System.out.println("Cannot read file: " + csv.getAbsolutePath());
		}
		return csvmap;
	}
	
	private BaseballPlayer parseRecord(String[] record) {
		BaseballPlayer player = new BaseballPlayer();
		player.setNickname(record[8]);
		player.setURI(record[5]);
		System.out.println(record[8]);
		
		player.setTeam(record[2]);
		
		if(!record[6].isEmpty()&&!record[6].equals("null")) {
			String[] birth = parseBirth(record[6]);
			if(birth!=null) {
				player.setBirthdateString(birth[0]);
				player.setBirthplace(birth[1]);
			} else {
				System.out.println("could not parse birth data");
			}
		} 
		
		if(!record[7].isEmpty()&&!record[7].equals("null")) {
			String[] death = parseBirth(record[7]);
			if(death!=null) {
				player.setDeathdateString(death[0]);
				player.setDeathplace(death[1]);
			} else {
				System.out.println("could not parse death data");
			}	
		} 
		
		player.setFullname(record[12]);
		if(!record[10].isEmpty()&&!record[10].equals("null")) {
			player.setImage(record[10]);
		} 
		if(!record[9].isEmpty()&&!record[9].equals("null")) {
			player.setPosition(parsePosition(record[9]));
			//height may contain weight
			String[] bodyData = parseBody(record[9]);
			player.setHeight(bodyData[0]);
			player.setWeight(bodyData[1]);
		}
		if(!record[11].isEmpty()&&!record[11].equals("null")) {
			String college = parseCollege(record[11]);
			if(college.isEmpty()) {
				player.setHasCollege(false);
			} else {
				player.setHasCollege(true);
			}
			player.setCollege(college);
			player.setDebut(parseDebut(record[11]));
			player.setLastGame(parseLastGame(record[11]));
		}
		
		return player;
	}
	
	public ArrayList<BaseballPlayer> renameImages(ArrayList<BaseballPlayer> players) {
		for(BaseballPlayer player : players) {
			String image = player.getImage();
			if(!image.isEmpty()) {
				image = image.substring(image.lastIndexOf("/")+1);
				File imageFile = new File(this.pictureFolder.getAbsolutePath() + "/" +image);
				if(imageFile.exists()) {
					//already renamed
					if(imageFile.getName().startsWith(player.getNickname().replace(" ", "_"))) {
						System.out.println("Already renamed");
						return players;
					}
					String fileName = player.getNickname().replace(" ", "_")+"_"+image;
					imageFile.renameTo(new File(this.pictureFolder.getAbsolutePath() + "/" +fileName));
					player.setImage(fileName);
				} else {
					String filename = player.getNickname().replace(" ", "_")+"_"+image;
					imageFile = new File(this.pictureFolder.getAbsolutePath() + "/" +filename);
					if(imageFile.exists()) {
						player.setImage(filename);
					} else {
						System.out.println("IMAGE NOT FOUND "+image+" "+player.getURI());
					}
				}
			}
		}
		return players;
	}
	
	private String parseDebut(String record) {
		String debut = "";
		if(record.contains("Debut")) {
			String[] lines = record.split("\n");
			for(String line : lines) {
				if(line.startsWith("Debut")) {
					debut = line.substring(line.indexOf(":")+1).trim();
					if(debut.contains("(")) {
						debut = debut.substring(0,debut.indexOf("(")).trim();
					}						
					return debut;
				}
				
			}
		}
		return debut;
	}
	
	private String parseLastGame(String record) {
		String last = "";
		if(record.contains("Last Game")) {
			String[] lines = record.split("\n");
			for(String line : lines) {
				if(line.startsWith("Last Game")) {
					last = line.substring(line.indexOf(":")+1).trim();
					if(last.contains("(")) {
						last = last.substring(0,last.indexOf("(")).trim();
					}						
					return last;
				}
				
			}
		}
		return last;
	}
	
	private String parseCollege(String record) {
		String college = "";
		if(record.contains("School")) {
			String[] lines = record.split("\n");
			for(String line : lines) {
				if(line.startsWith("School")) {
					college = line.substring(line.indexOf(":")+1).trim();
					return college;
				}
			}
		}
		return college;
	}
	
	private String[] parseBody(String record) {
		String[] body = new String[2];
		if(record.contains("Height")) {
			String[] lines = record.split("\n");
			for(String line : lines) {
				if(line.contains("Height")) {
					String height = line.substring(line.indexOf(":")+1).trim();
					if(height.contains("Weight")) {
						int weightStart = height.indexOf("Weight");
						body[0] = height.substring(0,weightStart).trim();
						String weight = height.substring(weightStart+7).trim();
						body[1] = weight;
					} else {
						body[0] = height;
						body[1] = null;
					}
					return body;
				}
			}
		}
		return body;
	}
	
	private String convertHeight(String heightInImp) {
		
		int feet = Integer.parseInt(heightInImp.substring(0,heightInImp.indexOf("'")).trim());
		int inch = Integer.parseInt(heightInImp.substring(heightInImp.indexOf("'")+1,heightInImp.indexOf("\"")).trim());
		feet = feet*12;
		inch += feet;
		BigDecimal bd = new BigDecimal(inch*2.54);
		BigDecimal rounded = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
	    Double cm = rounded.doubleValue();
		return cm.toString();
	}
	
	private String convertWeight(String weightInImp) {
		
		int lbs = Integer.parseInt(weightInImp.substring(0,weightInImp.indexOf(" ")).trim());
		BigDecimal bd = new BigDecimal(lbs/2.20462262);
		BigDecimal rounded = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
	    Double kg = rounded.doubleValue();
		return kg.toString();
	}
	
	private String parsePosition(String record) {
		String position = "";
		if(record.contains("Position")) {
			String[] lines = record.split("\n");
			for(String line : lines) {
				if(line.contains("Position")) {
					position = line.substring(line.indexOf(":")+1).trim();
					return position;
				}
			}
		}
		return position;
	}
	
	private String[] parseBirth(String birth) {
		String[] birthdata = new String[2];
		if(birth.contains("Born")||birth.contains("Died")) {
			//date and 
			String date = "";
			String place = "";
			if(birth.contains("in")) {
				date = birth.substring(birth.indexOf(":")+1,birth.indexOf("in")).trim();
				place = birth.substring(birth.indexOf("in")+2).trim();
			} else {
				date = birth.substring(birth.indexOf(":")+1).trim();
			}
			
			if(date.contains("(")) {
				date = date.substring(0,date.indexOf("(")).trim();
			}
			if(place.contains("(")) {
				place = place.substring(0,place.indexOf("(")).trim();
			}
			birthdata[0] = date;
			birthdata[1] = place;
		} else {
			return null;
		}		
		return birthdata;
	}
	
	public void printCsv(ArrayList<BaseballPlayer> players) {
		try {
			SimpleDateFormat yearformat = new SimpleDateFormat("yyyy");
			SimpleDateFormat monthformat = new SimpleDateFormat("MM");
			SimpleDateFormat dayformat = new SimpleDateFormat("dd");
			BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
			writer.append("fullname\tnickname\timage\tteam\thasCollege\tcollege\t"
					+ "birthYear\tbirthMonth\tbirthDay\tbirthPlace\t"
					+ "deathYear\tdeathMonth\tdeathDay\tdeathPlace\t"
					+ "height(cm)\tweight(kg)\tposition\tdebutyear\tdebutmonth\tdebutday\tlastgameyear\tlastgamemonth\tlastgameday\tsource\n");
			for(BaseballPlayer player : players) {
				writer.append(player.getFullname()+"\t");
				writer.append(player.getNickname()+"\t");
				writer.append(player.getImage()+"\t");
				writer.append(player.getTeam()+"\t");
				if(player.isHasCollege())
					writer.append(String.valueOf(1)+"\t");
				else
					writer.append(String.valueOf(0)+"\t");
				writer.append(player.getCollege()+"\t");
				Date birth = formatDate(player.getBirthdateString());
				if(birth==null) {
					writer.append("-1\t-1\t-1\t");
				} else {
					writer.append(yearformat.format(birth)+"\t");
					writer.append(monthformat.format(birth)+"\t");
					writer.append(dayformat.format(birth)+"\t");
				}
				writer.append(player.getBirthplace()+"\t");
				Date death = formatDate(player.getDeathdateString());
				if(death==null) {
					writer.append("-1\t-1\t-1\t");
				} else {
					writer.append(yearformat.format(death)+"\t");
					writer.append(monthformat.format(death)+"\t");
					writer.append(dayformat.format(death)+"\t");
				}
				writer.append(player.getDeathplace()+"\t");
				writer.append(convertHeight(player.getHeight())+"\t");
				writer.append(convertWeight(player.getWeight())+"\t");
				writer.append(player.getPosition()+"\t");
//				writer.append(player.getDebut()+"\t");
				Date debut = formatDate(player.getDebut());
				if(debut==null) {
					writer.append("-1\t-1\t-1\t");
				} else {
					writer.append(yearformat.format(debut)+"\t");
					writer.append(monthformat.format(debut)+"\t");
					writer.append(dayformat.format(debut)+"\t");
				}
//				writer.append(player.getLastGame()+"\t");
				Date lastGame = formatDate(player.getLastGame());
				if(lastGame==null) {
					writer.append("-1\t-1\t-1\t");
				} else {
					writer.append(yearformat.format(lastGame)+"\t");
					writer.append(monthformat.format(lastGame)+"\t");
					writer.append(dayformat.format(lastGame)+"\t");
				}
				writer.append(player.getURI()+"\n");
				writer.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private Date formatDate(String dateString) {
		Date date = null;
		if(dateString.isEmpty())
			return date;
		SimpleDateFormat formatter = new SimpleDateFormat("MMMMM  dd, yyyy");
		try {
			date = formatter.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			
			formatter = new SimpleDateFormat("MMMMM dd, yyyy");
			try {
				date = formatter.parse(dateString);
			} catch (ParseException e2) {
				System.out.println("Could not parse "+dateString);
			}	
		}
		return date;
	}
	
	
	public static void main(String[] args) {
		String pictureFolder = args[0];
		String csvFile = args[1];
		String outFile = args[2];
		BaseballCleaner cleaner = new BaseballCleaner(pictureFolder, csvFile, outFile);
		ArrayList<BaseballPlayer> players = cleaner.readCSV();
		players = cleaner.renameImages(players);
		cleaner.printCsv(players);
	}
}
