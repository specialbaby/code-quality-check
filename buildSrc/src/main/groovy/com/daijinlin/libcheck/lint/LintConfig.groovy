package com.daijinlin.libcheck.lint

import com.daijinlin.libcheck.common.CommonConfig
import org.gradle.api.Project

/**
 * <pre>
 * Created by J!nl!n on 2018/3/15.
 * Copyright © 1990-2018 J!nl!n™ Inc. All rights reserved.
 *
 * </pre>
 */
class LintConfig extends CommonConfig {
  boolean warningsAsErrors = false
  boolean ignoreWarnings = true
  LintConfig(Project project) { super(project) }
}