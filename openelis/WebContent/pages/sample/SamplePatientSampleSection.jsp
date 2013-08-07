<%@ page import="us.mn.state.health.lims.common.util.StringUtil" %>
<%@ taglib uri="/tags/struts-html"		prefix="html" %>
<%@ taglib uri="/tags/struts-tiles"     prefix="tiles" %>


<!-- <html:button styleId="samplesDisplayButton" property="showHide" value="-" onclick="showHideSection(this, 'samplesDisplay');" />
 -->
 <h2><%= StringUtil.getContextualMessageForKey("sample.entry.sampleList.label") %><span class="requiredlabel">*</span>
</h2>

<div id="samplesDisplay" class="colorFill" style="display:block;" >
    <tiles:insert attribute="addSample"/>
</div>

<br />