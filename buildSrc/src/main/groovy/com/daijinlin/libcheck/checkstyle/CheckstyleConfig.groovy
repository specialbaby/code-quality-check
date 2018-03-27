package com.daijinlin.libcheck.checkstyle

import com.daijinlin.libcheck.common.CommonConfig
import org.gradle.api.Project

/**
 * <pre>
 * Created by J!nl!n on 2018/3/15.
 * Copyright © 1990-2018 J!nl!n™ Inc. All rights reserved.
 *
 * </pre>
 *
 * See <a href="https://docs.gradle.org/4.6/dsl/org.gradle.api.plugins.quality.Checkstyle.html">Checkstyle</a>
 */
class CheckstyleConfig extends CommonConfig {
  String toolVersion = "8.8"
  String checkstyleSuppressionsPath
  /**
   * Whether rule violations are to be displayed on the console.
   */
  boolean showViolations = false

  boolean xmlReports = false
  boolean htmlReports = true

  CheckstyleConfig(Project project) { super(project) }
}