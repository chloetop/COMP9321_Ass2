<!-- Modal -->
<div class="container">
	<div id="quicksearch" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">Quick Search</h4>
				</div>
				<form class="form-horizontal" action="search" method="post">
					<div class="modal-body">
						<input type="hidden" name="action" value="quick_search" />
						<!-- Title begin -->
						<div class="form-group has-feedback">
							<label name="title_label" class="control-label">Title</label> <input
								type="text" class="form-control" name="title"
								placeholder=" Enter the title"> </input>
						</div>
						<!-- Title End -->
						<!-- Author begin -->
						<div class="form-group">
							<label name="author_label" class="control-label">Author</label> <input
								type="text" class="form-control" name="author"
								placeholder="Enter the author"> </input>
						</div>
						<!-- Author End -->
						<!-- Document type search -->
						<div class="form-group">
							<label for="select" class="control-label">Document Type</label> <select
								class="form-control" name="type">
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
					<div class="modal-footer">
						<button type="submit" class="btn btn-default btn-sm">Search</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>