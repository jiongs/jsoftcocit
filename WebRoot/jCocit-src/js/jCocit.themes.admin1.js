var doOpenTab = function(tabs, title, linkUrl) {
	jCocit.util.openTab(tabs, title, linkUrl);
}
var doSelectTab = function(tabTitle, tabIndex) {
//	if (tabIndex == 0 && tabTitle == '个人工作台') {
//		var $tab = $(this).tabs("getTab", tabIndex);
//		if ($tab.length) {
//			$tab.panel("refresh");
//		}
//	} else {
		var $uiList = $(".jCocit-ui", this);
		$uiList.each(function() {
			var $ui = $(this);
			if ($ui.hasClass("jCocit-datagrid") || $ui.hasClass("jCocit-treegrid")) {
				var uiOpts = $ui.datagrid("options");
				if (uiOpts.expired) {
					$ui.datagrid("reload")
					uiOpts.expired = false;
				} else {
					var $view = $ui.datagrid("getPanel").find("div.datagrid-view");
					if ($view.height() == 0) {
						$ui.datagrid("reload");
					}
				}
			}
		});
//	}
}

var doInitIndex = function(indexID) {
	var $index = $(indexID);
	var $tree = $index.find("UL.menu_tree");

	$(".menu_item", $index).click(function() {
		var $this = $(this);

		var options = $.parseOptions($this);
		var linkURL = $this.attr("linkURL");
		var title = $this.find(".title").text();

		jCocit.util.openTab($(".jCocit-tabs", $index), title, linkURL);
	});

	$(".menu2", $tree).click(function() {
		var $this = $(this);

		$(".menu2_selected", $tree).removeClass("menu2_selected");
		$(".menu2_leaf_selected", $tree).removeClass("menu2_leaf_selected");
		$(".menu3_leaf_selected", $tree).removeClass("menu3_leaf_selected");
		$(".menu4_selected", $tree).removeClass("menu4_selected");

		if ($this.hasClass("menu2_leaf")) {
			$this.addClass("menu2_leaf_selected");
		} else {
			$this.addClass("menu2_selected");
		}

		var submenus = $(".menu2_submenus", $this.parent());
		if (submenus.length > 0) {
			if ($this.hasClass("menu2_expend")) {
				$(".menu2", $tree).removeClass("menu2_expend");
				$(".menu2_submenus", $tree).hide();
			} else {
				$(".menu2", $tree).removeClass("menu2_expend");
				$(".menu2_submenus", $tree).hide();

				submenus.show();
				$this.addClass("menu2_expend");
			}
		} else {
			$(".menu2_expend", $tree).removeClass("menu2_expend");
		}

	}).hover(function() {
		$(this).addClass("menu2_hover");
	}, function() {
		$(this).removeClass("menu2_hover");
	});

	$(".menu3", $tree).click(function() {
		var $this = $(this);

		$(".menu2_selected", $tree).removeClass("menu2_selected");
		$(".menu2_leaf_selected", $tree).removeClass("menu2_leaf_selected");
		$(".menu3_leaf_selected", $tree).removeClass("menu3_leaf_selected");
		$(".menu4_selected", $tree).removeClass("menu4_selected");

		if ($this.hasClass("menu3_leaf")) {
			$this.addClass("menu3_leaf_selected");
		} else {
			$this.addClass("menu3_selected");
		}

		$this.closest(".menu2_submenus").prev(".menu2").addClass("menu2_selected");

		var submenus = $(".menu3_submenus", $this.parent());
		if (submenus.length > 0) {
			if ($this.hasClass("menu3_expend")) {
				$(".menu3", $tree).removeClass("menu3_expend");
				$(".menu3_submenus", $tree).hide();
			} else {
				$(".menu3", $tree).removeClass("menu3_expend");
				$(".menu3_submenus", $tree).hide();

				submenus.show();
				$this.addClass("menu3_expend");
			}
		} else {
			$(".menu3_expend", $tree).removeClass("menu3_expend");
		}

	}).hover(function() {
		$(this).addClass("menu3_hover");
	}, function() {
		$(this).removeClass("menu3_hover");
	});
	$(".menu4", $tree).click(function() {
		var $this = $(this);

		$(".menu2_selected", $tree).removeClass("menu2_selected");
		$(".menu2_leaf_selected", $tree).removeClass("menu2_leaf_selected");
		$(".menu3_leaf_selected", $tree).removeClass("menu3_leaf_selected");
		$(".menu4_selected", $tree).removeClass("menu4_selected");

		$this.closest(".menu2_submenus").prev(".menu2").addClass("menu2_selected");

		$this.addClass("menu4_selected");
	}).hover(function() {
		$(this).addClass("menu4_hover");
	}, function() {
		$(this).removeClass("menu4_hover");
	});

}
jCocit.admin1 = {
	openWindow : function(btn) {
		var $btn = $(btn);
		jCocit.util.openWindow(btn, null, {
			onClose : function() {
				var tabs = $btn.closest(".coc-tabs");
				var tab = tabs.find(".coc-tabs-header .coc-tab-header-selected");
				tab.click();
			}
		});
	},
	loadScheduleHTML : function(event, htmlContainer) {
		var btn = $(event.target).closest(".coc-tab-header");
		if (btn.length) {
			var options = $.parseOptions(btn);
			var url = options.opUrl;
			if (url) {
				var tabs = btn.closest(".coc-tabs");

				var data = {};
				var cal = tabs.find(".jCocit-calendar");
				if (cal.length) {
					try {
						var dateObj = cal.calendar("getValue");
						data["dateFrom"] = dateObj.getTime();
					} catch (e) {
					}
				}
				$(htmlContainer).doLoad({
					type : "POST",
					async : true,
					url : url,
					data : data
				});
				btn.closest(".coc-tabs-header").find(".coc-tab-header-selected").removeClass("coc-tab-header-selected");
				btn.addClass("coc-tab-header-selected");
			}
		}

		if (event.stopPropagation) {
			event.stopPropagation();
		} else if (event.preventDefault) {
			event.preventDefault();
		}
	},
	newPlanCalendar : function(date) {

		var cal = $(this);
		var tabs = cal.closest(".coc-tabs");
		var isDepSch = tabs.find(".coc-tab-header-selected").hasClass("sch_department");
		var current = cal.calendar('getValue');
		var strDate = current.getFullYear() + '-' + (current.getMonth() + 1) + '-' + current.getDate();
		if (isDepSch) {
			jCocit.util.openWindow(this, jCocit.contextPath + '/cocentity/getFormToSave/XZSW_RC_BM:XZSW_RC:dc/0?entity.qssj=' + strDate);
		} else {
			jCocit.util.openWindow(this, jCocit.contextPath + '/cocentity/getFormToSave/XZSW_RC_GR:XZSW_RC:pc/0?entity.qssj=' + strDate, {});
		}
	}
}