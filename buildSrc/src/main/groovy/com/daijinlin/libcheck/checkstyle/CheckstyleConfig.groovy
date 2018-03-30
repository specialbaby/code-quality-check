package com.daijinlin.libcheck.checkstyle

import com.daijinlin.libcheck.common.CommonConfig
import com.daijinlin.libcheck.common.Utils
import org.gradle.api.Project

/**
 * <pre>
 * Created by J!nl!n on 2018/3/15.
 * Copyright © 1990-2018 J!nl!n™ Inc. All rights reserved.
 *
 * </pre>
 *
 *
 */
class CheckstyleConfig extends CommonConfig {
  String toolVersion = "8.8"
  String checkstyleSuppressionsPath
  /**
   * Whether rule violations are to be displayed on the console.
   */
  boolean showViolations = false

  CheckstyleConfig(Project project) { super(project) }

  File resolveSuppressionsFile() {
    File file = new File(project.buildDir, "config/code-check/suppressions.xml")
    file.parentFile.mkdirs()
    file.delete()
    file << resolveSuppressions()
    return file
  }

  private String resolveSuppressions() {
    if (checkstyleSuppressionsPath) {
      return project.file(checkstyleSuppressionsPath).text
    }

    File file = project.file("config/quality/checkstyle/suppressions.xml")
    if (file.exists()) {
      return file.text
    }

    File rootFile = project.rootProject.file("config/quality/checkstyle/suppressions.xml")
    if (rootFile.exists()) {
      return rootFile.text
    }

    return Utils.getResource(project, "checkstyle/suppressions.xml")
  }
}