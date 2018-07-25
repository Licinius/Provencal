package view;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

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
	@FXML
	private JFXTextField keywordField;
	@FXML
	private JFXToggleButton toggleHighlight;
	
	private static final StringWriter scriptjs = new StringWriter();
	private Question question;
	private HashSet<String> keywords;
	private boolean highlight;
	
	/**
	 * This function initialize the static attribute scriptjs if it's not already initialize
	 * Then it adds the listener on the text selection
	 */
	public void initialize() {
		if(scriptjs.toString().isEmpty()) {
			InputStream scriptInputStream = getClass().getClassLoader().getResourceAsStream("view/resources/script.js");
			String line = null;
			try {
				BufferedInputStream bufferedInputStream = new BufferedInputStream(scriptInputStream);
				InputStreamReader inputStreamReader = new InputStreamReader(bufferedInputStream, StandardCharsets.UTF_8);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				while((line = bufferedReader.readLine()) != null) {
					scriptjs.append(line+"\n");//Add end of line not read 
	            }
	            bufferedReader.close(); 
			} catch (IOException e) {
				new AlertException(e).showAlert();
			}	
		}
		this.body.setOnMouseReleased(arg0->{
            Object selection = this.body.getEngine().executeScript(scriptjs.toString());
            if (selection instanceof String) {
            	keywordField.setText(((String) selection).trim());
            }
		});
		
	}
	/**
	 * Set the information of a question in the view
	 * @param question The question to set
	 */
	public void setQuestion(Question question, HashSet<String> keywords,boolean highlight){
		this.keywords = keywords;
		this.question = question;
		this.highlight = highlight;
		this.title.setText(question.getTitle());
		this.body.getEngine().setUserStyleSheetLocation(getClass().getClassLoader().getResource("view/resources/style.css").toExternalForm());
		this.body.getEngine().loadContent(question.getBody());
		if(question.isClassified()) {
			this.title.setStyle(this.title.getStyle()+"-fx-text-fill: #64DD17;");
		}
		String classesName = question.getClasses().stream().map(p -> p.getName())
                .collect(Collectors.joining(", "));
		this.classes.setText(classesName);
		if(highlight) {
			//HighLight keyword
			Task<String> highlightWords = highlightWord(keywords);
			highlightWords.setOnSucceeded(arg0 -> {
				this.body.getEngine().loadContent(highlightWords.getValue());
			});
			new Thread(highlightWords).start();
		}
		toggleHighlight.setSelected(highlight);
	}
	
	/**
	 * 
	 * @return the question in the View
	 */
	public Question getQuestion() {
		return this.question;
	}
	/**
	 * 
	 * @return True if the toggle is armed
	 */
	public boolean getStateToggleHighLight() {
		return this.highlight;
	}
	/**
	 * handle the highlight of the keywords
	 */
	@FXML
	public void handleHighlightKeywords() {
		if(!this.highlight) {
			this.highlight = true;
			//HighLight keyword
			Task<String> highlightWords = highlightWord(keywords);
			highlightWords.setOnSucceeded(arg0 -> {
				this.body.getEngine().loadContent(highlightWords.getValue());
			});
			new Thread(highlightWords).start();
		}else {
			this.highlight = false;
			this.body.getEngine().loadContent(question.getBody());
		}
	}
	/**
	 * Add the keyword to the hashSet then update the content
	 */
	@FXML
	public void addKeyword() {
		this.keywords.add(this.keywordField.getText());
		Task<String> highlightWords = highlightWord(keywords);
		highlightWords.setOnSucceeded(arg0 -> {
			this.body.getEngine().loadContent(highlightWords.getValue());
		});
		new Thread(highlightWords).start();
		toggleHighlight.setSelected(true);
	}
	
	private Task<String> highlightWord(HashSet<String> keywords){
		return new Task<String>() {
			
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
					splitKeyword.clear();
					index = 1;
					found = true;
					for(String s : keyword.split("((?<=[ ])|(?=[ ]))")) {
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
			private Element formatElement(Element element) {
				Element newElement = new Element(element.nodeName());
				List<Node> children = element.childNodes();
				ArrayList<String> potentialKeywords = new ArrayList<String>();
				Pattern wordPattern;
				int wordIndex = 0;
				for(Node n : children) {
					if(!n.nodeName().equals("#text")) {
						newElement.appendChild(formatElement((Element) n));
					}
					else {
						String[] words = n.toString().split("((?<=[^A-z0-9-])|(?=[^A-z0-9-]))");
						while(wordIndex<words.length) {
							potentialKeywords.clear();
							String word = words[wordIndex];
							TextNode text = new TextNode(word);
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
										text.text(word);
										Element span = new Element("span");
										if(sizeMultiword>1)
											span.attr("class", "highlight-n");
										else {
											span.attr("class", "highlight");	
										}
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
			@Override
			protected String call() throws Exception {
				String xml = "<body>" + question.getBody() + "</body>";
				Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
				Element body = doc.select("body").get(0);				
		        return formatElement(body).toString();
			}		
		};
	}
	
}
