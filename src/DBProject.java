import java.io.IOException;


public class DBProject {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		DBSystem db = new DBSystem();
		
		db.readConfig("config.txt");
		db.populateDBInfo();
		db.getRecord("countries", 5);
		db.getRecord("countries", 5);
		db.getRecord("countries", 8);
		db.getRecord("countries", 18);
		db.getRecord("countries", 28);
		db.getRecord("countries", 1);
		db.getRecord("countries", 8);
		db.getRecord("countries", 5);
		db.insertRecord("countries", "record");
	}

}
