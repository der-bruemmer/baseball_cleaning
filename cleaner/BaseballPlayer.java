import java.util.Date;

public class BaseballPlayer {

	private String team = "";
	private String URI = "";
	private String nickname = "";
	private String fullname = "";
	private String birthdateString = "";
	private String birthplace = "";
	private String deathdateString = "";
	private String deathplace = "";
	private String age = "";
	private String position = "";
	private String height = "";
	private String weight = "";
	private String image = "";
	private String college = "";
	private boolean hasCollege = false;
	private String debut = "";
	private String lastGame = "";
	
	public BaseballPlayer() {
		
	}
	
	

	public String getTeam() {
		return team;
	}



	public void setTeam(String team) {
		this.team = team;
	}



	public String getURI() {
		return URI;
	}

	public void setURI(String uRI) {
		URI = uRI;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getBirthdateString() {
		return birthdateString;
	}

	public void setBirthdateString(String birthdate) {
		this.birthdateString = birthdate;
	}

	public String getBirthplace() {
		return birthplace;
	}

	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}

	public String getDeathdateString() {
		return deathdateString;
	}

	public void setDeathdateString(String deathdate) {
		this.deathdateString = deathdate;
	}

	public String getDeathplace() {
		return deathplace;
	}

	public void setDeathplace(String deathplace) {
		this.deathplace = deathplace;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public boolean isHasCollege() {
		return hasCollege;
	}

	public void setHasCollege(boolean hasCollege) {
		this.hasCollege = hasCollege;
	}

	public String getDebut() {
		return debut;
	}

	public void setDebut(String debut) {
		this.debut = debut;
	}

	public String getLastGame() {
		return lastGame;
	}

	public void setLastGame(String lastGame) {
		this.lastGame = lastGame;
	}

	public String toString() {
		String player = "";
		player += "Nick: "+nickname+"\n";
		player += "Fullname: "+fullname+"\n";
		player += "URI: "+URI+"\n";
		player += "birthdate: "+birthdateString+"\n";
		player += "birthplace: "+birthplace+"\n";
		player += "deathdate: "+deathdateString+"\n";
		player += "deathplace: "+deathplace+"\n";
		player += "age: "+age+"\n";
		player += "position: "+position+"\n";
		player += "height: "+height+"\n";
		player += "weight: "+weight+"\n";
		player += "image: "+image+"\n";
		player += "college: "+college+"\n";
		player += "hascollege: "+hasCollege+"\n";
		player += "debut: "+debut+"\n";
		player += "last game: "+lastGame+"\n";
		return player;
	}
	
}
