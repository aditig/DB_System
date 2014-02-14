import java.io.IOException;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.*;


public class DBProject {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, StandardException {
		// TODO Auto-generated method stub
		
		DBSystem db = new DBSystem();
		
		db.readConfig("config.txt");
		db.populateDBInfo();
		/*db.getRecord("countries", 5);
		db.getRecord("countries", 5);
		db.getRecord("countries", 8);
		db.getRecord("countries", 18);
		db.getRecord("countries", 28);
		db.getRecord("countries", 1);
		db.getRecord("countries", 8);
		db.getRecord("countries", 5);
		db.insertRecord("countries", "record");*/
        
        db.queryType("CREATE TABLE abc (a VARCHAR(5), b INTEGER, c FLOAT)");
        db.queryType("SELECT a from abc where b > 0 having (c < 5 and c > 3) or b = 2");
        
	}


}
