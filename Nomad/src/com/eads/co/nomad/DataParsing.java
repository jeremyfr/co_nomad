package com.eads.co.nomad;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
	private ArrayList<String> stepsProcedure = new ArrayList<String>();
	public DataParsing(InputStream input) throws JDOMException, IOException {
		super();
		parser = new SAXBuilder();
		setInput(input);
	}


	public ArrayList<String> getStepsProcedure() {
		return stepsProcedure;
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
		String warnings = "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\"/><script type=\"text/javascript\">function getPosition(element){var curtop = 0;var obj = document.getElementById(element); if (obj.offsetParent) {	do {curtop += 1.3308*obj.offsetTop;	} while (obj = obj.offsetParent);}MyAndroid.receiveValueFromJs(curtop);}</script></head><body>";
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
		warnings += "</body></html>";
		return warnings;
	}
	
	public String getJobSetUp() {
		String jobSetUp = "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\"/><script type=\"text/javascript\">function getPosition(element){var curtop = 0;var obj = document.getElementById(element); if (obj.offsetParent) {	do {curtop += 1.3308*obj.offsetTop;	} while (obj = obj.offsetParent);}MyAndroid.receiveValueFromJs(curtop);}</script></head><body>";
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
		jobSetUp += "</body></html>";
		return formatText(jobSetUp);
	}

	public String getProcedure() {
		String procedure = "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\"/><script type=\"text/javascript\">function getPosition(element){var curtop = 0;var obj = document.getElementById(element); if (obj.offsetParent) {	do {curtop += 1.3308*obj.offsetTop;	} while (obj = obj.offsetParent);}MyAndroid.receiveValueFromJs(curtop);}</script></head><body>";
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
								stepsProcedure.add(para.getText());
								listRef = para.getChildren("REFBLOCK");
								iteratorRef = listRef.iterator();
								while(iteratorRef.hasNext()){
									Element refblock = iteratorRef.next();
									listRefInt = refblock.getChildren("REFINT");
									iteratorRefInt = listRefInt.iterator();
								    while (iteratorRefInt.hasNext()) {
										Element refInt = (Element) iteratorRefInt.next();
										procedure += "<a href='"+refInt.getAttributeValue("REFID")+"?y="+procedure.length()+"'>"+refInt.getText()+"</a><br>";
										stepsProcedure.add("<a href='"+refInt.getAttributeValue("REFID")+"?y="+procedure.length()+"'>"+refInt.getText()+"</a><br>");
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
										stepsProcedure.add(para2.getText());
										listRef = para2.getChildren("REFBLOCK");
										iteratorRef = listRef.iterator();
										while(iteratorRef.hasNext()){
											Element refblock = iteratorRef.next();
											listRefInt = refblock.getChildren("REFINT");
											iteratorRefInt = listRefInt.iterator();
										    while (iteratorRefInt.hasNext()) {
												Element refInt = (Element) iteratorRefInt.next();
												procedure += "<a href='"+refInt.getAttributeValue("REFID")+"?y="+procedure.length()+"'>"+refInt.getText()+"</a><br>";
												stepsProcedure.add("<a href='"+refInt.getAttributeValue("REFID")+"?y="+procedure.length()+"'>"+refInt.getText()+"</a><br>");
											}
											
										}
										
										procedure += "</li>";
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
												stepsProcedure.add(para3.getText());
												listRef = para3.getChildren("REFBLOCK");
												iteratorRef = listRef.iterator();
												while(iteratorRef.hasNext()){
													Element refblock = iteratorRef.next();
													listRefInt = refblock.getChildren("REFINT");
													iteratorRefInt = listRefInt.iterator();
												    while (iteratorRefInt.hasNext()) {
														Element refInt = (Element) iteratorRefInt.next();
														procedure += "<a href='"+refInt.getAttributeValue("REFID")+"?y="+procedure.length()+"'>"+refInt.getText()+"</a><br>";
														stepsProcedure.add("<a href='"+refInt.getAttributeValue("REFID")+"?y="+procedure.length()+"'>"+refInt.getText()+"</a><br>");
													}
													
												}
												procedure += "</li></ul>";
											}
											
										}
										
									}
									procedure +="</ul>";
									listWarning = l2Item.getChildren("WARNING");
									iteratorWarning = listWarning.iterator();
									
									while(iteratorWarning.hasNext()){
										Element warning = iteratorWarning.next();
										listPara4 = warning.getChildren("PARA");
										iteratorPara4 = listPara4.iterator();
										while (iteratorPara4.hasNext()) {
											Element para4 = (Element) iteratorPara4.next();
											procedure += "<ul id=warning><li>"+para4.getText();;
											stepsProcedure.add(para4.getText());
											listRef = para4.getChildren("REFBLOCK");
											iteratorRef = listRef.iterator();
											while(iteratorRef.hasNext()){
												Element refblock = iteratorRef.next();
												listRefInt = refblock.getChildren("REFINT");
												iteratorRefInt = listRefInt.iterator();
											    while (iteratorRefInt.hasNext()) {
													Element refInt = (Element) iteratorRefInt.next();
													procedure += "<a href='"+refInt.getAttributeValue("REFID")+"?y="+procedure.length()+"'>"+refInt.getText()+"</a><br>";
													stepsProcedure.add("<a href='"+refInt.getAttributeValue("REFID")+"?y="+procedure.length()+"'>"+refInt.getText()+"</a><br>");
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
											stepsProcedure.add(para5.getText());
											listRef = para5.getChildren("REFBLOCK");
											iteratorRef = listRef.iterator();
											while(iteratorRef.hasNext()){
												Element refblock = iteratorRef.next();
												listRefInt = refblock.getChildren("REFINT");
												iteratorRefInt = listRefInt.iterator();
											    while (iteratorRefInt.hasNext()) {
													Element refInt = (Element) iteratorRefInt.next();
													procedure += "<a href='"+refInt.getAttributeValue("REFID")+"?y="+procedure.length()+"'>"+refInt.getText()+"</a><br>";
													stepsProcedure.add("<a href='"+refInt.getAttributeValue("REFID")+"?y="+procedure.length()+"'>"+refInt.getText()+"</a><br>");
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
		procedure += "<div id='test'><p>Ligne test pour l'infobulle...</p></div>";
		procedure += "</body></html>";
		return formatText(procedure);
	}
	
	public String getCloseUp() {
		String closeUp = "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\"/><script type=\"text/javascript\">function getPosition(element){var curtop = 0;var obj = document.getElementById(element); if (obj.offsetParent) {	do {curtop += 1.3308*obj.offsetTop;	} while (obj = obj.offsetParent);}MyAndroid.receiveValueFromJs(curtop);}</script></head><body>";
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
				listSubTask = topic.getChildren("SUBTASK");
				iteratorSubTask = listSubTask.iterator();
				while (iteratorSubTask.hasNext()) {
					Element subTask = (Element) iteratorSubTask.next();
					closeUp += "<p>";
					closeUp += "Task : <a href='"+subTask.getAttributeValue("KEY")+"'>"+subTask.getAttributeValue("KEY")+"</a><br/>";
					
					listList1 = subTask.getChildren("LIST1");
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
		closeUp += "</body></html>";
		return formatText(closeUp);
	}
	
	public String getTools() {
		String tools = "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\"/><script type=\"text/javascript\">function getPosition(element){var curtop = 0;var obj = document.getElementById(element); if (obj.offsetParent) {	do {curtop += 1.3308*obj.offsetTop;	} while (obj = obj.offsetParent);}MyAndroid.receiveValueFromJs(curtop);}</script></head><body>";
		List<Element> listPretopic = racine.getChild("TFMATR").getChildren("PRETOPIC");
		List<Element> listList1;
		List<Element> listL1Item;
		List<Element> listPara;
		List<Element> listRow;
		List<Element> listEntry;
		Iterator<Element> iteratorList1;
		Iterator<Element> iteratorL1Item;
		Iterator<Element> iteratorPara;
		Iterator<Element> iteratorRow;
		Iterator<Element> iteratorEntry;

		Iterator<Element> iteratorTopics = listPretopic.iterator();
		/* Topics */
		while (iteratorTopics.hasNext()) {
			Element topic = (Element) iteratorTopics.next();
			/* Choix de la bonne partie */
			if(topic.getChild("TITLE").getText().equals("Job Set-up Information")){
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
						Element tgGroup = table.getChild("TGROUP");
						Element spanSpec = tgGroup.getChild("COLSPEC");
						int nbCol = Integer.parseInt(tgGroup.getAttributeValue("COLS"));
						while(1 < nbCol){
							spanSpec = spanSpec.getChild("COLSPEC");
							nbCol--;
						}
						spanSpec = spanSpec.getChild("SPANSPEC");
						Element thead = spanSpec.getChild("THEAD");
						Element row = thead.getChild("ROW");
						listEntry = row.getChildren("ENTRY");
						iteratorEntry = listEntry.iterator();
						tools += "<tr>";
						while (iteratorEntry.hasNext()) {
							tools += "<th>";
							Element entry = (Element) iteratorEntry.next();
							Element para = entry.getChild("PARA");
							if(para == null){
								para = row.getChild("STDNAME");
							}
							tools += para.getText();
							tools += "</th>";
						}
						tools += "</tr>";

						Element tbody = spanSpec.getChild("TBODY");
						listRow = tbody.getChildren("ROW");
						iteratorRow = listRow.iterator();
						while (iteratorRow.hasNext()) {
							row = (Element) iteratorRow.next();
							listEntry = row.getChildren("ENTRY");
							iteratorEntry = listEntry.iterator();
							tools += "<tr>";
							while (iteratorEntry.hasNext()) {
								tools += "<td>";
								Element entry = (Element) iteratorEntry.next();
								listPara = entry.getChildren("PARA");
								iteratorPara = listPara.iterator();
								while (iteratorPara.hasNext()) {
									Element paraCurrent = (Element) iteratorPara.next();
									Element para = paraCurrent;
									para = para.getChild("CON");
									if(para != null){
										para = para.getChild("CONNBR");
										tools += para.getText();
									}else{
										para = paraCurrent.getChild("STDNAME");
										if(para != null){
											tools += para.getText();
										}else{
											para = paraCurrent.getChild("ZONE");
											if(para != null){
												tools += para.getText();
											}else{
												para = paraCurrent.getChild("REFINT");
												if(para != null){
													tools += "<a href='"+para.getAttributeValue("REFID")+"?y="+tools.length()+"'>"+para.getText()+"</a><br>";
												}else{
													para = paraCurrent.getChild("GRPHCREF");
													if(para != null){
														tools += "<a href='"+para.getAttributeValue("REFID")+"?y="+tools.length()+"'>"+para.getText()+"</a><br>";
													}else{
														List<Element> listPan = paraCurrent.getChildren("PAN");
														if(!listPan.isEmpty()){
															Iterator<Element> iteratorPan = listPan.iterator();
															while (iteratorPan.hasNext()) {
																Element panCurrent = (Element) iteratorPan.next();
																tools += panCurrent.getText();
																if(iteratorPan.hasNext()){
																	tools += ",&nbsp;";
																}
															}
														}else{
															tools += paraCurrent.getText();
														}
													}
												}
											}
										}
									}
								}
								tools += "</td>";
							}
							tools += "</tr>";
						}
						tools += "</table>";
					}
				}
			}
		}
		tools += "</body></html>";
		return tools;
	}


	public String getPictures() {
		String pictures = "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\"/><script type=\"text/javascript\">function getPosition(element){var curtop = 0;var obj = document.getElementById(element); if (obj.offsetParent) {	do {curtop += 1.3308*obj.offsetTop;	} while (obj = obj.offsetParent);}MyAndroid.receiveValueFromJs(curtop);}</script></head><body>";
		// TODO Auto-generated method stub
		pictures += "PICTURES";
		pictures += "</body></html>";
		return pictures;
	}
	
	private String formatText(String text){
		// Mesures
		text = text.replaceAll("(\\d+\\.\\d+) (mm|cm|in)", "<span id='number')>$1 $2</span>"); // double
		text = text.replaceAll("(\\d+) (mm|cm|in)", "<span id='number')>$1 $2</span>");  // int
		// ....
		
		return text;
	}
}
