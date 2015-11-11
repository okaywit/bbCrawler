package com.bbcow.test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class TestDate {
	public static void main(String[] args) {
		ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
		System.out.println(now.toString());
	}
}
