/**
 * <p>
 * <B></B>
 * <UL>
 * <LI>dc: datagrid config, contains view/view1/view2/header1/header2/body1/body2/footer1/footer2
 * </UL>
 * 
 * <pre>
 * 	[div class='panel datagrid']
 * 		[div class='panel-header'][/div]
 * 		[div class='panel-body']
 * 			[div class='datagrid-wrap panel-content']
 * 				[div class='datagrid-toolbar'][/div]
 * 				[div class='datagrid-view']
 * 					[style type='text/css']
 * 						div.datagrid-cell-c1-field1 {width:72px;}
 * 						div.datagrid-cell-c1-field2 {width:82px;}
 * 						...
 * 						div.datagrid-cell-c1-fieldn {width:52px;}
 * 					[/style]
 * 					[div class='datagrid-view1']
 * 						[div class='datagrid-header'][div class='datagrid-header-inner'][/div][/div]
 * 						[div class='datagrid-body'][div class='datagrid-body-inner'][/div][/div]
 * 						[div class='datagrid-footer'][div class='datagrid-footer-inner'][/div][/div]
 * 					[/div]
 * 					[div class='datagrid-view2']
 * 						[div class='datagrid-header'][div class='datagrid-header-inner'][/div][/div]
 * 						[div class='datagrid-body'][/div]
 * 						[div class='datagrid-footer'][div class='datagrid-footer-inner'][/div][/div]
 * 					[/div]
 * 				[/div]
 * 				[div class='datagrid-pager pagination'][/div]
 * 			[/div]
 * 		[/div]
 * 	[/div]
 * </pre>
 * <pre>
 * 	[div class='datagrid-view1']
 * 		[div class='datagrid-header'][div class='datagrid-header-inner']
 * 			[table class='datagrid-htable']
 * 				[tr class='datagrid-header-row']
 * 					[td rowspan='0'][div class='datagrid-header-rownumber'][/div][/td]
 * 					[td field='field1']
 * 						[div class='datagrid-cell'][span]Column Title[/span][span class='datagrid-sort-icon'][/span][/div]
 * 					[/td]
 * 					...
 * 					[td field='fieldn'][/td]
 * 				[/tr]
 * 			[/table]
 * 		[/div][/div]
 * 		[div class='datagrid-body'][div class='datagrid-body-inner']
 * 			[table class='datagrid-btable datagrid-btable-frozen']
 * 				...
 * 			[/table]
 * 			[table class='datagrid-btable']
 * 				[tr id='datagrid-row-r1-1-0' class='datagrid-row' datagrid-row-index='0']
 * 					[td class='datagrid-td-rownumber'][div class='datagrid-cell-rownumber'] 1 [/div][/td]
 * 					[td field='field1']
 * 						[div class='datagrid-cell datagrid-cell-c1-field1']...[/div]
 * 	 				[/td]
 * 					...
 * 					[td field='fieldn'][/td]
 * 				[/tr]
 * 				[tr id='datagrid-row-r1-1-1' class='datagrid-row' datagrid-row-index='1']
 * 					[td class='datagrid-td-rownumber'][div class='datagrid-cell-rownumber'] 2 [/div][/td]
 * 				[/tr]
 * 				...
 * 				[tr id='datagrid-row-r1-1-n' class='datagrid-row' datagrid-row-index='n']
 * 					[td class='datagrid-td-rownumber'][div class='datagrid-cell-rownumber'] n+1 [/div][/td]
 * 				[/tr]
 * 			[/table]
 * 		[/div][/div]
 * 		[div class='datagrid-footer'][div class='datagrid-footer-inner']
 * 			[table class='datagrid-ftable']
 * 				...
 * 			[/table]
 * 		[/div][/div]
 * 	[/div]
 * </pre>
 * <pre>
 * 	[div class='datagrid-view2']
 * 		[div class='datagrid-header'][div class='datagrid-header-inner']
 * 			[table class='datagrid-htable']
 * 				[tr class='datagrid-header-row']
 * 					[td field='field1']
 * 						[div class='datagrid-cell'][span]Column1 Title[/span][span class='datagrid-sort-icon'][/span][/div]
 * 					[/td]
 * 					...
 * 					[td field='fieldn'][/td]
 * 				[/tr]
 * 			[/table]
 * 		[/div][/div]
 * 		[div class='datagrid-body']
 * 			[table class='datagrid-btable datagrid-btable-frozen']
 * 				...
 * 			[/table]
 * 			[table class='datagrid-btable']
 * 				[tr id='datagrid-row-r1-2-0' class='datagrid-row' datagrid-row-index='0']
 * 					[td field='field1'][div class='datagrid-cell datagrid-cell-c1-field1']...[/div][/td]
 * 					...
 * 					[td field='fieldn'][div class='datagrid-cell datagrid-cell-c1-fieldn']...[/div][/td]
 * 				[/tr]
 * 				[tr id='datagrid-row-r1-2-1' class='datagrid-row' datagrid-row-index='1'][/tr]
 * 				...
 * 				[tr id='datagrid-row-r1-2-n' class='datagrid-row' datagrid-row-index='9'][/tr]
 * 			[/table]
 * 		[/div]
 * 		[div class='datagrid-footer'][div class='datagrid-footer-inner']
 * 			[table class='datagrid-ftable']
 * 				...
 * 			[/table]
 * 		[/div][/div]
 * 	[/div]
 * </pre>
 * <pre>
 * 	[div class='datagrid-pager pagination']
 * 		[table][/table]
 * 		[div class='pagination-info']Displaying 1 to 10 of 800 items[/div]
 * 		[div style='clear:both;'][/div]
 * 	[/div]
 * </pre>
 * 
 * <p>
 * <b></b>
 * <UL>
 * <LI>panel:
 * <LI>panel header:
 * <LI>panel body: it is grid wrap, contains grid-toolbar/grid-view/grid-pager
 * <LI>
 * <LI>toolbar: it is grid toolbar.
 * <LI>
 * <LI>view: it is grid view, contains header/body/footer.
 * <LI>view header: it is header columns of view, contains view1 header and view2 header.
 * <LI>view body: it is body data rows of view, contains view1 body and view2 body.
 * <LI>view footer: it is footer data rows of view, contains view1 footer and view2 footer.
 * <LI>view1: it is left view, contains left header/body/footer.
 * <LI>view2: it is right view, contains right header/body/footer.
 * <LI>view1 header: it is view left header.
 * <LI>view2 header: it is view right header.
 * <LI>view1 body: it is view left body data rows, contains frozen-body/body.
 * <LI>view2 body: it is view right body data rows, contains frozen-body/body.
 * <LI>view1 footer: it is view left footer.
 * <LI>view2 footer: it is view right footer.
 * <LI>
 * <LI>pager: it is grid pager.
 * </UL>
 */
