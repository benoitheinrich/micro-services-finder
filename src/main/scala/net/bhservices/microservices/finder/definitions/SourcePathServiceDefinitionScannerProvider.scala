package net.bhservices.microservices.finder.definitions

import java.io.File

import net.bhservices.microservices.finder.{FileUtility, VerboseLogger}

import scala.io.Source

/**
 * Scans the `sourcePath` for any files of a given type.
 */
trait SourcePathServiceDefinitionScannerProvider extends ServiceDefinitionScannerProvider {
  override def scanner = new SourcePathServiceDefinitionScanner

  def sourcePath: File

  class SourcePathServiceDefinitionScanner extends ServiceDefinitionScanner {

    import scala.language.postfixOps

    /**
     * Scan the definitions from the scanner.
     * @return the list of scanned definitions.
     */
    override def scanDefinitions: Seq[ServiceDefinition] = {
      val apiJavaFilePattern = """.*/([^/]*)/src/main/.*/([^/]*).java""".r

      val filesAndBaseDefinition: Seq[(File, ServiceDefinition)] = FileUtility.getFileTree(sourcePath).filter(_.getName.endsWith(".java")).flatMap { f =>
        f.getAbsoluteFile.getPath match {
          case apiJavaFilePattern(moduleName, className) =>
            Some((f, ServiceDefinition(name = className, module = moduleName)))
          case _ => None
        }
      }

      filesAndBaseDefinition.par flatMap {
        case (f, serviceDef) =>
          val content = Source.fromFile(f).getLines().mkString("\n")
          parseDefinition(content, serviceDef)
      } toList
    }


    private def parseDefinition(content: String, serviceDef: ServiceDefinition): Option[ServiceDefinition] = {
      val serviceNameMatcher = """(?s).*package\s+([^;\s]+)\s*;.*public\s+interface\s+([^\s{]*)[\s+{].*""".r
      val extendsMatcher = """(?s).*\s+extends\s+([^{]*)\s*\{.*""".r
      val importMatcher = """(?s).*import\s+([^;\s]+)\s*;""".r

      def fullClassName(packageName: String, serviceName: String): ModuleName = s"$packageName.$serviceName"

      def extractAllImports(content: String): Seq[ModuleName] = {
        importMatcher.findAllMatchIn(content) map { r =>
          r.group(1)
        } toSeq
      }

      def extractExtends(imports: Seq[ModuleName], extendsString: String, packageName: String): Seq[ModuleName] = {
        extendsString.split(",") map (extendsPiece => extendsPiece.trim) map {
          serviceName =>
            imports.find(_.endsWith(serviceName)) match {
              case Some(importedClass) => importedClass
              case None => fullClassName(packageName, serviceName)
            }
        }
      }


      content match {
        case serviceNameMatcher(packageName, serviceName) =>
          val qualifiedService = content match {
            case extendsMatcher(extendsString) =>
              val imports = extractAllImports(content)
              Some(serviceDef.copy(name = fullClassName(packageName, serviceName), extendsList = extractExtends(imports, extendsString, packageName)))
            case _ =>
              Some(serviceDef.copy(name = fullClassName(packageName, serviceName)))
          }
          VerboseLogger.log(s"Service definition match found: $qualifiedService")

          qualifiedService
        case _ =>
          None
      }
    }
  }

}
