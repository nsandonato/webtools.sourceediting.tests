<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic"%>
<%! int alpha = 5; %>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"%>
<logic:iterate id="iterator">
<%= iterator %>
</logic:iterate>
<bean:define id="id3" type="com.nitin.TestBean"></bean:define>
<%  %>

<logic:iterate id="iterator2">
	<%=iterator2 + "A"%>
 
	<%= id3 + alpha %>
</logic:iterate>