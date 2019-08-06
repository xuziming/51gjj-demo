package com.simon.credit.util;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 通用工具类
 * @author XUZIMING 2016-10-11
 */
public class CommonUtils {

	public static final String UTF8 = "UTF-8";
	public static String DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private static ThreadLocal<Map<String, DateFormat>> threadLocal = new ThreadLocal<Map<String, DateFormat>>();

	/**======================================================================================*/
	/**================================= 字符/数组等非空判断与操作=================================*/
	/**======================================================================================*/

	public static final boolean isEmpty(Object obj) {
		if (obj instanceof String) {
			return obj == null || isEmptyContainNull(obj.toString());
		}

		if (obj instanceof Object[]) {
			Object[] array = (Object[]) obj;
			return isEmpty(array);
		}

		if (obj instanceof Collection) {
			Collection<?> c = (Collection<?>) obj;
			return isEmpty(c);
		}

		if (obj instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) obj;
			return isEmpty(map);
		}

		return obj == null;
	}

	public static final boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	public static final boolean isEmpty(String input) {
		return input == null || input.trim().isEmpty();
	}

	public static final boolean isEmptyContainNull(String input) {
		return input == null || input.trim().isEmpty() || input.trim().equalsIgnoreCase("null");
	}

	public static final boolean isEmpty(Object[] array) {
		return array == null || array.length == 0;
	}

	public static final boolean isEmpty(Collection<?> c) {
		return c == null || c.isEmpty();
	}

	public static final boolean isNotEmpty(Collection<?> c) {
		return !isEmpty(c);
	}

	public static final boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	public static final boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}

	public static final String trim(final String input) {
		return input == null ? null : input.trim();
	}

	public static final String trim(String input, String emptyDefault) {
		return isEmpty(input) ? emptyDefault : input.trim();
	}

	public static final String trimToEmpty(String input) {
		return isEmptyContainNull(input) ? "" : input.trim();
	}

	public static final String trimToEmpty(Object input) {
		return trimToEmpty(Objects.toString(input));
	}

	public static final boolean isNoneEmpty(List<Object> objs) {
		return !isAnyEmpty(objs);
	}

	public static final boolean isAnyEmpty(List<Object> objs) {
		if (isEmpty(objs)) {
			return true;
		}
		return isAnyEmpty(objs.toArray());
	}

	/**
	 * 判断目标对象列表的元素全部不为空值(即: 没有任何一个为空值)
	 * <pre>空值包括: null、空字符串(即: "")、若干空格组成的字符串(如: "  ")</pre>
	 * @param objs
	 * @return
	 */
	public static final boolean isNoneEmpty(Object... objs) {
		if (objs == null || objs.length == 0) {
			return false;
		}
		for (Object obj : objs) {
			if (isEmpty(obj)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断目标对象列表的元素是否存在空值
	 * <pre>空值包括: null、空字符串(即: "")、若干空格组成的字符串(如: "  ")</pre>
	 * @param objs
	 * @return
	 */
	public static final boolean isAnyEmpty(Object... objs) {
		if (objs == null || objs.length == 0) {
			return true;
		}
		for (Object obj : objs) {
			if (isEmpty(obj)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断目标对象列表的元素是否全部为空值
	 * <pre>空值包括: null、空字符串(即: "")、若干空格组成的字符串(如: "  ")</pre>
	 * @param objs
	 * @return
	 */
	public static final boolean isAllEmpty(Object... objs) {
		if (objs == null || objs.length == 0) {
			return true;
		}
		for (Object obj : objs) {
			if (!isEmpty(obj)) {
				return false;
			}
		}
		return true;
	}

	public static final boolean endsWithAnyIgnoreCase(CharSequence string, CharSequence... searchStrings) {
		if (string == null || string.length() == 0 || searchStrings == null || searchStrings.length == 0) {
            return false;
        }
        for (final CharSequence searchString : searchStrings) {
            if (endsWithIgnoreCase(string, searchString)) {
                return true;
            }
        }
        return false;
	}

	private static final boolean endsWithIgnoreCase(final CharSequence str, final CharSequence suffix) {
        return endsWith(str, suffix, true);
    }

	private static final boolean endsWith(final CharSequence str, final CharSequence suffix, final boolean ignoreCase) {
        if (str == null || suffix == null) {
            return str == null && suffix == null;
        }
        if (suffix.length() > str.length()) {
            return false;
        }
        final int strOffset = str.length() - suffix.length();
        return regionMatches(str, ignoreCase, strOffset, suffix, 0, suffix.length());
    }

	private static final boolean regionMatches(final CharSequence cs, final boolean ignoreCase, 
		final int thisStart, final CharSequence substring, final int start, final int length) {

		if (cs instanceof String && substring instanceof String) {
			return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
		}

		int index1 = thisStart;
		int index2 = start;
		int tmpLen = length;

		while (tmpLen-- > 0) {
			char c1 = cs.charAt(index1++);
			char c2 = substring.charAt(index2++);

			if (c1 == c2) continue;

			if (!ignoreCase) return false;

			// The same check as in String.regionMatches():
			if (Character.toUpperCase(c1) != Character.toUpperCase(c2) && Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
				return false;
			}
		}

		return true;
	}

	/**======================================================================================*/
	/**==================================== 字符相等或不等判断 ===================================*/
	/**======================================================================================*/

	/**
	 * 判断两个字符串是否相同(区别于: ==)
	 * @param input1 字符串1
	 * @param input2 字符串2
	 * @return
	 */
	public static final boolean equals(String input1, String input2) {
		if (input1 == input2) {
			return true;
		}
		if (input1 == null || input2 == null) {
			return false;
		}

		input1 = input1.trim();
		input2 = input2.trim();

		if (input1 instanceof String && input2 instanceof String) {
			return input1.equals(input2);
		}
        return regionMatches(input1, false, 0, input2, 0, Math.max(input1.length(), input2.length()));
	}

	/**
	 * 判断两个字符串是否不同(区别于: !=)
	 * @param input1 字符串1
	 * @param input2 字符串2
	 * @return
	 */
	public static final boolean notEquals(String input1, String input2) {
		return !equals(input1, input2);
	}

	/**
	 * 判断两个字符串是否相同(不区分大小写)
	 * @param input1 字符串1
	 * @param input2 字符串2
	 * @return
	 */
	public static final boolean equalsIgnoreCase(String input1, String input2) {
		if (input1 == null || input2 == null) {
			return input1 == input2;
		}
		if (input1 == input2) {
			return true;
		}

		input1 = input1.trim();
		input2 = input2.trim();

		if (input1.length() != input2.length()) {
			return false;
		}
		return regionMatches(input1, true, 0, input2, 0, input1.length());
	}

	/**
	 * 判断两个字符串是否不同(不区分大小写)
	 * @param input1 字符串1
	 * @param input2 字符串2
	 * @return
	 */
	public static final boolean notEqualsIgnoreCase(String input1, String input2) {
		return !equalsIgnoreCase(input1, input2);
	}

	/**======================================================================================*/
	/**=============================== 布尔字符串true或false相关判断==============================*/
	/**======================================================================================*/

	/**
	 * 判断布尔字符串是否为true
	 * <pre>
	 * CommonUtils.isTrue("true")  = true
	 * CommonUtils.isTrue("false") = false
	 * CommonUtils.isTrue(null)    = false
	 * </pre>
	 * 
	 * @param input 输入值
	 * @return
	 */
	public static final boolean isTrue(String input) {
		Boolean bool = Boolean.valueOf(input == null ? null : input.toLowerCase());
		return Boolean.TRUE.equals(bool);
	}

	/**
	 * 判断布尔字符串是否不为true
	 * <pre>
	 * CommonUtils.isNotTrue("true")  = false
	 * CommonUtils.isNotTrue("false") = true
	 * CommonUtils.isNotTrue(null)    = true
	 * </pre>
	 * 
	 * @param input 输入值
	 * @return
	 */
	public static final boolean isNotTrue(String input) {
		return !isTrue(input);
	}

	/**
	 * 判断布尔字符串是否为false
	 * <pre>
	 * CommonUtils.isFalse("true")  = false
	 * CommonUtils.isFalse("false") = true
	 * CommonUtils.isFalse(null)    = false
	 * </pre>
	 * 
	 * @param input 输入值
	 * @return
	 */
	public static final boolean isFalse(String input) {
		if (isEmptyContainNull(input)) {
			return false;
		}
		Boolean bool = Boolean.valueOf(input == null ? null : input.toLowerCase());
		return Boolean.FALSE.equals(bool);
	}

	/**
	 * 判断布尔字符串是否不为false
	 * <pre>
	 * CommonUtils.isNotFalse("true")  = true
	 * CommonUtils.isNotFalse("false") = false
	 * CommonUtils.isNotFalse(null)    = true
	 * <pre>
	 * 
	 * @param input 输入值
	 * @return
	 */
	public static final boolean isNotFalse(String input) {
		return !isFalse(input);
	}

	/**======================================================================================*/
	/**========================================空集处理========================================*/
	/**======================================================================================*/

	public static final <T> List<T> emptyList() {
		return new ArrayList<T>();
	}

	public static final <T> List<T> emptyList(List<T> list) {
		if (list == null) {
			return emptyList();
		} else {
			return list;
		}
	}

	public static final <K, V> Map<K, V> emptyMap() {
		return new HashMap<K, V>();
	}

	public static final <K, V> Map<K, V> emptyMap(Map<K, V> map) {
		if (map == null) {
			return emptyMap();
		} else {
			return map;
		}
	}

	public static final <K, V> Map<K, V> stableMap(int size) {
		return new HashMap<K, V>(size, 1.0f);
	}

	/**======================================================================================*/
	/**======================================基本类型转换  ======================================*/
	/**======================================================================================*/

	public static final short parseShort(Object data) {
		if (data == null) {
			return 0;
		}

		try {
			if (data instanceof Short) {
				return (Short) data;
			} else {
				return Short.valueOf(trim(String.valueOf(data)));
			}
		} catch (Exception e) {
			return 0;
		}
	}

	public static final int parseInt(Object data) {
		return parseInt(data, 0);
	}

	public static final int parseInt(Object data, int def) {
		if (data == null) {
			return def;
		}

		try {
			if (data instanceof Integer) {
				return (Integer) data;
			} else {
				return Integer.valueOf(trim(String.valueOf(data)));
			}
		} catch (Exception e) {
			return def;
		}
	}

	public static final long parseLong(Object data) {
		return parseLong(data, 0);
	}

	public static final long parseLong(Object data, long def) {
		if (data == null) {
			return def;
		}

		try {
			if (data instanceof Long) {
				return (Long) data;
			} else {
				return Long.valueOf(trim(String.valueOf(data)));
			}
		} catch (Exception e) {
			return def;
		}
	}

	public static final double parseDouble(Object data) {
		return parseDouble(data, (double) 0);
	}

	public static final double parseDouble(Object data, double def) {
		if (data != null) {
			try {
				double value = def;
				if (data != null) {
					if (data instanceof BigDecimal) {
						value = ((BigDecimal) data).doubleValue();
					} else if (data instanceof Double) {
						value = ((Double) data).doubleValue();
					} else {
						value = Double.valueOf(trim(String.valueOf(data)));
					}
				}
				return value == 0 ? 0 : roundHalfUp(value, 2);
			} catch (Exception e) {
			}
		}
		return def;
	}

	public static DateFormat getDateFormat(String pattern) {
		return getDateFormat(pattern, null);
	}

	public static DateFormat getDateFormat(String pattern, Locale locale) {
		if (isEmptyContainNull(pattern)) {
			throw illegalArgumentException("date format pattern cann't be empty!");
		}

		Map<String, DateFormat> dateFormatMap = threadLocal.get();
		if (dateFormatMap == null) {
			dateFormatMap = new HashMap<String, DateFormat>();
			threadLocal.set(dateFormatMap);
		}

		DateFormat dateFormat = dateFormatMap.get(pattern);
		if (dateFormat == null) {
			if (locale == null) {
				dateFormat = new SimpleDateFormat(pattern);
			} else {
				dateFormat = new SimpleDateFormat(pattern, locale);
			}
		}

		return dateFormat;
	}

	public static final String formatDate(final Date date) {
		return formatDate(date, DEFFAULT_DATE_FORMAT);
	}

	public static final String formatDate(final Date date, final String pattern) {
		if (date == null || isEmptyContainNull(pattern)) {
			return "";
		}

		// 此种实现方式在高并发环境下存在性能问题
		// final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		// return sdf.format(date);

		// 使用Apache commons 里的FastDateFormat，号称既快又线程安全的DateFormat
		// return DateFormatUtils.format(date, pattern);

		// 使用ThreadLocal, 将共享变量变为独享，线程独享肯定比方法独享在并发环境中能减少不少创建对象的开销。若对性能要求比较高，推荐此方法
		return getDateFormat(pattern).format(date);
	}

	private static final Map<Integer, String> DATE_FORMAT_PATTERN_MAP = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 6468294574893504185L;
		{
			put(8 , "yyyy-M-d");
			put(10, "yyyy-MM-dd");
			put(19, "yyyy-MM-dd HH:mm:ss");
			put(23, "yyyy-MM-dd HH:mm:ss.SSS");
		}
	};

	public static final Date parseDate(Object value) {
		if (value == null) {
			return null;
		}

		if (value instanceof Calendar) {
			return ((Calendar) value).getTime();
		}

		if (value instanceof Date) {
			return (Date) value;
		}

		long longValue = -1;

		if (value instanceof Number) {
			longValue = ((Number) value).longValue();
			return new Date(longValue);
		}

		if (value instanceof String) {
			String strVal = trimToEmpty(value);

			if (strVal.length() == 0) {
				return null;
			}

			DateFormat dateFormat = null;

			if (strVal.indexOf('-') != -1) {
				String pattern = DATE_FORMAT_PATTERN_MAP.get(strVal.length());
				dateFormat = getDateFormat(pattern);
			} else if (strVal.length() == "yyyyMMddHHmmss".length()) {
				dateFormat = getDateFormat("yyyyMMddHHmmss");
			} else {
				dateFormat = getDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
			}

			try {
				return dateFormat.parse(strVal);
			} catch (ParseException e) {
				// ignore
			}

			// 判断是否整型(Short、Integer、Long)字符串
			if (Pattern.compile("^[+-]?[0-9]+$").matcher(strVal).find()) {
				longValue = Long.parseLong(strVal);
			}
		}

		if (longValue < 0) {
			throw illegalArgumentException("can not cast to Date, value : " + value);
		}

		return new Date(longValue);
	}

	public static final BigDecimal roundHalfUp(BigDecimal value, int scale) {
		if (value == null) return null;
		return value.setScale(scale, BigDecimal.ROUND_HALF_EVEN);
	}

	/**
	 * 对双精度浮点数进行四舍五入
	 * @param value double类型数字(非空)
	 * @param scale 小数点后保留的小数位数
	 * @return
	 */
	public static final double roundHalfUp(double value, int scale) {
		BigDecimal decimal = new BigDecimal(value);
		return roundHalfUp(decimal, scale).doubleValue();
	}

	public static final byte[] toBinary(String content) {
		try {
			return content.getBytes(UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("unsupport charset: " + UTF8);
		}
	}

	public static final String toString(byte[] data) {
		return toString(data, UTF8);
	}

	public static final String toString(byte[] data, String characterEncoding) {
		if (data == null || data.length == 0) {
			return null;
		}
		try {
			return new String(data, characterEncoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("unsupport charset: " + characterEncoding);
		}
	}

	/**======================================================================================*/
	/**====================================集合/数组/Map操作====================================*/
	/**======================================================================================*/

	public static final int size(List<?> list) {
		return list == null ? 0 : list.size();
	}

	public static final int size(Collection<?> c) {
		return c == null ? 0 : c.size();
	}

	public static final <K, V> void put(Map<K, V> map, K key, V value) {
		if (map != null && key != null && value != null) {
			map.put(key, value);
		}
	}

	public static final <T> T getValue(Map<String, T> dataMap, String key) {
		T value = dataMap.get(key);
		return value == null ? null : (T) value;
	}

	/**
	 * 获取数组指定位置的值,越界则返回def
	 * 
	 * @param array
	 * @param index
	 * @param def
	 * @return
	 */
	public static final <T> T get(T[] array, int index, T def) {
		int arrayLength = array == null ? 0 : array.length;
		return get(array, arrayLength, index, def);
	}

	/**
	 * 获取数组指定位置的值,越界则返回def
	 * 
	 * @param array
	 * @param arrayLength
	 * @param index
	 * @param def
	 * @return
	 */
	public static final <T> T get(T[] array, int arrayLength, int index, T def) {
		if (index >= 0 && index < arrayLength) {
			return array[index];
		}
		return def;
	}

	/**======================================================================================*/
	/**=====================================异常定义与处理 ======================================*/
	/**======================================================================================*/

	public static final IllegalStateException illegalStateException(Throwable t) {
		return new IllegalStateException(t);
	}

	public static final IllegalStateException illegalStateException(String message) {
		return new IllegalStateException(message);
	}

	public static final IllegalStateException illegalStateException(String message, Throwable t) {
		return new IllegalStateException(message, t);
	}

	public static final IllegalArgumentException illegalArgumentException(String message) {
		return new IllegalArgumentException(message);
	}

	public static final UnsupportedOperationException unsupportedMethodException() {
		return new UnsupportedOperationException("unsupport this method");
	}

	/**
	 * 获取最原始的抛出异常
	 * @param t 捕捉到的异常抛出对象
	 * @return
	 */
	public static final Throwable foundRealThrowable(Throwable t) {
		Throwable cause = t.getCause();
		if (cause == null) return t;
		return foundRealThrowable(cause);
	}

	/**
	 * 格式化异常
	 * 
	 * @param t
	 * @return
	 */
	public static final String formatThrowable(Throwable t) {
		if (t == null) return "";
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		pw.flush();
		sw.flush();
		return sw.toString();
	}

	public static final String formatThrowableForHtml(Throwable t) {
		String ex = formatThrowable(t);
		return ex.replaceAll("\n\t", " ");
	}

	/**======================================================================================*/
	/**======================================反射/实例化 =======================================*/
	/**======================================================================================*/

	/**
	 * 实例化对象,注意该对象必须有无参构造函数
	 * 
	 * @param klass
	 * @return
	 */
	public static final <T> T newInstance(Class<T> klass) {
		try {
			return (T) klass.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException("instance class[" + klass + "] with ex:", e);
		}
	}

	@SuppressWarnings("unchecked")
	public static final <T> T newInstance(String className) {
		try {
			return (T) newInstance(Class.forName(className));
		} catch (Exception e) {
			throw new IllegalArgumentException("instance class[" + className + "] with ex:", e);
		}
	}

	public static final Class<?> classForName(String className) {
		try {
			return Class.forName(className);
		} catch (Exception e) {
			throw new IllegalArgumentException("classForName(" + className + ")  with ex:", e);
		}
	}

	/**======================================================================================*/
	/**====================================== URL编/解码 ======================================*/
	/**======================================================================================*/

	public static final String urlDecodeUTF8(String input) {
		if (input == null) return null;
		try {
			return URLDecoder.decode(input, UTF8);
		} catch (Exception e) {
			throw illegalStateException(e);
		}
	}

	public static final String urlEncodeUTF8(String input) {
		if (input == null) return null;
		try {
			return URLEncoder.encode(input, UTF8);
		} catch (Exception e) {
			throw illegalStateException(e);
		}
	}

	/**======================================================================================*/
	/**==================================classpath下文件读取 ===================================*/
	/**======================================================================================*/

	public static final InputStream getInputStreamFromClassPath(String filename) {
		return CommonUtils.isEmpty(filename) ? null : 
			CommonUtils.class.getClassLoader().getResourceAsStream(filename);
	}

	/**======================================================================================*/
	/**=======================================文件路径解析======================================*/
	/**======================================================================================*/

	/**
	 * 将文件路径转换为Java可识别的路径
	 * @param path 文件路径
	 * @return
	 */
	public static final String castToJavaFilePath(String path) {
		String FILE_SEPERATOR = "/";

		if (path == null) return null;

		// 反斜杠
		path = replace(path, "\\", FILE_SEPERATOR);
		path = replace(path, "\\\\", FILE_SEPERATOR);

		// 斜杠
		path = replace(path, "//", FILE_SEPERATOR);
		path = replace(path, "////", FILE_SEPERATOR);
		path = replace(path, "//////", FILE_SEPERATOR);
		path = replace(path, "////////", FILE_SEPERATOR);

		path = replace(path, "/", FILE_SEPERATOR);
		path = replace(path, "//", FILE_SEPERATOR);
		path = replace(path, "///", FILE_SEPERATOR);
		path = replace(path, "////", FILE_SEPERATOR);

		path = replace(path, "${FILE_SEPERATOR}", FILE_SEPERATOR);

		return path;
	}

	public static final String replace(final String text, final String searchString, final String replacement) {
		return replace(text, searchString, replacement, -1);
	}

	public static final String replace(final String text, final String searchString, final String replacement, int max) {
		if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
			return text;
		}
		int start = 0;
		int end = text.indexOf(searchString, start);
		if (end == -1) {
			return text;
		}
		final int replLength = searchString.length();
		int increase = replacement.length() - replLength;
		increase = increase < 0 ? 0 : increase;
		increase *= max < 0 ? 16 : max > 64 ? 64 : max;
		final StringBuilder buf = new StringBuilder(text.length() + increase);
		while (end != -1) {
			buf.append(text.substring(start, end)).append(replacement);
			start = end + replLength;
			if (--max == 0) {
				break;
			}
			end = text.indexOf(searchString, start);
		}
		buf.append(text.substring(start));
		return buf.toString();
	}

	/**
	 * java去除字符串中的空格、回车、换行符、制表符
	 * 
	 * @param input 输入值
	 * @return
	 */
	public static final String deleteWhitespace(String input) {
		if (isEmpty(input)) {
			return input;
		}
		final int sz = input.length();
		final char[] chs = new char[sz];
		int count = 0;
		for (int i = 0; i < sz; i++) {
			if (!Character.isWhitespace(input.charAt(i))) {
				chs[count++] = input.charAt(i);
			}
		}
		if (count == sz) {
			return input;
		}
		return new String(chs, 0, count);
	}

}
