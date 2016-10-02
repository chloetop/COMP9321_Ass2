<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<!-- IE Edge Meta Tag -->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- Viewport -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Bootstrap CSS -->
<link rel="stylesheet" href="css/bootstrap.min.css" />
<link rel="stylesheet" href="css/style.css" />
<title>dblp Store</title>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
</head>
<body>
	<%@ include file="cart.jsp"%>
	<!--Root page container-->
	<div class="container-fluid" id="main">
		<%@ include file="nav.jsp"%>
		<!--Home Page begin -->
		<%@ include file="alert.jsp"%>
		<div class="container-fluid" id="home">
			<div class="col-md-12">
				<h4>
					<strong>Suggestions</strong>
				</h4>
			</div>
			<%@ include file="item.jsp"%>
		</div>
		<!--Home Page End-->
		<!--Search Page Begin-->
		<%@ include file="search.jsp"%>
		<!--Search Page End-->
		<!--Upload Page Begin-->
		<%@ include file="upload_item.jsp"%>
		<!--Upload Page End-->
		<!--Footer-->
		<%@ include file="footer.jsp"%>
	</div>

	<!--Root page content end-->
	<!-- Scripts -->
	<script src="js/jquery-3.1.0.min.js"></script>
	<script src="js/bootstrap.js"></script>
	<script src="js/script.js"></script>
	<%@ include file="quick_search.jsp"%>
</body>
</html>