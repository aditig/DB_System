import java.util.ArrayList;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.*;


public class QueryTreeVisitor implements Visitor {
	private QueryTreeNode node;
	private CursorNode cNode;
	private ArrayList<String> fromTables = new ArrayList<String>();
	private ArrayList<String> columns = new ArrayList<String>();
	private ArrayList<String> orderColumns = new ArrayList<String>();
	private ArrayList<String> groupColumns = new ArrayList<String>();
	private String queryType;
	private boolean isDistinct;
	
	@Override
    public Visitable visit(Visitable visitable) throws StandardException {
        node = (QueryTreeNode)visitable;

        if(node.getNodeType() == NodeTypes.CURSOR_NODE) {
        	cNode = (CursorNode)node;
        	//c.treePrint();
        	queryType = cNode.statementToString();
        	//System.out.println("Querytype: " + queryType);
        	if(queryType.equals("SELECT")) {
        		processSelect((SelectNode)cNode.getResultSetNode());
        	}
        }
        return visitable;
    }
	
	//TODO write a different method for printing
	private void processSelect(SelectNode selNode) throws StandardException {
		FromList fList = selNode.getFromList();
		isDistinct = selNode.isDistinct();
		
		for (FromTable t : fList) {
			fromTables.add(t.getTableName().getTableName());
		}
		
		//TODO select *
		//TODO null condition
		
		ResultColumnList colList = selNode.getResultColumns();
		//r.treePrint();
		//System.out.print("Columns: ");
		for (ResultColumn col : colList) {
			columns.add(col.getName());
		}
		
		ValueNode have = selNode.getHavingClause();
		if(have != null) {
			//have.treePrint();
			//System.out.println(have.toString());
		}
		
		OrderByList orderList = cNode.getOrderByList();
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
	}
	
	public void printQuery() {
		if(queryType.equals("SELECT")) {
			System.out.println("Querytype: " + queryType);
		}
		
		boolean first = true;
		System.out.print("Tables: ");
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
		System.out.print("Columns: ");
		for (String s : columns) {
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
		System.out.print("Distinct: ");
		if(!isDistinct) {
			System.out.println("NA");
		}
		else {
			for (String s : columns) {
				if (first) {
					first = false;
				}
				else {
					System.out.print(", ");
				}
				System.out.print(s);
			}
			System.out.println();
		}
		
		first = true;
		System.out.print("Order By: ");
		if (orderColumns.size() == 0) {
			System.out.println("NA");
		}
		else {
			for (String s : columns) {
				if (first) {
					first = false;
				}
				else {
					System.out.print(", ");
				}
				System.out.print(s);
			}
			System.out.println();
		}
		
		first = true;
		System.out.print("Group By: ");
		if (groupColumns.size() == 0) {
			System.out.println("NA");
		}
		else {
			for (String s : columns) {
				if (first) {
					first = false;
				}
				else {
					System.out.print(", ");
				}
				System.out.print(s);
			}
			System.out.println();
		}
	}
	
    @Override
    public boolean visitChildrenFirst(Visitable node) {
        return false;
    }

    @Override
    public boolean stopTraversal() {
        return false;
    }

    @Override
    public boolean skipChildren(Visitable node) throws StandardException {
        return false;
    }

}
