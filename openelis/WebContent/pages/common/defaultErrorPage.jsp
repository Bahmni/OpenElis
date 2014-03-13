<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%
String path = request.getContextPath();
String basePath = path + "/";
%>

<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <title><bean:message key="errors.unhandled.title"/></title>   
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">    
    <meta http-equiv="description" content="Default error page">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>css/bootstrap.css" />
    <script type="text/javascript" src="<%=basePath%>scripts/jquery-1.8.0.min.js"></script>
        
    <!-- Inline css --> 
    <style type="text/css">
    #header {
        margin-bottom: 1em;
        min-width: 970px;
        margin-bottom: 10px;
        padding: 10px 0px 0px 15px;
        height: 88px;
        overflow: visible;
        background: #486086; /* Old browsers */
        background: -moz-linear-gradient(top, #486086 0%, #557aaf 100%); /* FF3.6+ */
        background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#486086), color-stop(100%,#557aaf)); /* Chrome,Safari4+ */
        background: -webkit-linear-gradient(top, #486086 0%,#557aaf 100%); /* Chrome10+,Safari5.1+ */
        background: -o-linear-gradient(top, #486086 0%,#557aaf 100%); /* Opera11.10+ */
        background: -ms-linear-gradient(top, #486086 0%,#557aaf 100%); /* IE10+ */
        filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#486086', endColorstr='#557aaf',GradientType=0 ); /* IE6-9 */
        background: linear-gradient(top, #486086 0%,#557aaf 100%); /* W3C */
    }
    #oe-logo {
        float: left;
        margin: 0 15px 0 0
    }
    #oe-title {
        font-size: 24px;
        line-height: 32px;
        color: #fff;
        font-weight: bold
    }
    .troubleshooting {
        font-family: Menlo, Monaco, Consolas, "Courier New", monospace;
        font-size: 12px;
    }
    </style>
</head>
  
<body id="defaultErrorPage">

<div id="header">
    <div id="oe-logo"><img src="<%=basePath%>images/openelis_logo.png" title="OpenELIS" alt="OpenELIS"></div>
    <div id="oe-title"><bean:message key="homePage.heading"/></div>
</div>
      
<div class="container">

    <h1><bean:message key="errorpage.title"/></h1>
    <br />

    <div class="well">	
        <div class="row">
            <div class="span2">
                <img src="<%=basePath%>images/icon-exclamation.png"  title="<bean:message key="errorpage.title"/>" alt="<bean:message key="errorpage.title"/>" />
            </div>
            <div class="span9">
                <h3>Une erreur syst&egrave;me est survenue.</h3> 

                <p>S'il vous pla&icirc;t le signaler &agrave; votre administrateur syst&egrave;me. Afin de comprendre ce qui a caus&eacute; le probl&egrave;me, s'il vous pla&icirc;t nous dire:</p>

                <ul>
                    <li>Que faisiez-vous quand c'est arriv&eacute;.</li>
                    <li>Que vous ayez v&eacute;cu cette m&ecirc;me erreur avant.</li>
                    <li>Dites &agrave; votre administrateur si:
                        <ul>
                            <li>Vous &ecirc;tes en mesure de continuer &agrave; travailler malgr&eacute; l'erreur, ou</li>
                            <li>Cette erreur qui vous emp&ecirc;che de faire de votre travail.</li>
                        </ul>
                    </li>
                    <li>Inclure les informations de d&eacute;pannage ci-dessous.</li>
                </ul>

                <p>Apr&egrave;s nous avoir signal&eacute; l'erreur, vous pouvez revenir &agrave; la page pr&eacute;c&eacute;dente en utilisant le bouton ci-dessous.</p>

                <br />
                <button class="btn" onclick="history.go( -1 );return true;"><i class="icon-chevron-left"></i> Retour &agrave; la page pr&eacute;c&eacute;dente</button>
                <br /><br />

                <h3>Informations sur le d&eacute;pannage</h3>
                <p>Envoyer cette information &agrave; votre administrateur syst&egrave;me:</p>
                <Br />
                <div class="troubleshooting">
                    <p>Temps d'erreur: <span id="error-time"> </span></p>

                    <p>Page sur laquelle l'erreur s'est produite:<br />
                    <span id="error-path"> </span></p>

                    <p id="previous-path"></p>

                    <div>D&eacute;tails du navigateur Web:</div>
                    <ul id="browser-details"></ul>
                </div>

            </div>
        </div>
    </div>	

</div>
    
<script type="text/javascript">
$(document).ready(function () {
    // jquery functions for details on error
    // Currently not localized.
    var myDate = new Date().toDateString() + " " + new Date().toTimeString();
    $("#error-time").html(myDate);
    var browserDetails = "<li>Version: " + navigator.appVersion + "</li>";
    browserDetails += "<li>Cookies Enabled: " + navigator.cookieEnabled + "</li>";
    browserDetails += "<li>Platform: " + navigator.platform + "</li>";
    browserDetails += "<li>User-agent header: " + navigator.userAgent + "</li>";
    $("#browser-details").html(browserDetails);
    var pathname = location.href;
    $("#error-path").html(pathname);
    var referrerPath = document.referrer;
    var referrer = 'Page pr&eacute;c&eacute;dente:<br />' + referrerPath;
    if (referrerPath != '') {
            $("#previous-path").html(referrer);
            // To build mailto link - currenly not in use
            //var mailAdmin = '<a href="mailto:address_of_admin@uw.edu&subject=Page Error&body=A major error occurred in the OpenELIS system.%0D%0A%0D%0AError page: ' + pathname +'%0D%0AError Time: ' + myDate + '%0D%0AUser-agent header: ' + navigator.userAgent + '">system administrator</a>';
            //$("#admin-mail").html(mailAdmin);
    }
});	
</script>
    
</body>
</html>


