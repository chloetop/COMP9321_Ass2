<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="container-fluid" id="search" style="display: none;padding-top: 2em;">
	<!--Search Query Form-->
	<form class="form-horizontal" action="search" method="post">
		<input type="hidden" name="action" value="advanced_search" />
		<fieldset>
			<!--Form details begin-->
			<div class="col-md-12">
				<legend>
					<h4>
						<strong>Search</strong>
					</h4>
				</legend>
			</div>
			<!--Form Item Generic begin-->
			<c:forEach var="field" items="${sessionScope.search_fields}">
				<div class="form-group col-md-6">
					<label name="<c:out value="${field.key}" />_label"
						class="col-md-3 control-label"><c:out
							value="${field.value}" /></label>
					<div class="col-md-7">
						<input type="text" class="form-control"
							name="<c:out value="${field.key}" />"
							placeholder="Enter the Search <c:out value="${field.value}" />"></input>
					</div>
				</div>
				<!-- Form Item Generic End -->
			</c:forEach>

			<!--Form Item Price begin-->
			<div class="form-group col-md-6">
				<label name="item_name_label" class="col-md-3 control-label">Price
					Range</label>
				<div class="col-md-7">
					<div data-role="rangeslider">
						<label for="price-min">Min Price:</label> <input type="range"
							name="price-min" id="price-min" value="200" min="0" max="1000"
							oninput="priceMinUpdate(value)">
						<output for="price-min" id="price-min-out">200</output>
						<label for="price-max">Max Price:</label> <input type="range"
							name="price-max" id="price-max" value="800" min="0" max="1000"
							oninput="priceMaxUpdate(value)">
						<output for="price-max" id="price-max-out">800</output>
					</div>
					<br />
				</div>
			</div>
			<!--Form Item Price end-->

			<!--Form Item Type begin-->
			<div class="form-group col-md-6">
				<label for="select" class="col-md-3 control-label">Document
					Type</label>
				<div class="col-md-7">
					<select multiple="multiple" class="form-control" name="type">
						<option value="article">Article</option>
						<option value="inproceedings">Inproceedings</option>
						<option value="proceedings">Proceedings</option>
						<option value="book">Book</option>
						<option value="incollection">Incollection</option>
						<option value="phdthesis">PHD Thesis</option>
						<option value="mastersthesis">Masters Thesis</option>
						<option value="www">Web Resource</option>
					</select>
				</div>
			</div>
			<!--Form Item Type end-->

			<br />
			<!--Form Heading-->
			<div class="form-group col-md-11">
				<div class="col-md-12 text-right">
					<button type="submit" class="btn btn-primary btn-sm">Search</button>
				</div>
			</div>
			<!--Form details end-->
		</fieldset>
	</form>
	<!--Search query form end-->
</div>