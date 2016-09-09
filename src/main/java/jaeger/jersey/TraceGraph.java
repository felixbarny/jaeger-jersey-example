package jaeger.jersey;

import com.uber.jaeger.filters.jaxrs2.ClientFilter;
import com.uber.jaeger.filters.jaxrs2.ServerFilter;
import com.uber.jaeger.metrics.Metrics;
import com.uber.jaeger.metrics.NullStatsReporter;
import com.uber.jaeger.metrics.StatsFactoryImpl;
import com.uber.jaeger.propagation.b3.B3TextMapCodec;
import com.uber.jaeger.reporters.RemoteReporter;
import com.uber.jaeger.samplers.ConstSampler;
import com.uber.jaeger.samplers.Sampler;
import com.uber.jaeger.senders.zipkin.ZipkinSender;
import io.opentracing.Tracer;
import io.opentracing.propagation.Format;

import static com.uber.jaeger.context.TracingUtils.getTraceContext;

final class TraceGraph {
  private final Tracer tracer;

  TraceGraph(String serviceName) {
    StatsFactoryImpl statsFactory = new StatsFactoryImpl(new NullStatsReporter());
    RemoteReporter reporter = new RemoteReporter(
        ZipkinSender.create("http://localhost:9411/api/v1/spans"),
        1000 /* flushInterval */,
        1000 /* maxQueueSize */,
        new Metrics(statsFactory)
    );
    B3TextMapCodec b3Codec = new B3TextMapCodec();
    Sampler sampler = new ConstSampler(true);
    this.tracer = new com.uber.jaeger.Tracer.Builder(serviceName, reporter, sampler)
        .registerInjector(Format.Builtin.HTTP_HEADERS, b3Codec)
        .registerExtractor(Format.Builtin.HTTP_HEADERS, b3Codec)
        .build();
  }

  ClientFilter clientFilter() {
    return new ClientFilter(tracer, getTraceContext());
  }

  ServerFilter serverFilter() {
    return new ServerFilter(tracer, getTraceContext());
  }
}