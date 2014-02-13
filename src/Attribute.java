public class Attribute {
	public static final int INTEGER = 1;
	public static final int FLOAT = 2;
	public static final int VARCHAR = 3;
	private String name;
	private int dataType;
	private int stringLength;
	
	Attribute (String columnName, String type) {
		name = columnName;
		//System.out.println("Column name and type " + name + " " + type);
		
		if(type.toUpperCase().equals("INTEGER")) {
			dataType = INTEGER;
		} else if(type.toUpperCase().equals("FLOAT")) {
			dataType = FLOAT;
		} else if(type.toUpperCase().startsWith("VARCHAR")) {
			dataType = VARCHAR;
			stringLength = Integer.parseInt(type.substring(8, type.length() - 1));
		}
	}
	
	public String getName() {
		return name;
	}
	
	public int getDataType() {
		return dataType;
	}
	
	public int getStringLength() {
		return stringLength;
	}

}
