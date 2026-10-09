SUMMARY = "eglfs KMS configuration for Qt applications"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://qt-eglfs-kms.json file://qt-eglfs.sh"
S = "${UNPACKDIR}"

inherit allarch

do_install() {
    install -Dm 0644 ${S}/qt-eglfs-kms.json ${D}${sysconfdir}/qt-eglfs-kms.json
    install -Dm 0644 ${S}/qt-eglfs.sh ${D}${sysconfdir}/profile.d/qt-eglfs.sh
}
