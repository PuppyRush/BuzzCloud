
 function checkFullForm(form)
 {
	 
   if(form.nickname.value == "") {
	   	ohSnap("Error: nickname cannot be blank!",{color:"red"});
	     form.nickname.focus();
	     return false;
	   	}
	   re = /^\w+$/;
	   if(!re.test(form.nickname.value)) {
	   	ohSnap("Error: nickname must contain only letters, numbers and underscores!",{color:"red"});
	     form.nickname.focus();
	     return false;
	   	}
	 
   if(form.password.value != "" && form.password.value == form.password2.value) {
     if(form.pwd1.value.length < 6) {
     ohSnap("Error: Password must contain at least six characters!",{color:"red"});
       form.password.focus();
       return false;
     	}

	 if(form.password.value == form.nickname.value) {
	 		ohSnap("Error: Password must be different from nickname!",{color:"red"});
	   form.password.focus();
	   return false;
 		}
	 re = /[0-9]/;
	 if(!re.test(form.password.value)) {
	 		ohSnap("Error: password must contain at least one number (0-9)!",{color:"red"});
	   form.password.focus();
	   return false;
     	}
     re = /[a-z]/;
     if(!re.test(form.password.value)) {
       ohSnap("Error: password must contain at least one lowercase letter (a-z)!",{color:"red"});
       form.password.focus();
       return false;
     }
     re = /[A-Z]/;
     if(!re.test(form.password.value)) {
     ohSnap("Error: password must contain at least one uppercase letter (A-Z)!",{color:"red"});
       form.password.focus();
       return false;
     }
   } else {
     ohSnap("Error: Please check that you've entered and confirmed your password!",{color:"red"});
     form.password.focus();
     return false;
   }
   
   return true;
 }
 
 function checkProfileForm(form){
	 
	 
 	 if(!isValidateString(form.firstname.value)){
 	 		ohSnap("변경할 값에 and or = 혹은 특수문자가 들어갈 수 없습니다.",{color:"red"});
 	 	}
 	if(!isValidateString(form.lastname.value)){
		ohSnap("변경할 값에 and or = 혹은 특수문자가 들어갈 수 없습니다.",{color:"red"});
	}
 	if(!isValidateString(form.nickname.value)){
		ohSnap("변경할 값에 and or = 혹은 특수문자가 들어갈 수 없습니다.",{color:"red"});
	}
	 if(form.firstname.value==""){
	 ohSnap("Error: firstname cannot be blank!",{color:"red"});
	   form.firstname.focus();
	   return false;
	 }
	 if(form.lastname.value==""){
	 ohSnap("Error: lastname cannot be blank!",{color:"red"});
	   form.lastname.focus();
	   return false;
	 }
	 if(form.nickname.value==""){
	 ohSnap("Error: nickname cannot be blank!",{color:"red"});
	   form.nickname.focus();
	   return false;
	 }
	

   
   return true;
 }
 	
 
 function checkPasswordForm(form){
	 re = /[0-9]/;
	 if(!re.test(form.password.value)) {
	   ohsnap("Error: password must contain at least one number (0-9)!",{color:"red"});
	   form.password.focus();
	   return false;
     	}
     re = /[a-z]/;
     if(!re.test(form.password.value)) {
       ohSnap("Error: password must contain at least one lowercase letter (a-z)!",{color:"red"});
       form.password.focus();
       return false;
     	}
	 re = /[A-Z]/;
	 if(!re.test(form.password.value)) {
	 ohSnap("Error: password must contain at least one uppercase letter (A-Z)!",{color:"red"});
	   form.password.focus();
	   return false;
	 		}
	 if(form.password.value != form.password2.value) {
	     ohSnap("Error: Please check that you've entered and confirmed your password!",{color:"red"});
	     form.password.focus();
	     return false;
	   	}
 }