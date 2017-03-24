			/**
			 * 
			 */
			
			
			function ComAjax(opt_formId) {
				var fv_ajaxCallback = "";
				this.url = "";
				this.formId = gfn_isNull(opt_formId) == true ? "commonForm" : opt_formId;
				this.param = "";
				this.type = "get";
				this.async = true;
				
				this.setAsync = function setAsync(async){
					this.async = async;
				}
				
				this.setUrl = function setUrl(url) {
					this.url = url;
				};
				
				this.setCallback = function setCallback(callBack) {
					fv_ajaxCallback = callBack;
				};
				
				this.setType = function setType(type){
					this.type = type;
				}
				
				this.addParam = function addParam(key, value) {
					this.param = this.param + "&" + key + "=" + value;
				};
				
				this.ajax = function ajax() {
					if (this.formId != "commonForm") {
					this.param += "&" + $("#" + this.formId).serialize();
					
				}
					
					$.ajax({
						async : this.async,
						dataType : "json",
						url : this.url,
						type : this.type,
						data : this.param,
						success : function(data) {
							
							if (typeof (fv_ajaxCallback) == "function") {
								fv_ajaxCallback(data);
							} else {
							eval(fv_ajaxCallback + "(data);");
							}
						},
						error : function(request, error) {
							console.log("code:" + request.status + "\n" + "error:" + error);
						}
					
					});
				};
			}
			
			function gfn_isNull(sValue) {
				if (new String(sValue).valueOf() == "undefined")
					return true;
				if (sValue == null)
					return true;
				
				var v_ChkStr = new String(sValue);
				
				if (v_ChkStr == null)
					return true;
				if (v_ChkStr.toString().length == 0)
					return true;
				return false;
			}
