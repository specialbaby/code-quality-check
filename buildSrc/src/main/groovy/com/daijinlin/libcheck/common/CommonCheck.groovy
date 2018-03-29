package com.daijinlin.libcheck.common

import com.daijinlin.libcheck.CodeCheckExtension
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.tasks.TaskState

abstract class CommonCheck<Config extends CommonConfig> implements TaskExecutionListener {

  final String taskName
  final String taskDescription
  final String taskGroup = "verification"
  CodeCheckExtension extension
  File xmlReportFile
  File htmlReportFile

  CommonCheck(String taskName, String taskDescription) {
    this.taskName = taskName
    this.taskDescription = taskDescription
  }

  protected Set<String> getDependencies() { [] }

  protected abstract Config getConfig(CodeCheckExtension extension)

  protected abstract void performCheck(Project project, List<File> sources,
                                       File configFile, File xmlReportFile, File htmlReportFile)

  protected abstract int getErrorCount(File xmlReportFile, File htmlReportFile)

  protected abstract String getErrorMessage(int errorCount, File xmlReportFile, File htmlReportFile)

  protected void reformatReport(Project project, File styleFile,
                                File xmlReportFile, File htmlReportFile) {
    project.ant.xslt(in: xmlReportFile, out: htmlReportFile) {
      style { string(styleFile.text) }
    }
  }

  void apply(Project target) {
    extension = target.extensions.findByType(CodeCheckExtension)
    final String name = target.name
    target.gradle.addListener(this)
    if (extension.excludeProjects.contains(name) || extension.skip) { // 如果项目在忽略列表中则直接跳过
      L.d("skip project $name")
      return
    }
    Config config = getConfig(extension)
    boolean skip = config.resolveSkip(extension.skip)
    File configFile = config.resolveConfigFile(taskName)
    xmlReportFile = target.file("$extension.reportsPath/${taskName}/${taskName}.xml")
    htmlReportFile = target.file("$extension.reportsPath/${taskName}/${taskName}.html")
    List<File> sources = config.getAndroidSources()

    if (skip) {
      L.d("skip $taskName")
    } else {
      performCheck(target, sources, configFile, xmlReportFile, htmlReportFile)
    }
  }

  @Override
  void beforeExecute(Task task) {
  }

  @Override
  void afterExecute(Task task, TaskState taskState) {
    if (task.name == taskName) {
      boolean abortOnError = getConfig(extension).resolveAbortOnError(extension.abortOnError)
      int errorCount = getErrorCount(xmlReportFile, htmlReportFile)
      if (errorCount) {
        String errorMessage = getErrorMessage(errorCount, xmlReportFile, htmlReportFile)
        if (abortOnError) {
          throw new GradleException(errorMessage)
        } else {
          L.d(errorMessage)
        }
      }
    }
  }
}