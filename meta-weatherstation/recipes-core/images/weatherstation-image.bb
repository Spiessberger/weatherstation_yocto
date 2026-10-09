SUMMARY = "Weatherstation image"
LICENSE = "MIT"

inherit core-image

IMAGE_FEATURES += " \
    ssh-server-openssh \
    empty-root-password \
    allow-empty-password \
    allow-root-login \
"

IMAGE_INSTALL += " \
    networkmanager \
    networkmanager-nmcli \
    networkmanager-wifi \
    qtbase \
    qtdeclarative \
    qtdeclarative-tools \
    qt-eglfs-config \
    ttf-roboto \
    cage \
    wlr-randr \
    qtwayland \
    xdg-runtime-dir \
    cage-kiosk \
"

# SDK (bitbake -c populate_sdk): target sysroot gets -dev of the Qt modules above, host gets Qt tools.
# Not populate_sdk_qt6: that pulls every Qt module into the SDK.
inherit populate_sdk_qt6_base
TOOLCHAIN_HOST_TASK:append = " nativesdk-packagegroup-qt6-toolchain-host-essentials nativesdk-ninja"
