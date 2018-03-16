package com.daijinlin.libcheck.pmd

import com.daijinlin.libcheck.CodeCheckExtension
import com.daijinlin.libcheck.common.CommonCheck
import com.daijinlin.libcheck.common.CommonConfig
import org.gradle.api.Project
import org.gradle.api.plugins.quality.Pmd

/**
 * <pre>
 * Created by J!nl!n on 2018/3/15.
 * Copyright © 1990-2018 J!nl!n™ Inc. All rights reserved.
 *
 * </pre>
 */
class PmdCheck extends CommonCheck<PmdConfig> {

  PmdCheck() { super('pmd', 'Runs PMD Task') }

  @Override
  protected PmdConfig getConfig(CodeCheckExtension extension) {
    return extension.mPmdConfig
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

    project.pmd {
      toolVersion = "6.1.0"/*extension.pmd.toolVersion*/
      ignoreFailures = true/*extension.pmd.ignoreFailures != null ? extension.pmd.ignoreFailures : !extension.failEarly*/
      ruleSetFiles = project.files("${project.rootDir}/config/quality/pmd/pmd-ruleset.xml")/*subProject.files(rootProject.file(extension.pmd.ruleSetFile))*/
      ruleSets = []
    }

    project.task(taskName, type: Pmd) {
      description = this.taskDescription
      group = GROUP_VERIFICATION

      source = sources/*project.fileTree(extension.pmd.source)*/
      include '**/*.java'/*extension.pmd.include*/
      exclude('**/gen/**', '**/debug/**')/*extension.pmd.exclude*/

      doLast {
        reports {
          html.enabled = true/*extension.htmlReports*/
          xml.destination project.file("$project.buildDir/reports/pmd/pmd.xml")
          xml.enabled = true/*extension.xmlReports*/
          html.destination project.file("$project.buildDir/reports/pmd/pmd.html")
        }
      }
    }

    project.tasks.getByName('check').dependsOn taskName
  }
}
