package com.daijinlin.libcheck.findbugs

import com.daijinlin.libcheck.CodeCheckExtension
import com.daijinlin.libcheck.common.CommonCheck
import com.daijinlin.libcheck.common.L
import com.daijinlin.libcheck.common.Utils
import groovy.util.slurpersupport.GPathResult
import org.gradle.api.Project
import org.gradle.api.plugins.quality.FindBugs

/**
 * <pre>
 * Created by J!nl!n on 2018/3/15.
 * Copyright © 1990-2018 J!nl!n™ Inc. All rights reserved.
 *
 * </pre>
 * See <a href="https://docs.gradle.org/4.6/dsl/org.gradle.api.plugins.quality.FindBugs.html">FindBugs</a>
 */
class FindbugsCheck extends CommonCheck<FindbugsConfig> {

  FindbugsCheck() { super('findbugs', 'Runs FindBugs Task') }

  @Override
  protected FindbugsConfig getConfig(CodeCheckExtension extension) {
    return extension.mFindbugsConfig
  }

  @Override
  protected int getErrorCount(File xmlReportFile) {
    GPathResult xml = new XmlSlurper().parseText(xmlReportFile.text)
    return xml.FindBugsSummary.getProperty('@total_bugs').text() as int
  }

  @Override
  protected String getErrorMessage(int errorCount, File htmlReportFile) {
    return "$errorCount FindBugs rule violations were found. See the report at: ${htmlReportFile.toURI()}"
  }

  @Override
  protected void performCheck(Project project, List sources, File configFile, File xmlReportFile, File htmlReportFile) {
    project.plugins.apply(taskName)
    project.tasks.getByName('check').dependsOn taskName
    project.findbugs {
      sourceSets = []
      ignoreFailures = extension.mFindbugsConfig.ignoreFailures
      toolVersion = extension.mFindbugsConfig.toolVersion
      effort = extension.mFindbugsConfig.effort
      reportLevel = extension.mFindbugsConfig.reportLevel
      excludeFilter = configFile/*rootProject.file(extension.findbugs.excludeFilter)*/
      L.d("toolVersion:" + toolVersion + " effort:" + effort + " reportLevel:" + reportLevel)
    }

    project.task('findbugs', type: FindBugs, dependsOn: 'assemble') {
      description = this.taskDescription
      group = this.taskGroup

      boolean isJava = Utils.isJavaProject(project)
//      classes = /*subProject.fileTree(findbugsClassesPath)*/
      if (isJava) {
        classes = project.files("$project.projectDir.absolutePath/build/classes")
      } else {
        classes = project.files("$project.projectDir.absolutePath/build/intermediates/classes")
      }
      source = sources/*subProject.fileTree(extension.findbugs.source)*/
      // empty classpath
      classpath = project.files()

      reports {
        xml.enabled = extension.xmlReports
        xml.destination xmlReportFile
        html.enabled = extension.htmlReports
        html.destination htmlReportFile
        //findbugs不能同时生成xml和html文件
        if (xml.enabled && html.enabled) {
          xml.enable = false
        }
      }
    }
  }
}