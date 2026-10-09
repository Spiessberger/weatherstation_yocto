import QtQuick

// Left: animated load (+/- to change) with touch dots and trails. Right: kinetic-scroll list.
// Top bar: fps and worst frame time over the last second, touch events per second.
Window {
    id: root
    width: 1280
    height: 800
    visible: true
    color: "#101418"

    property int load: 50

    component Btn: Rectangle {
        property alias label: t.text
        signal clicked
        width: 100
        height: 70
        radius: 10
        color: tap.pressed ? "#3d6fa5" : "#2b3a4a"
        Text { id: t; anchors.centerIn: parent; color: "white"; font.pixelSize: 40 }
        TapHandler { id: tap; onTapped: parent.clicked() }
    }

    FrameAnimation {
        id: frames
        running: true
        property int count: 0
        property real acc: 0
        property real worst: 0
        property real fps: 0
        property real worstMs: 0
        onTriggered: {
            count++
            acc += frameTime
            worst = Math.max(worst, frameTime)
            if (acc >= 1) {
                fps = count / acc
                worstMs = worst * 1000
                touch.rate = touch.events
                touch.events = 0
                count = 0; acc = 0; worst = 0
            }
        }
    }

    Rectangle {
        id: bar
        width: parent.width
        height: 90
        color: "#1c232b"

        Row {
            x: 24
            anchors.verticalCenter: parent.verticalCenter
            spacing: 40
            Text { text: frames.fps.toFixed(1) + " fps"; color: frames.fps < 55 ? "#ff7060" : "#70e090"; font.pixelSize: 36 }
            Text { text: "worst " + frames.worstMs.toFixed(1) + " ms"; color: "#d0d8e0"; font.pixelSize: 36 }
            Text { text: touch.rate + " touch ev/s"; color: "#d0d8e0"; font.pixelSize: 36 }
            Text { text: root.load + " items"; color: "#d0d8e0"; font.pixelSize: 36 }
        }
        Row {
            anchors { right: parent.right; rightMargin: 24; verticalCenter: parent.verticalCenter }
            spacing: 16
            Btn { label: "−"; onClicked: root.load = Math.max(0, root.load - 50) }
            Btn { label: "+"; onClicked: root.load = Math.min(5000, root.load + 50) }
        }
    }

    Item {
        id: canvas
        anchors { left: parent.left; top: bar.bottom; bottom: parent.bottom }
        width: parent.width * 0.6
        clip: true

        Repeater {
            model: root.load
            Rectangle {
                required property int index
                width: 80
                height: 80
                radius: 12
                x: Math.random() * (canvas.width - width)
                y: Math.random() * (canvas.height - height)
                color: Qt.hsla((index * 0.07) % 1, 0.7, 0.5, 0.5)
                RotationAnimation on rotation {
                    from: 0; to: 360; loops: Animation.Infinite
                    duration: 2000 + (index * 137) % 3000
                }
            }
        }

        Component {
            id: trailDot
            Rectangle {
                id: dot
                width: 16
                height: 16
                radius: 8
                color: "white"
                NumberAnimation on opacity { from: 0.8; to: 0; duration: 500; onFinished: dot.destroy() }
            }
        }

        MultiPointTouchArea {
            id: touch
            anchors.fill: parent
            property int events: 0
            property int rate: 0
            touchPoints: [
                TouchPoint {}, TouchPoint {}, TouchPoint {}, TouchPoint {}, TouchPoint {},
                TouchPoint {}, TouchPoint {}, TouchPoint {}, TouchPoint {}, TouchPoint {}
            ]
            // One dot per event: dot spacing shows the touch sample rate, the gap to the finger shows lag
            onTouchUpdated: (points) => {
                events++
                for (let i = 0; i < points.length; i++)
                    trailDot.createObject(canvas, { x: points[i].x - 8, y: points[i].y - 8 })
            }
        }

        Repeater {
            model: touch.touchPoints
            Rectangle {
                required property var modelData
                visible: modelData.pressed
                width: 110
                height: 110
                radius: 55
                x: modelData.x - 55
                y: modelData.y - 55
                color: "transparent"
                border { color: "#ffd060"; width: 4 }
            }
        }
    }

    ListView {
        anchors { left: canvas.right; right: parent.right; top: bar.bottom; bottom: parent.bottom }
        clip: true
        model: 1000
        delegate: Rectangle {
            required property int index
            width: ListView.view.width
            height: 80
            color: index % 2 ? "#18202a" : "#1f2935"
            Text { x: 24; anchors.verticalCenter: parent.verticalCenter; text: "Row " + index; color: "#d0d8e0"; font.pixelSize: 30 }
        }
    }
}
