package de.akquinet.jbosscc.needle.injection;

import de.akquinet.jbosscc.needle.NeedleTestcase;

/**
 * Provides an instances of {@code T} and verifies an injection target.
 * 
 * @param <T>
 *            - The type of the provided object.
 * 
 * 
 *            <pre>
 * Example for javax.inject.Qualifier:
 * 
 * public class InjectionProvider<User>() {
 * 	&#064;Override
 * 	public boolean verify(final InjectionTargetInformation information) {
 * 	 return information.getAnnotation(CurrentUser.class) != null;
 * 	}
 * 
 * 	&#064;Override
 * 	public Object getKey(final InjectionTargetInformation information) {
 * 	 return CurrentUser.class;
 * 	}
 * 
 * 	&#064;Override
 * 	public User getInjectedObject(final Class<?> type) {
 * 	 return new User();
 * 	}
 * }
 * 
 * </pre>
 */
public interface InjectionProvider<T> extends InjectionVerifier {

	/**
	 * Provides an instance of {@code T}.
	 * 
	 * @param injectionPointType
	 *            the type of the injection target.
	 * @return <T> instance of {@code T}
	 */
	T getInjectedObject(Class<?> injectionPointType);

	/**
	 * Returns a key object, which identifies the provided object.
	 * 
	 * @param injectionTargetInformation
	 *            information about the injection point
	 * @return the key of the provided object
	 * 
	 * @see NeedleTestcase#getInjectedObject(Object)
	 */
	Object getKey(InjectionTargetInformation injectionTargetInformation);
}
