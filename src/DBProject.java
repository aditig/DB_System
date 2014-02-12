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
		/*db.populateDBInfo();
		db.getRecord("countries", 5);
		db.getRecord("countries", 5);
		db.getRecord("countries", 8);
		db.getRecord("countries", 18);
		db.getRecord("countries", 28);
		db.getRecord("countries", 1);
		db.getRecord("countries", 8);
		db.getRecord("countries", 5);
		db.insertRecord("countries", "record");*/
		
		SQLParser parser = new SQLParser();
        StatementNode stmt = parser.parseStatement("select a from b where a > 4");
        //System.out.println(stmt.toString());
        /*FromSubquery f = new FromSubquery();
        HasNodeVisitor h = new HasNodeVisitor(f.getClass());
        h.visit(stmt);
        System.out.println(h.hasNode());*/
        //stmt.treePrint();
        stmt.accept(new QueryTreeVisitor());
        System.out.println();
        
	}


}
