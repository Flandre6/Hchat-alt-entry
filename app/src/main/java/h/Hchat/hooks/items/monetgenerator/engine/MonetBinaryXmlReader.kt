/*
 * Adapted from Flandre6/WeKit commit bdc7f18033d87a2f6307d2caedfdc6502986a401.
 * Licensed under GPL-3.0; see docs/third_party/monet-engine-module-generator-GPL-3.0.txt.
 */
package h.Hchat.hooks.items.monetgenerator.engine

import com.reandroid.arsc.chunk.xml.ResXmlDocument
import com.reandroid.arsc.chunk.xml.ResXmlElement
import com.reandroid.arsc.chunk.xml.ResXmlTextNode

data class MonetBinaryXml(
    val root: MonetXmlElement,
)

object MonetBinaryXmlReader {
    fun read(document: ResXmlDocument): MonetBinaryXml {
        val referenceIds = linkedSetOf<Int>()
        val root = document.elements.asSequence().firstOrNull()
            ?: error("binary XML document has no root element")
        return MonetBinaryXml(root.toMonetElement(referenceIds))
    }

    private fun ResXmlElement.toMonetElement(referenceIds: MutableSet<Int>): MonetXmlElement =
        MonetXmlElement(
            name = name,
            namespace = uri,
            attributes = attributes.asSequence().map { attribute ->
                val valueType = requireNotNull(attribute.valueType) {
                    "binary XML attribute ${attribute.name} has no value type"
                }
                val value = if (valueType.isReference) {
                    MonetResourceValue.Reference(attribute.data, valueType.name).also {
                        referenceIds += attribute.data
                    }
                } else {
                    MonetResourceValue.Literal(
                        valueType = valueType.name,
                        data = Integer.toUnsignedLong(attribute.data),
                    )
                }
                MonetXmlAttribute(
                    namespace = attribute.uri,
                    name = attribute.name,
                    nameId = attribute.nameId.takeIf { it != 0 },
                    valueType = valueType.name,
                    value = value,
                )
            }.toList(),
            children = iterator().asSequence().mapNotNull { child ->
                when (child) {
                    is ResXmlElement -> child.toMonetElement(referenceIds)
                    is ResXmlTextNode -> null
                    else -> error("unsupported binary XML node ${child.javaClass.name}")
                }
            }.toList(),
        )
}
