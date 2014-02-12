import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.*;


public class QueryTreeVisitor implements Visitor {
	@Override
    public Visitable visit(Visitable visitable) throws StandardException {
        QueryTreeNode node = (QueryTreeNode)visitable;


        if(node.getNodeType() == NodeTypes.CURSOR_NODE) {
        	CursorNode c = (CursorNode)node;
        	//c.treePrint();
        	System.out.println("Querytype: " + c.statementToString());
        	if(c.statementToString().equals("SELECT")) {
        		SelectNode r = (SelectNode)c.getResultSetNode();
        		
        		FromList fList = r.getFromList();
        		System.out.print("Tables: ");
        		boolean first = true;
        		for (FromTable t : fList) {
        			if (first) {
        				first = false;
        			}
        			else {
        				System.out.print(", ");
        			}
        			System.out.print(t.getTableName());
        		}
        		System.out.println();
        		
        		//TODO select *
        		//TODO null condition
        		
        		ResultColumnList colList = r.getResultColumns();
        		//r.treePrint();
        		System.out.print("Columns: ");
        		first = true;
        		for (ResultColumn col : colList) {
        			if (first) {
        				first = false;
        			}
        			else {
        				System.out.print(", ");
        			}
        			System.out.print(col.getName());
        		}
        		System.out.println();
        		
        		System.out.print("Distinct: ");
        		if(!r.isDistinct()) {
        			System.out.println("NA");
        		}
        		else {
	        		first = true;
	        		for (ResultColumn col : colList) {
	        			if (first) {
	        				first = false;
	        			}
	        			else {
	        				System.out.print(", ");
	        			}
	        			System.out.print(col.getName());
	        		}
	        		System.out.println();
        		}
        		
        		ValueNode have = r.getHavingClause();
        		if(have != null) {
        			//have.treePrint();
        			//System.out.println(have.toString());
        		}
        		
        		OrderByList orderList = c.getOrderByList();
        		System.out.print("OrderBy: ");
        		if (orderList == null) {
        			System.out.println("NA");
        		}
        		else {
	        		first = true;
	        		for (OrderByColumn col : orderList) {
	        			if (first) {
	        				first = false;
	        			}
	        			else {
	        				System.out.print(", ");
	        			}
	        			System.out.print(col.getExpression().getColumnName());
	        		}
	        		System.out.println();
        		}
        		
        		GroupByList groupList = r.getGroupByList();
        		System.out.print("GroupBy: ");
        		if (groupList == null) {
        			System.out.println("NA");
        		}
        		else {
	        		first = true;
	        		for (GroupByColumn col : groupList) {
	        			if (first) {
	        				first = false;
	        			}
	        			else {
	        				System.out.print(", ");
	        			}
	        			System.out.print(col.getColumnName());
	        			
	        		}
	        		System.out.println();
        		}
        		
        	}
        }
        
        return visitable;
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
