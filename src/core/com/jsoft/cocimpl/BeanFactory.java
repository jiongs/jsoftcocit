package com.jsoft.cocimpl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;

import com.jsoft.cocimpl.entityengine.impl.PatternAdaptersImpl;
import com.jsoft.cocimpl.entityengine.impl.pattern.CheckEmailAdapter;
import com.jsoft.cocimpl.entityengine.impl.pattern.CheckPhoneAdapter;
import com.jsoft.cocimpl.entityengine.impl.pattern.CodePatternAdapter;
import com.jsoft.cocimpl.entityengine.impl.pattern.DatePatternAdapter;
import com.jsoft.cocimpl.entityengine.impl.pattern.DateTimePatternAdapter;
import com.jsoft.cocimpl.entityengine.impl.pattern.ImagePatternAdapter;
import com.jsoft.cocimpl.entityengine.impl.pattern.PasswordPatternAdapter;
import com.jsoft.cocimpl.orm.generator.impl.EntityGeneratorsImpl;
import com.jsoft.cocimpl.orm.generator.impl.TimeSNGenerator;
import com.jsoft.cocimpl.ui.UIView;
import com.jsoft.cocimpl.ui.UIViews;
import com.jsoft.cocimpl.ui.view.JSONView;
import com.jsoft.cocimpl.ui.view.JSPView;
import com.jsoft.cocimpl.ui.view.SmartyView;
import com.jsoft.cocimpl.ui.view.UIViewsImpl;
import com.jsoft.cocimpl.ui.view.gridcell.LinkCellView;
import com.jsoft.cocimpl.ui.view.gridcell.LinkToFormCellView;
import com.jsoft.cocimpl.ui.view.gridcell.RowActionsCellView;
import com.jsoft.cocimpl.ui.view.gridcell.RowLinkActionsCellView;
import com.jsoft.cocimpl.ui.view.gridcell.security.ActionsAuthCellView;
import com.jsoft.cocimpl.ui.view.gridcell.security.MenuActionsCellView;
import com.jsoft.cocimpl.ui.view.gridcell.security.MenuDatasCellView;
import com.jsoft.cocimpl.ui.view.gridcell.security.MenuFieldsCellView;
import com.jsoft.cocimpl.ui.view.gridcell.security.RowsAuthCellView;
import com.jsoft.cocimpl.ui.view.jcocit.UIButtonsView;
import com.jsoft.cocimpl.ui.view.jcocit.UIEntitiesView;
import com.jsoft.cocimpl.ui.view.jcocit.UIEntityView;
import com.jsoft.cocimpl.ui.view.jcocit.UIFormViews;
import com.jsoft.cocimpl.ui.view.jcocit.UIGridViews;
import com.jsoft.cocimpl.ui.view.jcocit.UIListViews;
import com.jsoft.cocimpl.ui.view.jcocit.UIMenuView;
import com.jsoft.cocimpl.ui.view.jcocit.UISearchBoxView;
import com.jsoft.cocimpl.ui.view.jcocit.UISearchFormView;
import com.jsoft.cocimpl.ui.view.jcocit.UITreeGridViews;
import com.jsoft.cocimpl.ui.view.jcocit.UITreeViews;
import com.jsoft.cocimpl.ui.view.jcocitfield.CKEditorFieldView;
import com.jsoft.cocimpl.ui.view.jcocitfield.ComboBoxFieldView;
import com.jsoft.cocimpl.ui.view.jcocitfield.ComboDateFieldView;
import com.jsoft.cocimpl.ui.view.jcocitfield.ComboDateTimeFieldView;
import com.jsoft.cocimpl.ui.view.jcocitfield.ComboGridFieldView;
import com.jsoft.cocimpl.ui.view.jcocitfield.ComboTreeFieldView;
import com.jsoft.cocimpl.ui.view.jcocitfield.NumberBoxFieldView;
import com.jsoft.cocimpl.ui.view.jcocitfield.OneToManyFieldView;
import com.jsoft.cocimpl.ui.view.jcocitfield.PasswordFieldView;
import com.jsoft.cocimpl.ui.view.jcocitfield.RadioFieldView;
import com.jsoft.cocimpl.ui.view.jcocitfield.SelectFieldView;
import com.jsoft.cocimpl.ui.view.jcocitfield.TextFieldView;
import com.jsoft.cocimpl.ui.view.jcocitfield.TextareaFieldView;
import com.jsoft.cocimpl.ui.view.jcocitfield.UploadFieldView;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.entityengine.PatternAdapter;
import com.jsoft.cocit.entityengine.PatternAdapters;
import com.jsoft.cocit.orm.generator.EntityGenerators;
import com.jsoft.cocit.orm.generator.Generator;
import com.jsoft.cocit.ui.view.UICellView;
import com.jsoft.cocit.ui.view.UIFieldView;
import com.jsoft.cocit.util.ClassUtil;
import com.jsoft.cocit.util.LogUtil;

