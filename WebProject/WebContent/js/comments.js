function addComment(id) {
	var value = $('#' + id + ' input').val();
	if(value != '') {
		var c = JSON.stringify({text: value, postID: id});
		$.ajax({
			url: "rest/profile/addComment",
			type: "POST",
			data: c,
			contentType: "application/json",
			dataType: "json",
			complete: function(data) {
				comment = data.responseJSON;
				$('#' + id + ' .comments-content').append(makeComment(comment, id));
				getLogged(function(user) {
					if (user.id == comment.authorId) {
						$('#' + comment.id).append('<span class="edit" onclick="editComment(\'' + comment.id + '\',\'' + comment.text + '\',\'' + id + '\')"><i class="uil uil-edit"></i></span>');
					} 
					if (user.id == comment.authorId || user.admin) {
						$('#' + comment.id).append('<span class="del" onclick="deleteComment(\'' + comment.id + '\',\'' + id + '\')"><i class="uil uil-trash-alt"></i></span>');
					} 
				});
				$('#' + id + ' input').val('');
				event.preventDefault();
	        }
	    });
	} else {
		alert("You can't add empty comment.");
	}
}

function makeComment(comment, postID) {
	var cardTemplate = [
		'<div class="message-left" id="' + comment.id + '">',
        '<div class="message-container">',
        '<div class="profile-picture">',
        '<img style="object-fit:cover;width:1.5rem;height:1.5rem;"',
        'src="images/userPictures/' + comment.authorId + '/' + comment.profilePicture + '">',
        '</div><div class="comment-author" style="font-size:12px;" id="' + comment.author + '">' + comment.name + ' ' + comment.lastname + '  ',
        '</div><div class="message-text"><span style="font-size:10px;padding:0px;">',
        comment.text,
        '</span></div></div><small style="font-size:8px;margin-left:1rem;color:black;float:left;">Last edited: ' + printDateTime(comment.lastEdited),
        '</small></div>'
	];
	return $(cardTemplate.join(''));
}

function viewComments(postID) {
	if ($('#' + postID + ' #view-comments p').text() != "Hide comments") {
		$.ajax({
	        url: "rest/profile/loadComments",
	        type: "POST",
	        data: postID,
	        contentType: "application/json",
	        complete: function(data) {
	        	var comments = data.responseJSON;
	        	$('#' + postID + ' #view-comments').empty();
	        	if (comments.length == 0) {
	        		$('#' + postID + ' #view-comments').append("<p>No comments here.</p>");
	        		event.preventDefault();
	        		return;
	        	}
	        	loadCommentsOnPost(comments, postID);
	        	event.preventDefault();
	        }
	    });
	} else {
		$('#' + postID + ' #view-comments').empty();
		$('#' + postID + ' #view-comments').append('<p>View all comments</p>');
	}
}

function loadCommentsOnPost(comments, id) {
	$('#' + id + ' #view-comments').append('<p>Hide comments</p><div class="comments-content"' + ' id="' + id + '"></div>');
	for (let i = comments.length - 1; i >= 0; i -= 1) {
		$('#' + id + ' #view-comments').append(makeComment(comments[i], id));
		getLogged(function(user) {
			if (user.id == comments[i].authorId) {
				$('#' + comments[i].id).append('<span class="edit" onclick="editComment(\'' + comments[i].id + '\',\'' + comments[i].text + '\',\'' + id + '\')"><i class="uil uil-edit"></i></span>');
			} 
			if (user.id == comments[i].authorId || user.admin) {
				$('#' + comments[i].id).append('<span class="del" onclick="deleteComment(\'' + comments[i].id + '\',\'' + id + '\')"><i class="uil uil-trash-alt"></i></span>');
			} 
		});
	}
}

function deleteComment(comID, pid) {
	  if (confirm('Are you sure you want to delete this comment?')) {
		var c = JSON.stringify({commentID: comID, text: '', postID: pid});
		  $.ajax({
				url: "rest/profile/deleteComment",
				type: "POST",
				data: c,
				contentType: "application/json",
				dataType: "json",
				complete: function(data) {
					comment = data.responseJSON;
					$('#' + comment.id).fadeOut();
					event.preventDefault();
		        }
		    });
	  }
}

function editComment(comID, comText, pid) {
	let text = prompt("Edit comment", comText);
	  if (text != null) {
		var c = JSON.stringify({commentID: comID, text: text, postID: pid});
		  $.ajax({
				url: "rest/profile/editComment",
				type: "POST",
				data: c,
				contentType: "application/json",
				dataType: "json",
				complete: function(data) {
					comment = data.responseJSON;
					$('#' + comment.id + ' small').empty();
					$('#' + comment.id + ' small').append('Last edited: ' + printDateTime(comment.lastEdited));
					$('#' + comment.id + ' .message-text span').empty();
					$('#' + comment.id + ' .message-text span').append(comment.text);
					event.preventDefault();
		        }
		    });
	  }
}