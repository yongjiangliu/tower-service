package cn.com.boco.dss.subject.common.gsonadapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
	private static Gson gson;

	public static Gson buildGson() {
		if (gson == null) {
			gson = new GsonBuilder().registerTypeAdapter(Integer.class, new IntegerDefaultNullAdapter())
					.registerTypeAdapter(int.class, new IntegerDefault0Adapter())
					.registerTypeAdapter(Double.class, new DoubleDefaultNullAdapter())
					.registerTypeAdapter(double.class, new DoubleDefault0Adapter())
					.registerTypeAdapter(Long.class, new LongDefaultNullAdapter())
					.registerTypeAdapter(long.class, new LongDefault0Adapter()).create();
		}
		return gson;
	}
}