(function($, jCocit) {

	// var validatebox = $.fn.validatebox || false;
	var gridCount = 0;

	function _indexOfRow(rows, row) {
		for ( var i = 0, len = rows.length; i < len; i++) {
			if (rows[i] == row) {
				return i;
			}
		}
		return -1;
	}

	function _removeRow(rows, idField, row) {
		if (typeof idField == "string") {
			for ( var i = 0, len = rows.length; i < len; i++) {
				if (rows[i][idField] == row) {
					rows.splice(i, 1);
					return;
				}
			}
		} else {
			var index = _indexOfRow(rows, idField);
			if (index != -1) {
				rows.splice(index, 1);
			}
		}
	}

	function _addRow(rows, idField, row) {
		for ( var i = 0, len = rows.length; i < len; i++) {
			if (rows[i][idField] == row[idField]) {
				return;
			}
		}
		rows.push(row);
	}

	function resizePanel(gridTable, size) {
		var opts = $d(gridTable, "datagrid").options;
		var $panelContent = $d(gridTable, "datagrid").panel;
		if (size) {
			if (size.width) {
				opts.width = size.width;
			}
			if (size.height) {
				opts.height = size.height;
			}
		}
		if (opts.fit == true) {
			var p = $p($panelContent.panel("panel"));
			opts.width = $w(p);
			opts.height = $h(p);
		}

		$panelContent.panel("resize", {
			width : opts.width,
			height : opts.height
		});
	}

	function _setViewSize(gridTable) {
		var opts = $d(gridTable, "datagrid").options;
		var dc = $d(gridTable, "datagrid").dc;
		var $panelContent = $d(gridTable, "datagrid").panel;

		var panelBodyWidth = $w($panelContent);
		var panelBodyHeight = $h($panelContent);

		var $view = dc.view;
		var $view1 = dc.view1;
		var $view2 = dc.view2;

		var $view1Header = $c("div.datagrid-header", $view1);
		var $view2Header = $c("div.datagrid-header", $view2);
		var $view1HeaderTable = $f("table", $view1Header);
		var $view2HeaderTable = $f("table", $view2Header);

		// set grid-view header/body/footer width
		$w(panelBodyWidth, $view);
		var $view1HeaderInner = $c("div.datagrid-header-inner", $view1Header).show();
		$w($w($f("table", $view1HeaderInner)), $view1);
		if (!opts.showHeader) {
			$view1HeaderInner.hide();
		}
		$w(panelBodyWidth - $view1.ow(), $view2);
		$w($w($view1), $c("div.datagrid-header,div.datagrid-body,div.datagrid-footer", $view1));
		$w($w($view2), $c("div.datagrid-header,div.datagrid-body,div.datagrid-footer", $view2));

		// set grid-view header height
		$view1Header.css("height", "");
		$view2Header.css("height", "");
		$view1HeaderTable.css("height", "");
		$view2HeaderTable.css("height", "");
		var viewHeaderHeight = Math.max($h($view1HeaderTable), $h($view2HeaderTable));
		$h(viewHeaderHeight, $view1HeaderTable);
		$h(viewHeaderHeight, $view2HeaderTable);
		$view1Header.add($view2Header).oh(viewHeaderHeight);

		// viewBodyHeight = panelBodyHeight - toolbarHeight - viewHeaderHeight - viewFooterHeight - pagerHeight
		if (opts.height != "auto") {

			var viewBodyHeight = panelBodyHeight - $c("div.datagrid-header", $view2).oh() - $c("div.datagrid-footer", $view2).oh() - $c("div.datagrid-toolbar", $panelContent).oh();
			$c("div.datagrid-pager", $panelContent).each(function() {
				viewBodyHeight -= $(this).oh();
			});

			// set view frozen-body position
			$c("table.datagrid-btable-frozen", dc.body1.add(dc.body2)).css({
				position : "absolute",
				top : dc.header2.oh()
			});

			// set view body height
			var viewBodyHeightFrozen = $c("table.datagrid-btable-frozen", dc.body2).oh();
			$c("div.datagrid-body", $view1.add($view2)).css({
				marginTop : viewBodyHeightFrozen,
				height : (viewBodyHeight - viewBodyHeightFrozen)
			});
		}

		// set grid-view height
		$h($h($view2), $view);
		// $h(opts.height - 35, $view);
	}

	function fixRowHeight(gridTable, rowIndex, syncFrozen) {
		if ($.type(rowIndex) == "object") {
			syncFrozen = rowIndex.syncFrozen;
			rowIndex = rowIndex.rowIndex;
		}

		var rows = $d(gridTable, "datagrid").data.rows;
		var opts = $d(gridTable, "datagrid").options;
		var dc = $d(gridTable, "datagrid").dc;

		if (!dc.body1.is(":empty") && (!opts.nowrap || opts.autoRowHeight || syncFrozen)) {
			if (rowIndex != undefined) {
				var tr1 = opts.finder.getTr(gridTable, rowIndex, "body", 1);
				var tr2 = opts.finder.getTr(gridTable, rowIndex, "body", 2);
				_setTrHeight(tr1, tr2);
			} else {
				var tr1 = opts.finder.getTr(gridTable, 0, "allbody", 1);
				var tr2 = opts.finder.getTr(gridTable, 0, "allbody", 2);
				_setTrHeight(tr1, tr2);
				if (opts.showFooter) {
					var tr1 = opts.finder.getTr(gridTable, 0, "allfooter", 1);
					var tr2 = opts.finder.getTr(gridTable, 0, "allfooter", 2);
					_setTrHeight(tr1, tr2);
				}
			}
		}

		_setViewSize(gridTable);
		if (opts.height == "auto") {
			var $view1 = $p(dc.body1);
			var $body2 = dc.body2;
			var viewBody2Height = 0;
			var viewBody2Width = 0;
			$c($body2).each(function() {
				var $table = $(this);
				if ($table.is(":visible")) {
					viewBody2Height += $table.oh();
					viewBody2Width = Math.max(viewBody2Width, $table.ow());
				}
			});

			// add horizontal scroll bar height
			if (viewBody2Width > $w($body2)) {
				viewBody2Height += 18;
			}

			$h(viewBody2Height, $view1);
			$h(viewBody2Height, $body2);
			$h($h(dc.view2), dc.view);
		}
		dc.body2.triggerHandler("scroll");

		function _setTrHeight($tr1, $tr2) {
			for ( var i = 0; i < $tr2.length; i++) {
				var tr1 = $($tr1[i]);
				var tr2 = $($tr2[i]);
				tr1.css("height", "");
				tr2.css("height", "");
				var trHeight = Math.max($h(tr1), $h(tr2));
				tr1.css("height", trHeight);
				tr2.css("height", trHeight);
			}
		}

	}

	/**
	 * Freezen Data Rows
	 */
	function freezeRow(gridTable, rowIndex) {
		var state = $d(gridTable, "datagrid");
		var opts = state.options;
		var dc = state.dc;

		// add frozen-body table
		if (!$c("table.datagrid-btable-frozen", dc.body2).length) {
			dc.body1.add(dc.body2).prepend('<table class="datagrid-btable datagrid-btable-frozen" cellspacing="0" cellpadding="0"></table>');
		}

		_appendFrozenRow(true);
		_appendFrozenRow(false);
		_setViewSize(gridTable);

		function _appendFrozenRow(isView1) {
			var viewIndex = isView1 ? 1 : 2;
			var tr = opts.finder.getTr(gridTable, rowIndex, "body", viewIndex);
			$c("table.datagrid-btable-frozen", (isView1 ? dc.body1 : dc.body2)).append(tr);
		}

	}

	function _init(gridTable, rownumbers) {
		function _getColumnsArray() {
			var frozenColumns = [];
			var columns = [];
			$c("thead", $(gridTable)).each(function() {
				var opt = jCocit.parseOptions(this, [ {
					frozen : "b"
				} ]);
				$f("tr", $(this)).each(function() {
					var cols = [];
					$f("th", $(this)).each(function() {
						var th = $(this);
						var col = $.extend({}, jCocit.parseOptions(this, [ "field", "align", "halign", "order", "name", {
							sortable : "b",
							checkbox : "b",
							rowbuttons : "b",
							resizable : "b"
						}, {
							rowspan : "n",
							colspan : "n",
							width : "n"
						} ]), {
							title : (th.html() || undefined),
							hidden : (th.attr("hidden") ? true : undefined),
							formatter : (th.attr("formatter") ? $fn(th.attr("formatter")) : undefined),
							styler : (th.attr("styler") ? $fn(th.attr("styler")) : undefined),
							sorter : (th.attr("sorter") ? $fn(th.attr("sorter")) : undefined)
						});
						if (th.attr("editor")) {
							var s = $.trim(th.attr("editor"));
							if (s.substr(0, 1) == "{") {
								col.editor = $fn("(" + s + ")");
							} else {
								col.editor = s;
							}
						}
						cols.push(col);
					});
					opt.frozen ? frozenColumns.push(cols) : columns.push(cols);
				});
			});
			return [ frozenColumns, columns ];
		}

		var $panelContent = $(
				'<div class="datagrid-wrap">' + '<div class="datagrid-view">' + '<div class="datagrid-view1">'
						+ '<div class="datagrid-header"><div class="datagrid-header-inner"></div></div>'
						+ '<div class="datagrid-body"><div class="datagrid-body-inner"></div></div>'
						+ '<div class="datagrid-footer"><div class="datagrid-footer-inner"></div></div>' + '</div>' + '<div class="datagrid-view2">'
						+ '<div class="datagrid-header"><div class="datagrid-header-inner"></div></div>' + '<div class="datagrid-body"></div>'
						+ '<div class="datagrid-footer"><div class="datagrid-footer-inner"></div></div>' + '</div>' + '</div>' + '</div>').insertAfter(gridTable);

		// init grid panel
		$panelContent.panel({
			doSize : false
		});

		$ac("datagrid", $panelContent.panel("panel")).bind("_resize", function(e, fit) {
			var opts = $d(gridTable, "datagrid").options;
			if (opts.fit == true || fit) {
				resizePanel(gridTable);
				setTimeout(function() {
					if ($d(gridTable, "datagrid")) {
						fixColumnSize(gridTable);
					}
				}, 0);
			}
			return false;
		});

		var $view = $c("div.datagrid-view", $panelContent);
		$(gridTable).hide().appendTo($view);

		var $view1 = $c("div.datagrid-view1", $view);
		var $view2 = $c("div.datagrid-view2", $view);

		var columnsArray = _getColumnsArray();
		return {
			panel : $panelContent,
			frozenColumns : columnsArray[0],
			columns : columnsArray[1],
			dc : {
				view : $view,
				view1 : $view1,
				view2 : $view2,
				header1 : $f("div.datagrid-header>div.datagrid-header-inner", $view1),
				header2 : $f("div.datagrid-header>div.datagrid-header-inner", $view2),
				body1 : $f("div.datagrid-body>div.datagrid-body-inner", $view1),
				body2 : $c("div.datagrid-body", $view2),
				footer1 : $f("div.datagrid-footer>div.datagrid-footer-inner", $view1),
				footer2 : $f("div.datagrid-footer>div.datagrid-footer-inner", $view2)
			}
		};
	}

	/**
	 * Parse Grid Data from table.
	 */
	function _parseGridData(gridTable) {
		var data = {
			total : 0,
			rows : []
		};
		var fields = getColumnFields(gridTable, true).concat(getColumnFields(gridTable, false));
		$f("tbody tr", $(gridTable)).each(function() {
			data.total++;
			var row = {};
			for ( var i = 0; i < fields.length; i++) {
				row[fields[i]] = $("td:eq(" + i + ")", this).html();
			}
			data.rows.push(row);
		});
		return data;
	}

	/**
	 * Initialize grid model and later fill data.
	 */
	function _initGridModel(gridTable) {
		var state = $d(gridTable, "datagrid");
		var opts = state.options;
		var dc = state.dc;
		var $panelContent = state.panel;

		$panelContent.panel($.extend({}, opts, {
			url : null,
			id : null,
			doSize : false,
			onResize : function(width, height) {
				setTimeout(function() {
					if ($d(gridTable, "datagrid")) {
						_setViewSize(gridTable);
						fitColumnsWidth(gridTable);
						opts.onResize.call($panelContent, width, height);
					}
				}, 0);
			},
			onExpand : function() {
				fixRowHeight(gridTable);
				opts.onExpand.call($panelContent);
			}
		}));
		state.rowIdPrefix = "datagrid-row-r" + (++gridCount);

		// init header
		_initHeader(dc.header1, opts.frozenColumns, true);
		_initHeader(dc.header2, opts.columns, false);
		_initColumnsStyle();

		dc.header1.add(dc.header2).css("display", opts.showHeader ? "block" : "none");
		dc.footer1.add(dc.footer2).css("display", opts.showFooter ? "block" : "none");

		// init toolbar
		if (opts.toolbar) {
			if (typeof opts.toolbar == "string") {
				$ac("datagrid-toolbar", $(opts.toolbar)).prependTo($panelContent);
				$(opts.toolbar).show();
			} else {
				$("div.datagrid-toolbar", $panelContent).remove();
				var tb = $('<div class="datagrid-toolbar"><table cellspacing="0" cellpadding="0"><tr></tr></table></div>').prependTo($panelContent);
				var tr = $f("tr", tb);
				for ( var i = 0; i < opts.toolbar.length; i++) {
					var btn = opts.toolbar[i];
					if (btn == "-") {
						$('<td><div class="datagrid-btn-separator"></div></td>').appendTo(tr);
					} else {
						var td = $("<td></td>").appendTo(tr);
						var $btn = $('<a href="javascript:void(0)"></a>').appendTo(td);
						// $btn[0].onclick = $fn(btn.handler || function() {
						// });
						$btn.button($.extend({}, btn, {
							plain : true
						}));
					}
				}
			}
		} else {
			$("div.datagrid-toolbar", $panelContent).remove();
		}

		// init pager
		$("div.datagrid-pager", $panelContent).remove();
		if (opts.pagination) {
			var $pager = $('<div class="datagrid-pager"></div>');
			if (opts.paginationPos == "bottom") {
				// append bottom pager
				$pager.appendTo($panelContent);
			} else if (opts.paginationPos == "top") {
				// append top pager
				$ac("datagrid-pager-top", $pager).prependTo($panelContent);
			} else {
				// append top and bottom pager
				var $topPager = $('<div class="datagrid-pager datagrid-pager-top"></div>').prependTo($panelContent);
				$pager.appendTo($panelContent);
				$pager = $pager.add($topPager);
			}

			$pager.pagination({
				total : 0,
				pageIndex : opts.pageIndex,
				pageSize : opts.pageSize,
				pageOptions : opts.pageOptions,
				showPageList : opts.showPageList,
				showRefresh : opts.showRefresh,
				buttons : opts.pageButtons,
				onSelectPage : function(pageIndex, pageSize) {
					opts.pageIndex = pageIndex;
					opts.pageSize = pageSize;
					$pager.pagination("refresh", {
						pageIndex : pageIndex,
						pageSize : pageSize
					});
					loadGridData(gridTable);
				}
			});
			opts.pageSize = $pager.pagination("options").pageSize;
		}

		// init header
		function _initHeader($header, columns, frozen) {
			if (!columns) {
				return;
			}
			$header.show();
			$header.empty();
			var $headerTable = $('<table class="datagrid-htable" border="0" cellspacing="0" cellpadding="0"><tbody></tbody></table>').appendTo($header);
			for ( var i = 0; i < columns.length; i++) {
				var tr = $('<tr class="datagrid-header-row"></tr>').appendTo($("tbody", $headerTable));
				var cols = columns[i];
				for ( var j = 0; j < cols.length; j++) {
					var col = cols[j];
					var tdAttrs = "";
					if (col.rowspan) {
						tdAttrs += 'rowspan="' + col.rowspan + '" ';
					}
					if (col.colspan) {
						tdAttrs += 'colspan="' + col.colspan + '" ';
					}
					var td = $('<td ' + tdAttrs + '></td>').appendTo(tr);
					if (col.checkbox) {
						td.attr("field", col.field);
						/*
						 * COC: checkAll uncheckAll reverse
						 */
						// $('<div class="datagrid-header-check"></div>').html('<input type="checkbox"/>').appendTo(td);
						var checkdiv = $('<div class="datagrid-header-check"></div>').appendTo(td);
						if (!opts.singleSelect)
							checkdiv.html('<input type="checkbox"/>');
					} else {
						if (col.field) {
							td.attr("field", col.field);
							td.append('<div class="datagrid-cell"><span>' + col.title || "" + '</span><span class="datagrid-sort-icon">&nbsp;</span></div>');
							var $headerCell = $f("div.datagrid-cell", td);
							if (col.resizable == false) {
								$headerCell.attr("resizable", "false");
							}
							if (col.width) {
								$headerCell.ow(col.width);
								col.boxWidth = parseInt($headerCell[0].style.width);
							} else {
								col.auto = true;
							}
							$headerCell.css("text-align", (col.halign || col.align || ""));
							col.cellClass = "datagrid-cell-c" + gridCount + "-" + col.field.replace(/\./g, "-");
							col.cellSelector = "div." + col.cellClass;
						} else {
							$('<div class="datagrid-cell-group"></div>').html(col.title).appendTo(td);
						}
					}
					if (col.hidden) {
						td.hide();
					}
				}
			}

			// init row number
			if (frozen && opts.rownumbers) {
				var td = $('<td rowspan=' + opts.frozenColumns.length + '"><div class="datagrid-header-rownumber"></div></td>');
				if ($("tr", $headerTable).length == 0) {
					$p(td.wrap('<tr class="datagrid-header-row"></tr>')).appendTo($("tbody", $headerTable));
				} else {
					td.prependTo($("tr:first", $headerTable));
				}
			}
		}

		function _initColumnsStyle() {
			var ss = [ '<style type="text/css">' ];
			var fields = getColumnFields(gridTable, true).concat(getColumnFields(gridTable));
			for ( var i = 0; i < fields.length; i++) {
				var col = getColumnOption(gridTable, fields[i]);
				if (col && !col.checkbox) {
					ss.push(col.cellSelector + " {width:" + col.boxWidth + "px;}");
				}
			}
			ss.push("</style>");
			$(ss.join("\n")).prependTo(dc.view);
		}
	}

	function _bindGridEvents(gridTable) {
		var state = $d(gridTable, "datagrid");
		var $panelContent = state.panel;
		var opts = state.options;
		var dc = state.dc;
		var $header = dc.header1.add(dc.header2);

		// mouse click header checkbox
		$f("input[type=checkbox]", $header).unbind(".datagrid").bind("click.datagrid", function(e) {
			if (opts.singleSelect && opts.selectOnCheck) {
				return false;
			}
			if ($(this).is(":checked")) {
				checkAll(gridTable);
			} else {
				uncheckAll(gridTable);
			}
			e.stopPropagation();
		});

		// mouse enter/leave/contextmenu header cell
		var $headerCells = $f("div.datagrid-cell", $header);
		$l("td", $headerCells).unbind(".datagrid").bind("mouseenter.datagrid", function() {
			if (state.resizing) {
				return;
			}
			$ac("datagrid-header-over", $(this));
		}).bind("mouseleave.datagrid", function() {
			$rc("datagrid-header-over", $(this));
		}).bind("contextmenu.datagrid", function(e) {
			var field = $(this).attr("field");
			opts.onHeaderContextMenu.call(gridTable, e, field);
		});

		// mouse click/dblclick header cell
		$headerCells.unbind(".datagrid").bind("click.datagrid", function(e) {
			var p1 = $(this).offset().left + 5;
			var p2 = $(this).offset().left + $(this).ow() - 5;

			// sort by field
			if (e.pageX < p2 && e.pageX > p1) {
				var field = $p($(this)).attr("field");
				var col = getColumnOption(gridTable, field);
				if (!col.sortable || state.resizing) {
					return;
				}
				opts.sortField = field;
				opts.sortOrder = col.order || "asc";
				var cls = "datagrid-sort-" + opts.sortOrder;
				if ($hc("datagrid-sort-asc", $(this))) {
					cls = "datagrid-sort-desc";
					opts.sortOrder = "desc";
				} else {
					if ($hc("datagrid-sort-desc", $(this))) {
						cls = "datagrid-sort-asc";
						opts.sortOrder = "asc";
					}
				}
				$rc("datagrid-sort-asc datagrid-sort-desc", $headerCells);
				$ac(cls, $(this));
				if (opts.sortOnRemote) {
					loadGridData(gridTable);
				} else {
					var gridData = $d(gridTable, "datagrid").data;
					loadData(gridTable, gridData);
				}
				opts.onSortColumn.call(gridTable, opts.sortField, opts.sortOrder);
			}
		}).bind("dblclick.datagrid", function(e) {
			var p1 = $(this).offset().left + 5;
			var p2 = $(this).offset().left + $(this).ow() - 5;
			var resizable = opts.resizeHandle == "right" ? (e.pageX > p2) : (opts.resizeHandle == "left" ? (e.pageX < p1) : (e.pageX < p1 || e.pageX > p2));
			if (resizable) {
				var field = $p($(this)).attr("field");
				var col = getColumnOption(gridTable, field);
				if (col.resizable == false) {
					return;
				}
				$(gridTable).datagrid("autoSizeColumn", field);
				col.auto = false;
			}
		});

		// header column resizable
		var resizeHandles = opts.resizeHandle == "right" ? "e" : (opts.resizeHandle == "left" ? "w" : "e,w");
		$headerCells.each(function() {
			$(this).resizable({
				handles : resizeHandles,
				disabled : ($(this).attr("resizable") ? $(this).attr("resizable") == "false" : false),
				minWidth : 25,
				onStartResize : function(e) {
					state.resizing = true;
					$header.css("cursor", $("body").css("cursor"));
					if (!state.proxy) {
						state.proxy = $('<div class="datagrid-resize-proxy"></div>').appendTo(dc.view);
					}
					state.proxy.css({
						left : e.pageX - $($panelContent).offset().left - 1,
						display : "none"
					});
					setTimeout(function() {
						if (state.proxy) {
							state.proxy.show();
						}
					}, 500);
				},
				onResize : function(e) {
					state.proxy.css({
						left : e.pageX - $($panelContent).offset().left - 1,
						display : "block"
					});
					return false;
				},
				onStopResize : function(e) {
					var ths = this;
					var $this = $(ths);

					$header.css("cursor", "");
					var field = $p($this).attr("field");
					var col = getColumnOption(gridTable, field);
					col.width = $this.ow();
					col.boxWidth = parseInt(ths.style.width);
					col.auto = undefined;
					fixColumnSize(gridTable, field);
					state.proxy.remove();
					state.proxy = null;
					if ($hc("datagrid-view1", $p($this.parents("div:first.datagrid-header")))) {
						_setViewSize(gridTable);
					}
					fitColumnsWidth(gridTable);
					opts.onResizeColumn.call(gridTable, field, col.width);

					setTimeout(function() {
						state.resizing = false;
					}, 50);
				}
			});
		});

		// mouse over/out/click/dblclick/contextmenu body row
		dc.body1.add(dc.body2).unbind().bind("mouseover", function(e) {
			if (state.resizing) {
				return;
			}
			var tr = $l("tr.datagrid-row", $(e.target));
			if (!tr.length) {
				return;
			}
			var rowIndex = _getRowIndex(tr);
			$ac("datagrid-row-over", opts.finder.getTr(gridTable, rowIndex));
			e.stopPropagation();
		}).bind("mouseout", function(e) {
			var tr = $l("tr.datagrid-row", $(e.target));
			if (!tr.length) {
				return;
			}
			var rowIndex = _getRowIndex(tr);
			$rc("datagrid-row-over", opts.finder.getTr(gridTable, rowIndex));
			e.stopPropagation();
		}).bind("click", function(e) {
			var tt = $(e.target);

			var tr = $l("tr.datagrid-row", tt);
			// COC
			// if (!tr.length || opts.noSelect) {
			// return;
			// }
			var rowIndex = _getRowIndex(tr);
			if (typeof rowIndex == "undefined") {
				return;
			}

			var isCheckbox = $hc("datagrid-cell-check", $p(tt)) || $hc("TrC", tt);
			if (isCheckbox) {
				if (opts.singleSelect && opts.selectOnCheck) {
					if (!opts.checkOnSelect) {
						uncheckAll(gridTable, true);
					}
					checkRow(gridTable, rowIndex);
				} else {
					var checked;
					if (tt.hasClass("TrC")) {
						checked = !$hc("TrC1", tt);
					} else {
						checked = tt.is(":checked");
					}
					if (checked) {
						checkRow(gridTable, rowIndex);
					} else {
						uncheckRow(gridTable, rowIndex);
					}
				}
			} else {
				var row = opts.finder.getRow(gridTable, rowIndex);
				var td = $l("td[field]", tr, tt);
				if (td.length) {
					var field = td.attr("field");
					opts.onClickCell.call(gridTable, rowIndex, field, row[field], td, tt);
				}

				/*
				 * COC: 点击的UI为按钮或输入框：则不选中
				 */
				if (e.target.name || $(e.target).attr("for") || opts.noSelect) {
				} else {
					if (opts.singleSelect == true) {
						selectRow(gridTable, rowIndex);
					} else {
						if ($hc("datagrid-row-selected", tr)) {
							unselectRow(gridTable, rowIndex);
						} else {
							selectRow(gridTable, rowIndex);
						}
					}
				}

				opts.onClickRow.call(gridTable, rowIndex, row);
			}

			e.stopPropagation();
		}).bind("dblclick", function(e) {
			var tt = $(e.target);
			var tr = $l("tr.datagrid-row", tt);
			if (!tr.length) {
				return;
			}
			var rowIndex = _getRowIndex(tr);
			var row = opts.finder.getRow(gridTable, rowIndex);
			var td = $l("td[field]", tr, tt);
			if (td.length) {
				var field = td.attr("field");
				opts.onDblClickCell.call(gridTable, rowIndex, field, row[field]);
			}
			opts.onDblClickRow.call(gridTable, rowIndex, row);
			e.stopPropagation();
		}).bind("contextmenu", function(e) {
			var tr = $l("tr.datagrid-row", $(e.target));
			if (!tr.length) {
				return;
			}
			var rowIndex = _getRowIndex(tr);
			var row = opts.finder.getRow(gridTable, rowIndex);
			opts.onRowContextMenu.call(gridTable, e, rowIndex, row, tr);
			e.stopPropagation();
		});

		// scroll body
		dc.body2.bind("scroll", function() {
			$c("div.datagrid-body", dc.view1).scrollTop($(this).scrollTop());
			$c("div.datagrid-header,div.datagrid-footer", dc.view2)._scrollLeft($(this)._scrollLeft());
			$c("table.datagrid-btable-frozen", dc.body2).css("left", -$(this)._scrollLeft());
		});

		function _getRowIndex(tr) {
			if (tr.attr("datagrid-row-index")) {
				return parseInt(tr.attr("datagrid-row-index"));
			} else {
				return tr.attr("node-id");
			}
		}

	}

	/**
	 * Fit Columns Width to blank space of panel content.
	 */
	function fitColumnsWidth(gridTable) {
		var opts = $d(gridTable, "datagrid").options;
		var dc = $d(gridTable, "datagrid").dc;

		if (!opts.fitColumns) {
			return;
		}

		var $view2Header = $c("div.datagrid-header", dc.view2);
		var fixedColumnsWidth = 0;
		var lastColumn;
		var fields = getColumnFields(gridTable, false);
		for ( var i = 0; i < fields.length; i++) {
			var col = getColumnOption(gridTable, fields[i]);
			if (_isFixedColumn(col)) {
				fixedColumnsWidth += col.width;
				lastColumn = col;
			}
		}
		var $view2HeaderInner = $c("div.datagrid-header-inner", $view2Header).show();
		var usableWidth = $w($view2Header) - $w($f("table", $view2Header)) - opts.scrollbarSize;
		var rate = usableWidth / fixedColumnsWidth;
		if (!opts.showHeader) {
			$view2HeaderInner.hide();
		}
		for ( var i = 0; i < fields.length; i++) {
			var col = getColumnOption(gridTable, fields[i]);
			if (_isFixedColumn(col)) {
				var adjustWidth = Math.floor(col.width * rate);
				_adjustColumnWidth(col, adjustWidth);
				usableWidth -= adjustWidth;
			}
		}
		if (usableWidth && lastColumn) {
			_adjustColumnWidth(lastColumn, usableWidth);
		}
		fixColumnSize(gridTable);

		function _adjustColumnWidth(col, width) {
			col.width += width;
			col.boxWidth += width;
			$w(col.boxWidth, $f('td[field="' + col.field + '"] div.datagrid-cell', $view2Header));
		}

		function _isFixedColumn(col) {
			if (!col.hidden && !col.checkbox && !col.auto) {
				return true;
			}
		}

	}

	/**
	 * Auto set column width
	 */
	function autoSizeColumn(gridTable, field) {
		var $grid = $(gridTable);
		var opts = $d(gridTable, "datagrid").options;
		var dc = $d(gridTable, "datagrid").dc;
		// fit rownumbers column width

		if (field) {
			_setColumnWidth($grid, field);
			if (opts.fitColumns) {
				_setViewSize(gridTable);
				fitColumnsWidth(gridTable);
			}
		} else {
			var hasAutoColumn = false;
			var fields = getColumnFields(gridTable, true).concat(getColumnFields(gridTable, false));
			for ( var i = 0; i < fields.length; i++) {
				var field = fields[i];
				var col = getColumnOption(gridTable, field);
				if (col.auto) {
					_setColumnWidth($grid, field);
					hasAutoColumn = true;
				}
			}
			if (hasAutoColumn && opts.fitColumns) {
				_setViewSize(gridTable);
				fitColumnsWidth(gridTable);
			}
		}

		function _setColumnWidth($grid, field) {
			var $headerCell = $f('div.datagrid-header td[field="' + field + '"] div.datagrid-cell', dc.view);
			$headerCell.css("width", "");
			var col = $grid.datagrid("getColumnOption", field);
			col.width = undefined;
			col.boxWidth = undefined;
			col.auto = true;
			$grid.datagrid("fixColumnSize", field);
			var headerCellWidth = Math.max($headerCell.ow(), _getColumnMaxWidth("allbody"), _getColumnMaxWidth("allfooter"));
			$headerCell.ow(headerCellWidth);
			col.width = headerCellWidth;
			col.boxWidth = parseInt($headerCell[0].style.width);
			$grid.datagrid("fixColumnSize", field);
			opts.onResizeColumn.call(gridTable, field, col.width);

			function _getColumnMaxWidth(type) {
				var maxWidth = 0;
				$f('td[field="' + field + '"] div.datagrid-cell', opts.finder.getTr(gridTable, 0, type)).each(function() {
					var w = $(this).ow();
					if (maxWidth < w) {
						maxWidth = w;
					}
				});
				if (maxWidth < 60)
					return 60;
				return maxWidth;
			}

		}

	}

	function mergeCells(gridTable, cellConfig) {
		var opts = $d(gridTable, "datagrid").options;
		cellConfig.rowspan = cellConfig.rowspan || 1;
		cellConfig.colspan = cellConfig.colspan || 1;
		if (cellConfig.rowspan == 1 && cellConfig.colspan == 1) {
			return;
		}
		var tr = opts.finder.getTr(gridTable, (cellConfig.index != undefined ? cellConfig.index : cellConfig.id));
		if (!tr.length) {
			return;
		}
		var row = opts.finder.getRow(gridTable, tr);
		var fieldValue = row[cellConfig.field];
		var td = $f('td[field="' + cellConfig.field + '"]', tr);
		td.attr("rowspan", cellConfig.rowspan).attr("colspan", cellConfig.colspan);
		$ac("datagrid-td-merged", td);
		for ( var i = 1; i < cellConfig.colspan; i++) {
			td = td.next();
			td.hide();
			row[td.attr("field")] = fieldValue;
		}
		for ( var i = 1; i < cellConfig.rowspan; i++) {
			tr = tr.next();
			if (!tr.length) {
				break;
			}
			var row = opts.finder.getRow(gridTable, tr);
			var td = $f('td[field="' + cellConfig.field + '"]', tr).hide();
			row[td.attr("field")] = fieldValue;
			for ( var j = 1; j < cellConfig.colspan; j++) {
				td = td.next();
				td.hide();
				row[td.attr("field")] = fieldValue;
			}
		}
		resizeMergedCellWidth(gridTable);
	}

	function resizeMergedCellWidth(gridTable) {
		var dc = $d(gridTable, "datagrid").dc;
		$f("td.datagrid-td-merged", dc.body1.add(dc.body2)).each(function() {
			var td = $(this);
			var colspan = td.attr("colspan") || 1;
			var mergedCellWidth = getColumnOption(gridTable, td.attr("field")).width;
			for ( var i = 1; i < colspan; i++) {
				td = td.next();
				mergedCellWidth += getColumnOption(gridTable, td.attr("field")).width + 1;
			}
			$c("div.datagrid-cell", $(this)).ow(mergedCellWidth);
		});
	}

	function fixColumnSize(gridTable, field) {
		var opts = $d(gridTable, "datagrid").options;
		var dc = $d(gridTable, "datagrid").dc;
		var $bodyAndFooterTables = $f("table.datagrid-btable,table.datagrid-ftable", dc.view);
		$bodyAndFooterTables.css("table-layout", "fixed");
		if (field) {
			_fix(field);
		} else {
			var ff = getColumnFields(gridTable, true).concat(getColumnFields(gridTable, false));
			for ( var i = 0; i < ff.length; i++) {
				_fix(ff[i]);
			}
		}
		$bodyAndFooterTables.css("table-layout", "auto");
		resizeMergedCellWidth(gridTable);
		setTimeout(function() {
			fixRowHeight(gridTable);
			$(gridTable).datagrid("resizeEditor");
		}, 0);

		function _fix(field) {
			var col = getColumnOption(gridTable, field);
			if (col.checkbox) {
				return;
			}
			var style = $c("style", dc.view)[0];
			var styleSheet = style.styleSheet ? style.styleSheet : (style.sheet || document.styleSheets[document.styleSheets.length - 1]);
			var cssRules = styleSheet.cssRules || styleSheet.rules;
			for ( var i = 0, len = cssRules.length; i < len; i++) {
				var cssRule = cssRules[i];
				if (cssRule.selectorText.toLowerCase() == col.cellSelector.toLowerCase()) {
					cssRule.style["width"] = col.boxWidth ? col.boxWidth + "px" : "auto";
					break;
				}
			}
		}
	}

	function getColumnOption(gridTable, field) {
		function _getColumnOption(columns) {
			if (columns) {
				for ( var i = 0; i < columns.length; i++) {
					var cols = columns[i];
					for ( var j = 0; j < cols.length; j++) {
						var col = cols[j];
						if (col.field == field) {
							return col;
						}
					}
				}
			}
			return null;
		}

		var opts = $d(gridTable, "datagrid").options;
		var col = _getColumnOption(opts.columns);
		if (!col) {
			col = _getColumnOption(opts.frozenColumns);
		}
		return col;
	}

	function getColumnFields(gridTable, frozen) {
		var opts = $d(gridTable, "datagrid").options;
		var columns = (frozen == true) ? (opts.frozenColumns || [ [] ]) : opts.columns;
		if (columns.length == 0) {
			return [];
		}
		var fields = [];

		function _parseColIndex(colIndex) {
			var c = 0;
			var i = 0;
			while (true) {
				if (fields[i] == undefined) {
					if (c == colIndex) {
						return i;
					}
					c++;
				}
				i++;
			}
		}

		function _parseFields(headerRowIndex) {
			var ff = [];
			var colIndex = 0;
			for ( var i = 0; i < columns[headerRowIndex].length; i++) {
				var col = columns[headerRowIndex][i];
				if (col.field) {
					ff.push([ colIndex, col.field ]);
				}
				colIndex += parseInt(col.colspan || "1");
			}
			for ( var i = 0; i < ff.length; i++) {
				// ff[i] is array, ff[i][0] is colIndex
				ff[i][0] = _parseColIndex(ff[i][0]);
			}
			for ( var i = 0; i < ff.length; i++) {
				var f = ff[i];
				// f[0] is colIndex, f[1] is field
				fields[f[0]] = f[1];
			}
		}

		for ( var i = 0; i < columns.length; i++) {
			_parseFields(i);
		}
		return fields;
	}

	function loadData(gridTable, gridData) {
		var time0 = new Date().getTime();

		var $grid = $(gridTable);

		var state = $d(gridTable, "datagrid");
		var opts = state.options;
		var dc = state.dc;

		gridData = opts.loadFilter.call(gridTable, gridData);
		gridData.total = parseInt(gridData.total);
		state.data = gridData;
		if (gridData.footer) {
			state.footer = gridData.footer;
		}
		if (!opts.sortOnRemote) {
			var sortCol = getColumnOption(gridTable, opts.sortField);
			if (sortCol) {
				var sorter = sortCol.sorter || function(a, b) {
					return (a > b ? 1 : -1);
				};
				gridData.rows.sort(function(row1, row2) {
					return sorter(row1[opts.sortField], row2[opts.sortField]) * (opts.sortOrder == "asc" ? 1 : -1);
				});
			}
		}
		if (opts.view.onBeforeRender) {
			opts.view.onBeforeRender.call(opts.view, gridTable, gridData.rows);
		}
		opts.view.render.call(opts.view, gridTable, dc.body2, false);
		opts.view.render.call(opts.view, gridTable, dc.body1, true);
		if (opts.showFooter) {
			opts.view.renderFooter.call(opts.view, gridTable, dc.footer2, false);
			opts.view.renderFooter.call(opts.view, gridTable, dc.footer1, true);
		}
		if (opts.view.onAfterRender) {
			opts.view.onAfterRender.call(opts.view, gridTable);
		}
		$c("style:gt(0)", dc.view).remove();

		// COC: 解析UI组件
		var rows = $grid.datagrid("getPanel").find(".datagrid-row");
		jCocit.parseUI(rows);

		opts.onLoadSuccess.call(gridTable, gridData);
		var $pager = $grid.datagrid("getPager");
		if ($pager.length) {
			if ($pager.pagination("options").total != gridData.total) {
				$pager.pagination("refresh", {
					total : gridData.total
				});
			}
		}
		fixRowHeight(gridTable);
		dc.body2.triggerHandler("scroll");
		$grid.datagrid("setSelectionState");
		$grid.datagrid("autoSizeColumn");

		$log("ui.datagrid: loadData (rows = {1}, title = {2})", time0, gridData.rows.length, gridTable.title || "");

	}

	function setSelectionState(gridTable) {
		var state = $.data(gridTable, "datagrid");
		var opts = state.options;
		var dc = state.dc;
		dc.header1.add(dc.header2).find("input[type=checkbox]")._propAttr("checked", false);
		if (opts.idField) {
			var isTreeGrid = $.data(gridTable, "treegrid") ? true : false;
			var onSelect = opts.onSelect;
			var onCheck = opts.onCheck;
			opts.onSelect = opts.onCheck = function() {
			};
			var rows = opts.finder.getRows(gridTable);
			for ( var i = 0; i < rows.length; i++) {
				var row = rows[i];
				var idOrIndex = isTreeGrid ? row[opts.idField] : i;
				if (_isSelected(state.selectedRows, row)) {
					selectRow(gridTable, idOrIndex, true);
				}
				if (_isSelected(state.checkedRows, row)) {
					checkRow(gridTable, idOrIndex, true);
				}
			}
			opts.onSelect = onSelect;
			opts.onCheck = onCheck;
		}
		function _isSelected(rows, row) {
			for ( var i = 0; i < rows.length; i++) {
				if (rows[i][opts.idField] == row[opts.idField]) {
					rows[i] = row;
					return true;
				}
			}
			return false;
		}
		;
	}

	function getRowIndex(gridTable, rowOrId) {
		var opts = $d(gridTable, "datagrid").options;
		var rows = $d(gridTable, "datagrid").data.rows;
		if (typeof rowOrId == "object") {
			return _indexOfRow(rows, rowOrId);
		} else {
			for ( var i = 0; i < rows.length; i++) {
				if (rows[i][opts.idField] == rowOrId) {
					return i;
				}
			}
			return -1;
		}
	}

	function getSelectedRows(gridTable) {
		var state = $d(gridTable, "datagrid");
		var opts = state.options;
		var gridData = state.data;
		if (opts.idField && !opts.WYSIWYG) {
			return state.selectedRows;
		} else {
			var rows = [];
			opts.finder.getTr(gridTable, "", "selected", 2).each(function() {
				rows.push(opts.finder.getRow(gridTable, $(this)));
			});
			return rows;
		}
	}

	function getCheckedRows(gridTable) {
		var state = $d(gridTable, "datagrid");
		var opts = state.options;
		if (opts.idField && !opts.WYSIWYG) {
			return state.checkedRows;
		} else {
			var rows = [];
			opts.finder.getTr(gridTable, "", "checked", 2).each(function() {
				rows.push(opts.finder.getRow(gridTable, $(this)));
			});
			return rows;
		}
	}

	function selectRecord(gridTable, rowOrId) {
		var opts = $d(gridTable, "datagrid").options;
		if (opts.idField) {
			var rowIndex = getRowIndex(gridTable, rowOrId);
			if (rowIndex >= 0) {
				selectRow(gridTable, rowIndex);
			}
		}
	}

	function selectRow(gridTable, rowIndex, ignoreCheck) {
		if ($.type(rowIndex) == "object") {
			rowIndex = rowIndex.rowIndex;
			ignoreCheck = rowIndex.ignoreCheck;
		}

		var state = $d(gridTable, "datagrid");
		var dc = state.dc;
		var opts = state.options;
		var selectedRows = state.selectedRows;

		// COC: 点击行点选；勾选为多选
		uncheckAll(gridTable, true);
		// if (opts.singleSelect) {
		unselectAll(gridTable);
		selectedRows.splice(0, selectedRows.length);
		// }
		// COC: 点击行点选；勾选为多选：END!

		if (!ignoreCheck && opts.checkOnSelect) {
			checkRow(gridTable, rowIndex, true);
		}
		var row = opts.finder.getRow(gridTable, rowIndex);
		if (opts.idField) {
			_addRow(selectedRows, opts.idField, row);
		}
		var tr = $ac("datagrid-row-selected", opts.finder.getTr(gridTable, rowIndex));

		// scroll to selected row
		if (tr.length) {
			if ($hc("datagrid-btable-frozen", $l("table", tr))) {
				return;
			}
			var view2HeaderHeight = $c("div.datagrid-header", dc.view2).oh();
			var $body2 = dc.body2;
			var borderPaddingHeight = $oh(true, $body2) - $oh($body2);
			var top = tr.position().top - view2HeaderHeight - borderPaddingHeight;
			if (top < 0) {
				$body2.scrollTop($body2.scrollTop() + top);
			} else {
				if (top + tr.oh() > $h($body2) - 18) {
					$body2.scrollTop($body2.scrollTop() + top + tr.oh() - $h($body2) + 18);
				}
			}
		}

		opts.onSelect.call(gridTable, rowIndex, row);
	}

	function unselectRow(gridTable, rowIndex, ignoreCheck) {
		var state = $d(gridTable, "datagrid");
		var dc = state.dc;
		var opts = state.options;
		var selectedRows = $d(gridTable, "datagrid").selectedRows;
		if (!ignoreCheck && opts.checkOnSelect) {
			uncheckRow(gridTable, rowIndex, true);
		}
		var tr = opts.finder.getTr(gridTable, rowIndex);
		$rc("datagrid-row-selected", tr);
		var row = opts.finder.getRow(gridTable, rowIndex);
		if (opts.idField) {
			_removeRow(selectedRows, opts.idField, row[opts.idField]);
		}
		opts.onUnselect.call(gridTable, rowIndex, row);
	}

	function selectAll(gridTable, ignoreCheck) {
		var state = $d(gridTable, "datagrid");
		var opts = state.options;
		var rows;
		if (opts.finder.getRows) {
			rows = opts.finder.getRows(gridTable);
		} else {
			rows = state.data.rows;
		}
		var selectedRows = $d(gridTable, "datagrid").selectedRows;
		if (!ignoreCheck && opts.checkOnSelect) {
			checkAll(gridTable, true);
		}
		$ac("datagrid-row-selected", opts.finder.getTr(gridTable, "", "allbody"));
		if (opts.idField) {
			for ( var rowIndex = 0; rowIndex < rows.length; rowIndex++) {
				_addRow(selectedRows, opts.idField, rows[rowIndex]);
			}
		}
		opts.onSelectAll.call(gridTable, rows);
	}

	function unselectAll(gridTable, ignoreCheck) {
		var state = $d(gridTable, "datagrid");
		var opts = state.options;
		var rows;
		if (opts.finder.getRows) {
			rows = opts.finder.getRows(gridTable);
		} else {
			rows = state.data.rows;
		}
		var selectedRows = $d(gridTable, "datagrid").selectedRows;

		// COC: 点击行点选；勾选为多选
		// if (!ignoreCheck && opts.checkOnSelect) {
		// uncheckAll(gridTable, true);
		// }
		// COC: 点击行点选；勾选为多选：END!

		$rc("datagrid-row-selected", opts.finder.getTr(gridTable, "", "selected"));
		if (opts.idField) {
			for ( var _ee = 0; _ee < rows.length; _ee++) {
				_removeRow(selectedRows, opts.idField, rows[_ee][opts.idField]);
			}
		}
		opts.onUnselectAll.call(gridTable, rows);
	}

	function checkRow(gridTable, rowIndex, ignoreSelect, ignoreEvent) {

		var state = $d(gridTable, "datagrid");
		var opts = state.options;

		// COC: 点击行点选；勾选为多选
		if (opts.singleSelect) {
			uncheckAll(gridTable, true);
		}
		if (!ignoreSelect) {
			var selectedRows = state.selectedRows;
			unselectAll(gridTable);
			selectedRows.splice(0, selectedRows.length);
		}
		if (!ignoreSelect && opts.selectOnCheck && opts.singleSelect) {
			selectRow(gridTable, rowIndex, true);
		}
		// COC: 点击行点选；勾选为多选：END!

		// V3
		// var ck = $f("div.datagrid-cell-check input[type=checkbox]", opts.finder.getTr(gridTable, rowIndex));
		// ck._attr("checked", true);
		// ck = $f("div.datagrid-cell-check input[type=checkbox]:not(:checked)", opts.finder.getTr(gridTable, "", "allbody"));
		// if (!ck.length) {
		// var dc = state.dc;
		// var $viewHeader = dc.header1.add(dc.header2);
		// $f("input[type=checkbox]", $viewHeader)._attr("checked", true);
		// }
		// var row = opts.finder.getRow(gridTable, rowIndex);
		// if (opts.idField) {
		// _addRow(state.checkedRows, opts.idField, row);
		// }
		// opts.onCheck.call(gridTable, rowIndex, row);

		// V4
		var row = opts.finder.getRow(gridTable, rowIndex);
		var tr = opts.finder.getTr(gridTable, rowIndex).addClass("datagrid-row-checked");
		tr.find("div.datagrid-cell-check input[type=checkbox]")._propAttr("checked", true);
		tr.find("span.TrC").addClass("TrC1");
		var tr2 = opts.finder.getTr(gridTable, "", "checked", 2);
		if (tr2.length == opts.finder.getRows(gridTable).length) {
			var dc = state.dc;
			dc.header1.add(dc.header2).find("input[type=checkbox]")._propAttr("checked", true);
		}
		if (opts.idField) {
			_addRow(state.checkedRows, opts.idField, row);
		}

		// COC: 级联勾选
		if (!ignoreSelect) {
			var subtree = tr.next();
			if (subtree.hasClass("treegrid-tr-tree")) {
				subtree.find("tr.datagrid-row").addClass("datagrid-row-checked");
				subtree.find("span.TrC").addClass("TrC1");
			}
		}

		if (!ignoreEvent)
			opts.onCheck.call(gridTable, rowIndex, row, tr);
	}

	function uncheckRow(gridTable, rowIndex, ignoreSelect) {
		var state = $d(gridTable, "datagrid");
		var opts = state.options;

		// V3
		// if (!ignoreSelect && opts.selectOnCheck) {
		// unselectRow(gridTable, rowIndex, true);
		// }
		// var ck = $f("div.datagrid-cell-check input[type=checkbox]", opts.finder.getTr(gridTable, rowIndex));
		// ck._attr("checked", false);
		// var dc = state.dc;
		// var $viewHeader = dc.header1.add(dc.header2);
		// $f("input[type=checkbox]", $viewHeader)._attr("checked", false);
		// var row = opts.finder.getRow(gridTable, rowIndex);
		// if (opts.idField) {
		// _removeRow(state.checkedRows, opts.idField, row[opts.idField]);
		// }
		// opts.onUncheck.call(gridTable, rowIndex, row);

		// V4
		var row = opts.finder.getRow(gridTable, rowIndex);
		// if (opts.onBeforeUncheck.call(gridTable, rowIndex, row) == false) {
		// return;
		// }
		if (!ignoreSelect && opts.selectOnCheck) {
			unselectRow(gridTable, rowIndex, true);
		}
		var tr = opts.finder.getTr(gridTable, rowIndex).removeClass("datagrid-row-checked");
		tr.find("div.datagrid-cell-check input[type=checkbox]")._propAttr("checked", false);
		tr.find("span.TrC").removeClass("TrC1");
		var dc = state.dc;
		var $viewHeader = dc.header1.add(dc.header2);
		$viewHeader.find("input[type=checkbox]")._propAttr("checked", false);
		if (opts.idField) {
			_removeRow(state.checkedRows, opts.idField, row[opts.idField]);
		}

		// COC: 级联勾选
		if (!ignoreSelect) {
			var subtree = tr.next();
			if (subtree.hasClass("treegrid-tr-tree")) {
				subtree.find("tr.datagrid-row").removeClass("datagrid-row-checked");
				subtree.find("span.TrC").removeClass("TrC1");
			}
		}

		opts.onUncheck.call(gridTable, rowIndex, row, tr);
	}

	function checkAll(gridTable, ignoreSelect) {
		var state = $d(gridTable, "datagrid");
		var opts = state.options;
		var rows;
		if (opts.finder.getRows) {
			rows = opts.finder.getRows(gridTable);
		} else {
			rows = state.data.rows;
		}

		// COC: 点击行点选；勾选为多选
		var selectedRows = state.selectedRows;
		unselectAll(gridTable);
		selectedRows.splice(0, selectedRows.length);
		// if (!ignoreSelect && opts.selectOnCheck) {
		// selectAll(gridTable, true);
		// }
		// COC: 点击行点选；勾选为多选：END!

		// V3
		// var dc = state.dc;
		// var hck = $f("input[type=checkbox]", dc.header1.add(dc.header2));
		// var bck = $f("div.datagrid-cell-check input[type=checkbox]", opts.finder.getTr(gridTable, "", "allbody"));
		// hck.add(bck)._attr("checked", true);
		// if (opts.idField) {
		// for ( var i = 0; i < rows.length; i++) {
		// _addRow(state.checkedRows, opts.idField, rows[i]);
		// }
		// }
		// opts.onCheckAll.call(gridTable, rows);

		// V4
		var dc = state.dc;
		var tr = opts.finder.getTr(gridTable, "", "allbody").addClass("datagrid-row-checked");
		var hck = dc.header1.add(dc.header2).find("input[type=checkbox]");
		var bck = tr.find("div.datagrid-cell-check input[type=checkbox]");
		hck.add(bck)._propAttr("checked", true);
		tr.find("span.TrC").addClass("TrC1");
		if (opts.idField) {
			for ( var i = 0; i < rows.length; i++) {
				_addRow(state.checkedRows, opts.idField, rows[i]);
			}
		}
		opts.onCheckAll.call(gridTable, rows);
	}

	function uncheckAll(gridTable, ignoreSelect) {
		var state = $d(gridTable, "datagrid");
		var opts = state.options;
		var rows;
		if (opts.finder.getRows) {
			rows = opts.finder.getRows(gridTable);
		} else {
			rows = state.data.rows;
		}
		if (!ignoreSelect && opts.selectOnCheck) {
			unselectAll(gridTable, true);
		}

		// V3
		// var dc = state.dc;
		// var hck = $f("input[type=checkbox]", dc.header1.add(dc.header2));
		// var bck = $f("div.datagrid-cell-check input[type=checkbox]", opts.finder.getTr(gridTable, "", "allbody"));
		// hck.add(bck)._attr("checked", false);
		// if (opts.idField) {
		// for ( var i = 0; i < rows.length; i++) {
		// _removeRow(state.checkedRows, opts.idField, rows[i][opts.idField]);
		// }
		// }
		// opts.onUncheckAll.call(gridTable, rows);

		// V4
		var dc = state.dc;
		var tr = opts.finder.getTr(gridTable, "", "checked").removeClass("datagrid-row-checked");
		var hck = dc.header1.add(dc.header2).find("input[type=checkbox]");
		var bck = tr.find("div.datagrid-cell-check input[type=checkbox]");
		hck.add(bck)._propAttr("checked", false);
		tr.find("span.TrC").removeClass("TrC1");
		if (opts.idField) {
			for ( var i = 0; i < rows.length; i++) {
				_removeRow(state.checkedRows, opts.idField, rows[i][opts.idField]);
			}
		}
		opts.onUncheckAll.call(gridTable, rows);
	}

	function clearChanged(gridTable) {
		var state = $d(gridTable, "datagrid");
		var opts = state.options;
		var data = state.data;
		var rows;
		if (opts.finder.getRows) {
			rows = opts.finder.getRows(gridTable);
		} else {
			rows = state.data.rows;
		}
		var originalRows = [];
		for ( var i = 0; i < rows.length; i++) {
			originalRows.push($.extend({}, rows[i]));
		}
		state.originalRows = originalRows;
		state.updatedRows = [];
		state.insertedRows = [];
		state.deletedRows = [];
	}

	function loadGridData(gridTable, queryParams) {
		var opts = $d(gridTable, "datagrid").options;
		if (queryParams) {
			opts.queryParams = queryParams;
		}

		// build query parameters
		var params = $.extend({}, opts.queryParams);

		// build pagination parameters
		if (opts.pagination) {
			$.extend(params, {
				pageIndex : opts.pageIndex,
				pageSize : opts.pageSize
			});
		}

		// build sort field parameters
		if (opts.sortField) {
			$.extend(params, {
				sortField : opts.sortField,
				sortOrder : opts.sortOrder
			});
		}

		// call-back onBeforeLoad function
		if (opts.onBeforeLoad.call(gridTable, params) == false) {
			return;
		}

		// show loading status message
		$(gridTable).datagrid("loading");
		setTimeout(function() {
			_loadGridData();
		}, 50);

		// load grid data
		function _loadGridData() {
			var loading = opts.loader.call(gridTable, params, function(data) {
				setTimeout(function() {
					$(gridTable).datagrid("loaded");
				}, 0);
				loadData(gridTable, data);
				setTimeout(function() {
					clearChanged(gridTable);
				}, 0);
			}, function() {
				setTimeout(function() {
					$(gridTable).datagrid("loaded");
				}, 0);
				opts.onLoadError.apply(gridTable, arguments);
			});
			if (loading == false) {
				$(gridTable).datagrid("loaded");
			}
		}

	}
	/*
	 * CURD
	 */

	var validatebox = $.fn.validatebox || false;

	function _indexOfRow(rows, row) {
		for ( var i = 0, len = rows.length; i < len; i++) {
			if (rows[i] == row) {
				return i;
			}
		}
		return -1;
	}

	function _removeRow(rows, idField, row) {
		if (typeof idField == "string") {
			for ( var i = 0, len = rows.length; i < len; i++) {
				if (rows[i][idField] == row) {
					rows.splice(i, 1);
					return;
				}
			}
		} else {
			var index = _indexOfRow(rows, idField);
			if (index != -1) {
				rows.splice(index, 1);
			}
		}
	}

	function resizeEditorWidth(gridTable) {
		var dc = $d(gridTable, "datagrid").dc;
		$f("div.datagrid-editable", dc.view).each(function() {
			var $editor = $(this);
			var field = $p($editor).attr("field");
			var col = $(gridTable).datagrid("getColumnOption", field);
			$editor.ow(col.width);
			var ed = $d(this, "datagrid.editor");
			if (ed.actions.resize) {
				ed.actions.resize(ed.target, $w($editor));
			}
		});
	}

	function beginEdit(gridTable, rowIndex) {
		var opts = $d(gridTable, "datagrid").options;
		var tr = opts.finder.getTr(gridTable, rowIndex);
		var row = opts.finder.getRow(gridTable, rowIndex);

		if ($hc("datagrid-row-editing", tr)) {
			return;
		}
		if (opts.onBeforeEdit.call(gridTable, rowIndex, row) == false) {
			return;
		}

		$ac("datagrid-row-editing", tr);
		_addRowCellEditor(gridTable, rowIndex);
		resizeEditorWidth(gridTable);
		$f("div.datagrid-editable", tr).each(function() {
			var field = $p($(this)).attr("field");
			var ed = $d(this, "datagrid.editor");
			var defaultValues = opts.defaultValues;
			if (defaultValues && defaultValues[field]) {
				if (row[field]) {
					ed.actions.setValue(ed.target, row[field]);
				} else {
					ed.actions.setValue(ed.target, defaultValues[field]);
				}
			} else {
				ed.actions.setValue(ed.target, row[field]);
			}
		});
		validateRow(gridTable, rowIndex);
	}

	function submitEdit(gridTable, rowIndex) {
		var state = $d(gridTable, "datagrid");
		var opts = state.options;
		var updatedRows = state.updatedRows;
		var insertedRows = state.insertedRows;

		var tr = opts.finder.getTr(gridTable, rowIndex);
		var row = opts.finder.getRow(gridTable, rowIndex);

		if (!$hc("datagrid-row-editing", tr)) {
			return;
		}

		if (!validateRow(gridTable, rowIndex)) {
			return;
		}

		var changed = false;
		var changedRow = {};
		$f("div.datagrid-editable", tr).each(function() {
			var field = $p($(this)).attr("field");
			var ed = $d(this, "datagrid.editor");
			var value = ed.actions.getValue(ed.target);
			if (row[field] != value) {
				row[field] = value;
				changed = true;
				changedRow[field] = value;
			}
		});
		if (changed) {
			if (_indexOfRow(insertedRows, row) == -1) {
				if (_indexOfRow(updatedRows, row) == -1) {
					updatedRows.push(row);
				}
			}
		}

		opts.onEdit.call(gridTable, rowIndex, row, changedRow);
	}

	function endEdit(gridTable, rowIndex, cancel) {
		var state = $d(gridTable, "datagrid");
		var opts = state.options;
		var updatedRows = state.updatedRows;
		var insertedRows = state.insertedRows;

		var tr = opts.finder.getTr(gridTable, rowIndex);
		var row = opts.finder.getRow(gridTable, rowIndex);
		if (!$hc("datagrid-row-editing", tr)) {
			return;
		}

		if (!cancel) {
			if (!validateRow(gridTable, rowIndex)) {
				return;
			}

			var changed = false;
			var changedRow = {};
			$f("div.datagrid-editable", tr).each(function() {
				var field = $p($(this)).attr("field");
				var ed = $d(this, "datagrid.editor");
				var value = ed.actions.getValue(ed.target);
				if (row[field] != value) {
					row[field] = value;
					changed = true;
					changedRow[field] = value;
				}
			});
			if (changed) {
				if (_indexOfRow(insertedRows, row) == -1) {
					if (_indexOfRow(updatedRows, row) == -1) {
						updatedRows.push(row);
					}
				}
			}
		}

		$rc("datagrid-row-editing", tr);
		_removeRowCellEditor(gridTable, rowIndex);
		$(gridTable).datagrid("refreshRow", rowIndex);

		if (!cancel) {
			opts.onAfterEdit.call(gridTable, rowIndex, row, changedRow);
		} else {
			opts.onCancelEdit.call(gridTable, rowIndex, row);
		}
	}

	function getEditors(gridTable, rowIndex) {
		var opts = $d(gridTable, "datagrid").options;
		var tr = opts.finder.getTr(gridTable, rowIndex);

		var editors = [];
		$c("td", tr).each(function() {
			var cell = $f("div.datagrid-editable", $(this));
			if (cell.length) {
				var ed = $d(cell[0], "datagrid.editor");
				editors.push(ed);
			}
		});

		return editors;
	}

	function getEditor(gridTable, col) {
		var editors = getEditors(gridTable, col.index);
		for ( var i = 0; i < editors.length; i++) {
			if (editors[i].field == col.field) {
				return editors[i];
			}
		}
		return null;
	}

	function _addRowCellEditor(gridTable, rowIndex) {
		var opts = $d(gridTable, "datagrid").options;
		var tr = opts.finder.getTr(gridTable, rowIndex);
		var $grid = $(gridTable);

		$c("td", tr).each(function() {
			var cell = $f("div.datagrid-cell", $(this));
			var field = $(this).attr("field");
			var col = $grid.datagrid("getColumnOption", field);
			if (!col || !col.editor) {
				return;
			}

			var editorType, editorOptions;
			if (typeof col.editor == "string") {
				editorType = col.editor;
			} else {
				editorType = col.editor.type;
				editorOptions = col.editor.options;
			}
			if (editorType != "buttons") {
				var editable = true;
				if (tr.attr("node-id")) {
					editable = col.editor.updatable;
				} else {
					editable = col.editor.insertable;
				}
				if (!editable) {
					return;
				}
			}

			var editor = opts.editors[editorType];
			if (editor) {
				var oldHtml = cell.html();
				var cellWidth = cell.ow();
				$ac("datagrid-editable", cell);
				cell.ow(cellWidth);
				cell.html('<table border="0" cellspacing="0" cellpadding="1"><tr><td></td></tr></table>');
				$c("table", cell).bind("click dblclick contextmenu", function(e) {
					var tt = $(e.target);
					var isEditSaveButton = tt.hasClass("datagrid-edit-save");
					var tr = tt.closest("tr.datagrid-row");
					rowIndex = tr.attr("datagrid-row-index");
					if (isEditSaveButton) {
						submitEdit(gridTable, rowIndex);
					} else {
						var isEditCancelButton = tt.hasClass("datagrid-edit-cancel");
						if (isEditCancelButton) {
							var options = $grid.datagrid("options");
							if (options.onEdit != $n) {
								$grid.datagrid("reload");
							} else {
								var rowIndex;
								var tr = tt.closest("tr.datagrid-row");
								if (tr.length) {
									rowIndex = tr.attr("datagrid-row-index");
								} else {
									var row = $grid.datagrid("getSelected");
									rowIndex = $grid.datagrid("getRowIndex", row);
									tr = $d(gridTable, "datagrid").options.finder.getTr(gridTable, rowIndex);
								}

								if ($(tr).attr("node-id")) {
									endEdit(gridTable, rowIndex, true);
								} else {
									deleteRow(gridTable, rowIndex);
								}
								resetRowIndex(gridTable);
							}
						}
					}
					e.stopPropagation();
				});
				$d(cell[0], "datagrid.editor", {
					actions : editor,
					target : editor.init($f("td", cell), editorOptions, opts),
					field : field,
					type : editorType,
					oldHtml : oldHtml
				});
			}
		});
		$grid.datagrid("fixRowHeight", {
			rowIndex : rowIndex,
			syncFrozen : true
		});
	}

	function _removeRowCellEditor(gridTable, rowIndex) {
		var opts = $d(gridTable, "datagrid").options;
		var tr = opts.finder.getTr(gridTable, rowIndex);
		$c("td", tr).each(function() {
			var cell = $f("div.datagrid-editable", $(this));
			if (cell.length) {
				var ed = $d(cell[0], "datagrid.editor");
				if (ed.actions.destroy) {
					ed.actions.destroy(ed.target);
				}
				cell.html(ed.oldHtml);
				$.removeData(cell[0], "datagrid.editor");
				$rc("datagrid-editable", cell);
				cell.css("width", "");
			}
		});
	}

	function validateRow(gridTable, rowIndex) {
		var tr = $d(gridTable, "datagrid").options.finder.getTr(gridTable, rowIndex);
		if (!$hc("datagrid-row-editing", tr)) {
			return true;
		}
		var vbox = $f(".validatebox-text", tr);
		if (validatebox)
			vbox.validatebox("validate");
		vbox.trigger("mouseleave");
		var $invalid = $f(".validatebox-invalid", tr);
		return $invalid.length == 0;
	}

	function getChangedRows(gridTable, changedType) {
		var state = $d(gridTable, "datagrid");
		var insertedRows = state.insertedRows;
		var deletedRows = state.deletedRows;
		var updatedRows = state.updatedRows;
		if (!changedType) {
			var rows = [];
			rows = rows.concat(insertedRows);
			rows = rows.concat(deletedRows);
			rows = rows.concat(updatedRows);
			return rows;
		} else {
			if (changedType == "inserted") {
				return insertedRows;
			} else if (changedType == "deleted") {
				return deletedRows;
			} else if (changedType == "updated") {
				return updatedRows;
			}

		}
		return [];
	}

	function getChangedDatas(gridTable) {

		function _getRowIndex(tr) {
			if (tr.attr("datagrid-row-index")) {
				return parseInt(tr.attr("datagrid-row-index"));
			} else {
				return tr.attr("node-id");
			}
		}

		var state = $d(gridTable, "datagrid");
		var opts = state.options;
		var changedDatas = [];

		var panel = $(gridTable).datagrid("getPanel");

		panel.find(".datagrid-view2").find("tr.datagrid-row-editing").each(function() {
			var $tr = $(this);
			var rowIndex = _getRowIndex($tr);
			var row = opts.finder.getRow(gridTable, rowIndex);
			var newRow = {};
			newRow["id"] = row["id"];
			$f("div.datagrid-editable", $tr).each(function() {
				var field = $p($(this)).attr("field");
				var col = $(gridTable).datagrid("getColumnOption", field);
				if (col && col.editor) {
					var editorType, editorOptions;
					if (typeof col.editor == "string") {
						editorType = col.editor;
					} else {
						editorType = col.editor.type;
						editorOptions = col.editor.options;
					}
					if (editorType != "buttons") {
						var editable = true;
						if ($tr.attr("node-id")) {
							editable = col.editor.updatable;
						} else {
							editable = col.editor.insertable;
						}
						if (editable) {
							var ed = $d(this, "datagrid.editor");
							var value = ed.actions.getValue(ed.target);
							if (row[field] != value) {
								row[field] = value;
							}
							newRow[field] = row[field];
						}
					}
				}
			});
			changedDatas.push(newRow);
		});
		return changedDatas;
	}

	function resetRowIndex(gridTable) {
		var state = $d(gridTable, "datagrid");
		var $grid = $(gridTable);
		var panel = $(gridTable).datagrid("getPanel");
		var i = 0;
		panel.find(".datagrid-view1").find("tr.datagrid-row").each(function() {
			var trID = state.rowIdPrefix + "-" + (true ? 1 : 2) + "-" + i;
			$(this).attr("id", trID);
			$(this).attr("datagrid-row-index", i);
			$f("div.datagrid-cell-rownumber", $(this)).text(i + 1);
			i++;
		});
		i = 0;
		panel.find(".datagrid-view2").find("tr.datagrid-row").each(function() {
			var trID = state.rowIdPrefix + "-" + (false ? 1 : 2) + "-" + i;
			$(this).attr("id", trID);
			$(this).attr("datagrid-row-index", i);
			i++;
		});
	}

	function insertRow(gridTable, rowConfig) {
		var data = $d(gridTable, "datagrid").data;
		var view = $d(gridTable, "datagrid").options.view;
		var insertedRows = $d(gridTable, "datagrid").insertedRows;
		view.insertRow.call(view, gridTable, rowConfig.index, rowConfig.row);
		insertedRows.push(rowConfig.row);
		$(gridTable).datagrid("getPager").pagination("refresh", {
			total : data.total
		});
	}

	function appendRow(gridTable, row) {
		var data = $d(gridTable, "datagrid").data;
		var view = $d(gridTable, "datagrid").options.view;
		var insertedRows = $d(gridTable, "datagrid").insertedRows;
		view.insertRow.call(view, gridTable, null, row);
		insertedRows.push(row);
		$(gridTable).datagrid("getPager").pagination("refresh", {
			total : data.total
		});
	}

	function deleteRow(gridTable, rowIndex) {
		var state = $d(gridTable, "datagrid");
		var opts = state.options;
		var data = state.data;
		var insertedRows = state.insertedRows;
		var deletedRows = state.deletedRows;
		var $grid = $(gridTable).datagrid("cancelEdit", rowIndex);

		var row = data.rows[rowIndex];
		if (_indexOfRow(insertedRows, row) >= 0) {
			_removeRow(insertedRows, row);
		} else {
			deletedRows.push(row);
		}
		_removeRow(state.selectedRows, opts.idField, data.rows[rowIndex][opts.idField]);
		_removeRow(state.checkedRows, opts.idField, data.rows[rowIndex][opts.idField]);
		opts.view.deleteRow.call(opts.view, gridTable, rowIndex);
		if (opts.height == "auto") {
			$grid.datagrid("fixRowHeight");
		}
		$grid.datagrid("getPager").pagination("refresh", {
			total : data.total
		});
	}

	function acceptChanges(gridTable) {
		var data = $d(gridTable, "datagrid").data;
		var ok = true;
		for ( var i = 0, len = data.rows.length; i < len; i++) {
			if (validateRow(gridTable, i)) {
				endEdit(gridTable, i, false);
			} else {
				ok = false;
			}
		}
		if (ok) {
			$(gridTable).datagrid("clearChanged");
		}
	}

	function rejectChanges(gridTable) {
		var state = $d(gridTable, "datagrid");
		var opts = state.options;
		var originalRows = state.originalRows;
		var insertedRows = state.insertedRows;
		var deletedRows = state.deletedRows;
		var selectedRows = state.selectedRows;
		var checkedRows = state.checkedRows;
		var data = state.data;
		var $grid = $(gridTable);

		function _getIds(rows) {
			var ids = [];
			for ( var i = 0; i < rows.length; i++) {
				ids.push(rows[i][opts.idField]);
			}
			return ids;
		}

		function _selectRow(ids, type) {
			for ( var i = 0; i < ids.length; i++) {
				var rowIndex = $grid.datagrid("getRowIndex", ids[i]);
				$grid.datagrid((type == "s" ? "selectRow" : "checkRow"), {
					rowIndex : rowIndex,
					ignoreCheck : true
				});
			}
		}

		for ( var i = 0; i < data.rows.length; i++) {
			endEdit(gridTable, i, true);
		}
		var selectedIds = _getIds(selectedRows);
		var checkedIds = _getIds(checkedRows);
		selectedRows.splice(0, selectedRows.length);
		checkedRows.splice(0, checkedRows.length);
		data.total += deletedRows.length - insertedRows.length;
		data.rows = originalRows;
		$grid.datagrid("loadData", data);
		_selectRow(selectedIds, "s");
		_selectRow(checkedIds, "c");
		$(gridTable).datagrid("clearChanged");
	}
	var editors = {
		text : {
			init : function(td, options) {
				var $editable = $('<input type="text" class="datagrid-editable-input">').appendTo(td);
				return $editable;
			},
			getValue : function(input) {
				return $(input).val();
			},
			setValue : function(input, value) {
				$(input).val(value);
			},
			resize : function(input, w) {
				$(input).ow(w);
			}
		},
		textarea : {
			init : function(td, options) {
				var $editable = $('<textarea class="datagrid-editable-input"></textarea>').appendTo(td);
				return $editable;
			},
			getValue : function(input) {
				return $(input).val();
			},
			setValue : function(input, v) {
				$(input).val(v);
			},
			resize : function(input, w) {
				$(input).ow(w);
			}
		},
		checkbox : {
			init : function(td, options) {
				var $editor = $('<input type="checkbox">').appendTo(td);
				$editor.val(options.on);
				$editor.attr("offval", options.off);
				return $editor;
			},
			getValue : function(input) {
				if ($(input).is(":checked")) {
					return $(input).val();
				} else {
					return $(input).attr("offval");
				}
			},
			setValue : function(input, v) {
				var checked = false;
				if ($(input).val() == v) {
					checked = true;
				}
				$(input)._attr("checked", checked);
			}
		},
		numberbox : {
			init : function(td, options) {
				var $editor = $('<input type="text" class="datagrid-editable-input">').appendTo(td);
				$editor.numberbox(options);
				return $editor;
			},
			destroy : function(input) {
				$(input).numberbox("destroy");
			},
			getValue : function(input) {
				$(input).blur();
				return $(input).numberbox("getValue");
			},
			setValue : function(input, value) {
				$(input).numberbox("setValue", value);
			},
			resize : function(input, width) {
				$(input).ow(width);
			}
		},
		validatebox : {
			init : function(td, options) {
				var $editor = $('<input type="text" class="datagrid-editable-input">').appendTo(td);
				if (validatebox)
					$editor.validatebox(options);
				return $editor;
			},
			destroy : function(input) {
				if (validatebox)
					$(input).validatebox("destroy");
			},
			getValue : function(input) {
				return $(input).val();
			},
			setValue : function(input, value) {
				$(input).val(value);
			},
			resize : function(input, width) {
				$(input).ow(width);
			}
		},
		combodate : {
			init : function(td, options) {
				var $editor = $('<input type="text"/>').appendTo(td);
				$editor.combodate(options);
				return $editor;
			},
			destroy : function(input) {
				$(input).combodate("destroy");
			},
			getValue : function(input) {
				return $(input).combodate("getValue");
			},
			setValue : function(input, value) {
				$(input).combodate("setValue", value);
			},
			resize : function(input, width) {
				$(input).combodate("resize", width);
			}
		},
		combodatetime : {
			init : function(td, options) {
				var $editor = $('<input type="text"/>').appendTo(td);
				$editor.combodatetime(options);
				return $editor;
			},
			destroy : function(input) {
				$(input).combodatetime("destroy");
			},
			getValue : function(input) {
				return $(input).combodatetime("getValue");
			},
			setValue : function(input, value) {
				$(input).combodatetime("setValue", value);
			},
			resize : function(input, width) {
				$(input).combodatetime("resize", width);
			}
		},
		combobox : {
			init : function(td, options) {
				var $editor = $('<input type="text">').appendTo(td);
				$editor.combobox(options || {});
				return $editor;
			},
			destroy : function(input) {
				$(input).combobox("destroy");
			},
			getValue : function(input) {
				return $(input).combobox("getValue");
			},
			setValue : function(input, value) {
				var $input = $(input);
				if (typeof value == "object") {
					$input.combobox("setValue", value.value || "");
					$input.combobox("setText", value.text || "");
				} else {
					$input.combobox("setValue", value);
				}
			},
			resize : function(input, width) {
				$(input).combobox("resize", width);
			}
		},
		combotree : {
			init : function(td, options) {
				var $editor = $('<input type="text">').appendTo(td);
				$editor.combotree(options);
				return $editor;
			},
			destroy : function(input) {
				$(input).combotree("destroy");
			},
			getValue : function(input) {
				return $(input).combotree("getValue");
			},
			setValue : function(input, value) {
				var $input = $(input);
				if (typeof value == "object") {
					$input.combotree("setValue", value.value || "");
					$input.combotree("setText", value.text || "");
				} else {
					$input.combotree("setValue", value);
				}
			},
			resize : function(input, width) {
				$(input).combotree("resize", width);
			}
		},
		buttons : {
			init : function(td, options, gridOptions) {
				var html = '<a href="javascript: void(0)" class="datagrid-edit-btn datagrid-edit-cancel">' + $.fn.datagrid.defaults.editCancelButton + '</a>';
				if (gridOptions.onEdit != $n) {
					html = '<a href="javascript: void(0)" class="datagrid-edit-btn datagrid-edit-save">' + $.fn.datagrid.defaults.editSaveButton + '</a>&nbsp;' + html;
				}
				var $editor = $(html).appendTo(td);
				return $editor;
			},
			destroy : function(input) {
			},
			getValue : function(input) {
				return null;
			},
			setValue : function(input, value) {
			},
			resize : function(input, width) {
			}
		}
	};

	$.fn.datagrid = function(options, args) {
		if (typeof options == "string") {
			var fn = $.fn.datagrid.methods[options];
			if (fn)
				return fn(this, args);
			else
				$.error('The method ' + options + ' does not exist in $.fn.datagrid');
		}
		options = options || {};
		return this.each(function() {
			// var time0 = new Date().getTime();

			var state = $d(this, "datagrid");
			var opts;
			if (state) {
				opts = $.extend(state.options, options);
				state.options = opts;
			} else {
				opts = $.extend({}, $.extend({}, $.fn.datagrid.defaults, {
					queryParams : {}
				}), $.fn.datagrid.parseOptions(this), options);

				$(this).css("width", "").css("height", "");

				var wrapObj = _init(this, opts.rownumbers);

				if (!opts.columns) {
					opts.columns = wrapObj.columns;
				}
				if (!opts.frozenColumns) {
					opts.frozenColumns = wrapObj.frozenColumns;
				}
				opts.columns = $.extend(true, [], opts.columns);
				opts.frozenColumns = $.extend(true, [], opts.frozenColumns);
				opts.view = $.extend({}, opts.view);

				$d(this, "datagrid", {
					options : opts,
					panel : wrapObj.panel,
					dc : wrapObj.dc,
					selectedRows : opts.checkedRows || [],
					checkedRows : opts.checkedRows || [],
					data : {
						total : 0,
						rows : []
					},
					originalRows : [],
					updatedRows : [],
					insertedRows : [],
					deletedRows : []
				});
			}

			_initGridModel(this);

			if (opts.data) {
				loadData(this, opts.data);
				clearChanged(this);
			} else {
				var data = _parseGridData(this);
				if (data.total > 0) {
					loadData(this, data);
					clearChanged(this);
				}
			}

			resizePanel(this);
			loadGridData(this);
			_bindGridEvents(this);

			// $log("ui.datagrid: created (title = {1})", time0, this.title || "");
		});

	};

	$.fn.datagrid.methods = {
		options : function(jq) {
			var state = $d(jq[0], "datagrid");
			var opts = state.options;
			var panelOpts = state.panel.panel("options");
			opts = $.extend(opts, {
				width : panelOpts.width,
				height : panelOpts.height,
				closed : panelOpts.closed,
				collapsed : panelOpts.collapsed,
				minimized : panelOpts.minimized,
				maximized : panelOpts.maximized
			});
			return opts;
		},
		setSelectionState : function(jq) {
			return jq.each(function() {
				setSelectionState(this);
			});
		},
		getPanel : function(jq) {
			return $d(jq[0], "datagrid").panel;
		},
		getPager : function(jq) {
			return $c("div.datagrid-pager", $d(jq[0], "datagrid").panel);
		},
		/**
		 * arg: frozen -
		 */
		getColumnFields : $x(getColumnFields),
		/**
		 * arg: field -
		 */
		getColumnOption : $x(getColumnOption),
		getColumnOptions : function(jq) {
			var state = $d(jq[0], "datagrid");
			return state.options.columns[0];
		},
		/**
		 * arg: size -
		 */
		resize : $X(resizePanel),
		/**
		 * Load data with query parameters
		 */
		load : function(jq, queryParams) {
			return jq.each(function() {
				var opts = $(this).datagrid("options");
				opts.pageIndex = 1;
				var $pager = $(this).datagrid("getPager");
				$pager.pagination({
					pageIndex : 1
				});
				loadGridData(this, queryParams);
			});
		},
		/**
		 * arg: queryParams -
		 */
		reload : $X(loadGridData),
		/**
		 * Reload datagrid footer
		 */
		reloadFooter : function(jq, footer) {
			return jq.each(function() {
				var opts = $d(this, "datagrid").options;
				var dc = $d(this, "datagrid").dc;
				if (footer) {
					$d(this, "datagrid").footer = footer;
				}
				if (opts.showFooter) {
					opts.view.renderFooter.call(opts.view, this, dc.footer2, false);
					opts.view.renderFooter.call(opts.view, this, dc.footer1, true);
					if (opts.view.onAfterRender) {
						opts.view.onAfterRender.call(opts.view, this);
					}
					$(this).datagrid("fixRowHeight");
				}
			});
		},
		loading : function(jq) {
			return jq.each(function() {
				var opts = $d(this, "datagrid").options;
				$(this).datagrid("getPager").pagination("loading");
				if (opts.loadMsg) {
					var $panel = $(this).datagrid("getPanel");
					$('<div class="datagrid-mask" style="display:block;"></div>').appendTo($panel);
					var msg = $('<div class="datagrid-mask-msg" style="display:block;left:50%"></div>').html(opts.loadMsg).appendTo($panel);
					msg.css("marginLeft", -$ow(msg) / 2);
				}
			});
		},
		loaded : function(jq) {
			return jq.each(function() {
				$(this).datagrid("getPager").pagination("loaded");
				var $panel = $(this).datagrid("getPanel");
				$c("div.datagrid-mask-msg", $panel).remove();
				$c("div.datagrid-mask", $panel).remove();
			});
		},
		fitColumns : $X(fitColumnsWidth),
		/**
		 * arg: field -
		 */
		fixColumnSize : $X(fixColumnSize),
		/**
		 * arg: rowIndex -
		 */
		fixRowHeight : $X(fixRowHeight),
		/**
		 * arg: rowIndex -
		 */
		freezeRow : $X(freezeRow),
		/**
		 * arg: rows - row numbers
		 */
		freezeRows : function(jq, rows) {
			return jq.each(function() {
				for ( var i = 0; i < rows; i++) {
					freezeRow(jq[0], i);
				}
			});
		},
		/**
		 * arg: field -
		 */
		autoSizeColumn : $X(autoSizeColumn),
		loadData : function(jq, data) {
			return jq.each(function() {
				loadData(this, data);
				clearChanged(this);
			});
		},
		getData : function(jq) {
			return $d(jq[0], "datagrid").data;
		},
		getRows : function(jq) {
			return $d(jq[0], "datagrid").data.rows;
		},
		getFooterRows : function(jq) {
			return $d(jq[0], "datagrid").footer;
		},
		/**
		 * arg: rowOrId -
		 */
		getRowIndex : $x(getRowIndex),
		getChecked : $x(getCheckedRows),
		getSelected : function(jq) {
			var rows = getSelectedRows(jq[0]);
			return rows.length > 0 ? rows[0] : null;
		},
		getSelections : $x(getSelectedRows),
		clearSelections : function(jq) {
			return jq.each(function() {
				var selectedRows = $d(this, "datagrid").selectedRows;
				selectedRows.splice(0, selectedRows.length);
				unselectAll(this);
			});
		},
		clearChecked : function(jq) {
			return jq.each(function() {
				var state = $.data(this, "datagrid");
				var selectedRows = state.selectedRows;
				var checkedRows = state.checkedRows;
				checkedRows.splice(0, checkedRows.length);
				unselectAll(this);
				if (state.options.selectOnCheck) {
					selectedRows.splice(0, selectedRows.length);
				}
			});
		},
		clearChanged : $X(clearChanged),
		selectAll : $X(selectAll),
		unselectAll : $X(unselectAll),
		/**
		 * arg: rowIndex -
		 */
		selectRow : $X(selectRow),
		/**
		 * arg: rowOrId -
		 */
		selectRecord : $X(selectRecord),
		/**
		 * arg: rowIndex -
		 */
		unselectRow : $X(unselectRow),
		/**
		 * arg: rowIndex -
		 */
		checkRow : $X(checkRow),
		/**
		 * arg: rowIndex -
		 */
		uncheckRow : $X(uncheckRow),
		checkAll : $X(checkAll),
		uncheckAll : $X(uncheckAll),
		showColumn : function(jq, field) {
			return jq.each(function() {
				var $panel = $(this).datagrid("getPanel");
				$f('td[field="' + field + '"]', $panel).show();
				$(this).datagrid("getColumnOption", field).hidden = false;
				$(this).datagrid("fitColumns");
			});
		},
		hideColumn : function(jq, field) {
			return jq.each(function() {
				var $panel = $(this).datagrid("getPanel");
				$f('td[field="' + field + '"]', $panel).hide();
				$(this).datagrid("getColumnOption", field).hidden = true;
				$(this).datagrid("fitColumns");
			});
		},
		/**
		 * arg: cellConfig -
		 */
		mergeCells : $X(mergeCells),
		/*
		 * CURD
		 */
		resizeEditor : $X(resizeEditorWidth),
		refreshRow : function(jq, rowData) {
			return jq.each(function() {
				var opts = $d(this, "datagrid").options;
				opts.view.refreshRow.call(opts.view, this, rowData);
			});
		},
		validateRow : $x(validateRow),
		updateRow : function(jq, rowConfig) {
			return jq.each(function() {
				var opts = $d(this, "datagrid").options;
				opts.view.updateRow.call(opts.view, this, rowConfig.index, rowConfig.row);
			});
		},
		/**
		 * arg: rowData -
		 */
		appendRow : $X(appendRow),
		/**
		 * arg: rowData -
		 */
		insertRow : $X(insertRow),
		/**
		 * arg: rowIndex -
		 */
		deleteRow : $X(deleteRow),
		/**
		 * arg: changedType -
		 */
		getChanges : $x(getChangedRows),
		getChangeDatas : $x(getChangedDatas),
		acceptChanges : $X(acceptChanges),
		rejectChanges : $X(rejectChanges),
		/**
		 * arg: rowIndex -
		 */
		beginEdit : $X(beginEdit),
		/**
		 * arg: rowIndex -
		 */
		endEdit : $X(endEdit, false),
		/**
		 * arg: rowIndex -
		 */
		cancelEdit : $X(endEdit, true),
		/**
		 * arg: rowIndex -
		 */
		getEditors : $x(getEditors),
		/**
		 * arg: col -
		 */
		getEditor : $x(getEditor)
	};

	$.fn.datagrid.parseOptions = function(gridTable) {
		var t = $(gridTable);
		return $.extend({}, $.fn.panel.parseOptions(gridTable), jCocit.parseOptions(gridTable, [ "url", "toolbar", "idField", "sortField", "sortOrder", "paginationPos", "resizeHandle", {
			fitColumns : "b",
			autoRowHeight : "b",
			striped : "b",
			nowrap : "b"
		}, {
			rownumbers : "b",
			singleSelect : "b",
			noSelect : "b",
			checkOnSelect : "b",
			selectOnCheck : "b"
		}, {
			pagination : "b",
			pageSize : "n",
			pageIndex : "n"
		}, {
			sortOnRemote : "b",
			showHeader : "b",
			showFooter : "b"
		}, {
			scrollbarSize : "n"
		} ]), {
			pageOptions : (t.attr("pageOptions") ? $fn(t.attr("pageOptions")) : undefined),
			loadMsg : (t.attr("loadMsg") != undefined ? t.attr("loadMsg") : undefined),
			rowStyler : (t.attr("rowStyler") ? $fn(t.attr("rowStyler")) : undefined)
		});
	};

	var viewRenders = {
		render : function(gridTable, $body, frozen) {
			var state = $d(gridTable, "datagrid");
			var opts = state.options;
			var rows = state.data.rows;
			var fields = $(gridTable).datagrid("getColumnFields", frozen);
			if (frozen) {
				if (!(opts.rownumbers || (opts.frozenColumns && opts.frozenColumns.length))) {
					return;
				}
			}
			var $bodyTable = [ '<table class="datagrid-btable" cellspacing="0" cellpadding="0" border="0"><tbody>' ];
			for ( var i = 0; i < rows.length; i++) {
				var cls = (i % 2 && opts.striped) ? 'class="datagrid-row"' : 'class="datagrid-row datagrid-row-alt"';
				var styleText = opts.rowStyler ? opts.rowStyler.call(gridTable, i, rows[i]) : "";
				var styleAttr = styleText ? 'style="' + styleText + '"' : '';
				var trID = state.rowIdPrefix + "-" + (frozen ? 1 : 2) + "-" + i;
				$bodyTable.push('<tr id="' + trID + '" node-id="' + rows[i].id + '" datagrid-row-index="' + i + '" ' + cls + ' ' + styleAttr + '>');
				$bodyTable.push(this.renderRow.call(this, gridTable, fields, frozen, i, rows[i]));
				$bodyTable.push("</tr>");
			}
			$bodyTable.push("</tbody></table>");
			$($body).html($bodyTable.join(""));
		},
		renderFooter : function(gridTable, $footer, frozen) {
			var opts = $d(gridTable, "datagrid").options;
			var rows = $d(gridTable, "datagrid").footer || [];
			var fields = $(gridTable).datagrid("getColumnFields", frozen);
			var $footerTable = [ '<table class="datagrid-ftable" cellspacing="0" cellpadding="0" border="0"><tbody>' ];
			for ( var i = 0; i < rows.length; i++) {
				$footerTable.push('<tr class="datagrid-row" datagrid-row-index="' + i + '">');
				$footerTable.push(this.renderRow.call(this, gridTable, fields, frozen, i, rows[i]));
				$footerTable.push("</tr>");
			}
			$footerTable.push("</tbody></table>");
			$($footer).html($footerTable.join(""));
		},
		renderRow : function(gridTable, fields, frozen, rowIndex, row) {
			var state = $d(gridTable, "datagrid");
			var opts = state.options;
			var cc = [];
			if (frozen && opts.rownumbers) {
				var rownumber = rowIndex + 1;
				if (opts.pagination) {
					rownumber += (opts.pageIndex - 1) * opts.pageSize;
				}
				var rownumberMax = opts.pageIndex * opts.pageSize;
				var rownumberWidth = 8 + ("" + rownumberMax).length * 7;
				var $rownumbers = $f("div.datagrid-header-rownumber", state.dc.header1);
				$w(rownumberWidth, $rownumbers);
				cc.push('<td class="datagrid-td-rownumber"><div class="datagrid-cell-rownumber" style="width: ' + rownumberWidth + 'px;">' + rownumber + '</div></td>');
			}
			for ( var i = 0; i < fields.length; i++) {
				var field = fields[i];
				var col = $(gridTable).datagrid("getColumnOption", field);
				if (col) {
					var fieldValue = row[field];
					var styleText = col.styler ? (col.styler(fieldValue, row, rowIndex) || "") : "";
					var styleAttr = col.hidden ? 'style="display:none;' + styleText + '"' : (styleText ? 'style="' + styleText + '"' : "");
					cc.push('<td field="' + field + '" ' + styleAttr + '>');
					if (col.checkbox) {
						var styleAttr = "";
					} else {
						var styleAttr = "";
						if (col.align) {
							styleAttr += "text-align:" + col.align + ";";
						}
						if (!opts.nowrap) {
							styleAttr += "white-space:normal;height:auto;";
						} else {
							if (opts.autoRowHeight) {
								styleAttr += "height:auto;";
							}
						}
					}
					cc.push('<div style="' + styleAttr + '" ');
					if (col.checkbox) {
						cc.push('class="datagrid-cell-check ');
					} else {
						cc.push('class="datagrid-cell ' + col.cellClass);
					}
					if (col.showTips) {
						cc.push(' text-ellipsis" title="' + ($(fieldValue).text() || fieldValue) + '"');
					} else {
						cc.push('"');
					}
					cc.push('>');
					if (col.checkbox) {
						cc.push('<input type="checkbox" name="' + field + '" value="' + (fieldValue != undefined ? fieldValue : "") + '"/>');
					} else {
						if (col.formatter) {
							cc.push(col.formatter(fieldValue, row, rowIndex));
						} else {
							if (typeof fieldValue == "object") {
								cc.push(fieldValue.text);
							} else {
								cc.push(fieldValue);
							}
						}
					}
					cc.push("</div>");
					cc.push("</td>");
				}
			}
			return cc.join("");
		},
		onBeforeRender : function(gridTable, rows) {
		},
		onAfterRender : function(gridTable) {
			var opts = $d(gridTable, "datagrid").options;
			if (opts.showFooter) {
				var $footer = $f("div.datagrid-footer", $(gridTable).datagrid("getPanel"));
				$f("div.datagrid-cell-rownumber,div.datagrid-cell-check", $footer).css("visibility", "hidden");
			}
		},
		/*
		 * CURD
		 */
		refreshRow : function(gridTable, rowIndex) {
			this.updateRow.call(this, gridTable, rowIndex, {});
		},
		updateRow : function(gridTable, rowIndex, row) {
			var opts = $d(gridTable, "datagrid").options;
			var rows = $(gridTable).datagrid("getRows");
			$.extend(rows[rowIndex], row);
			var styleText = opts.rowStyler ? opts.rowStyler.call(gridTable, rowIndex, rows[rowIndex]) : "";

			function _updateRow(frozen) {
				var fields = $(gridTable).datagrid("getColumnFields", frozen);
				var tr = opts.finder.getTr(gridTable, rowIndex, "body", (frozen ? 1 : 2));
				var $checked = $f("div.datagrid-cell-check input[type=checkbox]", tr).is(":checked");
				tr.html(this.renderRow.call(this, gridTable, fields, frozen, rowIndex, rows[rowIndex]));
				tr.attr("style", styleText || "");
				if ($checked) {
					$f("div.datagrid-cell-check input[type=checkbox]", tr)._attr("checked", true);
				}
			}

			_updateRow.call(this, true);
			_updateRow.call(this, false);
			$(gridTable).datagrid("fixRowHeight", rowIndex);
		},
		insertRow : function(gridTable, rowIndex, row) {
			var state = $d(gridTable, "datagrid");
			var opts = state.options;
			var dc = state.dc;
			var data = state.data;
			if (rowIndex == undefined || rowIndex == null) {
				rowIndex = data.rows.length;
			}
			if (rowIndex > data.rows.length) {
				rowIndex = data.rows.length;
			}

			function _updateRownumbers(frozen) {
				var viewIndex = frozen ? 1 : 2;
				for ( var i = data.rows.length - 1; i >= rowIndex; i--) {
					var tr = opts.finder.getTr(gridTable, i, "body", viewIndex);
					tr.attr("datagrid-row-index", i + 1);
					tr.attr("id", state.rowIdPrefix + "-" + viewIndex + "-" + (i + 1));
					if (frozen && opts.rownumbers) {
						var rownumber = i + 2;
						if (opts.pagination) {
							rownumber += (opts.pageIndex - 1) * opts.pageSize;
						}
						$f("div.datagrid-cell-rownumber", tr).html(rownumber);
					}
				}
			}

			function _insertRow(frozen) {
				var viewIndex = frozen ? 1 : 2;
				var fields = $(gridTable).datagrid("getColumnFields", frozen);
				var rowId = state.rowIdPrefix + "-" + viewIndex + "-" + rowIndex;
				var tr = '<tr id="' + rowId + '" class="datagrid-row" datagrid-row-index="' + rowIndex + '"></tr>';
				if (rowIndex >= data.rows.length) {
					if (data.rows.length) {
						opts.finder.getTr(gridTable, "", "last", viewIndex).after(tr);
					} else {
						var $body = frozen ? dc.body1 : dc.body2;
						$body.html('<table cellspacing="0" cellpadding="0" border="0"><tbody>' + tr + '</tbody></table>');
					}
				} else {
					opts.finder.getTr(gridTable, rowIndex + 1, "body", viewIndex).before(tr);
				}
			}

			_updateRownumbers.call(this, true);
			_updateRownumbers.call(this, false);
			_insertRow.call(this, true);
			_insertRow.call(this, false);
			data.total += 1;
			data.rows.splice(rowIndex, 0, row);
			this.refreshRow.call(this, gridTable, rowIndex);
		},
		deleteRow : function(gridTable, rowIndex) {
			var state = $d(gridTable, "datagrid");
			var opts = state.options;
			var data = state.data;

			function _updateRownumbers(frozen) {
				var viewIndex = frozen ? 1 : 2;
				for ( var i = rowIndex + 1; i < data.rows.length; i++) {
					var tr = opts.finder.getTr(gridTable, i, "body", viewIndex);
					tr.attr("datagrid-row-index", i - 1);
					tr.attr("id", state.rowIdPrefix + "-" + viewIndex + "-" + (i - 1));
					if (frozen && opts.rownumbers) {
						var rownumber = i;
						if (opts.pagination) {
							rownumber += (opts.pageIndex - 1) * opts.pageSize;
						}
						$f("div.datagrid-cell-rownumber", tr).html(rownumber);
					}
				}
			}

			opts.finder.getTr(gridTable, rowIndex).remove();
			_updateRownumbers.call(this, true);
			_updateRownumbers.call(this, false);
			data.total -= 1;
			data.rows.splice(rowIndex, 1);
		}
	};

	$.fn.datagrid.defaults = $.extend({}, $.fn.panel.defaults, {
		/**
		 * frozen columns array: each element of frozenColumns are cols array indicate a row of header which each element is cell of header.
		 */
		frozenColumns : undefined,
		/**
		 * columns array: each element of columns are cols array indicate a row of header which each element is cell of header.
		 */
		columns : undefined,
		/**
		 * fitColumns is true means that the columns will be fit to blank space of data grid panel content.
		 */
		fitColumns : false,
		/**
		 * resize specified border of header cell, the column width will be changed.
		 */
		resizeHandle : "right",
		autoRowHeight : true,
		/**
		 * data grid tool bar
		 */
		toolbar : null,
		/**
		 * striped is true means that the data row style will be shown alternately.
		 */
		striped : true,
		method : "post",
		nowrap : true,
		/**
		 * id field name used to identify data row.
		 */
		idField : null,
		/**
		 * true means to hold selected or checked status base on the current page.
		 */
		WYSIWYG : true,
		/**
		 * remote url used to ajax load grid data.
		 */
		url : null,
		/**
		 * grid data
		 */
		data : null,
		loadMsg : "Processing, please wait ...",
		/**
		 * rownumbers is true means that the a sequence number will be shown before data row.
		 */
		rownumbers : false,
		/**
		 * true means that the datagrid only support single selection.
		 */
		singleSelect : false,
		/**
		 * true means that the data row will be selected when checkbox is checked.
		 */
		selectOnCheck : true,
		/**
		 * true means that the data row checkbox will be checked when row is selected.
		 */
		checkOnSelect : true,
		/**
		 * true means that the pagination bar will be shown in position specified by "paginationPos" of data grid.
		 */
		pagination : false,
		/**
		 * pagination position
		 */
		paginationPos : "bottom",
		/**
		 * data page index, from 1 to n
		 */
		pageIndex : 1,
		/**
		 * page size means how many records will be shown in datagrid.
		 */
		pageSize : 50,
		/**
		 * page options will be shown in the drop-down list of pagination bar.
		 */
		pageOptions : [ 10, 20, 50, 100, 200 ],
		/**
		 * query parameters used to query data from remote server.
		 */
		queryParams : {},
		/**
		 * sort field means that the grid data will be sorted by specified field.
		 */
		sortField : null,
		/**
		 * sort order means that the grid data will be sorted by specified order, options are "asc/desc"
		 */
		sortOrder : "asc",
		/**
		 * true means that the sort action will be finished on remote side.
		 */
		sortOnRemote : true,
		/**
		 * true means that the grid header fields name will be shown
		 */
		showHeader : true,
		/**
		 * true means that the grid footer summary data will be shown.
		 */
		showFooter : false,
		/**
		 * scroll bar width used to fit columns evaluate grid blank space.
		 */
		scrollbarSize : 21,
		editSaveButton : 'Ok',
		editCancelButton : 'Cancel',
		/**
		 * rowIndex, rowData
		 */
		rowStyler : $n,
		loader : function(queryParams, _doLoadSuccess, _doLoadError) {
			var opts = $(this).datagrid("options");
			if (!opts.url) {
				return false;
			}
			$.doAjax({
				type : opts.method,
				url : opts.url,
				data : queryParams,
				dataType : "json",
				success : function(data) {
					_doLoadSuccess(data);
					// },
					// error : function() {
					// _doLoadError.apply(this, arguments);
				}
			});
		},
		loadFilter : function(data) {
			// data is array means data is rows
			if (typeof data.length == "number" && typeof data.splice == "function") {
				return {
					total : data.length,
					rows : data
				};
			} else {
				return data;
			}
		},
		finder : {
			getTr : function(gridTable, rowIndex, type, viewIndex) {
				type = type || "body";
				viewIndex = viewIndex || 0;
				var _238 = $.data(gridTable, "datagrid");
				var dc = _238.dc;
				var opts = _238.options;
				if (viewIndex == 0) {
					var tr1 = opts.finder.getTr(gridTable, rowIndex, type, 1);
					var tr2 = opts.finder.getTr(gridTable, rowIndex, type, 2);
					return tr1.add(tr2);
				} else {
					if (type == "body") {
						var tr = $("#" + _238.rowIdPrefix + "-" + viewIndex + "-" + rowIndex);
						if (!tr.length) {
							tr = (viewIndex == 1 ? dc.body1 : dc.body2).find(">table>tbody>tr[datagrid-row-index=" + rowIndex + "]");
						}
						return tr;
					} else {
						if (type == "footer") {
							return (viewIndex == 1 ? dc.footer1 : dc.footer2).find(">table>tbody>tr[datagrid-row-index=" + rowIndex + "]");
						} else {
							if (type == "selected") {
								return (viewIndex == 1 ? dc.body1 : dc.body2).find(">table>tbody>tr.datagrid-row-selected");
							} else {
								if (type == "highlight") {
									return (viewIndex == 1 ? dc.body1 : dc.body2).find(">table>tbody>tr.datagrid-row-over");
								} else {
									if (type == "checked") {
										return (viewIndex == 1 ? dc.body1 : dc.body2).find(">table>tbody>tr.datagrid-row-checked");
									} else {
										if (type == "editing") {
											return (viewIndex == 1 ? dc.body1 : dc.body2).find(">table>tbody>tr.datagrid-row-editing");
										} else {
											if (type == "last") {
												return (viewIndex == 1 ? dc.body1 : dc.body2).find(">table>tbody>tr[datagrid-row-index]:last");
											} else {
												if (type == "allbody") {
													return (viewIndex == 1 ? dc.body1 : dc.body2).find(">table>tbody>tr[datagrid-row-index]");
												} else {
													if (type == "allfooter") {
														return (viewIndex == 1 ? dc.footer1 : dc.footer2)
																.find(">table>tbody>tr[datagrid-row-index]");
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			},
			getRow : function(gridTable, row) {
				var rowIndex = (typeof row == "object") ? row.attr("datagrid-row-index") : row;
				return $d(gridTable, "datagrid").data.rows[parseInt(rowIndex)];
			},
			getRows : function(gridTable) {
				return $(gridTable).datagrid("getRows");
			}
		},
		view : viewRenders,
		/**
		 * queryParams
		 */
		onBeforeLoad : $n,
		onLoadSuccess : $n,
		onLoadError : $n,
		/**
		 * rowIndex, row
		 */
		onClickRow : $n,
		/**
		 * rowIndex, row
		 */
		onDblClickRow : $n,
		/**
		 * rowIndex, field, fieldValue, td, target
		 */
		onClickCell : $n,
		/**
		 * rowIndex, field, fieldValue
		 */
		onDblClickCell : $n,
		/**
		 * sortField, sortOrder
		 */
		onSortColumn : $n,
		/**
		 * field, width
		 */
		onResizeColumn : $n,
		/**
		 * rowIndex, row
		 */
		onSelect : $n,
		/**
		 * rowIndex, row
		 */
		onUnselect : $n,
		/**
		 * rows
		 */
		onSelectAll : $n,
		/**
		 * rows
		 */
		onUnselectAll : $n,
		/**
		 * rowIndex, row, tr
		 */
		onCheck : $n,
		/**
		 * rowIndex, row, tr
		 */
		onUncheck : $n,
		/**
		 * rows
		 */
		onCheckAll : $n,
		/**
		 * rows
		 */
		onUncheckAll : $n,
		/**
		 * args: e, field
		 */
		onHeaderContextMenu : $n,
		/**
		 * args: e, rowIndex, row, tr
		 */
		onRowContextMenu : $n,
		/*
		 * CURD
		 */
		editors : editors,
		/**
		 * rowIndex, row
		 */
		onBeforeEdit : $n,
		/**
		 * rowIndex, row, changedRow
		 */
		onEdit : $n,
		/**
		 * rowIndex, row, changedRow
		 */
		onAfterEdit : $n,
		/**
		 * rowIndex, row
		 */
		onCancelEdit : $n
	});
})(jQuery, jCocit);
