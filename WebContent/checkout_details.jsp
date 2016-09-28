<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!-- Check out panel begin -->
<div class="panel panel-primary">
	<div class="panel-body">
		<!-- Checkout items table -->
		<table class="table table-striped">
			<tr>
				<th>Title</th>
				<th>Quantity</th>
				<th>Unit Price</th>
			</tr>
			<c:forEach items="${requestScope.checkout_items}" var="item">
				<tr>
					<td><c:out value="${item.title}" /></td>
					<td><c:out value="${item.quantity}" /></td>
					<td><c:out value="${item.unit_price}" /></td>
				</tr>
			</c:forEach>
		</table>
		<h6>
			<strong>Total Price:</strong>
			<c:out value="${requestScope.total_price}" />
		</h6>
	</div>
</div>
<!-- Check out panel end -->