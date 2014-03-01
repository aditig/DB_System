import java.io.IOException;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.*;


public class DBProject {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, StandardException {
		// TODO Auto-generated method stub
		Scanner sc = null;
		DBSystem db = new DBSystem();
		
		//System.out.println("in " + args[1]);
		if(args.length < 2)
			System.out.println("Usage: java -cp fdb-sql-parser-1.1.0.jar:. DBProject <config file> <query file>");
		db.readConfig(args[0]);
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
        
		File file = new File(args[1]);
	try {
		sc = new Scanner(file);
		while(sc.hasNextLine()) {
			//System.out.println(sc.nextLine());
			db.queryType(sc.nextLine());
		}
	}
	catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			if (sc !=  null) {
				sc.close();
			}
		}
	/*db.queryType("Create table abc (a varchar(5), b integer, c float)");
        db.queryType("CREATE TABLE xyz (x VARCHAR(5), y INTEGER, z FLOAT)");
        db.queryType("SELECT * from abc,xyz where c > 30 group by b having c = 56 order by y");*/        
	}


}
