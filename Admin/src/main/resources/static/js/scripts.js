/**
 * 
 */

// lock account by select box 
document.addEventListener("DOMContentLoaded", function() {
	const chooseAll = document.getElementById('selectAllUsers');
	const selectionBoxes = document.querySelectorAll('.checkboxUser');
	const switchSelectedButton = document.getElementById('toggleSelected');

	chooseAll.addEventListener('change', function() {
		selectionBoxes.forEach(selectbox => {
			selectbox.checked = chooseAll.checked;
		});
	});

	switchSelectedButton.addEventListener('click', function() {
		const userIdentity = Array.from(selectionBoxes)
			.filter(currentBox => currentBox.checked)
			.map(currentBox => currentBox.value); 

		if (userIdentity.length === 0) {
			bootbox.alert('No user is seleted.');
			return;
		}

		const currentSelect = document.querySelector(`tr input[value="${userIdentity[0]}"]`).closest('tr');
		const checkCurrent = currentSelect.querySelector('td:nth-child(7)').textContent.trim() === 'true';

		const operation = checkCurrent ? 'lock' : 'unlock';
		const approvalPrompt = `Do you really want to ${operation} this user(s) profile?`;

		bootbox.confirm({
			message: approvalPrompt,
			buttons: {
				confirm: {
					label: 'Verifies',
					className: 'btn-success'
				},
				cancel: {
					label: 'Exit',
					className: 'btn-danger'
				}
			},
			callback: function(validated) {
				if (validated) {
					const link = checkCurrent ? '/admin/lockUsers' : '/admin/unlockUsers';

					fetch(link, {
						method: 'PUT',
						headers: {
							'Content-Type': 'application/json'
						},
						body: JSON.stringify({ userIds: userIdentity }) 
					})
						.then(reply => {
							if (reply.ok) {
								const completionMessage = 'User account has been successfully updated.';
								bootbox.alert(completionMessage); 

								userIdentity.forEach(identification => {
									const entry = document.querySelector(`tr input[value="${identification}"]`).closest('tr');
									if (checkCurrent) {
										entry.querySelector('td:nth-child(7)').textContent = 'false'; 
										const trigger = entry.querySelector('button');
										trigger.classList.remove('btn-success');
										trigger.classList.add('btn-danger');
										trigger.querySelector('span.fa').classList.remove('fa-unlock');
										trigger.querySelector('span.fa').classList.add('fa-lock');
										trigger.querySelector('span:last-child').textContent = ' Lock'; 
									} else {
										entry.querySelector('td:nth-child(7)').textContent = 'true'; 
										const trigger = entry.querySelector('button');
										trigger.classList.remove('btn-danger');
										trigger.classList.add('btn-success');
										trigger.querySelector('span.fa').classList.remove('fa-lock');
										trigger.querySelector('span.fa').classList.add('fa-unlock');
										trigger.querySelector('span:last-child').textContent = ' Unlock';
									}
								});
							} else {
								bootbox.alert('Update failed for user(s) status.'); 
							}
						})
						.catch(error => {
							console.error('Error:', error);
							bootbox.alert('Error encountered during update for user(s) status.'); 
						});
				}
			}
		});
	});
});


// lock account by click the button
document.addEventListener("DOMContentLoaded", function() {
	document.querySelectorAll('.toggle-user').forEach(function(button) {
		button.addEventListener('click', function(event) {
			event.preventDefault(); 

			let identity = this.getAttribute('data-user-id'); 
			if (!identity) {
				console.error('No User ID provided');
				return;
			}

			const entity = this.closest('tr');
			const isActived = entity.querySelector('td:nth-child(7)').textContent.trim() === "true";

			const task = isActived ? 'lock' : 'unlock';
			const approvalPrompt = `Do you really want to ${task} this user profile?`;

			bootbox.confirm({
				message: approvalPrompt,
				buttons: {
					confirm: {
						label: 'Approve',
						className: 'btn-success'
					},
					cancel: {
						label: 'Terminate',
						className: 'btn-danger'
					}
				},
				callback: function(Validate) {
					if (Validate) {
						const link = isActived ? `/admin/lockUser/${identity}` : `/admin/unlockUser/${identity}`;

						fetch(link, {
							method: 'PUT',
							headers: {
								'Content-Type': 'application/json'
							}
						})
							.then(reply => {
								if (reply.ok) {
									const confirmationMessage = 'The update to the user account was successful.';
									bootbox.alert(confirmationMessage); 

									if (isActived) {
										entity.querySelector('td:nth-child(7)').textContent = "false"; 
										button.classList.remove('btn-success');
										button.classList.add('btn-danger');
										button.querySelector('span.fa').classList.remove('fa-unlock');
										button.querySelector('span.fa').classList.add('fa-lock');
										button.querySelector('span:last-child').textContent = ' Lock'; 
									} else {
										entity.querySelector('td:nth-child(7)').textContent = "true"; 
										button.classList.remove('btn-danger');
										button.classList.add('btn-success');
										button.querySelector('span.fa').classList.remove('fa-lock');
										button.querySelector('span.fa').classList.add('fa-unlock');
										button.querySelector('span:last-child').textContent = ' Unlock'; 
									}
								} else {
									const failureMessage = isActived ? 'User account could not be locked.' : 'Unable to unlock the user account.';
									bootbox.alert(failureMessage); 
								}
							})
							.catch(error => {
								console.error('Error:', error);
								bootbox.alert('Something went wrong.'); 
							});
					}
				}
			});
		});
	});
});















// delete user by click the button

$(document).ready(function() {
	$('.delete-shirt').on('click', function() {
		var route = /*[[@{/}]]*/'remove';

		var identity = $(this).attr('id');

		bootbox.confirm({
			message: "Are you certain you want to remove this shirt? This action cannot be undone.",
			buttons: {
				cancel: {
					label: '<i class="fa fa-times"></i> Cancel'
				},
				confirm: {
					label: '<i class="fa fa-check"></i> Confirm'
				}
			},
			callback: function(validated) {
				if (validated) {
					$.post(route, { 'id': identity }, function() {
						location.reload();
					});
				}
			}
		});
	});



	

	$('#deleteSelected').click(function() {
		var identity = $('.checkboxShirt');
		var identityList = [];
		for (var i = 0; i < identity.length; i++) {
			if (identity[i].checked == true) {
				identityList.push(identity[i]['id'])
			}
		}


		var link = /*[[@{/}]]*/'removeList';

		bootbox.confirm({
			message: "Do you really want to delete all selected shirts? This change is irreversible.",
			buttons: {
				cancel: {
					label: '<i class="fa fa-times"></i> Cancel'
				},
				confirm: {
					label: '<i class="fa fa-check"></i> Confirm'
				}
			},
			callback: function(approved) {
				if (approved) {
					$.ajax({
						type: 'POST',
						url: link,
						data: JSON.stringify(identityList),
						contentType: "application/json",
						success: function() {
							location.reload()
						},
						error: function() {
							location.reload();
						}
					});
				}
			}
		});
	});

	
	$("#selectAllShirts").click(function() {
		if ($(this).prop("checked") == true) {
			$(".checkboxShirt").prop("checked", true);
		} else if ($(this).prop("checked") == false) {
			$(".checkboxShirt").prop("checked", false);
		}
	})
});