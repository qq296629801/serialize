

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.jyd.bms.tool.beanCopy.BeanCopyUtils;
import com.jyd.bms.tool.beanCopy.CollectionCopy;
import com.jyd.bms.tool.beanCopy.CompoundTypeCopy;
import com.jyd.bms.tool.beanCopy.CopyHandlerFactory;
import com.jyd.bms.tool.beanCopy.MapCopy;
import com.jyd.bms.tool.beanCopy.PrimitiveCopy;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class ConverterUtils {
	/**
	 * 转换对象为Base64字符�?
	 * 
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public static String convertObjectToBase64String(Object object) throws IOException {
		ByteArrayOutputStream ms = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(ms);
		oos.writeObject(object);
		oos.close();

		String base64String = new BASE64Encoder().encode(ms.toByteArray());
		return base64String;
	}

	/**
	 * 转换Base64字符串为对象
	 * 
	 * @param base64String
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static <T> T convertBase64StringToObject(String base64String, Object object)
			throws IOException, ClassNotFoundException {
		ByteArrayOutputStream backMs = new ByteArrayOutputStream();

		BASE64Decoder decoder = new BASE64Decoder();
		byte bt[] = decoder.decodeBuffer(base64String);
		ByteArrayInputStream is = new ByteArrayInputStream(bt);
		ObjectInputStream ois = new ObjectInputStream(is);

		CopyHandlerFactory.getInstance().registerHandler(new PrimitiveCopy());
		CopyHandlerFactory.getInstance().registerHandler(new CollectionCopy());
		CopyHandlerFactory.getInstance().registerHandler(new MapCopy());
		CopyHandlerFactory.getInstance().registerHandler(new CompoundTypeCopy());
		BeanCopyUtils.copyProperties(ois.readObject(), object);

		return (T) object;
	}
}
