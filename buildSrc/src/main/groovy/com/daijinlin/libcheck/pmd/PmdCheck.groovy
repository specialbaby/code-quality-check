package com.daijinlin.libcheck.pmd

import com.daijinlin.libcheck.CodeCheckExtension
import com.daijinlin.libcheck.common.CommonCheck
import org.gradle.api.Project
import org.gradle.api.plugins.quality.Pmd

/**
 * <pre>
 * Created by J!nl!n on 2018/3/15.
 * Copyright © 1990-2018 J!nl!n™ Inc. All rights reserved.
 *
 * </pre>
 *
 * @see <a href="https://docs.gradle.org/4.6/dsl/org.gradle.api.plugins.quality.Pmd.html">Pmd</a>
 */
class PmdCheck extends CommonCheck<PmdConfig> {

  PmdCheck() { super('pmd', 'Runs PMD Task') }

  @Override
  protected PmdConfig getConfig(CodeCheckExtension extension) {
    return extension.mPmdConfig
  }

  @Override
  protected int getErrorCount(File xmlReportFile, File htmlReportFile) {
    return 1
  }

  @Override
  protected String getErrorMessage(int errorCount, File xmlReportFile, File htmlReportFile) {
    return "$errorCount Pmd rule violations were found. See the report at: ${htmlReportFile.toURI()}"
  }

  @Override
  protected void performCheck(Project project, List sources, File configFile, File xmlReportFile, File htmlReportFile) {
    project.plugins.apply(taskName)
    project.tasks.getByName('check').dependsOn taskName

    project.pmd {
      toolVersion = extension.mPmdConfig.toolVersion
      // Whether or not to allow the build to continue if there are warnings. Example: ignoreFailures = true
      ignoreFailures = !extension.abortOnError
      ruleSetFiles = project.files(configFile.absolutePath)
      ruleSets = extension.mPmdConfig.ruleSets
    }

    project.task(taskName, type: Pmd) {
      description = this.taskDescription
      group = this.taskGroup

      source = sources
      include extension.includeFiles
      exclude extension.excludeFiles

      reports {
        xml.enabled = extension.xmlReports
        xml.destination xmlReportFile
        html.enabled = extension.htmlReports
        html.destination htmlReportFile
      }
    }
  }
}
