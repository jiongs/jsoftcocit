/**
 * They are extend functions from jCocit object to support auto parse plugins of HTML UI element.
 */
(function($, jCocit) {
	$.extend(jCocit, {
		/**
		 * Parse HTML element to plugin. This function will be auto invoked after HTML document loaded.
		 * 
		 * <p>
		 * <b>Note:</b>
		 * 
		 * <pre>
		 *  [DIV class='jCocit-ui jCocit-draggable'][/DIV]
		 *  [UL class='jCocit-ui jCocit-tree'][/UL]
		 *  [INPUT class='jCocit-ui jCocit-combo'][/UL]
		 * </pre>
		 * 
		 * <UL>
		 * <LI>jCocit-ui: This HTML element will be automatic parsed if it's "class" contains "jCocit-ui".
		 * <LI>jCocit-{plugin}: This HTML element will be parsed to "plugin" object. It must be used combine with "jCocit-ui". (eg: jCocit-draggable, jCocit-resizable, jCocit-menu etc.)
		 * </UL>
		 * 
		 * <p>
		 * <B>Parameters:</B>
		 * <UL>
		 * <LI>context: this is HTML element or jQuery object.
		 * </UL>
		 */
		parseUI : function(context) {
			var pluginObjs = {};
			var prefix = "jCocit-";
			var self = this;
			var time0 = new Date().getTime();
			var $uis = $(".jCocit-ui", context);
			$uis.each(function() {
				var $self = $(this);
				var classNames = $self.attr("class").split(" ");

				for ( var i = 0; i < classNames.length; i++) {
					var className = classNames[i];
					if (className.startsWith(prefix)) {
						var plugin = className.trim().substring(prefix.length);
						if (plugin != "ui" && $self[plugin]) {
							// try {
							var time1 = new Date().getTime();

							var $pluginObj = $self[plugin]();
							if (pluginObjs[plugin])
								pluginObjs[plugin] = pluginObjs[plugin].add($pluginObj);
							else
								pluginObjs[plugin] = $pluginObj;

							$log("ui.{1}: created (title = {2})", time1, plugin, this.title || this.id);
							// } catch (e) {
							// $log("jCocit.parseUI (" + plugin + ") Error: " + e);
							// }
						}
					}
				}

			});

			if (jCocit.defaults.debug) {
				var s = "";
				for ( var name in pluginObjs) {
					var $pluginObj = pluginObjs[name];
					s += ", " + name + "(" + $pluginObj.length + ")";
				}
				$log("Parse UI: Finished (total = {1}){2}", time0, $uis.length, s);
			}

			return pluginObjs;
		},

		/**
		 * Parse options of HTML element jQuery object specified "sourceHTML".
		 * <p>
		 * <B>Parameters:</B>
		 * <UL>
		 * <LI>sourceHTML: This is HTML element or it's jQuery object.
		 * <LI>props: This is a property description array, used to describe attributes of HTML element which will be parsed and regard as sourceHTML options settings.
		 * </UL>
		 * <B>Return:</B> JSON object. these attributes of HTML element specified by the parameter "props" will be regard as the properties of JSON object.
		 */
		parseOptions : function(sourceHTML, props) {
			return $.parseOptions(sourceHTML, props);
		},
		parseValue : function(attr, value, view, scrollbarSize) {
			scrollbarSize = scrollbarSize || 0;
			var v = $.trim(String(value || ""));
			var _a = v.substr(v.length - 1, 1);
			if (_a == "%") {
				v = parseInt(v.substr(0, v.length - 1));
				if (attr.toLowerCase().indexOf("width") >= 0) {
					v = Math.floor((view.width() - scrollbarSize) * v / 100);
				} else {
					v = Math.floor((view.height() - scrollbarSize) * v / 100);
				}
			} else {
				v = parseInt(v) || undefined;
			}
			return v;
		},
	});
	

	$.fn._outerWidth = function(w) {
		if (w == undefined) {
			if (this[0] == window) {
				return this.width() || document.body.clientWidth;
			}
			return this.outerWidth() || 0;
		}
		return this._size("width", w);
	};
	$.fn._outerHeight = function(h) {
		if (h == undefined) {
			if (this[0] == window) {
				return this.height() || document.body.clientHeight;
			}
			return this.outerHeight() || 0;
		}
		return this._size("height", h);
	};
	$.fn._scrollLeft = function(l) {
		if (l == undefined) {
			return this.scrollLeft();
		} else {
			return this.each(function() {
				$(this).scrollLeft(l);
			});
		}
	};
	$.fn._propAttr = $.fn.prop || $.fn.attr;
	$.fn._size = function(_14, _15) {
		if (typeof _14 == "string") {
			if (_14 == "clear") {
				return this.each(function() {
					$(this).css({
						width : "",
						minWidth : "",
						maxWidth : "",
						height : "",
						minHeight : "",
						maxHeight : ""
					});
				});
			} else {
				if (_14 == "fit") {
					return this.each(function() {
						_16(this, this.tagName == "BODY" ? $("body") : $(this).parent(), true);
					});
				} else {
					if (_14 == "unfit") {
						return this.each(function() {
							_16(this, $(this).parent(), false);
						});
					} else {
						if (_15 == undefined) {
							return _17(this[0], _14);
						} else {
							return this.each(function() {
								_17(this, _14, _15);
							});
						}
					}
				}
			}
		} else {
			return this.each(function() {
				_15 = _15 || $(this).parent();
				$.extend(_14, _16(this, _15, _14.fit) || {});
				var r1 = _18(this, "width", _15, _14);
				var r2 = _18(this, "height", _15, _14);
				if (r1 || r2) {
					$(this).addClass("easyui-fluid");
				} else {
					$(this).removeClass("easyui-fluid");
				}
			});
		}
		function _16(_19, _1a, fit) {
			if (!_1a.length) {
				return false;
			}
			var t = $(_19)[0];
			var p = _1a[0];
			var _1b = p.fcount || 0;
			if (fit) {
				if (!t.fitted) {
					t.fitted = true;
					p.fcount = _1b + 1;
					$(p).addClass("panel-noscroll");
					if (p.tagName == "BODY") {
						$("html").addClass("panel-fit");
					}
				}
				return {
					width : ($(p).width() || 1),
					height : ($(p).height() || 1)
				};
			} else {
				if (t.fitted) {
					t.fitted = false;
					p.fcount = _1b - 1;
					if (p.fcount == 0) {
						$(p).removeClass("panel-noscroll");
						if (p.tagName == "BODY") {
							$("html").removeClass("panel-fit");
						}
					}
				}
				return false;
			}
		}
		;
		function _18(_1c, _1d, _1e, _1f) {
			var t = $(_1c);
			var p = _1d;
			var p1 = p.substr(0, 1).toUpperCase() + p.substr(1);
			var min = $.parser.parseValue("min" + p1, _1f["min" + p1], _1e);
			var max = $.parser.parseValue("max" + p1, _1f["max" + p1], _1e);
			var val = $.parser.parseValue(p, _1f[p], _1e);
			var _20 = (String(_1f[p] || "").indexOf("%") >= 0 ? true : false);
			if (!isNaN(val)) {
				var v = Math.min(Math.max(val, min || 0), max || 99999);
				if (!_20) {
					_1f[p] = v;
				}
				t._size("min" + p1, "");
				t._size("max" + p1, "");
				t._size(p, v);
			} else {
				t._size(p, "");
				t._size("min" + p1, min);
				t._size("max" + p1, max);
			}
			return _20 || _1f.fit;
		}
		;
		function _17(_21, _22, _23) {
			var t = $(_21);
			if (_23 == undefined) {
				_23 = parseInt(_21.style[_22]);
				if (isNaN(_23)) {
					return undefined;
				}
				if ($._boxModel) {
					_23 += _24();
				}
				return _23;
			} else {
				if (_23 === "") {
					t.css(_22, "");
				} else {
					if ($._boxModel) {
						_23 -= _24();
						if (_23 < 0) {
							_23 = 0;
						}
					}
					t.css(_22, _23 + "px");
				}
			}
			function _24() {
				if (_22.toLowerCase().indexOf("width") >= 0) {
					return t.outerWidth() - t.width();
				} else {
					return t.outerHeight() - t.height();
				}
			}
			;
		}
		;
	};
})(jQuery, jCocit);

/**
 * Auto Parse jCocit UI element specified by class name "jCocit-ui" when the document is ready.
 */
$(document).ready(function() {
	jCocit.parseUI();
});
