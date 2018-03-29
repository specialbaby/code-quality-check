package com.daijinlin.libcheck.checkstyle

import com.daijinlin.libcheck.CodeCheckExtension
import com.daijinlin.libcheck.common.CommonCheck
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
  protected void performCheck(Project project, List sources, File config, File xmlReportFile, File htmlReportFile) {
    project.plugins.apply(taskName) // 1.应用插件
    project.tasks.getByName('check').dependsOn taskName

    project.checkstyle {
      toolVersion = extension.mCheckstyleConfig.toolVersion
      //configFile config
      configFile project.file("${project.rootDir}/config/quality/checkstyle/checkstyle.xml")
      configProperties.checkstyleSuppressionsPath = project.file("${project.rootDir}/config/quality/checkstyle/suppressions.xml").absolutePath
      //configProperties.checkstyleSuppressionsPath = project.file(extension.mCheckstyleConfig.checkstyleSuppressionsPath).absolutePath
      ignoreFailures extension.abortOnError
      // Whether this task will ignore failures and continue running the build.
      showViolations extension.mCheckstyleConfig.showViolations
      // Whether rule violations are to be displayed on the console.
    }

    project.task(taskName, type: Checkstyle) {
      description = this.taskDescription
      group = this.taskGroup

      source sources
      include extension.includeFiles
      exclude extension.excludeFiles
      // empty classpath
      classpath = project.files()

      reports {
        xml.enabled = extension.mCheckstyleConfig.xmlReports
        xml.destination xmlReportFile
        //xml.destination project.file(extension.mCheckstyleConfig.xmlReportsPath)
        html.enabled = extension.mCheckstyleConfig.htmlReports
        html.destination htmlReportFile
        //html.destination project.file(extension.mCheckstyleConfig.htmlReportsPath)
      }
    }
  }

  @Override
  protected int getErrorCount(File xmlReportFile, File htmlReportFile) {
    if (xmlReportFile.exists()) {
      GPathResult xml = new XmlSlurper().parseText(xmlReportFile.text)
      return xml.file.inject(0) { count, file -> count + file.error.size() }
    } else if (htmlReportFile.exists()) {
      //todo:解析html
      return 123
    }
    return 0
  }

  @Override
  protected String getErrorMessage(int errorCount, File xmlReportFile, File htmlReportFile) {
    return "$errorCount Checkstyle rule violations were found. See the report at: ${htmlReportFile.toURI()}"
  }

}