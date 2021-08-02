package mc.jabber.core.data.cardinal

import mc.jabber.core.data.serial.LongBox

class ComputeData(up: LongBox?, down: LongBox?, left: LongBox?, right: LongBox?) :
    CardinalData<LongBox>(up, down, left, right) { override val typeByte: Int = 1 }
