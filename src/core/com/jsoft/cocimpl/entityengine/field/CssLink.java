package com.jsoft.cocimpl.entityengine.field;

import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.util.StringUtil;

@CocColumn(uiView = "ui.widget.field.Composite")
public class CssLink extends JsonField<CssLink> {
	@CocColumn(name = "链接", sn = 10, uiView = "ui.widget.field.CssFont")
	protected CssFont a;

	@CocColumn(name = "未访问的链接", sn = 11, uiView = "ui.widget.field.CssFont")
	protected CssFont aLink;

	@CocColumn(name = "已访问的链接", sn = 12, uiView = "ui.widget.field.CssFont")
	protected CssFont aVisited;

	@CocColumn(name = "鼠标悬停链接", sn = 13, uiView = "ui.widget.field.CssFont")
	protected CssFont aHover;

	@CocColumn(name = "被选中的链接", sn = 14, uiView = "ui.widget.field.CssFont")
	protected CssFont aActive;

	public CssLink() {
		this("");
	}

	public CssLink(String str) {
		super(str);
	}

	@Override
	protected void init(CssLink obj) {
		if (obj != null) {
			this.a = obj.a;
			this.aLink = obj.aLink;
			this.aVisited = obj.aVisited;
			this.aHover = obj.aHover;
			this.aActive = obj.aActive;
		}
	}

	public String toCssStyle(String cssClass) {
		StringBuffer sb = new StringBuffer();

		if (a != null) {
			String css = a.toCssStyle();
			if (!StringUtil.isBlank(css))
				sb.append("\n").append(cssClass).append(" a{").append(css).append("}");
		}
		if (aLink != null) {
			String css = aLink.toCssStyle();
			if (!StringUtil.isBlank(css))
				sb.append("\n").append(cssClass).append(" a:link{").append(css).append("}");
		}
		if (aVisited != null) {
			String css = aVisited.toCssStyle();
			if (!StringUtil.isBlank(css))
				sb.append("\n").append(cssClass).append(" a:visited{").append(css).append("}");
		}
		if (aHover != null) {
			String css = aHover.toCssStyle();
			if (!StringUtil.isBlank(css))
				sb.append("\n").append(cssClass).append(" a:hover{").append(css).append("}");
		}
		if (aActive != null) {
			String css = aActive.toCssStyle();
			if (!StringUtil.isBlank(css))
				sb.append("\n").append(cssClass).append(" a:active{").append(css).append("}");
		}

		return sb.toString();
	}

}
