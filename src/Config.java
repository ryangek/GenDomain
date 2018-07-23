import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Config {
	private String url;
	private String username;
	private String password;
	private String[] typeSQL;
	private String[] typeJava;
	
	public Config() {
		File configFile = new File("./config/config.properties");
		try {
		    FileReader reader = new FileReader(configFile);
		    Properties props = new Properties();
		    props.load(reader);
		    
		    this.setUrl(props.getProperty("url"));
		    this.setUsername(props.getProperty("username"));
		    this.setPassword(props.getProperty("password"));
		    this.setTypeSQL(props.getProperty("sql").split(","));
		    this.setTypeJava(props.getProperty("java").split(","));
		    
		    reader.close();
		} catch (FileNotFoundException ex) {
		    // file does not exist
			ex.printStackTrace();
		} catch (IOException ex) {
		    // I/O error
			ex.getStackTrace();
		}
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String[] getTypeSQL() {
		return typeSQL;
	}
	public void setTypeSQL(String[] typeSQL) {
		this.typeSQL = typeSQL;
	}
	public String[] getTypeJava() {
		return typeJava;
	}
	public void setTypeJava(String[] typetypeJava) {
		this.typeJava = typetypeJava;
	}
}
