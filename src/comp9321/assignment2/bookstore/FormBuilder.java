package comp9321.assignment2.bookstore;

import java.io.IOException;
import java.util.HashMap;

import comp9321.assignment2.bookstore.dbLoad.PropertyValues;

public class FormBuilder {

	public HashMap<String, String> getFormFields() throws IOException {
		PropertyValues property = new PropertyValues();
		String field_names = property.getPropValues("field_names");
		String[] fields = field_names.split("\\|");
		HashMap<String, String> fields_map = new HashMap<String, String>();
		String display_value = new String();

		for (int i = 0; i < fields.length; i++) {
			String field = fields[i];
			if (!field.isEmpty()) {
				display_value = property.getPropValues(field);
				if(display_value != null){
					fields_map.put(field,display_value.substring(0, 1).toUpperCase() + display_value.substring(1));
				}else{
					fields_map.put(field, field.substring(0, 1).toUpperCase() + field.substring(1));
				}
			}
		}
		
		return fields_map;

	}

	public static void main(String args[]) {
		FormBuilder form = new FormBuilder();

		try {HashMap<String,String> values = form.getFormFields();

			for(String key:values.keySet()){
				System.out.println(key+" : "+values.get(key));
			}
			
			System.out.println(values.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
