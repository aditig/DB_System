import com.foundationdb.sql.types.DataTypeDescriptor;


public class Attribute {
	private String name;
	private String dataType;
	
	Attribute (String columnName, String type) {
		name = columnName;
		//System.out.println("Column name and type " + name + " " + type);
		
		dataType = type;
		
	}
	
	public String getName() {
		return name;
	}
	
	public String getDataType() {
		return dataType;
	}

}
