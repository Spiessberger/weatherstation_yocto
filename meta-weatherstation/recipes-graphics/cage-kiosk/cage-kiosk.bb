SUMMARY = "Run cage at boot on the DSI panel, rotated 180 degrees, for kiosk app services"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://cage.service \
    file://wayland.conf \
    file://99-touchscreen-rotation.rules \
    file://99-ignore-hdmi-cec-input.rules \
"
S = "${UNPACKDIR}"

inherit allarch systemd

do_install() {
    install -Dm 0644 ${S}/cage.service ${D}${sysconfdir}/systemd/system/cage.service
    install -Dm 0644 ${S}/wayland.conf ${D}${sysconfdir}/systemd/system.conf.d/wayland.conf
    install -Dm 0644 ${S}/99-touchscreen-rotation.rules ${D}${sysconfdir}/udev/rules.d/99-touchscreen-rotation.rules
    install -Dm 0644 ${S}/99-ignore-hdmi-cec-input.rules ${D}${sysconfdir}/udev/rules.d/99-ignore-hdmi-cec-input.rules
}

SYSTEMD_SERVICE:${PN} = "cage.service"
SYSTEMD_AUTO_ENABLE = "enable"

RDEPENDS:${PN} = "cage wlr-randr"
