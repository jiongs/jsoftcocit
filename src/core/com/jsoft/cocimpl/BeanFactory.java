package com.jsoft.cocimpl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.nutz.resource.Scans;

import com.jsoft.cocimpl.dmengine.impl.CommandInterceptorsImpl;
import com.jsoft.cocimpl.dmengine.impl.PatternAdaptersImpl;
import com.jsoft.cocimpl.dmengine.impl.pattern.CheckEmailAdapter;
import com.jsoft.cocimpl.dmengine.impl.pattern.CheckPhoneAdapter;
import com.jsoft.cocimpl.dmengine.impl.pattern.CodePatternAdapter;
import com.jsoft.cocimpl.dmengine.impl.pattern.DatePatternAdapter;
import com.jsoft.cocimpl.dmengine.impl.pattern.DateTimePatternAdapter;
import com.jsoft.cocimpl.dmengine.impl.pattern.ImagePatternAdapter;
import com.jsoft.cocimpl.dmengine.impl.pattern.PasswordPatternAdapter;
import com.jsoft.cocimpl.orm.generator.impl.EntityGeneratorsImpl;
import com.jsoft.cocimpl.orm.generator.impl.TimeSNGenerator;
import com.jsoft.cocimpl.ui.view.JSONView;
import com.jsoft.cocimpl.ui.view.JSPView;
import com.jsoft.cocimpl.ui.view.SmartyView;
import com.jsoft.cocimpl.ui.view.UIViewsImpl;
import com.jsoft.cocimpl.ui.view.gridcell.LinkCellView;
import com.jsoft.cocimpl.ui.view.jcocit.UIEntityView;
import com.jsoft.cocimpl.ui.view.jcocitfield.TextFieldView;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.dmengine.IPatternAdapter;
import com.jsoft.cocit.dmengine.IPatternAdapters;
import com.jsoft.cocit.dmengine.command.ICommandInterceptor;
import com.jsoft.cocit.dmengine.command.ICommandInterceptors;
import com.jsoft.cocit.orm.generator.EntityGenerators;
import com.jsoft.cocit.orm.generator.Generator;
import com.jsoft.cocit.ui.UIView;
import com.jsoft.cocit.ui.UIViews;
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

			/*
			 * 添加自定义模型视图
			 */
			List<String> extViews = Cocit.me().getBeansConfig().getViews();
			if (extViews == null) {
				extViews = new ArrayList();
			}
			extViews.add(LinkCellView.class.getPackage().getName());
			extViews.add(UIEntityView.class.getPackage().getName());
			extViews.add(TextFieldView.class.getPackage().getName());
			for (String view : extViews) {
				List<Class<?>> types = Scans.me().scanPackage(view);
				for (Class type : types) {
					if (UIView.class.isAssignableFrom(type)) {
						try {
							views.addView((UIView) type.newInstance());
						} catch (Throwable e1) {
						}
					} else if (UIFieldView.class.isAssignableFrom(type)) {
						try {
							views.addFieldView((UIFieldView) type.newInstance());
						} catch (Throwable e1) {
						}
					} else if (UICellView.class.isAssignableFrom(type)) {
						try {
							views.addCellView((UICellView) type.newInstance());
						} catch (Throwable e1) {
						}
					}
				}
			}

			beansMap.put("views", views);
		}

		return views;
	}

	IPatternAdapters getPatternAdapters() {
		IPatternAdapters patternAdapters = (IPatternAdapters) beansMap.get("patternAdapters");

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
						patternAdapters.addAdapter((IPatternAdapter) ClassUtil.forName(pa).newInstance());
					} catch (Throwable e) {

						List<Class<?>> types = Scans.me().scanPackage(pa);
						for (Class type : types) {
							if (IPatternAdapter.class.isAssignableFrom(type)) {
								try {
									patternAdapters.addAdapter((IPatternAdapter) type.newInstance());
								} catch (Throwable e1) {
								}
							}
						}

					}
				}
			}

			beansMap.put("patternAdapters", patternAdapters);
		}

		return patternAdapters;
	}

	ICommandInterceptors getCommandInterceptors() {
		ICommandInterceptors commandInterceptors = (ICommandInterceptors) beansMap.get("commandInterceptors");

		if (commandInterceptors == null) {
			commandInterceptors = new CommandInterceptorsImpl();

			// /*
			// * 添加内置CommandReceiver适配器
			// */
			// commandInterceptors.addCommandInterceptor(new DeleteCommand());
			// commandInterceptors.addCommandInterceptor(new QueryCommand());
			// commandInterceptors.addCommandInterceptor(new SaveCommand());
			// commandInterceptors.addCommandInterceptor(new GetCommand());
			// commandInterceptors.addCommandInterceptor(new EvalProxyActionCommand());

			/*
			 * 添加自定义CommandReceiver适配器
			 */
			List<String> packages = Cocit.me().getBeansConfig().getCommandInterceptors();
			if (packages == null) {
				packages = new ArrayList();
			}
			if (!packages.contains("com.jsoft.cocit.dmengine.command.impl")) {
				packages.add("com.jsoft.cocit.dmengine.command.impl");
			}
			if (commandInterceptors != null) {
				for (String pa : packages) {
					try {
						commandInterceptors.addCommandInterceptor((ICommandInterceptor) ClassUtil.forName(pa).newInstance());
					} catch (Throwable e) {

						List<Class<?>> types = Scans.me().scanPackage(pa);
						for (Class type : types) {
							if (ICommandInterceptor.class.isAssignableFrom(type)) {
								try {
									commandInterceptors.addCommandInterceptor((ICommandInterceptor) type.newInstance());
								} catch (Throwable e1) {
								}
							}
						}

					}
				}
			}

			beansMap.put("commandInterceptors", commandInterceptors);
		}

		return commandInterceptors;
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

						List<Class<?>> types = Scans.me().scanPackage(str);
						for (Class type : types) {
							if (Generator.class.isAssignableFrom(type)) {
								try {
									entityGenerators.addGenerator((Generator) type.newInstance());
								} catch (Throwable e1) {
								}
							}
						}

					}
				}
			}

			beansMap.put("entityGenerators", entityGenerators);
		}

		return entityGenerators;
	}
}
