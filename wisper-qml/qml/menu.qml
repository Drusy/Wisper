/****************************************************************************
**
** Copyright (C) 2013 Digia Plc and/or its subsidiary(-ies).
** Contact: http://www.qt-project.org/legal
**
** This file is part of the examples of the Qt Toolkit.
**
** $QT_BEGIN_LICENSE:BSD$
** You may use this file under the terms of the BSD license as follows:
**
** "Redistribution and use in source and binary forms, with or without
** modification, are permitted provided that the following conditions are
** met:
**   * Redistributions of source code must retain the above copyright
**     notice, this list of conditions and the following disclaimer.
**   * Redistributions in binary form must reproduce the above copyright
**     notice, this list of conditions and the following disclaimer in
**     the documentation and/or other materials provided with the
**     distribution.
**   * Neither the name of Digia Plc and its Subsidiary(-ies) nor the names
**     of its contributors may be used to endorse or promote products derived
**     from this software without specific prior written permission.
**
**
** THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
** "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
** LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
** A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
** OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
** SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
** LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
** DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
** THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
** (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
** OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE."
**
** $QT_END_LICENSE$
**
****************************************************************************/

import QtQuick 2.0
import QtQuick.Particles 2.0

Rectangle {
    property int screenHeight: 480
    property int screenWidth: 320
    property int headerHeight: 20
    property int footerHeight: 44
    property int fontPixelSize: 14
    property int blockSize: 32
    property int toolButtonHeight: 32
    property int menuButtonSpacing: 0

    color: "black"
    width: 400
    height: 400

    ParticleSystem {
        id: particleSystem
        width: 350
        height: 350
        anchors.horizontalCenter: parent.horizontalCenter

        ImageParticle {
            source: "qrc:///particleresources/glowdot.png"
            anchors.fill: parent
            color: "#336666CC"
            colorVariation: 0.0
        }

        Emitter {
            anchors.fill: parent
            emitRate: 2000
            lifeSpan: 400
            size: 10
            shape: MaskShape {
                source: "qrc:///images/blue-wisper-shape.png"
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
