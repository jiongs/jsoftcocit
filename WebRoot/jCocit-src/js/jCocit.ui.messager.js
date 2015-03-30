(function($) {
	function _1(el, _2, _3, _4) {
		var _5 = $(el).window("window");
		if (!_5) {
			return;
		}
		switch (_2) {
		case null:
			_5.show();
			break;
		case "slide":
			_5.slideDown(_3);
			break;
		case "fade":
			_5.fadeIn(_3);
			break;
		case "show":
			_5.show(_3);
			break;
		}
		var _6 = null;
		if (_4 > 0) {
			_6 = setTimeout(function() {
				_7(el, _2, _3);
			}, _4);
		}
		_5.hover(function() {
			if (_6) {
				clearTimeout(_6);
			}
		}, function() {
			if (_4 > 0) {
				_6 = setTimeout(function() {
					_7(el, _2, _3);
				}, _4);
			}
		});
	}
	;
	function _7(el, _8, _9) {
		if (el.locked == true) {
			return;
		}
		el.locked = true;
		var _a = $(el).window("window");
		if (!_a) {
			return;
		}
		switch (_8) {
		case null:
			_a.hide();
			break;
		case "slide":
			_a.slideUp(_9);
			break;
		case "fade":
			_a.fadeOut(_9);
			break;
		case "show":
			_a.hide(_9);
			break;
		}
		setTimeout(function() {
			$(el).window("destroy");
		}, _9);
	}
	;
	function show(opts) {
		var options = $.extend({}, $.fn.window.defaults, {
			collapsible : false,
			minimizable : false,
			maximizable : false,
			shadow : false,
			draggable : false,
			resizable : false,
			closed : true,
			style : {
				left : "",
				top : "",
				right : 0,
				zIndex : $.fn.window.defaults.zIndex++,
				bottom : -document.body.scrollTop - document.documentElement.scrollTop
			},
			onBeforeOpen : function() {
				_1(this, options.showType, options.showSpeed, options.timeout);
				return false;
			},
			onBeforeClose : function() {
				_7(this, options.showType, options.showSpeed);
				return false;
			}
		}, {
			title : "",
			width : 250,
			height : 100,
			showType : "slide",
			showSpeed : 600,
			msg : "",
			timeout : 4000
		}, opts);
		options.style.zIndex = $.fn.window.defaults.zIndex++;
		var $window = $("<div class=\"messager-body\"></div>").html(options.msg).appendTo("body");
		$window.window(options);
		$window.window("window").addClass("window-messager").css(options.style);
		$window.window("open");
		return $window;
	}
	;
	function openWindow(title, msgDIV, buttons) {
		var win = $("<div class=\"messager-body\"></div>").appendTo("body");
		win.append(msgDIV);
		win.dialog({
			title : title,
			noheader : (title ? false : true),
			width : 400,
			height : "auto",
			modal : true,
			shadow: false,
			collapsible : false,
			minimizable : false,
			maximizable : false,
			resizable : false,
			onClose : function() {
				setTimeout(function() {
					win.window("destroy");
				}, 100);
			},
			buttons: buttons
		});
		win.window("window").addClass("messager-window");
		return win;
	}
	;
	$.messager = {
		show : function(options) {
			return show(options);
		},
		showMsg : function(msg) {
			this.show({
				title : '',
				msg : msg,
				showType : 'slide',
				height : 40,
				style : {
					right : '',
					top : document.body.scrollTop + document.documentElement.scrollTop,
					bottom : ''
				}
			});
		},
		alert : function(title, msg, type, callback) {
			var msgDIV = "<div>" + msg + "</div>";
			switch (type) {
			case "error":
				msgDIV = "<div class=\"messager-icon messager-error\"></div>" + msgDIV;
				break;
			case "info":
				msgDIV = "<div class=\"messager-icon messager-info\"></div>" + msgDIV;
				break;
			case "question":
				msgDIV = "<div class=\"messager-icon messager-question\"></div>" + msgDIV;
				break;
			case "warning":
				msgDIV = "<div class=\"messager-icon messager-warning\"></div>" + msgDIV;
				break;
			}
			msgDIV += "<div style=\"clear:both;\"/>";
			var buttons = [ {
				text : '确定',
				iconCls : 'icon-ok',
				onClick : function(data) {
					$(this).dialog('close');
				}
			}]
			var win = openWindow(title, msgDIV, buttons);
			return win;
		},
		confirm : function(_19, msg, fn) {
			var _1a = "<div class=\"messager-icon messager-question\"></div>" + "<div>" + msg + "</div>" + "<div style=\"clear:both;\"/>";
			var _1b = {};
			_1b[$.messager.defaults.ok] = function() {
				win.window("close");
				if (fn) {
					fn(true);
					return false;
				}
			};
			_1b[$.messager.defaults.cancel] = function() {
				win.window("close");
				if (fn) {
					fn(false);
					return false;
				}
			};
			var win = openWindow(_19, _1a, _1b);
			return win;
		},
		prompt : function(_1c, msg, fn) {
			var _1d = "<div class=\"messager-icon messager-question\"></div>" + "<div>" + msg + "</div>" + "<br/>" + "<div style=\"clear:both;\"/>"
					+ "<div><input class=\"messager-input\" type=\"text\"/></div>";
			var _1e = {};
			_1e[$.messager.defaults.ok] = function() {
				win.window("close");
				if (fn) {
					fn($(".messager-input", win).val());
					return false;
				}
			};
			_1e[$.messager.defaults.cancel] = function() {
				win.window("close");
				if (fn) {
					fn();
					return false;
				}
			};
			var win = openWindow(_1c, _1d, _1e);
			win.children("input.messager-input").focus();
			return win;
		},
		progress : function(_1f) {
			var _20 = {
				bar : function() {
					return $("body>div.messager-window").find("div.messager-p-bar");
				},
				close : function() {
					var win = $("body>div.messager-window>div.messager-body:has(div.messager-progress)");
					if (win.length) {
						win.window("close");
					}
				}
			};
			if (typeof _1f == "string") {
				var _21 = _20[_1f];
				return _21();
			}
			var _22 = $.extend({
				title : "",
				msg : "",
				text : undefined,
				interval : 300
			}, _1f || {});
			var _23 = "<div class=\"messager-progress\"><div class=\"messager-p-msg\"></div><div class=\"messager-p-bar\"></div></div>";
			var win = openWindow(_22.title, _23, null);
			win.find("div.messager-p-msg").html(_22.msg);
			var bar = win.find("div.messager-p-bar");
			bar.progressbar({
				text : _22.text
			});
			win.window({
				closable : false,
				onClose : function() {
					if (this.timer) {
						clearInterval(this.timer);
					}
					$(this).window("destroy");
				}
			});
			if (_22.interval) {
				win[0].timer = setInterval(function() {
					var v = bar.progressbar("getValue");
					v += 10;
					if (v > 100) {
						v = 0;
					}
					bar.progressbar("setValue", v);
				}, _22.interval);
			}
			return win;
		}
	};
	$.messager.defaults = {
		ok : "Ok",
		cancel : "Cancel"
	};
	/*
	 * Common Functions
	 */
//	Jerror = function(message, title, callback) {
//		$.messager.alert("提示", message+"<p><p><p><P><P>"+message, "error", callback);
//	};
//	Jwarn = function(message, title, callback) {
//		$.messager.alert("提示", message+"<p><p><p><P><P>"+message, "error", callback);
//	};
//	Jinfo = function(message, title, callback) {
//		$.messager.alert("提示", message+"<p><p><p><P><P>"+message, "error", callback);
//	};
//	Jalert = Jinfo;
//	Jsuccess = function(message, title, callback) {
//		$.messager.alert("提示", message, "info", callback);
//	};
//	Jconfirm = function(message, title, callback) {
//		$.messager.alert("提示", message, "question", callback);
//	};
//	Jprompt = function(message, value, title, callback) {
//		$.messager.alert("提示", message, "question", callback);
//	};
})(jQuery);