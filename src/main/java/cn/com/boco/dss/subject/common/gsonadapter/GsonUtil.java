package cn.com.boco.dss.subject.common.gsonadapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
	private static Gson gson;

	public static Gson buildGson() {
		if (gson == null) {
			gson = new GsonBuilder().registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
					.registerTypeAdapter(int.class, new IntegerDefault0Adapter())
					.registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
					.registerTypeAdapter(double.class, new DoubleDefault0Adapter())
					.registerTypeAdapter(Long.class, new LongDefault0Adapter())
					.registerTypeAdapter(long.class, new LongDefault0Adapter()).create();
		}
		return gson;
	}
}
