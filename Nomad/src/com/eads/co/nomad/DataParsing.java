package com.eads.co.nomad;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class DataParsing {
	private SAXBuilder parser;
	private Document document;
	private Element racine;

	public DataParsing(InputStream input) throws JDOMException, IOException {
		super();
		parser = new SAXBuilder();
		setInput(input);
	}


	public void setInput(InputStream input) throws JDOMException, IOException {
		document = parser.build(input);
		document.getBaseURI();
		racine = document.getRootElement();
	}

	public String getTitle() {
		return racine.getAttributeValue("CHAPNBR")+"."+
				racine.getAttributeValue("SECTNBR")+"."+
				racine.getAttributeValue("SUBJNBR")+" - "+
				racine.getChild("TITLE").getText();
	}

	public String getWarnings() {
		String warnings = "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\"/><body>";
		List<Element> listWarnings = racine.getChildren("WARNING");
		List<Element> listItems;
		List<Element> listList;
		List<Element> listListItem;
		Iterator<Element> iteratorListList;
		Iterator<Element> iteratorListItem;
		Iterator<Element> iteratorItems;

		Iterator<Element> iteratorWarning = listWarnings.iterator();
		/* Warnings */
		while (iteratorWarning.hasNext()) {
			Element currentWarning = (Element) iteratorWarning.next();
			listList = currentWarning.getChildren("UNLIST"); // Lists of warning node
			iteratorListList = listList.iterator();
			listItems = currentWarning.getChildren("PARA"); // Warning para
			iteratorItems = listItems.iterator();
			/* Para */
			while (iteratorItems.hasNext()) {
				warnings += "<p id='warning'>";
				Element currentItem = (Element) iteratorItems.next();
				warnings += currentItem.getText()+"<br/>"; // Display para text
				/* Display list of para is necessary */
				if(currentItem.getText().endsWith(": ")){
					Element currentListItem = (Element) iteratorListList.next();
					listListItem = currentListItem.getChildren("UNLITEM");
					iteratorListItem = listListItem.iterator();
					warnings += "<ul id='warning'>";
					while (iteratorListItem.hasNext()) {
						Element currentListListItem = (Element) iteratorListItem.next();
						warnings += "<li>"+currentListListItem.getChild("PARA").getText()+"</li>";
					}
					warnings += "</ul>";
					warnings += "</p>";
				}
			}
			warnings += "<br/>";
		}
		warnings += "</body>";
		return warnings;
	}
	
	public String getJobSetUp() {
		String jobSetUp = "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\"/><body>";
		List<Element> listTopic = racine.getChildren("TOPIC");
		List<Element> listSubTask;
		List<Element> listList1;
		List<Element> listL1Item;
		List<Element> listPara;
		Iterator<Element> iteratorSubTask;
		Iterator<Element> iteratorListList;
		Iterator<Element> iteratorL1Item;
		Iterator<Element> iteratorPara;

		Iterator<Element> iteratorTopics = listTopic.iterator();
		/* Topics */
		while (iteratorTopics.hasNext()) {
			Element topic = (Element) iteratorTopics.next();
			/* Choix de la bonne partie */
			if(topic.getChild("TITLE").getText().equals("Job Set-up")){
				System.out.println("JOB SET UP");
				listSubTask = topic.getChildren("SUBTASK");
				iteratorSubTask = listSubTask.iterator();
				while (iteratorSubTask.hasNext()) {
					Element subTask = (Element) iteratorSubTask.next();
					jobSetUp += "<p>";
					jobSetUp += "Task : <a href='"+subTask.getAttributeValue("KEY")+"'>"+subTask.getAttributeValue("KEY")+"</a><br/>";
					
					listList1 = subTask.getChildren("LIST1");
					iteratorListList = listList1.iterator();
					/* LIST1 */
					while (iteratorListList.hasNext()) {
						Element list1 = (Element) iteratorListList.next();
						listL1Item = list1.getChildren("L1ITEM");
						iteratorL1Item = listL1Item.iterator();
						while (iteratorL1Item.hasNext()) {
							Element l1Item = (Element) iteratorL1Item.next();
							listPara = l1Item.getChildren("PARA");
							iteratorPara = listPara.iterator();
							jobSetUp += "<ul>";
							while (iteratorPara.hasNext()) {
								Element para = (Element) iteratorPara.next();
								jobSetUp += "<li>"+para.getText()+"</li>";
							}
							jobSetUp += "</ul>";
						}
					}
					jobSetUp += "</p>";
				}
				
			}
		}
		jobSetUp += "</body>";
		return jobSetUp;
	}

	public String getProcedure() {
		String procedure = "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\"/><body>";
		// TODO Auto-generated method stub
		procedure += "PROCEDURE";
		procedure += "</body>";
		return procedure;
	}
	
	public String getCloseUp() {
		String closeUp = "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\"/><body>";
		List<Element> listTopic = racine.getChildren("TOPIC");
		List<Element> listSubTask;
		List<Element> listList1;
		List<Element> listL1Item;
		List<Element> listList2;
		List<Element> listL2Item;
		Iterator<Element> iteratorSubTask;
		Iterator<Element> iteratorListList;
		Iterator<Element> iteratorL1Item;
		Iterator<Element> iteratorL2Item;

		Iterator<Element> iteratorTopics = listTopic.iterator();
		/* Topics */
		while (iteratorTopics.hasNext()) {
			Element topic = (Element) iteratorTopics.next();
			/* Choix de la bonne partie */
			if(topic.getChild("TITLE").getText().equals("Close-up")){
				System.out.println("CLOSE UP");
				listSubTask = topic.getChildren("SUBTASK");
				iteratorSubTask = listSubTask.iterator();
				while (iteratorSubTask.hasNext()) {
					Element subTask = (Element) iteratorSubTask.next();
					closeUp += "<p>";
					closeUp += "Task : <a href='"+subTask.getAttributeValue("KEY")+"'>"+subTask.getAttributeValue("KEY")+"</a><br/>";
					
					listList1 = subTask.getChildren("LIST1");
					System.out.println("LIST 1 : "+listList1.size());
					iteratorListList = listList1.iterator();
					/* LIST1 */
					while (iteratorListList.hasNext()) {
						Element list1 = (Element) iteratorListList.next();
						listL1Item = list1.getChildren("L1ITEM");
						iteratorL1Item = listL1Item.iterator();
						while (iteratorL1Item.hasNext()) {
							Element l1Item = (Element) iteratorL1Item.next();
							Element para = l1Item.getChild("PARA");
							closeUp += para.getText()+"<br/>";
							listList2 = l1Item.getChildren("LIST2");
							System.out.println("LIST LIST2 : "+listList2.size());
							iteratorL2Item = listList2.iterator();
							while (iteratorL2Item.hasNext()) {
								Element list2 = (Element) iteratorL2Item.next();
								listL2Item = list2.getChildren("L2ITEM");
								iteratorL2Item = listL2Item.iterator();
								closeUp += "<ul>";
								while (iteratorL2Item.hasNext()) {
									Element l2Item = (Element) iteratorL2Item.next();
									Element paraL2Item = l2Item.getChild("PARA");
									System.out.println("PARA : "+paraL2Item.getText());
									closeUp += "<li>"+paraL2Item.getText()+"</li>";
								}
								closeUp += "</ul>";
							}
						}
					}
					closeUp += "</p>";
				}
				
			}
		}
		closeUp += "</body>";
		return closeUp;
	}
	
	public String getTools() {
		String tools = "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\"/><body>";
		List<Element> listPretopic = racine.getChild("TFMATR").getChildren("PRETOPIC");
		List<Element> listSubTask;
		List<Element> listList1;
		List<Element> listL1Item;
		List<Element> listPara;
		Iterator<Element> iteratorList1;
		Iterator<Element> iteratorListList;
		Iterator<Element> iteratorL1Item;
		Iterator<Element> iteratorPara;

		Iterator<Element> iteratorTopics = listPretopic.iterator();
		/* Topics */
		while (iteratorTopics.hasNext()) {
			Element topic = (Element) iteratorTopics.next();
			/* Choix de la bonne partie */
			if(topic.getChild("TITLE").getText().equals("Job Set-up Information")){
				System.out.println("JOB SET UP");
				listList1 = topic.getChildren("LIST1");
				iteratorList1 = listList1.iterator();
				while (iteratorList1.hasNext()) {
					Element list1 = (Element) iteratorList1.next();
					listL1Item = list1.getChildren("L1ITEM");
					iteratorL1Item = listL1Item.iterator();

					while (iteratorL1Item.hasNext()) {
						tools += "<p>";
						Element l1Item = (Element) iteratorL1Item.next();
						listPara = l1Item.getChildren("PARA");
						iteratorPara = listPara.iterator();
						while (iteratorPara.hasNext()) {
							Element para = (Element) iteratorPara.next();
							tools += "<b>"+para.getText()+"</b>";
						}
						Element table = l1Item.getChild("TABLE");
						tools += "<table>";
						tools += "<row>";
						tools += "<td>";
						tools += "TODO";
						tools += "</td>";
						tools += "</row>";
						tools += "</table>";
						tools += "</p>";
					}
				}
			}
		}
		tools += "</body>";
		return tools;
	}


	public String getPictures() {
		String pictures = "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\"/><body>";
		// TODO Auto-generated method stub
		pictures += "PICTURES";
		pictures += "</body>";
		return pictures;
	}
}
