SUMMARY = "QtQuick demo for checking rendering performance and touch responsiveness"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://main.qml file://qtquick-demo file://qtquick-demo.service"
S = "${UNPACKDIR}"

inherit allarch systemd

do_install() {
    install -Dm 0644 ${S}/main.qml ${D}${datadir}/qtquick-demo/main.qml
    install -Dm 0755 ${S}/qtquick-demo ${D}${bindir}/qtquick-demo
    install -Dm 0644 ${S}/qtquick-demo.service ${D}${sysconfdir}/systemd/system/qtquick-demo.service
}

SYSTEMD_SERVICE:${PN} = "qtquick-demo.service"
SYSTEMD_AUTO_ENABLE = "enable"

RDEPENDS:${PN} = "cage-kiosk qt-eglfs-config qtdeclarative-qmlplugins qtdeclarative-tools ttf-roboto"
