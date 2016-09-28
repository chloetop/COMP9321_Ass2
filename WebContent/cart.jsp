<!--Side Navigation: Shopping Cart-->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<div id="mySidenav" class="sidenav">
	<!--Shopping Cart Icon-->
	<div class="col-md-12 text-right">
		<a href="javascript:void(0)" class="closebtn" onclick="closeNav()">&times;</a>
	</div>
	<div class="col-md-12"></div>
	<div class="col-md-10">
		<div class="">
			<img src="images/Cart.png" height=50px width=50px />
			<div class="col-md-10">
				<h4>Shopping Cart</h4>
				<br /> <br />
			</div>
			<div id="cart_content">
				<c:if test="${fn:length(sessionScope.cart) == 0}">
					<div class="col-md-12 panel panel-primary" id="empty_cart">
						<div>
							<h6>Shopping Cart is Empty!</h6>

						</div>
					</div>
				</c:if>
				<c:if test="${fn:length(sessionScope.cart) > 0}">
					<div class="col-md-12">
						<form method="post" action="next">
							<input type="hidden" name="action" value="checkout" /> <input
								type="submit" class="btn btn-success cart pager"
								value="Checkout&nbsp;>">
						</form>
					</div>
					<c:forEach items="${sessionScope.cart}" var="cart_item">
						<c:set var="cartMap" value="${cart_item.getItemList()}" />
						<c:set var="cartId" value="${cart_item.getId()}" />
						<div class="col-md-12 panel panel-primary"
							id="cart_item<c:out value="${cartId}"></c:out>">
							<div>
								<h6>
									<c:out value="${cartMap['title']}"></c:out>
									<br />
									<button class="btn btn-warning btn-xs cart"
										onclick="removeCartItem('cart_item<c:out value="${cartId}"></c:out>','<c:out value="${cartMap['title']}"></c:out>')">Remove</button>
								</h6>
								<br />

							</div>
						</div>
					</c:forEach>

				</c:if>
			</div>
		</div>
	</div>
</div>