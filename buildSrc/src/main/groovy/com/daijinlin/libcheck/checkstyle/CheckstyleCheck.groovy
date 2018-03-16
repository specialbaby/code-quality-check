package com.daijinlin.libcheck.checkstyle

import com.daijinlin.libcheck.CodeCheckExtension
import com.daijinlin.libcheck.common.CommonCheck
import com.daijinlin.libcheck.common.CommonConfig
import groovy.util.slurpersupport.GPathResult
import org.gradle.api.Project
import org.gradle.api.plugins.quality.Checkstyle

/**
 * <pre>
 * Created by J!nl!n on 2018/3/15.
 * Copyright © 1990-2018 J!nl!n™ Inc. All rights reserved.
 *
 * </pre>
 */
class CheckstyleCheck extends CommonCheck<CheckstyleConfig> {

  CheckstyleCheck() { super('checkstyle', 'Runs Checkstyle Task') }

  @Override
  protected CheckstyleConfig getConfig(CodeCheckExtension extension) {
    return extension.mCheckstyleConfig
  }

  @Override
  protected void performCheck(Project project, List sources, File config, File xmlReportFile) {
    project.plugins.apply(taskName) // 1.应用插件
    project.checkstyle {
      toolVersion = "8.5"
      configFile config
      ignoreFailures true
      showViolations true
    }

    project.task(taskName, type: Checkstyle) {
      description = this.taskDescription
      group = 'verification'

      source sources
      include '**/*.java'
      exclude '**/gen/**'
      exclude '**/test/**'
      exclude '**/androidTest/**'
      exclude '**/R.java'
      exclude '**/BuildConfig.java'

      classpath = project.files()

      doLast {
        reports {
          html.enabled = true
          xml.enabled = true
        }
      }
    }

    project.tasks.getByName('check').dependsOn taskName
  }

  @Override
  protected int getErrorCount(File xmlReportFile) {
    GPathResult xml = new XmlSlurper().parseText(xmlReportFile.text)
    return xml.file.inject(0) { count, file -> count + file.error.size() }
  }

  @Override
  protected String getErrorMessage(int errorCount, File htmlReportFile) {
    return "$errorCount Checkstyle rule violations were found. See the report at: ${htmlReportFile.toURI()}"
  }

}