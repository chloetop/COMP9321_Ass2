<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="container-fluid" id="upload_item" style="display: none;">
	<!--Search Query Form-->
	<form class="form-horizontal" action="home" method="post" enctype="multipart/form-data">
		<input type="hidden" name="action" value="upload_item"/>
		
		<div class="form-group col-md-6">
			<label name="item_name_label" class="col-md-3 control-label">
				Publication Type</label>
			<div class="col-md-7">
				<input type="text" class="form-control" name="publication" placeholder="Book, Article, Thesis.."></input>
			</div>
		</div>
		
		<div class="form-group col-md-6">
			<label name="item_name_label" class="col-md-3 control-label">
				Publication Title</label>
			<div class="col-md-7">
				<input type="text" class="form-control" name="item_name" placeholder="Enter your Publication Title"></input>
			</div>
		</div>
		
		<div class="form-group col-md-6">
			<label name="item_name_label" class="col-md-3 control-label">
				Authors</label>
			<div class="col-md-7">
				<input type="text" class="form-control" name="authors" placeholder="Enter Authors' Names seperated by comma"></input>
			</div>
		</div>
		
		<div class="form-group col-md-6">
			<label name="item_name_label" class="col-md-3 control-label">
				Price</label>
			<div class="col-md-7">
				<input type="text" class="form-control" name="price" placeholder="Enter price for your Article"></input>
			</div>
		</div>
		
		<div class="form-group col-md-6">
			<label name="item_name_label" class="col-md-3 control-label">Publication Year</label>
			<div class="col-md-7">
				<input type="text" class="form-control" name="year" placeholder="Year"></input>
			</div>
		</div>
		
		<div class="form-group col-md-6">
			<label name="item_name_label" class="col-md-3 control-label">Link</label>
			<div class="col-md-7">
				<input type="text" class="form-control" name="url" placeholder="URL"></input>
			</div>
		</div>
		
		<div class="form-group col-md-6">
			<div class="col-md-7">
				<label name="upload_file_label" class="col-md-3 control-label">Upload Publication Cover:</label>
				<input type="file" name="photo"></input>
			</div>
		</div>
		
		<div class="form-group col-md-6">
			<div class="col-md-7">
				<input type="checkbox" name="on_hold"></input>
				<label name="item_name_label" class="col-md-3 control-label">Put item on hold</label>
			</div>
		</div>
		
		<div class="form-group col-md-11">
			<div class="col-md-12 text-right">
				<button type="submit" class="btn btn-primary btn-sm">Upload</button>
			</div>
		</div>
		
<!-- 		<div class="alert alert-success" id="success_dialogue">
  			<strong>Success!</strong> Item uploaded successfully!
  			<span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
		</div> -->
		
	</form>
</div>