
public class Attributes {
	private String name;
	private int dataType;
	
	Attributes (String columnName, String type) {
		name = columnName;
		System.out.println("col name and type " + name + " " + type);
		
		if (type.equals("int")) {
			dataType = 1;
		}
		else if (type.equals("String")) {
			dataType = 2;
		}
		else if (type.equals("float")) {
			dataType = 3;
		}
	}
	
	public String getName() {
		return name;
	}
	
	public int getDataType() {
		return dataType;
	}

}
