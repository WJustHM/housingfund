package com.handge.housingfund.common.service.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashSet;

public class ActionUtil {
	public static String getUrlMD5(String httpMethod, String urlPath, HashSet<String> actionKeywords) {
		StringBuilder builder = new StringBuilder();
		builder.append(httpMethod);
		String[] keys = urlPath.split("/");
		for (String key : keys) {
			if (actionKeywords.contains(key)) {
				builder.append(key);
			} else {
				builder.append("0");
			}
		}
		return DigestUtils.md5Hex(builder.toString());
	}
}
