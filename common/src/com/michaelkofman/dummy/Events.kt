package com.michaelkofman.dummy

import java.io.Serializable
import org.axonframework.serialization.Revision

@Revision("1.0")
class DummyEvent(val id: String, val value: String): Serializable