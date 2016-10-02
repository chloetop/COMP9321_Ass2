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
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-switch/3.3.2/css/bootstrap3/bootstrap-switch.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-switch/3.3.2/js/bootstrap-switch.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
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
		<div class="container-fluid" id="home" style="margin: -1em 0 2em 0;">
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
		<!--Footer-->
		<%@ include file="footer.jsp"%>
	</div>

	<!--Root page content end-->
	<!-- Scripts -->
<!-- 	<script>
	("").on("click", "#myTab", function(){
		  $('ul.nav-tabs li.active').removeClass('active');
		  $(this).addClass('in active');
		});
</script> -->

	<script src="js/script.js"></script>
	<%@ include file="quick_search.jsp"%>
	<%@ include file="login.jsp"%>
	
</body>
</html>