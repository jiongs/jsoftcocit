package com.jsoft.cocimpl.config.impl;

import java.text.MessageFormat;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.config.IConfig;
import com.jsoft.cocit.config.IMessageConfig;
import com.jsoft.cocit.util.StringUtil;

public class MessageConfig extends BaseConfig implements IMessageConfig {
	public MessageConfig() {
		super(Cocit.me().getConfig().getI18NConfig());
	}

	public IConfig copy() {
		MessageConfig ret = new MessageConfig();
		this.copyTo(ret);

		return ret;
	}

	@Override
	public String getMsg(String key, Object... args) {
		String msg = this.get(key);
		if (StringUtil.isBlank(msg)) {

			if (key.indexOf("%s") > -1) {
				return String.format(key, args);
			}

			return key;
		} else {
			return MessageFormat.format(msg, args);
		}
	}
}
