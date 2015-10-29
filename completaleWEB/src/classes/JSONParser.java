package classes;

public class JSONParser {
	
	public static String parserJSONObjects (String[] array){
		String response = "{";
		for(int i=0; i<array.length; i=i+2){
			response += "\""+array[i]+"\":\""+array[i+1]+"\"";
			if (i!=(array.length-2)) response += ", ";
		}
		response += "}";
		return response;
	}
	
	public static String parserJSONArrays (String[][] array){
		
		String response = "[";
		for (int i=0;i<array.length;i++){
			
			response += "[";
			for (int j=0; j<array[i].length;j++){
				response += "\"" + array[i][j] + "\"";
				if (j!=array[i].length-1) response += ", ";
			}
			response += "]";
			if (i!=array.length-1) response += ", ";
		}
		response += "]";		
		
		return response;
	}
	
	public static String parserJSONArray (String[] array){
		
		String response = "[";
		for (int i=0;i<array.length;i++){			
			response += "\"" + array[i] + "\"";
			if (i!=array.length-1) response += ", ";
		}
		response += "]";		
		
		return response;
	}
}
