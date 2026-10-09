SUMMARY = "Set XDG_RUNTIME_DIR for login shells (needed by Wayland compositors and clients)"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://xdg-runtime-dir.sh"
S = "${UNPACKDIR}"

inherit allarch

do_install() {
    install -Dm 0644 ${S}/xdg-runtime-dir.sh ${D}${sysconfdir}/profile.d/xdg-runtime-dir.sh
}
