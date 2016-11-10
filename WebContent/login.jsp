<!-- Modal -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${empty sessionScope.user_id}">
	<div class="container" id="login"
		style="display: none; padding-top: 2em;">
		<div class="row">
			<div class="col-md-6 col-md-offset-3">
				<div class="panel panel-login">
					<div class="panel-heading">
						<div class="row">
							<div class="col-xs-6">
								<a href="#" class="active" id="login-form-link">Login</a>
							</div>
							<div class="col-xs-6">
								<a href="#" id="register-form-link">Register</a>
							</div>
						</div>
						<hr>
					</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-lg-12">
								<div id="login-form" role="form" style="display: block;">
									<script>
										$(document)
												.ready(
														function() {
															$("#login-submit").unbind('click').bind('click', function () { });
															$('#login-submit')
																	.click(
																			function() {
																				var user = $(
																						'#username')
																						.val();
																				var pwd = $(
																						'#password')
																						.val();
																				$
																						.ajax({
																							type : "POST",
																							url : "home",
																							data : {
																								"user" : user,
																								"password" : pwd,
																								"action" : "login"
																							},
																							success : function(
																									data) {
																								if (data == 'True') {
																									$(
																											location)
																											.attr(
																													'href',
																													'home');
																								} else {
																									alert('Fail....');
																								}
																							}
																						});
																			});
														});
									</script>
									<input type="hidden" name="action" value="login" />
									<div class="form-group">
										<input type="text" name="uname" id="username" tabindex="1"
											class="form-control" placeholder="Username" value="">
									</div>
									<div class="form-group">
										<input type="password" name="password" id="password"
											tabindex="2" class="form-control" placeholder="Password">
									</div>
									<div class="form-group text-center">
										<input type="checkbox" tabindex="3" class="" name="remember"
											id="remember"> <label for="remember">
											Remember Me</label>
									</div>
									<div class="form-group">
										<div class="row">
											<div class="col-sm-6 col-sm-offset-3">
												<input type="submit" name="login-submit" id="login-submit"
													tabindex="4" class="form-control btn btn-login"
													value="Log In">
											</div>
										</div>
									</div>
									<div class="form-group">
										<div class="row">
											<div class="col-lg-12">
												<div class="text-center">
													<a href="http://phpoll.com/recover" tabindex="5"
														class="forgot-password">Forgot Password?</a>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div id="register-form"
									
									role="form" style="display: none;">
									<script>
										$(document)
												.ready(
														function() {
															/* $("#register-submit").unbind('click').bind('click', function () { }); */
															
															var flag = false;
															$(
																	'#register-submit')
																	.click(
																			function() {
																				var user = $(
																						'#reg_username')
																						.val();
																				var nickName = $(
																						'#nickName')
																						.val();
																				var fname = $(
																						'#fname')
																						.val();
																				var lname = $(
																						'#lname')
																						.val();
																				var email = $(
																						'#email')
																						.val();
																				var yob = $(
																						'#yob')
																						.val();
																				var full_address = $(
																						'#full_address')
																						.val();
																				var type = $(
																						'#type')
																						.val();
																				var CC = $(
																						'#CC')
																						.val();
																				var pwd = $(
																						'#reg_password')
																						.val();
																				if (user.length == 0
																						|| nickName.length == 0
																						|| fname.length == 0
																						|| lname.length == 0
																						|| email.length == 0
																						|| yob.length == 0
																						|| full_address.length == 0
																						|| CC.length == 0)
																					flag = true;
																				else
																					flag = false;
																				if (!flag) {
																					$
																							.ajax({
																								type : "POST",
																								url : "home",
																								data : {
																									"username" : user,
																									"nickName" : nickName,
																									"full_address" : full_address,
																									"fname" : fname,
																									"lname" : lname,
																									"email" : email,
																									"yob" : yob,
																									"type" : type,
																									"CC" : CC,
																									"password" : pwd,
																									"action" : "signup"
																								},

																								success : function(
																										data) {
																									if (data == 'True') {
																										$(
																												location)
																												.attr('href',
																														'home');
																									} else {
																										alert('Fail....');
																									}
																								}
																							});
																				} else {
																					$(
																							'#validFrm')
																							.html(
																									'Either one of the field is empty or Entered Data is not Valid')
																							.css(
																									'color',
																									'red');

																				}
																			});
															$(
																	'#reg_password, #confirm-password')
																	.on(
																			'keyup',
																			function() {
																				var pass = $(
																						'#reg_password')
																						.val();
																				var conf = $(
																						'#confirm-password')
																						.val();
																				if (pass == conf) {
																					flag = false;
																					$(
																							'#divCheckPasswordMatch')
																							.html(
																									'Passwords Matching')
																							.css(
																									'color',
																									'green');
																				} else {
																					flag = true;
																					$(
																							'#divCheckPasswordMatch')
																							.html(
																									'Passwords Not Matching')
																							.css(
																									'color',
																									'red');
																				}

																			});

															$('#CC')
																	.on(
																			'keyup',
																			function() {
																				var CC = $(
																						'#CC')
																						.val();
																				console
																						.log(CC);
																				if (CC.length < 16) {
																					flag = true;
																					$(
																							'#validCC')
																							.html(
																									'Credit Card Number Invalid')
																							.css(
																									'color',
																									'red');
																				} else {
																					$(
																							'#validCC')
																							.html(
																									'Credit Card Number Valid')
																							.css(
																									'color',
																									'green');
																					flag = false
																				}
																			});

														});
									</script>
									<div class="form-group">
										<input type="text" name="username" id="reg_username"
											tabindex="1" class="form-control" placeholder="Username"
											value="">
									</div>
									<div class="form-group">
										<input type="text" name="nickName" id="nickName" tabindex="1"
											class="form-control" placeholder="Preferred Name" value="">
									</div>
									<div class="form-group">
										<input type="text" name="fname" id="fname" tabindex="1"
											class="form-control" placeholder="First Name" value="">
									</div>
									<div class="form-group">
										<input type="text" name="lname" id="lname" tabindex="1"
											class="form-control" placeholder="Last Name" value="">
									</div>
									<div class="form-group">
										<input type="email" name="email" id="email" tabindex="1"
											class="form-control" placeholder="Email Address" value="">
									</div>
									<div class="form-group">
										<input type="text" name="full_address" id="full_address"
											tabindex="1" class="form-control"
											placeholder="Mailing Address" value="">
									</div>
									<div class="form-group">
										<input type="number" name="yob" id="yob" tabindex="1"
											class="form-control" placeholder="Year of Birth" value="">
									</div>
									<div class="form-group">
										<select name="type" id="type" class="form-control">
											<option value="1">Register as Customer</option>
											<option value="2">Register as Seller</option>
										</select>
									</div>
									<div class="form-group">
										<input type="text" pattern="[0-9]{13,16}" name="CC" id="CC"
											tabindex="1" class="form-control"
											placeholder="Credit Card number" value="">
									</div>
									<div class="form-group">
										<input type="password" name="reg_password" id="reg_password"
											class="form-control" placeholder="Password" value="">
									</div>
									<div class="form-group">
										<input type="password" name="confirm-password"
											id="confirm-password" class="form-control"
											placeholder="Confirm Password" value=""> <span
											id="divCheckPasswordMatch"></span><br> <span
											id="validCC"></span><br> <span id="validFrm"></span>
									</div>
									<div class="form-group">
										<div class="row">
											<div class="col-sm-6 col-sm-offset-3">
												<input type="submit" name="register-submit"
													id="register-submit" tabindex="4"
													class="form-control btn btn-register" value="Register Now">
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</c:if>
<c:if test="${not empty sessionScope.user_id}">
	<div class="container" id="login"
		style="display: none; padding-top: 2em;">
		<div class="span3 well">
			<center>
				<a href="#aboutModal" data-toggle="modal" data-target="#myModal"><img
					src="https://openclipart.org/download/247320/abstract-user-flat-4.svg"
					name="aboutme" width="240" height="240" class="img-circle" /></a>
				<h3><%=session.getAttribute("full_name")%></h3>
				<em>click my face for more info<br></em>

			</center>
		</div>
		<!-- Modal -->
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-hidden="true"></button>
						<h4 class="modal-title" id="myModalLabel">
							More About
							<%=session.getAttribute("fname")%></h4>
					</div>
					<div class="modal-body">
						<center>
							<img
								src="https://openclipart.org/download/247320/abstract-user-flat-4.svg"
								name="aboutme" width="240" height="240" border="0"
								class="img-circle"></a>
							<h3 class="media-heading"><%=session.getAttribute("full_name")%>
								<small></small>
							</h3>
							<span><strong>Account Attributes: </strong></span>
							<c:if test="${sessionScope.admin_status == 1}">
								<span class="label label-warning">Administrator</span>
							</c:if>
							<c:if test="${sessionScope.type == 2}">
								<span class="label label-info">Seller</span>
								<span class="label label-success">Customer</span>
							</c:if>
							<c:if test="${sessionScope.type == 1}">
								<span class="label label-success">Customer</span>
							</c:if>
						</center>
						<hr>
						<center>
							<p class="text-left">
								<strong>Profile: </strong><br>
							<table>
								<tr>
									<td><b>Email: </b></td>
									<td><%=" " + session.getAttribute("email")%></td>
								</tr>
								<tr>
									<td><b>Year of birth: </b></td>
									<td><%=" " + session.getAttribute("yob")%></td>
								</tr>
								<tr>
									<td><b>Address: </b></td>
									<td><%=" " + session.getAttribute("full_address")%></td>
								</tr>
								<tr>
									<td><b>Credit Card No: </b></td>
									<td><%=" XXXX XXXX XXXX " + session.getAttribute("cc").toString()
						.substring(session.getAttribute("cc").toString().length() - 4)%></td>
								</tr>
							</table>
							</p>
							<br>
						</center>
					</div>
					<div class="modal-footer">
						<center>
							<button type="button" class="btn btn-default"
								data-dismiss="modal">
								I've heard enough about
								<%=session.getAttribute("fname")%></button>
						</center>
					</div>
				</div>
			</div>
		</div>
		<c:if test="${sessionScope.admin_status == 1}">
			<center>
				<h2>
					You are a Superuser and With great power comes great responsibility.<br>
				</h2>
			</center>
			<br>
			<br>
			<div class="container demo">


				<div class="panel-group" id="accordion" role="tablist"
					aria-multiselectable="true">

					<div class="panel panel-default">
						<div class="panel-heading" role="tab" id="headingOne">
							<h4 class="panel-title">
								<a role="button" data-toggle="collapse" data-parent="#accordion"
									href="#collapseOne" aria-expanded="true"
									aria-controls="collapseOne"> <i
									class="more-less glyphicon glyphicon-plus"></i> Ban/Activate
									User
								</a>
							</h4>
						</div>
						<div id="collapseOne" class="panel-collapse collapse"
							role="tabpanel" aria-labelledby="headingOne">
							<div class="panel-body">
								<div id="login-form" role="form" style="display: block;">
									<script>
										var request = new XMLHttpRequest();
										function searchInfoUser() {
											var name = $("#search_username")
													.val();
											var url = "toggle_search.jsp?val="
													+ name + "&target=usr";
											try {
												request.onreadystatechange = function() {
													if (request.readyState == 4) {
														var val = request.responseText;
														document.getElementById('search_results_user').innerHTML = val;
													}
												}//end of function  
												request.open("GET", url, true);
												request.send();
												delete request;
											} catch (e) {
												alert("Unable to connect to server");
											}
										}
										function searchInfoUserActivity() {
											$('#search_results_act').html('').css("display","block");
											var name = $("#search_user_act")
													.val();
											var url = "toggle_search.jsp?val="
													+ name + "&target=act";
											try {
												request.onreadystatechange = function() {
													if (request.readyState == 4) {
														var val = request.responseText;
														
														document.getElementById('search_results_act').innerHTML = val;
													}
												}//end of function  
												request.open("GET", url, true);
												request.send();
												delete request;
											} catch (e) {
												alert("Unable to connect to server");
											}
										}
										function searchInfoItem() {
											var name = $("#search_item").val();
											var url = "toggle_search.jsp?val="
													+ name + "&target=item";
											try {
												request.onreadystatechange = function() {
													if (request.readyState == 4) {
														var val = request.responseText;
														document
																.getElementById('search_results_item').innerHTML = val;
													}
												}//end of function  
												request.open("GET", url, true);
												request.send();
												delete request;
											} catch (e) {
												alert("Unable to connect to server");
											}
										}
										function searchInfoUserAct() {
											var name = $("#search_item_userAct").val();
											var url = "toggle_search.jsp?val="
													+ name + "&target=act";
											try {
												request.onreadystatechange = function() {
													if (request.readyState == 4) {
														var val = request.responseText;
														document.getElementById('search_results_userAct').innerHTML = val;
													}
												}//end of function  
												request.open("GET", url, true);
												request.send();
												delete request;
											} catch (e) {
												alert("Unable to connect to server");
											}
										}
									</script>
									<div class="form-group">
										<input type="text" name="search_username" id="search_username"
											tabindex="1" class="form-control"
											placeholder="Username to Search" value=""
											onkeyup="setTimeout(searchInfoUser(),2000)">
									</div>
									<div class="form-group" id="search_results_user"></div>
								</div>
							</div>
						</div>
					</div>
					<div class="panel panel-default">
						<div class="panel-heading" role="tab" id="headingTwo">
							<h4 class="panel-title">
								<a class="collapsed" role="button" data-toggle="collapse"
									data-parent="#accordion" href="#collapseTwo"
									aria-expanded="false" aria-controls="collapseTwo"> <i
									class="more-less glyphicon glyphicon-plus"></i> Toggle Item
									Status
								</a>
							</h4>
						</div>
						<div id="collapseTwo" class="panel-collapse collapse"
							role="tabpanel" aria-labelledby="headingTwo">
							<div class="panel-body">
								<div id="login-form" role="form" style="display: block;">
									<div class="form-group">
										<input type="text" name="search_item" id="search_item"
											tabindex="1" class="form-control"
											placeholder="Item to Search" value=""
											onkeyup="setTimeout(searchInfoItem(),5000)">
									</div>
									<div class="form-group" id="search_results_item"></div>
								</div>
							</div>
						</div>
					</div>

					<div class="panel panel-default">
						<div class="panel-heading" role="tab" id="headingThree">
							<h4 class="panel-title">
								<a class="collapsed" role="button" data-toggle="collapse"
									data-parent="#accordion" href="#collapseThree"
									aria-expanded="false" aria-controls="collapseThree"> <i
									class="more-less glyphicon glyphicon-plus"></i> Customer
									Activity Log
								</a>
							</h4>
						</div>
						<div id="collapseThree" class="panel-collapse collapse"
							role="tabpanel" aria-labelledby="headingThree">
							<div class="panel-body">
								<div id="login-form" role="form" style="display: block;">
									<div class="form-group">
										<input type="text" name="search_item_userAct" id="search_item_userAct"
											tabindex="1" class="form-control"
											placeholder="User to Search" value=""
											onkeyup="setTimeout(searchInfoUserAct(),5000)">
									</div>
									<div class="form-group" id="search_results_userAct"></div>
									<div class="form-group" id="activity_table"></div>
								</div>
							</div>
						</div>
					</div>

				</div>
				<!-- panel-group -->


			</div>
			<!-- container -->

		</c:if>

	</div>
</c:if>

