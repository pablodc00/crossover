package com.crossover.trial.properties.type;

import com.amazonaws.regions.Regions;
import net.balusc.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

/**
 * Predefined set of ValueCheckers/ValueTesters to run against a string property value
 *
 * @author alexey.zakharchenko@gmail.com
 */
public final class ClassFinders {
    public static final Logger log = LoggerFactory.getLogger(ClassFinders.class);

    // ORDERED collection of parsers. We should check ints prior to longs and doubles
    private static final Collection<ClassFinder> finders = new LinkedList<>();

    static {
        // Order matters!
        add(Integer::parseInt, Integer.class);
        add(Long::parseLong, Long.class);
        add(Double::parseDouble, Double.class);

        add(URL::new, URL.class);

        add(s -> "true".equalsIgnoreCase(s) || "false".equalsIgnoreCase(s), Boolean.class);
        add(DateUtil::parse, Date.class);

        //add(Locale::forLanguageTag, Locale.class);

        // Amazon
        add(s -> {
            for (Regions r : Regions.values())
                if (r.getName().equals(s))
                    return true;

            return false;
        }, Regions.class);

        add(Class::forName, Class.class);
        // Default
//        add(String::valueOf, String.class);

        // @todo Arrays? Lists? File? what else?
    }

    /**
     * @param value Property value (or any other string)
     *
     * @return Class type for the given value; if no specific class is found, returns String class
     */
    public static Class find(String value) {
        if (value == null)
            return null;

        for (ClassFinder p : finders) {
            Class clazz = p.check(value);
            if (clazz != null) {
                //log.debug("{} is of class {}", value, clazz);
                return clazz;
            }
        }

        return String.class;
    }

    private static void add(ValueTester tester, Class clazz) {
        finders.add(new ClassFinder(tester, clazz));
    }

    private static void add(ValueChecker checker, Class clazz) {
        finders.add(new ClassFinder(checker, clazz));
    }

    private static class ClassFinder {
        private ValueChecker tester;
        private Class clazz;

        public ClassFinder(ValueChecker tester, Class clazz) {
            this.tester = tester;
            this.clazz = clazz;
        }

        public Class check(String value) {
            if (value == null)
                return null;

            try {
                if (tester.accepts(value.trim())){
                    return clazz;
                }
            } catch (Throwable ignored) {}

            return null;
        }
    }

}