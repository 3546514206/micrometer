/*
 * Copyright 2024 VMware, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micrometer.test.ltw;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.observation.DefaultMeterObservationHandler;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.Observations;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

import java.util.Collection;

import static org.assertj.core.api.BDDAssertions.then;

@DisabledForJreRange(min = JRE.JAVA_17,
        disabledReason = "See https://github.com/micrometer-metrics/micrometer/pull/5401#issuecomment-2308440259")
class MeasuredClassTest {

    MeterRegistry registry = new SimpleMeterRegistry();

    ObservationRegistry observationRegistry = ObservationRegistry.create();

    MeasuredClass measured = new MeasuredClass();

    @BeforeEach
    void setUp() {
        observationRegistry.observationConfig().observationHandler(new DefaultMeterObservationHandler(registry));
        // Global registry must be used because aspect gets created for us
        Metrics.addRegistry(registry);
        Observations.setRegistry(observationRegistry);
    }

    @AfterEach
    void cleanUp() {
        Metrics.removeRegistry(registry);
        Observations.resetRegistry();
    }

    @Test
    void shouldWrapMethodWithTimedAspectThroughLTW() {
        // when
        measured.timedMethod();
        // then
        Collection<Timer> timers = registry.find(TimedAspect.DEFAULT_METRIC_NAME)
            .tag("class", MeasuredClass.class.getName())
            .tag("method", "timedMethod")
            .timers();
        then(timers).hasSize(1);
        Timer timer = timers.iterator().next();
        then(timer.count()).isEqualTo(1);

        // when
        measured.timedMethod();
        // then
        then(timer.count()).isEqualTo(2);
    }

    @Test
    void shouldWrapMethodWithCountedAspectThroughLTW() {
        // when
        measured.countedMethod();
        // then
        Collection<Counter> counters = registry.find("method.counted")
            .tag("class", MeasuredClass.class.getName())
            .tag("method", "countedMethod")
            .counters();
        then(counters).hasSize(1);
        Counter counter = counters.iterator().next();
        then(counter.count()).isEqualTo(1);

        // when
        measured.countedMethod();
        // then
        then(counter.count()).isEqualTo(2);
    }

    @Test
    void shouldWrapMethodWithObservedAspectThroughLTW() {
        // when
        measured.observedMethod();
        // then
        Collection<Timer> timers = registry.find("method.observed")
            .tag("class", MeasuredClass.class.getName())
            .tag("method", "observedMethod")
            .timers();
        then(timers).hasSize(1);
        Timer timer = timers.iterator().next();
        then(timer.count()).isEqualTo(1);

        // when
        measured.observedMethod();
        // then
        then(timer.count()).isEqualTo(2);
    }

    @Test
    void shouldWrapMethodWithClassLevelTimedAspectThroughLTW() {
        // when
        measured.classLevelTimedMethod();
        // then
        Collection<Timer> timers = registry.find(TimedAspect.DEFAULT_METRIC_NAME)
            .tag("class", MeasuredClass.class.getName())
            .tag("method", "classLevelTimedMethod")
            .timers();
        then(timers).hasSize(1);
        Timer timer = timers.iterator().next();
        then(timer.count()).isEqualTo(1);

        // when
        measured.classLevelTimedMethod();
        // then
        then(timer.count()).isEqualTo(2);
    }

    @Test
    void shouldWrapMethodWithClassLevelCountedAspectThroughLTW() {
        // when
        measured.classLevelCountedMethod();
        // then
        Collection<Counter> counters = registry.find("method.counted")
            .tag("class", MeasuredClass.class.getName())
            .tag("method", "classLevelCountedMethod")
            .counters();
        then(counters).hasSize(1);
        Counter counter = counters.iterator().next();
        then(counter.count()).isEqualTo(1);

        // when
        measured.classLevelCountedMethod();
        // then
        then(counter.count()).isEqualTo(2);
    }

    @Test
    void shouldWrapMethodWithClassLevelObservedAspectThroughLTW() {
        // when
        measured.classLevelObservedMethod();
        // then
        Collection<Timer> timers = registry.find("method.observed")
            .tag("class", MeasuredClass.class.getName())
            .tag("method", "classLevelObservedMethod")
            .timers();
        then(timers).hasSize(1);
        Timer timer = timers.iterator().next();
        then(timer.count()).isEqualTo(1);

        // when
        measured.classLevelObservedMethod();
        // then
        then(timer.count()).isEqualTo(2);
    }

}
