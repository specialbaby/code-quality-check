package com.daijinlin.libcheck

import com.daijinlin.libcheck.checkstyle.CheckstyleConfig
import com.daijinlin.libcheck.common.CommonConfig
import com.daijinlin.libcheck.findbugs.FindbugsConfig
import com.daijinlin.libcheck.lint.LintConfig
import com.daijinlin.libcheck.pmd.PmdConfig
import org.gradle.api.Action
import org.gradle.api.Project

/**
 * <pre>
 * Created by J!nl!n on 2018/3/15.
 * Copyright © 1990-2018 J!nl!n™ Inc. All rights reserved.
 *
 * </pre>
 */
class CodeCheckExtension {

  static final String NAME = "codeCheck"

  private final Project mProject

  CheckstyleConfig mCheckstyleConfig

  void checkstyle(Action<CheckstyleConfig> action) { action.execute(mCheckstyleConfig) }

  FindbugsConfig mFindbugsConfig

  void findbugs(Action<FindbugsConfig> action) { action.execute(mFindbugsConfig) }

  PmdConfig mPmdConfig

  void pmd(Action<PmdConfig> action) { action.execute(mPmdConfig) }

  LintConfig mLintConfig

  void lint(Action<LintConfig> action) { action.execute(mLintConfig) }

  CodeCheckExtension(Project project) {
    this.mProject = project
    this.mCheckstyleConfig = new CheckstyleConfig(project)
    this.mFindbugsConfig = new FindbugsConfig(project)
    this.mPmdConfig = new PmdConfig(project)
    this.mLintConfig = new LintConfig(project)
  }

  /**
   * @desc: 是否忽略
   * @since: 1.0.0
   */
  boolean skip = false

  /**
   * @desc: Whether this task will ignore failures and continue running the build. the same as ignoreFailures
   * @since: 1.0.0
   */
  boolean abortOnError = false

  boolean xmlReports = false

  boolean htmlReports = true

  String reportsPath = "build/reports"

  String[] excludeProjects = []

  String[] includeFiles = ['**/*.java']

  String[] excludeFiles = ['**/gen/**',
                           '**/test/**',
                           '**/androidTest/**',
                           '**/R.java',
                           '**/BuildConfig.java']

}
