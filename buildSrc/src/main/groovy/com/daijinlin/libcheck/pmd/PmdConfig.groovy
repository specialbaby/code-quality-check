package com.daijinlin.libcheck.pmd

import com.daijinlin.libcheck.common.CommonConfig
import org.gradle.api.Project

/**
 * <pre>
 * Created by J!nl!n on 2018/3/15.
 * Copyright © 1990-2018 J!nl!n™ Inc. All rights reserved.
 *
 * </pre>
 */
class PmdConfig extends CommonConfig {
  String toolVersion = "6.1.0"

  List<File> ruleSets = []

  PmdConfig(Project project) { super(project) }
}