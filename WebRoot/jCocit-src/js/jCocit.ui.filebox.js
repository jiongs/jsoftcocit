/**
 * 文件列表组件
 */

(function($){
	$.fn.filebox = function(options, args){
		if (typeof options == "string") {
			var fn = methods[options];
			if (fn){
				return fn(this, args);
			}else{
				alert('The method ' + options + ' does not exist in $.fn.filebox');
			}
		}
		options = options || {};
		return this.each(function(){
			var state = $.data(this, "datafilebox");
			var opts;
			if (state) {
				opts = $.extend(state.options, options);
				state.options = opts;
			}else {
				opts = $.extend({}, defaultOpts, _parseOptions(this), options);
				var fileInfoStr = $(this).val() || "";
				var data = {
					total:0,
					row:[]
				};
				if(fileInfoStr != ""){
					try{
						var fileInfoArry = eval("data=" + fileInfoStr);
						data = {
							total : fileInfoArry.length,
							row : fileInfoArry
						};
					}catch(e){
						alert("解析文件信息Json失败，文件列表加载失败！");
						return false;
					}
				}
				var extStr = $(this).prop("id") || Math.round(Math.random() * 10000);
				var fileboxId = "filebox-table-" + extStr; 
				var selectFileBtnId = fileboxId + "-sect";
				var uploadQueueId = "fileQueue-" + extStr;
				$.data(this, "datafilebox", {
					fileboxId : fileboxId,
					selectFileBtnId: selectFileBtnId,
					uploadQueueId: uploadQueueId,
					options : opts,
					data : data
				});
			}
			_initFileBox(this);
		});
	};
	
	var methods = {
			addFile: function($self, args){
				if(typeof args == 'object'){
					_addFile($self, args);
				}else{
					alert("参数错误，添加文件失败！");
				}
			},
			delFile: function($self, args){
				if(typeof args == 'string'){
					_delFile($self, args)
				}else{
					alert("参数错误，删除文件失败！");
				}
			},
			download: function($self, args){
				if(typeof args == 'string'){
					_delFile($self, args)
				}else{
					alert("参数错误，下载文件失败！");
				}
			},
			readFile: function($self, args){
				
			},
			editFile: function($self, args){
				
			},
			existUnupload: function($self, args){
				
			}
	};
	
	var defaultOpts = {
			swfPath: jCocit.contextPath + "/jCocit-src/css/images/ui_upload_uploadify.swf",
			cancelImgPath: jCocit.contextPath + "/jCocit-src/css/images/ui_upload_uploadify_cancel.png",
			uploadUrl: jCocit.contextPath + "/coc/upload",
			editurl: jCocit.contextPath + "/coc/download",
			isThunderDownload: true
	};
	
	function _parseOptions(iptTarget){
		var $ipt = $(iptTarget);
		var opts = {};
		opts.model = $ipt.data("model") || "";
		opts.deplayUpload = $ipt.data("deplayupload") || false;
		opts.multiUpload = $ipt.data("multiupload") || false;
		
		var swfPath = $ipt.data("swfpath") || "";
		if(swfPath){
			opts.swfPath = swfPath;
		}
		var cancelImgPath = $ipt.data("cancelimgpath") || "";
		if(cancelImgPath){
			opts.cancelImgPath = cancelImgPath;
		}
		var formDataStr = $ipt.data("formdata") || "";
		if(formDataStr != ""){
			try{
				opts.formData = eval("data=" + formDataStr);
			}catch(e){
				opts.formData = {};
				alert("解析formData失败！");
			}
		}
		var uploadUrl = $ipt.data("uploadurl") || "";
		if(uploadUrl){
			opts.uploadUrl = uploadUrl;
		}
		var uploadUrl = $ipt.data("uploadurl") || "";
		if(uploadUrl){
			opts.uploadUrl = uploadUrl;
		}
		var editUrl = $ipt.data("editurl") || "";
		if(editUrl){
			opts.editUrl = editUrl;
		}
		var delUrl = $ipt.data("delurl") || "";
		if(delUrl){
			opts.delUrl = delUrl;
		}
		
		return opts;
	};
	
	function _initFileBox(iptTarget) {
		var state = $.data(iptTarget, "datafilebox");
		var opts = state.options;
		var data = state.data;
		
		if(opts.swfPath && opts.cancelImgPath){
			_initHeader(iptTarget);
			if(data.total > 0){
				for(var i = 0; i < data.total; i++){
					_addFile(iptTarget, data.row[i]);
				}
			}
			_initUploadBtn(iptTarget);
			_initAllBtn(iptTarget);
		}else{
			alert("参数错误，文件列表加载失败！");
		}
	};
	
	function _initHeader(iptTarget){
		var opts = $.data(iptTarget, "datafilebox").options;
		var fileboxId = $.data(iptTarget, "datafilebox").fileboxId;
		var selectFileBtnId = $.data(iptTarget, "datafilebox").selectFileBtnId;
		var $fileBoxTable = $("#"+fileboxId);
		if($fileBoxTable.length == 0){
			var tabHtmlStr = "<table id=\""+ fileboxId +"\" class=\"filebox-table\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">" +
			"<tr class=\"filebox-header-row\">" +
			"<th class=\"filebox-td-rownum\">" +
			"序号" +
			"</th>" +
			"<th class=\"filebox-td-title\">" +
			"文件名称" +
			"</th>" +
			"<th class=\"filebox-td-status\">" +
			"状态" +
			"</th>" +
			"<th class=\"filebox-td-opt\">";
			if(opts.model){
				if(opts.model.indexOf("a") != -1){
					tabHtmlStr += "<a id=\"" + selectFileBtnId + "\" class=\"filebox-opt-sect\" href=\"javascript:void(0);\">选择</a>";
				}
				if(opts.model.indexOf("n") != -1){
					tabHtmlStr += "<a id=\"" + fileboxId + "-new\" class=\"filebox-opt-new\" href=\"javascript:void(0);\">新建</a>";
				}
				if(opts.model.indexOf("a") != -1 && opts.deplayUpload){
					tabHtmlStr += "<a id=\"" + fileboxId + "-upload-all\" class=\"filebox-opt-upload-all\" href=\"javascript:void(0)\">上传全部</a>";
				}
			}
			tabHtmlStr += "</th></tr>";
			tabHtmlStr += "</table>";
			$(iptTarget).after(tabHtmlStr);
			$(iptTarget).css("display", "none");
		}
	};
	
	function _initUploadBtn(iptTarget){
		var opts = $.data(iptTarget, "datafilebox").options;
		var fileboxId = $.data(iptTarget, "datafilebox").fileboxId;
		var $fileBoxTable = $("#"+fileboxId);
		var uploadQueueId = $.data(iptTarget, "datafilebox").uploadQueueId;
		
		 $("#"+fileboxId+"-sect").uploadify({
			 'uploader': opts.uploadUrl,
             'swf': opts.swfPath,
             'cancelImage': opts.cancelImgPath,
             'fileObjName': 'upload',
             'queueID': uploadQueueId,
             'auto': !opts.deplayUpload,
             'multi': opts.multiUpload,
             'progressData': 'percentage',
             'removeCompleted': true, //这个地方必须为true
             'removeTimeout': 0,
             'buttonText': '添加文件',
             'overrideEvents': ['onSelect', 'onUploadComplete', 'onUploadError'],
             'formData': opts.formData,
             'width' : 60,
             'height' : 16,
             // 选择文件后触发
             'onSelect': function (file) {
            	 var i = this.settings;
            	 var rowData = {
            			 fileId: file.id,
            			 text: file.name,
            			 type: file.type,
            			 size: file.size,
            			 instanceId: i.id
            	 };
            	 _addFile(iptTarget, rowData);
             },
             // 选择文件后出错时触发
             'onSelectError': function (file, errorCode, errorMsg) {
                 // $('#fileQueue').attr('style', 'visibility :hidden');
             },
             // 在一个文件开始上传之前触发。
             'onUploadStart': function (file) {
                 // $('#fileQueue').attr('style', 'top:200px;left:400px;width:400px;height :400px;visibility :visible');
             },
             // 每个文件上传后更新一次进度信息
             'onUploadProgress': function(file, bytesUploaded, bytesTotal, totalBytesUploaded, totalBytesTotal){
            	 
             },
             // 上传文件成功后触发（每一个文件都触发一次）
             'onUploadComplete': function (file){
            	 var g = this.settings, d = this;
            	 var e = this.getStats();
            	 if (g.removeCompleted) {
 					switch (file.filestatus) {
 						case SWFUpload.FILE_STATUS.COMPLETE:
 							setTimeout(function() {
 								if ($("#" + file.id).length > 0) {
 									d.queueData.queueSize -= file.size;
 									d.queueData.queueLength -= 1;
 									delete d.queueData.files[file.id];
 									$("#" + file.id).fadeOut(500, function() {
 										$(this).remove();
 									});
 								}
 							}, g.removeTimeout * 1000);
 							break;
 						case SWFUpload.FILE_STATUS.ERROR:
 							$("#" + file.id).find(".filebox-row-opt-del-fail").click(function(){
 								if (!g.requeueErrors) {
 	 								setTimeout(function() {
 	 									if ($("#" + file.id).length > 0) {
 	 										d.queueData.queueSize -= file.size;
 	 										d.queueData.queueLength -= 1;
 	 										delete d.queueData.files[file.id];
 	 										$("#" + file.id).fadeOut(500, function() {
 	 											$(this).remove();
 	 											_doSort(iptTarget);
 	 										});
 	 									}
 	 								}, g.removeTimeout * 1000);
 	 							}
 							});
 							break;
 					}
            	 } else {
            		 f.uploaded = true;
            	 }
            	 
            	 _initAllBtn(iptTarget);
            	 _doSort(iptTarget);
            	 if(opts.deplayUpload && $fileBoxTable.find(".filebox-no-upload").length == 0){
            		 $fileBoxTable.find(".filebox-opt-upload-all").unbind("click");
            	 }
             },
             'onUploadError': function (file, errorCode, errorMsg, errorString){
            	 var setting = this.settings;
            	 var tipText = "上传失败";
            	 switch (errorCode) {
            	 	case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
            	 		errorString = "·HTTP错误 (" + errorMsg + ")";
            	 		break;
            	 	case SWFUpload.UPLOAD_ERROR.MISSING_UPLOAD_URL:
            	 		errorString = "·上传URL为空";
	 					break;
	 				case SWFUpload.UPLOAD_ERROR.IO_ERROR:
	 					errorString = "·IO错误";
	 					break;
	 				case SWFUpload.UPLOAD_ERROR.SECURITY_ERROR:
	 					errorString = "·安全性错误(" + errorMsg + ")";
	 					break;
	 				case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
	 					errorString = "·每次最多上传"+ setting.uploadLimit +"个";
	 					break;
	 				case SWFUpload.UPLOAD_ERROR.UPLOAD_FAILED:
	 					errorString = "·" + errorMsg;
	 					break;
	 				case SWFUpload.UPLOAD_ERROR.SPECIFIED_FILE_ID_NOT_FOUND:
	 					errorString = "·找不到指定文件";
	 					break;
	 				case SWFUpload.UPLOAD_ERROR.FILE_VALIDATION_FAILED:
	 					errorString = "·文件校验失败";
	 					break;
	 				case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
	 					tipText = "取消上传";
	 					errorString = "";
	 					break;
	 				case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
	 					tipText = "上传暂停";
	 					errorString = "";
	 					break;
            	 }
            	 $("#" + file.id).find(".uploadify-progress-bar").css("width", "1px");
            	 if (errorCode != SWFUpload.UPLOAD_ERROR.FILE_CANCELLED && errorCode != SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED) {
            		 $("#" + file.id).addClass("uploadify-error");
            	 }
            	 if (errorCode != SWFUpload.UPLOAD_ERROR.SPECIFIED_FILE_ID_NOT_FOUND && file.status != SWFUpload.FILE_STATUS.COMPLETE) {
            		 $("#" + file.id).find(".filebox-upload-tip").html(tipText + "&nbsp;&nbsp;<span class=\"data\">"+ errorString +"</span>");
            		 $("#" + file.id).find(".data").css("color", "#00F");
            		 $("#" + file.id).find(".filebox-td-opt").html("<a class=\"filebox-row-opt-del-fail\" href=\"javascript:void(0);\">删除</a>");
            	 }
             },
             // 在每一个文件上传成功后触发
             'onUploadSuccess': function (file, data, response){
            	 var result = JSON.parse(data);
            	 if(result.success){
            		 var rowData = {
                			 fileId: file.id,
                			 code: result.code,
                			 text: file.name,
                			 type: file.type,
                			 size: file.size,
                			 statusCode: "1",
                			 statusText: "正常"
                	 };
                	 _addFile(iptTarget, rowData);
                	 if($(iptTarget).length > 0){
                		 var fileStr = "{'code':'" + result.code + "', 'text':'" + file.name + "', statusCode:1, statusText:'正常', type:'" + file.type + "', size:'" + file.size + "'}";
                		 var targerVal = $(iptTarget).val() || "";
                		 targerVal = targerVal.replace("[", "").replace("]","");
                		 if(targerVal == ""){
                			 targerVal = "[" + targerVal + fileStr + "]";
                		 }else{
                			 targerVal = "[" + targerVal + "," + fileStr + "]";
                		 }
                		 $(iptTarget).val(targerVal);
                	 }
            	 }else{
            		 alert(result.customMsg);
            	 }
             },
             // 在队列中的文件上传完成后触发
             'onQueueComplete': function (queueData) {
                 // $('#fileQueue').attr('style', 'visibility :hidden');
             },
             'onCancel': function(file){
            	 $cancelTr = $("#"+file.id);
            	 if($cancelTr.length > 0){
            		 $cancelTr.addClass("filebox-cancel-row");
            		 $cancelTr.css("display","none");
            	 }
            	 if(opts.deplayUpload && this.queueData.queueLength == 0){
            		 $fileBoxTable.find(".filebox-opt-upload-all").unbind("click");
            	 }
            	 _doSort(iptTarget);
             }
		 });
	};
	
	function _upload(instanceID, fileID) {
		var $updtr = $("#" + fileID);
		if($updtr.length > 0){
			$updtr.find(".filebox-upload-tip").html("正在上传&nbsp;&nbsp;<span class=\"data\"></span>");
			$updtr.find(".filebox-td-opt").html("<a class=\"filebox-row-opt-cancle\" href=\"javascript:$(\'#" + instanceID + "\').uploadify(\'cancel\', \'" + fileID + "\');\">取消上传</a>");
			$("#" + instanceID).uploadify("upload",fileID);
		}
	};
	
	function _uploadAll(fileboxId, instanceID) {
		var $fileBoxTable = $("#"+fileboxId);
		if($fileBoxTable.length > 0){
			$fileBoxTable.find(".filebox-no-upload").each(function(){
				var fileId = $(this).data("fileid");
				$(this).find(".filebox-upload-tip").html("正在上传&nbsp;&nbsp;<span class=\"data\"></span>");
				$(this).find(".filebox-td-opt").html("<a class=\"filebox-row-opt-cancle\" href=\"javascript:$(\'#" + instanceID + "\').uploadify(\'cancel\', \'" + fileId + "\');\">取消上传</a>");
			});
			$("#" + instanceID).uploadify("upload","*");
		}
	};
	
	function _initAllBtn(iptTarget){
		var opts = $.data(iptTarget, "datafilebox").options;
		var fileboxId = $.data(iptTarget, "datafilebox").fileboxId;
		var selectFileBtnId = $.data(iptTarget, "datafilebox").selectFileBtnId;
		var $fileBoxTable = $("#"+fileboxId);
		if($fileBoxTable.length > 0) {
			$fileBoxTable.find(".filebox-opt-new").unbind("click").click(function(){
				$(iptTarget).ntkOfficePlugin("newFile");
			});
			
			$fileBoxTable.find(".filebox-row-opt-del").unbind("click").click(function(){
				var $tr = $(this).closest("tr");
				var fileCode = $tr.data("code");
				if(fileCode){
					alert("删除！");
					_doSort(iptTarget);
				}else{
					return;
				}
			});
			
			$fileBoxTable.find(".filebox-row-opt-read").unbind("click").click(function(){
				var $tr = $(this).closest("tr");
				var fileCode = $tr.data("code");
				if(fileCode){
					$(iptTarget).ntkOfficePlugin("readFile");
				}else{
					return;
				}
			});
			
			$fileBoxTable.find(".filebox-row-opt-view").unbind("click").click(function(){
				var $tr = $(this).closest("tr");
				var fileCode = $tr.data("code");
				if(opts.editurl && fileCode){
					var fileUrl = opts.editurl + "/" + fileCode;
					fileDownload._openFile(fileUrl);
				}else{
					return;
				}
			});
			
			$fileBoxTable.find(".filebox-row-opt-edit").unbind("click").click(function(){
				var $tr = $(this).closest("tr");
				var fileCode = $tr.data("code");
				if(fileCode){
					$(iptTarget).ntkOfficePlugin("editFile");
				}else{
					return;
				}
			});
			
			$fileBoxTable.find(".filebox-row-opt-download").unbind("click").click(function(){
				var $tr = $(this).closest("tr");
				var fileCode = $tr.data("code");
				if(opts.editurl && fileCode){
					var fileUrl = opts.editurl + "/" + fileCode;
					if(opts.isThunderDownload) {
						fileDownload._download(fileUrl);
					}else {
						fileDownload._downloadDefault(fileUrl);
					}
				}else{
					return;
				}
			});
		}
	};
	
	function _addFile(iptTarget, rowData) {
		var opts = $.data(iptTarget, "datafilebox").options;
		var fileboxId = $.data(iptTarget, "datafilebox").fileboxId;
		var $fileBoxTable = $("#"+fileboxId);
		
		var rowIndex = 0;
		var rowNum = 1;
		var trHtmlStr = "";
		if($fileBoxTable.length != 0){
			if(typeof rowData == "string"){
				rowData = "rowData=" + rowData;
				try{
					rowData = eval(rowData);
				}catch(e){
					alert("解析文件信息Json失败，文件列表添加行失败！");
				}
			}else{
				rowData = rowData || {};
			}
			if(rowData.text && rowData.type && (rowData.fileId || (rowData.code && rowData.statusText))) {
				if(rowData.fileId && rowData.code) {
					var $proTr = $("#" + rowData.fileId);
					rowIndex = $proTr.data("rowindex");
					rowNum = $proTr.data("rownum");
				}else {
					rowIndex = $fileBoxTable.find("tr.filebox-data-row").length || 0;
					rowNum = rowIndex + 1;
				}

				if(rowData.size){
					var j = Math.round(rowData.size / 1024);
					var o = "KB";
					if (j > 1000) {
						j = Math.round(j / 1000);
						o = "MB";
					}
					var l = j.toString().split(".");
					j = l[0];
					if (l.length > 1) {
						j += "." + l[1].substr(0, 2);
					}
					j += o;
					rowData.size = j;
				}

				if(rowData.code){
					trHtmlStr = "<tr class=\"filebox-data-row";
				}else {
					trHtmlStr = "<tr id=\"" + rowData.fileId + "\" class=\"filebox-data-row filebox-no-upload";
				}

				if (rowIndex === 0) {
					trHtmlStr += " first";
		        } else {
		            if (rowIndex % 2 === 1) {
		            	trHtmlStr += " even";
		            }
		        }

				trHtmlStr += "\" filebox-row-index=\"" + rowIndex + "\"";
				trHtmlStr += " data-rowindex=\"" + rowIndex + "\"";
				trHtmlStr += " data-rownum=\"" + rowNum + "\"";
				for(var itm in rowData){
					var tempStr =" data-" + itm + "=\"" + rowData[itm]+ "\"";
					trHtmlStr += tempStr;
				}
				trHtmlStr += ">";
				trHtmlStr += "<td class=\"filebox-td-rownum\">";
				trHtmlStr += rowNum;
				trHtmlStr += "</td>";
				trHtmlStr += "<td class=\"filebox-td-title";
				if(rowData.code && opts.model.indexOf("g") != -1){
					trHtmlStr += " folebox-title-download\">";
					trHtmlStr += "<a title=\"";
					trHtmlStr += rowData.text || "empty";
					trHtmlStr += "\" class=\"folebox-title\" href=\"javascript:void(0);\"";
					for(var itm in rowData){
						var tempStr =" data-" + itm + "=\"" + rowData[itm]+ "\"";
						trHtmlStr += tempStr;
					}
					trHtmlStr += ">";
					trHtmlStr += rowData.text || "empty";
					trHtmlStr += "</a></td>";
				}else {
					trHtmlStr += "\">";
					trHtmlStr += "<a title=\"";
					trHtmlStr += rowData.text || "empty";
					trHtmlStr += "\" class=\"folebox-title\" href=\"javascript:void(0);\">";
					trHtmlStr += rowData.text || "empty";
					trHtmlStr += "</a></td>";
				}
				if(rowData.code){
					trHtmlStr += "<td class=\"filebox-td-status\">";
					trHtmlStr += rowData.statusText || "empty";
					trHtmlStr += "</td>";
					trHtmlStr += "<td class=\"filebox-td-opt\">";
					if(opts.model.indexOf("d") != -1){
						trHtmlStr += "<a class=\"filebox-row-opt-del\" href=\"javascript:void(0);\">删除</a>";
					}
					if(opts.model.indexOf("r") != -1){
						if(rowData.type && (rowData.type.indexOf("doc") != -1 || rowData.type.indexOf("docx") != -1 || rowData.type.indexOf("xls") != -1 || rowData.type.indexOf("xlsx") != -1 || rowData.type.indexOf("ppt") != -1 || rowData.type.indexOf("pptx") != -1)){
							trHtmlStr += "<a class=\"filebox-row-opt-read\" href=\"javascript:void(0);\">阅读</a>";
						}else{
							trHtmlStr += "<a class=\"filebox-row-opt-view\" href=\"javascript:void(0);\">查看</a>";
						}
					}
					if(opts.model.indexOf("e") != -1){
						if(rowData.type && (rowData.type.indexOf("doc") != -1 || rowData.type.indexOf("docx") != -1 || rowData.type.indexOf("xls") != -1 || rowData.type.indexOf("xlsx") != -1 || rowData.type.indexOf("ppt") != -1 || rowData.type.indexOf("pptx") != -1)){
							trHtmlStr += "<a class=\"filebox-row-opt-edit\" href=\"javascript:void(0);\">编辑</a>";
						}
					}
					if(opts.model.indexOf("g") != -1){
						trHtmlStr += "<a class=\"filebox-row-opt-download\" href=\"javascript:void(0);\">下载</a>";
					}
					trHtmlStr += "</td>";
				}else{
					if(opts.deplayUpload){
						trHtmlStr += "<td class=\"filebox-td-progress\">";
						var uploadProgressStr = "<div class=\"uploadify-progress-bar\">&nbsp;</div><div class=\"filebox-upload-tip\">未上传&nbsp;&nbsp;<span class=\"data\"></span></div>"
						trHtmlStr += uploadProgressStr;
						trHtmlStr += "</td>";
						trHtmlStr += "<td class=\"filebox-td-opt\">";
						trHtmlStr += "<a class=\"filebox-row-opt-upload\" href=\"javascript:void(0);\">上传</a>";
						trHtmlStr += "<a class=\"filebox-row-opt-cancle\" href=\"javascript:void(0);\">删除</a>";
						trHtmlStr += "</td>";
					}else{
						trHtmlStr += "<td class=\"filebox-td-progress\">";
						var uploadProgressStr = "<div class=\"uploadify-progress-bar\">&nbsp;</div><div class=\"filebox-upload-tip\">正在上传&nbsp;&nbsp;<span class=\"data\"></span></div>"
						trHtmlStr += uploadProgressStr;
						trHtmlStr += "</td>";
						trHtmlStr += "<td class=\"filebox-td-opt\">";
						trHtmlStr += "<a class=\"filebox-row-opt-cancle\" href=\"javascript:void(0);\">取消上传</a>";
						trHtmlStr += "</td>";
					}
				}
				trHtmlStr += "</tr>";
				if(rowData.fileId && rowData.code){
					var $proTr = $("#" + rowData.fileId);
					$proTr.after(trHtmlStr);
					$proTr.addClass("filebox-cancel-row");
					$proTr.css("display","none");
				}else{
					$fileBoxTable.append(trHtmlStr);
				}
				
				$fileBoxTable.find(".filebox-no-upload").each(function(){
					var fileId = $(this).data("fileid");
					$(this).find(".filebox-row-opt-upload").click(function(){
						_upload(rowData.instanceId, fileId)
					});
				});
				$fileBoxTable.find(".filebox-no-upload").each(function(){
					var fileId = $(this).data("fileid");
					$(this).find(".filebox-row-opt-cancle").click(function(){
						$("#" + rowData.instanceId).uploadify("cancel", fileId);
					});
				});
				if(opts.deplayUpload){
					$fileBoxTable.find(".filebox-opt-upload-all").click(function(){
						_uploadAll(fileboxId, rowData.instanceId);
					}); 
				}
			}else{
				alert("数据异常，文件列表添加数据失败！");
			}
		}else{
			alert("文件列表组件不存在，添加行操作失败！");
		}
	};
	
	// 后续还要添加ajax后台请求部分
	function _delFile(iptTarget, filecodes) {
		var fileboxId = $.data(iptTarget, "datafilebox").fileboxId;
		var $fileBoxTable = $("#"+fileboxId);
		if($fileBoxTable.length > 0) {
			if(filecodes){
				var fileCodeArry = filecodes.split(",");
				for(var fileCode in fileCodeArry){
					$fileBoxTable.find("tr.filebox-data-row").each(function(){
						if($(this).data("code") && $(this).data("code") == fileCode){
							$(this).remove();
							return false; 
						}
					});
				}
				_doSort(iptTarget);
			}else{
				alert("删除文件Code参数为空，删除文件失败！");
			}
		}else {
			alert("文件列表组件不存在，删除文件失败！");
		}
	};
	
	function _downloadFile(iptTarget, filecode) {
		if(filecodes) {
			var url = "";
			fileDownload._download(url);
		}
	};
	
	function _doSort(iptTarget) {
		var fileboxId = $.data(iptTarget, "datafilebox").fileboxId;
		var $fileBoxTable = $("#"+fileboxId);
		if($fileBoxTable.length != 0){
			$fileBoxTable = $($.find(".filebox-table")[0]);
			$fileBoxTable.find("tr.filebox-data-row").not("tr.filebox-cancel-row").each(function(i){
				var rowNum = i + 1;
				var $it = $(this);
				$it.find("td.filebox-td-rownum").text(rowNum);
				$(this).attr("filebox-row-index", i);
				$it.removeClass("even");
	            if (i === 0) {
	                $it.addClass("first");
	            } else {
	                if (i % 2 === 1) {
	                    $it.addClass("even");
	                }
	            }
			});
		}
		return;
	};
	
	function _existUnupload(iptTarget){
		var result = {
				succeed:true,
				msg:"文件列表中不存在未上传文件！"
		};
		var fileboxId = $.data(iptTarget, "datafilebox").fileboxId;
		var $fileBoxTable = $("#"+fileboxId);
		if($fileBoxTable.find("tr.filebox-no-upload").not("tr.filebox-cancel-row").length > 0){
			result.succeed = false;
			result.msg = "文件列表中有未上传的文件，请先上传或删除！";
		}
		return result;
	};
	
	function _newFile(iptTarget, fileType, opts, target) {
		
	};
	
	function _editFile(iptTarget, fileCode, opts, target) {
		
	};
	
	function _readFile(iptTarget, fileCode, opts) {
		
	};
	
	var fileDownload = {
			_initialActiveXObject: function () {
				var Thunder;
				try {
					Thunder = new ActiveXObject("ThunderAgent.Agent")  
				}catch(e) {
					try {
						Thunder=new ActiveXObject("ThunderServer.webThunder.1");
					}catch(e) {
						try {
							Thunder = new ActiveXObject("ThunderAgent.Agent.1");
						}catch(e) {
							Thunder = null;
						}      
					}    
				}
				return Thunder;
			},
			
			_download: function (url) {
				var Thunder = this._initialActiveXObject();
				if(Thunder == null) {
					this._downloadDefault(url);
					return;
				}  
				try {
					Thunder.AddTask(url,"","","","",1,1,10);
					Thunder.CommitTasks();
				}catch(e) {
					try {
						Thunder.CallAddTask(url,"","",1,"","");      
					}catch(e) {
						this._downloadDefault(url);
					}
				}
			},
			
			_downloadDefault: function (url) {
				var downLoadIframe = document.getElementById("downLoadIframe");
				
				if (!downLoadIframe){
					downLoadIframe = document.createElement('iframe');
					downLoadIframe.id = "downLoadIframe";
				}
				downLoadIframe.src=url;
				downLoadIframe.style.display = "none";
	            document.body.appendChild(downLoadIframe);
			},
			
			_openFile: function(url){
				window.open(url);
			}
	};
})(jQuery);