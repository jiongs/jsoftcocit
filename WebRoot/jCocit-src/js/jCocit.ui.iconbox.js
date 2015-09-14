/**
 * 图标切换组件
 */
(function($) {
	$.fn.iconbox = function(options) {
		if(typeof options == "object") {
			options = options || {};
			return this.each(function() {
				var state = $.data(this, "iconboxdata");
				var opts;
				if (state) {
					opts = $.extend(state.options, options, _iconNameAnalysis(this));
					state.options = opts;
				}else {
					opts = $.extend({}, defaultOpts, _parseData(this), options, _iconNameAnalysis(this));
					var extStr = $(this).prop("id") || Math.round(Math.random() * 10000);
					var iconBoxBtnId = "iconbox-btn-" + extStr; 
					var iconBoxPanelId = "iconbox-panel-" + extStr; 
					var iconBoxTableId = "iconbox-table-" + extStr;
					var maxIconNum = opts.boxRows * opts.boxCols;
					if(opts.iconTotalNum > maxIconNum) {
						opts.iconTotalNum = maxIconNum;
					}
					$.data(this, "iconboxdata", {
						iconBoxBtnId : iconBoxBtnId,
						iconBoxPanelId : iconBoxPanelId,
						iconBoxTableId: iconBoxTableId,
						options : opts
					});
				}
				_initIconBox(this);
			});
		}else{
			return this.each(function() {
				var state = $.data(this, "iconboxdata");
				var opts;
				if (!state) {
					opts = $.extend({}, defaultOpts, _parseData(this), _iconNameAnalysis(this));
					var extStr = $(this).prop("id") || Math.round(Math.random() * 10000);
					var iconBoxBtnId = "iconbox-btn-" + extStr; 
					var iconBoxPanelId = "iconbox-panel-" + extStr; 
					var iconBoxTableId = "iconbox-table-" + extStr;
					var maxIconNum = opts.boxRows * opts.boxCols;
					if(opts.iconTotalNum > maxIconNum) {
						opts.iconTotalNum = maxIconNum;
					}
					$.data(this, "iconboxdata", {
						iconBoxBtnId : iconBoxBtnId,
						iconBoxPanelId : iconBoxPanelId,
						iconBoxTableId: iconBoxTableId,
						options : opts
					});
				}
				_initIconBox(this);
			});
		}
	};
	
	var defaultOpts = {
			iconRows:10, // 图片上的图标行数
			iconCols:10, // 图片上的图标列数
			iconTotalNum:100, // 图片中有效图标数
			iconSize:16 // 图片上的图标大小
	};
	
	function _parseData(target) {
		var data= {
		};
		
		data.iconImgPath = $(target).data("iconimgpath") || "";
		if(data.iconImgPath){
			data.imgName = data.iconImgPath.substring(data.iconImgPath.lastIndexOf("/") + 1);
			
			var imgNameStr = data.imgName.replace(/-/g,"_");
			var iconSizeStr;
			if(imgNameStr.lastIndexOf("_") >= 0){
				iconSizeStr = imgNameStr.substring(imgNameStr.lastIndexOf("_") + 1).replace(".png", "");
			}else{
				iconSizeStr = imgNameStr.replace(".png", "");
			}
			if(iconSizeStr && !isNaN(iconSizeStr)){
				data.iconSize = iconSizeStr;
			}
		}
		
		if($(target).data("iconrows")){
			data.iconRows = $(target).data("iconrows");
		}
		if($(target).data("iconcols")){
			data.iconCols = $(target).data("iconcols");
		}
		if($(target).data("icontotalnum")){
			data.iconTotalNum = $(target).data("icontotalnum");
		}
		
		if(data.iconSize && !data.iconRows && !data.iconCols && !data.iconTotalNum){
			if(data.iconSize == 32){
				data.iconRows = 8;
				data.iconCols = 8;
				data.iconTotalNum = 64;
			}
		}
		
		return data;
	};
	
	function _iconNameAnalysis(target) {
		var tagValue = $(target).val() || $(target).data("value");
		var wzStr;
		var icoParam={
				iconX:-1, //当前有效图标横坐标
				iconY:-1 //当前有效图标纵坐标
		};
		if(tagValue && isNaN(tagValue)){
			tagValue = tagValue.trim().replace(/：/g,":");
			if(tagValue.indexOf(":") == 0) {
				icoParam.icoName = "";
				wzStr = tagValue.substring(1);
				if(wzStr){
					wzStr = wzStr.replace(/，/g,",");
					if(wzStr.indexOf(",") > 0){
						var wzArry = wzStr.split(",");
						if(wzArry.length >= 2 && !isNaN(wzArry[0]) && !isNaN(wzArry[1])){
							icoParam.iconX = wzArry[1];
							icoParam.iconY = wzArry[0];
						}
					}
				}
			}else if(tagValue.indexOf(":") > 0) {
				var tabArry = tagValue.split(":");
				icoParam.icoName = tabArry[0];
				if(tabArry[1]){
					wzStr = tabArry[1].replace(/，/g,",");
					if(wzStr.indexOf(",") > 0){
						var wzArry = wzStr.split(",");
						if(wzArry.length >= 2 && !isNaN(wzArry[0]) && !isNaN(wzArry[1])){
							icoParam.iconX = wzArry[1];
							icoParam.iconY = wzArry[0];
						}
					}
				}
			}else{
				icoParam.icoName = tagValue;
			}
		}else if(tagValue && !isNaN(tagValue)){
			icoParam.icoName = tagValue;
		}else{
			icoParam.icoName = "";
		}
		return icoParam;
	};
	
	function _initIconBox(target) {
		var iconBoxBtnId = $.data(target, "iconboxdata").iconBoxBtnId;
		var opts = $.data(target, "iconboxdata").options;
		if(opts.iconImgPath != "" &&  opts.iconSize > 0) {
			var imgPath = opts.iconImgPath;
			var x = 0, y = 0;
			if(opts.imgName == opts.icoName && opts.iconX >= 0 && opts.iconY >= 0){
				x = opts.iconX * opts.iconSize;
				y = opts.iconY * opts.iconSize;
			}
			var position = "-" + y + "px -" + x + "px";
			var $iconBoxBtn = $("<div id=\"" + iconBoxBtnId + "\" class=\"iconBox_btn\"></div>");
			$iconBoxBtn.css("width",opts.iconSize + "px");
			$iconBoxBtn.css("height",opts.iconSize + "px");
			$iconBoxBtn.css("background-image","url(" + imgPath + ")");
			$iconBoxBtn.css("background-position", position);
			$iconBoxBtn.css("background-repeat","no-repeat");
			$(target).after($iconBoxBtn);
			$(target).css("display", "none");
			var editFlag = $(target).data("editflag") || false;
			if(editFlag){
				// 点击页面其他地方，自动关闭div
				$(document).click(function() {
					$(".iconbox-panel").remove();
				});
				
				$iconBoxBtn.bind("click",function(e) {
					_showIconBox(this, target);
					e.stopPropagation(); //阻止事件冒泡
				});
			}
		}else{
			alert("参数错误，图标切换控件加载失败！");
		}
	};
	
	function _showIconBox(btnTarget, target) {
		var iconBoxPanelId = $.data(target, "iconboxdata").iconBoxPanelId;
		var iconBoxTableId = $.data(target, "iconboxdata").iconBoxTableId;
		var opts = $.data(target, "iconboxdata").options;
		var icoParam = _iconNameAnalysis(target);
		opts = $.extend(opts, icoParam);
		
		var panelWidth = opts.iconRows * opts.iconSize + 10;
		var panelHeight = opts.iconCols * opts.iconSize + 10;
		var imgPath = opts.iconImgPath;
		
		$(".iconbox-panel").remove();
		var $iconPanel = $("<div id=\"" +iconBoxPanelId + "\" class=\"iconbox-panel\"></div>").appendTo("body");
		var tableHtml = "<table id=\"" + iconBoxTableId + "\" class=\"iconbox-table\" cellspacing=\"0\" cellpadding=\"0\">"; 
		var curryNum = 0;
		for(var i=0; i < opts.iconCols; i++) {
			var rowIndex = i + 1;
			tableHtml += "<tr class=\"iconbox-row-data\" iconbox-row-index=\"" + rowIndex + "\" data-index=\"" + rowIndex + "\">";
			for(var j=0; j < opts.iconRows; j++) {
				curryNum ++;
				tableHtml += "<td class=\"iconbox-icon-data";
				if(curryNum < opts.iconTotalNum + 1) {
					tableHtml += " enable";
					if(i == opts.iconX && j == opts.iconY) {
						tableHtml += " selected";
					}
				}else{
					tableHtml += " disable";
				}
				tableHtml += "\" data-icon-x=\"" + i + "\" data-icon-y=\"" + j + "\">";
				tableHtml += "</td>";
			}
			tableHtml += "</tr>";
		}
		tableHtml += "</table>"; 
		$iconTab = $(tableHtml);
		$iconPanel.append($iconTab);
		$iconTab.css("background-image","url(" + imgPath + ")");
		
		$iconPanel.find(".iconbox-table .iconbox-row-data > td").each(function() {
			$(this).css("width", opts.iconSize-2 + "px");
			$(this).css("height", opts.iconSize-2 + "px");
			if($(this).hasClass("enable")){
				$(this).click(function() {
					var iconX = $(this).data("icon-x");
					var iconY = $(this).data("icon-y");
					$iconPanel.find(".iconbox-table .iconbox-row-data > td.selected").removeClass("selected");
					$(this).addClass("selected");
					$(btnTarget).css("background-image","url(" + imgPath + ")");
					var x = iconX * opts.iconSize;
					var y = iconY * opts.iconSize;
					var position = "-" + y + "px -" + x + "px";
					$(btnTarget).css("background-position", position);
					var iconName = opts.imgName + ":" + iconX + "," + iconY;
					$(target).val(iconName);
					$(target).data("value", iconName);
					
					_closeIconBox(target);
				});
			}
		});
		
		if($(btnTarget).offset().top > panelHeight && $(btnTarget).offset().top + panelHeight > $(document).height()) {
			var panelTop = $(btnTarget).offset().top - $iconPanel.outerHeight();
			$iconPanel.css("top",panelTop + "px");
		}else{
			var panelTop = $(btnTarget).offset().top + $(btnTarget).outerHeight();
			$iconPanel.css("top",panelTop + "px");
		}
		if($(btnTarget).offset().left > panelWidth && $(btnTarget).offset().left + panelWidth > $(document).width()){
			var panelLeft = $(btnTarget).offset().left - panelWidth + $(btnTarget).width();
			$iconPanel.css("left",panelLeft + "px");
		}else{
			$iconPanel.css("left",$(btnTarget).offset().left + "px");
		}
		
		//为了防止点击div 也关闭div 此处要防止冒泡
		$iconPanel.click(function() {
			return false;
		});
	};
	
	function _closeIconBox(target) {
		var iconBoxPanelId = $.data(target, "iconboxdata").iconBoxPanelId;
		var $panel = $("#"+iconBoxPanelId);
		if($panel.length > 0) {
			$panel.remove();
		}
	}
	
	function _isFileExist(fileUrl) {
		var existFlag = false;
		var xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		xmlhttp.open("GET",fileUrl,false);
		xmlhttp.send();
		if(xmlhttp.readyState==4) { 
			if(xmlhttp.status==200) {
				existFlag = true;
			}
		} 
		return existFlag;
	};
	
	// 扩展String
	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	};
	
	String.prototype.ltrim = function() {
		return this.replace(/(^\s*)/g,"");
	};
	
	String.prototype.rtrim = function() {
		 return this.replace(/(\s*$)/g,"");
	};
	
	String.prototype.endWith = function(str) {
		if(str==null || str=="" || this.length==0 || str.length > this.length) {
			return false;
		}
		if(this.substring(this.length-str.length) == str) {
			return true;
		}else {
			return false;
		}
		return true;
	};

	String.prototype.startWith=function(str) {
		if(str==null || str=="" || this.length==0 || str.length > this.length) {
			return false;
		}
		if(this.substr(0,str.length) == str) {
			return true;
		}else{
			 return false;
		}
		return true;
	};
	
})(jQuery);