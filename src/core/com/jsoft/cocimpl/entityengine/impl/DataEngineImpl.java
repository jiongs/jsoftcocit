package com.jsoft.cocimpl.entityengine.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.jsoft.cocimpl.log.LoginLogUtil;
import com.jsoft.cocimpl.log.OperationLogUtil;
import com.jsoft.cocimpl.log.RunningLogUtil;
import com.jsoft.cocimpl.orm.Pager;
import com.jsoft.cocit.entity.log.ILoginLog;
import com.jsoft.cocit.entity.log.IOperationLog;
import com.jsoft.cocit.entity.log.IRunningLog;
import com.jsoft.cocit.entityengine.DataEngine;
import com.jsoft.cocit.entityengine.bizplugin.BizEvent;
import com.jsoft.cocit.entityengine.bizplugin.IBizPlugin;
import com.jsoft.cocit.orm.ExtOrm;
import com.jsoft.cocit.orm.NoTransConnCallback;
import com.jsoft.cocit.orm.Orm;
import com.jsoft.cocit.orm.OrmCallback;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.orm.expr.NullCndExpr;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.LogUtil;

/**
 * 实体引擎
 * 
 * @author yongshan.ji
 * @preserve
 */
public class DataEngineImpl implements DataEngine {
	private ExtOrm orm;

	// 延迟操作队列
	// private Queue<DelayAction> actionQueue;

	// public EntityEngineImpl() {
	// actionQueue = new LinkedBlockingQueue();
	// new ExecuteActionThread(actionQueue).start();
	// }

	DataEngineImpl(Orm orm) {
		this.orm = (ExtOrm) orm;

		// actionQueue = new LinkedBlockingQueue();
		// new ExecuteActionThread(actionQueue).start();
	}

	public void release() {
		orm = null;
	}

	//
	//
	// public EntityEngine me() {
	// return this;
	// }
	//

	public Orm orm() {
		return orm;
	}

	protected BizEvent createEvent() {
		BizEvent e = new BizEvent();
		e.setOrm(orm);

		return e;
	}

	private void fireLoadEvent(IBizPlugin[] plugins, BizEvent event) {
		if (plugins != null) {
			for (IBizPlugin l : plugins) {
				l.beforeLoad(event);
			}
		}
	}

	private void fireLoadedEvent(IBizPlugin[] plugins, BizEvent event) {
		if (plugins != null) {
			for (IBizPlugin l : plugins) {
				l.afterLoad(event);
			}
		}
	}

	private void fireBeforeEvent(IBizPlugin[] plugins, BizEvent event) {
		if (plugins != null) {
			for (IBizPlugin l : plugins) {
				l.beforeSubmit(event);
			}
		}
	}

	private void fireAfterEvent(IBizPlugin[] plugins, BizEvent event) {
		if (plugins != null) {
			for (IBizPlugin l : plugins) {
				l.afterSubmit(event);
			}
		}
	}

	// ========================================================================
	// 同步实体管理
	// ========================================================================

	public int save(Object obj, IBizPlugin... plugins) {
		return save(obj, null, plugins);
	}

	public int save(final Object obj, final CndExpr fieldRexpr, final IBizPlugin... plugins) {
		final BizEvent event = createEvent();
		Trans.exec(new Atom() {
			public void run() {
				event.setDataObject(obj);
				event.setExpr(fieldRexpr);

				fireBeforeEvent(plugins, event);

				event.setReturnValue(orm.save(event.getDataObject(), null));

				fireAfterEvent(plugins, event);
			}
		});
		return (Integer) event.getReturnValue();
	}

	public int updateMore(final Object obj, final CndExpr expr, final IBizPlugin... plugins) {
		final BizEvent event = createEvent();
		Trans.exec(new Atom() {
			public void run() {
				event.setDataObject(obj);
				event.setExpr(expr);

				fireBeforeEvent(plugins, event);

				event.setReturnValue(orm.updateMore(event.getDataObject(), (CndExpr) event.getExpr()));

				fireAfterEvent(plugins, event);
			}
		});
		return (Integer) event.getReturnValue();
	}

	public int delete(final Object entity, final IBizPlugin... plugins) {
		final BizEvent event = createEvent();
		Trans.exec(new Atom() {
			public void run() {
				event.setDataObject(entity);

				fireBeforeEvent(plugins, event);

				event.setReturnValue(orm.delete(entity));

				fireAfterEvent(plugins, event);
			}
		});
		return (Integer) event.getReturnValue();
	}

	public int delete(final Class klass, final Long id, final IBizPlugin... plugins) {
		final BizEvent event = createEvent();
		Trans.exec(new Atom() {
			public void run() {
				event.setTypeOfEntity(klass);
				event.setDataID(id);

				fireBeforeEvent(plugins, event);

				event.setReturnValue(orm.delete(event.getTypeOfEntity(), event.getDataID()));

				fireAfterEvent(plugins, event);
			}
		});
		return (Integer) event.getReturnValue();
	}

