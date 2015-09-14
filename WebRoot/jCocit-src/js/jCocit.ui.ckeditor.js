(function($, jCocit) {

	$.fn.ckeditor = function(options, args) {
		if (typeof options == "string") {
			var fn = $.fn.ckeditor.methods[options];
			if (fn)
				return fn(this, args);
			else
				$.error('The method ' + options + ' does not exist in $.fn.ckeditor');
		}
		options = options || {};
		return this.each(function() {
			var state = $d(this, "ckeditor");
			var opts;
			if (state) {
				opts = $.extend(state.options, options);
				state.options = opts;
			} else {
				$d(this, "ckeditor", {
					options : $.extend({}, $.fn.ckeditor.defaults, $.fn.ckeditor.parseOptions(this), options),
					tabs : [],
					selectHis :  []
				});
				if (typeof CKEDITOR != "undefined") {
					delete CKEDITOR.instances[this.id];
					CKEDITOR.replace(this.id);
				}
				if (typeof CKFinder != "undefined") {
					CKFinder.setupCKEditor(null, {
						basePath : jCocit.contextPath + '/jCocit/js/ckfinder/',
						filebrowserBrowseUrl : jCocit.contextPath + '/jCocit/js/ckfinder/ckfinder.html',
						filebrowserImageBrowseUrl : jCocit.contextPath + '/jCocit/js/ckfinder/ckfinder.html?type=Images',
						filebrowserFlashBrowseUrl : jCocit.contextPath + '/jCocit/js/ckfinder/ckfinder.html?type=Flash',
						filebrowserUploadUrl : jCocit.contextPath + '/coc/getCKFinder?command=QuickUpload&type=Files',
						filebrowserImageUploadUrl : jCocit.contextPath + '/coc/getCKFinder?command=QuickUpload&type=Images',
						filebrowserFlashUploadUrl : jCocit.contextPath + '/coc/getCKFinder?command=QuickUpload&type=Flash'
					});
				}
			}
		});
	};

	$.fn.ckeditor.methods = {};
	$.fn.ckeditor.parseOptions = function(html) {
		return $.extend({}, jCocit.parseOptions(html, []));
	};
	$.fn.ckeditor.defaults = $.extend({}, $.fn.button.defaults, {});
})(jQuery, jCocit);
