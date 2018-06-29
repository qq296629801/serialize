package com.jyd.bms.tool.beanCopy;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanCopyUtils {
	public static void copyProperties(Object sourceObj, Object targetObj, String... notCopyFidlds) {

		Map<String, PropertyDescriptor> nameAndSourceProp = getPropertyDesc(sourceObj);

		Map<String, PropertyDescriptor> nameAndTargetProp = getPropertyDesc(targetObj);

		List<PropertyCopyInfo> propertyCopyInfoList = getBeanCopyInfo(nameAndSourceProp, nameAndTargetProp);

		filterNotCopy(propertyCopyInfoList, notCopyFidlds);

		copyProperties(sourceObj, targetObj, propertyCopyInfoList);
	}

	private static void filterNotCopy(List<PropertyCopyInfo> propertyCopyInfoList, String... notCopyFidlds) {
		if (null == notCopyFidlds || 0 == notCopyFidlds.length) {
			return;
		}
		List<String> fieldsList = Arrays.asList(notCopyFidlds);
		for (int i = propertyCopyInfoList.size() - 1; i >= 0; i--) {
			if (fieldsList.contains(propertyCopyInfoList.get(i).getPropertyName())) {
				propertyCopyInfoList.remove(i);
			}
		}
	}

	private static void copyProperties(Object source, Object target, List<PropertyCopyInfo> propertyCopyInfoList) {
		for (PropertyCopyInfo copyInfo : propertyCopyInfoList) {
			Object sourceValue;
			Method readMeghod = copyInfo.getReadMethod();
			try {
				sourceValue = readMeghod.invoke(source);
				if (null == sourceValue) {
					continue;
				}
				if (sourceValue instanceof Class) {
					continue;
				}

				Method writeMethod = copyInfo.getWriteMeghod();

				for (AbstractCopy abstractCopy : CopyHandlerFactory.getCopySet()) {
					boolean bool = abstractCopy.executeCopy(sourceValue, target, writeMethod);
					if (bool) {
						break;
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
		}
	}

	private static List<PropertyCopyInfo> getBeanCopyInfo(Map<String, PropertyDescriptor> nameAndSourceProp,
			Map<String, PropertyDescriptor> nameAndTargetProp) {
		List<PropertyCopyInfo> proList = new ArrayList<PropertyCopyInfo>();
		for (Map.Entry<String, PropertyDescriptor> entry : nameAndTargetProp.entrySet()) {
			Method writeMethod = null;
			Method readMethod = null;
			// filter not exist in source obj
			if (null == nameAndSourceProp.get(entry.getKey())) {
				continue;
			}
			// filter has no writeMethod
			if (null == entry.getValue() || null == entry.getValue().getWriteMethod()) {
				continue;
			}

			if (null == nameAndSourceProp.get(entry.getKey()).getReadMethod()) {
				continue;
			}

			writeMethod = entry.getValue().getWriteMethod();
			readMethod = nameAndSourceProp.get(entry.getKey()).getReadMethod();

			PropertyCopyInfo copyInfo = new PropertyCopyInfo();
			copyInfo.setPropertyName(entry.getKey());
			copyInfo.setReadMethod(readMethod);
			copyInfo.setWriteMeghod(writeMethod);
			proList.add(copyInfo);
		}
		return proList;
	}

	public static Map<String, PropertyDescriptor> getPropertyDesc(Object obj) {
		if (null == obj) {
			throw new RuntimeException("obj can't be null");
		}
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(obj.getClass());
		} catch (IntrospectionException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		Map<String, PropertyDescriptor> map = new HashMap<String, PropertyDescriptor>();
		for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
			map.put(property.getDisplayName(), property);
		}
		return map;
	}

	public static boolean checkHasNoArgumentsConstructor(Object obj) {
		Constructor<?>[] constructors = null;
		if (obj instanceof Class) {
			constructors = ((Class<?>) obj).getConstructors();
		} else {
			constructors = obj.getClass().getConstructors();
		}

		boolean hasNoArguments = false;
		for (Constructor<?> constructor : constructors) {
			if (0 == constructor.getParameterTypes().length) {
				hasNoArguments = true;
				break;
			}
		}
		return hasNoArguments;
	}

}
