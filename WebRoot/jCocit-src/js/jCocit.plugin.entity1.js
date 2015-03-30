(function($, jCocit) {
	var contextPath = jCocit.contextPath, dialogWidth = 800, dialogHeight = 600;
	jCocit.setOptions = function(options) {
		// if (options.contentWidth) {
		// dialogWidth = options.contentWidth - 200;
		// }
		// if (options.contentHeight) {
		// dialogHeight = options.contentHeight - 100;
		// }
		if (options.contextPath) {
			contextPath = options.contextPath;
			jCocit.contextPath = contextPath;
		}
	}
	jCocit.entity = {
		doAction : function(opts) {
			var opCode = opts.opCode;
			// 校验：
			if (opCode == 101) {
				getForm(opts);
			} else if (opCode >= 102 && opCode <= 150) {
				getRowForm(opts, true);
			} else if (opCode >= 151 && opCode <= 190) {
				getRowsForm(opts, true);
			} else if (opCode >= 191 && opCode <= 200) {
				getRowsForm(opts, false);
			} else if (opCode >= 201 && opCode <= 250) {
				switch (opCode) {
				case 241:// insert grid row
					jCocit.entity.addGridRow(opts);
					break;
				case 242:// edit grid row
					jCocit.entity.editGridRow(opts);
					break;
				default:
					doOnRow(opts, true);
				}
			} else if (opCode >= 251 && opCode <= 290) {
				doOnRows(opts, true);
			} else if (opCode >= 291 && opCode <= 300) {
				doOnRows(opts, false);
			}
		},
		doRowAction : function(e, btn) {
			var opts = $.parseOptions(btn);
			opts.eventTarget = e.target;
			var opCode = opts.opCode;
			if (opCode >= 102 && opCode <= 150) {
				getRowForm(opts, false);
			} else if (opCode >= 151 && opCode <= 190) {
				getRowsForm(opts, false);
			} else if (opCode >= 201 && opCode <= 250) {
				switch (opCode) {
				case 241:// insert grid row
					jCocit.entity.addGridRow(btn);
					break;
				case 242:// edit grid row
					jCocit.entity.editGridRow(btn);
					break;
				default:
					doOnRow(opts, false);
				}
			} else if (opCode >= 251 && opCode <= 290) {
				doOnRows(opts, false);
			} else {
				getRowForm(opts, false);
			}
			if (e.stopPropagation) {
				e.stopPropagation();
			} else if (e.preventDefault) {
				e.preventDefault();
			}
		},
		doAddTableRow : function(btn, rowTemplate, listName) {
			var $rowTemplate = $(rowTemplate);
			var $table = $(btn).closest("table");
			var $newRow = $($rowTemplate.val());
			$newRow.appendTo($table);
			var count = parseInt($rowTemplate.attr("count"));
			count++;

			getElementsOf($newRow).each(function() {
				var $this = $(this);
				var name = $this.attr("name");

				$this.attr("name", listName + "[" + count + "]." + name);
			});

			$rowTemplate.attr("count", "" + count);

			jCocit.parseUI($newRow);
		},
		doDelTableRow : function(btn) {
			var $tr = $(btn).closest("tr");
			$tr.remove();
		},
		doSetting : function(opts) {
			Jalert(jCocit.entity.defaults.unsupport);
		},
		doCloseWindow : function(btn, token) {
			jCocit.util.closeWindow(btn, token);
		},
		doSubmitForm : function(btn, resultUI, data, callback) {
			jCocit.util.submitForm(btn, resultUI, data, callback);
		},
		doResetForm : function(btn) {
			jCocit.util.resetForm(btn);
		},
		doSearch : function(btn) {
			var $btn;
			if (btn && btn.tagName) {
				$btn = $(btn);
			} else {
				$btn = $(this);
			}
			if ($btn.hasClass("jCocit-searchbox")) {
				doRefreshUI($btn.searchbox("options"));
			} else {
				var $form = $btn.closest("form");
				var opts = $.parseOptions($form);
				doRefreshUI(opts);
			}
		},
		doSelectFilter : function(node) {
			doRefreshUI($(this).tree("options"));
		},
		doSelectGrid : function(rowIndex, row) {
			doRefreshUI($(this).datagrid("options"));
		},
		doRefreshUI : function(options) {
			doRefreshUI(options);
		},
		doBeforeLoadGrid : function(row, queryParams) {
			var options = $(this).datagrid("options");
			options.editRowIndex = undefined;

			if (queryParams) {
				makeUIParams(options, queryParams);
			} else {
				makeUIParams(options, row);
			}
		},
		doGridHeaderContextMenu : function(e, field) {
			var $grid = $(this);
			var options = $grid.datagrid("options");

			// 查找Grid表头环境菜单
			var contextMenuID = "context_menu_grid_" + options.token;
			var $contextMenu = $("#" + contextMenuID);

			// 创建Grid表头环境菜单
			if ($contextMenu.length == 0) {
				$contextMenu = $('<div id="' + contextMenuID + '"/>').appendTo('body');
				$contextMenu.menu({
					hideOnMouseLeave : true,
					hideOnMouseClick : false,
					onClick : function(item) {
						if (item.iconCls == 'icon-check') {
							$grid.datagrid('hideColumn', item.name);
							$contextMenu.menu('setIcon', {
								target : item.target,
								iconCls : 'icon-uncheck'
							});
						} else {
							$grid.datagrid('showColumn', item.name);
							$contextMenu.menu('setIcon', {
								target : item.target,
								iconCls : 'icon-check'
							});
						}
					}
				});
				var fields = $grid.datagrid('getColumnFields');
				for ( var i = 2; i < fields.length; i++) {
					var field = fields[i];
					var col = $grid.datagrid('getColumnOption', field);
					$contextMenu.menu('append', {
						text : col.title,
						name : field,
						iconCls : 'icon-check'
					});
				}
			}
			$contextMenu.menu('show', {
				left : e.pageX,
				top : e.pageY
			});
			e.preventDefault();
		},
		/**
		 * 切换业务子表Tab项时将调用该方法，如果Tab中的Grid已经存在，则刷新Grid数据。
		 */
		doSelectTabs : function(tabTitle, tabIndex) {
			// 查找选中的子业务表Tab
			var $childTab = $(this).tabs("getSelected");
			// 查找子业务表Grid
			var $childGrid = $(".jCocit-datagrid", $childTab);
			// 刷新子业务表Grid数据
			if ($childGrid.length)
				doRefreshUI($childGrid.datagrid("options"));
		},
		doSelectSystemMenuTree : function(node) {
			if (node.linkURL) {
				var doc = window.parent.document;
				var bodyWindow = doc.getElementById("body");
				if (bodyWindow.openFuncInTab) {
					bodyWindow.openFuncInTab(node.id, node.text, node.linkURL + "/true");
				} else {
					bodyWindow.src = node.linkURL;
				}
			}
		},
		addGridRow : function(btn) {
			var opts;
			if (btn && btn.tagName) {
				opts = $.parseOptions(btn);
			} else {
				opts = btn;
			}
			
			var $grid = $("#" + opts.resultUI[0]);
			var options = $grid.datagrid("options");
			if(typeof options.singleEditable != "undefined" && options.singleEditable == true){
				if (typeof options.editRowIndex == "undefined") {
					var rowIndex = 0;
					$grid.datagrid("insertRow", {
						index : rowIndex,
						row : {}
					});
					$grid.datagrid('selectRow', rowIndex);
					$grid.datagrid('beginEdit', rowIndex);

					var options = $grid.datagrid("options");
					options.editRowIndex = rowIndex;
				}else{
					$.messager.showMsg(jCocit.entity.defaults.promptSave);
				}
			}else{
				var rowIndex = 0;
				$grid.datagrid("insertRow", {
					index : rowIndex,
					row : {}
				});
				$grid.datagrid('selectRow', rowIndex);
				$grid.datagrid('beginEdit', rowIndex);

				var options = $grid.datagrid("options");
				options.editRowIndex = rowIndex;
			}
		},
		editGridRow : function(btn) {
			var opts = $.parseOptions(btn);
			if (btn && btn.tagName) {
				opts = $.parseOptions(btn);
			} else {
				opts = btn;
			}

			var $grid = $("#" + opts.resultUI[0]);

			var tr = $(btn).closest("tr.datagrid-row");

			var rowIndex;
			if (tr.length) {
				rowIndex = tr.attr("datagrid-row-index");
			} else {
				var row = $grid.datagrid("getSelected");
				rowIndex = $grid.datagrid("getRowIndex", row);
			}

			var options = $grid.datagrid("options");
			if (options.editUrl) {
				if(typeof options.singleEditable != "undefined" && options.singleEditable == true){
					if (typeof options.editRowIndex == "undefined") {
						$grid.datagrid('selectRow', rowIndex);
						$grid.datagrid('beginEdit', rowIndex);
					}else{
						$.messager.showMsg(jCocit.entity.defaults.promptSave);
					}
				}else{
					$grid.datagrid('selectRow', rowIndex);
					$grid.datagrid('beginEdit', rowIndex);
				}
			}
		},
		doEditGrid : function(rowIndex, oldRow, changedRow) {
			var $grid = $(this);
			var options = $grid.datagrid("options");

			var row = {};
			var data = {};
			data["entity"] = row;
			for ( var fld in oldRow) {
				row[fld] = oldRow[fld];
			}
			for ( var fld in changedRow) {
				row[fld] = changedRow[fld];
			}

			// alert($.toJsonString(data));

			var url;
			if (row.id) {
				url = options.editUrl + "/" + row.id;
			} else {
				url = options.addUrl + "/0"
			}

			_doAjax(url, {}, data, function(json) {
				if (json.success) {
					$grid.datagrid('reload');
				}
			});
		}
	};
	function getUIFieldValues(options, field) {
		var rows = getUIValues(options);

		if (!field)
			field = "id";

		var ids = [];
		for (i = 0; i < rows.length; i++) {
			ids[ids.length] = rows[i][field];
		}

		return ids;
	}
	function getUIValues(options) {
		var rows = [];
		var resultUI = options.resultUI;
		if (resultUI && resultUI.length > 0) {
			for ( var i = 0; i < resultUI.length; i++) {
				var $ui = $("#" + resultUI[i]);
				if ($ui.hasClass("jCocit-datagrid") || $ui.hasClass("jCocit-treegrid")) {
					rows = $ui.datagrid("getChecked");
					if (rows.length == 0) {
						var row = $ui.datagrid("getSelected");
						if (row) {
							rows[0] = row;
						}
					}
				}
			}
		}
		return rows;
	}
	function getUIParams(options) {
		var resultUI = options.resultUI;
		if (resultUI && resultUI.length > 0) {
			for ( var i = 0; i < resultUI.length; i++) {
				var $ui = $("#" + resultUI[i]);
				if ($ui.hasClass("jCocit-datagrid") || $ui.hasClass("jCocit-treegrid")) {
					return makeUIParams($ui.datagrid("options"));
				}
			}
		}
		return null;
	}
	/**
	 * 根据options中指定的参数UI(paramUI)，生成查询参数。
	 */
	function makeUIParams(options, queryParams) {
		if (typeof queryParams == "undefined" || queryParams == null) {
			queryParams = {};
		}

		var existed = queryParams["_uiToken"];
		if (!existed && options.token) {
			queryParams["_uiToken"] = options.token;
		}

		/*
		 * jsonExpr——JSON表达式语法：{fld1: [val1, val2, ...], fld2: [val3, val4, ...]}
		 */
		var jsonExpr = {};
		/*
		 * sqlExpr——SQL语法：(fld1 eq 2) and (fld2 in 2,3,4)
		 */
		// var sqlExpr = "";
		// var filterJson = {};
		/*
		 * 参数UI：即加载此UI数据时将从哪些UI获取参数？
		 */
		var paramUI = options.paramUI || [];
		var fkTargetFields = options.fkTargetFields || [];
		var fkFields = options.fkFields || [];
		for ( var pIndex = 0; pIndex < paramUI.length; pIndex++) {
			var uiID = "#" + paramUI[pIndex];
			var $ui = $(uiID);

			/*
			 * 创建GRID过滤器
			 */
			if ($ui.hasClass("jCocit-datagrid") || $ui.hasClass("jCocit-treegrid")) {
				var fkField = null;
				if (fkFields.length > pIndex) {
					fkField = fkFields[pIndex];
				} else {
					Jwarn(uiID + " ‘fkField’ not be found!");
					break;
				}

				var fkTargetField = null;
				if (fkTargetFields.length > pIndex) {
					fkTargetField = fkTargetFields[pIndex];
				} else {
					Jwarn(uiID + " ‘fkTargetField’ not be found!");
					break;
				}

				// 获取选中的行
				var rows = $ui.datagrid("getChecked");
				if (rows.length == 0) {
					var row = $ui.datagrid("getSelected");
					if (row) {
						rows.push(row);
					}
				}

				// 获取选中的字段值
				var values = [];
				for (j = 0; j < rows.length; j++) {
					values.push(rows[j][fkTargetField]);
				}

				jsonExpr[fkField] = values;
			}
			/*
			 * 创建TREE过滤器：tree节点ID格式——fldname:fldvalue
			 */
			else if ($ui.hasClass("jCocit-tree")) {
				var treeOptions = $ui.tree("options");
				var nodes = $ui.tree("getChecked");
				if (!treeOptions.checkbox) {
					var node = $ui.tree("getSelected");
					if (node)
						nodes.push(node);
				}

				// 生成导航树过滤表达式。导航树节点ID格式为“字段名:字段值”。
				var nodeID, idx, fld, val, arr;

				for ( var j = 0; j < nodes.length; j++) {
					nodeID = nodes[j].id;
					idx = nodeID.indexOf(":");
					fld = "";
					val = "";
					if (idx > 0) {
						fld = nodeID.substring(0, idx);
						val = nodeID.substring(idx + 1);
					}

					if (fld.length > 0 && val.length > 0) {
						arr = jsonExpr[fld];
						if (!arr) {
							arr = [];
							jsonExpr[fld] = arr;
						}
						arr.push(val);
					}
				}
			}
			/*
			 * 创建 ComboTree 过滤器：tree节点ID格式——fldname:fldvalue
			 */
			else if ($ui.hasClass("jCocit-combotree")) {
				var $ui = $ui.combotree("tree");

				var treeOptions = $ui.tree("options");
				var nodes = $ui.tree("getChecked");
				if (!treeOptions.checkbox) {
					var node = $ui.tree("getSelected");
					if (node)
						nodes.push(node);
				}

				// 生成导航树过滤表达式。导航树节点ID格式为“字段名:字段值”。
				var nodeID, idx, fld, val, arr;

				for ( var j = 0; j < nodes.length; j++) {
					nodeID = nodes[j].id;
					idx = nodeID.indexOf(":");
					fld = "";
					val = "";
					if (idx > 0) {
						fld = nodeID.substring(0, idx);
						val = nodeID.substring(idx + 1);
					}

					if (fld.length > 0 && val.length > 0) {
						arr = jsonExpr[fld];
						if (!arr) {
							arr = [];
							jsonExpr[fld] = arr;
						}
						arr.push(val);
					}
				}
			}
			/*
			 * 关键字查询框
			 */
			else if ($ui.hasClass("jCocit-searchbox")) {
				var field = $ui.searchbox("getName");// 字段名
				var value = $ui.searchbox("getValue");// 字段值
				// 生成关键字查询表达式
				if (field != null && field.trim().length > 0) {
					jsonExpr[field] = value;
				} else {
					jsonExpr["-keywords-"] = value;
				}
			}
			/*
			 * 查询表单
			 */
			else if ($ui.hasClass("jCocit-searchform")) {
				var array = serializeArray($ui);
				$.each(array, function() {
					if (!jsonExpr[this.name]) {
						jsonExpr[this.name] = [];
					}
					jsonExpr[this.name].push(this.value);
				});
			}

		}

		queryParams["query.jsonExpr"] = $.toJsonString(jsonExpr);
		// queryParams["query.filterExpr"] = $.toJsonString(filterJson);

		// alert(queryParams["query.jsonExpr"]);

		return queryParams;
	}
	function doRefreshUI(options) {
		var resultUI = options.resultUI;
		if (resultUI && resultUI.length > 0) {
			for ( var i = 0; i < resultUI.length; i++) {
				var $ui = $("#" + resultUI[i]);
				if ($ui.hasClass("jCocit-datagrid")) {
					$ui.datagrid("reload");

					var uiOpts = $ui.datagrid("options");
					if (uiOpts.entityKey) {
						doExpiredEntityUI(resultUI[i], uiOpts.entityKey);
					}
				} else if ($ui.hasClass("jCocit-treegrid")) {
					$ui.treegrid("reload");

					var uiOpts = $ui.datagrid("options");
					if (uiOpts.entityKey) {
						doExpiredEntityUI(resultUI[i], uiOpts.entityKey);
					}
				}
			}
		}
	}
	/**
	 * UI刷新后，与UI相同的所有实体UI都要刷新。
	 */
	function doExpiredEntityUI(targetUI, entityKey) {
		var $entityUI = $(".entity-" + entityKey);
		$entityUI.each(function() {
			var $ui = $(this);
			if ($ui.attr("id") != targetUI) {
				if ($ui.hasClass("jCocit-datagrid")) {
					$ui.datagrid("options").expired = true;
				} else if ($ui.hasClass("jCocit-treegrid")) {
					$ui.treegrid("options").expired = true;
				}
			}
		});
	}
	function doSubmitForm(submitUrl, $form, data, funcSuccess, funcComplete) {
		if (!submitUrl || submitUrl.length == 0) {
			return;
		}
		if (typeof CKEDITOR != "undefined") {
			$(".jCocit-ckeditor", $form).each(function() {
				var editor = CKEDITOR.instances[this.id];
				editor.updateElement();
			});
		}
		/*
		 * 序列化 JS BEAN
		 */
		var formdata = {};
		var $bean = $(".bean", $form);
		if ($bean.length > 0) {
			var beanName = $bean.attr("beanName");
			if (beanName && beanName.length > 1) {
				if ($bean.length > 1) {
					formdata[beanName] = [];
					$bean.each(function() {
						formdata[beanName].push(serializeJson($(this)));
					})
				} else {
					formdata[beanName] = serializeJson($bean);
				}
				formdata = jQuery.param(formdata);
			} else {
				formdata = $form.serialize();
			}
		} else {
			formdata = $form.serialize();
		}
		var params = jQuery.param(data);
		/*
		 *
		 */
		var griddataarray =  new Array();
		$form.find(".jCocit-datagrid").each(function(){
			$grid = $(this);
			var gridname = $grid.attr("name");
			var options = $grid.datagrid("options");
			var defaultFieldValues = options.defaultFieldValues;
			if(typeof gridname != "undefined" && gridname != null && gridname.length > 0){
				var changedrows = $grid.datagrid("getChangeDatas");
				if(changedrows.length > 0){
					var _datastr = "";
					var _dataarray = [];
					for(var i=0; i < changedrows.length; i++){
						if(typeof defaultFieldValues == "object"){
							for(var def in defaultFieldValues){
								if(typeof changedrows[i][def] == "undefined"){
									changedrows[i][def] = defaultFieldValues[def];
								}
							}
						}
						for(var fn in changedrows[i]){
							if("_row_buttons_" !== fn){
								if(typeof changedrows[i][fn] == "object"){
									_datastr = gridname + "[" + i + "]." + fn + "=" +encodeURI(changedrows[i][fn].value);
								}else{
									_datastr = gridname + "[" + i + "]." + fn + "=" +encodeURI(changedrows[i][fn]);
								}
								_dataarray.push(_datastr);
							}
							_datastr = "";
						}
					}
					var gridstr = _dataarray.join("&");
					griddataarray.push(gridstr);
				}
			}
		});
		var griddatastr = griddataarray.join("&");
		/*
		 * 提交数据
		 */
		$.doAjax({
			type : "POST",
			dataType : "json",
			url : submitUrl,
			data : formdata + "&" + griddatastr + "&" + params,
			success : funcSuccess,
			complete : funcComplete
		});
	}
	function _getForm(url, opts, data) {
		if (!data) {
			data = {};
		}
		var idx = url.indexOf("?");
		if (opts.opUrlTarget && opts.opUrlTarget.length > 0) {
			var $target = $(opts.eventTarget);
			var $tabs = $target.closest(".jCocit-tabs");
			if ($tabs.length > 0 && opts.opUrlTarget == "tabs") {
				if (idx > 0) {
					url += "&";
				} else {
					url += "/true?";// isAjax=true
				}
				var title = opts.title || opts.text || opts.name;
				var tab = $tabs.tabs("getTab", title);
				if (!tab) {
					$tabs.tabs("add", {
						url : url + "_uiToken=" + opts.token + "&_resultUI=" + (opts.resultUI || []).join(",") + "&" + $.param(data),
						cache : false,
						closable : true,
						title : title
					});
				} else {
					$tabs.tabs("select", title);
				}
			} else {
				if (idx > 0) {
					url += "&";
				} else {
					url += "?";
				}
				window.open(url + "_uiToken=" + opts.token + "&_resultUI=" + (opts.resultUI || []).join(",") + "&" + $.param(data), opts.opUrlTarget).focus();
			}
		} else {
			if (idx > 0) {
				url += "&";
			} else {
				url += "/true?";// isAjax=true
			}
			var w = opts.windowWidth || dialogWidth;
			var h = opts.windowHeight || dialogHeight;
			$.doAjax({
				type : "POST",
				url : url + "_uiToken=" + opts.token + "&_resultUI=" + (opts.resultUI || []).join(",") + "&_uiWidth=" + w + "&_uiHeight=" + h,
				data : data,
				success : function(responseText, status, jqXHR) {
					var buttons = $(".dialog-buttons", $(responseText)).remove();
					var toolbar = $(".dialog-toolbar", $(responseText)).remove();
					jCocit.dialog.open(null, "dialog_" + opts.token + "_" + opts.opCode, {
						title : opts.title || opts.text || opts.name,
						width : w,
						height : h,
						logoCls : opts.iconCls || 'icon-logo',
						modal : true,
						shadow : false,
						maxable : false,
						draggable : true,
						content : responseText,
						buttons : buttons.html(),
						toolbar : toolbar.html()
					});

				},
				error : function(jqXHR, statusText, responseError) {
					Jerror(responseError);
				}
			});
		}
	}
	function makeUrl(urlExpr, rows) {
		var url = "";
		var from = urlExpr.indexOf("${");
		while (from > -1) {
			var to = urlExpr.indexOf("}");
			if (to < from) {
				break;
			}
			var field = urlExpr.substring(from + 2, to);
			var value = "";
			for ( var i = 0; i < rows.length; i++) {
				var id = rows[i][field];
				if (typeof id == "undefined") {
					throw "Field '" + field + "' not be found!";
				} else {
					value = "," + id;
				}
			}
			if (value.length == 0) {
				throw "Field '" + field + "' not be found!";
			}
			value = value.substring(1);
			url = urlExpr.substring(0, from) + value;
			urlExpr = urlExpr.substring(to + 1);
		}
		url = url + urlExpr;

		return url;
	}
	function getForm(opts) {
		var url;
		if (opts.urlExpr) {
			try {
				url = makeUrl(opts.urlExpr, getUIValues(opts));
			} catch (e) {
				Jwarn(e);
			}
		} else if (opts.opUrl) {
			url = opts.opUrl + "/0";
		} else {
			return;
		}

		_getForm(url, opts, getUIParams(opts));
	}
	function getRowForm(opts, rowRequired) {
		var url;
		if (opts.urlExpr) {
			try {
				var rows = getUIValues(opts);

				if (rowRequired) {
					if (rows.length == 0) {
						Jwarn(jCocit.entity.defaults.unselectedOne);
						return;
					} else if (rows.length > 1) {
						Jwarn(jCocit.entity.defaults.onlySelectedOne);
						return;
					}
				}

				url = makeUrl(opts.urlExpr, rows);

			} catch (e) {
				Jwarn(e);
			}
		} else if (opts.opUrl) {
			url = opts.opUrl;
			var rows = getUIFieldValues(opts);

			if (rowRequired) {
				if (rows.length == 0) {
					Jwarn(jCocit.entity.defaults.unselectedOne);
					return;
				} else if (rows.length > 1) {
					Jwarn(jCocit.entity.defaults.onlySelectedOne);
					return;
				}
			}

			url = url + "/" + rows.join(",");
		} else {
			return;
		}

		_getForm(url, opts, getUIParams(opts));
	}
	function getRowsForm(opts, rowsRequired) {
		var url;
		if (opts.urlExpr) {
			try {
				var rows = getUIValues(opts);

				if (rowRequired && rows.length == 0) {
					Jwarn(jCocit.entity.defaults.unselectedAny);
					return;
				}

				url = makeUrl(opts.urlExpr, rows);

			} catch (e) {
				Jwarn(e);
			}
		} else if (opts.opUrl) {
			url = opts.opUrl;
			var rows = getUIFieldValues(opts);

			if (rowsRequired && rows.length == 0) {
				Jwarn(jCocit.entity.defaults.unselectedAny);
				return;
			}

			url = url + "/" + rows.join(",");
		} else {
			return;
		}

		_getForm(url, opts, getUIParams(opts));
	}
	function _doAjax(url, opts, data, success) {
		$.doAjax({
			type : "POST",
			dataType : "json",
			url : url,
			data : data,
			success : function(json) {
				var msg = json.message || opts.successMessage;
				if (msg) {
					$.messager.showMsg(msg);
				}
				doRefreshUI(opts);
				if (success) {
					success(json);
				}
			},
			complete : function() {
			}
		});
	}
	function doOnRow(opts, rowRequired) {
		var url;
		if (opts.urlExpr) {
			try {
				var rows = getUIValues(opts);

				if (rowRequired) {
					if (rows.length == 0) {
						Jwarn(jCocit.entity.defaults.unselectedOne);
						return;
					} else if (rows.length > 1) {
						Jwarn(jCocit.entity.defaults.onlySelectedOne);
						return;
					}
				}

				url = makeUrl(opts.urlExpr, rows);

			} catch (e) {
				Jwarn(e);
			}
		} else if (opts.opUrl) {
			url = opts.opUrl;
			var rows = getUIFieldValues(opts);

			if (rowRequired) {
				if (rows.length == 0) {
					Jwarn(jCocit.entity.defaults.unselectedOne);
					return;
				} else if (rows.length > 1) {
					Jwarn(jCocit.entity.defaults.onlySelectedOne);
					return;
				}
			}

			url = url + "/" + rows.join(",");
		} else {
			return;
		}

		if (opts.warnMessage) {
			Jconfirm(opts.warnMessage, "", function(ok) {
				if (ok) {
					_doAjax(url, opts, getUIParams(opts));
				}
			});
		} else {
			_doAjax(url, opts, getUIParams(opts));
		}
	}
	function doOnRows(opts, rowsRequired) {
		var url;
		if (opts.urlExpr) {
			try {
				var rows = getUIValues(opts);

				if (rowRequired && rows.length == 0) {
					Jwarn(jCocit.entity.defaults.unselectedAny);
					return;
				}

				url = makeUrl(opts.urlExpr, rows);

			} catch (e) {
				Jwarn(e);
			}
		} else if (opts.opUrl) {
			url = opts.opUrl;
			var rows = getUIFieldValues(opts);

			if (rowsRequired && rows.length == 0) {
				Jwarn(jCocit.entity.defaults.unselectedAny);
				return;
			}

			url = url + "/" + rows.join(",");
		} else {
			return;
		}

		var msg = opts.warnMessage;
		if (!msg && (opts.opCode == 269 || opts.opCode == 263)) {
			msg = jCocit.entity.defaults.deleteMessage;
		}
		if (msg) {
			Jconfirm(msg, "", function(ok) {
				if (ok) {
					_doAjax(url, opts, getUIParams(opts));
				}
			});
		} else {
			_doAjax(url, opts, getUIParams(opts));
		}
	}

	jCocit.entity.defaults = {
		confirm : "Confirm",
		cancel : "Cancel",
		unsupport : "Not Support!",
		deleteMessage : "Are you sure delete the selected data?",
		warnMessage : "Are you sure execute the operation?",
		unselectedAny : "Please select one record at least",
		onlySelectedOne : "Onle select one record",
		promptSave : "Please first save editing data"
	};

	/**
	 * 不在使用
	 */
	jCocit.AdminConsole = {
		closeAllTabs : function(tabsID) {
			jCocit.util.closeAllTabs(tabsID);
		},
		reloadTab : function(btn, tabTitle) {
			jCocit.util.reloadTab(btn, tabTitle);
		},
		closeTab : function(btn, tabTitle) {
			jCocit.util.closeTab(btn, tabTitle);
		}
	};
	jCocit.util = {
		doClickCellForAuth : function(rowIndex, field, fieldValue, td, target) {
			var $grid = $(this);

			if (target.is("input")) {
				$grid.datagrid("checkRow", rowIndex);
			}

			switch (field) {
			case 'actionsAuthUrl':
				if (target.hasClass("CkAll")) {

					if (!target.hasClass("Ck1")) {
						target.removeClass("Ck2").addClass("Ck1");
						target.closest("fieldset").find(".CkOne").removeClass("Ck2").addClass("Ck1");
					} else {
						target.removeClass("Ck1");
						target.closest("fieldset").find(".CkOne").removeClass("Ck1");
					}

					// $grid.datagrid("checkRow", rowIndex);
				} else if (target.hasClass("CkOne")) {

					if (!target.hasClass("Ck1")) {
						target.addClass("Ck1");

						$grid.datagrid("checkRow", rowIndex, false, true);
					} else {
						target.removeClass("Ck1");
					}

					var ck = target.closest("fieldset").find(".CkList .Ck");
					var ck1 = target.closest("fieldset").find(".CkList .Ck1");

					var ckAll = target.closest("fieldset").find(".CkAll");
					if (ck1.length == ck.length) {
						ckAll.removeClass("Ck2").addClass("Ck1");
					} else if (ck1.length == 0) {
						ckAll.removeClass("Ck1").removeClass("Ck2");
					} else {
						ckAll.removeClass("Ck1").addClass("Ck2");
					}
				}

				break;
			case 'menuName':
				break;
			}
		},
		doCheckForAuth : function(rowIndex, row, tr) {
			var subtree = tr.next();

			tr.find(".Ck").addClass("Ck1").removeClass("Ck2");
			if (subtree.hasClass("treegrid-tr-tree")) {
				subtree.find(".Ck").addClass("Ck1").removeClass("Ck2");// subtree
			}

			// clear combo values
			tr.find(".jCocit-combotree").combotree("clear");
			if (subtree.hasClass("treegrid-tr-tree")) {
				subtree.find(".jCocit-combotree").combotree("clear");// subtree
			}
		},
		doUncheckForAuth : function(rowIndex, row, tr) {
			var subtree = tr.next();

			tr.find(".Ck").removeClass("Ck1").removeClass("Ck2");
			if (subtree.hasClass("treegrid-tr-tree")) {
				subtree.find(".Ck").removeClass("Ck1").removeClass("Ck2");// subtree
			}

			// clear combo values
			tr.find(".jCocit-combotree").combotree("clear");
			if (subtree.hasClass("treegrid-tr-tree")) {
				subtree.find(".jCocit-combotree").combotree("clear");// subtree
			}
		},
		doSubmitGrid : function(btn, gridSelector, data, callback) {
			var opts = $.parseOptions(btn);

			var $grid = $(gridSelector);
			var columnOptions = $grid.datagrid("getColumnOptions");
			var checkedRows = $grid.datagrid("getChecked");
			var panel = $grid.datagrid("getPanel");
			var gridName = $grid.attr("name");

			var dataRows = [];
			for (i = 0; i < checkedRows.length; i++) {
				var row = checkedRows[i];
				var dataRow = {};

				var tr = $f("tr[node-id='" + row.id + "']", panel);
				for (j = 0; j < columnOptions.length; j++) {
					var colOption = columnOptions[j];
					if (!colOption.name) {
						continue;
					}

					var td = tr.find("td[field='" + colOption.field + "']");
					if ($(".Ck", td).length > 0) {
						var cellValue = [];
						$(".Ck1", td).each(function() {
							var val = $(this).attr("val");
							if (val)
								cellValue.push(val);
						});

						dataRow[colOption.name] = cellValue.join('|');
					} else {
						var inputs = $("input", td);
						if (inputs.length > 0) {
							var fldvalues = {};

							var fldval, idx, fld, val, arr, input;
							for ( var k = 0; k < inputs.length; k++) {
								input = inputs[k];
								fldval = input.value;

								fld = input.name;
								val = fldval;

								idx = fldval.indexOf(":");
								if (idx > 0) {
									fld = fldval.substring(0, idx);
									val = fldval.substring(idx + 1);
								}

								if (fld.length > 0 && val.length > 0) {
									arr = fldvalues[fld];
									if (!arr) {
										arr = [];
										fldvalues[fld] = arr;
									}
									arr.push(val);
								}
							}

							for (fld in fldvalues) {
								val = fldvalues[fld];
								dataRow[fld] = val.join('|');
							}

						} else {
							dataRow[colOption.name] = row[colOption.field];
						}
					}
				}

				dataRows.push(dataRow);
			}

			var json = {};
			json[gridName] = dataRows;

			// alert($.toJsonString(json));

			var params = jQuery.param(json);
			if (params.length == 0) {
				Jerror("请选择要授权的菜单！");
				return;
			}

			var $btn = $(btn).attr("disabled", true);
			$.doAjax({
				type : "POST",
				dataType : "json",
				url : opts.opUrl,
				data : json,
				success : function(json) {
					if (json.success) {
						$.messager.showMsg(json.message);
					}
				},
				complete : function() {
					$btn.attr("disabled", false);
				}
			});
		},
		closeAllTabs : function(tabsID) {
			var $tabs = $(tabsID);
			var tabs = $tabs.tabs("tabs");
			var tab;
			for (i = tabs.length - 1; i >= 0; i--) {
				tab = tabs[i];
				var opts = tab.panel("options");
				if (opts.closable) {
					$tabs.tabs("close", opts.title);
				}
			}
		},
		reloadTab : function(btn, tabTitle) {
			var $btn = $(btn);
			var $tabs = $btn.closest(".jCocit-tabs");
			if (tabTitle) {
				var $tab = $tabs.tabs("getTab", tabTitle);
				$tab.panel("refresh");
			} else {
				var $tab = $btn.closest(".PnBC");
				$tab.panel("refresh");
			}
		},
		closeTab : function(btn, tabTitle) {
			var $btn = $(btn);
			var $tabs = $btn.closest(".jCocit-tabs");
			if (tabTitle) {
				$tabs.tabs("close", tabTitle);
			} else {
				var $tab = $btn.closest(".PnBC");
				var opts = $tab.panel("options");
				if (opts.tab) {
					$tabs.tabs("close", opts.title);
				}
			}
		},
		openTab : function(btn, title, linkUrl) {
			var $tabs = $(btn);
			if (!$tabs.hasClass("jCocit-tabs"))
				$tabs = $tabs.closest(".jCocit-tabs");
			if ($tabs.length > 1) {
				$tabs = $($tabs.get(0));
			}
			var tab = $tabs.tabs("getTab", title);
			if (!tab) {
				if (linkUrl && linkUrl.trim().length > 0) {
					$tabs.tabs("add", {
						url : linkUrl + "/true",
						cache : true,
						closable : true,
						title : title
					});
				}
			} else {
				$tabs.tabs("select", title);
			}
		},
		closeWindow : function(btn, token) {
			if (window.opener) {
				window.close();
			} else {
				var dialog = $(btn).closest(".Wd");
				if (dialog.length > 0) {
					$(".PnBC:eq(0)", dialog).dialog("close");
				} else {
					jCocit.util.closeTab(btn);
				}
			}
		},
		getForm : function(btn) {
			if (btn.form) {
				return btn.form;
			}

			var win = $(btn).closest(".Wd");
			if (win.length > 0) {
				var $form = win.find("form");
				if ($form.length > 0) {
					return $form.get(0);
				}
			}

			return null;
		},
		submitForm : function(btn, resultUI, data, callback) {
			var $btn = $(btn).attr("disabled", true);
			var opts = $.parseOptions(btn);
			if ($.type(data) == "function") {
				callback = data;
				data = {};
			}

			var $body = $btn.closest(".WdB");
			var form;
			if (btn.form) {
				form = btn.form;
			} else {
				form = $("form", $body);
				if (form.length > 0) {
					form = form.get(0);
				}
			}

			var url;
			if (opts.opUrl) {
				url = opts.opUrl;
			} else if (form) {
				url = form.action;
			}

			if (!resultUI) {
				resultUI = opts.resultUI || [];
			}

			if (url) {
				doSubmitForm(url, $(form), data || {}, function(json) {
					if (json.success) {
						if (window.opener) {
							window.opener.jCocit.entity.doRefreshUI({
								resultUI : resultUI
							});
							if (!callback || callback(json)) {
								window.close();
							}
						} else {
							jCocit.entity.doRefreshUI({
								resultUI : resultUI
							});
							if (!callback || callback(json)) {
								var dialog = $btn.closest(".Wd");
								if (dialog.length > 0) {
									$(".PnBC", dialog).dialog('close');
								} else {
									Jsuccess(json.message);
								}
							}
						}
					}
				}, function() {
					$btn.attr("disabled", false);
				});
			}
		},
		resetForm : function(btn) {
			var $btn;
			if (btn && btn.tagName) {
				$btn = $(btn);
			} else {
				$btn = $(this);
			}
			var $form = $btn.closest("form");
			if ($form.length > 0) {
				$form.get(0).reset();
				$(".jCocit-combotree", $form).each(function() {
					$(this).combotree("reset");
				});
				var opts = $.parseOptions($form);
				doRefreshUI(opts);
			}
		}

	};
})(jQuery, jCocit);
