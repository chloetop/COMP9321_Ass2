<div class="col-md-6">
	<div id="checkout-form" method="post" role="form" action="check_out">
		<script>
			$(document).ready(function() {
								var flag = false;
								$('#checkout-submit').click(
												function() {

													var email = $('#email')
															.val();

													var full_address = $(
															'#full_address')
															.val();

													var CC = $('#CC').val();

													if (email.length == 0
															|| full_address.length == 0
															|| CC.length == 0)
														flag = true;
													else
														flag = false;
													if (!flag) {
														$
																.ajax({
																	type : "POST",
																	url : "check_out",
																	data : {
																		"full_address" : full_address,
																		"email" : email,
																		"CC" : CC,
																		"action" : "checkout_done"
																		
																	},
																	success: function(data){
																		if(data == "True"){
																			window.location.replace('checkout_done.jsp');
																		}
																	}

																});
													} else {
														$('#validFrm').html(
																		'Either one of the field is empty or Entered Data is not Valid').css('color',
																		'red');

													}
													
												});

								$('#CC').on(
												'keyup',
												function() {
													var CC = $('#CC').val();
													console.log(CC);
													if (CC.length < 16) {
														flag = true;
														$('#validCC').html(
																		'Credit Card Number Invalid').css('color',
																		'red');
													} else {
														$('#validCC').html(
																		'Credit Card Number Valid').css('color',
																		'green');
														flag = false
													}
												});

							});
		</script>

		<%
			String email = new String();
			String cc = new String();
			String address = new String();

			if (session.getAttribute("email") != null) {
				email = session.getAttribute("email").toString();
			} else {
				email = "";
			}
			if (session.getAttribute("full_address") != null) {
				address = session.getAttribute("full_address").toString();
			} else {
				address = "";
			}

			if (session.getAttribute("cc") != null) {
				cc = " XXXX XXXX XXXX "
						+ session
								.getAttribute("cc")
								.toString()
								.substring(
										session.getAttribute("cc").toString()
												.length() - 4);
		
			} else {
				cc = "";
			}
		%>
		<div class="form-group">
			<input type="email" name="email" id="email" tabindex="1"
				class="form-control" placeholder="Email Address" value="<%=email%>">
		</div>
		<div class="form-group">
			<input type="text" name="full_address" id="full_address" tabindex="1"
				class="form-control" placeholder="Mailing Address"
				value="<%=address%>">
		</div>


		<div class="form-group">
			<input type="text" pattern="[0-9]{13,16}" name="CC" id="CC"
				tabindex="1" class="form-control" placeholder="Credit Card number"
				value="<%=cc%>">
		</div>

		<div class="form-group">
			<div class="row">
				<div class="col-sm-4 ">
					<input type="submit" name="checkout-submit" id="checkout-submit"
						tabindex="4" class="form-control btn btn-primary" value="Save">
				</div>
			</div>
		</div>
		<span id="validCC"></span><br> <span id="validFrm"></span>
	</div>
</div>