package eu.iamgio.chorus.menus

import eu.iamgio.chorus.Chorus
import javafx.application.Platform
import javafx.css.PseudoClass
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.VBox


const val HOVER_STYLE_CLASS = "bvhover"

/**
 * @author Gio
 */
open class BrowsableVBox(textfield: TextField? = null) : VBox() {

    var scrollPane: ScrollPane? = null

    private var last: Node? = null

    init {
        textfield?.setOnKeyPressed {
            val showing = Showables.SHOWING
            if(showing == this) {
                browse(it, showing)
            }
        }
        Platform.runLater {
            if(Showables.SHOWING == this) {
                children.forEach {node ->
                    node.setOnMouseMoved {
                        children.forEach {
                            it.setBVHover(false)
                        }
                        node.setBVHover()
                    }
                    node.setOnMouseExited {
                        node.setBVHover(false)
                    }
                }
            }
        }
        Chorus.getInstance().root.scene.setOnKeyReleased {
            val showing = Showables.SHOWING
            if(showing == this) {
                if(textfield == null) {
                    browse(it, showing)
                }
                if(it.code == KeyCode.ENTER) {
                    val node: Node = if(children.size == 1) {
                        children[0]
                    } else {
                        children.filtered {it.isBVHover()}.firstOrNull() ?: return@setOnKeyReleased
                    }
                    if(node is Button) {
                        node.fire()
                    } else if(node is Actionable) {
                        node.action.run()
                    }
                }
            }
        }
    }

    private fun browse(event: KeyEvent, showing: VBox) {
        if(event.code == KeyCode.UP || event.code == KeyCode.DOWN) {
            val selected: Node? = showing.children.filtered {it.isBVHover()}.firstOrNull() ?: last
            var index: Int
            if(selected == null) {
                index = if(event.code == KeyCode.UP) lastIndex else 0
                setBVHover(index)
            } else {
                index = children.indexOf(selected)
                setBVHover(index, false)
                index = if(event.code == KeyCode.UP)
                    if(index == 0) lastIndex else index - 1 else if(index == lastIndex) 0 else index + 1
                children.forEach {it.setBVHover(false)}
                setBVHover(index)
            }
            if(scrollPane != null) {
                scrollPane!!.vvalue = index / (children.size.toDouble() - 1)
            }
        }
    }

    private val lastIndex
        get() = children.size - 1

    private fun Node.isBVHover(): Boolean = pseudoClassStates.map {it.pseudoClassName}.contains(HOVER_STYLE_CLASS)

    private fun Node.setBVHover(state: Boolean = true) {
        pseudoClassStateChanged(PseudoClass.getPseudoClass(HOVER_STYLE_CLASS), state)
        last = this
    }

    private fun setBVHover(i: Int, state: Boolean = true) {
        if(i >= 0) children[i].setBVHover(state)
    }
}