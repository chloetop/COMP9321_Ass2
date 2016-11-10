package comp9321.assignment2.bookstore.dbLoad;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

import comp9321.assignment2.bookstore.dao.CustomerDAO;

public class ReadXMLFile {

	// Variables for the XML file read
	XMLInputFactory xmlInputFactory;
	XMLEventReader xmlEventReader;
	XMLEvent xmlEvent;
	String file_name;// file name
	String document_types; // document types in the XML file
	String formatter_nodes;// stores the nodes like<i> or <sup> in the XML

	// Variables for data extraction from the XML file
	XMLOutputFactory xmlOutputFactory;
	XMLEventWriter xmlWriter;
	String root_node;//

	public ReadXMLFile() throws XMLStreamException, IOException {
		xmlInputFactory = XMLInputFactory.newInstance();
		xmlWriter = null;

		// retrieve the file name and the document types
		PropertyValues property = new PropertyValues();
		file_name = property.getPropValues("file_name");
		document_types = property.getPropValues("document_types");
		root_node = property.getPropValues("root_node");
		formatter_nodes = property.getPropValues("formatter_nodes");

		// Open the XML file
		xmlEventReader = xmlInputFactory
				.createXMLEventReader(new FileInputStream(file_name));

		// Initialize the XML output fields to extract the data
		xmlOutputFactory = XMLOutputFactory.newInstance();

	}

	public boolean compareCategory(String categoryList, String compareString) {
		/*
		 * Compares the category value sent in the parameter compareString to
		 * the pipe delimited list of categories in the string parameter
		 * categoryList. This function returns 'true' if the compareString value
		 * sent in the parameter is in the categoryList
		 */
		if (categoryList.contains("|" + compareString + "|"))
			return true;
		return false;
	}

	public boolean matchNodes(HashMap<String, String> node,
			HashMap<String, String> query, boolean exact_match) {
		/*
		 * Compares the query value sent in the parameter query to the value in
		 * the HashMap node that is extracted from the source XML file. The
		 * function checks for an exact match based on the parameter value -
		 * exact_match
		 */
		String value = new String();
		String query_value = new String();
		boolean match = false;

		if (query.isEmpty()) {
			match = true;
		}

		if (query.containsKey("type")) {
			String document_type = node.get("type");
			String query_document_type = query.get("type");

			if (!query_document_type.contains("|" + document_type + "|")) {
				return false;
			} else if (query.keySet().size() == 1) {
				return true;
			}
		}

		for (String key : query.keySet()) {
			if (node.containsKey(key) && !key.equals("type")) {
				value = node.get(key).trim().toLowerCase();
				query_value = query.get(key).trim().toLowerCase();

				if (value.contains(query_value)) {
					match = true;
				} else if (exact_match) {
					match = false;
					break;
				}

			}
		}

		return match;

	}

	// Returns all the attributes of the node passed
	public void getAttributes(Iterator<?> attrs,
			HashMap<String, String> document) throws IOException {
		while (attrs.hasNext()) {
			Attribute attr = (Attribute) attrs.next();
			String attribute_value = attr.getValue();
			String attribute_name = attr.getName().getLocalPart();
			if (!attribute_value.isEmpty())
				addNode(attribute_name, attribute_value, document);
		}
	}

	// Add value to the document hashmap
	public void addNode(String node_name, String node_value,
			HashMap<String, String> document) throws IOException {
		PropertyValues property = new PropertyValues();
		String key = property.getPropValues(node_name);

		if (key == null)
			key = node_name;

		if (!document_types.contains("|" + node_name + "|")
				&& !formatter_nodes.contains("|" + node_name + "|")) {
			if (document.containsKey(key)) {
				document.put(key, document.get(key) + ", " + node_value);
			} else {
				document.put(key, node_value);
			}
		}
	}

