package view;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import model.Question;
/**
 * Controller for an anchor pane that display a question
 * @author Dell'omo
 */
public class QuestionViewController {
	@FXML
	private Label title;
	@FXML
	private WebView body;
	@FXML
	private Label classes;
	private Question question;
	
	/**
	 * Set the information of a question in the view
	 * @param question The question to set
	 */
	public void setQuestion(Question question, HashSet<String> keywords){
		this.question = question;
		this.title.setText(question.getTitle());
		this.body.getEngine().setUserStyleSheetLocation(getClass().getResource("/view/resources/style.css").toString());
		this.body.getEngine().loadContent(question.getBody());
		if(question.isClassified()) {
			this.title.setStyle(this.title.getStyle()+"-fx-text-fill: #64DD17;");
		}
		String classesName = question.getClasses().stream().map(p -> p.getName())
                .collect(Collectors.joining(", "));
		this.classes.setText(classesName);
		Task<String> highlightWords = fastHighlightWords(keywords);
		highlightWords.setOnSucceeded(arg0 -> {
			this.body.getEngine().loadContent(highlightWords.getValue());
		});
		new Thread(highlightWords).start();
	}
	
	/**
	 * 
	 * @return the question in the View
	 */
	public Question getQuestion() {
		return this.question;
	}
	
	private Task<String> fastHighlightWords(HashSet<String> keywords){
		return new Task<String>() {
			
			private Node formatElement(Node node, Document doc) {
				Element newElement = doc.createElement(node.getNodeName());
				NodeList children = node.getChildNodes();
				int size = children.getLength();
				ArrayList<String> potentialKeywords = new ArrayList<String>();
				Pattern wordPattern;
				int wordIndex = 0;
				for(int indexChildren = 0; indexChildren<size;indexChildren++) {
					if(children.item(indexChildren).getNodeType() == Node.ELEMENT_NODE)
						newElement.appendChild(formatElement(children.item(indexChildren),doc));
					else {
						String[] words = children.item(indexChildren).getTextContent().split("((?<=[^A-z0-9-])|(?=[^A-z0-9-]))");
						while(wordIndex<words.length) {
							potentialKeywords.clear();
							String word = words[wordIndex];
							Node text = doc.createTextNode(word);
							if(!word.trim().isEmpty()) {
								wordPattern = Pattern.compile("^(?i)\\b" + Pattern.quote(word) + "\\b.*");
								for(String keyword : keywords) {
									if(wordPattern.matcher(keyword).matches())
										potentialKeywords.add(keyword);	
								}
								if(!potentialKeywords.isEmpty()) {
									int sizeMultiword = getSizeMultiword(words,wordIndex,potentialKeywords);
									if(sizeMultiword>0){
										for(int i = 1; i<sizeMultiword; i++) 
											word+=words[++wordIndex];
										//Create the tag
										text.setTextContent(word);
										Element span = doc.createElement("span");
										span.setAttribute("class", "highlight");
										span.appendChild(text);
										newElement.appendChild(span);
									}else {
										newElement.appendChild(text);
									}
									
								}else {
									newElement.appendChild(text);
								}
							}else {
								newElement.appendChild(text);
							}
							wordIndex++;
						}
					}
				}
				return newElement;
			}
			
			/**
			 * Check if there is a multiword in the words for the word words[wordIndex].
			 * @param words A Array of string
			 * @param wordIndex The index of the word to test
			 * @param potentialKeywords the postential keyword that match the word
			 * @return the size of the biggest keyword
			 */
			private int getSizeMultiword(String[] words, int wordIndex, ArrayList<String> potentialKeywords) {
				int sizeMax = -1;//default value
				ArrayList<String> splitKeyword = new ArrayList<>();
				int index;
				boolean found; //default true and false if the splitkeyword does not match the text
				for(String keyword : potentialKeywords) {
					index = 1;
					found = true;
					for(String s : keyword.split(" ")) {
						splitKeyword.add(s);
					}
					Iterator<String> splitwordIterator = splitKeyword.iterator();
					String splitword = splitwordIterator.next();
					while (splitwordIterator.hasNext()){
						splitword = splitwordIterator.next();
						int indexNextWord = wordIndex+index;
						if(!(indexNextWord<words.length) || !splitword.equalsIgnoreCase(words[indexNextWord])) {
							found = false;
							break;
						}
						index++;
					}
					if(found) 
						sizeMax = (sizeMax<splitKeyword.size()?splitKeyword.size():sizeMax);
				}
				return sizeMax;
			}
			
			@Override
			protected String call() throws Exception {
				
				String xml = "<body>" + question.getBody() + "</body>";
				xml = xml.replaceAll("<br>", "<br/>");//Some question have br or hr not closed
				xml = xml.replaceAll("<hr>", "<hr/>");
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				InputStream inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
				Document doc = docBuilder.parse(inputStream);
				Node n = doc.getFirstChild();
				n = formatElement(n, doc);
				StringWriter sw = new StringWriter();
				TransformerFactory tf = TransformerFactory.newInstance();
		        Transformer transformer = tf.newTransformer();
		        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		        transformer.transform(new DOMSource(n), new StreamResult(sw));
		        return sw.toString();
			}		
		};
		
	}
	
}