class BeanFactory {
	private Map<String, Object> beansMap;

	private NutIoc beans;

	static BeanFactory make(ServletContext context) {
		List<String> configs = new ArrayList();

		configs.add(Cocit.class.getPackage().getName().replace(".", "/") + "/BeanFactory.json");

		File iocConfigFile = new File(Cocit.me().getConfig().getConfigDir() + "/iocconfig.json");
		if (iocConfigFile.exists())
			configs.add(iocConfigFile.getAbsolutePath());

		for (String config : configs) {
			LogUtil.info("BeanFactory.make: 加载COC配置文件... {config:%s}", config);
		}

		BeanFactory ret = new BeanFactory();
		ret.beansMap = new HashMap();
		String[] array = new String[configs.size()];
		for (int i = configs.size() - 1; i >= 0; i--) {
			array[i] = configs.get(i);
		}
		ret.beans = new NutIoc(new JsonLoader(array));

		return ret;
	}

	void clear() {
	}

	<T> T getBean(String name) {
		try {
			return (T) beans.get(null, name);
		} catch (Exception e) {
			LogUtil.error("BeanFactory.get: 失败! {name:%s, error: %s}", name, e);
		}

		return null;
	}

	<T> T getBean(Class<T> type) {
		try {
			String name = type.getSimpleName();
			name = name.substring(0, 1).toLowerCase() + name.substring(1);
			return (T) beans.get(null, name);
		} catch (Exception e) {
			LogUtil.error("BeanFactory.get: 失败! {type:%s, error: %s}", type, e);
		}

		return null;
	}

