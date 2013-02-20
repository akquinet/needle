package de.akquinet.jbosscc.needle.postconstruct;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import de.akquinet.jbosscc.needle.ObjectUnderTestInstantiationException;
import de.akquinet.jbosscc.needle.annotation.ObjectUnderTest;
import de.akquinet.jbosscc.needle.reflection.ReflectionUtil;

/**
 * Handles execution of postConstruction methods of instances marked with {@link ObjectUnderTest#postConstruct()}
 * <p>
 * Note: Behavior in an inheritance hierarchy is not defined by the common annotations specification
 * </p>
 * 
 * @author Jan Galinski, Holisticon AG (jan.galinski@holisticon.de)
 * @author Heinz Wilming, akquinet AG (heinz.wilming@akquinet.de)
 */
public class PostConstructProcessor {

    /**
     * internal Container of all Annotations that trigger invocation
     */
    private final Set<Class<? extends Annotation>> postConstructAnnotations = new HashSet<Class<? extends Annotation>>();

    @SuppressWarnings("unchecked")
    public PostConstructProcessor(final Set<Class<?>> postConstructAnnotations) {
        for (final Class<?> annotation : postConstructAnnotations) {
            this.postConstructAnnotations.add((Class<? extends Annotation>)annotation);
        }
    }

    /**
     * calls process(instance) only if field is marked with
     * 
     * @ObjectUNderTest(postConstruct=true), else ignored
     * @param objectUnderTestField
     * @param instance
     * @throws ObjectUnderTestInstantiationException
     */
    public void process(final ObjectUnderTest objectUnderTestAnnotation, final Object instance)
            throws ObjectUnderTestInstantiationException {
        if (objectUnderTestAnnotation != null && objectUnderTestAnnotation.postConstruct()) {
            process(instance);
        }
    }

    /**
     * invokes @PostConstruct annotated method
     * 
     * @param instance
     * @throws ObjectUnderTestInstantiationException
     */
    private void process(final Object instance) throws ObjectUnderTestInstantiationException {

        final Set<Method> postConstructMethods = getPostConstructMethods(instance.getClass());

        for (final Method method : postConstructMethods) {
            try {
                ReflectionUtil.invokeMethod(method, instance);
            }
            catch (final Exception e) {
                throw new ObjectUnderTestInstantiationException("error executing postConstruction method '"
                        + method.getName() + "'", e);
            }

        }
    }

    /**
     * @param instance
     * @return all instance methods that are marked as postConstruction methods
     */
    Set<Method> getPostConstructMethods(final Class<?> type) {
        final Set<Method> postConstructMethods = new LinkedHashSet<Method>();

        for (final Class<? extends Annotation> postConstructAnnotation : postConstructAnnotations) {
            postConstructMethods.addAll(ReflectionUtil.getAllMethodsWithAnnotation(type, postConstructAnnotation));
        }
        return postConstructMethods;
    }
}
