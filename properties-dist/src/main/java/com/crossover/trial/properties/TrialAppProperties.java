package com.crossover.trial.properties;

import com.crossover.trial.properties.type.ClassFinders;
import com.crossover.trial.properties.util.Utils;

import java.util.*;

import static com.crossover.trial.properties.util.Utils.normalizeKey;

/**
 * A dummy implementation of TrialAppProperties, this clearly doesn't work. Candidates SHOULD change 
 * this class to add their implementation. You are also free to create additional classes
 *
 * note: a default constructor is required.
 *
 * @author code test administrator
 */
public class TrialAppProperties implements AppProperties {

    private final Map<PropertyWrapper, TypedProperty> properties = new HashMap<>();
    private final List<String> missing = new LinkedList<>();

    public TrialAppProperties() {}

    @Override
    public List<String> getMissingProperties() {
        return Collections.unmodifiableList(missing);
    }

    @Override
    public List<String> getKnownProperties() {
        final List<String> names = new ArrayList<>(properties.size());
        properties.keySet().forEach(p -> names.add(p.name));

        return names;
    }

    @Override
    public boolean isValid() {
        return missing.isEmpty();
    }

    @Override
    public void clear() {
        properties.clear();
        missing.clear();
    }

    @Override
    public Object get(String key) {
        return properties.getOrDefault(new PropertyWrapper(key), TypedProperty.NULL);
    }

    public String getStringValue(String key) {
        return properties.getOrDefault(new PropertyWrapper(key), TypedProperty.NULL).value;
    }


    /**
     * Sets the value of the given property.
     *
     * @param name Property name
     * @param value Property value. If empty, property is referred to as missing
     * @param clazz Value class
     */
    public void setProperty(String name, String value, Class clazz) {
        if (Utils.isVoid(value) || Utils.isVoid(name))
            missing.add(name);

        properties.put(
                new PropertyWrapper(name),
                new TypedProperty(value, clazz)
        );
    }

    public void setProperty(String name, String value) {
        setProperty(name, value, ClassFinders.find(value));
    }

    public void setProperties(Properties props) {
        for (String name : props.stringPropertyNames())
            setProperty(name, props.getProperty(name));
    }

    // Incapsulates value + class
    private static final class TypedProperty {
        private static final TypedProperty NULL = new TypedProperty(null, null);

        private final String value;
        private final Class clazz;

        public TypedProperty(String value, Class clazz) {
            this.value = value;
            this.clazz = clazz;
        }

        @Override
        public String toString() {
            if (value == null) return "null";

            return clazz.getName() + ", " + value;
        }
    }

    // Provides property equality for upper/lower cases and '_' and '.' interoperability
    static class PropertyWrapper {
        private final String name;

        public PropertyWrapper(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PropertyWrapper that = (PropertyWrapper) o;

            return name != null ? normalizeKey(name).equals(normalizeKey(that.name)) : that.name == null;

        }

        @Override
        public int hashCode() {
            return name != null ? normalizeKey(name).hashCode() : 0;
        }
    }
}

