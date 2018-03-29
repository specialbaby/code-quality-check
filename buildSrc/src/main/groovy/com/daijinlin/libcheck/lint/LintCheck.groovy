package com.daijinlin.libcheck.lint

import com.daijinlin.libcheck.CodeCheckExtension
import com.daijinlin.libcheck.common.CommonCheck
import com.daijinlin.libcheck.common.CommonConfig
import org.gradle.api.Project

/**
 * <pre>
 * Created by J!nl!n on 2018/3/15.
 * Copyright © 1990-2018 J!nl!n™ Inc. All rights reserved.
 *
 * </pre>
 *
 * @see <a href="https://developer.android.com/studio/write/lint.html">Improve Your Code with Lint</a>
 * @see <a href="http://google.github.io/android-gradle-dsl/current/com.android.build.gradle.internal.dsl.LintOptions.html#com.android.build.gradle.internal.dsl.LintOptions">LintOptions</a>
 */
class LintCheck extends CommonCheck {

  LintCheck() {
    super("lint", "Runs Android Lint")
  }

  @Override
  protected CommonConfig getConfig(CodeCheckExtension extension) {
    return extension.mLintConfig
  }

  @Override
  protected int getErrorCount(File xmlReportFile, File htmlReportFile) {
    return 1
  }

  @Override
  protected String getErrorMessage(int errorCount, File xmlReportFile, File htmlReportFile) {
    return "$errorCount issues found. See the report at:${htmlReportFile.toURI()}"
  }

  @Override
  protected void performCheck(Project project, List sources, File configFile, File xmlReportFile, File htmlReportFile) {
    project.android.lintOptions {
      warningsAsErrors false
      abortOnError extension.abortOnError
    }

//    if (extension.lint.checkAllWarnings != null) {
//      subProject.android.lintOptions {
//        checkAllWarnings = extension.lint.checkAllWarnings
//      }
//    }
//
//    if (extension.lint.absolutePaths != null) {
//      subProject.android.lintOptions {
//        absolutePaths = extension.lint.absolutePaths
//      }
//    }
//
//    if (extension.lint.baselineFileName != null) {
//      subProject.android.lintOptions {
//        baseline subProject.file(extension.lint.baselineFileName)
//      }
//    }
//
    if (extension.lint.lintConfig != null) {
      project.android.lintOptions {
        lintConfig project.file("${project.rootDir}/config/quality/lint/lint.xml")
      }
    }
//
//    if (extension.lint.checkReleaseBuilds != null) {
//      subProject.android.lintOptions {
//        checkReleaseBuilds extension.lint.checkReleaseBuilds
//      }
//    }
//
//    if (extension.lint.textReport != null) {
//      subProject.android.lintOptions {
//        textReport extension.lint.textReport
//        textOutput extension.lint.textOutput
//      }
//    }
  }

}
