import dev.kikugie.stonecutter.build.StonecutterBuildExtension
import net.fabricmc.loom.util.Constants
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskProvider
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.gradle.kotlin.dsl.the

/**
 * fabric-loomの難読化版、非難読化版の差分を吸収するGradleプラグイン。
 * Kikugie氏の実装を元に、Matt Sturgeon氏の改良を参考にしつつ、少し変更を加えている。
 * @author Kikugie
 * @author Matt Sturgeon
 * @author BlueSheep2804
 * @see <a href="https://discord.com/channels/1135884510613995590/1192572106056142951/1485634997837893653">オリジナル</a>
 * @see <a href="https://github.com/MinecraftFreecam/Freecam/blob/accbafa7ab093990cf4aecb2bcd281b8fd1df53f/build-logic/loom-adapter/src/main/kotlin/net/xolt/freecam/gradle/LoomAdapterPlugin.kt">Matt Sturgeon氏の実装(Freecam)</a>
 */
open class FabricPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        val current = the<StonecutterBuildExtension>().current.parsed
        if (current > "26.0") setupNewLoomFacade() else setupOldLoomFacade()
        extensions.create<FabricExtensions>("fabric", this, current > "26.0")
    }

    private fun Project.setupNewLoomFacade() {
        plugins.apply("net.fabricmc.fabric-loom")

        val names = listOf(
            JavaPlugin.API_CONFIGURATION_NAME,
            JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME,
            JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME,
            JavaPlugin.RUNTIME_ONLY_CONFIGURATION_NAME,
            Constants.Configurations.LOCAL_RUNTIME
        )

        for (it in names) {
            val modConfiguration: Provider<Configuration> = configurations.register("mod" + it.uppercaseFirstChar())
            configurations.named(it) { extendsFrom(modConfiguration.get()) }
        }

        configurations.register("mappings") {
            isCanBeResolved = false
            isCanBeConsumed = false
        }
    }

    private fun Project.setupOldLoomFacade() {
        plugins.apply("net.fabricmc.fabric-loom-remap")
    }

    open class FabricExtensions(val project: Project, val isNew: Boolean) {
        val modJar: TaskProvider<Jar> by lazy {
            val candidate = if (isNew) project.tasks.named("jar")
            else project.tasks.named("remapJar")
            candidate as TaskProvider<Jar>
        }

        val modSourcesJar: TaskProvider<Jar> by lazy {
            val candidate = if (isNew) project.tasks.named("sourcesJar")
            else project.tasks.named("remapSourcesJar")
            candidate as TaskProvider<Jar>
        }
    }
}