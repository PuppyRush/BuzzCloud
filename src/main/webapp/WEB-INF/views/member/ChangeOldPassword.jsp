<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<!doctype html>
<html lang="en">
 <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
  <title>Widget Store - í¨ì¤ìë ë³ê²½</title>
  <link href="WidgetClientPage/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="WidgetClientPage/css/stylish-portfolio.css" rel="stylesheet">
	<!-- bootsnipp down -->
	<link href="WidgetClientPage/css/login.css" rel="stylesheet">
    <!-- Custom Fonts -->
    <link href="WidgetClientPage/font-awesome/font-awesome.css" rel="stylesheet" type="text/css">
    <link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,700,300italic,400italic,700italic" rel="stylesheet" type="text/css">
 </head>
 <body>
	<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header" align="center">
					<img class="img-circle" id="img_logo2" src="WidgetClientPage/img/test1.jpg">
					
				</div>
    <div id="div-forms">
		<form id="register-edit-form" onsubmit="return match_Password()" method="GET" ACTION="ChangePasswd.do">
			<div class="modal-body">
		    	<div id="div-edit-register-msg">
                    <div id="icon-edit-register-msg" class="glyphicon glyphicon-chevron-right"></div>
                         <span id="text-register-msg">Change Password [ìì´ë ë¤ì´ê° ê³³]</span>
                    </div>
					<input id="register_edit_password1" name="register_edit_password1" class="form-control" title="minimum is 8 characters. and contain special character, numeric" type="password" placeholder="Password" required>
					<input id="register_edit_password2" name="register_edit_password2" class="form-control" type="password" placeholder="Rewrite password" required>
            		</div>
				<div class="modal-footer">
                    <div>
                         <button type="submit" class="btn btn-primary btn-lg btn-block">submit</button>
					</div>
                </div>
			</div>
       </form>
    </div>
	<script>
	function match_Password(){
		var pw1 = $('register_edit_password1').val();
		var pw2 = $('register_edit_password2').val();

		if(pw1 == pw2){
			return true;
		}
		else{
			alert("í¨ì¤ìëê° ì¼ì¹íì§ ììµëë¤.");
			return false;
		}
	}
	</script>

    <script src="WidgetClientPage/js/jquery.js"></script>
    <script src="WidgetClientPage/js/bootstrap.min.js"></script>
 </body>
</html>
