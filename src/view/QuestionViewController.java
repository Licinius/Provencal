package view;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashSet;
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
	
	private Task<String> slowHighlightWords(HashSet<String> keywords){
		return new Task<String>() {
			@Override
			protected String call() throws Exception {
				String formatQuestionBody = question.getBody();
				for(String keyword : keywords) {
					formatQuestionBody = formatQuestionBody.replaceAll("(?<!<[^>]*)\\b"+keyword+"\\b(?<![^>]*<)", "<span class='highlight'>"+keyword+"</span>");
				}
				return formatQuestionBody;
			}
		};
	}
	
	private Task<String> fastHighlightWords(HashSet<String> keywords){
		return new Task<String>() {
			
			protected Node formatElement(Node node, Document doc) {
				Element newElement = doc.createElement(node.getNodeName());
				NodeList children = node.getChildNodes();
				int size = children.getLength();
				for(int i = 0; i<size;i++) {
					if(children.item(i).getNodeType() == Node.ELEMENT_NODE)
						newElement.appendChild(formatElement(children.item(i),doc));
					else {
						for(String s : children.item(i).getTextContent().split("((?<=[^A-z0-9])|(?=[^A-z0-9]))")) {
							Node text = doc.createTextNode(s);
							if(keywords.contains(s)) {
								Element span = doc.createElement("span");
								span.setAttribute("class", "highlight");
								span.appendChild(text);
								newElement.appendChild(span);
							}else {
								newElement.appendChild(text);
							}
						}
					}
				}
				return newElement;
			}
			@Override
			protected String call() throws Exception {
				String xml = "<body>" + question.getBody() + "</body>";
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				InputStream inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
				Document doc = docBuilder.parse(inputStream);
				Node n = doc.getFirstChild();
				n = formatElement(doc.getFirstChild(), doc);
				StringWriter sw = new StringWriter();
				TransformerFactory tf = TransformerFactory.newInstance();
		        Transformer transformer = tf.newTransformer();
		        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

		        transformer.transform(new DOMSource(n), new StreamResult(sw));
		        return sw.toString();
			}		
		};
		
	}
	
}
