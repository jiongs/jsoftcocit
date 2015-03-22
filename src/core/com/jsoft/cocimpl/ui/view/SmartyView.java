package com.jsoft.cocimpl.ui.view;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.lilystudio.smarty4j.Context;
import org.lilystudio.smarty4j.Engine;
import org.lilystudio.smarty4j.Template;
import org.lilystudio.smarty4j.TemplateException;

import com.jsoft.cocimpl.ui.UIView;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.config.ICommonConfig;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.SmartyModel;
import com.jsoft.cocit.util.FileUtil;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.StringUtil;

public class SmartyView implements UIView<SmartyModel> {

	private Map<String, Engine> engineMap;

	private ICommonConfig commonConfig;

	public SmartyView() {
		engineMap = new HashMap();
		commonConfig = Cocit.me().getConfig();
	}

	@Override
	public String getName() {
		return ViewNames.VIEW_SMARTY;
	}

	public void render(Writer out, SmartyModel model) throws Exception {
		this.render(out, model.getContext(), model.getTemplatePath());
	}

	public void render(Writer out, Map context, String templateName) throws Exception {
		String templateDir = "";
		if (templateName.charAt(0) == '/') {
			if (StringUtil.isBlank(templateDir))
				templateDir = Cocit.me().getContextDir();
		} else {
			if (StringUtil.isBlank(templateDir)) {
				if (new File(commonConfig.getClassDir() + File.separator + templateName).exists()) {
					templateDir = commonConfig.getClassDir();
					templateName = File.separator + templateName;
				}
			} else {
				templateName = "/" + templateName;
			}
		}

		Engine engine = getEngine(templateDir);

		Context ctx = new Context();
		ctx.putAll(context);

		engine.getTemplate(templateName).merge(ctx, out);
	}

	private Engine getEngine(String tplDir) throws IOException {
		if (tplDir == null)
			tplDir = "";

		Engine engine = engineMap.get(tplDir);
		if (engine == null) {
			engine = new DemsyEngine();
			engine.setTemplatePath(tplDir);
			engine.setDebug(!Cocit.me().getConfig().isProductMode());

			engineMap.put(tplDir, engine);
		}
		return engine;
	}

	protected void renderExpression(String classPath, String expression, Map context, Writer out) throws Exception {
		DemsyEngine engine = new DemsyEngine();
		engine.setTemplatePath("");

		Context ctx = new Context();
		ctx.putAll(context);

		engine.getExpressionTemplate(classPath, expression).merge(ctx, out);
	}

	private static class DemsyEngine extends Engine {

		private boolean supportCached = false;

		private Map<String, Template> classTemplates = new HashMap<String, Template>(256);

		private Template get(String name) {
			if (!supportCached)
				return null;

			return classTemplates.get(name);
		}

		private void cache(String name, Template t) {

			if (!supportCached)
				return;

			classTemplates.put(name, t);
		}

		public DemsyEngine() {
			super();
		}

		public Template getTemplate(String name) throws IOException, TemplateException {
			LogUtil.debug("MVC.getTemplate: ...... [name: %s]", name);

			String path = this.getTemplatePath() + name;
			int idx = path.indexOf("/../");
			if (idx > -1) {
				String prev = path.substring(0, idx);
				String post = path.substring(idx + 3);
				idx = prev.lastIndexOf("/");
				if (idx > -1) {
					path = prev.substring(0, idx) + post;
				}
			}

			if (this.isDebug()) {
				InputStream is = null;
				InputStreamReader isr = null;
				try {
					is = FileUtil.findFileAsStream(path);
					if (is == null) {
						throw new IOException("文件不存在! [" + path + "]");
					}
					isr = new InputStreamReader(is, getEncoding());
					return new SmartyTemplate(this, isr, name);
				} finally {
					if (isr != null)
						try {
							isr.close();
						} catch (Exception e) {
						}
					if (is != null)
						try {
							is.close();
						} catch (Exception e) {
						}
				}
			}

			Template template = get(name);

			if (template != null) {
				return template;
			}

			InputStream is = null;
			InputStreamReader isr = null;
			try {
				String fileName = name;
				if (name.startsWith("/")) {
					fileName = Cocit.me().getContextDir() + name;
				}
				is = FileUtil.findFileAsStream(fileName);
				if (is == null) {
					throw new IOException("文件不存在! [" + fileName + "]");
				}
				isr = new InputStreamReader(is, getEncoding());
				template = new SmartyTemplate(this, isr, name);
				cache(name, template);
			} finally {
				if (isr != null)
					try {
						isr.close();
					} catch (Exception e) {
					}
				if (is != null)
					try {
						is.close();
					} catch (Exception e) {
					}
			}

			return template;
		}

		public Template getExpressionTemplate(String classPath, String expression) throws IOException, TemplateException {
			StringReader isr = null;
			try {
				isr = new StringReader(expression);
				return new SmartyTemplate(this, isr, classPath);
			} finally {
				if (isr != null)
					try {
						isr.close();
					} catch (Exception e) {
					}
			}
		}
	}

}
