<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!--Search Query Form-->
<form class="form-horizontal" action="search" method="post">
	<input type="hidden" name="action" value="graph_search_form" />
	<fieldset>
		<!--Form details begin-->
		<div class="col-md-12">
			<legend>
				<h4>
					<strong>Graph Search</strong>
				</h4>
			</legend>
		</div>
		<div class="col-md-8">
			<div class="col-md-6">
				<!-- Search value begin -->
				<div class="form-group">
					<label name="graph_search_label" class="control-label">Value</label>
					<input type="text" class="form-control" name="search_key"
						placeholder="Enter the search key"> </input>
				</div>
				<!-- Search Value End -->
			</div>
			<div class="col-md-6">
				<!-- Search type search -->
				<div class="form-group">
					<label for="select" class="control-label">Search Type</label> <select
						class="form-control" name="search_type">
						<option value="">Select</option>
						<option value="author">Author</option>
						<option value="publication">Publication</option>
						<option value="venue">Venue</option>
						<option value="year">Year</option>
					</select>
				</div>
				<!--Seach Type end-->
			</div>
		</div>
		<br />
		<!--Form Heading-->
		<div class="form-group col-md-12">
			<div class="col-md-8 text-right">
				<button type="submit" class="btn btn-primary btn-sm">Search</button>
			</div>
		</div>
		<!--Form details end-->
	</fieldset>
</form>
<!--Search query form end-->