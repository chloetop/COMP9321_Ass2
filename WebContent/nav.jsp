<!--Navigation Bar for the page-->
<nav class="navbar navbar-default">
	<!--Page Navigation bar header-->
	<div class="navbar-header">
		<button type="button" class="navbar-toggle collapsed"
			data-toggle="collapse" data-target="#bs-example-navbar-collapse-2">
			<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span>
			<span class="icon-bar"></span> <span class="icon-bar"></span>
		</button>
		<a class="navbar-brand" href="#">&nbsp;<strong>dblp</strong> Store
		</a>
	</div>
	<!--Page Navigation bar header end-->
	<!--Navigation bar menu items-->
	<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-2">
		<ul class="nav navbar-nav">
			<li class="active" id="nav-menu-item-1" onclick="onClick()"><a
				href="." onclick="openPage(event, 'home')">Home<span
					class="sr-only">(current)</span></a></li>
			<li id="nav-menu-item-2" onclick="onClick()"><a href="#"
				onclick="openPage(event, 'search')">Search</a></li>
		</ul>
		<!--Title Search form in the Navigation bar-->
		<form class="navbar-form navbar-right" role="search_title"
			method="post" action="search" onsubmit="titlesearch()">
			<div class="form-group">
				<input type="text" class="form-control" placeholder="Title Search"
					name="title_query" /> <input type="hidden" name="action"
					value="search_title" />
			</div>
			<button type="submit" class="btn btn-primary btn-sm">Submit</button>
		</form>
		<!--Title Search form end-->
		<ul class="nav navbar-nav navbar-right">
			<li><a href="#" data-toggle="modal" data-target="#quicksearch"><span
					style="cursor: pointer">Quick Search</span></a></li>
			<li><a href="#" onclick="openNav()"><span
					style="cursor: pointer">Shopping cart</span></a></li>
		</ul>
	</div>
	<!--Navigation bar menu items end-->
</nav>