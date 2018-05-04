package com.newaim.core.utils;

public class FieldCmdUtil {

	//LT，GT，EQ，LE，GE,LK,EMP,NOTEMP,NULL,NOTNULL,IN
	//<,>,=,<=,>=,like,empty,not empty,null,not null
	// TODO
	public static String getPartHql(String entityName, String operation, String field, String value) {
		return FieldCmdUtil.getPartSql(entityName, operation, field, value);
	}
	
	public static String getPartSql(String entityName, String operation, String field, String value) {
		String result = "";
		String fieldName = (field.indexOf("_")>0 ? field.replace("_", ".") :
			entityName != null && !"".equals(entityName) ? entityName + "." + field : field ) ;
		
    	if("LT".equals(operation)){
    		result = " "+ fieldName  + " < '" + value + "'";    		
		}else if("GT".equals(operation)){
			result = " "+ fieldName + " > '" + value + "'";
		}else if("EQ".equals(operation)){
			result = " "+ fieldName + " = '" + value + "'";
		}else if("LE".equals(operation)){
			result = " "+ fieldName + " <= '" + value + "'";    
		}else if("GE".equals(operation)){
			result = " "+ fieldName + " >= '" + value + "'";    
		}else if("LK".equals(operation)){
			result = " "+ fieldName + " like '%" + value + "%'";
		}else if("LFK".equals(operation)){
			result = " "+ fieldName + " like '" + value + "%'";
		}else if("RHK".equals(operation)){
			result = " "+ fieldName + " like '%" + value + "'";
		}else if("NULL".equals(operation)){
			result = " ISNULL("+ fieldName+")";
		}else if("NOTNULL".equals(operation)){
			result = " NOT ISNULL("+ fieldName+")";
		}else if("EMP".equals(operation)){
			result = " "+ fieldName + " = ''";
		}else if("NOTEMP".equals(operation)){
			result = " "+ fieldName + " !=''";
		}else if("NEQ".equals(operation)){
			result = " "+ fieldName + " != '" + value + "'";
		}else{
			result = " '" + value + "' IN "+ fieldName;
		}

		return result;
	}
}
