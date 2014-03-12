package com.eads.co.nomad;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
	private ArrayList<String> stepsWarning = new ArrayList<String>();
	private ArrayList<String> stepsJobSetup = new ArrayList<String>();
	private ArrayList<String> stepsCloseUp = new ArrayList<String>();
	private List<Element> listGrphref;
	private Iterator<Element> iteratorGrphref;
	private static String header = "<html><head><meta name=\"viewport\" content=\"minimum-scale=1\" /><link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\"/><script type=\"text/javascript\">function getPosition(element){var curtop = 0;var obj = document.getElementById(element.replace(/ /g, '..')); if (obj.offsetParent) {	do {curtop += 2*obj.offsetTop;	} while (obj = obj.offsetParent);}MyAndroid.receiveValueFromJs(curtop);}</script></head><body>";

	public DataParsing(InputStream input) throws JDOMException, IOException {
		super();
		parser = new SAXBuilder();
		setInput(input);
	}

	public ArrayList<String> getStepsProcedure() {
		return stepsProcedure;
	}

	public ArrayList<String> getStepsCloseUp() {
		return stepsCloseUp;
	}

	public ArrayList<String> getStepsJobSetup() {
		return stepsJobSetup;
	}

	public ArrayList<String> getStepsWarning() {
		return stepsWarning;
	}

	public void setInput(InputStream input) throws JDOMException, IOException {
		document = parser.build(input);
		document.getBaseURI();
		racine = document.getRootElement();
	}

	public Date getLastRevision() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		try {
	        date = formatter.parse(racine.getAttributeValue("REVDATE"));
			System.out.println(date);
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			System.out.println("DATE : "+formatter.format(date));
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
			return date;
		}
	}
	
	public String getTitle() {
		return racine.getAttributeValue("CHAPNBR") + "."
				+ racine.getAttributeValue("SECTNBR") + "."
				+ racine.getAttributeValue("SUBJNBR") + " - "
				+ racine.getChild("TITLE").getText();
	}

	public String getWarnings() {
		String warnings = header;
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
			listList = currentWarning.getChildren("UNLIST"); // Lists of warning
																// node
			iteratorListList = listList.iterator();
			listItems = currentWarning.getChildren("PARA"); // Warning para
			iteratorItems = listItems.iterator();
			/* Para */
			while (iteratorItems.hasNext()) {
				warnings += "<p id='warning'>";
				Element currentItem = (Element) iteratorItems.next();
				warnings += currentItem.getText() + "<br/>"; // Display para
																// text
				stepsWarning.add("<ul id='warning'>" + currentItem.getText()
						+ "</ul>");
				/* Display list of para is necessary */
				if (currentItem.getText().endsWith(": ")) {
					Element currentListItem = (Element) iteratorListList.next();
					listListItem = currentListItem.getChildren("UNLITEM");
					iteratorListItem = listListItem.iterator();
					warnings += "<ul id='warning'>";
					while (iteratorListItem.hasNext()) {
						Element currentListListItem = (Element) iteratorListItem
								.next();
						warnings += "<li>"
								+ currentListListItem.getChild("PARA")
										.getText() + "</li>";
						stepsWarning.add("<ul id='warning'>"
								+ currentListListItem.getChild("PARA")
										.getText() + "</ul>");
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
		String jobSetUp = header;
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
			if (topic.getChild("TITLE").getText().equals("Job Set-up")) {
				listSubTask = topic.getChildren("SUBTASK");
				iteratorSubTask = listSubTask.iterator();
				while (iteratorSubTask.hasNext()) {
					Element subTask = (Element) iteratorSubTask.next();
					jobSetUp += "<p>";
					jobSetUp += "Task : <a href='"
							+ subTask.getAttributeValue("KEY") + "'>"
							+ subTask.getAttributeValue("KEY") + "</a><br/>";
					stepsJobSetup.add("Task : <a href='"
							+ subTask.getAttributeValue("KEY") + "'>"
							+ subTask.getAttributeValue("KEY") + "</a><br/>");
					listList1 = subTask.getChildren("LIST1");
					iteratorListList = listList1.iterator();
					/* LIST1 */
					while (iteratorListList.hasNext()) {
						Element list1 = (Element) iteratorListList.next();
						listL1Item = list1.getChildren("L1ITEM");
						iteratorL1Item = listL1Item.iterator();
						while (iteratorL1Item.hasNext()) {
							Element l1Item = (Element) iteratorL1Item.next();
							jobSetUp += "<ul>";
							listPara = l1Item.getChildren("PARA");
							iteratorPara = listPara.iterator();
							while (iteratorPara.hasNext()) {
								Element para = (Element) iteratorPara.next();
								List<Element> listRef = para
										.getChildren("REFBLOCK");
								if (!listRef.isEmpty()) {
									Iterator<Element> iteratorRef = listRef
											.iterator();
									while (iteratorRef.hasNext()) {
										Element refblock = iteratorRef.next();
										List<Element> listRefInt = refblock
												.getChildren("REFINT");
										Iterator<Element> iteratorRefInt = listRefInt
												.iterator();
										while (iteratorRefInt.hasNext()) {
											Element refInt = (Element) iteratorRefInt
													.next();
											jobSetUp += "<li><span id='"
													+ refInt.getText().replaceAll(" ", "..")
													+ "'><a href="
													+ refInt.getAttributeValue("REFID").replaceAll(" ", "..")
													+ "?id=" + refInt.getText().replaceAll(" ", "..")
													+ ">" + refInt.getText()
													+ "</a></span></li>";
											stepsJobSetup
													.set(stepsJobSetup.size()-1,stepsJobSetup.get(stepsJobSetup.size()-1)+"<a href='"
															+ refInt.getAttributeValue("REFID")
															+ "?id="
															+ refInt.getText()
															+ "'>"
															+ refInt.getText()
															+ "</a>");
										}
									}
								} else {
									jobSetUp += "<li>" + para.getText()
											+ "</li>";
									stepsJobSetup.add(para.getText());
								}
							}

							List<Element> listCblst = l1Item
									.getChildren("CBLST");
							Iterator<Element> iteratorCblst = listCblst
									.iterator();
							while (iteratorCblst.hasNext()) {
								Element cblst = (Element) iteratorCblst.next();
								List<Element> listCbsublst = cblst
										.getChildren("CBSUBLST");
								Iterator<Element> iteratorCbsublst = listCbsublst
										.iterator();
								int nbCblst = 1;
								while (iteratorCbsublst.hasNext()) {
									Element cbsublst = iteratorCbsublst.next();
									List<Element> listCbdata = cbsublst
											.getChildren("CBDATA");
									Iterator<Element> iteratorCbdata = listCbdata
											.iterator();
									jobSetUp += "<ul>";
									int nbCbdata = 1;
									while (iteratorCbdata.hasNext()) {
										Element cbdata = iteratorCbdata.next();
										jobSetUp += "<li>";
										jobSetUp += cbdata
												.getChildText("CBNAME");
										jobSetUp += "<div class='onoffswitch'>";
										jobSetUp += "<input type='checkbox' name='onoffswitch"
												+ nbCbdata
												+ nbCblst
												+ "' class='onoffswitch-checkbox' id='myonoffswitch"
												+ nbCbdata
												+ nbCblst
												+ "' checked>";
										jobSetUp += "<label class='onoffswitch-label' for='myonoffswitch"
												+ nbCbdata + nbCblst + "'>";
										jobSetUp += "<div class='onoffswitch-inner'></div>";
										jobSetUp += "<div class='onoffswitch-switch'></div>";
										jobSetUp += "</label>";
										jobSetUp += "</div>";
										jobSetUp += "</li>";
										nbCbdata++;
									}
									jobSetUp += "</ul>";
									nbCblst++;
								}
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
		String procedure = header;
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
			if (topic.getChild("TITLE").getText().equals("Procedure")) {
				listSubTask = topic.getChildren("SUBTASK");
				iteratorSubTask = listSubTask.iterator();
				while (iteratorSubTask.hasNext()) {
					Element subTask = (Element) iteratorSubTask.next();
					procedure += "<p>";
					procedure += "Task : <a href='"
							+ subTask.getAttributeValue("KEY") + "'>"
							+ subTask.getAttributeValue("KEY") + "</a><br/>";
					stepsProcedure.add("Task : <a href='"
							+ subTask.getAttributeValue("KEY") + "'>"
							+ subTask.getAttributeValue("KEY") + "</a>");
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
								procedure += "<li>" + para.getText();
								stepsProcedure.add("<ul><li>" + para.getText()
										+ "</li></ul>");
								listRef = para.getChildren("REFBLOCK");
								iteratorRef = listRef.iterator();
								while (iteratorRef.hasNext()) {
									Element refblock = iteratorRef.next();
									listRefInt = refblock.getChildren("REFINT");
									iteratorRefInt = listRefInt.iterator();
									while (iteratorRefInt.hasNext()) {
										Element refInt = (Element) iteratorRefInt
												.next();
										procedure += "<span id='"
												+ refInt.getText().replaceAll(" ", "..")
												+ "'><a href="
												+ refInt.getAttributeValue("REFID").replaceAll(" ", "..")
												+ "?id=" + refInt.getText().replaceAll(" ", "..")
												+ ">" + refInt.getText()
												+ "</a></span><br>";
										stepsProcedure
												.set(stepsProcedure.size()-1,(stepsProcedure.get(stepsProcedure.size()-1).replace("</li></ul>",""))+"<a href='"
														+ refInt.getAttributeValue("REFID")
														+ "?id="
														+ refInt.getText()
														+ "'>"
														+ refInt.getText()
														+ "</a></li></ul>");
									}

								}
								
								listGrphref = para.getChildren("GRPHCREF");
								iteratorGrphref = listGrphref.iterator();
								while(iteratorGrphref.hasNext()){
									Element grphref = iteratorGrphref.next();
									stepsProcedure
									.set(stepsProcedure.size()-1,(stepsProcedure.get(stepsProcedure.size()-1).replace("</li></ul>", ""))+"<a href='"
											+ grphref.getAttributeValue("REFID")
											+ "?id="
											+ grphref.getAttributeValue("REFID")
											+ "'>"
											+ grphref.getAttributeValue("REFID")
											+ "</a></li></ul>");
								}
								
								
								procedure += "</li>";

								List<Element> listCblst = l1Item
										.getChildren("CBLST");
								Iterator<Element> iteratorCblst = listCblst
										.iterator();
								while (iteratorCblst.hasNext()) {
									Element cblst = (Element) iteratorCblst
											.next();
									List<Element> listCbsublst = cblst
											.getChildren("CBSUBLST");
									Iterator<Element> iteratorCbsublst = listCbsublst
											.iterator();
									int nbCblst = 1;
									while (iteratorCbsublst.hasNext()) {
										Element cbsublst = iteratorCbsublst
												.next();
										List<Element> listCbdata = cbsublst
												.getChildren("CBDATA");
										Iterator<Element> iteratorCbdata = listCbdata
												.iterator();
										procedure += "<ul>";
										int nbCbdata = 1;
										while (iteratorCbdata.hasNext()) {
											Element cbdata = iteratorCbdata
													.next();
											procedure += "<li>";
											procedure += cbdata
													.getChildText("CBNAME");
											procedure += "<div class='onoffswitch'>";
											procedure += "<input type='checkbox' name='onoffswitch"
													+ nbCbdata
													+ nbCblst
													+ "' class='onoffswitch-checkbox' id='myonoffswitch"
													+ nbCbdata
													+ nbCblst
													+ "' checked>";
											procedure += "<label class='onoffswitch-label' for='myonoffswitch"
													+ nbCbdata + nbCblst + "'>";
											procedure += "<div class='onoffswitch-inner'></div>";
											procedure += "<div class='onoffswitch-switch'></div>";
											procedure += "</label>";
											procedure += "</div>";
											procedure += "</li>";
											nbCbdata++;
										}
										procedure += "</ul>";
										nbCblst++;
									}
								}
							}
							listList2 = l1Item.getChildren("LIST2");
							iteratorList2 = listList2.iterator();
							/* LIST2 */
							while (iteratorList2.hasNext()) {
								Element list2 = (Element) iteratorList2.next();
								listL2Item = list2.getChildren("L2ITEM");
								iteratorL2Item = listL2Item.iterator();
								while (iteratorL2Item.hasNext()) {
									Element l2Item = (Element) iteratorL2Item
											.next();
									listPara2 = l2Item.getChildren("PARA");

									iteratorPara2 = listPara2.iterator();
									procedure += "<ul>";

									while (iteratorPara2.hasNext()) {
										Element para2 = (Element) iteratorPara2
												.next();
										procedure += "<li>" + para2.getText();
										stepsProcedure.set(stepsProcedure.size()-1,stepsProcedure.get(stepsProcedure.size()-1)+"<ul><ul><li>"
												+ para2.getText()
												+ "</li></ul></ul>");
										listRef = para2.getChildren("REFBLOCK");
										iteratorRef = listRef.iterator();
										while (iteratorRef.hasNext()) {
											Element refblock = iteratorRef
													.next();
											listRefInt = refblock
													.getChildren("REFINT");
											iteratorRefInt = listRefInt
													.iterator();
											while (iteratorRefInt.hasNext()) {
												Element refInt = (Element) iteratorRefInt
														.next();
												procedure += "<span id='"
														+ refInt.getText().replaceAll(" ", "..")
														+ "'><a href="
														+ refInt.getAttributeValue("REFID").replaceAll(" ", "..")
														+ "?id="
														+ refInt.getText().replaceAll(" ", "..")
														+ ">"
														+ refInt.getText()
														+ "</a></span><br>";
												stepsProcedure
														.set(stepsProcedure.size()-1,(stepsProcedure.get(stepsProcedure.size()-1).replace("</li></ul></ul>",""))+"<a href='"
																+ refInt.getAttributeValue("REFID")
																+ "?id="
																+ refInt.getText()
																+ "'>"
																+ refInt.getText()
																+ "</a></li></ul></ul>");
											}

										}

										procedure += "</li>";
									}

									listUnList = l2Item.getChildren("UNLIST");
									iteratorUnList = listUnList.iterator();

									while (iteratorUnList.hasNext()) {

										Element unlist = iteratorUnList.next();
										listUnLitem = unlist
												.getChildren("UNLITEM");

										iteratorUnLitem = listUnLitem
												.iterator();
										while (iteratorUnLitem.hasNext()) {
											Element unlitem = iteratorUnLitem
													.next();
											listPara3 = unlitem
													.getChildren("PARA");
											iteratorPara3 = listPara3
													.iterator();
											while (iteratorPara3.hasNext()) {
												Element para3 = (Element) iteratorPara3
														.next();
												procedure += "<ul><li>"
														+ para3.getText();
												stepsProcedure.set(stepsProcedure.size()-1,stepsProcedure.get(stepsProcedure.size()-1)+"<ul><ul><ul><li>"
														+ para3.getText()
														+ "</li></ul></ul></ul>");
												listRef = para3
														.getChildren("REFBLOCK");
												iteratorRef = listRef
														.iterator();
												while (iteratorRef.hasNext()) {
													Element refblock = iteratorRef
															.next();
													listRefInt = refblock
															.getChildren("REFINT");
													iteratorRefInt = listRefInt
															.iterator();
													while (iteratorRefInt
															.hasNext()) {
														Element refInt = (Element) iteratorRefInt
																.next();
														procedure += "<span id='"
																+ refInt.getText().replaceAll(" ", "..")
																+ "'><a href="
																+ refInt.getAttributeValue("REFID").replaceAll(" ", "..")
																+ "?id="
																+ refInt.getText().replaceAll(" ", "..")
																+ ">"
																+ refInt.getText()
																+ "</a></span><br>";
														stepsProcedure
																.set(stepsProcedure.size()-1,(stepsProcedure.get(stepsProcedure.size()-1).replace("</li></ul></ul></ul>",""))+"<a href='"
																		+ refInt.getAttributeValue("REFID")
																		+ "?id="
																		+ refInt.getText()
																		+ "'>"
																		+ refInt.getText()
																		+ "</a></li></ul></ul></ul>");
													}

												}
												procedure += "</li></ul>";
											}

										}

									}
									procedure += "</ul>";
									listWarning = l2Item.getChildren("WARNING");
									iteratorWarning = listWarning.iterator();

									while (iteratorWarning.hasNext()) {
										Element warning = iteratorWarning
												.next();
										listPara4 = warning.getChildren("PARA");
										iteratorPara4 = listPara4.iterator();
										while (iteratorPara4.hasNext()) {
											Element para4 = (Element) iteratorPara4
													.next();
											procedure += "<ul id=warning><li>"
													+ para4.getText();
											;
											stepsProcedure
													.add("<ul id=warning>"
															+ para4.getText()
															+ "</ul>");
											listRef = para4
													.getChildren("REFBLOCK");
											iteratorRef = listRef.iterator();
											while (iteratorRef.hasNext()) {
												Element refblock = iteratorRef
														.next();
												listRefInt = refblock
														.getChildren("REFINT");
												iteratorRefInt = listRefInt
														.iterator();
												while (iteratorRefInt.hasNext()) {
													Element refInt = (Element) iteratorRefInt
															.next();
													procedure += "<span id='"
															+ refInt.getText().replaceAll(" ", "..")
															+ "'><a href="
															+ refInt.getAttributeValue("REFID").replaceAll(" ", "..")
															+ "?id="
															+ refInt.getText().replaceAll(" ", "..")
															+ ">"
															+ refInt.getText()
															+ "</a></span><br>";
													stepsProcedure
															.set(stepsProcedure.size()-1,(stepsProcedure.get(stepsProcedure.size()-1))+"<a href='"
																	+ refInt.getAttributeValue("REFID")
																	+ "?id="
																	+ refInt.getText()
																	+ "'>"
																	+ refInt.getText()
																	+ "</a><br>");
												}

											}
											procedure += "</li></ul>";
										}
									}

									listCaution = l2Item.getChildren("CAUTION");
									iteratorCaution = listCaution.iterator();

									while (iteratorCaution.hasNext()) {
										Element caution = iteratorCaution
												.next();
										listPara5 = caution.getChildren("PARA");
										iteratorPara5 = listPara5.iterator();
										while (iteratorPara5.hasNext()) {
											Element para5 = (Element) iteratorPara5
													.next();
											procedure += "<ul id=caution><li>"
													+ para5.getText();
											;
											stepsProcedure
													.add("<ul id=caution>"
															+ para5.getText()
															+ "</ul>");
											listRef = para5
													.getChildren("REFBLOCK");
											iteratorRef = listRef.iterator();
											while (iteratorRef.hasNext()) {
												Element refblock = iteratorRef
														.next();
												listRefInt = refblock
														.getChildren("REFINT");
												iteratorRefInt = listRefInt
														.iterator();
												while (iteratorRefInt.hasNext()) {
													Element refInt = (Element) iteratorRefInt
															.next();
													procedure += "<span id='"
															+ refInt.getText().replaceAll(" ", "..")
															+ "'><a href="
															+ refInt.getAttributeValue("REFID").replaceAll(" ", "..")
															+ "?id="
															+ refInt.getText().replaceAll(" ", "..")
															+ ">"
															+ refInt.getText()
															+ "</a></span><br>";
													stepsProcedure
															.set(stepsProcedure.size()-1,stepsProcedure.get(stepsProcedure.size()-1)+"<a href='"
																	+ refInt.getAttributeValue("REFID")
																	+ "?id="
																	+ refInt.getText()
																	+ "'>"
																	+ refInt.getText()
																	+ "</a><br>");
												}

											}
											procedure += "</li></ul>";
										}
									}

									procedure += "</ul>";
								}

							}
							procedure += "</ul>";

						}
					}
					procedure += "</p>";
				}

			}
		}
		procedure += "</body></html>";
		return formatText(procedure);
	}

	public String getCloseUp() {
		String closeUp = header;
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
			if (topic.getChild("TITLE").getText().equals("Close-up")) {
				listSubTask = topic.getChildren("SUBTASK");
				iteratorSubTask = listSubTask.iterator();
				while (iteratorSubTask.hasNext()) {
					Element subTask = (Element) iteratorSubTask.next();
					closeUp += "<p>";
					closeUp += "Task : <a href='"
							+ subTask.getAttributeValue("KEY") + "'>"
							+ subTask.getAttributeValue("KEY") + "</a><br/>";
					stepsCloseUp.add("Task : <a href='"
							+ subTask.getAttributeValue("KEY") + "'>"
							+ subTask.getAttributeValue("KEY") + "</a>");
					listList1 = subTask.getChildren("LIST1");
					iteratorListList = listList1.iterator();
					/* LIST1 */
					while (iteratorListList.hasNext()) {
						Element list1 = (Element) iteratorListList.next();
						listL1Item = list1.getChildren("L1ITEM");
						iteratorL1Item = listL1Item.iterator();
						closeUp += "<ul>";
						while (iteratorL1Item.hasNext()) {
							Element l1Item = (Element) iteratorL1Item.next();
							Element para = l1Item.getChild("PARA");
							closeUp += "<li>" + para.getText() + "</li>";
							stepsCloseUp.add("<ul><li>" + para.getText() + "</li></ul>");
							List<Element> listCblst = l1Item
									.getChildren("CBLST");
							Iterator<Element> iteratorCblst = listCblst
									.iterator();
							while (iteratorCblst.hasNext()) {
								int nbCblst = 1;
								Element cblst = (Element) iteratorCblst.next();
								List<Element> listCbsublst = cblst
										.getChildren("CBSUBLST");
								Iterator<Element> iteratorCbsublst = listCbsublst
										.iterator();
								while (iteratorCbsublst.hasNext()) {
									Element cbsublst = iteratorCbsublst.next();
									List<Element> listCbdata = cbsublst
											.getChildren("CBDATA");
									Iterator<Element> iteratorCbdata = listCbdata
											.iterator();
									closeUp += "<ul>";
									int nbCbdata = 1;
									while (iteratorCbdata.hasNext()) {
										Element cbdata = iteratorCbdata.next();
										closeUp += "<li>";
										closeUp += cbdata
												.getChildText("CBNAME");
										closeUp += "<div class='onoffswitch'>";
										closeUp += "<input type='checkbox' name='onoffswitch"
												+ nbCbdata
												+ nbCblst
												+ "' class='onoffswitch-checkbox' id='myonoffswitch"
												+ nbCbdata
												+ nbCblst
												+ "' checked>";
										closeUp += "<label class='onoffswitch-label' for='myonoffswitch"
												+ nbCbdata + nbCblst + "'>";
										closeUp += "<div class='onoffswitch-inner'></div>";
										closeUp += "<div class='onoffswitch-switch'></div>";
										closeUp += "</label>";
										closeUp += "</div>";
										closeUp += "</li>";
										nbCbdata++;
									}
									closeUp += "</ul>";
									nbCblst++;
								}
							}

							listList2 = l1Item.getChildren("LIST2");
							iteratorL2Item = listList2.iterator();
							while (iteratorL2Item.hasNext()) {
								Element list2 = (Element) iteratorL2Item.next();
								listL2Item = list2.getChildren("L2ITEM");
								iteratorL2Item = listL2Item.iterator();
								closeUp += "<ul>";
								while (iteratorL2Item.hasNext()) {
									Element l2Item = (Element) iteratorL2Item
											.next();
									Element paraL2Item = l2Item
											.getChild("PARA");
									closeUp += "<li>" + paraL2Item.getText();
									stepsCloseUp.add("<ul><li>"+paraL2Item.getText()+"</li></ul>");
									Element refblock = paraL2Item
											.getChild("REFBLOCK");
									if (refblock != null) {
										listRefInt = refblock
												.getChildren("REFINT");
										iteratorRefInt = listRefInt.iterator();
										while (iteratorRefInt.hasNext()) {
											Element refInt = (Element) iteratorRefInt
													.next();
											closeUp += "<span id='"
													+ refInt.getText().replaceAll(" ", "..")
													+ "'><a href="
													+ refInt.getAttributeValue("REFID").replaceAll(" ", "..")
													+ "?id=" + refInt.getText().replaceAll(" ", "..")
													+ ">" + refInt.getText()
													+ "</a></span>";
											stepsCloseUp.set(stepsCloseUp.size()-1,(stepsCloseUp.get(stepsCloseUp.size()-1).replace("</li></ul>", ""))+"<a href='"
													+ refInt.getAttributeValue("REFID")
													+ "?id="
													+ refInt.getText()
													+ "'>"
													+ refInt.getText()
													+ "</a></li></ul>");
													
										}
									}
									closeUp += "</li>";
								}
								closeUp += "</ul>";
							}
						}
						closeUp += "</ul>";
					}
					closeUp += "</p>";
				}

			}
		}
		closeUp += "</body></html>";
		return formatText(closeUp);
	}

	public String getTools() {
		String tools = header;
		List<Element> listPretopic = racine.getChild("TFMATR").getChildren(
				"PRETOPIC");
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
			if (topic.getChild("TITLE").getText()
					.equals("Job Set-up Information")) {
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
							tools += "<b>" + para.getText() + "</b>";
						}
						Element table = l1Item.getChild("TABLE");
						tools += "<table>";
						Element tgGroup = table.getChild("TGROUP");
						Element spanSpec = tgGroup.getChild("COLSPEC");
						int nbCol = Integer.parseInt(tgGroup
								.getAttributeValue("COLS"));
						while (1 < nbCol) {
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
							if (para == null) {
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
									Element paraCurrent = (Element) iteratorPara
											.next();
									Element para = paraCurrent;
									para = para.getChild("CON");
									if (para != null) {
										para = para.getChild("CONNBR");
										tools += para.getText();
									} else {
										para = paraCurrent.getChild("STDNAME");
										if (para != null) {
											tools += para.getText();
										} else {
											para = paraCurrent.getChild("ZONE");
											if (para != null) {
												tools += para.getText();
											} else {
												para = paraCurrent
														.getChild("REFINT");
												if (para != null) {
													tools += "<span id='"
															+ para.getText().replaceAll(" ", "..")
															+ "'><a href="
															+ para.getAttributeValue("REFID").replaceAll(" ", "..")
															+ "?id="
															+ para.getText().replaceAll(" ", "..")
															+ ">"
															+ para.getText()
															+ "</a></span>";
												} else {
													para = paraCurrent
															.getChild("GRPHCREF");
													if (para != null) {
														tools += "</td><td><span id='"
																+ para.getText().replaceAll(" ", "..")
																+ "'><a href="
																+ para.getAttributeValue("REFID").replaceAll(" ", "..")
																+ "?id="
																+ para.getText().replaceAll(" ", "..")
																+ ">"
																+ para.getText()
																+ "</a></span>";
													} else {
														List<Element> listPan = paraCurrent
																.getChildren("PAN");
														if (!listPan.isEmpty()) {
															Iterator<Element> iteratorPan = listPan
																	.iterator();
															while (iteratorPan
																	.hasNext()) {
																Element panCurrent = (Element) iteratorPan
																		.next();
																tools += panCurrent
																		.getText();
																if (iteratorPan
																		.hasNext()) {
																	tools += ",&nbsp;";
																}
															}
														} else {
															tools += paraCurrent
																	.getText();
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
		String pictures = header;
		List<Element> listPretopic = racine.getChild("TFMATR").getChildren(
				"PRETOPIC");
		List<Element> listList1;
		List<Element> listL1Item;
		List<Element> listTable;
		List<Element> listTgroup;
		List<Element> listColspec;
		List<Element> listColspec2;
		List<Element> listSpanSpec;
		List<Element> listTbody;
		List<Element> listRow;
		List<Element> listEntry;
		List<Element> listPara;
		List<Element> listrefint;
		Iterator<Element> iteratorL1Item;
		Iterator<Element> iteratorPara;
		Iterator<Element> iteratorTopics = listPretopic.iterator();
		Iterator<Element> iteratorList1;
		Iterator<Element> iteratorTable;
		Iterator<Element> iteratorTgroup;
		Iterator<Element> iteratorColspec;
		Iterator<Element> iteratorColspec2;
		Iterator<Element> iteratorSpanSpec;
		Iterator<Element> iteratorTbody;
		Iterator<Element> iteratorRow;
		Iterator<Element> iteratorEntry;
		Iterator<Element> iteratorrefint;
		pictures += "<br><table><tr><th width='20%'>TITLE</th><th width='80%'>PICTURE</th></tr>";
		while (iteratorTopics.hasNext()) {
			Element topic = iteratorTopics.next();
			listList1 = topic.getChildren("LIST1");
			iteratorList1 = listList1.iterator();
			while (iteratorList1.hasNext()) {
				Element list1 = iteratorList1.next();
				listL1Item = list1.getChildren("L1ITEM");
				iteratorL1Item = listL1Item.iterator();
				while (iteratorL1Item.hasNext()) {
					Element l1item = iteratorL1Item.next();
					listTable = l1item.getChildren("TABLE");
					iteratorTable = listTable.iterator();
					while (iteratorTable.hasNext()) {
						Element table = iteratorTable.next();
						listTgroup = table.getChildren("TGROUP");
						iteratorTgroup = listTgroup.iterator();
						while (iteratorTgroup.hasNext()) {
							Element tGroup = iteratorTgroup.next();
							listColspec = tGroup.getChildren("COLSPEC");
							iteratorColspec = listColspec.iterator();
							while (iteratorColspec.hasNext()) {
								Element colspec = iteratorColspec.next();
								listColspec2 = colspec.getChildren("COLSPEC");
								iteratorColspec2 = listColspec2.iterator();
								while (iteratorColspec2.hasNext()) {
									Element colspec2 = iteratorColspec2.next();
									listSpanSpec = colspec2
											.getChildren("SPANSPEC");
									iteratorSpanSpec = listSpanSpec.iterator();
									while (iteratorSpanSpec.hasNext()) {
										Element spanspec = iteratorSpanSpec
												.next();
										listTbody = spanspec
												.getChildren("TBODY");
										iteratorTbody = listTbody.iterator();
										while (iteratorTbody.hasNext()) {
											Element tBody = iteratorTbody
													.next();
											listRow = tBody.getChildren("ROW");
											iteratorRow = listRow.iterator();
											while (iteratorRow.hasNext()) {
												Element row = iteratorRow
														.next();
												listEntry = row
														.getChildren("ENTRY");
												iteratorEntry = listEntry
														.iterator();
												while (iteratorEntry.hasNext()) {
													Element entry = iteratorEntry
															.next();
													listPara = entry
															.getChildren("PARA");
													iteratorPara = listPara
															.iterator();
													while (iteratorPara
															.hasNext()) {
														Element para = iteratorPara
																.next();
														listrefint = para
																.getChildren("REFINT");
														iteratorrefint = listrefint
																.iterator();
														while (iteratorrefint
																.hasNext()) {
															Element refint = iteratorrefint
																	.next();
															pictures += "<tr><td>" +refint.getText() 
																	+ "</td><td><img src='ata.jpg' width='100%'/></td></tr>";
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		pictures += "</table><br>";

		pictures += "</body></html>";
		return pictures;
	}

	private String formatText(String text) {
		// Mesures
		text = text.replaceAll("(\\d+\\.\\d+) (mm|cm|in)",
				"<span id='number')>$1 $2</span>"); // double
		text = text.replaceAll("(\\d+) (mm|cm|in)",
				"<span id='number')>$1 $2</span>"); // int
		// ....

		return text;
	}

}
