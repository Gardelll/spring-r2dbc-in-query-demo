# R2dbc with mysql driver cannot process "in" query with multiple same parameter.

# Steps

Create a table named `foo`:

```mysql
CREATE TABLE `foo`
(
    `id`       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `test_key` VARCHAR(100) NOT NULL,
    `name`     VARCHAR(100) NOT NULL
);
```

Create a query with this SQL:

```mysql
SELECT `id`, `test_key`, `name`
FROM `foo`
WHERE (`test_key` = :key AND `name` IN (:names))
   OR (`test_key` IN (:names) AND `name` = :key)
```

# Stacktrace

```txt
java.lang.IllegalStateException: Parameter 3 has no binding
	at dev.miku.r2dbc.mysql.ParametrizedStatementSupport$Bindings.validatedFinish(ParametrizedStatementSupport.java:187)
	at dev.miku.r2dbc.mysql.ParametrizedStatementSupport$Bindings.access$100(ParametrizedStatementSupport.java:149)
	at dev.miku.r2dbc.mysql.ParametrizedStatementSupport.execute(ParametrizedStatementSupport.java:109)
	at dev.miku.r2dbc.mysql.ParametrizedStatementSupport.execute(ParametrizedStatementSupport.java:39)
	at org.springframework.r2dbc.core.StatementFilterFunction.lambda$static$0(StatementFilterFunction.java:45)
	at org.springframework.r2dbc.core.DefaultDatabaseClient$DefaultGenericExecuteSpec.lambda$execute$3(DefaultDatabaseClient.java:375)
	at org.springframework.r2dbc.core.ConnectionFunction.apply(ConnectionFunction.java:46)
	at org.springframework.r2dbc.core.ConnectionFunction.apply(ConnectionFunction.java:31)
	at org.springframework.r2dbc.core.DefaultFetchSpec.lambda$all$2(DefaultFetchSpec.java:88)
	at org.springframework.r2dbc.core.ConnectionFunction.apply(ConnectionFunction.java:46)
	at org.springframework.r2dbc.core.ConnectionFunction.apply(ConnectionFunction.java:31)
	at org.springframework.r2dbc.core.DefaultDatabaseClient.lambda$inConnectionMany$6(DefaultDatabaseClient.java:138)
	at reactor.core.publisher.FluxUsingWhen.deriveFluxFromResource(FluxUsingWhen.java:120)
	at reactor.core.publisher.FluxUsingWhen.access$000(FluxUsingWhen.java:54)
	at reactor.core.publisher.FluxUsingWhen$ResourceSubscriber.onNext(FluxUsingWhen.java:193)
	at reactor.core.publisher.FluxMap$MapSubscriber.onNext(FluxMap.java:122)
	at reactor.core.publisher.FluxOnErrorResume$ResumeSubscriber.onNext(FluxOnErrorResume.java:79)
	at reactor.core.publisher.FluxOnErrorResume$ResumeSubscriber.onNext(FluxOnErrorResume.java:79)
	at reactor.core.publisher.FluxRetry$RetrySubscriber.onNext(FluxRetry.java:87)
	at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1816)
	at reactor.core.publisher.MonoFlatMap$FlatMapInner.onNext(MonoFlatMap.java:249)
	at io.r2dbc.pool.MonoDiscardOnCancel$MonoDiscardOnCancelSubscriber.onNext(MonoDiscardOnCancel.java:92)
	at reactor.core.publisher.FluxOnErrorResume$ResumeSubscriber.onNext(FluxOnErrorResume.java:79)
	at reactor.core.publisher.MonoIgnoreThen$ThenIgnoreMain.complete(MonoIgnoreThen.java:292)
	at reactor.core.publisher.MonoIgnoreThen$ThenIgnoreMain.onNext(MonoIgnoreThen.java:187)
	at reactor.core.publisher.MonoIgnoreThen$ThenIgnoreMain.subscribeNext(MonoIgnoreThen.java:236)
	at reactor.core.publisher.MonoIgnoreThen$ThenIgnoreMain.onComplete(MonoIgnoreThen.java:203)
	at reactor.core.publisher.MonoIgnoreElements$IgnoreElementsSubscriber.onComplete(MonoIgnoreElements.java:89)
	at reactor.core.publisher.FluxHandleFuseable$HandleFuseableSubscriber.onComplete(FluxHandleFuseable.java:236)
	at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1817)
	at reactor.core.publisher.MonoSupplier.subscribe(MonoSupplier.java:62)
	at reactor.core.publisher.Mono.subscribe(Mono.java:4397)
	at reactor.core.publisher.MonoIgnoreThen$ThenIgnoreMain.subscribeNext(MonoIgnoreThen.java:263)
	at reactor.core.publisher.MonoIgnoreThen.subscribe(MonoIgnoreThen.java:51)
	at reactor.core.publisher.InternalMonoOperator.subscribe(InternalMonoOperator.java:64)
	at io.r2dbc.pool.MonoDiscardOnCancel.subscribe(MonoDiscardOnCancel.java:50)
	at reactor.core.publisher.MonoFlatMap$FlatMapMain.onNext(MonoFlatMap.java:157)
	at reactor.pool.AbstractPool$Borrower.deliver(AbstractPool.java:467)
	at reactor.pool.SimpleDequePool.lambda$drainLoop$9(SimpleDequePool.java:423)
	at reactor.core.publisher.FluxDoOnEach$DoOnEachSubscriber.onNext(FluxDoOnEach.java:154)
	at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1816)
	at reactor.core.publisher.MonoFlatMap$FlatMapInner.onNext(MonoFlatMap.java:249)
	at reactor.core.publisher.FluxMapFuseable$MapFuseableSubscriber.onNext(FluxMapFuseable.java:129)
	at reactor.core.publisher.Operators$MonoSubscriber.complete(Operators.java:1816)
	at reactor.core.publisher.MonoTakeLastOne$TakeLastOneSubscriber.onComplete(MonoTakeLastOne.java:120)
	at reactor.core.publisher.FluxFlatMap$FlatMapMain.checkTerminated(FluxFlatMap.java:846)
	at reactor.core.publisher.FluxFlatMap$FlatMapMain.drainLoop(FluxFlatMap.java:608)
	at reactor.core.publisher.FluxFlatMap$FlatMapMain.drain(FluxFlatMap.java:588)
	at reactor.core.publisher.FluxFlatMap$FlatMapMain.onComplete(FluxFlatMap.java:465)
	at reactor.core.publisher.FluxMap$MapSubscriber.onComplete(FluxMap.java:144)
	at reactor.core.publisher.FluxWindowPredicate$WindowPredicateMain.checkTerminated(FluxWindowPredicate.java:538)
	at reactor.core.publisher.FluxWindowPredicate$WindowPredicateMain.drainLoop(FluxWindowPredicate.java:486)
	at reactor.core.publisher.FluxWindowPredicate$WindowPredicateMain.drain(FluxWindowPredicate.java:430)
	at reactor.core.publisher.FluxWindowPredicate$WindowPredicateMain.onComplete(FluxWindowPredicate.java:310)
	at reactor.core.publisher.FluxHandleFuseable$HandleFuseableSubscriber.onComplete(FluxHandleFuseable.java:236)
	at reactor.core.publisher.FluxContextWrite$ContextWriteSubscriber.onComplete(FluxContextWrite.java:126)
	at dev.miku.r2dbc.mysql.util.DiscardOnCancelSubscriber.onComplete(DiscardOnCancelSubscriber.java:104)
	at reactor.core.publisher.FluxPeekFuseable$PeekConditionalSubscriber.onComplete(FluxPeekFuseable.java:940)
	at reactor.core.publisher.MonoFlatMapMany$FlatMapManyInner.onComplete(MonoFlatMapMany.java:260)
	at reactor.core.publisher.FluxPeek$PeekSubscriber.onComplete(FluxPeek.java:260)
	at reactor.core.publisher.FluxHandle$HandleSubscriber.onComplete(FluxHandle.java:220)
	at reactor.core.publisher.FluxHandle$HandleSubscriber.onNext(FluxHandle.java:141)
	at reactor.core.publisher.FluxPeekFuseable$PeekConditionalSubscriber.onNext(FluxPeekFuseable.java:854)
	at reactor.core.publisher.EmitterProcessor.drain(EmitterProcessor.java:537)
	at reactor.core.publisher.EmitterProcessor.tryEmitNext(EmitterProcessor.java:343)
	at reactor.core.publisher.InternalManySink.emitNext(InternalManySink.java:27)
	at reactor.core.publisher.EmitterProcessor.onNext(EmitterProcessor.java:309)
	at dev.miku.r2dbc.mysql.client.ReactorNettyClient$ResponseSink.next(ReactorNettyClient.java:340)
	at dev.miku.r2dbc.mysql.client.ReactorNettyClient.lambda$new$0(ReactorNettyClient.java:103)
	at reactor.core.publisher.FluxPeek$PeekSubscriber.onNext(FluxPeek.java:185)
	at reactor.netty.channel.FluxReceive.drainReceiver(FluxReceive.java:279)
	at reactor.netty.channel.FluxReceive.onInboundNext(FluxReceive.java:388)
	at reactor.netty.channel.ChannelOperations.onInboundNext(ChannelOperations.java:404)
	at reactor.netty.channel.ChannelOperationsHandler.channelRead(ChannelOperationsHandler.java:93)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357)
	at dev.miku.r2dbc.mysql.client.MessageDuplexCodec.handleDecoded(MessageDuplexCodec.java:187)
	at dev.miku.r2dbc.mysql.client.MessageDuplexCodec.channelRead(MessageDuplexCodec.java:95)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357)
	at io.netty.handler.codec.ByteToMessageDecoder.fireChannelRead(ByteToMessageDecoder.java:327)
	at io.netty.handler.codec.ByteToMessageDecoder.channelRead(ByteToMessageDecoder.java:299)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357)
	at io.netty.channel.DefaultChannelPipeline$HeadContext.channelRead(DefaultChannelPipeline.java:1410)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
	at io.netty.channel.DefaultChannelPipeline.fireChannelRead(DefaultChannelPipeline.java:919)
	at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:166)
	at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:722)
	at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:658)
	at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:584)
	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:496)
	at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:997)
	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
	at java.base/java.lang.Thread.run(Thread.java:833)
```

# Example Project

Run this project.

`mvnw test`

> Do not forget change mysql credential in `src/test/java/com/example/r2dbctest/FooRepoTest.java`

# Related code

In method:
`org.springframework.data.r2dbc.core.NamedParameterUtils.ExpandedQuery#bind(org.springframework.r2dbc.core.binding.BindTarget, java.lang.String, java.lang.Object)`

When the size of `bindMarkers` is larger than the parameter collection (which can be caused by two parameters with the
same name), the remaining `bindMarkers` placeholders will not be bound.
