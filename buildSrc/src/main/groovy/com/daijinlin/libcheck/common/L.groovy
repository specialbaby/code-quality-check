package com.daijinlin.libcheck.common

import groovy.util.logging.Slf4j
import org.gradle.api.logging.Logging


/**
 * <pre>
 * Created by J!nl!n on 2018/3/16.
 * Copyright © 1990-2018 J!nl!n™ Inc. All rights reserved.
 *
 * </pre>
 */
@Slf4j
class L {
  static void d(Object... param) {
    final String TOP_LEFT_CORNER = '┌'
    final String BOTTOM_LEFT_CORNER = '└'
    final String MIDDLE_CORNER = '├'
    final String HORIZONTAL_LINE = '│'
    final String DOUBLE_DIVIDER = "────────────────────────────────────────────────────────"
    final String SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄"
    final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER
    final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER
    final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER
    log.error("${TOP_BORDER}")
    log.error("├ $param")
    log.error("${BOTTOM_BORDER}")
  }
}
