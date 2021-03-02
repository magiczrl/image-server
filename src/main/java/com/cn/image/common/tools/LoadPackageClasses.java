package com.cn.image.common.tools;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * The type Load package classes.
 * @Type LoadPackageClasses.java
 * @Desc package中类获取器
 * @version
 */
public class LoadPackageClasses {

    /**
     * 日志
     */
    protected final Log logger = LogFactory.getLog(getClass());

    /**
     * RESOURCE_PATTERN  资源匹配规则
     */
    private static final String RESOURCE_PATTERN = "/**/*.class";

    /**
     * 资源解析器
     */
    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    /**
     * package列表
     */
    private List<String> packagesList = new LinkedList<String>();

    /**
     * 类型列表
     */
    private List<TypeFilter> typeFilters = new LinkedList<TypeFilter>();

    /**
     * 类Set集合
     */
    private Set<Class<?>> classSet = new HashSet<Class<?>>();

    /**
     * 构造函数
     * @param packagesToScan 指定哪些包需要被扫描,支持多个包"package.a,package.b"并对每个包都会递归搜索
    * @param annotationFilter 指定扫描包中含有特定注解标记的bean,支持多个注解
    */
    public LoadPackageClasses(String[] packagesToScan,
                              @SuppressWarnings("unchecked") Class<? extends Annotation>... annotationFilter) {
        if (packagesToScan != null) {
            for (String packagePath : packagesToScan) {
                this.packagesList.add(packagePath);
            }
        }
        if (annotationFilter != null) {
            for (Class<? extends Annotation> annotation : annotationFilter) {
                typeFilters.add(new AnnotationTypeFilter(annotation, false));
            }
        }
    }

    /**
     * 将符合条件的Bean以Class集合的形式返回
     * @return  class set
    * @throws IOException the io exception
    * @throws ClassNotFoundException the class not found exception
    */
    public Set<Class<?>> getClassSet() throws IOException, ClassNotFoundException {
        this.classSet.clear();
        if (!this.packagesList.isEmpty()) {
            for (String pkg : this.packagesList) {
                String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                        + ClassUtils.convertClassNameToResourcePath(pkg) + RESOURCE_PATTERN;
                Resource[] resources = this.resourcePatternResolver.getResources(pattern);
                MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(
                        this.resourcePatternResolver);
                for (Resource resource : resources) {
                    if (resource.isReadable()) {
                        MetadataReader reader = readerFactory.getMetadataReader(resource);
                        String className = reader.getClassMetadata().getClassName();
                        if (matchesEntityTypeFilter(reader, readerFactory)) {
                            this.classSet.add(Class.forName(className));
                        }
                    }
                }
            }
        }
        //输出日志
        if (logger.isInfoEnabled()) {
            for (Class<?> clazz : this.classSet) {
                logger.info(String.format("Found class:%s", clazz.getName()));
            }
        }
        return this.classSet;
    }

    /**
     * 检查当前扫描到的Bean含有任何一个指定的注解标记
     * @param reader
     * @param readerFactory
     * @return
     * @throws IOException
     */
    private boolean matchesEntityTypeFilter(MetadataReader reader,
                                            MetadataReaderFactory readerFactory)
            throws IOException {
        if (!this.typeFilters.isEmpty()) {
            for (TypeFilter filter : this.typeFilters) {
                if (filter.match(reader, readerFactory)) {
                    return true;
                }
            }
        }
        return false;
    }

}
