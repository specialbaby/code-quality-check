package com.daijinlin.libcheck.findbugs

import com.daijinlin.libcheck.CodeCheckExtension
import com.daijinlin.libcheck.common.CommonCheck
import com.daijinlin.libcheck.common.L
import com.daijinlin.libcheck.common.Utils
import groovy.util.slurpersupport.GPathResult
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.quality.FindBugs
import org.gradle.api.tasks.TaskState

/**
 * <pre>
 * Created by J!nl!n on 2018/3/15.
 * Copyright © 1990-2018 J!nl!n™ Inc. All rights reserved.
 *
 * </pre>
 * @see <a href="https://docs.gradle.org/4.6/dsl/org.gradle.api.plugins.quality.FindBugs.html">FindBugs</a>
 */
class FindbugsCheck extends CommonCheck<FindbugsConfig> {

  FindbugsCheck() { super('findbugs', 'Runs FindBugs Task') }

  @Override
  protected FindbugsConfig getConfig(CodeCheckExtension extension) {
    return extension.mFindbugsConfig
  }

  @Override
  protected int getErrorCount(File xmlReportFile, File htmlReportFile) {
    if (xmlReportFile.exists()) {
      GPathResult xml = new XmlSlurper().parseText(xmlReportFile.text)
      return xml.FindBugsSummary.getProperty('@total_bugs').text() as int
    } else if (htmlReportFile.exists()) {
//      def parser = new XmlSlurper()
//      parser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)
//      parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
//      def html = parser.parseText(htmlReportFile.text)
//      html.body.findAll() {
//      }
      return 123
    }
    return 0
  }

  @Override
  protected String getErrorMessage(int errorCount, File xmlReportFile, File htmlReportFile) {
    return "$errorCount FindBugs rule violations were found. See the report at: ${htmlReportFile.absolutePath}"
  }

  @Override
  protected void performCheck(Project project, List sources, File configFile, File xmlReportFile, File htmlReportFile) {
    project.plugins.apply(taskName)
    project.tasks.getByName('check').dependsOn taskName

    project.findbugs {
      toolVersion = extension.mFindbugsConfig.toolVersion
      effort = extension.mFindbugsConfig.effort
      reportLevel = extension.mFindbugsConfig.reportLevel
      excludeFilter = configFile
    }

    project.task('findbugs', type: FindBugs, dependsOn: 'assemble') {
      description = this.taskDescription
      group = this.taskGroup

      boolean isJava = Utils.isJavaProject(project)
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
        //,findbugs不能同时生成xml和html文件
        if (xml.enabled && html.enabled) {
          xml.enabled = false
        }
      }
    }
  }
}