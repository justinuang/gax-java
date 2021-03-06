/*
 * Copyright 2018 Google LLC
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *     * Neither the name of Google LLC nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.google.api.gax.rpc;

import static com.google.common.truth.Truth.assertThat;

import com.google.api.core.ApiClock;
import java.util.concurrent.ScheduledExecutorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.threeten.bp.Duration;

@RunWith(JUnit4.class)
public class FixedWatchdogProviderTest {
  @Test
  public void testNull() {
    WatchdogProvider provider = FixedWatchdogProvider.create(null);
    assertThat(provider.getWatchdog()).isNull();
  }

  @Test
  public void testSameInstance() {
    Watchdog watchdog = Mockito.mock(Watchdog.class);
    WatchdogProvider provider = FixedWatchdogProvider.create(watchdog);
    assertThat(provider.getWatchdog()).isSameInstanceAs(watchdog);
  }

  @Test
  public void testNoModifications() {
    WatchdogProvider provider = FixedWatchdogProvider.create(Mockito.mock(Watchdog.class));

    assertThat(provider.needsCheckInterval()).isFalse();
    assertThat(provider.needsClock()).isFalse();
    assertThat(provider.needsExecutor()).isFalse();
    assertThat(provider.shouldAutoClose()).isFalse();

    Throwable actualError = null;
    try {
      provider.withCheckInterval(Duration.ofSeconds(10));
    } catch (Throwable t) {
      actualError = t;
    }
    assertThat(actualError).isInstanceOf(UnsupportedOperationException.class);

    actualError = null;
    try {
      provider.withClock(Mockito.mock(ApiClock.class));
    } catch (Throwable t) {
      actualError = t;
    }
    assertThat(actualError).isInstanceOf(UnsupportedOperationException.class);

    actualError = null;
    try {
      provider.withExecutor(Mockito.mock(ScheduledExecutorService.class));
    } catch (Throwable t) {
      actualError = t;
    }
    assertThat(actualError).isInstanceOf(UnsupportedOperationException.class);
  }
}
