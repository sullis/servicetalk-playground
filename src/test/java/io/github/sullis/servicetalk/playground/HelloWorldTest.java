package io.github.sullis.servicetalk.playground;

import io.github.nettyplus.leakdetector.junit.NettyLeakDetectorExtension;
import io.netty.channel.epoll.Epoll;
import io.servicetalk.http.api.HttpClient;
import io.servicetalk.http.api.HttpServerContext;
import io.servicetalk.http.netty.HttpClients;
import io.servicetalk.http.netty.HttpServers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.servicetalk.concurrent.api.Single.succeeded;
import static io.servicetalk.http.api.HttpSerializers.textSerializerUtf8;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(NettyLeakDetectorExtension.class)
public class HelloWorldTest {
  @Test
  @EnabledOnOs(value = OS.LINUX)
  void epollIsAvailableOnLinux() throws Throwable {
    assertThat(Epoll.isAvailable()).isTrue();
  }

  @Test
  void hello() throws Exception {
    HttpServerContext server = HttpServers.forPort(8080)
        .listenAndAwait((ctx, request, responseFactory) ->
            succeeded(responseFactory.ok()
                .payloadBody("Hello World!", textSerializerUtf8())));

    try (HttpClient client = HttpClients.forSingleAddress("localhost", 8080).build()) {
      client.request(client.get("/sayHello"))
          .whenOnSuccess(resp -> {
            System.out.println(resp.toString((name, value) -> value));
            System.out.println(resp.payloadBody(textSerializerUtf8()));
          })
          // This example is demonstrating asynchronous execution, but needs to prevent the main thread from exiting
          // before the response has been processed. This isn't typical usage for an asynchronous API but is useful
          // for demonstration purposes.
          .toFuture().get();
    }

    server.closeGracefully();
  }
}
