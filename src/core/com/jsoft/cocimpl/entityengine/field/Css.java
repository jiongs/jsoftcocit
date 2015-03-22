package com.jsoft.cocimpl.entityengine.field;

import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.util.StringUtil;

@CocColumn(uiView = "ui.widget.field.Composite")
public class Css extends JsonField<Css> {
	@CocColumn(name = "宽度", sn = 1)
	protected String width;

	@CocColumn(name = "高度", sn = 2)
	protected String height;

	@CocColumn(name = "背景", sn = 3, uiView = "ui.widget.field.CssBackground")
	protected CssBackground background;

	@CocColumn(name = "边框", sn = 4, uiView = "ui.widget.field.CssBorder")
	protected CssBorder border;

	@CocColumn(name = "上边框", sn = 5, uiView = "ui.widget.field.CssBorder")
	protected CssBorder borderTop;

	@CocColumn(name = "下边框", sn = 6, uiView = "ui.widget.field.CssBorder")
	protected CssBorder borderBottom;

	@CocColumn(name = "左边框", sn = 7, uiView = "ui.widget.field.CssBorder")
	protected CssBorder borderLeft;

	@CocColumn(name = "右边框", sn = 8, uiView = "ui.widget.field.CssBorder")
	protected CssBorder borderRight;

	@CocColumn(name = "字体", sn = 9, uiView = "ui.widget.field.CssFont")
	protected CssFont font;

	public Css() {
		this("");
	}

	public Css(String str) {
		super(str);
	}

	@Override
	protected void init(Css obj) {
		if (obj != null) {
			this.width = obj.width;
			this.height = obj.height;
			this.background = obj.background;
			this.border = obj.border;
			this.borderTop = obj.borderTop;
			this.borderRight = obj.borderRight;
			this.borderBottom = obj.borderBottom;
			this.borderLeft = obj.borderLeft;
			this.font = obj.font;
		}
	}

	public String toCssStyle() {
		StringBuffer sb = new StringBuffer();

		if (!StringUtil.isBlank(width))
			sb.append("width:").append(width).append(";");
		if (!StringUtil.isBlank(height))
			sb.append("height:").append(height).append(";");
		if (background != null) {
			sb.append(background.toCssStyle());
		}
		if (border != null)
			sb.append(border.toCssStyle(""));
		if (borderTop != null)
			sb.append(borderTop.toCssStyle("-top"));
		if (borderRight != null)
			sb.append(borderRight.toCssStyle("-right"));
		if (borderBottom != null)
			sb.append(borderBottom.toCssStyle("-bottom"));
		if (borderLeft != null)
			sb.append(borderLeft.toCssStyle("-left"));
		if (font != null)
			sb.append(font.toCssStyle());

		return sb.toString();
	}

	public CssBackground getBackground() {
		return background;
	}

	public CssBorder getBorder() {
		return border;
	}

	public CssFont getFont() {
		return font;
	}

	public void setBackground(CssBackground background) {
		this.background = background;
	}

	public void setBorder(CssBorder border) {
		this.border = border;
	}

	public void setFont(CssFont font) {
		this.font = font;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public CssBorder getBorderTop() {
		return borderTop;
	}

	public void setBorderTop(CssBorder borderTop) {
		this.borderTop = borderTop;
	}

	public CssBorder getBorderRight() {
		return borderRight;
	}

	public void setBorderRight(CssBorder borderRight) {
		this.borderRight = borderRight;
	}

	public CssBorder getBorderBottom() {
		return borderBottom;
	}

	public void setBorderBottom(CssBorder borderBottom) {
		this.borderBottom = borderBottom;
	}

	public CssBorder getBorderLeft() {
		return borderLeft;
	}

	public void setBorderLeft(CssBorder borderLeft) {
		this.borderLeft = borderLeft;
	}

}
