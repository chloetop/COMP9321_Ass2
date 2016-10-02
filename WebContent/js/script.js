function openNav() {
	document.getElementById("mySidenav").style.width = "450px";
	document.getElementById("main").style.marginLeft = "450px";
	document.body.style.backgroundColor = "rgba(0,0,0,0.4)";
}

function closeNav() {
	document.getElementById("mySidenav").style.width = "0";
	document.getElementById("main").style.marginLeft = "0";
	document.body.style.backgroundColor = "white";
}

function onClick() {
	document.getElementById('nav-menu-item-1').classList.toggle('active');
	document.getElementById('nav-menu-item-2').classList.toggle('active');
	document.getElementById('nav-menu-item-3').classList.toggle('active');
}

function openPage(evt, page) {
	document.getElementById(page).style.display = "block";

	if (page === "home") {
		document.getElementById("search").style.display = "none";
		document.getElementById("upload_item").style.display = "none";
	}

	if (page === "search") {
		document.getElementById("home").style.display = "none";
		document.getElementById("upload_item").style.display = "none";
	}
	
	if (page === "upload_item") {
		document.getElementById("search").style.display = "none";
		document.getElementById("home").style.display = "none";
	}
}

function modal_open(hashcode) {
	var modal = document.getElementById("myModal_".concat(hashcode));
	modal.style.display = "block";
}

function close_modal(hashcode) {
	var modal = document.getElementById("myModal_".concat(hashcode));
	modal.style.display = "none";
}

function modal_cart_open(hashcode) {
	var modal = document.getElementById("cartModal_".concat(hashcode));
	modal.style.display = "block";
}

function close_cart_modal(hashcode) {
	var modal = document.getElementById("cartModal_".concat(hashcode));
	modal.style.display = "none";
}

// Hide the alerts for the cart messages
$(document).ready(function() {
	$("#item_add_success").hide();
	$("#item_remove_success").hide();

});

// add item to the cart
function addToCart(hashcode) {
	var form = $('#cart'.concat(hashcode));
	var test = $('#cart'.concat(hashcode)).serialize();

	// Close the document display modal pane
	var modal = document.getElementById("myModal_".concat(hashcode));
	modal.style.display = "none";

	if (test === null) {
		console.log("Test is null");
	}
	$
			.ajax({
				type : 'POST',
				url : 'cart',
				data : {
					'title' : $(('input#'.concat('title_')).concat(hashcode))
							.val(),
					'action' : 'add_cart',
					'hashcode' : hashcode
				},
				success : function(data) {
					
					new_title = data;
					
					var cart_json = JSON.parse(data);
					
					var cart_content = document.getElementById("cart_content").innerHTML;
					var modal_content = document
							.getElementById("modal_contents").innerHTML;

					var cart_empty = null;
					if (document.getElementById("empty_cart") !== null)
						cart_empty = document.getElementById("empty_cart").innerHTML;

					if (!(cart_empty === null)) {
						document.getElementById("cart_content").innerHTML = cart_json.cart;
						document.getElementById("modal_contents").innerHTML = cart_json.modal;
					} else {
						document.getElementById("cart_content").innerHTML = cart_content
								+ cart_json.cart;
						document.getElementById("modal_contents").innerHTML = modal_content
								+ cart_json.modal;
					}
					// Display alert
					$("#item_add_success").alert();
					$("#item_add_success").fadeTo(2000, 500).slideUp(500,
							function() {
								$("#item_add_success").slideUp(500);
							});

				}
			}

			);
	return false; // not refreshing page
}

function removeCartItem(hashcode, title) {
	$('#'.concat(hashcode)).remove();

	$.ajax({
		type : 'POST',
		url : 'cart',
		data : {
			'title' : title,
			'action' : 'remove_cart',
			'hashcode' : hashcode
		},
		success : function(data) {
			// Empty Cart

			if (data.length > 2) {
				document.getElementById("cart_content").innerHTML = data;

			}
			// Display Alert
			$("#item_remove_success").alert();
			$("#item_remove_success").fadeTo(2000, 500).slideUp(500,
					function() {
						$("#item_remove_success").slideUp(500);
					});

		}
	}

	);
	return false;
}

function titlesearch() {
	console.log('reached here to validate the form');
}

// Function to handle the next page logic
function nextPage() {
	console.log('Next Page function');

	$.ajax({
		type : 'POST',
		url : 'next',
		data : {
			'action' : 'next',
		},
		success : function(data) {
			console.log('Done');

		}
	}

	);
}

function quicksearch_open() {
	var modal = document.getElementById("quicksearch");
	modal.style.display = "block";

}

function close_quicksearch() {
	var modal = document.getElementById("quicksearch");
	modal.style.display = "none";
}

function priceMinUpdate(vol) {
	document.querySelector('#price-min-out').value = vol;
}

function priceMaxUpdate(vol) {
	document.querySelector('#price-max-out').value = vol;
}
