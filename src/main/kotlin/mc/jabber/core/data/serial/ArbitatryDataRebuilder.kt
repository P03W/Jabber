package mc.jabber.core.data.serial

import com.google.common.io.ByteStreams
import com.google.protobuf.ByteString
import mc.jabber.util.error.InvalidDataFormatException
import mc.jabber.util.error.UnknownDataFormatException
import net.minecraft.nbt.NbtIo

@Suppress("UnstableApiUsage")
fun rebuildArbitraryData(bytes: List<Byte>): NbtTransformable<*> {
    val mut = bytes.toMutableList()
    val id = mut.removeFirst().toInt()
    val tag = NbtIo.read(ByteStreams.newDataInput(mut.toByteArray()))
    return when (id) {
        0 -> throw InvalidDataFormatException("$id is not a valid data format, as that format is reserved for formats that cannot be serialized dependently")
        1 -> LongBox(tag.getLong("l"))
        else -> throw UnknownDataFormatException("Found an unknown stored data format with ID $id, which is not a known decode-able format")
    }
}

@Suppress("UnstableApiUsage")
fun rebuildArbitraryData(bytes: ByteString): NbtTransformable<*> {
    val mut = bytes.toMutableList()
    val id = mut.removeFirst().toInt()
    val tag = NbtIo.read(ByteStreams.newDataInput(mut.toByteArray()))
    return when (id) {
        0 -> throw InvalidDataFormatException("$id is not a valid data format, as that format is reserved for formats that cannot be serialized dependently")
        1 -> LongBox(tag.getLong("l"))
        else -> throw UnknownDataFormatException("Found an unknown stored data format with ID $id, which is not a known decode-able format")
    }
}

@Suppress("UnstableApiUsage")
fun rebuildArbitraryData(id: Int, bytes: List<Byte>): NbtTransformable<*> {
    val tag = NbtIo.read(ByteStreams.newDataInput(bytes.toByteArray()))
    return when (id) {
        0 -> throw InvalidDataFormatException("$id is not a valid data format, as that format is reserved for formats that cannot be serialized dependently")
        1 -> LongBox(tag.getLong("l"))
        else -> throw UnknownDataFormatException("Found an unknown stored data format with ID $id, which is not a known decode-able format")
    }
}

@Suppress("UnstableApiUsage")
fun rebuildArbitraryData(id: Int, bytes: ByteString): NbtTransformable<*> {
    val tag = NbtIo.read(ByteStreams.newDataInput(bytes.toByteArray()))
    return when (id) {
        0 -> throw InvalidDataFormatException("$id is not a valid data format, as that format is reserved for formats that cannot be serialized dependently")
        1 -> LongBox(tag.getLong("l"))
        else -> throw UnknownDataFormatException("Found an unknown stored data format with ID $id, which is not a known decode-able format")
    }
}
