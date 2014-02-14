import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.ColumnDefinitionNode;
import com.foundationdb.sql.parser.CreateTableNode;
import com.foundationdb.sql.parser.CursorNode;
import com.foundationdb.sql.parser.FromList;
import com.foundationdb.sql.parser.FromTable;
import com.foundationdb.sql.parser.GroupByColumn;
import com.foundationdb.sql.parser.GroupByList;
import com.foundationdb.sql.parser.OrderByColumn;
import com.foundationdb.sql.parser.OrderByList;
import com.foundationdb.sql.parser.QueryTreeNode;
import com.foundationdb.sql.parser.ResultColumn;
import com.foundationdb.sql.parser.ResultColumnList;
import com.foundationdb.sql.parser.SQLParser;
import com.foundationdb.sql.parser.SelectNode;
import com.foundationdb.sql.parser.StatementNode;
import com.foundationdb.sql.parser.TableElementList;
import com.foundationdb.sql.parser.TableElementNode;
import com.foundationdb.sql.parser.ValueNode;


public class DBSystem {
	private int pageSize, numPages;
	private String path;
	private ArrayList<Table> tables = new ArrayList<Table>();	
	private MainMemory m;
	
	public void readConfig(String configFilePath) {
		File file = new File(configFilePath);
		Scanner sc = null;
		String colName;
		
		try {
			sc = new Scanner(file);
			//TODO PAGESIZE or PAGE_SIZE??
			sc.next("PAGE_SIZE");
			pageSize = sc.nextInt();
			
			sc.next("NUM_PAGES");
			numPages = sc.nextInt();

			sc.next("PATH_FOR_DATA");
			path = sc.next();
			
			while(sc.hasNext("BEGIN")) {
				sc.next("BEGIN");
				Table t = new Table(sc.next());
				tables.add(t);
				while(!(sc.hasNext("END"))){
					colName = sc.next();
					//System.out.println(colName.substring(0,colName.length()-1));
					//TODO create <table>.data file
					t.addAttr(new Attribute(colName.substring(0, colName.length() - 1), sc.next()));
				}
				sc.next("END");
			}
			
			m = new MainMemory(numPages);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			if (sc !=  null) {
				sc.close();
			}
		}
	}

