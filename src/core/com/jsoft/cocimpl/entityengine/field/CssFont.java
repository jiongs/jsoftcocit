package com.jsoft.cocimpl.entityengine.field;

import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.util.StringUtil;

@CocColumn(name = "字体", uiView = "ui.widget.field.Composite")
public class CssFont extends JsonField<CssFont> {

	@CocColumn(name = "字体大小", sn = 1, uiView = "ui.widget.field.Spinner")
	protected Integer size;

	@CocColumn(name = "字体粗细", sn = 2, dicOptions = ":粗细,normal:正常,bold:粗体,bolder:更粗,lighter:更细,inherit:继承,100:100,200:200,300:300,400:400,500:500,600:600,700:700,800:800,900:900")
	protected String weight;

	@CocColumn(name = "字体颜色", sn = 3, uiView = "ui.widget.field.CssColor")
	protected String color;

	@CocColumn(name = "字体风格", sn = 4, dicOptions = ":风格,normal:正常,italic:斜体,oblique:倾斜,inherit:继承")
	protected String style;

	@CocColumn(name = "文本修饰", sn = 5, dicOptions = ":修饰,none:默认,underline:下划线,overline:上划线,line-through:穿越线,blink:闪烁,inherit:继承")
	protected String decoration;

	// @CocColumn(name = "名称")
	protected String name;

	public CssFont() {
		this("");
	}

	public CssFont(String str) {
		super(str);
	}

	@Override
	protected void init(CssFont obj) {
		if (obj != null) {
			this.size = obj.size;
			this.name = obj.name;
			this.style = obj.style;
			this.color = obj.color;
			this.weight = obj.weight;
			this.decoration = obj.decoration;
		}
	}

	public String toCssStyle() {
		if (StringUtil.isBlank(style) && StringUtil.isBlank(color) && StringUtil.isBlank(weight) && StringUtil.isBlank(decoration)) {
			return "";
		}

		StringBuffer sb = new StringBuffer();

		if (size != null)
			sb.append("font-size:").append(size).append("px;");
		if (!StringUtil.isBlank(style))
			sb.append("font-style:").append(style).append(";");
		if (!StringUtil.isBlank(color))
			sb.append("color:").append(color).append(";");
		if (!StringUtil.isBlank(weight))
			sb.append("font-weight:").append(weight).append(";");
		if (!StringUtil.isBlank(decoration))
			sb.append("text-decoration:").append(decoration).append(";");

		return sb.toString();
	}

	public String getStyle() {
		return style;
	}

	public String getColor() {
		return color;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public String getWeight() {
		return weight;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getDecoration() {
		return decoration;
	}

	public void setDecoration(String decoration) {
		this.decoration = decoration;
	}
}
