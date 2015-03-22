(function($, jCocit) {
	$e(jCocit.defaults, {
		loading : "正在加载...",
		title : ""
	});

	if ($.alerts)
		$e($.alerts.defaults, {
			ok : "确定",
			cancel : "取消",
			yes : "是",
			no : "否",
			warn : "提示",
			error : "提示",
			info : "提示",
			prompt : "提示",
			success : "提示",
			confirm : "确认"
		});

	if ($.fn.loginform)
		$e($.fn.loginform.defaults, {
			errorMsg : "用户名或密码非法！"
		});

	if ($.fn.calendar)
		$e($.fn.calendar.defaults, {
			weeks : [ "日", "一", "二", "三", "四", "五", "六" ],
			months : [ "01月", "02月", "03月", "04月", "05月", "06月", "07月", "08月", "09月", "10月", "11月", "12月" ],
			titleFormater : function(year, month) {
				var opts = $d(this, "calendar").options;
				return year + "年" + opts.months[month - 1];
			}
		});

	$e($.fn.combo.defaults, {
		width : 200
	});

	if ($.fn.combodate)
		$e($.fn.combodate.defaults, {
			todayText : "今天",
			closeText : "关闭",
			okText : "确定"
		});

	if ($.fn.combodatetime && $.fn.combodate)
		$e($.fn.combodatetime.defaults, {
			todayText : $.fn.combodate.defaults.todayText,
			closeText : $.fn.combodate.defaults.closeText,
			okText : $.fn.combodate.defaults.okText
		});

	if ($.fn.datagrid)
		$e($.fn.datagrid.defaults, {
			loadMsg : '正在加载数据，请稍待...',
			editSaveButton: '保存',
			editCancelButton: '取消'
		});

	if ($.fn.pagination)
		$e($.fn.pagination.defaults, {
			beforePageText : '第',// 第?页
			afterPageText : '页 共{pages}页',// 共?页
			displayMsg : '显示{from}-{to}条 共{total}条'
		});

	if ($.fn.validatebox)
		$e($.fn.validatebox.defaults, {
			missingMessage: '字段值不能为空！',
			rules: {
				email : {
					validator : function(strValue) {
						return /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(strValue);
					},
					message : "邮箱地址不正确，请重新输入正确的邮箱地址！"
				},
				url : {
					validator : function(strValue) {
						var strRegex = "^((https|http|ftp|rtsp|mms)?://)" + "?(([0-9a-zA-Z_!~*'().&=+$%-]+: )?[0-9a-zA-Z_!~*'().&=+$%-]+@)?" // ftp的user@
								+ "(([0-9]{1,3}\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
								+ "|" // 允许IP和DOMAIN（域名）
								+ "([0-9a-zA-Z_!~*'()-]+\.)*" // 域名- www.
								+ "([0-9a-zA-Z][0-9a-zA-Z-]{0,61})?[0-9a-zA-Z]\." // 二级域名
								+ "[a-zA-Z]{2,6})" // first level domain- .com or .museum
								+ "(:[0-9]{1,4})?" // 端口- :80
								+ "((/?)|" + "(/[0-9a-zA-Z_!~*'().;?:@&=+$,%#-]+)+/?)$";
						return new RegExp(strRegex).test(strValue);
					},
					message : "URL地址不正确，请重新输入正确的URL地址！"
				},
				length : {
					validator : function(strValue, lenBetween) {
						var len = $.trim(strValue).length;
						return len >= lenBetween[0] && len <= lenBetween[1];
					},
					message : "字段值必需在 {0} 和 {1} 之间！"
				},
				remote : {
					/**
					 * config[0]: url
					 * <p>
					 * config[1]: field
					 */
					validator : function(strValue, config) {
						var data = {};
						data[config[1]] = strValue;
						var json = $.doAjax({
							url : config[0],
							dataType : "json",
							data : data,
							async : false,
							cache : false,
							type : "post"
						}).responseText;
						return json == "true";
					},
					message : "Please fix this field."
				}
			}
		});

	if (jCocit.entity)
		$e(jCocit.entity.defaults, {
			confirm : "确 定",
			cancel : "取 消",
			unsupport : "不支持该操作！",
			deleteMessage : "您确定要删除选中的数据吗？",
			warnMessage : "您确定要执行该操作吗？",
			unselectedAny : "您尚未选中任何记录，请先选中一条或多条记录！",
			unselectedOne : "您尚未选中任何记录，请先选中一条记录！",
			onlySelectedOne : "只能选中一条记录，请重新选择！",
			promptSave: "您正在编辑的数据尚未保存，请先保存！"
		});
})(jQuery, jCocit);