	public void populateDBInfo() {
		File file;
		Scanner sc = null;
		int size, start, count;
		String l;
		
		for (Table t : tables) {
			size = 0;
			start = 0;
			count = 0;
			file = new File(t.getName() + ".csv");
			//TODO add path
			//file = new File(path + t.getName() + ".csv");
			try {
				sc = new Scanner(file);
				while(sc.hasNextLine()) {
					l = sc.nextLine();
					if(count == 0 && l.length() > 0)
						t.addPage(start, pageSize);
					if(l.length() + size < pageSize) {
						size += l.length();
						t.getLastPage().addRecord(l);
					}
					else {
						//new page for table
						t.addPage(count, pageSize);
						t.getLastPage().addRecord(l);
						start = count;
						size = l.length();
					}
					count++;
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
		}
	}

	
	public String getRecord (String tableName, int recordId) {
		int index = -1;
		for (Table t : tables) {
			if (t.getName().equals(tableName)) {
				index = tables.indexOf(t);
				break;
			}
		}
		
		Page pg = tables.get(index).getPage(recordId);
		//System.out.println(pg.getTableName() + " " + pg.getStartId());
		System.out.println(m.getPage(pg));
		return pg.getRecord(recordId);
	}
	
	
	public void insertRecord(String tableName, String record) throws IOException {
		int index = -1;
		Page pg;
		for (Table t : tables) {
			if(t.getName().equals(tableName)) {
				index = tables.indexOf(t);
				break;
			}
		}
		
		pg = tables.get(index).getLastPage();
		if(record.length() >= pg.getFreeSpace()) {
			tables.get(index).addPage(pg.getEndId() + 1, pageSize);
			pg = tables.get(index).getLastPage();
		}
		m.getPage(pg);
		pg.addRecord(record);
		FileWriter f = new FileWriter(path + tableName + ".csv",true);
        f.append(record + '\n');
        f.close();
	}
	
	public void queryType (String query) {
		String s[] = query.trim().split("\\s+");
		if (s[0].equalsIgnoreCase("CREATE")) {
			createCommand(query);
		} else if (s[0].equalsIgnoreCase("SELECT")) {
			selectCommand(query);
		}
	}
	
	public void createCommand (String query) {
		SQLParser parser = new SQLParser();
        StatementNode node;
		try {
			node = parser.parseStatement(query);
			CreateTableNode createNode = (CreateTableNode) node;
			
			TableElementList elem = createNode.getTableElementList();
			
			//TODO check if table already exists
			if (isTable (createNode.getFullName())) {
				System.out.println("Query Invalid");
			} else {
				Table table = new Table(createNode.getFullName());
				System.out.println("Querytype:create\nTablename:" + table.getName());
				
				boolean isFirst = true;
				System.out.print("Attributes:");
				for (TableElementNode t : elem) {
					if(isFirst) {
						isFirst = false;
					} else {
						System.out.print(",");
					}
					ColumnDefinitionNode c = (ColumnDefinitionNode) t;
					table.addAttr(new Attribute (c.getName(), c.getType().getSQLstring()));
					
					System.out.print(c.getName() + " " + c.getType().getSQLstring());
				}
				System.out.println("\n");
				tables.add(table);
			}
			
		} catch (StandardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void selectCommand (String query) {
		SQLParser parser = new SQLParser();
        StatementNode node;
		try {
			node = parser.parseStatement(query);
		
	    	ArrayList<String> fromTables = new ArrayList<String>();
	    	ArrayList<String> columns = new ArrayList<String>();
	    	ArrayList<String> orderColumns = new ArrayList<String>();
	    	ArrayList<String> groupColumns = new ArrayList<String>();
	    	
	    	SelectNode selNode = (SelectNode) ((CursorNode)node).getResultSetNode();
	    	
	    	FromList fList = selNode.getFromList();
			boolean isDistinct = selNode.isDistinct();
			
			for (FromTable t : fList) {
				fromTables.add(t.getTableName().getTableName());
			}
			//TODO select *
			//TODO null condition
			
			ResultColumnList colList = selNode.getResultColumns();
			
			for (ResultColumn col : colList) {
				columns.add(col.getName());
			}
			
			ValueNode have = selNode.getHavingClause();
			if(have != null) {
				//have.treePrint();
				//System.out.println(have.toString());
			}
			
			OrderByList orderList = ((CursorNode)node).getOrderByList();
			if(orderList != null) {
				for (OrderByColumn col : orderList) {
					orderColumns.add(col.getExpression().getColumnName());
				}
			}
			
			GroupByList groupList = selNode.getGroupByList();
			if(groupList != null) {
				for(GroupByColumn col : groupList) {
					groupColumns.add(col.getColumnName());
				}
			}
			
			//TODO validation here
			boolean valid = true;
			for (String t : fromTables) {
				if (!isTable(t)) {
					valid = false;
					break;
				}
			}
			//System.out.println("table check " + valid);
			if (valid) {
				valid = areColumns(fromTables, columns);
				//System.out.println("column check " + valid);
			}
			if (valid) {
				valid = areColumns(fromTables, orderColumns);
				//System.out.println("orderby check " + valid);
			}
			if (!valid) {
				System.out.println("Query Invalid");
			} else {
				//PRINT QUERY TOKENS
				System.out.print("Querytype:select\nTablename:");
				boolean first = true;
				for (String s : fromTables) {
					if (first) {
						first = false;
					}
					else {
						System.out.print(", ");
					}
					System.out.print(s);
				}
				System.out.println();
				
				first = true;
				System.out.print("Columns:");
				for (String s : columns) {
					if (first) {
						first = false;
					}
					else {
						System.out.print(",");
					}
					System.out.print(s);
				}
				System.out.println();
				
				first = true;
				System.out.print("Distinct:");
				if(!isDistinct) {
					System.out.println("NA");
				}
				else {
					for (String s : columns) {
						if (first) {
							first = false;
						}
						else {
							System.out.print(",");
						}
						System.out.print(s);
					}
					System.out.println();
				}
				
				//TODO print condition
				
				first = true;
				System.out.print("Orderby:");
				if (orderColumns.size() == 0) {
					System.out.println("NA");
				}
				else {
					for (String s : columns) {
						if (first) {
							first = false;
						}
						else {
							System.out.print(",");
						}
						System.out.print(s);
					}
					System.out.println();
				}
				
				first = true;
				System.out.print("Groupby:");
				if (groupColumns.size() == 0) {
					System.out.println("NA");
				}
				else {
					for (String s : columns) {
						if (first) {
							first = false;
						}
						else {
							System.out.print(",");
						}
						System.out.print(s);
					}
					System.out.println();
					
				}
	
				//TODO print having
				System.out.println();
			}
		} catch (StandardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addTable(Table t) {
		tables.add(t);
	}

	public boolean isTable(String name) {
		for (Table t : tables) {
			if (t.getName().equals(name))
				return true;
		}
		return false;
	}
	
	public boolean areColumns(ArrayList<String> tbls, ArrayList<String> col) {
		for (String c : col) {
			for (String tb : tbls) {
				int index = -1;
				for (Table t : tables) {
					if (t.getName().equals(tb)) {
						index = tables.indexOf(t);
						break;
					}
				}
				if(!tables.get(index).isColumn(c)) {
					return false;
				}
			}
		}
		return true;
	}
}

