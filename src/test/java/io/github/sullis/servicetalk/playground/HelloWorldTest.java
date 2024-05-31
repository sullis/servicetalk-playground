package io.github.sullis.servicetalk.playground;

import io.netty.channel.epoll.Epoll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static org.assertj.core.api.Assertions.assertThat;


public class HelloWorldTest {
  @Test
  @EnabledOnOs(value = OS.LINUX)
  void epollIsAvailableOnLinux() throws Throwable {
    assertThat(Epoll.isAvailable()).isTrue();
  }

  @Test
  void hello() {
    // TODO
  }
}