	UIViews getViews() {
		UIViews views = (UIViews) beansMap.get("views");

		if (views == null) {
			views = new UIViewsImpl();

			/*
			 * 添加内置模型视图
			 */
			views.addView(new JSONView());
			views.addView(new JSPView());
			views.addView(new SmartyView());
			views.addView(new UIButtonsView());
			views.addView(new UIEntityView());
			views.addView(new UIFormViews.UIFormView());
			views.addView(new UIFormViews.UISubFormView());
			views.addView(new UIFormViews.UIFormDataView());
			views.addView(new UIGridViews.UIGridView());
			views.addView(new UIGridViews.UIGridDataView());
			views.addView(new UITreeGridViews.UIGridView());
			views.addView(new UITreeGridViews.UIGridDataView());
			views.addView(new UIListViews.UIListView());
			views.addView(new UIListViews.UIListDataView());
			views.addView(new UIMenuView());
			views.addView(new UIEntitiesView());
			views.addView(new UISearchBoxView());
			views.addView(new UISearchFormView());
			views.addView(new UITreeViews.UITreeView());
			views.addView(new UITreeViews.UITreeDataView());

			/*
			 * 添加自定义模型视图
			 */
			List<String> extViews = Cocit.me().getBeansConfig().getViews();
			if (extViews != null) {
				for (String view : extViews) {
					try {
						views.addView((UIView) ClassUtil.forName(view).newInstance());
					} catch (Throwable e) {
						LogUtil.error("BeanFactory.getViews: Invalid UIView! [viewClassName: %s]", view);
					}
				}
			}

			/*
			 * 添加内置字段视图
			 */
			views.addFieldView(new ComboBoxFieldView());
			views.addFieldView(new ComboDateFieldView());
			views.addFieldView(new ComboDateTimeFieldView());
			views.addFieldView(new TextFieldView());
			views.addFieldView(new NumberBoxFieldView());
			views.addFieldView(new SelectFieldView());
			views.addFieldView(new TextareaFieldView());
			views.addFieldView(new ComboGridFieldView());
			views.addFieldView(new ComboTreeFieldView());
			views.addFieldView(new UploadFieldView());
			views.addFieldView(new CKEditorFieldView());
			views.addFieldView(new RadioFieldView());
			views.addFieldView(new PasswordFieldView());
			views.addFieldView(new OneToManyFieldView());

			/*
			 * 添加自定义视图
			 */
			List<String> fieldViews = Cocit.me().getBeansConfig().getFieldViews();
			if (fieldViews != null) {
				for (String view : fieldViews) {
					try {
						views.addFieldView((UIFieldView) ClassUtil.forName(view).newInstance());
					} catch (Throwable e) {
						LogUtil.error("BeanFactory.getViews: Invalid UIFieldView! [viewClassName: %s]", view);
					}
				}
			}

			/**
			 * 
			 */
			views.addCellView(new LinkToFormCellView());
			views.addCellView(new LinkCellView());
			views.addCellView(new RowActionsCellView());
			views.addCellView(new RowLinkActionsCellView());
			views.addCellView(new MenuActionsCellView());
			views.addCellView(new MenuFieldsCellView());
			views.addCellView(new MenuDatasCellView());
			views.addCellView(new ActionsAuthCellView());
			views.addCellView(new RowsAuthCellView());

			/*
			 * 添加自定义视图
			 */
			List<String> cellViews = Cocit.me().getBeansConfig().getCellViews();
			if (cellViews != null) {
				for (String view : cellViews) {
					try {
						views.addCellView((UICellView) ClassUtil.forName(view).newInstance());
					} catch (Throwable e) {
						LogUtil.error("BeanFactory.getViews: Invalid UIGridCellView! [viewClassName: %s]", view);
					}
				}
			}

			beansMap.put("views", views);
		}

		return views;
	}

	PatternAdapters getPatternAdapters() {
		PatternAdapters patternAdapters = (PatternAdapters) beansMap.get("patternAdapters");

		if (patternAdapters == null) {
			patternAdapters = new PatternAdaptersImpl();

			/*
			 * 添加内置Pattern适配器
			 */
			patternAdapters.addAdapter(new DatePatternAdapter());
			patternAdapters.addAdapter(new DateTimePatternAdapter());
			patternAdapters.addAdapter(new ImagePatternAdapter());
			patternAdapters.addAdapter(new PasswordPatternAdapter());
			patternAdapters.addAdapter(new CodePatternAdapter());
			patternAdapters.addAdapter(new CheckEmailAdapter());
			patternAdapters.addAdapter(new CheckPhoneAdapter());

			/*
			 * 添加自定义Pattern适配器
			 */
			List<String> extPatternAdapters = Cocit.me().getBeansConfig().getPatternAdapters();
			if (patternAdapters != null) {
				for (String pa : extPatternAdapters) {
					try {
						patternAdapters.addAdapter((PatternAdapter) ClassUtil.forName(pa).newInstance());
					} catch (Throwable e) {
						LogUtil.error("BeanFactory.getViews: Invalid PatternAdapter! [patternName: %s]", pa);
					}
				}
			}

			beansMap.put("patternAdapters", patternAdapters);
		}

		return patternAdapters;
	}

	public EntityGenerators getEntityGenerators() {
		EntityGenerators entityGenerators = (EntityGenerators) beansMap.get("entityGenerators");

		if (entityGenerators == null) {
			entityGenerators = new EntityGeneratorsImpl();

			entityGenerators.addGenerator(new TimeSNGenerator());

			List<String> list = Cocit.me().getBeansConfig().getEntityGenerators();
			if (list != null) {
				for (String str : list) {
					try {
						entityGenerators.addGenerator((Generator) ClassUtil.forName(str).newInstance());
					} catch (Throwable e) {
						LogUtil.error("BeanFactory.getEntityGenerators: Invalid Generator! [name: %s]", str);
					}
				}
			}

			beansMap.put("entityGenerators", entityGenerators);
		}

		return entityGenerators;
	}
}
