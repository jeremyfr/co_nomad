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
		return formatText(jobSetUp);
	}

	public String getProcedure() {
		String procedure = "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\"/><body>";
		
		List<Element> listTopic = racine.getChildren("TOPIC");
		List<Element> listSubTask;
		List<Element> listList1;
		List<Element> listL1Item;
		List<Element> listPara;
		List<Element> listList2;
		List<Element> listL2Item;
		List<Element> listPara2;
		List<Element> listUnList;
		List<Element> listUnLitem;
		List<Element> listPara3;
		List<Element> listWarning;
		List<Element> listPara4;
		List<Element> listCaution;
		List<Element> listPara5;		
		List<Element> listRef;
		List<Element> listRefInt;
		Iterator<Element> iteratorSubTask;
		Iterator<Element> iteratorListList;
		Iterator<Element> iteratorL1Item;
		Iterator<Element> iteratorPara;
		Iterator<Element> iteratorList2;
		Iterator<Element> iteratorL2Item;
		Iterator<Element> iteratorPara2;
		Iterator<Element> iteratorUnList;
		Iterator<Element> iteratorUnLitem;
		Iterator<Element> iteratorPara3;
		Iterator<Element> iteratorWarning;
		Iterator<Element> iteratorPara4;
		Iterator<Element> iteratorCaution;
		Iterator<Element> iteratorPara5;
		Iterator<Element> iteratorRef;
		Iterator<Element> iteratorRefInt;
		Iterator<Element> iteratorTopics = listTopic.iterator();
		/* Topics */
		while (iteratorTopics.hasNext()) {
			Element topic = (Element) iteratorTopics.next();
			/* Choix de la bonne partie */
			if(topic.getChild("TITLE").getText().equals("Procedure")){
				System.out.println("Procedure");
				listSubTask = topic.getChildren("SUBTASK");
				iteratorSubTask = listSubTask.iterator();
				while (iteratorSubTask.hasNext()) {
					Element subTask = (Element) iteratorSubTask.next();
					procedure += "<p>";
					procedure += "Task : <a href='"+subTask.getAttributeValue("KEY")+"'>"+subTask.getAttributeValue("KEY")+"</a><br/>";
					
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
							procedure += "<ul>";
							while (iteratorPara.hasNext()) {
								Element para = (Element) iteratorPara.next();
								procedure += "<li>"+para.getText();
								
								listRef = para.getChildren("REFBLOCK");
								iteratorRef = listRef.iterator();
								while(iteratorRef.hasNext()){
									Element refblock = iteratorRef.next();
									listRefInt = refblock.getChildren("REFINT");
									iteratorRefInt = listRefInt.iterator();
								    while (iteratorRefInt.hasNext()) {
										Element refInt = (Element) iteratorRefInt.next();
										procedure += "<a href='"+refInt.getAttributeValue("REFID")+"?y="+procedure.length()+"'>"+refInt.getText()+"</a><br>";
									}
									
								}
								
								procedure += "</li>";
							}
							listList2 = l1Item.getChildren("LIST2");
							iteratorList2 = listList2.iterator();
							/*LIST2*/
							while(iteratorList2.hasNext()){
								Element list2 = (Element) iteratorList2.next();
								listL2Item = list2.getChildren("L2ITEM");
								iteratorL2Item = listL2Item.iterator();
								while(iteratorL2Item.hasNext()){
									Element l2Item = (Element) iteratorL2Item.next();
									listPara2 = l2Item.getChildren("PARA");
									
									iteratorPara2 = listPara2.iterator();
									procedure += "<ul>";
									
									while (iteratorPara2.hasNext()) {
										Element para2 = (Element) iteratorPara2.next();
										procedure += "<li>"+para2.getText();
										
										listRef = para2.getChildren("REFBLOCK");
										iteratorRef = listRef.iterator();
										while(iteratorRef.hasNext()){
											Element refblock = iteratorRef.next();
											listRefInt = refblock.getChildren("REFINT");
											iteratorRefInt = listRefInt.iterator();
										    while (iteratorRefInt.hasNext()) {
												Element refInt = (Element) iteratorRefInt.next();
												procedure += "<a href='"+refInt.getAttributeValue("REFID")+"?y="+procedure.length()+"'>"+refInt.getText()+"</a><br>";
											}
											
										}
										
										procedure += "</li></ul>";
									}
									
									listUnList = l2Item.getChildren("UNLIST");
									iteratorUnList = listUnList.iterator();
									
									while(iteratorUnList.hasNext()){
										
										Element unlist = iteratorUnList.next();
										listUnLitem = unlist.getChildren("UNLITEM");
										
										iteratorUnLitem = listUnLitem.iterator();
										while(iteratorUnLitem.hasNext()){
											Element unlitem = iteratorUnLitem.next();
											listPara3 = unlitem.getChildren("PARA");
											iteratorPara3 = listPara3.iterator();
											while (iteratorPara3.hasNext()) {
												Element para3 = (Element) iteratorPara3.next();
												procedure += "<ul><li>"+ para3.getText();
												
												listRef = para3.getChildren("REFBLOCK");
												iteratorRef = listRef.iterator();
												while(iteratorRef.hasNext()){
													Element refblock = iteratorRef.next();
													listRefInt = refblock.getChildren("REFINT");
													iteratorRefInt = listRefInt.iterator();
												    while (iteratorRefInt.hasNext()) {
														Element refInt = (Element) iteratorRefInt.next();
														procedure += "<a href='"+refInt.getAttributeValue("REFID")+"?y="+procedure.length()+"'>"+refInt.getText()+"</a><br>";
													}
													
												}
												procedure += "</li></ul>";
											}
											
										}
										
									}
									
									listWarning = l2Item.getChildren("WARNING");
									iteratorWarning = listWarning.iterator();
									
									while(iteratorWarning.hasNext()){
										Element warning = iteratorWarning.next();
										listPara4 = warning.getChildren("PARA");
										iteratorPara4 = listPara4.iterator();
										while (iteratorPara4.hasNext()) {
											Element para4 = (Element) iteratorPara4.next();
											procedure += "<ul id=warning><li>"+para4.getText();;
											
											listRef = para4.getChildren("REFBLOCK");
											iteratorRef = listRef.iterator();
											while(iteratorRef.hasNext()){
												Element refblock = iteratorRef.next();
												listRefInt = refblock.getChildren("REFINT");
												iteratorRefInt = listRefInt.iterator();
											    while (iteratorRefInt.hasNext()) {
													Element refInt = (Element) iteratorRefInt.next();
													procedure += "<a href='"+refInt.getAttributeValue("REFID")+"?y="+procedure.length()+"'>"+refInt.getText()+"</a><br>";
												}
												
											}
											procedure += "</li></ul>";
										}
									}
									
									listCaution = l2Item.getChildren("CAUTION");
									iteratorCaution = listCaution.iterator();
									
									while(iteratorCaution.hasNext()){
										Element caution = iteratorCaution.next();
										listPara5 = caution.getChildren("PARA");
										iteratorPara5 = listPara5.iterator();
										while (iteratorPara5.hasNext()) {
											Element para5 = (Element) iteratorPara5.next();
											procedure += "<ul id=caution><li>"+para5.getText();;
											
											listRef = para5.getChildren("REFBLOCK");
											iteratorRef = listRef.iterator();
											while(iteratorRef.hasNext()){
												Element refblock = iteratorRef.next();
												listRefInt = refblock.getChildren("REFINT");
												iteratorRefInt = listRefInt.iterator();
											    while (iteratorRefInt.hasNext()) {
													Element refInt = (Element) iteratorRefInt.next();
													procedure += "<a href='"+refInt.getAttributeValue("REFID")+"?y="+procedure.length()+"'>"+refInt.getText()+"</a><br>";
												}
												
											}
											procedure += "</li></ul>";
										}
									}
									
									procedure+="</ul>";
								}
								
								
							}
							procedure +="</ul>";
							
						}
					}
					procedure += "</p>";
				}
				
			}
		}
		procedure += "</body>";
		return formatText(procedure);
	}
	
	public String getCloseUp() {
		String closeUp = "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\"/><body>";
		List<Element> listTopic = racine.getChildren("TOPIC");
		List<Element> listSubTask;
		List<Element> listList1;
		List<Element> listL1Item;
		List<Element> listList2;
		List<Element> listL2Item;
		List<Element> listRefInt;
		Iterator<Element> iteratorSubTask;
		Iterator<Element> iteratorListList;
		Iterator<Element> iteratorL1Item;
		Iterator<Element> iteratorL2Item;
		Iterator<Element> iteratorRefInt;

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
									closeUp += "<li>"+paraL2Item.getText();
									Element refblock = paraL2Item.getChild("REFBLOCK");
									if(refblock!=null){
										listRefInt = refblock.getChildren("REFINT");
										iteratorRefInt = listRefInt.iterator();
									    while (iteratorRefInt.hasNext()) {
											Element refInt = (Element) iteratorRefInt.next();
											closeUp += "<a href='"+refInt.getAttributeValue("REFID")+"?y="+closeUp.length()+"'>"+refInt.getText()+"</a>";
										}
									}
									closeUp += "</li>";
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
		return formatText(closeUp);
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
		return formatText(tools+" 35.2cm  ");
	}


	public String getPictures() {
		String pictures = "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\"/><body>";
		// TODO Auto-generated method stub
		pictures += "PICTURES";
		pictures += "</body>";
		return pictures;
	}
	
	private String formatText(String text){
		// Numbers
		text = text.replaceAll("([: ])(\\d+\\.\\d+)( |[a-zA-Z]+)", "<span id='number')>$2$3</span>");
		text = text.replaceAll("([: ])(\\d+)( |[a-zA-Z]+)", "<span id='number')>$2$3</span>");
		// ....
		
		return text;
	}
}
