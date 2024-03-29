package analyzer.dependencyanalyzer;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Created on November 4, 2014 by Ellina.
 * Parses an XML file using SAX API.
 * Retrieves the information from an xml file
 * 			into ClassDependencyInfo and PackageDependencyInfo objects.
 * Reference for SAX API: http://sax.sourceforge.net/quickstart.html
 */

public class XmlParser extends DefaultHandler {

	private Vector<ClassDependencyInfo> classVector = new Vector<ClassDependencyInfo>();	// vector of Class Dependency objects
	private Vector<PackageDependencyInfo> packageVector = new Vector<PackageDependencyInfo>(); // vector of Package Dependency objects
	private Vector<String> tempAfferentClassVector; // temporarily contains names of afferent classes
	private Vector<String> tempEfferentClassVector; // temporarily contains names of efferent classes
	private Vector<String> tempAfferentPkgVector; // temporarily contains names of afferent packages
	private Vector<String> tempEfferentPkgVector; // temporarily contains names of efferent packages
	ClassDependencyInfo classInfo = null;
	PackageDependencyInfo packageInfo = null;
	DependencyAnalyzer dependencyAnalyzer; // used to reference the caller object, DependencyAnalyzer.
	private boolean encounteredCyclesTag = false;
	private boolean encounteredPackageCyclesTag = false;
	

	/**
	 * Default constructor.
	 */
	public XmlParser(DependencyAnalyzer da) {
		super();
		dependencyAnalyzer = da;
		encounteredCyclesTag = false;
		encounteredPackageCyclesTag = false;
	}

	/**
	 * Entry point of the XML Parser.
	 * Sets up and runs the parser given the XML file name.
	 * @param filePath A path to the xml file.
	 */
	public void startXmlParser(String filePath, DependencyAnalyzer da) throws XmlParserException {

		if(filePath == "" || filePath == null){
			System.err.println("File path to the xml file cannot be null or empty. Please, try again.");
			throw new XmlParserException("XML file cannot be null or empty");
		}

		XMLReader xr;
		try {
			xr = XMLReaderFactory.createXMLReader();
			XmlParser handler = new XmlParser(da);
			xr.setContentHandler(handler);
			xr.setErrorHandler(handler);
			
			// Parse the given XML file.
			FileReader r;
			try {
				r = new FileReader(filePath);
				xr.parse(new InputSource(r));
			} catch (FileNotFoundException e) {
				System.err.println("File not found. Please, provide a valid file name for XML Parser.");
				System.err.println(e);
				throw new XmlParserException("XML Parser exception: File not found");
			}
			catch (IOException e) {
				System.err.println(e);
				throw new XmlParserException("XML Parser exception: IO exception");
			}
		} catch (SAXException e) {
			System.err.println(e);
			throw new XmlParserException("XML Parser exception: SAX exception");
		}
	}

	// --------------- Event handlers --------------------------------//

	/**
	 * Handles the beginning of a document.
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	public void startDocument(){
		//System.out.println(" Start of document");
	}

	/**
	 * Handles the end of a document.
	 * @see org.xml.sax.helpers.DefaultHandler#endDocument()
	 */
	public void endDocument(){
		dependencyAnalyzer.setAllClassesDependencies(classVector);
		dependencyAnalyzer.setAllPackagesDependencies(packageVector);
	}

	/**
	 * Event handler for when the start of an element is encountered.
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement (String uri, String name, String qName, Attributes attributes){

		switch(qName){
		
		case "cycles":
			encounteredCyclesTag = true;
			break;
			
		case "packageCycles":
			encounteredPackageCyclesTag = true;
			break;
		
		case "class":
			
			if(encounteredCyclesTag){
				encounteredCyclesTag = false; // turn it off to read info from classRef
			}
			
			// Create a new object
			int usedBy = Integer.parseInt(attributes.getValue("usedBy"));
			int uses = Integer.parseInt(attributes.getValue("usesInternal"));
			classInfo = new ClassDependencyInfo(usedBy, uses);
			classInfo.setClassName(attributes.getValue("name"));
			// Reset temporary vectors
			tempAfferentClassVector = new Vector<String>();
			tempEfferentClassVector = new Vector<String>();
			break;

		case "classRef":
			
			if(encounteredCyclesTag){
				break;
			}
			
			String refName = attributes.getValue("name");
			String refType = attributes.getValue("type");

			// If type is usedBy, then it's an Afferent class.
			int comparison = refType.compareTo("usedBy");
			if(comparison == 0) { // strings are equal
				tempAfferentClassVector.add(refName);
			}

			// If type is usesInternal, then it's an Efferent class.
			int comparisonUses = refType.compareTo("usesInternal");
			if(comparisonUses == 0) { // strings are equal
				tempEfferentClassVector.add(refName);
			}
			// If type is usesExternal, then ignore it.
			break;

		case "package":
			
			if(encounteredPackageCyclesTag){
				encounteredPackageCyclesTag = false; // turn it off to read info from packageRef;
			}
			
			// Create a new PackageDependencyInfo object
			int usedByP = Integer.parseInt(attributes.getValue("usedBy"));
			int usesP = Integer.parseInt(attributes.getValue("usesInternal"));
			packageInfo = new PackageDependencyInfo(usedByP, usesP);
			packageInfo.setPackageName(attributes.getValue("name"));
			// Reset temporary vectors
			tempAfferentPkgVector = new Vector<String>();
			tempEfferentPkgVector = new Vector<String>();
			break;

		case "packageRef":
			
			if(encounteredPackageCyclesTag){
				break;
			}
			
			String refPkgName = attributes.getValue("name");
			String refPkgType = attributes.getValue("type");

			// If type is usedBy, then add to Afferent.
			int comparisonPkg = refPkgType.compareTo("usedBy");
			if(comparisonPkg == 0) { // strings are equal
				tempAfferentPkgVector.add(refPkgName);
			}

			// If type is usesInternal, then add to Efferent.
			int comparisonPkgUses = refPkgType.compareTo("usesInternal");
			if(comparisonPkgUses == 0) { // strings are equal
				tempEfferentPkgVector.add(refPkgName);
			}
			// If type is usesExternal, then ignore it.
			break;
		}
	}

	/**
	 * Event handler for when the end of an element is encountered.
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement (String uri, String name, String qName){

		switch(qName){
		case "class":
			// Populate an object with afferent classes
			for(int i=0; i < tempAfferentClassVector.size(); i++){
				classInfo.addAfferentClass(tempAfferentClassVector.get(i));
			}
			// Populate an object with efferent classes
			for(int i=0; i < tempEfferentClassVector.size(); i++){
				classInfo.addEfferentClass(tempEfferentClassVector.get(i));
			}
			// Save the object in a vector
			classVector.add(classInfo);
			break;

		case "package":
			// Populate an object with afferent package dependencies
			for(int i = 0; i < tempAfferentPkgVector.size(); i++){
				packageInfo.addAfferentPackage(tempAfferentPkgVector.get(i));
			}
			// Populate an object with efferent package dependencies
			for(int i = 0; i < tempEfferentPkgVector.size(); i++){
				packageInfo.addEfferentPackage(tempEfferentPkgVector.get(i));
			}
			// Save the object in a vector
			packageVector.add(packageInfo);
			break;
		}
	}

	/**
	 * Event handler for when the parser encounters text data.
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	public void characters (char ch[], int start, int length){
	}

}
