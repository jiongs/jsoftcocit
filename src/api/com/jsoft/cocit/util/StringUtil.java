// $codepro.audit.disable unnecessaryCast
package com.jsoft.cocit.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.jsoft.cocimpl.util.ResponseUtil;
import com.jsoft.cocit.baseentity.IDataEntity;
import com.jsoft.cocit.exception.CocException;

/**
 * 
 * @author Ji Yongshan
 * @preserve public
 * 
 */
public abstract class StringUtil {

	/**
	 * 验证护照
	 * <p>
	 * 因私普通护照号码格式有:14/15+7位数,G+8位数；
	 * <p>
	 * 因公普通的是:P.+7位数；
	 * <p>
	 * 公务的是：S.+7位数 或者 S+8位数；
	 * <p>
	 * 以D开头的是外交护照.D=diplomatic
	 * 
	 * @param id
	 * @return
	 */
	public static boolean isPassport(String id) {
		String pattern = "(P\\d{7})|(G\\d{8})";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(id);

		return m.matches();
	}

	/**
	 * 检查其他身份ID
	 * 
	 * @param id
	 * @return
	 */
	public static boolean isOtherNID(String id) {
		if (isBlank(id))
			return false;

		int len = id.trim().length();

		// 护照号码
		if (len >= 7 && len <= 10) {
			// char ch = id.trim().toUpperCase().charAt(0);
			// if (ch >= 'A' && ch <= 'Z') {
			// if (len <= 8)
			// return true;
			// } else {
			// if (len == 10)
			// return true;
			// }

			return true;
		}

		return false;
	}

	/**
	 * 检查身份证号码
	 * 
	 * @param id_number
	 * @return
	 */
	public static boolean isNID(String id) {
		if (isBlank(id))
			return false;

		String pattern = "((11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65|71|81|82|91)\\d{4})((((19|20)(([02468][048])|([13579][26]))0229))|((20[0-9][0-9])|(19[0-9][0-9]))((((0[1-9])|(1[0-2]))((0[1-9])|(1\\d)|(2[0-8])))|((((0[1,3-9])|(1[0-2]))(29|30))|(((0[13578])|(1[02]))31))))((\\d{3}(x|X))|(\\d{4}))";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(id);

		int[] weight = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		char[] checkDigit = new char[] { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };

		int sum = 0;
		if (m.matches()) {
			for (int i = 0; i < 17; i++) {
				int b = Integer.parseInt(id.substring(i, i + 1));
				int a = weight[i];
				sum = a * b + sum;
			}
			int mod = sum % 11;

			String s = "" + checkDigit[mod];
			String last = id.substring(id.length() - 1);

			return s.equalsIgnoreCase(last);
		}

		return false;
	}

	/**
	 * 检查手机号码
	 * 
	 * @param tel
	 * @return
	 */
	public static boolean isMobile(String tel) {
		Pattern pattern = Pattern.compile("\\d{11}$");// Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

		Matcher matcher = pattern.matcher(tel);

		return matcher.matches();
	}

	// public static String escapeHTML(String input) {
	// if (input == null) {
	// return "";
	// }
	// String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "");
	// str = str.replaceAll("\"", "&quot;");
	// return str;
	// }

	public static String escapeHtml(String value) {
		// if (value == null) {
		// return "";
		// }
		// return value//
		// .replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")//
		// .replace("<", "&lt;")//
		// .replace(">", "&gt;")//
		// .replace("\"", "&quot;")//
		// .replace(" ", "&nbsp;&nbsp;")//
		// .replace("\r\n", "<br />")//
		// .replace("\n", "<br />")//
		// .replace("\r", "<br />")//
		// ;

		return ResponseUtil.escapeHtml(value);
	}

