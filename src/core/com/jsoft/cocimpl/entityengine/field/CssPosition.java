package com.jsoft.cocimpl.entityengine.field;

import com.jsoft.cocit.entityengine.annotation.CocColumn;

@CocColumn(precision = 2000, uiView = "ui.widget.field.Composite")
public class CssPosition extends JsonField<CssPosition> {

	@CocColumn(name = "区域", sn = 1, dicOptions = "page:内容,top:顶部,bottom:底部")
	private String area;

	@CocColumn(name = "位置", sn = 2, dicOptions = "absolute:绝对位置,relative:相对位置")
	private String position;

	@CocColumn(name = "对齐", sn = 3, dicOptions = "1:左,2:右,3:顶,4:底")
	private Integer align;

	@CocColumn(name = "距左", sn = 4, uiView = "ui.widget.field.Spinner")
	private Integer left;

	@CocColumn(name = "距顶", sn = 5, uiView = "ui.widget.field.Spinner")
	private Integer top;

	@CocColumn(name = "宽度", sn = 6, uiView = "ui.widget.field.Spinner")
	private Integer width;

	@CocColumn(name = "高度", sn = 7, uiView = "ui.widget.field.Spinner")
	private Integer height;

	public CssPosition() {
		this("");
	}

	public CssPosition(String str) {
		super(str);
	}

	@Override
	protected void init(CssPosition obj) {
		if (obj != null) {
			this.area = obj.area;
			this.position = obj.position;
			this.align = obj.align;
			this.left = obj.left;
			this.top = obj.top;
			this.width = obj.width;
			this.height = obj.height;
		}
	}

	public String getPosition() {
		return position;
	}

	public Integer getLeft() {
		return left;
	}

	public Integer getTop() {
		return top;
	}

	public Integer getWidth() {
		return width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public void setLeft(Integer left) {
		this.left = left;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Integer getAlign() {
		return align;
	}

	public void setAlign(Integer align) {
		this.align = align;
	}

}
