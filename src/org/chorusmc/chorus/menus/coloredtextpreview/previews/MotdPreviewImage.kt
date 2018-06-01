package org.chorusmc.chorus.menus.coloredtextpreview.previews

import org.chorusmc.chorus.Chorus
import org.chorusmc.chorus.minecraft.chat.ChatParser
import org.chorusmc.chorus.util.toFlowList
import org.chorusmc.chorus.util.withStyleClass
import javafx.scene.image.Image
import javafx.scene.text.TextFlow

/**
 * @author Gio
 */
class MotdPreviewImage(title: String, first: String) : ColoredTextPreviewImage(
        ColoredTextBackground(Image(Chorus::class.java.getResourceAsStream("/assets/minecraft/previews/motd-background.png"))),
        listOf(
                ChatParser(title, true).toTextFlow().withStyleClass("minecraft-motd-preview-flow"),
                ChatParser(first, true).toTextFlow().withStyleClass("minecraft-motd-preview-flow"),
                TextFlow().withStyleClass("minecraft-motd-preview-flow")
        ).toFlowList()
) {

    override fun initFlow(flow: TextFlow, index: Int) {
        styleClass += "minecraft-motd-preview-flow"
        flow.layoutX = 65.0
        flow.layoutY = 5.0 + (index * (50 / 3))
    }
}