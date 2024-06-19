package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.artifacts.dsl.CapabilityNotationParser;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the {@code libs} extension.
 */
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final AvroLibraryAccessors laccForAvroLibraryAccessors = new AvroLibraryAccessors(owner);
    private final FlinkLibraryAccessors laccForFlinkLibraryAccessors = new FlinkLibraryAccessors(owner);
    private final KafkaLibraryAccessors laccForKafkaLibraryAccessors = new KafkaLibraryAccessors(owner);
    private final LogbackLibraryAccessors laccForLogbackLibraryAccessors = new LogbackLibraryAccessors(owner);
    private final Slf4jLibraryAccessors laccForSlf4jLibraryAccessors = new Slf4jLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) {
        super(config, providers, objects, attributesFactory, capabilityNotationParser);
    }

    /**
     * Group of libraries at <b>avro</b>
     */
    public AvroLibraryAccessors getAvro() {
        return laccForAvroLibraryAccessors;
    }

    /**
     * Group of libraries at <b>flink</b>
     */
    public FlinkLibraryAccessors getFlink() {
        return laccForFlinkLibraryAccessors;
    }

    /**
     * Group of libraries at <b>kafka</b>
     */
    public KafkaLibraryAccessors getKafka() {
        return laccForKafkaLibraryAccessors;
    }

    /**
     * Group of libraries at <b>logback</b>
     */
    public LogbackLibraryAccessors getLogback() {
        return laccForLogbackLibraryAccessors;
    }

    /**
     * Group of libraries at <b>slf4j</b>
     */
    public Slf4jLibraryAccessors getSlf4j() {
        return laccForSlf4jLibraryAccessors;
    }

    /**
     * Group of versions at <b>versions</b>
     */
    public VersionAccessors getVersions() {
        return vaccForVersionAccessors;
    }

    /**
     * Group of bundles at <b>bundles</b>
     */
    public BundleAccessors getBundles() {
        return baccForBundleAccessors;
    }

    /**
     * Group of plugins at <b>plugins</b>
     */
    public PluginAccessors getPlugins() {
        return paccForPluginAccessors;
    }

    public static class AvroLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public AvroLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>avro</b> with <b>org.apache.avro:avro</b> coordinates and
         * with version reference <b>avro</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> asProvider() {
            return create("avro");
        }

        /**
         * Dependency provider for <b>compiler</b> with <b>org.apache.avro:avro-compiler</b> coordinates and
         * with version reference <b>avro.compiler</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCompiler() {
            return create("avro.compiler");
        }

        /**
         * Dependency provider for <b>tools</b> with <b>org.apache.avro:avro-tools</b> coordinates and
         * with version reference <b>avro.tools</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getTools() {
            return create("avro.tools");
        }

    }

    public static class FlinkLibraryAccessors extends SubDependencyFactory {
        private final FlinkAvroLibraryAccessors laccForFlinkAvroLibraryAccessors = new FlinkAvroLibraryAccessors(owner);
        private final FlinkConnectorLibraryAccessors laccForFlinkConnectorLibraryAccessors = new FlinkConnectorLibraryAccessors(owner);
        private final FlinkStreamingLibraryAccessors laccForFlinkStreamingLibraryAccessors = new FlinkStreamingLibraryAccessors(owner);

        public FlinkLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>clients</b> with <b>org.apache.flink:flink-clients</b> coordinates and
         * with version reference <b>flink</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getClients() {
            return create("flink.clients");
        }

        /**
         * Group of libraries at <b>flink.avro</b>
         */
        public FlinkAvroLibraryAccessors getAvro() {
            return laccForFlinkAvroLibraryAccessors;
        }

        /**
         * Group of libraries at <b>flink.connector</b>
         */
        public FlinkConnectorLibraryAccessors getConnector() {
            return laccForFlinkConnectorLibraryAccessors;
        }

        /**
         * Group of libraries at <b>flink.streaming</b>
         */
        public FlinkStreamingLibraryAccessors getStreaming() {
            return laccForFlinkStreamingLibraryAccessors;
        }

    }

    public static class FlinkAvroLibraryAccessors extends SubDependencyFactory {
        private final FlinkAvroConfluentLibraryAccessors laccForFlinkAvroConfluentLibraryAccessors = new FlinkAvroConfluentLibraryAccessors(owner);

        public FlinkAvroLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>flink.avro.confluent</b>
         */
        public FlinkAvroConfluentLibraryAccessors getConfluent() {
            return laccForFlinkAvroConfluentLibraryAccessors;
        }

    }

    public static class FlinkAvroConfluentLibraryAccessors extends SubDependencyFactory {

        public FlinkAvroConfluentLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>registry</b> with <b>org.apache.flink:flink-avro-confluent-registry</b> coordinates and
         * with version reference <b>flink</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getRegistry() {
            return create("flink.avro.confluent.registry");
        }

    }

    public static class FlinkConnectorLibraryAccessors extends SubDependencyFactory {

        public FlinkConnectorLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>base</b> with <b>org.apache.flink:flink-connector-base</b> coordinates and
         * with version reference <b>flink</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getBase() {
            return create("flink.connector.base");
        }

        /**
         * Dependency provider for <b>kafka</b> with <b>org.apache.flink:flink-connector-kafka</b> coordinates and
         * with version reference <b>flink.connector.kafka</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getKafka() {
            return create("flink.connector.kafka");
        }

    }

    public static class FlinkStreamingLibraryAccessors extends SubDependencyFactory {

        public FlinkStreamingLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>java</b> with <b>org.apache.flink:flink-streaming-java</b> coordinates and
         * with version reference <b>flink</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJava() {
            return create("flink.streaming.java");
        }

    }

    public static class KafkaLibraryAccessors extends SubDependencyFactory {
        private final KafkaAvroLibraryAccessors laccForKafkaAvroLibraryAccessors = new KafkaAvroLibraryAccessors(owner);

        public KafkaLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>kafka.avro</b>
         */
        public KafkaAvroLibraryAccessors getAvro() {
            return laccForKafkaAvroLibraryAccessors;
        }

    }

    public static class KafkaAvroLibraryAccessors extends SubDependencyFactory {

        public KafkaAvroLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>serializer</b> with <b>io.confluent:kafka-avro-serializer</b> coordinates and
         * with version reference <b>kafka.avro.serializer</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getSerializer() {
            return create("kafka.avro.serializer");
        }

    }

    public static class LogbackLibraryAccessors extends SubDependencyFactory {

        public LogbackLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>classic</b> with <b>ch.qos.logback:logback-classic</b> coordinates and
         * with version reference <b>logback</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getClassic() {
            return create("logback.classic");
        }

        /**
         * Dependency provider for <b>core</b> with <b>ch.qos.logback:logback-core</b> coordinates and
         * with version reference <b>logback</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCore() {
            return create("logback.core");
        }

    }

    public static class Slf4jLibraryAccessors extends SubDependencyFactory {

        public Slf4jLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>api</b> with <b>org.slf4j:slf4j-api</b> coordinates and
         * with version reference <b>slf4j</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getApi() {
            return create("slf4j.api");
        }

    }

    public static class VersionAccessors extends VersionFactory  {

        private final AvroVersionAccessors vaccForAvroVersionAccessors = new AvroVersionAccessors(providers, config);
        private final FlinkVersionAccessors vaccForFlinkVersionAccessors = new FlinkVersionAccessors(providers, config);
        private final KafkaVersionAccessors vaccForKafkaVersionAccessors = new KafkaVersionAccessors(providers, config);
        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>logback</b> with value <b>1.4.12</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getLogback() { return getVersion("logback"); }

        /**
         * Version alias <b>slf4j</b> with value <b>2.0.12</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getSlf4j() { return getVersion("slf4j"); }

        /**
         * Group of versions at <b>versions.avro</b>
         */
        public AvroVersionAccessors getAvro() {
            return vaccForAvroVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.flink</b>
         */
        public FlinkVersionAccessors getFlink() {
            return vaccForFlinkVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.kafka</b>
         */
        public KafkaVersionAccessors getKafka() {
            return vaccForKafkaVersionAccessors;
        }

    }

    public static class AvroVersionAccessors extends VersionFactory  implements VersionNotationSupplier {

        public AvroVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>avro</b> with value <b>1.11.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> asProvider() { return getVersion("avro"); }

        /**
         * Version alias <b>avro.compiler</b> with value <b>1.8.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCompiler() { return getVersion("avro.compiler"); }

        /**
         * Version alias <b>avro.tools</b> with value <b>1.11.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getTools() { return getVersion("avro.tools"); }

    }

    public static class FlinkVersionAccessors extends VersionFactory  implements VersionNotationSupplier {

        private final FlinkConnectorVersionAccessors vaccForFlinkConnectorVersionAccessors = new FlinkConnectorVersionAccessors(providers, config);
        public FlinkVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>flink</b> with value <b>1.18.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> asProvider() { return getVersion("flink"); }

        /**
         * Group of versions at <b>versions.flink.connector</b>
         */
        public FlinkConnectorVersionAccessors getConnector() {
            return vaccForFlinkConnectorVersionAccessors;
        }

    }

    public static class FlinkConnectorVersionAccessors extends VersionFactory  {

        public FlinkConnectorVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>flink.connector.kafka</b> with value <b>3.1.0-1.18</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getKafka() { return getVersion("flink.connector.kafka"); }

    }

    public static class KafkaVersionAccessors extends VersionFactory  {

        private final KafkaAvroVersionAccessors vaccForKafkaAvroVersionAccessors = new KafkaAvroVersionAccessors(providers, config);
        public KafkaVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.kafka.avro</b>
         */
        public KafkaAvroVersionAccessors getAvro() {
            return vaccForKafkaAvroVersionAccessors;
        }

    }

    public static class KafkaAvroVersionAccessors extends VersionFactory  {

        public KafkaAvroVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>kafka.avro.serializer</b> with value <b>6.2.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getSerializer() { return getVersion("kafka.avro.serializer"); }

    }

    public static class BundleAccessors extends BundleFactory {
        private final ProvidedBundleAccessors baccForProvidedBundleAccessors = new ProvidedBundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

        /**
         * Dependency bundle provider for <b>avro</b> which contains the following dependencies:
         * <ul>
         *    <li>org.apache.avro:avro</li>
         *    <li>org.apache.avro:avro-tools</li>
         *    <li>org.apache.avro:avro-compiler</li>
         * </ul>
         * <p>
         * This bundle was declared in catalog libs.versions.toml
         */
        public Provider<ExternalModuleDependencyBundle> getAvro() {
            return createBundle("avro");
        }

        /**
         * Dependency bundle provider for <b>logback</b> which contains the following dependencies:
         * <ul>
         *    <li>ch.qos.logback:logback-classic</li>
         *    <li>ch.qos.logback:logback-core</li>
         * </ul>
         * <p>
         * This bundle was declared in catalog libs.versions.toml
         */
        public Provider<ExternalModuleDependencyBundle> getLogback() {
            return createBundle("logback");
        }

        /**
         * Group of bundles at <b>bundles.provided</b>
         */
        public ProvidedBundleAccessors getProvided() {
            return baccForProvidedBundleAccessors;
        }

    }

    public static class ProvidedBundleAccessors extends BundleFactory {

        public ProvidedBundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

        /**
         * Dependency bundle provider for <b>provided.flink</b> which contains the following dependencies:
         * <ul>
         *    <li>org.apache.flink:flink-streaming-java</li>
         *    <li>org.apache.flink:flink-clients</li>
         *    <li>io.confluent:kafka-avro-serializer</li>
         * </ul>
         * <p>
         * This bundle was declared in catalog libs.versions.toml
         */
        public Provider<ExternalModuleDependencyBundle> getFlink() {
            return createBundle("provided.flink");
        }

    }

    public static class PluginAccessors extends PluginFactory {

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

    }

}
