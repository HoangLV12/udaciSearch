package com.udacity.webcrawler.profiler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A method interceptor that checks whether {@link Method}s are annotated with the {@link Profiled}
 * annotation. If they are, the method interceptor records how long the method invocation took.
 */
final class ProfilingMethodInterceptor implements InvocationHandler {

    private final Clock clock;
    private Object delegate;
    private ProfilingState state;
    private ZonedDateTime startTime;

    // TODO: You will need to add more instance fields and constructor arguments to this class.
    ProfilingMethodInterceptor(Clock clock) {
        this.clock = Objects.requireNonNull(clock);
    }

    ProfilingMethodInterceptor(Clock clock, Object delegate, ProfilingState state, ZonedDateTime startTime) {
        this.clock = Objects.requireNonNull(clock);
        this.delegate = delegate;
        this.state = state;
        this.startTime = startTime;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // TODO: This method interceptor should inspect the called method to see if it is a profiled
        //       method. For profiled methods, the interceptor should record the startTime time, then
        //       invoke the method using the object that is being profiled. Finally, for profiled
        //       methods, the interceptor should record how long the method call took, using the
        //       ProfilingState methods.
        Object invoked;
        Instant startTime = null;
        boolean isProfiled = method.getAnnotation(Profiled.class) != null;
        if (isProfiled) {
            startTime = clock.instant();
        }
        try {
            invoked = method.invoke(delegate, args);
        } catch (InvocationTargetException ex) {
            throw ex.getTargetException();
        } finally {
            if (isProfiled) {
                Duration duration = Duration.between(startTime, clock.instant());
                state.record(delegate.getClass(), method, duration);
            }
        }
        return invoked;
    }
}
