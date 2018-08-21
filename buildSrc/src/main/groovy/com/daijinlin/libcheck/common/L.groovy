package com.daijinlin.libcheck.common

import groovy.util.logging.Slf4j
import org.apache.tools.ant.util.StringUtils
import org.codehaus.groovy.util.StringUtil

/**
 * <pre>
 * Created by J!nl!n on 2018/3/16.
 * Copyright © 1990-2018 J!nl!n™ Inc. All rights reserved.
 *
 * </pre>
 */
@Slf4j
class L {
  private final static String TAG = "ConfigEncrypt"
  /**
   * Android's max limit for a log entry is ~4076 bytes,
   * so 4000 bytes is used as chunk size since default charset
   * is UTF-8
   */
  private static final int CHUNK_SIZE = 4000
  final static String TOP_LEFT_CORNER = '╭'
  final static String BOTTOM_LEFT_CORNER = '╰'
  final static String MIDDLE_CORNER = '├'
  final static String HORIZONTAL_LINE = '│'
  final static String DOUBLE_DIVIDER = "────────────────────────────────────────────────────────"
  final static String SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄"
  final static String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER
  final static String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER
  final static String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER

  static void d(String param) {
    log(TAG, param, 5)
  }

  static void d(String tag, String param) {
    log(tag, param, 5)
  }

  private static void log(String tag, String message, int methodCount) {
    logTopBorder(tag)
    logHeaderContent(tag)
    logDivider(tag)
    //get bytes of message with system's default charset (which is UTF-8 for Android)
    byte[] bytes = message.getBytes()
    int length = bytes.length
    if (length <= CHUNK_SIZE) {
//      if (methodCount > 0) {
//        logDivider(tag)
//      }
      logContent(tag, message)
      logBottomBorder(tag)
      return
    }
//    if (methodCount > 0) {
//      logDivider(tag)
//    }
    for (int i = 0; i < length; i += CHUNK_SIZE) {
      int count = Math.min(length - i, CHUNK_SIZE)
      //create a new String with system's default charset (which is UTF-8 for Android)
      logContent(tag, new String(bytes, i, count))
    }
    logBottomBorder(tag)
  }

  private static void logTopBorder(String tag) {
    logChunk(tag, TOP_BORDER)
  }

  private static void logHeaderContent(String tag) {
    logChunk(tag, HORIZONTAL_LINE + " ${tag}")
  }

  private static void logBottomBorder(String tag) {
    logChunk(tag, BOTTOM_BORDER)
  }

  private static void logDivider(String tag) {
    logChunk(tag, MIDDLE_BORDER)
  }

  private static void logContent(String tag, String chunk) {
    String[] lines = chunk.split(System.getProperty("line.separator"))
    for (String line : lines) {
      logChunk(tag, HORIZONTAL_LINE + " " + line)
    }
  }

  private static void logChunk(String tag, String chunk) {
    log.error("→ {}", chunk)
  }

}
