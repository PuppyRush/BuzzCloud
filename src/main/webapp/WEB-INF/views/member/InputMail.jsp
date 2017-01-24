<%

	request.setCharacterEncoding("UTF-8");

%>


<!doctype html>
<html lang="en">
 <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
  <title>Widget Store - 패스워드 변경</title>
  <link href="../WidgetClientPage/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="../WidgetClientPage/css/stylish-portfolio.css" rel="stylesheet">
	<!-- bootsnipp down -->
	<link href="../WidgetClientPage/css/login.css" rel="stylesheet">
    <!-- Custom Fonts -->
    <link href="../WidgetClientPage/font-awesome/font-awesome.css" rel="stylesheet" type="text/css">
    <link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,700,300italic,400italic,700italic" rel="stylesheet" type="text/css">
 </head>
 <body>

		

<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header" align="center">
					<img class="img-circle" id="img_logo2" src="../WidgetClientPage/img/test1.jpg">
					
				</div>
    <div id="div-forms">
                		<form id="register-edit-form" onsubmit="return match_Password()" method="GET" ACTION="ChangePasswd.do">
			<div class="modal-body">
		    	<div id="div-edit-register-msg">
        		  <div id="icon-register-msg" class="glyphicon glyphicon-chevron-right"></div>
                    <span id="text-register-msg">Register an account.</span>
                </div>
                    <input id="register_username" name="register_username" class="form-control" title="Username may only contain alphanumeric characters" type="text" placeholder="Username" required>
                            <input id="register_email" name="register_email" class="form-control" type="email" placeholder="E-Mail" required>
                            
				 <input id="register_password" name="register_password" class="form-control" title="minimum is 8 characters. and contain special character, numeric" type="password" placeholder="Password" required>
					<input id="register_password2" onchange="" class="form-control" type="password" placeholder="Rewrite password" required>
            		</div>
				<div class="modal-footer">
                    <div>
                         <button type="submit" class="btn btn-primary btn-lg btn-block">submit</button>
					</div>
                </div>
		
       </form>
		</div>
	</div>    
</div>
    	
	<script>
	function match_Password(){
		var pw1 = $('register_edit_password1').val();
		var pw2 = $('register_edit_password2').val();

		if(pw1 == pw2){
			return true;
		}
		else{
			alert("패스워드가 일치하지 않습니다.");
			return false;
		}
	}
	</script>

    <script src="../WidgetClientPage/js/jquery.js"></script>
    <script src="../WidgetClientPage/js/bootstrap.min.js"></script>
 </body>
</html>
