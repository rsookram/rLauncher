import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

/**
 * Reduces APK size by removing classes from dependencies that R8 is unable to
 */
class RemoveClassTransform : Transform() {

    // R8 would normally keep these because recyclerview v1.1.0 includes a rule
    // to keep all LayoutManagers, because they can be instantiated through
    // reflection if referenced in XML (by the layoutManager attribute).
    private val classesToRemove = setOf(
        "androidx/recyclerview/widget/GridLayoutManager.class",
        "androidx/recyclerview/widget/StaggeredGridLayoutManager.class"
    )

    override fun getName() = "RemoveClass"

    override fun getInputTypes() = setOf(QualifiedContent.DefaultContentType.CLASSES)

    override fun isIncremental() = true

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> =
        mutableSetOf(QualifiedContent.Scope.EXTERNAL_LIBRARIES)

    override fun transform(transformInvocation: TransformInvocation) {
        transformInvocation.inputs
            .flatMap { it.jarInputs }
            .forEach { jar ->
                val inputFile = JarFile(jar.file)

                val outputFile = transformInvocation.outputProvider.getContentLocation(
                    jar.name, jar.contentTypes, jar.scopes, Format.JAR
                )

                outputFile.outputStream().use { os ->
                    JarOutputStream(os).use { outputStream ->
                        inputFile.entries().asSequence()
                            .filter { it.name !in classesToRemove }
                            .forEach { entry ->
                                val zipEntry = ZipEntry(entry.name)
                                outputStream.putNextEntry(zipEntry)

                                inputFile.getInputStream(entry).use {
                                    it.copyTo(outputStream)
                                }

                                outputStream.closeEntry()
                            }
                    }
                }
            }
    }
}
