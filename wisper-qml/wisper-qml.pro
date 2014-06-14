# Add more folders to ship with the application, here
qml.source = qml
qml.target =
js.source = js
js.target =
DEPLOYMENTFOLDERS = qml js

# Additional import path used to resolve QML modules in Creator's code model
QML_IMPORT_PATH =

# The .cpp file which was generated for your project. Feel free to hack it.
SOURCES += main.cpp

# Installation path
# target.path =

android {
    QT += androidextras
}

# Please do not modify the following two lines. Required for deployment.
include(qtquick2applicationviewer/qtquick2applicationviewer.pri)
qtcAddDeployment()

OTHER_FILES += \
    js/android.js

RESOURCES += \
    resources.qrc
