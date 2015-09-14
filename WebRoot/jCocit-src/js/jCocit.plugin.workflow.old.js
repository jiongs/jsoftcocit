(function($, jCocit) {

	function submitAttention(form) {
		var users = [];
		var data = {};
		data["attentionUsers"] = users;
		form.find("tr").each(function() {
			var val = $(this).attr("value");
			if (val)
				users[users.length] = val;
		});
		var url = form.attr("action");
		jCocit.util.doAjax(url, {}, data);
	}

	jCocit.workflow = {
		getTaskPage : function(btn) {
			var $btn = $(btn);

			$btn.parent().find(".coc-btn").removeClass("coc-btn-selected");
			$btn.addClass("coc-btn-selected");

			var $dialog = $btn.closest(".PnBC");

			var options = $.parseOptions(btn);
			var url = options.opUrl;
			var box;
			if ($dialog.length) {
				box = $(options.pageBox, $dialog);
			} else {
				box = $(options.pageBox);
			}

			box.doLoad({
				type : "POST",
				async : true,
				url : url
			});
		},
		addAttention : function(row) {
			var $btn = $(this);
			var table = $btn.closest("table");
			var oldTr = table.find("tr[value=" + row.value + "]");
			if (oldTr.length == 0) {
				var tr = $("<tr></tr>").attr("value", row.value).appendTo(table);
				$('<td class="coc-fld" style="text-align: center;"></td>').html(row.text + '(' + row.value + ')').appendTo(tr);
				$(
						'<td class="coc-fld" style="text-align: center; width: 40px;"><a href="javascript: void(0)" onclick="jCocit.workflow.delAttetion(this)" class="coc-link">取消关注</a></td>')
						.appendTo(tr);

				submitAttention($btn.closest("form"));
			}
		},
		delAttetion : function(btn) {
			var $btn = $(btn);
			var form = $btn.closest("form");
			$btn.closest("tr").remove();

			submitAttention(form);
		},
		selectNode : function(node) {
			var tree = $(this);
			var treeOptions = tree.tree("options")
			var combo = treeOptions.combo;
			var comboOptions = combo.combo("options");
			var tr = combo.closest("tr");

			var $canFillUp = tr.find(".canFillUp");
			var check = $canFillUp.find("input")
			var nextNodeUrl = check.attr("nextNodeUrl");
			if (node.canFillUp == "true") {
				$canFillUp.show();
				var dot = nextNodeUrl.indexOf("?");
				nextNodeUrl = nextNodeUrl.substring(0, dot) + "?currentNodeCode=" + node.id;
				check.attr("nextNodeUrl", nextNodeUrl);
				check.attr("checked", false);
			} else {
				$canFillUp.hide();
			}

			var nextTr = tr.next("tr");
			while (nextTr.length > 0) {
				nextTr.remove();
				nextTr = tr.next("tr");
			}
		},
		appendNode : function(checkbox) {
			var $check = $(checkbox);
			var table = $check.closest("table");
			var nextNodeUrl = $check.attr("nextNodeUrl");
			var data = {};

			if ($check.is(":checked")) {
				if (nextNodeUrl)
					$.doAjax({
						type : "POST",
						url : nextNodeUrl,
						data : data,
						success : function(responseText, status, jqXHR) {
							if (responseText.trim().length > 0) {
								var tr = $(responseText).appendTo(table);
								jCocit.parseUI(tr);
							}
						}
					});
			} else {
				var tr = $check.closest("tr");
				var nextTr = tr.next("tr");
				while (nextTr.length > 0) {
					nextTr.remove();
					nextTr = tr.next("tr");
				}
			}
		},
		selectActorsFromComboDialog : function(row) {
			var $combo = $(this);
			var val = $combo.combo('getValue');
			var text = $combo.combo('getText');
			var valArray = [];
			var textArray = [];
			if (val.length) {
				var valArray = val.split(",");
				var textArray = text.split(",");
			}
			var existed = false;
			for ( var i = valArray.length - 1; i >= 0; i--) {
				if (valArray[i] == row.value) {
					existed = true;
					break;
				}
			}
			if (!existed) {
				valArray.push(row.value);
				textArray.push(row.text);
				$combo.combo('setValue', valArray.join(","));
				$combo.combo('setText', textArray.join(","));
			}
		},
		selectActor : function(box, event) {
			var $box = $(box);
			var $win = $box.closest(".wdf_node_setting");
			$win.find(".wfd_node_focus").removeClass("wfd_node_focus");
			var node = $box.parent().parent().parent();
			node.addClass("wfd_node_focus");

			if (event.stopPropagation) {
				event.stopPropagation();
			} else if (event.preventDefault) {
				event.preventDefault();
			}
		},
		selectActorFromGrid : function(rowIndex, row) {
			var $win = $(this).closest(".wdf_node_setting");
			var node = $win.find(".wfd_node_focus");
			if (node.length == 0) {
				$.messager.showMsg("请先选中流程任务节点");
			}

			var actor = node.find(".wfd_node_actor");
			if (actor.attr("multiActors") == "true") {
				var $name = node.find(".actorName");
				var $code = node.find(".actorCode");
				var text = $name.val();
				var code = $code.val();
				var codeArray = [];
				var textArray = [];
				if (code.length) {
					codeArray = code.split(",");
					textArray = text.split(",");
				}
				var existed = false;
				for ( var i = codeArray.length - 1; i >= 0; i--) {
					if (codeArray[i] == row.dlyhm) {
						existed = true;
						break;
					}
				}
				if (!existed) {
					codeArray.push(row.dlyhm);
					textArray.push(row.name);
					$name.val(textArray.join(","));
					$code.val(codeArray.join(","));
				}

				node.addClass("wfd_node_actor_existed");
			} else {
				node.find(".actorName").val(row.name);
				node.find(".actorCode").val(row.dlyhm);
				node.addClass("wfd_node_actor_existed");
				node.removeClass("wfd_node_focus");

				var nextnode = node.next(".wfd_node");
				nextnode.addClass("wfd_node_focus");
				nextnode.find(".input").focus();
			}
		},
		/**
		 * 流程办理
		 */
		transact : function(btn, resultUI, data) {
			var form = $(btn.form);
			var rows = [];
			form.find(".wf-task").each(function() {
				var tr = $(this);
				var row = {};

				tr.find("input[name=transactNodeCode]").each(function() {
					row["key"] = $(this).val();
				})

				var userCodes = [];
				tr.find("input[name=transactUserCode]").each(function() {
					userCodes.push($(this).val());
				})
				row["actorKeys"] = userCodes.join(",");

				tr.find("input[name=transactNodeFillup]").each(function() {
					row["fillup"] = $(this).is(":checked");
				});

				tr.find("input[name=transactNodeFillup]").each(function() {
					row["skip"] = $(this).is(":checked");
				});

				rows.push(row);
			})
			data["nodes"] = rows;

			/*
			 * 业务表单
			 */
			var uiContainer = form.closest(".coc-ui-container");
			var bizForms = uiContainer.find(".jCocit-tabs form");
			if (typeof CKEDITOR != "undefined") {
				$(".jCocit-ckeditor", bizForms).each(function() {
					var editor = CKEDITOR.instances[this.id];
					editor.updateElement();
				});
			}

			var queryString = $.param(data);
			queryString += "&" + bizForms.serialize();

			jCocit.util.submitForm(form, resultUI, queryString, function() {
				return true;
			});
		}
	}

})(jQuery, jCocit);