//Generated by the protocol buffer compiler. DO NOT EDIT!
// source: mc/jabber/proto/circuitBoardBuffer.proto

package mc.jabber.proto;

@kotlin.jvm.JvmSynthetic
inline fun circuitBoard(block: mc.jabber.proto.CircuitBoardKt.Dsl.() -> Unit): mc.jabber.proto.CircuitBoardBuffer.CircuitBoard =
  mc.jabber.proto.CircuitBoardKt.Dsl._create(mc.jabber.proto.CircuitBoardBuffer.CircuitBoard.newBuilder()).apply { block() }._build()
object CircuitBoardKt {
  @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
  @com.google.protobuf.kotlin.ProtoDslMarker
  class Dsl private constructor(
    @kotlin.jvm.JvmField private val _builder: mc.jabber.proto.CircuitBoardBuffer.CircuitBoard.Builder
  ) {
    companion object {
      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _create(builder: mc.jabber.proto.CircuitBoardBuffer.CircuitBoard.Builder): Dsl = Dsl(builder)
    }

    @kotlin.jvm.JvmSynthetic
    @kotlin.PublishedApi
    internal fun _build(): mc.jabber.proto.CircuitBoardBuffer.CircuitBoard = _builder.build()

    /**
     * <code>int32 sizeX = 1;</code>
     */
    var sizeX: kotlin.Int
      @JvmName("getSizeX")
      get() = _builder.getSizeX()
      @JvmName("setSizeX")
      set(value) {
        _builder.setSizeX(value)
      }
    /**
     * <code>int32 sizeX = 1;</code>
     */
    fun clearSizeX() {
      _builder.clearSizeX()
    }

    /**
     * <code>int32 sizeY = 2;</code>
     */
    var sizeY: kotlin.Int
      @JvmName("getSizeY")
      get() = _builder.getSizeY()
      @JvmName("setSizeY")
      set(value) {
        _builder.setSizeY(value)
      }
    /**
     * <code>int32 sizeY = 2;</code>
     */
    fun clearSizeY() {
      _builder.clearSizeY()
    }

    /**
     * An uninstantiable, behaviorless type to represent the field in
     * generics.
     */
    @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
    class EntriesProxy private constructor() : com.google.protobuf.kotlin.DslProxy()
    /**
     * <code>map&lt;int32, string&gt; entries = 3;</code>
     */
     val entries: com.google.protobuf.kotlin.DslMap<kotlin.Int, kotlin.String, EntriesProxy>
      @kotlin.jvm.JvmSynthetic
      @JvmName("getEntriesMap")
      get() = com.google.protobuf.kotlin.DslMap(
        _builder.getEntriesMap()
      )
    /**
     * <code>map&lt;int32, string&gt; entries = 3;</code>
     */
    @JvmName("putEntries")
    fun com.google.protobuf.kotlin.DslMap<kotlin.Int, kotlin.String, EntriesProxy>
      .put(key: kotlin.Int, value: kotlin.String) {
         _builder.putEntries(key, value)
       }
    /**
     * <code>map&lt;int32, string&gt; entries = 3;</code>
     */
    @kotlin.jvm.JvmSynthetic
    @JvmName("setEntries")
    inline operator fun com.google.protobuf.kotlin.DslMap<kotlin.Int, kotlin.String, EntriesProxy>
      .set(key: kotlin.Int, value: kotlin.String) {
         put(key, value)
       }
    /**
     * <code>map&lt;int32, string&gt; entries = 3;</code>
     */
    @kotlin.jvm.JvmSynthetic
    @JvmName("removeEntries")
    fun com.google.protobuf.kotlin.DslMap<kotlin.Int, kotlin.String, EntriesProxy>
      .remove(key: kotlin.Int) {
         _builder.removeEntries(key)
       }
    /**
     * <code>map&lt;int32, string&gt; entries = 3;</code>
     */
    @kotlin.jvm.JvmSynthetic
    @JvmName("putAllEntries")
    fun com.google.protobuf.kotlin.DslMap<kotlin.Int, kotlin.String, EntriesProxy>
      .putAll(map: kotlin.collections.Map<kotlin.Int, kotlin.String>) {
         _builder.putAllEntries(map)
       }
    /**
     * <code>map&lt;int32, string&gt; entries = 3;</code>
     */
    @kotlin.jvm.JvmSynthetic
    @JvmName("clearEntries")
    fun com.google.protobuf.kotlin.DslMap<kotlin.Int, kotlin.String, EntriesProxy>
      .clear() {
         _builder.clearEntries()
       }
  }
}
@kotlin.jvm.JvmSynthetic
inline fun mc.jabber.proto.CircuitBoardBuffer.CircuitBoard.copy(block: mc.jabber.proto.CircuitBoardKt.Dsl.() -> Unit): mc.jabber.proto.CircuitBoardBuffer.CircuitBoard =
  mc.jabber.proto.CircuitBoardKt.Dsl._create(this.toBuilder()).apply { block() }._build()