	public int deleteMore(final Class klass, final CndExpr expr, final IBizPlugin... plugins) {
		final BizEvent event = createEvent();
		Trans.exec(new Atom() {
			public void run() {
				event.setTypeOfEntity(klass);
				event.setExpr(expr);

				fireBeforeEvent(plugins, event);

				event.setReturnValue(orm.delete(event.getTypeOfEntity(), (CndExpr) event.getExpr()));

				fireAfterEvent(plugins, event);
			}
		});
		return (Integer) event.getReturnValue();
	}

	public Object load(Class klass, Long id, IBizPlugin... plugins) {
		return this.load(klass, id, null, plugins);
	}

	public Object load(final Class klass, final Long id, final CndExpr fieldRexpr, final IBizPlugin... plugins) {
		final BizEvent event = createEvent();
		Trans.exec(new Atom() {
			public void run() {
				event.setTypeOfEntity(klass);
				event.setExpr(fieldRexpr);
				event.setDataID(id);

				fireLoadEvent(plugins, event);

				event.setReturnValue(orm.get(event.getTypeOfEntity(), event.getDataID(), (CndExpr) event.getExpr()));

				fireLoadedEvent(plugins, event);
			}
		});
		return event.getReturnValue();
	}

	public Object load(final Class klass, final CndExpr expr, final IBizPlugin... plugins) {
		final BizEvent event = createEvent();
		Trans.exec(new Atom() {
			public void run() {
				event.setExpr(expr);
				event.setTypeOfEntity(klass);

				fireLoadEvent(plugins, event);

				event.setReturnValue(orm.get(event.getTypeOfEntity(), (CndExpr) event.getExpr()));

				fireLoadedEvent(plugins, event);
			}
		});
		return event.getReturnValue();
	}

	public int count(final Class klass, final CndExpr expr, final IBizPlugin... plugins) {
		final BizEvent event = createEvent();
		Trans.exec(new Atom() {
			public void run() {
				event.setExpr(expr);
				event.setTypeOfEntity(klass);

				fireLoadEvent(plugins, event);

				event.setReturnValue(orm.count(event.getTypeOfEntity(), (CndExpr) event.getExpr()));

				fireLoadedEvent(plugins, event);
			}
		});
		return (Integer) event.getReturnValue();
	}

	public List query(final Class klass, final CndExpr expr, final IBizPlugin... plugins) {
		final BizEvent event = createEvent();
		Trans.exec(new Atom() {
			public void run() {
				event.setTypeOfEntity(klass);
				event.setExpr(expr);

				fireLoadEvent(plugins, event);

				event.setReturnValue(orm.query(event.getTypeOfEntity(), (CndExpr) event.getExpr()));

				fireLoadedEvent(plugins, event);
			}
		});
		return (List) event.getReturnValue();
	}

	public List query(final Pager pager, final IBizPlugin... plugins) {
		final BizEvent event = createEvent();
		Trans.exec(new Atom() {
			public void run() {
				event.setDataObject(pager);

				fireLoadEvent(plugins, event);

				event.setReturnValue(orm.query(pager));

				fireLoadedEvent(plugins, event);
			}
		});
		return (List) event.getReturnValue();
	}

	public Object run(final Object obj, final CndExpr expr, final IBizPlugin... plugins) {
		final BizEvent event = createEvent();
		Trans.exec(new Atom() {
			public void run() {
				event.setDataObject(obj);
				event.setExpr(expr);
				fireBeforeEvent(plugins, event);
				// noop
				fireAfterEvent(plugins, event);
			}
		});
		return event.getReturnValue();
	}

	public Object run(final OrmCallback callback) {
		final List ret = new ArrayList();
		Trans.exec(new Atom() {
			public void run() {
				ret.add(callback.invoke(orm));
			}
		});
		return ret.get(0);
	}

	public Object run(final NoTransConnCallback conn) {
		final List ret = new ArrayList();
		Trans.exec(new Atom() {
			public void run() {
				ret.add(orm.run(conn));
			}
		});
		return ret.get(0);
	}

	public Object asynRun(Object obj, CndExpr fieldRexpr, IBizPlugin... plugins) {
		BizEvent event = createEvent();
		event.setDataObject(obj);
		event.setExpr(fieldRexpr);
		put(new DelayAction(ActionType.run, event, plugins));

		return event.getReturnValue();
	}

	// ========================================================================
	// 异步实体管理
	// ========================================================================

	public void asynSave(Object obj, IBizPlugin... plugins) {
		BizEvent event = createEvent();
		event.setDataObject(obj);
		put(new DelayAction(ActionType.save, event, plugins));
	}