	/**
	 * 判断字符串是否为空或一串空白？
	 * 
	 * @param str
	 * @return 参数为null或空白串都将返回true，否则返回false。
	 */
	public static boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}

	public static boolean hasContent(String str) {
		return !isBlank(str);
	}

	/**
	 * 剪切字符串两端的空白。
	 * 
	 * @param str
	 * @return 参数为null将返回空串"", 否则返回str.trim();
	 */
	public static String trim(String str) {
		if (str == null)
			return "";

		return str.trim();
	}

	public static String[] toArray(String str) {
		return toArray(str, null);
	}

	public static Option[] toOptions(String strOptions) {
		String[] strs = StringUtil.toArray(strOptions, ",;，；\r\t\n");
		Option[] options = new Option[strs.length];
		int i = 0;
		for (String item : strs) {
			item = item.trim();
			int idx = item.indexOf(":");
			if (idx < 0) {
				idx = item.indexOf("：");
			}
			if (idx > -1) {
				options[i++] = Option.make(item.substring(idx + 1).trim(), item.substring(0, idx).trim());
			} else {
				options[i++] = Option.make(item, item);
			}
		}
		return options;
	}

	public static String[] toArray(String str, String token) {
		List<String> list = toList(str, token);

		String[] array = new String[list.size()];

		for (int i = list.size() - 1; i >= 0; i--) {
			array[i] = list.get(i);
		}

		return array;
	}

	public static List<String> toList(String str) {
		return toList(str, null);
	}

	public static List<String> toList(String str, String token) {
		List<String> list = new ArrayList();

		if (isBlank(str)) {
			return list;
		}

		if (token == null || token.length() == 0)
			token = ";,|；， ";

		if (token.length() == 1) {
			int idx = str.indexOf(token);
			while (idx > -1) {
				list.add(str.substring(0, idx));
				str = str.substring(idx + 1);
				idx = str.indexOf(token);
			}
			list.add(str);
		} else {
			StringTokenizer st = new StringTokenizer(str, token);
			while (st.hasMoreElements()) {
				list.add((String) st.nextElement());
			}
		}

		return list;
	}

	/**
	 * Hex解密
	 * 
	 * @param str
	 * @return
	 * @throws IOException
	 */
	public static String decodeHex(String str) {
		if (str == null)
			return null;

		// return new String(BinaryCodec.fromAscii(str.toCharArray()));

		try {
			return new String(Hex.decodeHex(str.toCharArray()));
		} catch (DecoderException e) {
			LogUtil.warn("", e);
			return null;
		}
	}

	/**
	 * Hex加密
	 * 
	 * @param str
	 * @return
	 */
	public static String encodeHex(String str) {
		if (str == null)
			return null;

		// return new String(BinaryCodec.toAsciiBytes(str.getBytes()));
		return new String(Hex.encodeHex(str.getBytes()));
	}

	/**
	 * 将指定的值转换成特定类型的对象，可以是String/Long/Integer/Short/Byte/Double/Float/Boolean/Date/Number。
	 * <p>
	 * 如果值类型不属于上面的任何类型，则将value当作一个JSON文本，并试图将其转换成指定类型的java对象。
	 * 
	 * @param text
	 *            文本字符串值
	 * @param defaultReturn
	 *            返回的默认值
	 * @return 转换后的值对象
	 */
	public static <T> T castTo(String text, T defaultReturn) {

		if (text == null)
			return defaultReturn;
		if (defaultReturn == null)
			return (T) text;

		Class valueType = defaultReturn.getClass();

		try {
			T ret = (T) castTo(text, valueType);
			if (ret != null)
				return ret;
		} catch (Throwable e) {
			LogUtil.error("将文本转换成指定的Java对象失败！text=%s, valueType=%s, defaultReturn=%s", text, valueType, defaultReturn, e);
		}

		return defaultReturn;
	}

	/**
	 * 将指定的值转换成特定类型的对象，可以是String/Long/Integer/Short/Byte/Double/Float/Boolean/Date/Number。
	 * <p>
	 * 如果值类型不属于上面的任何类型，则将value当作一个JSON文本，并试图将其转换成指定类型的java对象。
	 * 
	 * @param text
	 *            文本字符串值
	 * @param valueType
	 *            需要转换的值类型
	 * @return 转换后的值对象
	 */
	public static <T> T castTo(String text, Class<T> valueType) {
		if (text == null || valueType == null)
			return null;

		if (isBlank(text))
			return null;

		if (valueType.equals(String.class))
			return (T) text;
		if (valueType.equals(Long.class))
			return (T) Long.valueOf(text);
		if (valueType.equals(Integer.class))
			return (T) Integer.valueOf(text);
		if (valueType.equals(Short.class))
			return (T) Short.valueOf(text);
		if (valueType.equals(Byte.class))
			return (T) Byte.valueOf(text);
		if (valueType.equals(Double.class))
			return (T) Double.valueOf(text);
		if (valueType.equals(Float.class))
			return (T) Float.valueOf(text);
		if (valueType.equals(Boolean.class))
			return (T) Boolean.valueOf(text);
		if (Date.class.isAssignableFrom(valueType))
			return (T) DateUtil.parse(text);
		if (Number.class.isAssignableFrom(valueType))
			return ClassUtil.newInstance(valueType, text);

		return JsonUtil.loadFromJson(valueType, text);
	}

	public static String join(String[] arr, String sep, boolean ignoreNil) {
		StringBuffer sb = new StringBuffer();

		for (String str : arr) {
			if (ignoreNil && isBlank(str))
				continue;

			sb.append(sep).append(str);
		}

		return sb.length() > 0 ? sb.substring(1) : "";
	}

	public static boolean isNumber(String number) {
		try {
			Long.parseLong(number);
			return true;
		} catch (Throwable e) {
			return false;
		}
	}

	public static void validatePassword(String pwssword) {
		int minLength = 8;
		int maxLength = 16;
		if (pwssword.length() < minLength || pwssword.length() > maxLength) {
			throw new CocException("密码长度必须为8-16位！");
		}
		// if (newPassword.toLowerCase().equals(newPassword)) {
		// throw new CocException("密码必须包含至少一个大写字母！");
		// }
		//
		// if (newPassword.toUpperCase().equals(newPassword)) {
		// throw new CocException("密码必须包含至少一个小写字母！");
		// }
		//
		// boolean containsNumber = false;
		// for (int i = newPassword.length() - 1; i >= 0; i--) {
		// char c = newPassword.charAt(i);
		// if (c >= '0' && c <= '9') {
		// containsNumber = true;
		// break;
		// }
		// }
		// if (!containsNumber) {
		// throw new CocException("密码必须包含至少一个数字！");
		// }

		// boolean containsSymbols = false;
		// for (int i = newPassword.length() - 1; i >= 0; i--) {
		// char c = newPassword.charAt(i);
		// if (!(c >= '0' && c <= '9')// is not number
		// && !(c >= 'a' && c <= 'z')// is not lower case
		// && !(c >= 'A' && c <= 'Z')// is not lower case
		// && !(c == ' ')// is not space
		// ) {
		// containsSymbols = true;
		// break;
		// }
		// }
		// if (!containsSymbols) {
		// throw new CocException("密码必须包含至少一个特殊字符（出数字、字母以外的字符）！");
		// }

	}

	public static String join(List list) {
		return join(list, null, ",");
	}

	public static String join(List list, String fld) {
		return join(list, fld, ",");
	}

	public static String join(List list, String fld, String seperator) {
		if (list == null || list.size() == 0) {
			return "";
		}
		if (seperator == null) {
			seperator = ",";
		}
		int length = list.size();
		if (length == 0)
			return "";
		StringBuffer buf = new StringBuffer();
		for (Object value : list) {
			if (!isBlank(fld))
				value = ObjectUtil.getValue(value, fld);

			buf.append(seperator).append(value);
		}
		return buf.toString().substring(seperator.length());
	}

	public static String join(String seperator, String[] strings) {
		int length = strings.length;
		if (length == 0)
			return "";
		StringBuffer buf = new StringBuffer(length * strings[0].length()).append(strings[0]);
		for (int i = 1; i < length; i++) {
			buf.append(seperator).append(strings[i]);
		}
		return buf.toString();
	}

	public static String replaceOnce(String template, String placeholder, String replacement) {
		int loc = template == null ? -1 : template.indexOf(placeholder);
		if (loc < 0) {
			return template;
		} else {
			return new StringBuffer(template.substring(0, loc)).append(replacement).append(template.substring(loc + placeholder.length())).toString();
		}
	}

	public static String unqualify(String qualifiedName) {
		int loc = qualifiedName.lastIndexOf(".");
		return (loc < 0) ? qualifiedName : qualifiedName.substring(loc + 1);
	}

	public static String addUnderscores(String name) {
		StringBuffer buf = new StringBuffer(name.replace('.', '_'));
		for (int i = 1; i < buf.length() - 1; i++) {
			if (Character.isLowerCase(buf.charAt(i - 1)) && Character.isUpperCase(buf.charAt(i)) && Character.isLowerCase(buf.charAt(i + 1))) {
				buf.insert(i++, '_');
			}
		}
		return buf.toString().toUpperCase();
	}

	public static String toUpperCase(String str) {
		return str == null ? null : str.toUpperCase();
	}

	public static String toLowerCase(String str) {
		return str == null ? null : str.toLowerCase();
	}

	public static String toUnicode(String str) {
		String result = "";
		for (int i = 0; i < str.length(); i++) {
			int chr1 = (char) str.charAt(i);
			if (chr1 >= 19968 && chr1 <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
				result += "\\u" + Integer.toHexString(chr1);
			} else {
				result += str.charAt(i);
			}
		}
		return result;
	}

	public static String encodeURL(Object value) {
		if (value == null) {
			return "";
		}
		return ResponseUtil.encodeURL(value.toString());
	}

	// public static String ipToName(String ip) {
	// try {
	// IPSeeker ipseeker = Cocit.me().getIpSeeker();
	// if (ipseeker == null) {
	// return ip;
	// }
	// String ret = ipseeker.getIPLocation(ip).getCountry() + ipseeker.getArea(ip);
	// if (ret.length() < 4)
	// return ip;
	//
	// return ret;
	// } catch (Throwable e) {
	// return ip;
	// }
	// }

	// public static String toHtml(Object value) {
	// if (value == null) {
	// return "";
	// }
	// return value.toString()//
	// .replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")//
	// .replace("<script>", "&lt;script&gt;")//
	// .replace("</script>", "&lt;/script&gt;")//
	// .replace("  ", "&nbsp;&nbsp;")//
	// .replace("\r\n", "<br />")//
	// .replace("\n", "<br />")//
	// .replace("\r", "<br />")//
	// ;
	// }

	public static String dencodeUri(Object value) {
		if (value == null) {
			return "";
		}
		try {
			return java.net.URLDecoder.decode(value.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return value.toString();
		}
	}

	private static String toPinyin(char c) {
		String[] pinyin = null;
		try {
			HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();

			// UPPERCASE：大写 (ZHONG)
			// LOWERCASE：小写 (zhong)
			format.setCaseType(HanyuPinyinCaseType.LOWERCASE);

			// WITHOUT_TONE：无音标 (zhong)
			// WITH_TONE_NUMBER：1-4数字表示英标 (zhong4)
			// WITH_TONE_MARK：直接用音标符（必须WITH_U_UNICODE否则异常） (zhòng)
			format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

			// WITH_V：用v表示ü (nv)
			// WITH_U_AND_COLON：用"u:"表示ü (nu:)
			// WITH_U_UNICODE：直接用ü (nü)
			format.setVCharType(HanyuPinyinVCharType.WITH_V);

			pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			LogUtil.error(e.toString());
		}

		// 如果c不是汉字，toHanyuPinyinStringArray会返回null
		if (pinyin == null)
			return null;

		// 只取一个发音，如果是多音字，仅取第一个发音

		return pinyin[0];

	}

	public static String toPinyin(String str) {

		StringBuilder sb = new StringBuilder();

		String tempPinyin = null;
		for (int i = 0; i < str.length(); ++i) {
			tempPinyin = toPinyin(str.charAt(i));
			if (tempPinyin == null) {
				sb.append(str.charAt(i));
			} else {
				sb.append(tempPinyin);
			}
		}

		return sb.toString();
	}

	public static String toPinyinFirstChar(String str) {

		StringBuilder sb = new StringBuilder();

		String tempPinyin = null;
		for (int i = 0; i < str.length(); ++i) {
			tempPinyin = toPinyin(str.charAt(i));
			if (tempPinyin == null) {
				sb.append(str.charAt(i));
			} else {
				sb.append(tempPinyin.charAt(0));
			}
		}

		return sb.toString();
	}

	public static String toString(Object obj) {
		if (obj instanceof String[])
			return toString((String[]) obj);
		if (obj instanceof Map)
			return toString((Map) obj);
		if (obj instanceof List)
			return toString((List) obj);

		if (obj instanceof IDataEntity) {
			return obj == null ? "<NULL>" : ((IDataEntity) obj).toJsonString();
		}

		return obj == null ? "<NULL>" : obj.toString();
	}

	public static String toString(List list) {
		if (list == null)
			return "<NULL>";

		StringBuffer sb = new StringBuffer();
		sb.append("[");
		int count = 0;
		for (Object str : list) {
			if (count > 0)
				sb.append(", ");

			sb.append(str);

			count++;
		}
		sb.append("]");

		return sb.toString();
	}

	public static String toString(String[] array) {
		if (array == null)
			return "<NULL>";

		StringBuffer sb = new StringBuffer();
		sb.append("[");
		int count = 0;
		for (String str : array) {
			if (count > 0)
				sb.append(", ");

			sb.append(str);

			count++;
		}
		sb.append("]");

		return sb.toString();
	}

	public static String toString(Map map) {
		if (map == null)
			return "<NULL>";

		StringBuffer sb = new StringBuffer();
		sb.append("{");
		Iterator<String> keys = map.keySet().iterator();
		int count = 0;
		while (keys.hasNext()) {
			String key = keys.next();
			Object value = map.get(key);
			if (count > 0)
				sb.append(", ");

			if (value instanceof String[])
				sb.append(key).append(":").append(toString((String[]) value));
			else if (value instanceof List)
				sb.append(key).append(":").append(toString((List) value));
			else
				sb.append(key).append(":").append(toString(value));
			count++;
		}
		sb.append("}");

		return sb.toString();
	}

	public static void toString(StringBuffer sb, String field, Object value) {
		if (value == null)
			return;
		if (value instanceof String && StringUtil.isBlank((String) value))
			return;

		String str = toString(value);
		if (isBlank(str) || "<NULL>".equals(str))
			return;

		sb.append(field + ": ").append(str).append(", ");
	}

	public static boolean isCode(String code) {
		if (StringUtil.isBlank(code)) {
			return false;
		}

		char c = code.charAt(0);
		if (!(c >= 'a' && c <= 'z') && !(c >= 'A' && c <= 'Z')) {
			return false;
		}
		for (int i = code.length() - 1; i > 0; i--) {
			c = code.charAt(i);
			if (!(c >= '0' && c <= '9')// is not number
			        && !(c >= 'a' && c <= 'z')// is not lower case
			        && !(c >= 'A' && c <= 'Z')// is not lower case
			        && c != '_'//
			) {
				return false;
			}
		}

		return true;
	}

	public static Map parseJsonToMap(String json) {
		if (StringUtil.hasContent(json)) {
			if (!json.startsWith("{")) {
				json = "{" + json + "}";
			}
			try {
				return JsonUtil.loadFromJson(Map.class, json);
			} catch (Throwable e) {
				throw new CocException("非法JSON对象！%s", ExceptionUtil.msg(e));
			}
		}

		return null;
	}

	public static String toJSArray(List<String> list) {
		StringBuffer sb = new StringBuffer();
		for (String s : list) {
			sb.append(",'" + s + "'");
		}
		String resultUI = "";
		if (sb.length() > 0) {
			resultUI = sb.substring(1);
		}

		return "[" + resultUI + "]";
	}

	public static int strlen(String str) {
		if (str == null || str.length() <= 0) {
			return 0;
		}
		int len = 0;
		char c;
		for (int i = str.length() - 1; i >= 0; i--) {
			c = str.charAt(i);
			if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {// 字母,
				                                                                             // 数字
				len++;
			} else {
				if (Character.isLetter(c)) { // 中文
					len += 2;
				} else { // 符号或控制字符
					len++;
				}
			}
		}
		return len;
	}

	public static String substr(String str, int size) {
		if (str == null || str.length() <= 0) {
			return "";
		}

		str = str.replace(" ", "");

		size = size * 2;
		if (strlen(str) <= size) {
			return str;
		}
		size = size - 2;
		int len = str.length();
		char c;
		int sublen = 0;
		int sizeCount = 0;
		for (int i = 0; i < len; i++) {
			c = str.charAt(i);
			if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z')) {// 字母,
				sizeCount++;
			} else {
				sizeCount += 2;
			}
			if (sizeCount > size) {
				break;
			}
			sublen++;
		}
		return str.substring(0, sublen) + "....";
	}
}
