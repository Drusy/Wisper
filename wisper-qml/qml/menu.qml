import QtQuick 2.0
import QtQuick.Particles 2.0
import "../js/android.js" as Android

Rectangle {
    property int headerHeight: 20 * Android.dp
    property int footerHeight: 44 * Android.dp
    property int fontPixelSize: 14 * Android.dp
    property int blockSize: 32 * Android.dp
    property int toolButtonHeight: 32 * Android.dp
    property int menuButtonSpacing: 0 * Android.dp
    property int emitterScale: 125 * Android.dp

    color: "black"

    ParticleSystem {
        id: wisperParticle
        width: parent.width
        height: parent.height / 1.5
        anchors.horizontalCenter: parent.horizontalCenter

        ImageParticle {
            source: "qrc:///particleresources/glowdot.png"
            anchors.fill: parent
            color: "#336666CC"
            colorVariation: 0.0
        }

        Emitter {
            anchors.fill: parent
            emitRate: 4000
            lifeSpan: 500
            size: 10
            shape: MaskShape {
                source: "qrc:///images/blue-wisper-shape.png"
            }
        }
    }

    ParticleSystem {
        id: wisperTextParticle
        width: parent.width
        height: parent.height / 4
        anchors.top: wisperParticle.bottom

        ImageParticle {
            source: "qrc:///particleresources/glowdot.png"
            anchors.fill: parent
            color: "#336666CC"
            colorVariation: 0.0
        }

        Emitter {
            anchors.fill: parent
            emitRate: 4000
            lifeSpan: 500
            size: 10
            shape: MaskShape {
                source: "qrc:///images/text-wisper-shape.png"
            }
        }
    }

    Image {
        id: bottomBar
        width: parent.width
        height: footerHeight
        source: "qrc:///images/bottom-bar.png"
        y: parent.height - footerHeight;
        z: 2
        Button {
            id: quitButton
            height: toolButtonHeight
            imgSrc: "qrc:///images/button-quit.png"
            onClicked: { Qt.quit(); }
            anchors { left: parent.left; verticalCenter: parent.verticalCenter; leftMargin: 11 }
        }
        Button {
            id: menuButton
            height: toolButtonHeight
            imgSrc: "qrc:///images/button-menu.png"
            //onClicked:
            anchors { left: quitButton.right; verticalCenter: parent.verticalCenter; leftMargin: 0 }
        }
        Button {
            id: againButton
            height: toolButtonHeight
            imgSrc: "qrc:///images/button-game-new.png"
            //onClicked:
            anchors { right: parent.right; verticalCenter: parent.verticalCenter; rightMargin: 11 }
        }
    }
}
