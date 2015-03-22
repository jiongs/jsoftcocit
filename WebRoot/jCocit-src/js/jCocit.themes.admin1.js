var doOpenTab = function(tabs, title, linkUrl) {
	var $tabs = $(tabs);
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
}
var doSelectTab = function(tabTitle, tabIndex) {
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
}

var doInitIndex = function(indexID) {
	var $index = $(indexID);
	var $tree = $index.find("UL.menu_tree");

	$(".menu_item", $index).click(function() {
		var $this = $(this);

		var linkURL = $this.attr("linkURL");
		var title = $this.find(".title").text();

		doOpenTab($(".jCocit-tabs", $index), title, linkURL);
	});

	$(".menu2", $tree).click(function() {
		var $this = $(this);

		if ($this.hasClass("menu2_leaf")) {
			$(".menu2_leaf_selected", $tree).removeClass("menu2_leaf_selected");

			$this.addClass("menu2_leaf_selected");
		} else {
			$(".menu2_leaf_selected", $tree).removeClass("menu2_leaf_selected");

			if ($this.hasClass("menu2_selected")) {
				$(".menu2_selected", $tree).removeClass("menu2_selected");
			} else {
				$(".menu2_selected", $tree).removeClass("menu2_selected");

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
			}
		}
	});

	$(".menu3", $tree).click(function() {
		var $this = $(this);

		$(".menu2_leaf_selected", $tree).removeClass("menu2_leaf_selected");
		$(".menu3_selected", $tree).removeClass("menu3_selected");

		$(".menu3_submenus", $this.parent().parent()).hide();
		$(".menu3_submenus", $this.parent()).show();

		$this.addClass("menu3_selected");
	});
	$(".menu4", $tree).click(function() {
		var $this = $(this);

		$(".menu2_leaf_selected", $tree).removeClass("menu2_leaf_selected");
		$(".menu3_leaf_selected", $tree).removeClass("menu3_leaf_selected");
		$(".menu4_selected", $tree).removeClass("menu4_selected");

		$this.addClass("menu4_selected");
	});

}
