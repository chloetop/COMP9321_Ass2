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

			<!--Form Item item_name begin-->
			<div class="form-group col-md-6">
				<label name="item_name_label" class="col-md-3 control-label">Item
					Name</label>
				<div class="col-md-7">
					<input type="text" class="form-control" name="item_name"
						placeholder="Enter the Search Item Name"></input>
				</div>
			</div>
			<!--Form Item 1 end-->

			<!--Form Item author begin-->
			<div class="form-group col-md-6">
				<label name="authors_label" class="col-md-3 control-label">Authors</label>
				<div class="col-md-7">
					<input type="text" class="form-control" name="authors"
						placeholder="Enter the Search Author Names seperated by comma"></input>
				</div>
			</div>
			<!--Form Item author end-->

			<!--Form Item Publication begin-->
			<div class="form-group col-md-6">
				<label name="publication_label" class="col-md-3 control-label">Publication</label>
				<div class="col-md-7">
					<input type="text" class="form-control" name="publication"
						placeholder="Enter the Publication Name"></input>
				</div>
			</div>
			<!--Form Item Publication end-->

			<!--Form Item Year begin-->
			<div class="form-group col-md-6">
				<label name="year_label" class="col-md-3 control-label">Year</label>
				<div class="col-md-7">
					<input type="text" class="form-control" name="year"
						placeholder="Enter the Year"></input>
				</div>
			</div>
			<!--Form Item Year end-->

			<!--Form Item SellerID begin-->
			<div class="form-group col-md-6">
				<label name="sellerid_label" class="col-md-3 control-label">Seller</label>
				<div class="col-md-7">
					<input type="text" class="form-control" name="sellerid"
						placeholder="Enter the Year"></input>
				</div>
			</div>
			<!--Form Item SellerID end-->

			<!--Form Item 2 begin-->
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
			<!--Form Item 2 end-->

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