package com.jsoft.cocimpl.entityengine.field;

import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.field.Upload;
import com.jsoft.cocit.util.MVCUtil;
import com.jsoft.cocit.util.StringUtil;

@CocColumn(name = "背景", uiView = "ui.widget.field.Composite")
public class CssBackground extends JsonField<CssBackground> {

	@CocColumn(name = "背景颜色", uiView = "ui.widget.field.CssColor", sn = 1)
	protected String color;

	@CocColumn(name = "背景图片", pattern = "*.jpg;*.gif;*.png", sn = 2)
	protected Upload image;

	@CocColumn(name = "图片平铺", dicOptions = ":平铺,repeat-x:横向,repeat-y:纵向,no-repeat:不平铺,inherit: 继承", sn = 3)
	protected String repeat;

	@CocColumn(name = "水平对齐", dicOptions = ":水平,left:居左,center:居中,right:居右", sn = 4)
	protected String positionX;

	@CocColumn(name = "垂直对齐", dicOptions = ":垂直,top:居顶,center:居中,bottom:居底", sn = 5)
	protected String positionY;

	public CssBackground() {
		this("");
	}

	public CssBackground(String str) {
		super(str);
	}

	@Override
	protected void init(CssBackground obj) {
		if (obj != null) {
			this.color = obj.color;
			this.image = obj.image;
			this.repeat = obj.repeat;
			this.positionX = obj.positionX;
			this.positionY = obj.positionY;
		}
	}

	public String toCssStyle() {
		if (StringUtil.isBlank(color) && (image == null || StringUtil.isBlank(image.toString())) && StringUtil.isBlank(repeat) && StringUtil.isBlank(positionX) && StringUtil.isBlank(positionY)) {
			return "";
		}

		StringBuffer sb = new StringBuffer();

		sb.append("background:");
		if (!StringUtil.isBlank(color))
			sb.append(color);
		if (image != null && !StringUtil.isBlank(image.toString()))
			sb.append(" ").append("url(").append(MVCUtil.makeUrl(image.toString())).append(")");
		if (!StringUtil.isBlank(repeat))
			sb.append(" ").append(repeat);
		if (!StringUtil.isBlank(positionX))
			sb.append(" ").append(positionX);
		if (!StringUtil.isBlank(positionY))
			sb.append(" ").append(positionY);
		sb.append(";");
		//
		// if (image != null && !StringUtil.isEmpty(image.toString()) && image.toString().toLowerCase().endsWith(".png")) {
		// sb.append("filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true,");
		// if (repeat != null && !"no-repeat".equals(repeat)) {
		// sb.append("sizingMethod=scale,");
		// }else{
		// sb.append("sizingMethod=image,");//corp,image
		// }
		// sb.append("src='");
		// sb.append(MvcUtil.contextPath(image.toString().trim()));
		// sb.append("');_background:none;");
		// }

		return sb.toString();
	}

	public String getColor() {
		return color;
	}

	public Upload getImage() {
		return image;
	}

	public String getRepeat() {
		return repeat;
	}

	public String getPositionX() {
		return positionX;
	}

	public String getPositionY() {
		return positionY;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setImage(Upload image) {
		this.image = image;
	}

	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}

	public void setPositionX(String positionX) {
		this.positionX = positionX;
	}

	public void setPositionY(String positionY) {
		this.positionY = positionY;
	}

}