	// Retrieves the next node from the XML file
	public HashMap<String, String> getNextNode() throws XMLStreamException,
			IOException {
		boolean add_elements = false;
		String node_name = new String();
		HashMap<String, String> document = new HashMap<String, String>();
		// StringWriter tag_content = new StringWriter();

		// Loop over the document
		while (xmlEventReader.hasNext()) {
			xmlEvent = xmlEventReader.nextEvent();// get the next event

			// Start element
			if (xmlEvent.isStartElement()) {
				node_name = xmlEvent.asStartElement().getName().getLocalPart();
				if (!node_name.equalsIgnoreCase(root_node)
						&& !formatter_nodes.contains("|" + node_name + "|")) {
					// not 'dblp' and is a document type tag
					if (document_types.contains("|" + node_name + "|")) {
						add_elements = true;
						document.put("type", node_name);
						// Read the attributes to the document
						getAttributes(
								xmlEvent.asStartElement().getAttributes(),
								document);
					} else {
						// xmlWriter = xmlOutputFactory
						// .createXMLEventWriter(tag_content);
					}
				}
			} else if (add_elements && xmlEvent.isEndElement()) {
				node_name = xmlEvent.asEndElement().getName().getLocalPart();
				if (!formatter_nodes.contains("|" + node_name + "|")) {
					// Close the XML writer
					// xmlWriter.close();
					// add the node content to the document
					// String tag_value = tag_content.toString();
					// if (tag_value != null)
					// addNode(node_name, tag_value.trim(), document);
					// // Refresh the tag content value
					// tag_content = new StringWriter();
					// Stop adding elements
					if (document_types.contains("|" + node_name + "|")) {
						add_elements = false;
						break;
					}
				}
			} else if (xmlEvent.isCharacters()) {
				// Add the characters to the XMLWriter stream
				String value = xmlEvent.asCharacters().getData().trim();
				if (!value.isEmpty())
					addNode(node_name, value, document);

			}

		}
		return document;

	}

	private String getValue(HashMap<String, String> node, String key) {

		if (node.containsKey(key)) {
			String node_value = node.get(key).trim();
			node_value = node_value.replaceAll("'", "\\\\'");
			return node_value;
		} else {
			return "";
		}
	}
	
	public static int randInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

	public void insertToTable() throws XMLStreamException, IOException {
		long counter = 0;
		while (getNextNode() != null) {
			try {
				HashMap<String, String> node = getNextNode();
				
				counter++;
				String query = "INSERT INTO bookstore.item"+
						"(`id`,`key`,`title`,`author`,`editor`,`booktitle`,`pages`,"+
						"`year`,`address`,`journal`,`volume`,`url`,`ee`,"+
						"`cdrom`,`cite`,`publisher`,`note`,`crossref`,`isbn`,"+
						"`series`,`school`,`chapter`,`mdate`,`publtype`,`type`,`price`,`seller_id`,`image_url`,`enabled_status`) VALUES ("+
						counter+", "+
						"'"+getValue(node,"key")+"', "+
						"'"+getValue(node,"title")+"', "+
						"'"+getValue(node,"author")+"', "+
						"'"+getValue(node,"editor")+"', "+
						"'"+getValue(node,"booktitle")+"', "+
						"'"+getValue(node,"pages")+"', "
						+ getValue(node,"year")+", "+
						"'"+getValue(node,"address")+"', "+
						"'"+getValue(node,"journal")+"', "+
						"'"+getValue(node,"volume")+"', "+
						"'"+getValue(node,"url")+"', "+
						"'"+getValue(node,"ee")+"', "+
						"'"+getValue(node,"cdrom")+"', "+
						"'"+getValue(node,"cite")+"', "+
						"'"+getValue(node,"publisher")+"', "+
						"'"+getValue(node,"note")+"', "+
						"'"+getValue(node,"crossref")+"', "+
						"'"+getValue(node,"isbn")+"', "+
						"'"+getValue(node,"series")+"', "+
						"'"+getValue(node,"school")+"', "+
						"'"+getValue(node,"chapter")+"', "+
						"'"+getValue(node,"mdate")+"', "+
						"'"+getValue(node,"publtype")+"', "+
						"'"+getValue(node,"type")+"', "+
						randInt(5, 500)+", "+
						"'1000', '', 1)";
				
				CustomerDAO.executeQuery(query);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}

		}
	}

	public static void main(String[] args) throws XMLStreamException, IOException {
		ReadXMLFile r = new ReadXMLFile();
		
		r.insertToTable();
		

	}

}