	public void asynSave(Object obj, CndExpr fieldRexpr, IBizPlugin... plugins) {
		BizEvent event = createEvent();
		event.setDataObject(obj);
		event.setExpr(fieldRexpr);
		put(new DelayAction(ActionType.save, event, plugins));
	}

	public void asynUpdateMore(Object obj, CndExpr expr, IBizPlugin... plugins) {
		BizEvent event = createEvent();
		event.setDataObject(obj);
		event.setExpr(expr);
		put(new DelayAction(ActionType.updateMore, event, plugins));
	}

	public void asynDelete(Object obj, IBizPlugin... plugins) {
		BizEvent event = createEvent();
		event.setDataObject(obj);
		put(new DelayAction(ActionType.delete, event, plugins));
	}

	public void asynDeleteMore(Class klass, CndExpr expr, IBizPlugin... plugins) {
		BizEvent event = createEvent();
		event.setTypeOfEntity(klass);
		event.setExpr(expr);
		put(new DelayAction(ActionType.deleteMore, event, plugins));
	}

	public void asynQuery(Pager pager, IBizPlugin... plugins) {
		BizEvent event = createEvent();
		event.setDataObject(pager);
		put(new DelayAction(ActionType.query, event, plugins));
	}

	private void put(DelayAction action) {
		if (action == null) {
			return;
		}
		// synchronized (actionQueue) {
		// int size = actionQueue.size();
		// actionQueue.add(action);
		// if (size == 0) {
		// actionQueue.notifyAll();
		// }
		// }
	}

	private enum ActionType {
		save, updateMore, delete, deleteMore, query, run;
	}

	private static class DelayAction {
		ActionType type;

		BizEvent event;

		IBizPlugin[] plugins;

		private DelayAction(ActionType type, BizEvent event, IBizPlugin... plugins) {
			this.type = type;
			this.event = event;
			this.plugins = plugins;
		}

	}

	@SuppressWarnings("unused")
	private class ExecuteActionThread extends Thread {
		private Queue<DelayAction> actionQueue;

		ExecuteActionThread(Queue<DelayAction> queue) {
			this.actionQueue = queue;
		}

		protected void execute(DelayAction action) throws Exception {
			BizEvent event = action.event;
			if (action.type == ActionType.save) {
				if (event.getDataObject() instanceof IRunningLog) {
					RunningLogUtil.get().save((IRunningLog) event.getDataObject());
					return;
				}
				if (event.getDataObject() instanceof ILoginLog) {
					LoginLogUtil.get().save((ILoginLog) event.getDataObject());
					return;
				}
				if (event.getDataObject() instanceof IOperationLog) {
					OperationLogUtil.get().save((IOperationLog) event.getDataObject());
					return;
				}
				if (event.getDataObject() instanceof IRunningLog) {
					RunningLogUtil.get().save((IRunningLog) event.getDataObject());
					return;
				}
				DataEngineImpl.this.save((Object) event.getDataObject(), (NullCndExpr) event.getExpr(), action.plugins);
			} else if (action.type == ActionType.updateMore) {
				DataEngineImpl.this.updateMore(event.getDataObject(), (CndExpr) event.getExpr(), action.plugins);
			} else if (action.type == ActionType.delete) {
				DataEngineImpl.this.delete(event.getDataObject(), action.plugins);
			} else if (action.type == ActionType.deleteMore) {
				DataEngineImpl.this.deleteMore(event.getTypeOfEntity(), (CndExpr) event.getExpr(), action.plugins);
			} else if (action.type == ActionType.query) {
				DataEngineImpl.this.query((Pager) event.getDataObject(), action.plugins);
			} else if (action.type == ActionType.run) {
				DataEngineImpl.this.run(event.getDataObject(), (CndExpr) event.getExpr(), action.plugins);
			}
		}

		public void run() {
			super.run();
			while (true) {
				synchronized (actionQueue) {
					if (actionQueue.size() == 0) {
						try {
							actionQueue.wait();
						} catch (InterruptedException e) {
							LogUtil.error("异步BizSession: 任务队列等待出错! %s", ExceptionUtil.msg(e));
							break;
						}
					}
				}
				if (actionQueue.size() == 0) {
					continue;
				}
				DelayAction action = actionQueue.poll();
				try {
					execute(action);
				} catch (Throwable e) {
					Object obj = action.event.getDataObject();
					if (obj instanceof IRunningLog) {
						System.err.println("异步BizSession: 执行任务出错! [action=" + action + ", obj=" + obj + "] " + e);
					} else {
						LogUtil.error("异步BizSession: 执行任务出错! [action=%s, obj=%s] %s ", action, obj, e);
					}
				}
			}
		}
	}
}
