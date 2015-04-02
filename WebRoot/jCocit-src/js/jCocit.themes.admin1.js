var doOpenTab = function(tabs, title, linkUrl) {
	jCocit.util.openTab(tabs, title, linkUrl);
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
	func1 : function() {

	},
	func2 : function() {

	}
}
