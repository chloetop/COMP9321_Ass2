<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!--Content Data-->
<c:if test="${fn:length(requestScope.item_list) == 0}">
	<div class="col-md-12">
		<h5>Sorry, no matching datasets found!</h5>
	</div>
</c:if>
<c:forEach items="${requestScope.item_list}" var="item">
	<!-- Item -->
	<div class="col-md-3">
		<!-- Item -->
		<c:set var="itemId" value="${item.getId()}" scope="page" />
		<c:set var="itemMap" value="${item.getItemList()}" scope="page" />
		<div class="panel panel-primary">
			<!--Panel Definition-->
			<div class="panel-heading">
				<a id="myBtn_${itemId}" class="btn-primary btn-block btn-sm"
					onclick="modal_open(<c:out value="${itemId}"></c:out>)"
					style="margin-bottom: 4px; white-space: normal; height: 50px; cursor: pointer;">
					<c:out value="${itemMap['title']}"></c:out>
				</a>
			</div>
			<!--Panel heading-->
			<div class="panel-body">
				<!--Panel Body-->
				<img src="images/<c:out value="${itemMap['image_url']}"></c:out>"
					height=150px width=150px class="center-block" />
				<table class="table table-striped">
					<tr>
						<th>Key</th>
						<td><c:out value="${itemMap['key']}"></c:out></td>
					</tr>
					<tr>
						<th>Price</th>
						<td><c:out value="${itemMap['price']}"></c:out> AUD</td>
					</tr>
					<tr>
						<th>Year</th>
						<td><c:out value="${itemMap['year']}"></c:out></td>
					</tr>
					<tr>
						<th>Volume</th>
						<td><c:out value="${itemMap['volume']}"></c:out></td>
					</tr>
				</table>
			</div>
			<!--Panel body end-->
		</div>
		<!--Panel Definition end-->
	</div>
	<!-- The Modal -->
	<div id="myModal_<c:out value="${itemId}"></c:out>"
		class="modal col-md-4">

		<!-- Modal content -->
		<div class="modal-content">
			<span class="close_<c:out value="${itemId}"></c:out> close"
				onclick="close_modal(<c:out value="${itemId}"></c:out>)">x&nbsp;</span>
			<div class="panel panel-primary">
				<!--Panel Definition-->
				<div class="panel-heading">
					<c:out value="${itemMap['title']}"></c:out>
				</div>
				<!--Panel heading-->
				<div class="panel-body">
					<!--Panel Body-->
					<form name="cart<c:out value="${itemId}"></c:out>" method="post"
						action="cart"
						onsubmit="addToCart(<c:out value="${itemId}"></c:out>); return false;">
						<table class="table table-striped">
							<input type="hidden" name="item_id"
								value="<c:out value="${itemId}"></c:out>" />
							<c:forEach var="item_value" items="${itemMap}">
								<c:if test="${item_value.key ne 'image_url'}">
									<tr>
										<th><c:out value="${item_value.key}" /></th>
										<td><c:out value="${item_value.value}" /></td>
									</tr>
								</c:if>
							</c:forEach>
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
	<c:if test="${requestScope.next eq 'true'}">
		<form method="post" action="next">
			<input type="hidden" name="action" value="next" /> <input
				type="submit" class="btn btn-primary btn-sm next pager"
				value="Next&nbsp;>">
		</form>
	</c:if>
	<c:if test="${requestScope.previous eq 'true'}">
		<form method="post" action="previous">
			<input type="hidden" name="action" value="previous" /> <input
				type="submit" class="btn btn-primary btn-sm previous pager"
				value="<&nbsp;Previous">
		</form>
	</c:if>
</div>