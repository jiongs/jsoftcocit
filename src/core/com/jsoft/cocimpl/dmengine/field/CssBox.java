package com.jsoft.cocimpl.dmengine.field;

import java.io.IOException;

import javax.persistence.Column;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.dmengine.annotation.CocColumn;
import com.jsoft.cocit.dmengine.field.UploadField;
import com.jsoft.cocit.util.ImageUtil;
import com.jsoft.cocit.util.StringUtil;

@CocColumn(uiView = "ui.widget.field.Composite")
public class CssBox extends JsonField<CssBox> {
	@CocColumn(name = "边框宽度", sn = 1, uiView = "ui.widget.field.Spinner")
	protected Integer width;

	@CocColumn(name = "边框高度", sn = 2, uiView = "ui.widget.field.Spinner")
	protected Integer height;

	@Column(length = 2000)
	@CocColumn(name = "样式编辑", sn = 4)
	protected String style;

	@CocColumn(name = "背景", sn = 10, uiView = "ui.widget.field.CssBackground")
	protected CssBackground background;

	@CocColumn(name = "边框", sn = 11, uiView = "ui.widget.field.CssBorder")
	protected CssBorder bsn;

	@CocColumn(name = "顶边框", sn = 12, uiView = "ui.widget.field.CssBorder")
	protected CssBorder bsnTop;

	@CocColumn(name = "底边框", sn = 13, uiView = "ui.widget.field.CssBorder")
	protected CssBorder bsnBottom;

	@CocColumn(name = "左边框", sn = 14, uiView = "ui.widget.field.CssBorder")
	protected CssBorder bsnLeft;

	@CocColumn(name = "右边框", sn = 15, uiView = "ui.widget.field.CssBorder")
	protected CssBorder bsnRight;

	// @CocColumn(name = "字体", sn = 9, uiWidget = "ui.widget.field.CssFont")
	// protected CssFont font;

	public CssBox() {
		this("");
	}

	public CssBox(String str) {
		super(str);
	}

	@Override
	protected void init(CssBox obj) {
		if (obj != null) {
			this.width = obj.width;
			this.height = obj.height;
			this.background = obj.background;
			this.bsn = obj.bsn;
			this.bsnTop = obj.bsnTop;
			this.bsnRight = obj.bsnRight;
			this.bsnBottom = obj.bsnBottom;
			this.bsnLeft = obj.bsnLeft;
			// this.font = obj.font;
			this.style = obj.style;
		}
	}

	public String toCssStyle(boolean autoBgSize) {
		StringBuffer sb = new StringBuffer();

		if (autoBgSize && background != null && background.getImage() != null) {
			UploadField upl = background.getImage();
			String img = upl.toString();
			if (!StringUtil.isBlank(img)) {
				try {
					String rpt = background.getRepeat();
					int[] size = ImageUtil.size(Cocit.me().getContextDir() + img);
					if (!"repeat".equals(rpt)) {
						if (width == null && !"repeat-x".equals(rpt))
							width = size[0];
						if (height == null && !"repeat-y".equals(rpt))
							height = size[1];
					}
				} catch (IOException e) {
				}
			}
		}

		if (width != null && width > 0) {
			sb.append("width:").append(width).append("px;");
		}
		if (height != null && height > 0)
			sb.append("height:").append(height).append("px;");
		if (background != null) {
			sb.append(background.toCssStyle());
		}
		if (bsn != null)
			sb.append(bsn.toCssStyle(""));
		if (bsnTop != null)
			sb.append(bsnTop.toCssStyle("-top"));
		if (bsnRight != null)
			sb.append(bsnRight.toCssStyle("-right"));
		if (bsnBottom != null)
			sb.append(bsnBottom.toCssStyle("-bottom"));
		if (bsnLeft != null)
			sb.append(bsnLeft.toCssStyle("-left"));
		// if (font != null)
		// sb.append(font.toCssStyle());

		return sb.toString();
	}

	public CssBackground getBackground() {
		return background;
	}

	public CssBorder getBorder() {
		return bsn;
	}

	//
	// public CssFont getFont() {
	// return font;
	// }

	public void setBackground(CssBackground background) {
		this.background = background;
	}

	public void setBorder(CssBorder bsn) {
		this.bsn = bsn;
	}

	//
	// public void setFont(CssFont font) {
	// this.font = font;
	// }

	public CssBorder getBorderTop() {
		return bsnTop;
	}

	public void setBorderTop(CssBorder bsnTop) {
		this.bsnTop = bsnTop;
	}

	public CssBorder getBorderRight() {
		return bsnRight;
	}

	public void setBorderRight(CssBorder bsnRight) {
		this.bsnRight = bsnRight;
	}

	public CssBorder getBorderBottom() {
		return bsnBottom;
	}

	public void setBorderBottom(CssBorder bsnBottom) {
		this.bsnBottom = bsnBottom;
	}

	public CssBorder getBorderLeft() {
		return bsnLeft;
	}

	public void setBorderLeft(CssBorder bsnLeft) {
		this.bsnLeft = bsnLeft;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

}
