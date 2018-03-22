package com.daijinlin.libcheck.findbugs

import com.daijinlin.libcheck.CodeCheckExtension
import com.daijinlin.libcheck.common.CommonCheck
import com.daijinlin.libcheck.common.CommonConfig
import com.daijinlin.libcheck.common.L
import com.daijinlin.libcheck.common.Utils
import org.gradle.api.Project
import org.gradle.api.plugins.quality.FindBugs

/**
 * <pre>
 * Created by J!nl!n on 2018/3/15.
 * Copyright © 1990-2018 J!nl!n™ Inc. All rights reserved.
 *
 * </pre>
 */
class FindbugsCheck extends CommonCheck<FindbugsConfig> {

  FindbugsCheck() { super('findbugs', 'Runs FindBugs Task') }

  @Override
  protected FindbugsConfig getConfig(CodeCheckExtension extension) {
    return extension.mFindbugsConfig
  }

  @Override
  protected int getErrorCount(File xmlReportFile) {
    return 0
  }

  @Override
  protected String getErrorMessage(int errorCount, File htmlReportFile) {
    return null
  }

  @Override
  protected void performCheck(Project project, List sources, File configFile, File xmlReportFile) {
    project.plugins.apply(taskName)
    project.tasks.getByName('check').dependsOn taskName

    project.findbugs {
      sourceSets = []
      ignoreFailures = true
/*extension.findbugs.ignoreFailures != null ? extension.findbugs.ignoreFailures : !extension.failEarly*/
      toolVersion = "3.0.1"/*extension.findbugs.toolVersion*/
      effort = "max"/*extension.findbugs.effort*/
      reportLevel = "high"/*extension.findbugs.reportLevel*/
      excludeFilter = configFile/*rootProject.file(extension.findbugs.excludeFilter)*/
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
        xml.destination project.file("$project.buildDir/reports/findbugs/findbugs.xml")
        html.enabled = extension.htmlReports
        html.destination project.file("$project.buildDir/reports/findbugs/findbugs.html")
      }
    }

  }
}