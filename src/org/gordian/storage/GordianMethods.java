package org.gordian.storage;

import api.gordian.Class;
import api.gordian.Object;
import api.gordian.Signature;
import api.gordian.methods.Method;
import api.gordian.methods.ValueReturned;
import api.gordian.storage.InternalNotFoundException;
import api.gordian.storage.Methods;
import java.util.Random;
import org.gordian.GordianClass;
import org.gordian.method.GordianMethod;
import org.gordian.value.GordianBoolean;
import org.gordian.value.GordianNumber;
import org.gordian.value.GordianString;

/**
 *
 * @author Joel Gallant <joelgallant236@gmail.com>
 */
public class GordianMethods implements Methods {

    private final GordianStorage storage = new GordianStorage();

    {
        final Random RANDOM = new Random();
        storage.reserve("return", new GordianMethod(
                new Signature(
                        new Class[]{GordianClass.ALL_CLASSES}
                )) {
                    public Object run(Object[] args) {
                        throw new ValueReturned(args[0]);
                    }
                }
        );
        storage.reserve("delete", new GordianMethod(
                new Signature(
                        new Class[]{GordianString.CLASS}
                )) {
                    public Object run(Object[] args) {
                        try {
                            remove(((GordianString) args[0]).getValue());
                        } catch (InternalNotFoundException ex) {
                            ex.printStackTrace();
                        }
                        return null;
                    }
                });
        storage.reserve("int", new GordianMethod(
                new Signature(
                        new Class[]{GordianNumber.CLASS}
                )) {
                    public Object run(Object[] args) {
                        return new GordianNumber(((GordianNumber) args[0]).getInt());
                    }
                });
        storage.reserve("num", new GordianMethod(
                new Signature(
                        new Class[]{GordianClass.ALL_CLASSES}
                )) {
                    public Object run(Object[] args) {
                        return new GordianNumber(Double.parseDouble(args[0].toString()));
                    }
                });
        storage.reserve("bool", new GordianMethod(
                new Signature(
                        new Class[]{GordianClass.ALL_CLASSES}
                )) {
                    public Object run(Object[] args) {
                        return new GordianBoolean(Boolean.parseBoolean(args[0].toString()));
                    }
                });
        storage.reserve("str", new GordianMethod(
                new Signature(
                        new Class[]{GordianClass.ALL_CLASSES}
                )) {
                    public Object run(Object[] args) {
                        return new GordianString(args[0].toString());
                    }
                });
        storage.reserve("print", new GordianMethod(
                new Signature(
                        new Class[]{GordianClass.ALL_CLASSES}
                )) {
                    public Object run(Object[] args) {
                        System.out.println(args[0].toString());
                        return null;
                    }
                });
        storage.reserve("sleep", new GordianMethod(
                new Signature(
                        new Class[]{GordianNumber.CLASS}
                )) {
                    public Object run(Object[] args) {
                        try {
                            Thread.sleep(((GordianNumber) args[0]).getLong());
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        return null;
                    }
                });
        storage.reserve("rand", new GordianMethod(
                new Signature(
                        new Class[]{GordianNumber.CLASS}
                )) {
                    public Object run(Object[] args) {
                        return new GordianNumber(RANDOM.nextDouble());
                    }
                });
        storage.reserve("randInt", new GordianMethod(
                new Signature(
                        new Class[]{GordianNumber.CLASS}
                )) {
                    public Object run(Object[] args) {
                        return new GordianNumber(RANDOM.nextInt());
                    }
                });
    }

    public Method get(String name) throws InternalNotFoundException {
        return (Method) storage.get(name);
    }

    public void put(String name, Method method) {
        storage.put(name, method);
    }

    public void set(String name, Method method) {
        storage.set(name, method);
    }

    public void remove(String name) throws InternalNotFoundException {
        storage.remove(name);
    }

    public void removeAll(String name) {
        storage.removeAll(name);
    }

    public boolean contains(String name) {
        return storage.contains(name);
    }
}
