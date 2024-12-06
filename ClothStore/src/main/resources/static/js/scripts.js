/**
 *  
 */

function validateBillingAddress() {
	if ($("#theSameAsShippingAddress").is(":checked")) {
		$(".billingAddress").prop("disabled", true);
	} else {
		$(".billingAddress").prop("disabled", false);
	}
}


function ensurePasswordMatch() {
	var secretKey = $("#txtNewPassword").val();
	var confirmSecretKey = $("#txtConfirmPassword").val();

	if (secretKey == "" && confirmSecretKey == "") {
		$("#checkPasswordMatch").html("");
		$("#updateUserInfoButton").prop('disabled', false);
	} else {
		if (secretKey != confirmSecretKey) {
			$("#checkPasswordMatch").html("Passwords do not match!");
			$("#updateUserInfoButton").prop('disabled', true);
		} else {
			$("#checkPasswordMatch").html("Passwords match");
			$("#updateUserInfoButton").prop('disabled', false);
		}
	}
}

$(document).ready(function() {
	$(".cartItemQty").on('change', function() {
		var id = this.id;

		$('#update-item-' + id).css('display', 'inline-block');
	});
	$("#theSameAsShippingAddress").on('click', validateBillingAddress);
	$("#txtConfirmPassword").keyup(ensurePasswordMatch);
	$("#txtNewPassword").keyup(ensurePasswordMatch);
});