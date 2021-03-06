<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
	<link rel="stylesheet" type="text/css" href="resources/mapStyle.css" media="all" />
	<script type="text/javascript"
		src="http://maps.google.com/maps/api/js?key=<add_your_key>&sensor=false"> </script>
	<script src="http://openlayers.org/api/OpenLayers.js" ></script> 
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.js" > </script>
	<script type="text/javascript">
	  	var baseURL = '<c:out value="${baseURL}"/>';
	</script>
	<script type="text/javascript" src="resources/maps.js"> </script>
</head>
<body onload="setupMap();">
	<form  id="submitter" METHOD="GET" >
		<fieldset>
    		<legend>City or zip:</legend>
  			<input type="text" id="cityOrZip"  /> 
  			<input id="submitButton" type="submit" value="Submit" />
  		</fieldset>
	</form>

	<div id="map_canvas"></div>
</body>
</html>
