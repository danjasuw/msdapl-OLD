
<%@page import="org.yeastrc.bio.go.GOUtils"%><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/yrc-www.tld" prefix="yrcwww" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<html>

<head>
 <title>Gene Ontology Term Search</title>
 	<link REL="stylesheet" TYPE="text/css" HREF="<yrcwww:link path='/css/global.css' />">
 	<link REL="stylesheet" TYPE="text/css" HREF="<yrcwww:link path='/css/jstree.css' />">
</head>

<body>
<script src="<yrcwww:link path='js/jquery.ui-1.6rc2/jquery-1.2.6.js'/>"></script>
<script src="<yrcwww:link path='js/msdapl.js'/>"></script>
<script>
$(document).ready(function() {

   $(".table_basic").each(function() {
   		makeStripedTable(this);
   		makeSortableTable(this);
   });
   $("#allSelector").click(function() {
   		selectToggle();
   });
   $(".selectedAdder").click(function() {
   		addSelected();
   });
});

function selectToggle() {
	
	var txt = $("#allSelector").val();
	if(txt == "Select ALL") {
		$("#allSelector").val("Deselect ALL");
		$(".selectable").attr("checked", "checked");
	}
	else {
		$("#allSelector").val("Select ALL");
		$(".selectable").attr("checked", "");
	}
}

function addSelected() {

	var selected = $(".selectable:checked").length
	var selectedArr = [selected];
	var index = 0;
	$(".selectable:checked").each(function() {
		selectedArr[index] = $(this).attr('id');
		index++;
	});
	window.opener.addToGoSearchTerms(selectedArr);
	window.close();
}
</script>

<%@ include file="/includes/errors.jsp" %>
<div style="margin: 10 0 10 0;">
<yrcwww:contentbox centered="true" title="Gene Ontology Term Search" width="95" widthRel="true">
	
	<html:form action="goTermSearch.do">
		<center>
		<div style="padding:10px; background-color:#E9EFFA; border:1px solid gray; width:80%;">
		<table align="center" cellpadding="3">
		<tr>
		<td>
			<html:text name="goTermSearchForm" property="terms" size="50"></html:text>
			<html:submit value="Search" styleClass="plain_button"></html:submit>
		</td>
		</tr>
		<tr>
		<td>
			Match: 
			<html:radio name="goTermSearchForm"  property="matchAllTerms" value="true"><b>ALL</b> words</html:radio>
			<html:radio name="goTermSearchForm"  property="matchAllTerms" value="false"><b>ANY</b> words</html:radio>
		</td>
		</tr>
		<tr>
		<td>
			Search Term Synonyms <html:checkbox name="goTermSearchForm" property="searchTermSynonyms"></html:checkbox>
			<br/>
			<span style="font-size:8pt;color:gray">If the box is checked both term names and synonyms are searched. 
			Otherwise only term names are searched.</span>
		</td>
		</tr>
		<tr>
		<td>
		<html:checkbox name="goTermSearchForm"  property="biologicalProcess"></html:checkbox>Biological Process &nbsp;
		<html:checkbox name="goTermSearchForm"  property="cellularComponent"></html:checkbox>Cellular Component &nbsp;
		<html:checkbox name="goTermSearchForm"  property="molecularFunction"></html:checkbox>Molecular Function
		</td>
		</tr>
		</table>
		</div>
		</center>
	</html:form>
	
	<logic:present name="nodes">
	<center>
	<div align="center" style="width:90%; padding-bottom:10px;">
	<table width="100%" align="center">
		<tr>
			<td align="left" valign="bottom">
				<span style="color:red;"><b><bean:write name="numNodes"/> matching GO terms found.</b></span>
			</td>
			<td align="right">
				<logic:greaterThan name="numNodes" value="0">
					<input type="button" class="plain_button" id="allSelector" value="Select ALL"/>
					<input type="button" value="Add Selected" class="plain_button selectedAdder"/>
				</logic:greaterThan>
			</td>
		</tr>
	</table>
	
	<logic:greaterThan name="numNodes" value="0">
	<table class="table_basic sortable" width="100%">
	<thead>
		<tr>
			<th></th>
			<th class="sort-alpha clickable">Term ID</th>
			<th class="sort-alpha clickable">Domain</th>
			<th class="sort-alpha clickable">Name</th>
		</tr>
	</thead>
	<tbody>
		<logic:iterate name="nodes" id="node">
		<tr>
			<td>
				<input type="checkbox" class="selectable" id="<bean:write name='node' property='accession'/>"/>
			</td>
			<td><bean:write name="node" property="accession"/></td>
			<td align="center"><b>
			<logic:equal name="node" property="aspect" value="<%=String.valueOf(GOUtils.BIOLOGICAL_PROCESS) %>">
				<span style="color:red;" title="Biological Process">BP</span></logic:equal>
			<logic:equal name="node" property="aspect" value="<%=String.valueOf(GOUtils.MOLECULAR_FUNCTION) %>">
				<span style="color:blue;" title="Molecular Function">MF</span></logic:equal>
			<logic:equal name="node" property="aspect" value="<%=String.valueOf(GOUtils.CELLULAR_COMPONENT) %>">
				<span style="color:green;" title="Cellular Component">CC</span></logic:equal>
			</b></td>
			<td>
				<bean:write name="node" property="name"/>
				<logic:notEmpty name="node" property="synonymsE">
					<div style="width:96%; border: 1px dotted #DDDDDD; font-size:8pt;padding:5px;">
						<b>Synonyms:</b>
						<ul style="list-style-type:none;padding-left:10px; margin-top:2px; margin-bottom: 2px;">
						<logic:iterate name="node" property="synonyms" id="synonym">
						<li><b>*</b>&nbsp;<bean:write name="synonym"/></li>
						</logic:iterate>
						</ul>
					</div>
				</logic:notEmpty>
			</td>
		</tr>
		</logic:iterate>
	</tbody>
	
	</table>
	</logic:greaterThan>
	
	</div>
	</center>
	</logic:present>

</yrcwww:contentbox>
</div>

</body>
</html>