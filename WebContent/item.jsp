<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!--Content Data-->
<c:forEach items="${requestScope.item_list}" var="item">
	<!-- Item -->
	<div class="col-md-3">
		<!-- Item -->

		<div class="panel panel-primary">
			<!--Panel Definition-->
			<div class="panel-heading">
				<button id="myBtn_<c:out value="${item.id}"></c:out>"
					class="btn btn-primary  btn-block btn-sm"
					onclick="modal_open(<c:out value="${item.id}"></c:out>)">
					<c:out value="${item.item_name}"></c:out>
				</button>
			</div>
			<!--Panel heading-->
			<div class="panel-body">
				<!--Panel Body-->

				<table class="table table-striped">
					<tr>
						<th>Key</th>
						<td><c:out value="${item.id}"></c:out></td>
					</tr>
					<tr>
						<th>Publication</th>
						<td><c:out value="${item.publication}"></c:out></td>
					</tr>
					<tr>
						<th>Year</th>
						<td><c:out value="${item.year}"></c:out></td>
					</tr>
					<tr>
						<th>URL</th>
						<td><c:out value="${item.URL}"></c:out></td>
					</tr>
				</table>
			</div>
			<!--Panel body end-->
		</div>
		<!--Panel Definition end-->
	</div>
	<!-- The Modal -->
	<div id="myModal_<c:out value="${item.id}"></c:out>"
		class="modal col-md-4">

		<!-- Modal content -->
		<div class="modal-content">
			<span class="close_<c:out value="${item.id}"></c:out> close"
				onclick="close_modal(<c:out value="${item.id}"></c:out>)">x&nbsp;</span>
			<div class="panel panel-primary">
				<!--Panel Definition-->
				<div class="panel-heading">
					<c:out value="${item.item_name}"></c:out>
				</div>
				<!--Panel heading-->
				<div class="panel-body">
					<!--Panel Body-->
					<form name="cart<c:out value="${item.id}"></c:out>" method="post"
						action="cart"
						onsubmit="addToCart(<c:out value="${item.id}"></c:out>); return false;">
						<table class="table table-striped">
							<input type="hidden" name="item_id"
								value="<c:out value="${item.id}"></c:out>" />
							<tr>
								<th>Key</th>
								<td><c:out value="${item.id}"></c:out></td>
							</tr>
							<tr>
								<th>Publication</th>
								<td><c:out value="${item.publication}"></c:out></td>
							</tr>
							<tr>
								<th>Authors</th>
								<td><c:out value="${item.publication}"></c:out></td>
							</tr>
							<tr>
								<th>Year</th>
								<td><c:out value="${item.year}"></c:out></td>
							</tr>
							<tr>
								<th>Seller ID</th>
								<td><c:out value="${item.seller_id}"></c:out></td>
							</tr>
							<tr>
								<th>Price</th>
								<td><c:out value="${item.price}"></c:out></td>
							</tr>
						</table>
						<input type="hidden" name="form_action" value="add_cart" /> <input
							class="btn btn-primary btn-xs" type="submit" value="Add to Cart"
							id="item_add"></input>
					</form>
				</div>
				<!--Panel body end-->
			</div>
			<!--Panel Definition end-->
		</div>
		<!--Item End-->
	</div>

	<!--Item End-->
</c:forEach>

<div class="col-md-12">
	<form method="post" action="next">
		<input type="hidden" name="action" value="next" /> <input
			type="submit" class="btn btn-primary btn-sm next" value="Next&nbsp;>">
	</form>
	<form method="post" action="previous">
		<input type="hidden" name="action" value="previous" /> <input
			type="submit" class="btn btn-primary btn-sm previous"
			value="<&nbsp;Previous">
	</form>
</div>