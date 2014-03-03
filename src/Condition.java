import java.math.BigDecimal;

import com.foundationdb.sql.parser.ConstantNode;


public class Condition {

	private int operator, leftType, rightType;
	private int leftCol, rightCol, leftDataType, rightDataType;
	private float leftNum, rightNum;
	private String leftChar, rightChar;
	public static final int IS_COLUMN = 1;
	public static final int IS_NUMBER = 2;
	public static final int IS_VARCHAR = 3;
	public static final int IS_EQUALS = 4;
	public static final int IS_GREATER_EQUALS = 5;
	public static final int IS_LESS_EQUALS = 6;
	public static final int IS_NOT_EQUALS = 7;
	public static final int IS_GREATER = 8;
	public static final int IS_LESS = 9;
	public static final int IS_LIKE = 10;
	
	public int getOperator() {
		return operator;
	}
	
	public void setOperator(String s) {
		//set appropriate value of operator
		if (s.equals("="))
			operator = IS_EQUALS;
		else if (s.equals("<"))
			operator = IS_LESS;
		else if (s.equals("<="))
			operator = IS_LESS_EQUALS;
		else if (s.equals(">"))
			operator = IS_GREATER;
		else if (s.equals(">="))
			operator = IS_GREATER_EQUALS;
		else if (s.equals("<>"))
			operator = IS_NOT_EQUALS;
		else if (s.equalsIgnoreCase("LIKE"))
			operator = IS_LIKE;
	}
	
	public int getLeftType() {
		return leftType;
	}
	
	public void setLeft(int leftType, int col, int type) {
		this.leftType = leftType;
		leftCol = col;
		leftDataType = type;
		//System.out.println(col);
	}
	
	public void setLeft(int leftType, float num) {
		this.leftType = leftType;
		leftNum = num;
		//System.out.println(num);
	}
	
	public void setLeft(int leftType, String s) {
		this.leftType = leftType;
		leftChar = s;
		//System.out.println(s);
	}
	
	public int getRightType() {
		return rightType;
	}
	
	public void setRight(int rightType, int col, int type) {
		this.rightType = rightType;
		rightCol = col;
		rightDataType = type;
		//System.out.println(col);
	}
	
	public void setRight(int rightType, float num) {
		this.rightType = rightType;
		rightNum = num;
		//System.out.println(num);
	}
	
	public void setRight(int rightType, String s) {
		this.rightType = rightType;
		rightChar = s;
		//System.out.println(s);
	}

	public void setOperator(int type) {
		operator = type;
	}
	
	public boolean evaluate(String record) {
		//do something here
		if (leftType == IS_NUMBER && rightType == IS_NUMBER)
			return checkNum();
		else if (leftType == IS_VARCHAR && rightType == IS_VARCHAR)
			return checkString();
		else if (leftType == IS_COLUMN) {
			String[] attr = record.split(",");
			if (leftDataType == Attribute.FLOAT || leftDataType == Attribute.INTEGER) {
				leftNum = Float.parseFloat(attr[leftCol].substring(1, attr[leftCol].length() - 1));
				if (rightType == IS_COLUMN)
					rightNum = Float.parseFloat(attr[rightCol].substring(1, attr[rightCol].length() - 1));
				return checkNum();
			} else {
				leftChar = attr[leftCol].substring(1, attr[leftCol].length() - 1);
				if (rightType == IS_COLUMN)
					rightChar = attr[rightCol].substring(1, attr[rightCol].length() - 1);
				return checkString();
			}
		} else if (rightType == IS_COLUMN) {
			String[] attr = record.split(",");
			if(rightDataType == Attribute.FLOAT || rightDataType == Attribute.INTEGER) {
				rightNum = Float.parseFloat(attr[rightCol].substring(1, attr[rightCol].length() - 1));
				return checkNum();
			} else {
				rightChar = attr[rightCol].substring(1, attr[rightCol].length() - 1);
				return checkString();
			}
		}
		return false;
	}
	
	public boolean checkString() {
		if ((operator == IS_EQUALS && leftChar.equals(rightChar)) || (operator == IS_LIKE && leftChar.equalsIgnoreCase(rightChar)))
			return true;
		return false;
	}
	
	public boolean checkNum() {
		if ((operator == IS_EQUALS && leftNum == rightNum) || (operator == IS_GREATER && leftNum > rightNum) ||
				(operator == IS_GREATER_EQUALS && leftNum >= rightNum) || (operator == IS_LESS && leftNum < rightNum) ||
				(operator == IS_LESS_EQUALS && leftNum <= rightNum) || (operator == IS_NOT_EQUALS && leftNum != rightNum))
				return true;
		return false;
	}

}
